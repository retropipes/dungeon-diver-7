/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.map.time;

import java.io.IOException;
import java.util.Timer;

import javax.swing.JOptionPane;

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
import org.retropipes.dungeondiver7.battle.map.MapBattleDefinitions;
import org.retropipes.dungeondiver7.battle.reward.BattleRewards;
import org.retropipes.dungeondiver7.battle.types.BattleType;
import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.creature.StatConstants;
import org.retropipes.dungeondiver7.creature.monster.FinalBossMonster;
import org.retropipes.dungeondiver7.creature.monster.MonsterFactory;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.dungeon.Dungeon;
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

public class MapTimeBattleLogic extends Battle {
    // Fields
    private BattleType battleType;
    private MapBattleDefinitions bd;
    private Dungeon battleMap;
    private DamageEngine pde;
    private DamageEngine ede;
    private final AutoMapAI auto;
    private int damage;
    private BattleResult result;
    private long battleExp;
    private boolean resultDoneAlready;
    private boolean lastAIActionResult;
    MapTimeBattleGUI battleGUI;
    private BattleCharacter me;
    private BattleCharacter enemy;
    private AIContext myContext;
    private AIContext enemyContext;
    private final Timer battleTimer;

    // Constructors
    public MapTimeBattleLogic() {
	this.battleGUI = new MapTimeBattleGUI();
	this.auto = new AutoMapAI();
	this.battleTimer = new Timer();
	this.battleTimer.schedule(new MapTimeBattlePlayerTask(this), 0, MapTimeBattleSpeed.getSpeed());
	this.battleTimer.schedule(new MapTimeBattleEnemyTask(this), 0, MapTimeBattleSpeed.getSpeed());
    }

    private boolean areTeamEnemiesAlive(final int teamID) {
	if (teamID == Creature.TEAM_PARTY) {
	    return this.enemy.getTemplate().isAlive();
	} else {
	    return this.me.getTemplate().isAlive();
	}
    }

    private boolean areTeamEnemiesDeadOrGone(final int teamID) {
	if (teamID == Creature.TEAM_PARTY) {
	    var deadCount = 0;
	    if (this.enemy != null) {
		final var res = this.enemy.getTemplate().isAlive() && this.enemy.isActive();
		if (res) {
		    return false;
		}
		if (!this.enemy.getTemplate().isAlive()) {
		    deadCount++;
		}
	    }
	    return deadCount > 0;
	} else {
	    var deadCount = 0;
	    if (this.me != null) {
		final var res = this.me.getTemplate().isAlive() && this.me.isActive();
		if (res) {
		    return false;
		}
		if (!this.me.getTemplate().isAlive()) {
		    deadCount++;
		}
	    }
	    return deadCount > 0;
	}
    }

    private boolean areTeamEnemiesGone(final int teamID) {
	if (teamID == Creature.TEAM_PARTY) {
	    var res = true;
	    if ((this.enemy != null) && this.enemy.getTemplate().isAlive()) {
		res = res && !this.enemy.isActive();
		if (!res) {
		    return false;
		}
	    }
	    return true;
	} else {
	    var res = true;
	    if ((this.me != null) && this.me.getTemplate().isAlive()) {
		res = res && !this.me.isActive();
		if (!res) {
		    return false;
		}
	    }
	    return true;
	}
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

    private boolean castEnemySpell() {
	// Active character has AI, and AI is turned on
	final var sp = this.enemy.getTemplate().getMapAI().getSpellToCast();
	final var success = SpellCaster.castSpell(sp, this.enemy.getTemplate());
	final var currResult = this.getResult();
	if (currResult != BattleResult.IN_PROGRESS) {
	    // Battle Done
	    this.result = currResult;
	    this.doResult();
	}
	return success;
    }

    @Override
    public boolean castSpell() {
	final var success = SpellCaster.selectAndCastSpell(this.me.getTemplate());
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
	this.displayRoundResults(theEnemy, acting, activeDE);
    }

    @Override
    public void displayActiveEffects() {
	// Do nothing
    }

    @Override
    public void displayBattleStats() {
	// Do nothing
    }

    private void displayRoundResults(final Creature theEnemy, final Creature active, final DamageEngine activeDE) {
	// Display round results
	final var activeName = active.getName();
	final var enemyName = theEnemy.getName();
	final var isPlayer = active.getTeamID() == Creature.TEAM_PARTY;
	final var counterSound = isPlayer ? Sounds.PARTY_COUNTER : Sounds.ENEMY_COUNTER;
	final var hitSound = isPlayer ? Sounds.ATTACK_PUNCH : Sounds.ENEMY_HIT;
	var damageString = Integer.toString(this.damage);
	var displayDamageString = " ";
	if (this.damage == 0) {
	    if (activeDE.weaponMissed()) {
		displayDamageString = activeName + " tries to hit " + enemyName + ", but MISSES!";
		SoundLoader.playSound(Sounds.MISSED);
	    } else if (activeDE.enemyDodged()) {
		displayDamageString = activeName + " tries to hit " + enemyName + ", but " + enemyName
			+ " AVOIDS the attack!";
		SoundLoader.playSound(Sounds.MISSED);
	    } else {
		displayDamageString = activeName + " tries to hit " + enemyName + ", but the attack is BLOCKED!";
		SoundLoader.playSound(Sounds.MISSED);
	    }
	} else if (this.damage < 0) {
	    damageString = Integer.toString(-this.damage);
	    var displayDamagePrefix = "";
	    if (activeDE.weaponCrit() && activeDE.weaponPierce()) {
		displayDamagePrefix = "PIERCING CRITICAL HIT! ";
		SoundLoader.playSound(counterSound);
		SoundLoader.playSound(Sounds.CRITICAL);
	    } else if (activeDE.weaponCrit()) {
		displayDamagePrefix = "CRITICAL HIT! ";
		SoundLoader.playSound(Sounds.CRITICAL);
	    } else if (activeDE.weaponPierce()) {
		displayDamagePrefix = "PIERCING HIT! ";
		SoundLoader.playSound(counterSound);
	    }
	    displayDamageString = displayDamagePrefix + activeName + " tries to hit " + enemyName + ", but " + enemyName
		    + " RIPOSTES for " + damageString + " damage!";
	    SoundLoader.playSound(counterSound);
	} else {
	    var displayDamagePrefix = "";
	    if (activeDE.weaponFumble()) {
		SoundLoader.playSound(Sounds.FUMBLE);
		displayDamageString = "FUMBLE! " + activeName + " drops their weapon on themselves, doing "
			+ damageString + " damage!";
	    } else {
		if (activeDE.weaponCrit() && activeDE.weaponPierce()) {
		    displayDamagePrefix = "PIERCING CRITICAL HIT! ";
		    SoundLoader.playSound(counterSound);
		    SoundLoader.playSound(Sounds.CRITICAL);
		} else if (activeDE.weaponCrit()) {
		    displayDamagePrefix = "CRITICAL HIT! ";
		    SoundLoader.playSound(Sounds.CRITICAL);
		} else if (activeDE.weaponPierce()) {
		    displayDamagePrefix = "PIERCING HIT! ";
		    SoundLoader.playSound(counterSound);
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
	// Set Action Bars
	this.battleGUI.setMaxPlayerActionBarValue(PartyManager.getParty().getLeader().getActionBarSpeed());
	this.battleGUI.setMaxEnemyActionBarValue(this.enemy.getTemplate().getActionBarSpeed());
	// Set Character Locations
	this.setCharacterLocations();
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
	    break;
	}
	this.battleGUI.resetPlayerActionBar();
	return true;
    }

    @Override
    public void doResult() {
	if (!this.resultDoneAlready) {
	    // Handle Results
	    this.resultDoneAlready = true;
	    if (this.getEnemy() instanceof FinalBossMonster) {
		switch (this.result) {
		case BattleResult.WON:
		case BattleResult.PERFECT:
		    this.setStatusMessage("You defeated the Boss!");
		    SoundLoader.playSound(Sounds.VICTORY);
		    break;
		case BattleResult.LOST:
		    this.setStatusMessage("The Boss defeated you...");
		    SoundLoader.playSound(Sounds.GAME_OVER);
		    PartyManager.getParty().getLeader().onDeath(-10);
		    break;
		case BattleResult.ANNIHILATED:
		    this.setStatusMessage("The Boss defeated you without suffering damage... you were annihilated!");
		    SoundLoader.playSound(Sounds.GAME_OVER);
		    PartyManager.getParty().getLeader().onDeath(-20);
		    break;
		case BattleResult.DRAW:
		    this.setStatusMessage("The Boss battle was a draw. You are fully healed!");
		    PartyManager.getParty().getLeader().healPercentage(Creature.FULL_HEAL_PERCENTAGE);
		    PartyManager.getParty().getLeader().regeneratePercentage(Creature.FULL_HEAL_PERCENTAGE);
		    break;
		case BattleResult.FLED:
		    this.setStatusMessage("You ran away successfully!");
		    break;
		case BattleResult.ENEMY_FLED:
		    this.setStatusMessage("The Boss ran away!");
		    break;
		default:
		    break;
		}
	    } else {
		switch (this.result) {
		case BattleResult.WON:
		    SoundLoader.playSound(Sounds.VICTORY);
		    CommonDialogs.showTitledDialog("The party is victorious!", "Victory!");
		    PartyManager.getParty().getLeader().offsetGold(this.getGold());
		    PartyManager.getParty().getLeader().offsetExperience(this.battleExp);
		    break;
		case BattleResult.LOST:
		    CommonDialogs.showTitledDialog("The party has been defeated!", "Defeat...");
		    break;
		case BattleResult.DRAW:
		    CommonDialogs.showTitledDialog("The battle was a draw.", "Draw");
		    break;
		case BattleResult.FLED:
		    CommonDialogs.showTitledDialog("The party fled!", "Party Fled");
		    break;
		case BattleResult.ENEMY_FLED:
		    CommonDialogs.showTitledDialog("The enemies fled!", "Enemies Fled");
		    break;
		case BattleResult.IN_PROGRESS:
		    CommonDialogs.showTitledDialog("The battle isn't over, but somehow the game thinks it is.",
			    "Uh-Oh!");
		    break;
		default:
		    CommonDialogs.showTitledDialog("The result of the battle is unknown!", "Uh-Oh!");
		    break;
		}
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
	Creature activeEnemy = null;
	try {
	    activeEnemy = this.getEnemyBC(this.me).getTemplate();
	} catch (final NullPointerException npe) {
	    // Ignore
	}
	int drainChance;
	var drainAmount = 0;
	drainChance = StatConstants.CHANCE_DRAIN;
	if (activeEnemy == null) {
	    // Failed - nobody to drain from
	    this.setStatusMessage(this.me.getName() + " tries to drain, but nobody is there to drain from!");
	    return false;
	}
	if (drainChance <= 0) {
	    // Failed
	    this.setStatusMessage(this.me.getName() + " tries to drain, but fails!");
	    return false;
	} else if (drainChance >= 100) {
	    // Succeeded, unless target has 0 MP
	    final var drained = new RandomRange(0, activeEnemy.getCurrentMP());
	    drainAmount = drained.generate();
	    if (drainAmount == 0) {
		this.setStatusMessage(this.me.getName() + " tries to drain, but no MP is left to drain!");
		return false;
	    } else {
		activeEnemy.offsetCurrentMP(-drainAmount);
		this.me.getTemplate().offsetCurrentMP(drainAmount);
		this.setStatusMessage(
			this.me.getName() + " tries to drain, and successfully drains " + drainAmount + " MP!");
		return true;
	    }
	} else {
	    final var chance = new RandomRange(0, 100);
	    final var randomChance = chance.generate();
	    if (randomChance <= drainChance) {
		// Succeeded
		final var drained = new RandomRange(0, activeEnemy.getCurrentMP());
		drainAmount = drained.generate();
		if (drainAmount == 0) {
		    this.setStatusMessage(this.me.getName() + " tries to drain, but no MP is left to drain!");
		    return false;
		} else {
		    activeEnemy.offsetCurrentMP(-drainAmount);
		    this.me.getTemplate().offsetCurrentMP(drainAmount);
		    this.setStatusMessage(
			    this.me.getName() + " tries to drain, and successfully drains " + drainAmount + " MP!");
		    return true;
		}
	    } else {
		// Failed
		this.setStatusMessage(this.me.getName() + " tries to drain, but fails!");
		return false;
	    }
	}
    }

    @Override
    public void endTurn() {
	// Do nothing
    }

    private boolean enemyDrain() {
	Creature activeEnemy = null;
	try {
	    activeEnemy = this.getEnemyBC(this.enemy).getTemplate();
	} catch (final NullPointerException npe) {
	    // Ignore
	}
	int drainChance;
	var drainAmount = 0;
	drainChance = StatConstants.CHANCE_DRAIN;
	if (activeEnemy == null) {
	    // Failed - nobody to drain from
	    this.setStatusMessage(this.enemy.getName() + " tries to drain, but nobody is there to drain from!");
	    return false;
	}
	if (drainChance <= 0) {
	    // Failed
	    this.setStatusMessage(this.enemy.getName() + " tries to drain, but fails!");
	    return false;
	} else if (drainChance >= 100) {
	    // Succeeded, unless target has 0 MP
	    final var drained = new RandomRange(0, activeEnemy.getCurrentMP());
	    drainAmount = drained.generate();
	    if (drainAmount == 0) {
		this.setStatusMessage(this.enemy.getName() + " tries to drain, but no MP is left to drain!");
		return false;
	    } else {
		activeEnemy.offsetCurrentMP(-drainAmount);
		this.enemy.getTemplate().offsetCurrentMP(drainAmount);
		this.setStatusMessage(
			this.enemy.getName() + " tries to drain, and successfully drains " + drainAmount + " MP!");
		return true;
	    }
	} else {
	    final var chance = new RandomRange(0, 100);
	    final var randomChance = chance.generate();
	    if (randomChance <= drainChance) {
		// Succeeded
		final var drained = new RandomRange(0, activeEnemy.getCurrentMP());
		drainAmount = drained.generate();
		if (drainAmount == 0) {
		    this.setStatusMessage(this.enemy.getName() + " tries to drain, but no MP is left to drain!");
		    return false;
		} else {
		    activeEnemy.offsetCurrentMP(-drainAmount);
		    this.enemy.getTemplate().offsetCurrentMP(drainAmount);
		    this.setStatusMessage(
			    this.enemy.getName() + " tries to drain, and successfully drains " + drainAmount + " MP!");
		    return true;
		}
	    } else {
		// Failed
		this.setStatusMessage(this.enemy.getName() + " tries to drain, but fails!");
		return false;
	    }
	}
    }

    private boolean enemySteal() {
	Creature activeEnemy = null;
	try {
	    activeEnemy = this.getEnemyBC(this.enemy).getTemplate();
	} catch (final NullPointerException npe) {
	    // Ignore
	}
	int stealChance;
	var stealAmount = 0;
	stealChance = StatConstants.CHANCE_STEAL;
	if (activeEnemy == null) {
	    // Failed - nobody to steal from
	    this.setStatusMessage(this.enemy.getName() + " tries to steal, but nobody is there to steal from!");
	    return false;
	}
	if (stealChance <= 0) {
	    // Failed
	    this.setStatusMessage(this.enemy.getName() + " tries to steal, but fails!");
	    return false;
	} else if (stealChance >= 100) {
	    // Succeeded, unless target has 0 Gold
	    final var stole = new RandomRange(0, activeEnemy.getGold());
	    stealAmount = stole.generate();
	    if (stealAmount == 0) {
		this.setStatusMessage(this.enemy.getName() + " tries to steal, but no Gold is left to steal!");
		return false;
	    } else {
		this.enemy.getTemplate().offsetGold(stealAmount);
		this.setStatusMessage(
			this.enemy.getName() + " tries to steal, and successfully steals " + stealAmount + " gold!");
		return true;
	    }
	} else {
	    final var chance = new RandomRange(0, 100);
	    final var randomChance = chance.generate();
	    if (randomChance <= stealChance) {
		// Succeeded, unless target has 0 Gold
		final var stole = new RandomRange(0, activeEnemy.getGold());
		stealAmount = stole.generate();
		if (stealAmount == 0) {
		    this.setStatusMessage(this.enemy.getName() + " tries to steal, but no Gold is left to steal!");
		    return false;
		} else {
		    this.enemy.getTemplate().offsetGold(stealAmount);
		    this.setStatusMessage(this.enemy.getName() + " tries to steal, and successfully steals "
			    + stealAmount + " gold!");
		    return true;
		}
	    } else {
		// Failed
		this.setStatusMessage(this.enemy.getName() + " tries to steal, but fails!");
		return false;
	    }
	}
    }

    private void executeAutoAI(final BattleCharacter acting, final BattleCharacter theEnemy,
	    final AIContext theContext) {
	final var action = this.auto.getNextAction(theContext);
	switch (action) {
	case BattleAction.MOVE:
	    final var x = this.auto.getMoveX();
	    final var y = this.auto.getMoveY();
	    final var activeTID = acting.getTeamID();
	    final var activeDE = activeTID == Creature.TEAM_PARTY ? this.ede : this.pde;
	    this.updatePositionInternal(x, y, acting, theEnemy, activeDE, theContext, false);
	    break;
	default:
	    break;
	}
    }

    @Override
    public void executeNextAIAction() {
	if (this.enemy != null && this.enemy.getTemplate() != null && this.enemy.getTemplate().getMapAI() != null) {
	    final var active = this.enemy;
	    if (active.getTemplate().isAlive()) {
		final var action = active.getTemplate().getMapAI().getNextAction(this.enemyContext);
		switch (action) {
		case BattleAction.MOVE:
		    final var x = active.getTemplate().getMapAI().getMoveX();
		    final var y = active.getTemplate().getMapAI().getMoveY();
		    this.lastAIActionResult = this.updatePositionInternal(x, y, this.enemy, this.me, this.ede,
			    this.enemyContext, false);
		    active.getTemplate().getMapAI().setLastResult(this.lastAIActionResult);
		    break;
		case BattleAction.CAST_SPELL:
		    this.lastAIActionResult = this.castEnemySpell();
		    active.getTemplate().getMapAI().setLastResult(this.lastAIActionResult);
		    break;
		case BattleAction.DRAIN:
		    this.lastAIActionResult = this.enemyDrain();
		    active.getTemplate().getMapAI().setLastResult(this.lastAIActionResult);
		    break;
		case BattleAction.STEAL:
		    this.lastAIActionResult = this.enemySteal();
		    active.getTemplate().getMapAI().setLastResult(this.lastAIActionResult);
		    break;
		default:
		    this.lastAIActionResult = true;
		    break;
		}
	    }
	}
    }

    @Override
    public Creature getEnemy() {
	return this.enemy.getTemplate();
    }

    private BattleCharacter getEnemyBC(final BattleCharacter acting) {
	final var px = acting.getX();
	final var py = acting.getY();
	final var m = this.battleMap;
	DungeonObject next = null;
	for (var x = -1; x <= 1; x++) {
	    for (var y = -1; y <= 1; y++) {
		if (x == 0 && y == 0) {
		    continue;
		}
		try {
		    next = m.getCell(px + x, py + y, 0, DungeonConstants.LAYER_OBJECT);
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
		if ((next != null) && next.isSolidInBattle()) {
		    if (next instanceof BattleCharacter) {
			return (BattleCharacter) next;
		    }
		}
	    }
	}
	return null;
    }

    private int getGold() {
	return this.enemy.getTemplate().getGold();
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

    private void hideBattle() {
	this.battleGUI.hideBattle();
    }

    private boolean isTeamAlive(final int teamID) {
	if (teamID == Creature.TEAM_PARTY) {
	    return this.me.getTemplate().isAlive();
	} else {
	    return this.enemy.getTemplate().isAlive();
	}
    }

    private boolean isTeamGone(final int teamID) {
	if (teamID == Creature.TEAM_PARTY) {
	    var res = true;
	    if ((this.me != null) && this.me.getTemplate().isAlive()) {
		res = res && !this.me.isActive();
		if (!res) {
		    return false;
		}
	    }
	    return true;
	} else {
	    var res = true;
	    if ((this.enemy != null) && this.enemy.getTemplate().isAlive()) {
		res = res && !this.enemy.isActive();
		if (!res) {
		    return false;
		}
	    }
	    return true;
	}
    }

    @Override
    public boolean isWaitingForAI() {
	return !this.battleGUI.areEventHandlersOn();
    }

    @Override
    public void maintainEffects(final boolean player) {
	if (player) {
	    if (this.me != null && this.me.isActive()) {
		final var active = this.me.getTemplate();
		// Use Effects
		active.useEffects();
		// Display all effect messages
		final var effectMessages = this.me.getTemplate().getAllCurrentEffectMessages();
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
		    if (this.me.getTeamID() != Creature.TEAM_PARTY) {
			// Update victory spoils
			this.battleExp = this.me.getTemplate().getExperience();
		    }
		    // Set dead character to inactive
		    this.me.deactivate();
		    // Remove effects from dead character
		    active.stripAllEffects();
		    // Remove character from battle
		    this.battleMap.setCell(new Empty(), this.me.getX(), this.me.getY(), 0,
			    DungeonConstants.LAYER_OBJECT);
		}
	    }
	} else if (this.enemy != null && this.enemy.isActive()) {
	    final var active = this.enemy.getTemplate();
	    // Use Effects
	    active.useEffects();
	    // Display all effect messages
	    final var effectMessages = this.enemy.getTemplate().getAllCurrentEffectMessages();
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
		if (this.enemy.getTeamID() != Creature.TEAM_PARTY) {
		    // Update victory spoils
		    this.battleExp = this.enemy.getTemplate().getExperience();
		}
		// Set dead character to inactive
		this.enemy.deactivate();
		// Remove effects from dead character
		active.stripAllEffects();
		// Remove character from battle
		this.battleMap.setCell(new Empty(), this.enemy.getX(), this.enemy.getY(), 0,
			DungeonConstants.LAYER_OBJECT);
	    }
	}
    }

    private void redrawBattle() {
	this.battleGUI.redrawBattle(this.battleMap);
    }

    @Override
    public void resetGUI() {
	// Destroy old GUI
	this.battleGUI.getOutputFrame().dispose();
	// Create new GUI
	this.battleGUI = new MapTimeBattleGUI();
    }

    private void setCharacterLocations() {
	final var randX = new RandomRange(0, this.battleMap.getRows() - 1);
	final var randY = new RandomRange(0, this.battleMap.getColumns() - 1);
	int rx, ry;
	// Set Player Location
	if ((this.me != null)
		&& (this.me.isActive() && this.me.getTemplate().getX() == -1 && this.me.getTemplate().getY() == -1)) {
	    rx = randX.generate();
	    ry = randY.generate();
	    var obj = this.battleMap.getCell(rx, ry, 0, DungeonConstants.LAYER_OBJECT);
	    while (obj.isSolidInBattle()) {
		rx = randX.generate();
		ry = randY.generate();
		obj = this.battleMap.getCell(rx, ry, 0, DungeonConstants.LAYER_OBJECT);
	    }
	    this.me.setX(rx);
	    this.me.setY(ry);
	    this.battleMap.setCell(this.me, rx, ry, 0, DungeonConstants.LAYER_OBJECT);
	}
	// Set Enemy Location
	if ((this.enemy != null) && (this.enemy.isActive() && this.enemy.getTemplate().getX() == -1
		&& this.enemy.getTemplate().getY() == -1)) {
	    rx = randX.generate();
	    ry = randY.generate();
	    var obj = this.battleMap.getCell(rx, ry, 0, DungeonConstants.LAYER_OBJECT);
	    while (obj.isSolidInBattle()) {
		rx = randX.generate();
		ry = randY.generate();
		obj = this.battleMap.getCell(rx, ry, 0, DungeonConstants.LAYER_OBJECT);
	    }
	    this.enemy.setX(rx);
	    this.enemy.setY(ry);
	    this.battleMap.setCell(this.enemy, rx, ry, 0, DungeonConstants.LAYER_OBJECT);
	}
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
	Creature activeEnemy = null;
	try {
	    activeEnemy = this.getEnemyBC(this.me).getTemplate();
	} catch (final NullPointerException npe) {
	    // Ignore
	}
	int stealChance;
	var stealAmount = 0;
	stealChance = StatConstants.CHANCE_STEAL;
	if (activeEnemy == null) {
	    // Failed - nobody to steal from
	    this.setStatusMessage(this.me.getName() + " tries to steal, but nobody is there to steal from!");
	    return false;
	}
	if (stealChance <= 0) {
	    // Failed
	    this.setStatusMessage(this.me.getName() + " tries to steal, but fails!");
	    return false;
	} else if (stealChance >= 100) {
	    // Succeeded, unless target has 0 Gold
	    final var stole = new RandomRange(0, activeEnemy.getGold());
	    stealAmount = stole.generate();
	    if (stealAmount == 0) {
		this.setStatusMessage(this.me.getName() + " tries to steal, but no Gold is left to steal!");
		return false;
	    } else {
		this.me.getTemplate().offsetGold(stealAmount);
		this.setStatusMessage(
			this.me.getName() + " tries to steal, and successfully steals " + stealAmount + " gold!");
		return true;
	    }
	} else {
	    final var chance = new RandomRange(0, 100);
	    final var randomChance = chance.generate();
	    if (randomChance <= stealChance) {
		// Succeeded, unless target has 0 Gold
		final var stole = new RandomRange(0, activeEnemy.getGold());
		stealAmount = stole.generate();
		if (stealAmount == 0) {
		    this.setStatusMessage(this.me.getName() + " tries to steal, but no Gold is left to steal!");
		    return false;
		} else {
		    this.me.getTemplate().offsetGold(stealAmount);
		    this.setStatusMessage(
			    this.me.getName() + " tries to steal, and successfully steals " + stealAmount + " gold!");
		    return true;
		}
	    } else {
		// Failed
		this.setStatusMessage(this.me.getName() + " tries to steal, but fails!");
		return false;
	    }
	}
    }

    private void updateAllAIContexts() {
	this.myContext.updateContext(this.battleMap);
	this.enemyContext.updateContext(this.battleMap);
    }

    @Override
    public boolean updatePosition(final int x, final int y) {
	var theEnemy = this.enemy;
	final var activeDE = this.pde;
	if (x == 0 && y == 0) {
	    theEnemy = this.me;
	}
	return this.updatePositionInternal(x, y, this.me, theEnemy, activeDE, this.myContext, true);
    }

    private boolean updatePositionInternal(final int x, final int y, final BattleCharacter active,
	    final BattleCharacter theEnemy, final DamageEngine activeDE, final AIContext activeContext,
	    final boolean updateView) {
	final var isPlayer = active.getTeamID() == Creature.TEAM_PARTY;
	final var stepSound = isPlayer ? Sounds.STEP_PARTY : Sounds.STEP_ENEMY;
	this.updateAllAIContexts();
	var px = active.getX();
	var py = active.getY();
	final var m = this.battleMap;
	DungeonObject next = null;
	DungeonObject nextGround = null;
	DungeonObject currGround = null;
	active.saveLocation();
	if (updateView) {
	    this.battleGUI.getViewManager().saveViewingWindow();
	}
	try {
	    next = m.getCell(px + x, py + y, 0, DungeonConstants.LAYER_OBJECT);
	    nextGround = m.getCell(px + x, py + y, 0, DungeonConstants.LAYER_GROUND);
	    currGround = m.getCell(px, py, 0, DungeonConstants.LAYER_GROUND);
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Ignore
	}
	if (next != null && nextGround != null && currGround != null) {
	    if (!next.isSolidInBattle()) {
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
		    obj1 = m.getCell(px - 1, py - 1, 0, DungeonConstants.LAYER_OBJECT);
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
		try {
		    obj2 = m.getCell(px, py - 1, 0, DungeonConstants.LAYER_OBJECT);
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
		try {
		    obj3 = m.getCell(px + 1, py - 1, 0, DungeonConstants.LAYER_OBJECT);
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
		try {
		    obj4 = m.getCell(px - 1, py, 0, DungeonConstants.LAYER_OBJECT);
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
		try {
		    obj6 = m.getCell(px + 1, py - 1, 0, DungeonConstants.LAYER_OBJECT);
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
		try {
		    obj7 = m.getCell(px - 1, py + 1, 0, DungeonConstants.LAYER_OBJECT);
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
		try {
		    obj8 = m.getCell(px, py + 1, 0, DungeonConstants.LAYER_OBJECT);
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
		try {
		    obj9 = m.getCell(px + 1, py + 1, 0, DungeonConstants.LAYER_OBJECT);
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Ignore
		}
		// Auto-attack check
		if ((obj1 != null) && (obj1 instanceof BattleCharacter)) {
		    if ((((x != -1) || (y != 0)) && ((x != -1) || (y != -1)) && ((x != 0) || (y != -1)))) {
			final var bc1 = (BattleCharacter) obj1;
			if (bc1.getTeamID() != active.getTeamID()) {
			    this.executeAutoAI(bc1, active, activeContext);
			}
		    }
		}
		if ((obj2 != null) && (obj2 instanceof BattleCharacter)) {
		    if (y == 1) {
			final var bc2 = (BattleCharacter) obj2;
			if (bc2.getTeamID() != active.getTeamID()) {
			    this.executeAutoAI(bc2, active, activeContext);
			}
		    }
		}
		if ((obj3 != null) && (obj3 instanceof BattleCharacter)) {
		    if ((((x != 0) || (y != -1)) && ((x != 1) || (y != -1)) && ((x != 1) || (y != 0)))) {
			final var bc3 = (BattleCharacter) obj3;
			if (bc3.getTeamID() != active.getTeamID()) {
			    this.executeAutoAI(bc3, active, activeContext);
			}
		    }
		}
		if ((obj4 != null) && (obj4 instanceof BattleCharacter)) {
		    if (x == 1) {
			final var bc4 = (BattleCharacter) obj4;
			if (bc4.getTeamID() != active.getTeamID()) {
			    this.executeAutoAI(bc4, active, activeContext);
			}
		    }
		}
		if ((obj6 != null) && (obj6 instanceof BattleCharacter)) {
		    if (x == -1) {
			final var bc6 = (BattleCharacter) obj6;
			if (bc6.getTeamID() != active.getTeamID()) {
			    this.executeAutoAI(bc6, active, activeContext);
			}
		    }
		}
		if ((obj7 != null) && (obj7 instanceof BattleCharacter)) {
		    if ((((x != -1) || (y != 0)) && ((x != -1) || (y != 1)) && ((x != 0) || (y != 1)))) {
			final var bc7 = (BattleCharacter) obj7;
			if (bc7.getTeamID() != active.getTeamID()) {
			    this.executeAutoAI(bc7, active, activeContext);
			}
		    }
		}
		if ((obj8 != null) && (obj8 instanceof BattleCharacter)) {
		    if (y == -1) {
			final var bc8 = (BattleCharacter) obj8;
			if (bc8.getTeamID() != active.getTeamID()) {
			    this.executeAutoAI(bc8, active, activeContext);
			}
		    }
		}
		if ((obj9 != null) && (obj9 instanceof BattleCharacter)) {
		    if ((((x != 0) || (y != 1)) && ((x != 1) || (y != 1)) && ((x != 1) || (y != 0)))) {
			final var bc9 = (BattleCharacter) obj9;
			if (bc9.getTeamID() != active.getTeamID()) {
			    this.executeAutoAI(bc9, active, activeContext);
			}
		    }
		}
		m.setCell(active.getSavedObject(), px, py, 0, DungeonConstants.LAYER_OBJECT);
		active.offsetX(x);
		active.offsetY(y);
		px += x;
		py += y;
		if (updateView) {
		    this.battleGUI.getViewManager().offsetViewingWindowLocationX(y);
		    this.battleGUI.getViewManager().offsetViewingWindowLocationY(x);
		}
		active.setSavedObject(m.getCell(px, py, 0, DungeonConstants.LAYER_OBJECT));
		m.setCell(active, px, py, 0, DungeonConstants.LAYER_OBJECT);
		SoundLoader.playSound(stepSound);
	    } else if (next instanceof BattleCharacter) {
		// Attack
		final var bc = (BattleCharacter) next;
		if (bc.getTeamID() == active.getTeamID()) {
		    // Attack Friend?
		    if (!active.getTemplate().hasMapAI()) {
			final var confirm = CommonDialogs.showConfirmDialog("Attack Friend?", "Battle");
			if (confirm != JOptionPane.YES_OPTION) {
			    return false;
			}
		    } else {
			return false;
		    }
		}
		// Do damage
		this.computeDamage(theEnemy.getTemplate(), active.getTemplate(), activeDE);
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
		    // Remove effects from dead character
		    bc.getTemplate().stripAllEffects();
		    // Set dead character to inactive
		    bc.deactivate();
		    // Remove character from battle
		    m.setCell(new Empty(), bc.getX(), bc.getY(), 0, DungeonConstants.LAYER_OBJECT);
		}
		// Handle self death
		if (!active.getTemplate().isAlive()) {
		    // Remove effects from dead character
		    active.getTemplate().stripAllEffects();
		    // Set dead character to inactive
		    active.deactivate();
		    // Remove character from battle
		    m.setCell(new Empty(), active.getX(), active.getY(), 0, DungeonConstants.LAYER_OBJECT);
		}
	    } else {
		// Move Failed
		if (!active.getTemplate().hasMapAI()) {
		    this.setStatusMessage("Can't go that way");
		}
		return false;
	    }
	} else {
	    // Confirm Flee
	    if (!active.getTemplate().hasMapAI()) {
		SoundLoader.playSound(Sounds.RUN);
		final var confirm = CommonDialogs.showConfirmDialog("Embrace Cowardice?", "Battle");
		if (confirm != JOptionPane.YES_OPTION) {
		    if (updateView) {
			this.battleGUI.getViewManager().restoreViewingWindow();
		    }
		    active.restoreLocation();
		    return false;
		}
	    }
	    // Flee
	    if (updateView) {
		this.battleGUI.getViewManager().restoreViewingWindow();
	    }
	    active.restoreLocation();
	    // Set fled character to inactive
	    active.deactivate();
	    // Remove character from battle
	    m.setCell(new Empty(), active.getX(), active.getY(), 0, DungeonConstants.LAYER_OBJECT);
	    // End Turn
	    this.endTurn();
	    this.updateStatsAndEffects();
	    final var currResult = this.getResult();
	    if (currResult != BattleResult.IN_PROGRESS) {
		// Battle Done
		this.result = currResult;
		this.doResult();
	    }
	    if (updateView) {
		this.battleGUI.getViewManager().setViewingWindowCenterX(py);
		this.battleGUI.getViewManager().setViewingWindowCenterY(px);
	    }
	    this.redrawBattle();
	    return true;
	}
	this.updateStatsAndEffects();
	final var currResult = this.getResult();
	if (currResult != BattleResult.IN_PROGRESS) {
	    // Battle Done
	    this.result = currResult;
	    this.doResult();
	}
	if (updateView) {
	    this.battleGUI.getViewManager().setViewingWindowCenterX(py);
	    this.battleGUI.getViewManager().setViewingWindowCenterY(px);
	}
	this.redrawBattle();
	return true;
    }

    private void updateStatsAndEffects() {
	this.battleGUI.updateStatsAndEffects(this.me);
    }
}
