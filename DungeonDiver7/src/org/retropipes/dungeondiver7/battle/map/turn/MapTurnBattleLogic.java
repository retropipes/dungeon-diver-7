/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.map.turn;

import java.io.IOException;

import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.StuffBag;
import org.retropipes.dungeondiver7.ai.AIContext;
import org.retropipes.dungeondiver7.ai.BattleAction;
import org.retropipes.dungeondiver7.ai.map.AutoMapAI;
import org.retropipes.dungeondiver7.battle.Battle;
import org.retropipes.dungeondiver7.battle.BattleResult;
import org.retropipes.dungeondiver7.battle.damage.DamageEngine;
import org.retropipes.dungeondiver7.battle.map.MapBattleAITask;
import org.retropipes.dungeondiver7.battle.map.MapBattleDefinitions;
import org.retropipes.dungeondiver7.battle.reward.BattleRewards;
import org.retropipes.dungeondiver7.battle.types.BattleType;
import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.creature.StatConstants;
import org.retropipes.dungeondiver7.creature.monster.MonsterFactory;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.dungeon.Dungeon;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractBattleCharacter;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.dungeon.objects.BattleCharacter;
import org.retropipes.dungeondiver7.dungeon.objects.Empty;
import org.retropipes.dungeondiver7.effect.Effect;
import org.retropipes.dungeondiver7.loader.music.MusicLoader;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.locale.Music;
import org.retropipes.dungeondiver7.prefs.Prefs;
import org.retropipes.dungeondiver7.spell.SpellCaster;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

public class MapTurnBattleLogic extends Battle {
    private static final int STEAL_ACTION_POINTS = 3;
    private static final int DRAIN_ACTION_POINTS = 3;
    // Fields
    private BattleType battleType;
    private MapBattleDefinitions bd;
    private DamageEngine pde;
    private DamageEngine ede;
    private final AutoMapAI auto;
    private int damage;
    private BattleResult result;
    private int activeIndex;
    private long battleExp;
    private boolean newRound;
    private int[] speedArray;
    private int lastSpeed;
    private boolean[] speedMarkArray;
    private boolean resultDoneAlready;
    private boolean lastAIActionResult;
    private final MapBattleAITask ait;
    private MapTurnBattleGUI battleGUI;
    private BattleCharacter enemy;

    // Constructors
    public MapTurnBattleLogic() {
	this.battleGUI = new MapTurnBattleGUI();
	this.auto = new AutoMapAI();
	this.ait = new MapBattleAITask(this);
	this.ait.start();
    }

    private boolean areTeamEnemiesAlive(final int teamID) {
	for (var x = 0; x < this.bd.getBattlers().length; x++) {
	    if (this.bd.getBattlers()[x] != null && this.bd.getBattlers()[x].getTeamID() != teamID) {
		final var res = this.bd.getBattlers()[x].getTemplate().isAlive();
		if (res) {
		    return true;
		}
	    }
	}
	return false;
    }

    private boolean areTeamEnemiesDeadOrGone(final int teamID) {
	var deadCount = 0;
	for (var x = 0; x < this.bd.getBattlers().length; x++) {
	    if (this.bd.getBattlers()[x] != null && this.bd.getBattlers()[x].getTeamID() != teamID) {
		final var res = this.bd.getBattlers()[x].getTemplate().isAlive() && this.bd.getBattlers()[x].isActive();
		if (res) {
		    return false;
		}
		if (!this.bd.getBattlers()[x].getTemplate().isAlive()) {
		    deadCount++;
		}
	    }
	}
	return deadCount > 0;
    }

    private boolean areTeamEnemiesGone(final int teamID) {
	var res = true;
	for (var x = 0; x < this.bd.getBattlers().length; x++) {
	    if (this.bd.getBattlers()[x] != null && this.bd.getBattlers()[x].getTeamID() != teamID
		    && this.bd.getBattlers()[x].getTemplate().isAlive()) {
		res = res && !this.bd.getBattlers()[x].isActive();
		if (!res) {
		    return false;
		}
	    }
	}
	return true;
    }

    @Override
    public void battleDone() {
	// Leave Battle
	this.hideBattle();
	DungeonDiver7.getStuffBag().setMode(StuffBag.STATUS_GAME);
	// Return to whence we came
	DungeonDiver7.getStuffBag().getGameLogic().showOutput();
	DungeonDiver7.getStuffBag().getGameLogic().redrawDungeon();
    }

    @Override
    public boolean castSpell() {
	// Check Spell Counter
	if (this.getActiveSpellCounter() <= 0) {
	    // Deny cast - out of actions
	    if (!this.bd.getActiveCharacter().getTemplate().hasAI()) {
		this.setStatusMessage("Out of actions!");
	    }
	    return false;
	}
	if (!this.bd.getActiveCharacter().getTemplate().hasAI()) {
	    // Active character has no AI, or AI is turned off
	    final var success = SpellCaster.selectAndCastSpell(this.bd.getActiveCharacter().getTemplate());
	    if (success) {
		SoundLoader.playSound(Sounds.PARTY_SPELL);
		this.decrementActiveSpellCounter();
	    }
	    final var currResult = this.getResult();
	    if (currResult != BattleResult.IN_PROGRESS) {
		// Battle Done
		this.result = currResult;
		this.doResult();
	    }
	    return success;
	}
	// Active character has AI, and AI is turned on
	final var sp = this.bd.getActiveCharacter().getTemplate().getAI().getSpellToCast();
	final var success = SpellCaster.castSpell(sp, this.bd.getActiveCharacter().getTemplate());
	if (success) {
	    SoundLoader.playSound(Sounds.ENEMY_SPELL);
	    this.decrementActiveSpellCounter();
	}
	final var currResult = this.getResult();
	if (currResult != BattleResult.IN_PROGRESS) {
	    // Battle Done
	    this.result = currResult;
	    this.doResult();
	}
	return success;
    }

    private void clearStatusMessage() {
	this.battleGUI.clearStatusMessage();
    }

    private void computeDamage(final Creature theEnemy, final Creature acting, final DamageEngine activeDE) {
	// Compute Damage
	this.damage = 0;
	final var actual = activeDE.computeDamage(theEnemy, acting);
	// Hit or Missed
	this.damage = actual;
	if (activeDE.weaponFumble()) {
	    acting.doDamage(this.damage);
	} else if (this.damage < 0) {
	    acting.doDamage(-this.damage);
	} else {
	    theEnemy.doDamage(this.damage);
	}
	if (acting.getTeamID() == Creature.TEAM_PARTY) {
	    this.displayPartyRoundResults(theEnemy, acting, activeDE);
	} else {
	    this.displayEnemyRoundResults(theEnemy, acting, activeDE);
	}
    }

    private void decrementActiveActionCounterBy(final int amount) {
	this.bd.getActiveCharacter().modifyAP(amount);
    }

    private void decrementActiveAttackCounter() {
	this.bd.getActiveCharacter().modifyAttacks(1);
    }

    private void decrementActiveSpellCounter() {
	this.bd.getActiveCharacter().modifySpells(1);
    }

    @Override
    public void displayActiveEffects() {
	// Do nothing
    }

    @Override
    public void displayBattleStats() {
	// Do nothing
    }

    private void displayPartyRoundResults(final Creature theEnemy, final Creature active, final DamageEngine activeDE) {
	// Display round results
	final var hitSound = active.getItems().getWeaponHitSound(active);
	final var activeName = active.getName();
	final var enemyName = theEnemy.getName();
	var damageString = Integer.toString(this.damage);
	var displayDamageString = " ";
	if (this.damage == 0) {
	    if (activeDE.weaponMissed()) {
		displayDamageString = activeName + " tries to hit " + enemyName + ", but MISSES!";
	    } else if (activeDE.enemyDodged()) {
		displayDamageString = activeName + " tries to hit " + enemyName + ", but " + enemyName
			+ " AVOIDS the attack!";
	    } else {
		displayDamageString = activeName + " tries to hit " + enemyName + ", but the attack is BLOCKED!";
	    }
	    SoundLoader.playSound(Sounds.MISSED);
	} else if (this.damage < 0) {
	    damageString = Integer.toString(-this.damage);
	    var displayDamagePrefix = "";
	    if (activeDE.weaponCrit() && activeDE.weaponPierce()) {
		displayDamagePrefix = "PIERCING CRITICAL HIT! ";
		SoundLoader.playSound(Sounds.PARTY_COUNTER);
		SoundLoader.playSound(Sounds.CRITICAL);
	    } else if (activeDE.weaponCrit()) {
		displayDamagePrefix = "CRITICAL HIT! ";
		SoundLoader.playSound(Sounds.CRITICAL);
	    } else if (activeDE.weaponPierce()) {
		displayDamagePrefix = "PIERCING HIT! ";
		SoundLoader.playSound(Sounds.PARTY_COUNTER);
	    }
	    displayDamageString = displayDamagePrefix + activeName + " tries to hit " + enemyName + ", but " + enemyName
		    + " RIPOSTES for " + damageString + " damage!";
	    SoundLoader.playSound(Sounds.PARTY_COUNTER);
	} else {
	    var displayDamagePrefix = "";
	    if (activeDE.weaponFumble()) {
		SoundLoader.playSound(Sounds.FUMBLE);
		displayDamageString = "FUMBLE! " + activeName + " drops their weapon on themselves, doing "
			+ damageString + " damage!";
	    } else {
		if (activeDE.weaponCrit() && activeDE.weaponPierce()) {
		    displayDamagePrefix = "PIERCING CRITICAL HIT! ";
		    SoundLoader.playSound(Sounds.PARTY_COUNTER);
		    SoundLoader.playSound(Sounds.CRITICAL);
		} else if (activeDE.weaponCrit()) {
		    displayDamagePrefix = "CRITICAL HIT! ";
		    SoundLoader.playSound(Sounds.CRITICAL);
		} else if (activeDE.weaponPierce()) {
		    displayDamagePrefix = "PIERCING HIT! ";
		    SoundLoader.playSound(Sounds.PARTY_COUNTER);
		}
		displayDamageString = displayDamagePrefix + activeName + " hits " + enemyName + " for " + damageString
			+ " damage!";
		SoundLoader.playSound(hitSound);
	    }
	}
	this.setStatusMessage(displayDamageString);
    }

    private void displayEnemyRoundResults(final Creature theEnemy, final Creature active, final DamageEngine activeDE) {
	// Display round results
	final var hitSound = active.getItems().getWeaponHitSound(active);
	final var activeName = active.getName();
	final var enemyName = theEnemy.getName();
	var damageString = Integer.toString(this.damage);
	var displayDamageString = " ";
	if (this.damage == 0) {
	    if (activeDE.weaponMissed()) {
		displayDamageString = activeName + " tries to hit " + enemyName + ", but MISSES!";
	    } else if (activeDE.enemyDodged()) {
		displayDamageString = activeName + " tries to hit " + enemyName + ", but " + enemyName
			+ " AVOIDS the attack!";
	    } else {
		displayDamageString = activeName + " tries to hit " + enemyName + ", but the attack is BLOCKED!";
	    }
	    SoundLoader.playSound(Sounds.MISSED);
	} else if (this.damage < 0) {
	    damageString = Integer.toString(-this.damage);
	    var displayDamagePrefix = "";
	    if (activeDE.weaponCrit() && activeDE.weaponPierce()) {
		displayDamagePrefix = "PIERCING CRITICAL HIT! ";
		SoundLoader.playSound(Sounds.ENEMY_COUNTER);
		SoundLoader.playSound(Sounds.CRITICAL);
	    } else if (activeDE.weaponCrit()) {
		displayDamagePrefix = "CRITICAL HIT! ";
		SoundLoader.playSound(Sounds.CRITICAL);
	    } else if (activeDE.weaponPierce()) {
		displayDamagePrefix = "PIERCING HIT! ";
		SoundLoader.playSound(Sounds.ENEMY_COUNTER);
	    }
	    displayDamageString = displayDamagePrefix + activeName + " tries to hit " + enemyName + ", but " + enemyName
		    + " RIPOSTES for " + damageString + " damage!";
	    SoundLoader.playSound(Sounds.ENEMY_COUNTER);
	} else {
	    var displayDamagePrefix = "";
	    if (activeDE.weaponFumble()) {
		SoundLoader.playSound(Sounds.FUMBLE);
		displayDamageString = "FUMBLE! " + activeName + " drops their weapon on themselves, doing "
			+ damageString + " damage!";
	    } else {
		if (activeDE.weaponCrit() && activeDE.weaponPierce()) {
		    displayDamagePrefix = "PIERCING CRITICAL HIT! ";
		    SoundLoader.playSound(Sounds.ENEMY_COUNTER);
		    SoundLoader.playSound(Sounds.CRITICAL);
		} else if (activeDE.weaponCrit()) {
		    displayDamagePrefix = "CRITICAL HIT! ";
		    SoundLoader.playSound(Sounds.CRITICAL);
		} else if (activeDE.weaponPierce()) {
		    displayDamagePrefix = "PIERCING HIT! ";
		    SoundLoader.playSound(Sounds.ENEMY_COUNTER);
		}
		displayDamageString = displayDamagePrefix + activeName + " hits " + enemyName + " for " + damageString
			+ " damage!";
		SoundLoader.playSound(hitSound);
	    }
	}
	this.setStatusMessage(displayDamageString);
    }

    @Override
    public void doBattle() {
	this.battleType = BattleType.createBattle();
	if (MusicLoader.isMusicPlaying()) {
	    MusicLoader.stopMusic();
	}
	MusicLoader.playMusic(Music.BATTLE);
	this.doBattleInternal();
    }

    @Override
    public void doBattleByProxy() {
	final var m = MonsterFactory.getNewMonsterInstance();
	final var playerCharacter = PartyManager.getParty().getLeader();
	playerCharacter.offsetExperience(m.getExperience());
	playerCharacter.offsetGold(m.getGold());
	// Level Up Check
	if (playerCharacter.checkLevelUp()) {
	    playerCharacter.levelUp();
	    DungeonDiver7.getStuffBag().getGameLogic().keepNextMessage();
	    DungeonDiver7.getStuffBag().showMessage("You reached level " + playerCharacter.getLevel() + ".");
	}
    }

    private void doBattleInternal() {
	// Initialize Battle
	Dungeon bMap = null;
	try {
	    bMap = Dungeon.getTemporaryBattleCopy();
	} catch (final IOException e) {
	    DungeonDiver7.logError(e);
	}
	DungeonDiver7.getStuffBag().getGameLogic().hideOutput();
	DungeonDiver7.getStuffBag().setMode(StuffBag.STATUS_BATTLE);
	this.bd = new MapBattleDefinitions();
	this.bd.setBattleDungeon(bMap);
	this.pde = DamageEngine.getPlayerInstance();
	this.ede = DamageEngine.getEnemyInstance();
	this.resultDoneAlready = false;
	this.result = BattleResult.IN_PROGRESS;
	// Generate Friends
	final var friends = PartyManager.getParty().getBattleCharacters();
	// Generate Enemies
	this.enemy = this.battleType.getBattlers();
	this.enemy.getTemplate().healAndRegenerateFully();
	this.enemy.getTemplate().loadCreature();
	// Merge and Create AI Contexts
	for (var x = 0; x < 2; x++) {
	    if (x == 0) {
		this.bd.addBattler(friends);
	    } else {
		this.bd.addBattler(this.enemy);
	    }
	    if (this.bd.getBattlers()[x] != null) {
		// Create an AI Context
		this.bd.getBattlerAIContexts()[x] = new AIContext(this.bd.getBattlers()[x],
			this.bd.getBattleDungeon());
	    }
	}
	// Reset Inactive Indicators and Action Counters
	this.bd.resetBattlers();
	// Generate Speed Array
	this.generateSpeedArray();
	// Set Character Locations
	this.setCharacterLocations();
	// Set First Active
	this.newRound = this.setNextActive(true);
	// Clear status message
	this.clearStatusMessage();
	// Start Battle
	this.battleGUI.getViewManager().setViewingWindowCenterX(this.bd.getActiveCharacter().getY());
	this.battleGUI.getViewManager().setViewingWindowCenterY(this.bd.getActiveCharacter().getX());
	SoundLoader.playSound(Sounds.DRAW_SWORD);
	this.showBattle();
	this.updateStatsAndEffects();
	this.redrawBattle();
    }

    @Override
    public void doFinalBossBattle() {
	this.battleType = BattleType.createFinalBossBattle();
	if (MusicLoader.isMusicPlaying()) {
	    MusicLoader.stopMusic();
	}
	MusicLoader.playMusic(Music.BOSS);
	this.doBattleInternal();
    }

    @Override
    public boolean doPlayerActions(final BattleAction action) {
	switch (action) {
	case BattleAction.CAST_SPELL:
	    this.castSpell();
	    break;
	case BattleAction.DRAIN:
	    this.drain();
	    break;
	case BattleAction.STEAL:
	    this.steal();
	    break;
	default:
	    this.endTurn();
	    break;
	}
	return true;
    }

    @Override
    public void doResult() {
	this.stopWaitingForAI();
	if (!this.resultDoneAlready) {
	    // Handle Results
	    this.resultDoneAlready = true;
	    if (this.result == BattleResult.WON) {
		SoundLoader.playSound(Sounds.VICTORY);
		CommonDialogs.showTitledDialog("The party is victorious!", "Victory!");
	    } else if (this.result == BattleResult.PERFECT) {
		SoundLoader.playSound(Sounds.VICTORY);
		CommonDialogs.showTitledDialog("The party is victorious, and avoided damage!", "Perfect Victory!");
	    } else if (this.result == BattleResult.LOST) {
		CommonDialogs.showTitledDialog("The party has been defeated!", "Defeat!");
	    } else if (this.result == BattleResult.ANNIHILATED) {
		CommonDialogs.showTitledDialog("The party has been defeated without dealing any damage!",
			"Annihilated!");
	    } else if (this.result == BattleResult.DRAW) {
		CommonDialogs.showTitledDialog("The battle was a draw.", "Draw");
	    } else if (this.result == BattleResult.FLED) {
		CommonDialogs.showTitledDialog("The party fled!", "Party Fled");
	    } else if (this.result == BattleResult.ENEMY_FLED) {
		CommonDialogs.showTitledDialog("The enemy fled!", "Enemy Fled");
	    } else {
		CommonDialogs.showTitledDialog("The battle isn't over, but somehow the game thinks it is.", "Uh-Oh!");
	    }
	    // Rewards
	    final var exp = this.battleExp;
	    final var gold = this.getEnemy().getGold();
	    BattleRewards.doRewards(this.battleType, this.result, exp, gold);
	    // Strip effects
	    PartyManager.getParty().getLeader().stripAllEffects();
	    // Level Up Check
	    PartyManager.getParty().checkPartyLevelUp();
	    // Battle Done
	    this.battleDone();
	}
    }

    @Override
    public boolean drain() {
	// Check Action Counter
	if (this.getActiveActionCounter() <= 0) {
	    // Deny drain - out of actions
	    if (!this.bd.getActiveCharacter().getTemplate().hasAI()) {
		this.setStatusMessage("Out of actions!");
	    }
	    return false;
	}
	Creature activeEnemy = null;
	final AbstractBattleCharacter enemyBC = this.getEnemyBC();
	if (enemyBC != null) {
	    activeEnemy = enemyBC.getTemplate();
	}
	int drainChance;
	var drainAmount = 0;
	this.bd.getActiveCharacter().modifyAP(MapTurnBattleLogic.DRAIN_ACTION_POINTS);
	drainChance = StatConstants.CHANCE_DRAIN;
	if (activeEnemy == null) {
	    // Failed - nobody to drain from
	    this.setStatusMessage(
		    this.bd.getActiveCharacter().getName() + " tries to drain, but nobody is there to drain from!");
	    return false;
	}
	if (drainChance <= 0) {
	    // Failed
	    this.setStatusMessage(this.bd.getActiveCharacter().getName() + " tries to drain, but fails!");
	    return false;
	}
	if (drainChance >= 100) {
	    // Succeeded, unless target has 0 MP
	    final var drained = new RandomRange(0, activeEnemy.getCurrentMP());
	    drainAmount = drained.generate();
	    if (drainAmount == 0) {
		this.setStatusMessage(
			this.bd.getActiveCharacter().getName() + " tries to drain, but no MP is left to drain!");
		return false;
	    }
	    activeEnemy.offsetCurrentMP(-drainAmount);
	    this.bd.getActiveCharacter().getTemplate().offsetCurrentMP(drainAmount);
	    this.setStatusMessage(this.bd.getActiveCharacter().getName() + " tries to drain, and successfully drains "
		    + drainAmount + " MP!");
	    return true;
	}
	final var chance = new RandomRange(0, 100);
	final var randomChance = chance.generate();
	if (randomChance > drainChance) {
	    // Failed
	    this.setStatusMessage(this.bd.getActiveCharacter().getName() + " tries to drain, but fails!");
	    return false;
	}
	// Succeeded
	final var drained = new RandomRange(0, activeEnemy.getCurrentMP());
	drainAmount = drained.generate();
	if (drainAmount == 0) {
	    this.setStatusMessage(
		    this.bd.getActiveCharacter().getName() + " tries to drain, but no MP is left to drain!");
	    return false;
	}
	activeEnemy.offsetCurrentMP(-drainAmount);
	this.bd.getActiveCharacter().getTemplate().offsetCurrentMP(drainAmount);
	this.setStatusMessage(this.bd.getActiveCharacter().getName() + " tries to drain, and successfully drains "
		+ drainAmount + " MP!");
	return true;
    }

    @Override
    public void endTurn() {
	this.newRound = this.setNextActive(this.newRound);
	if (this.newRound) {
	    this.setStatusMessage("New Round");
	    this.newRound = this.setNextActive(this.newRound);
	    // Check result
	    this.result = this.getResult();
	    if (this.result != BattleResult.IN_PROGRESS) {
		this.doResult();
		return;
	    }
	}
	this.updateStatsAndEffects();
	this.battleGUI.getViewManager().setViewingWindowCenterX(this.bd.getActiveCharacter().getY());
	this.battleGUI.getViewManager().setViewingWindowCenterY(this.bd.getActiveCharacter().getX());
	this.redrawBattle();
    }

    private void executeAutoAI(final BattleCharacter acting) {
	final var index = this.bd.findBattler(acting.getName());
	final var action = this.auto.getNextAction(this.bd.getBattlerAIContexts()[index]);
	switch (action) {
	case BattleAction.MOVE:
	    final var x = this.auto.getMoveX();
	    final var y = this.auto.getMoveY();
	    final var activeTID = this.bd.getActiveCharacter().getTeamID();
	    final var theEnemy = activeTID == Creature.TEAM_PARTY ? this.enemy
		    : this.bd.getBattlers()[this.bd.findFirstBattlerOnTeam(Creature.TEAM_PARTY)];
	    final var activeDE = activeTID == Creature.TEAM_PARTY ? this.ede : this.pde;
	    this.updatePositionInternal(x, y, false, acting, theEnemy, activeDE);
	    break;
	default:
	    break;
	}
    }

    @Override
    public void executeNextAIAction() {
	if (this.bd != null && this.bd.getActiveCharacter() != null
		&& this.bd.getActiveCharacter().getTemplate() != null
		&& this.bd.getActiveCharacter().getTemplate().getAI() != null) {
	    final var active = this.bd.getActiveCharacter();
	    if (active.getTemplate().isAlive()) {
		final var action = active.getTemplate().getAI()
			.getNextAction(this.bd.getBattlerAIContexts()[this.activeIndex]);
		switch (action) {
		case BattleAction.MOVE:
		    final var x = active.getTemplate().getAI().getMoveX();
		    final var y = active.getTemplate().getAI().getMoveY();
		    this.lastAIActionResult = this.updatePosition(x, y);
		    active.getTemplate().getAI().setLastResult(this.lastAIActionResult);
		    break;
		case BattleAction.CAST_SPELL:
		    this.lastAIActionResult = this.castSpell();
		    active.getTemplate().getAI().setLastResult(this.lastAIActionResult);
		    break;
		case BattleAction.DRAIN:
		    this.lastAIActionResult = this.drain();
		    active.getTemplate().getAI().setLastResult(this.lastAIActionResult);
		    break;
		case BattleAction.STEAL:
		    this.lastAIActionResult = this.steal();
		    active.getTemplate().getAI().setLastResult(this.lastAIActionResult);
		    break;
		default:
		    this.lastAIActionResult = true;
		    this.endTurn();
		    this.stopWaitingForAI();
		    this.ait.aiWait();
		    break;
		}
	    }
	}
    }

    private int findNextSmallestSpeed(final int max) {
	var res = -1;
	var found = 0;
	for (var x = 0; x < this.speedArray.length; x++) {
	    if (!this.speedMarkArray[x] && this.speedArray[x] <= max && this.speedArray[x] > found) {
		res = x;
		found = this.speedArray[x];
	    }
	}
	if (res != -1) {
	    this.speedMarkArray[res] = true;
	}
	return res;
    }

    private void generateSpeedArray() {
	this.speedArray = new int[this.bd.getBattlers().length];
	this.speedMarkArray = new boolean[this.speedArray.length];
	this.resetSpeedArray();
    }

    private int getActiveActionCounter() {
	return this.bd.getActiveCharacter().getActionsLeft();
    }

    private int getActiveAttackCounter() {
	return this.bd.getActiveCharacter().getAttacksLeft();
    }

    private int getActiveSpellCounter() {
	return this.bd.getActiveCharacter().getSpellsLeft();
    }

    @Override
    public Creature getEnemy() {
	return this.enemy.getTemplate();
    }

    private BattleCharacter getEnemyBC() {
	final var px = this.bd.getActiveCharacter().getX();
	final var py = this.bd.getActiveCharacter().getY();
	final var m = this.bd.getBattleDungeon();
	DungeonObject next = null;
	for (var x = -1; x <= 1; x++) {
	    for (var y = -1; y <= 1; y++) {
		if (x == 0 && y == 0) {
		    continue;
		}
		try {
		    next = m.getCell(px + x, py + y, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
		if (next != null && next.isSolidInBattle() && next instanceof BattleCharacter) {
		    return (BattleCharacter) next;
		}
	    }
	}
	return null;
    }

    @Override
    public boolean getLastAIActionResult() {
	return this.lastAIActionResult;
    }

    @Override
    public BattleResult getResult() {
	BattleResult currResult;
	if (this.result != BattleResult.IN_PROGRESS) {
	    return this.result;
	}
	if (this.areTeamEnemiesAlive(Creature.TEAM_PARTY) && !this.isTeamAlive(Creature.TEAM_PARTY)) {
	    currResult = BattleResult.LOST;
	} else if (!this.areTeamEnemiesAlive(Creature.TEAM_PARTY) && this.isTeamAlive(Creature.TEAM_PARTY)) {
	    currResult = BattleResult.WON;
	} else if (!this.areTeamEnemiesAlive(Creature.TEAM_PARTY) && !this.isTeamAlive(Creature.TEAM_PARTY)) {
	    currResult = BattleResult.DRAW;
	} else if (this.isTeamAlive(Creature.TEAM_PARTY) && !this.isTeamGone(Creature.TEAM_PARTY)
		&& this.areTeamEnemiesDeadOrGone(Creature.TEAM_PARTY)) {
	    currResult = BattleResult.WON;
	} else if (!this.isTeamAlive(Creature.TEAM_PARTY) && !this.isTeamGone(Creature.TEAM_PARTY)
		&& !this.areTeamEnemiesDeadOrGone(Creature.TEAM_PARTY)) {
	    currResult = BattleResult.LOST;
	} else if (this.areTeamEnemiesGone(Creature.TEAM_PARTY)) {
	    currResult = BattleResult.ENEMY_FLED;
	} else if (this.isTeamGone(Creature.TEAM_PARTY)) {
	    currResult = BattleResult.FLED;
	} else {
	    currResult = BattleResult.IN_PROGRESS;
	}
	return currResult;
    }

    private void handleDeath(final BattleCharacter activeBC) {
	// Something has died
	SoundLoader.playSound(Sounds.DEATH);
	final var active = activeBC.getTemplate();
	// Set dead character to inactive
	activeBC.deactivate();
	// Remove effects from dead character
	active.stripAllEffects();
	// Remove character from battle
	this.bd.getBattleDungeon().setCell(new Empty(), activeBC.getX(), activeBC.getY(), 0,
		DungeonConstants.LAYER_LOWER_OBJECTS);
	if (this.bd.getActiveCharacter().getName().equals(activeBC.getName())) {
	    // Active character died, end turn
	    this.endTurn();
	}
    }

    private void hideBattle() {
	this.battleGUI.hideBattle();
    }

    private boolean isTeamAlive(final int teamID) {
	for (var x = 0; x < this.bd.getBattlers().length; x++) {
	    if (this.bd.getBattlers()[x] != null && this.bd.getBattlers()[x].getTeamID() == teamID) {
		final var res = this.bd.getBattlers()[x].getTemplate().isAlive();
		if (res) {
		    return true;
		}
	    }
	}
	return false;
    }

    private boolean isTeamGone(final int teamID) {
	var res = true;
	for (var x = 0; x < this.bd.getBattlers().length; x++) {
	    if (this.bd.getBattlers()[x] != null && this.bd.getBattlers()[x].getTeamID() == teamID
		    && this.bd.getBattlers()[x].getTemplate().isAlive()) {
		res = res && !this.bd.getBattlers()[x].isActive();
		if (!res) {
		    return false;
		}
	    }
	}
	return true;
    }

    @Override
    public boolean isWaitingForAI() {
	return !this.battleGUI.areEventHandlersOn();
    }

    @Override
    public void maintainEffects(final boolean player) {
	for (var x = 0; x < this.bd.getBattlers().length; x++) {
	    // Maintain Effects
	    final var activeBC = this.bd.getBattlers()[x];
	    if (activeBC != null && activeBC.isActive()) {
		final var active = activeBC.getTemplate();
		// Use Effects
		active.useEffects();
		// Display all effect messages
		final var effectMessages = activeBC.getTemplate().getAllCurrentEffectMessages();
		final var individualEffectMessages = effectMessages.split("\n");
		for (final String message : individualEffectMessages) {
		    if (!message.equals(Effect.getNullMessage())) {
			this.setStatusMessage(message);
			try {
			    Thread.sleep(Prefs.getBattleSpeed());
			} catch (final InterruptedException ie) {
			    // Ignore
			}
		    }
		}
		// Handle low health for party members
		if (active.isAlive() && active.getTeamID() == Creature.TEAM_PARTY
			&& active.getCurrentHP() <= active.getMaximumHP() * 3 / 10) {
		    SoundLoader.playSound(Sounds.LOW_HEALTH);
		}
		// Cull Inactive Effects
		active.cullInactiveEffects();
		// Handle death caused by effects
		if (!active.isAlive()) {
		    if (activeBC.getTeamID() != Creature.TEAM_PARTY) {
			// Update victory spoils
			this.battleExp = activeBC.getTemplate().getExperience();
		    }
		    this.handleDeath(activeBC);
		}
	    }
	}
    }

    private void performNewRoundActions() {
	for (var x = 0; x < this.bd.getBattlers().length; x++) {
	    if (this.bd.getBattlers()[x] != null) {
		// Perform New Round Actions
		if (this.bd.getBattlerAIContexts()[x] != null
			&& this.bd.getBattlerAIContexts()[x].getCharacter().getTemplate().hasAI()
			&& this.bd.getBattlers()[x].isActive() && this.bd.getBattlers()[x].getTemplate().isAlive()) {
		    this.bd.getBattlerAIContexts()[x].getCharacter().getTemplate().getAI().newRoundHook();
		}
	    }
	}
    }

    private void redrawBattle() {
	this.battleGUI.redrawBattle(this.bd);
    }

    @Override
    public void resetGUI() {
	this.battleGUI = new MapTurnBattleGUI();
    }

    private void resetSpeedArray() {
	for (var x = 0; x < this.speedArray.length; x++) {
	    if (this.bd.getBattlers()[x] != null && this.bd.getBattlers()[x].getTemplate().isAlive()) {
		this.speedArray[x] = (int) this.bd.getBattlers()[x].getTemplate()
			.getEffectedStat(StatConstants.STAT_AGILITY);
	    } else {
		this.speedArray[x] = Integer.MIN_VALUE;
	    }
	}
	for (var x = 0; x < this.speedMarkArray.length; x++) {
	    if (this.speedArray[x] != Integer.MIN_VALUE) {
		this.speedMarkArray[x] = false;
	    } else {
		this.speedMarkArray[x] = true;
	    }
	}
    }

    private void setCharacterLocations() {
	final var randX = new RandomRange(0, this.bd.getBattleDungeon().getRows() - 1);
	final var randY = new RandomRange(0, this.bd.getBattleDungeon().getColumns() - 1);
	int rx, ry;
	// Set Character Locations
	for (var x = 0; x < this.bd.getBattlers().length; x++) {
	    if (this.bd.getBattlers()[x] != null && this.bd.getBattlers()[x].isActive()
		    && this.bd.getBattlers()[x].getTemplate().getX() == -1
		    && this.bd.getBattlers()[x].getTemplate().getY() == -1) {
		rx = randX.generate();
		ry = randY.generate();
		var obj = this.bd.getBattleDungeon().getCell(rx, ry, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
		while (obj.isSolidInBattle()) {
		    rx = randX.generate();
		    ry = randY.generate();
		    obj = this.bd.getBattleDungeon().getCell(rx, ry, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
		}
		this.bd.getBattlers()[x].setX(rx);
		this.bd.getBattlers()[x].setY(ry);
		this.bd.getBattleDungeon().setCell(this.bd.getBattlers()[x], rx, ry, 0,
			DungeonConstants.LAYER_LOWER_OBJECTS);
	    }
	}
    }

    private boolean setNextActive(final boolean isNewRound) {
	var res = 0;
	if (isNewRound) {
	    res = this.findNextSmallestSpeed(Integer.MAX_VALUE);
	} else {
	    res = this.findNextSmallestSpeed(this.lastSpeed);
	}
	if (res == -1) {
	    // Reset Speed Array
	    this.resetSpeedArray();
	    // Reset Action Counters
	    this.bd.roundResetBattlers();
	    // Maintain effects
	    this.maintainEffects(true);
	    this.updateStatsAndEffects();
	    // Perform new round actions
	    this.performNewRoundActions();
	    // Play new round sound
	    SoundLoader.playSound(Sounds.NEXT_ROUND);
	    // Nobody to act next, set new round flag
	    return true;
	}
	this.lastSpeed = this.speedArray[res];
	this.activeIndex = res;
	this.bd.setActiveCharacter(this.bd.getBattlers()[this.activeIndex]);
	// Check
	if (!this.bd.getActiveCharacter().isActive()) {
	    // Inactive, pick new active character
	    return this.setNextActive(isNewRound);
	}
	// AI Check
	if (this.bd.getActiveCharacter().getTemplate().hasAI()) {
	    // Run AI
	    this.waitForAI();
	    this.ait.aiRun();
	} else {
	    // No AI
	    SoundLoader.playSound(Sounds.PLAYER_UP);
	}
	return false;
    }

    @Override
    public void setResult(final BattleResult resultCode) {
	// Do nothing
    }

    @Override
    public void setStatusMessage(final String msg) {
	this.battleGUI.setStatusMessage(msg);
    }

    private void showBattle() {
	this.battleGUI.showBattle();
    }

    @Override
    public boolean steal() {
	// Check Action Counter
	if (this.getActiveActionCounter() <= 0) {
	    // Deny steal - out of actions
	    if (!this.bd.getActiveCharacter().getTemplate().hasAI()) {
		this.setStatusMessage("Out of actions!");
	    }
	    return false;
	}
	Creature activeEnemy = null;
	final AbstractBattleCharacter enemyBC = this.getEnemyBC();
	if (enemyBC != null) {
	    activeEnemy = enemyBC.getTemplate();
	}
	int stealChance;
	var stealAmount = 0;
	this.bd.getActiveCharacter().modifyAP(MapTurnBattleLogic.STEAL_ACTION_POINTS);
	stealChance = StatConstants.CHANCE_STEAL;
	if (activeEnemy == null) {
	    // Failed - nobody to steal from
	    this.setStatusMessage(
		    this.bd.getActiveCharacter().getName() + " tries to steal, but nobody is there to steal from!");
	    return false;
	}
	if (stealChance <= 0) {
	    // Failed
	    this.setStatusMessage(this.bd.getActiveCharacter().getName() + " tries to steal, but fails!");
	    return false;
	}
	if (stealChance >= 100) {
	    // Succeeded, unless target has 0 Gold
	    final var stole = new RandomRange(0, activeEnemy.getGold());
	    stealAmount = stole.generate();
	    if (stealAmount == 0) {
		this.setStatusMessage(
			this.bd.getActiveCharacter().getName() + " tries to steal, but no Gold is left to steal!");
		return false;
	    }
	    this.bd.getActiveCharacter().getTemplate().offsetGold(stealAmount);
	    this.setStatusMessage(this.bd.getActiveCharacter().getName() + " tries to steal, and successfully steals "
		    + stealAmount + " gold!");
	    return true;
	}
	final var chance = new RandomRange(0, 100);
	final var randomChance = chance.generate();
	if (randomChance > stealChance) {
	    // Failed
	    this.setStatusMessage(this.bd.getActiveCharacter().getName() + " tries to steal, but fails!");
	    return false;
	}
	// Succeeded, unless target has 0 Gold
	final var stole = new RandomRange(0, activeEnemy.getGold());
	stealAmount = stole.generate();
	if (stealAmount == 0) {
	    this.setStatusMessage(
		    this.bd.getActiveCharacter().getName() + " tries to steal, but no Gold is left to steal!");
	    return false;
	}
	this.bd.getActiveCharacter().getTemplate().offsetGold(stealAmount);
	this.setStatusMessage(this.bd.getActiveCharacter().getName() + " tries to steal, and successfully steals "
		+ stealAmount + " gold!");
	return true;
    }

    private void stopWaitingForAI() {
	this.battleGUI.turnEventHandlersOn();
    }

    private void updateAllAIContexts() {
	for (var x = 0; x < this.bd.getBattlers().length; x++) {
	    // Update all AI Contexts
	    if (this.bd.getBattlers()[x] != null && this.bd.getBattlerAIContexts()[x] != null) {
		this.bd.getBattlerAIContexts()[x].updateContext(this.bd.getBattleDungeon());
	    }
	}
    }

    @Override
    public boolean updatePosition(final int x, final int y) {
	final var activeTID = this.bd.getActiveCharacter().getTeamID();
	var theEnemy = activeTID == Creature.TEAM_PARTY ? this.enemy
		: this.bd.getBattlers()[this.bd.findFirstBattlerOnTeam(Creature.TEAM_PARTY)];
	final var activeDE = activeTID == Creature.TEAM_PARTY ? this.ede : this.pde;
	if (x == 0 && y == 0) {
	    theEnemy = this.bd.getActiveCharacter();
	}
	return this.updatePositionInternal(x, y, true, this.bd.getActiveCharacter(), theEnemy, activeDE);
    }

    private boolean updatePositionInternal(final int x, final int y, final boolean useAP,
	    final BattleCharacter activeBC, final BattleCharacter theEnemy, final DamageEngine activeDE) {
	final var active = activeBC.getTemplate();
	this.updateAllAIContexts();
	var px = activeBC.getX();
	var py = activeBC.getY();
	final var m = this.bd.getBattleDungeon();
	DungeonObject next = null;
	DungeonObject nextGround = null;
	DungeonObject currGround = null;
	activeBC.saveLocation();
	this.battleGUI.getViewManager().saveViewingWindow();
	try {
	    next = m.getCell(px + x, py + y, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    nextGround = m.getCell(px + x, py + y, 0, DungeonConstants.LAYER_LOWER_GROUND);
	    currGround = m.getCell(px, py, 0, DungeonConstants.LAYER_LOWER_GROUND);
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Ignore
	}
	if (next == null || nextGround == null || currGround == null) {
	    // Confirm Flee
	    if (!active.hasAI()) {
		SoundLoader.playSound(Sounds.QUESTION);
		final var confirm = CommonDialogs.showConfirmDialog("Embrace Cowardice?", "Battle");
		if (confirm != CommonDialogs.YES_OPTION) {
		    this.battleGUI.getViewManager().restoreViewingWindow();
		    activeBC.restoreLocation();
		    return false;
		}
	    }
	    // Flee
	    SoundLoader.playSound(Sounds.RUN);
	    this.battleGUI.getViewManager().restoreViewingWindow();
	    activeBC.restoreLocation();
	    // Set fled character to inactive
	    activeBC.deactivate();
	    // Remove character from battle
	    m.setCell(new Empty(), activeBC.getX(), activeBC.getY(), 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    // End Turn
	    this.endTurn();
	    this.updateStatsAndEffects();
	    final var currResult = this.getResult();
	    if (currResult != BattleResult.IN_PROGRESS) {
		// Battle Done
		this.result = currResult;
		this.doResult();
	    }
	    this.battleGUI.getViewManager().setViewingWindowCenterX(py);
	    this.battleGUI.getViewManager().setViewingWindowCenterY(px);
	    this.redrawBattle();
	    return true;
	}
	if (!next.isSolidInBattle()) {
	    if ((!useAP || this.getActiveActionCounter() < AIContext.getAPCost()) && useAP) {
		// Deny move - out of actions
		if (!this.bd.getActiveCharacter().getTemplate().hasAI()) {
		    this.setStatusMessage("Out of moves!");
		}
		return false;
	    }
	    // Move
	    DungeonObject obj1 = null;
	    DungeonObject obj2 = null;
	    DungeonObject obj3 = null;
	    DungeonObject obj4 = null;
	    DungeonObject obj6 = null;
	    DungeonObject obj7 = null;
	    DungeonObject obj8 = null;
	    DungeonObject obj9 = null;
	    try {
		obj1 = m.getCell(px - 1, py - 1, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    } catch (final ArrayIndexOutOfBoundsException aioob) {
		// Ignore
	    }
	    try {
		obj2 = m.getCell(px, py - 1, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    } catch (final ArrayIndexOutOfBoundsException aioob) {
		// Ignore
	    }
	    try {
		obj3 = m.getCell(px + 1, py - 1, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    } catch (final ArrayIndexOutOfBoundsException aioob) {
		// Ignore
	    }
	    try {
		obj4 = m.getCell(px - 1, py, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    } catch (final ArrayIndexOutOfBoundsException aioob) {
		// Ignore
	    }
	    try {
		obj6 = m.getCell(px + 1, py - 1, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    } catch (final ArrayIndexOutOfBoundsException aioob) {
		// Ignore
	    }
	    try {
		obj7 = m.getCell(px - 1, py + 1, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    } catch (final ArrayIndexOutOfBoundsException aioob) {
		// Ignore
	    }
	    try {
		obj8 = m.getCell(px, py + 1, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    } catch (final ArrayIndexOutOfBoundsException aioob) {
		// Ignore
	    }
	    try {
		obj9 = m.getCell(px + 1, py + 1, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    } catch (final ArrayIndexOutOfBoundsException aioob) {
		// Ignore
	    }
	    // Auto-attack check
	    if (obj1 instanceof BattleCharacter) {
		if ((x != -1 || y != 0) && (x != -1 || y != -1) && (x != 0 || y != -1)) {
		    final var bc1 = (BattleCharacter) obj1;
		    if (bc1.getTeamID() != activeBC.getTeamID()) {
			this.executeAutoAI(bc1);
		    }
		}
	    }
	    if (obj2 instanceof final BattleCharacter bc2 && y == 1 && bc2.getTeamID() != activeBC.getTeamID()) {
		this.executeAutoAI(bc2);
	    }
	    if (obj3 instanceof BattleCharacter) {
		if ((x != 0 || y != -1) && (x != 1 || y != -1) && (x != 1 || y != 0)) {
		    final var bc3 = (BattleCharacter) obj3;
		    if (bc3.getTeamID() != activeBC.getTeamID()) {
			this.executeAutoAI(bc3);
		    }
		}
	    }
	    if (obj4 instanceof final BattleCharacter bc4 && x == 1 && bc4.getTeamID() != activeBC.getTeamID()) {
		this.executeAutoAI(bc4);
	    }
	    if (obj6 instanceof final BattleCharacter bc6 && x == -1 && bc6.getTeamID() != activeBC.getTeamID()) {
		this.executeAutoAI(bc6);
	    }
	    if (obj7 instanceof BattleCharacter) {
		if ((x != -1 || y != 0) && (x != -1 || y != 1) && (x != 0 || y != 1)) {
		    final var bc7 = (BattleCharacter) obj7;
		    if (bc7.getTeamID() != activeBC.getTeamID()) {
			this.executeAutoAI(bc7);
		    }
		}
	    }
	    if (obj8 instanceof final BattleCharacter bc8 && y == -1 && bc8.getTeamID() != activeBC.getTeamID()) {
		this.executeAutoAI(bc8);
	    }
	    if (obj9 instanceof BattleCharacter) {
		if ((x != 0 || y != 1) && (x != 1 || y != 1) && (x != 1 || y != 0)) {
		    final var bc9 = (BattleCharacter) obj9;
		    if (bc9.getTeamID() != activeBC.getTeamID()) {
			this.executeAutoAI(bc9);
		    }
		}
	    }
	    m.setCell(activeBC.getSavedObject(), px, py, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    activeBC.offsetX(x);
	    activeBC.offsetY(y);
	    px += x;
	    py += y;
	    this.battleGUI.getViewManager().offsetViewingWindowLocationX(y);
	    this.battleGUI.getViewManager().offsetViewingWindowLocationY(x);
	    activeBC.setSavedObject(m.getCell(px, py, 0, DungeonConstants.LAYER_LOWER_OBJECTS));
	    m.setCell(activeBC, px, py, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    this.decrementActiveActionCounterBy(AIContext.getAPCost());
	    if (activeBC.getTeamID() == Creature.TEAM_PARTY) {
		SoundLoader.playSound(Sounds.STEP_PARTY);
	    } else {
		SoundLoader.playSound(Sounds.STEP_ENEMY);
	    }
	} else if (next instanceof BattleCharacter) {
	    if ((!useAP || this.getActiveAttackCounter() <= 0) && useAP) {
		// Deny attack - out of actions
		if (!this.bd.getActiveCharacter().getTemplate().hasAI()) {
		    this.setStatusMessage("Out of attacks!");
		}
		return false;
	    }
	    // Attack
	    final var bc = (BattleCharacter) next;
	    if (bc.getTeamID() == activeBC.getTeamID()) {
		// Attack Friend?
		if (active.hasAI()) {
		    return false;
		}
		final var confirm = CommonDialogs.showConfirmDialog("Attack Friend?", "Battle");
		if (confirm != CommonDialogs.YES_OPTION) {
		    return false;
		}
	    }
	    if (useAP) {
		this.decrementActiveAttackCounter();
	    }
	    // Do damage
	    this.computeDamage(theEnemy.getTemplate(), active, activeDE);
	    // Handle low health for party members
	    if (theEnemy.getTemplate().isAlive() && theEnemy.getTeamID() == Creature.TEAM_PARTY
		    && theEnemy.getTemplate().getCurrentHP() <= theEnemy.getTemplate().getMaximumHP() * 3 / 10) {
		SoundLoader.playSound(Sounds.LOW_HEALTH);
	    }
	    // Handle enemy death
	    if (!theEnemy.getTemplate().isAlive()) {
		if (theEnemy.getTeamID() != Creature.TEAM_PARTY) {
		    // Update victory spoils
		    this.battleExp = theEnemy.getTemplate().getExperience();
		}
		this.handleDeath(bc);
	    }
	    // Handle self death
	    if (!active.isAlive()) {
		this.handleDeath(activeBC);
	    }
	} else {
	    // Move Failed
	    if (!active.hasAI()) {
		this.setStatusMessage("Can't go that way");
	    }
	    return false;
	}
	this.updateStatsAndEffects();
	final var currResult = this.getResult();
	if (currResult != BattleResult.IN_PROGRESS) {
	    // Battle Done
	    this.result = currResult;
	    this.doResult();
	}
	this.battleGUI.getViewManager().setViewingWindowCenterX(py);
	this.battleGUI.getViewManager().setViewingWindowCenterY(px);
	this.redrawBattle();
	return true;
    }

    private void updateStatsAndEffects() {
	this.battleGUI.updateStatsAndEffects(this.bd);
    }

    private void waitForAI() {
	this.battleGUI.turnEventHandlersOff();
    }
}
