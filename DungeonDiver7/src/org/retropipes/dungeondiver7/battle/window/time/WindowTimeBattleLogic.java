/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.window.time;

import java.util.Timer;
import java.util.TimerTask;

import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.StuffBag;
import org.retropipes.dungeondiver7.battle.Battle;
import org.retropipes.dungeondiver7.battle.BattleAction;
import org.retropipes.dungeondiver7.battle.BattleCharacter;
import org.retropipes.dungeondiver7.battle.BattleResult;
import org.retropipes.dungeondiver7.battle.damage.DamageEngine;
import org.retropipes.dungeondiver7.battle.types.BattleType;
import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.creature.GameDifficulty;
import org.retropipes.dungeondiver7.creature.StatConstants;
import org.retropipes.dungeondiver7.creature.effect.Effect;
import org.retropipes.dungeondiver7.creature.monster.FinalBossMonster;
import org.retropipes.dungeondiver7.creature.monster.Monster;
import org.retropipes.dungeondiver7.creature.monster.MonsterFactory;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.creature.party.PartyMember;
import org.retropipes.dungeondiver7.creature.spell.SpellCaster;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.prefs.Prefs;

public class WindowTimeBattleLogic extends Battle {
    private class EnemyTask extends TimerTask {
	public EnemyTask() {
	    // Do nothing
	}

	private void enemyAct() {
	    final var logic = WindowTimeBattleLogic.this;
	    final var gui = logic.battleGUI;
	    // Do Enemy Actions
	    logic.executeNextAIAction();
	    gui.resetEnemyActionBar();
	    // Maintain Enemy Effects
	    logic.maintainEffects(false);
	    // Display Active Effects
	    logic.displayActiveEffects();
	    // Display End Stats
	    logic.displayBattleStats();
	    // Check Result
	    final var bResult = logic.getResult();
	    if (bResult != BattleResult.IN_PROGRESS) {
		logic.setResult(bResult);
		logic.doResult();
	    } else {
		// Strip Extra Newline Character
		gui.stripExtraNewLine();
		// Pack Battle Frame
		gui.battleFrame.pack();
	    }
	}

	@Override
	public void run() {
	    try {
		final var app = DungeonDiver7.getStuffBag();
		final var b = app.getBattle();
		if (app.getMode() == StuffBag.STATUS_BATTLE && b instanceof WindowTimeBattleLogic) {
		    final var logic = WindowTimeBattleLogic.this;
		    final var gui = logic.battleGUI;
		    if (!gui.isEnemyActionBarFull()) {
			gui.updateEnemyActionBarValue();
			if (gui.isEnemyActionBarFull()) {
			    this.enemyAct();
			}
		    }
		}
	    } catch (final Throwable t) {
		DungeonDiver7.logError(t);
	    }
	}
    }

    private class PlayerTask extends TimerTask {
	public PlayerTask() {
	    // Do nothing
	}

	@Override
	public void run() {
	    try {
		final var app = DungeonDiver7.getStuffBag();
		final var b = app.getBattle();
		if (app.getMode() == StuffBag.STATUS_BATTLE && b instanceof WindowTimeBattleLogic) {
		    final var logic = WindowTimeBattleLogic.this;
		    final var gui = logic.battleGUI;
		    if (!gui.isPlayerActionBarFull()) {
			gui.disableActionButtons();
			gui.updatePlayerActionBarValue();
			if (gui.isPlayerActionBarFull()) {
			    SoundLoader.playSound(Sounds.PLAYER_UP);
			    gui.enableActionButtons();
			}
		    } else {
			gui.enableActionButtons();
		    }
		}
	    } catch (final Throwable t) {
		DungeonDiver7.logError(t);
	    }
	}
    }

    private static final int BASE_RUN_CHANCE = 80;
    private static final int RUN_CHANCE_DIFF_FACTOR = 5;
    private static final int ENEMY_BASE_RUN_CHANCE = 60;
    private static final int ENEMY_RUN_CHANCE_DIFF_FACTOR = 10;
    // Fields
    private int stealAmount;
    private int damage;
    private boolean enemyDidDamage;
    private boolean playerDidDamage;
    private BattleType battleType;
    private Creature enemy;
    private BattleCharacter enemyBC;
    private BattleResult result;
    private final DamageEngine pde;
    private final DamageEngine ede;
    WindowTimeBattleGUI battleGUI;
    private final Timer battleTimer;

    // Constructor
    public WindowTimeBattleLogic() {
	// Initialize Battle Parameters
	this.pde = DamageEngine.getPlayerInstance();
	this.ede = DamageEngine.getEnemyInstance();
	this.battleTimer = new Timer();
	this.battleTimer.schedule(new PlayerTask(), 0, WindowTimeBattleSpeed.getSpeed());
	this.battleTimer.schedule(new EnemyTask(), 0, WindowTimeBattleSpeed.getSpeed());
	this.damage = 0;
	this.stealAmount = 0;
	this.enemyDidDamage = false;
	this.playerDidDamage = false;
	// Initialize GUI
	this.battleGUI = new WindowTimeBattleGUI();
    }

    @Override
    public final void battleDone() {
	this.battleGUI.getOutputFrame().setVisible(false);
	final var gm = DungeonDiver7.getStuffBag().getGame();
	gm.showOutput();
	gm.redrawDungeon();
    }

    @Override
    public boolean castSpell() {
	final var playerCharacter = PartyManager.getParty().getLeader();
	return SpellCaster.selectAndCastSpell(playerCharacter, this.enemy);
    }

    final void clearMessageArea() {
	this.battleGUI.clearMessageArea();
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
	// Check damage
	if (acting instanceof PartyMember) {
	    if (this.damage > 0) {
		this.playerDidDamage = true;
	    } else if (this.damage < 0) {
		this.enemyDidDamage = true;
	    }
	} else if (acting instanceof Monster || acting instanceof FinalBossMonster) {
	    if (this.damage > 0) {
		this.enemyDidDamage = true;
	    } else if (this.damage < 0) {
		this.playerDidDamage = true;
	    }
	}
    }

    final void computeEnemyDamage() {
	// Compute Enemy Damage
	this.computeDamage(PartyManager.getParty().getLeader(), this.enemy, this.ede);
    }

    final int computeEnemyRunChance() {
	return WindowTimeBattleLogic.ENEMY_BASE_RUN_CHANCE
		+ this.enemy.getLevelDifference() * WindowTimeBattleLogic.ENEMY_RUN_CHANCE_DIFF_FACTOR;
    }

    final void computePlayerDamage() {
	// Compute Player Damage
	this.computeDamage(this.enemy, PartyManager.getParty().getLeader(), this.pde);
    }

    final int computeRunChance() {
	return WindowTimeBattleLogic.BASE_RUN_CHANCE
		- this.enemy.getLevelDifference() * WindowTimeBattleLogic.RUN_CHANCE_DIFF_FACTOR;
    }

    @Override
    public final void displayActiveEffects() {
	boolean flag1 = false, flag2 = false, flag3 = false;
	final var playerCharacter = PartyManager.getParty().getLeader();
	final var effectString = playerCharacter.getCompleteEffectString();
	final var effectMessages = playerCharacter.getAllCurrentEffectMessages();
	final var enemyEffectMessages = this.enemy.getAllCurrentEffectMessages();
	final var nMsg = Effect.getNullMessage();
	if (!effectString.equals(nMsg)) {
	    flag1 = true;
	}
	if (!effectMessages.equals(nMsg)) {
	    flag2 = true;
	}
	if (!enemyEffectMessages.equals(nMsg)) {
	    flag3 = true;
	}
	if (flag1) {
	    this.setStatusMessage(effectString);
	}
	if (flag2) {
	    this.setStatusMessage(effectMessages);
	}
	if (flag3) {
	    this.setStatusMessage(enemyEffectMessages);
	}
    }

    @Override
    public final void displayBattleStats() {
	final var playerCharacter = PartyManager.getParty().getLeader();
	final var enemyName = this.enemy.getName();
	final var fightingWhat = this.enemy.getFightingWhatString();
	final var monsterLevelString = enemyName + "'s Level: " + Integer.toString(this.enemy.getLevel());
	final var monsterHPString = this.enemy.getHPString();
	final var monsterMPString = this.enemy.getMPString();
	final var playerHPString = playerCharacter.getHPString();
	final var playerMPString = playerCharacter.getMPString();
	final var displayMonsterHPString = enemyName + "'s HP: " + monsterHPString;
	final var displayMonsterMPString = enemyName + "'s MP: " + monsterMPString;
	final var displayPlayerHPString = "Your HP: " + playerHPString;
	final var displayPlayerMPString = "Your MP: " + playerMPString;
	final var displayString = fightingWhat + "\n" + monsterLevelString + "\n" + displayMonsterHPString + "\n"
		+ displayMonsterMPString + "\n" + displayPlayerHPString + "\n" + displayPlayerMPString;
	this.setStatusMessage(displayString);
    }

    final void displayEnemyRoundResults() {
	// Display enemy round results
	if (this.result != BattleResult.ENEMY_FLED) {
	    final var enemyName = this.enemy.getName();
	    final var enemyDamageString = Integer.toString(this.damage);
	    final var enemyFumbleDamageString = Integer.toString(this.damage);
	    String displayEnemyDamageString = null;
	    var enemyWhackString = "";
	    if (this.ede.weaponFumble()) {
		displayEnemyDamageString = "FUMBLE! The " + enemyName + " drops its weapon, doing "
			+ enemyFumbleDamageString + " damage to itself!";
		SoundLoader.playSound(Sounds.FUMBLE);
		enemyWhackString = "";
	    } else {
		if (this.damage == 0) {
		    displayEnemyDamageString = "The " + enemyName + " tries to hit you, but MISSES!";
		    SoundLoader.playSound(Sounds.MISSED);
		} else if (this.damage < 0) {
		    displayEnemyDamageString = "The " + enemyName + " tries to hit you, but you RIPOSTE for "
			    + -this.damage + " damage!";
		    SoundLoader.playSound(Sounds.PARTY_COUNTER);
		} else {
		    displayEnemyDamageString = "The " + enemyName + " hits you for " + enemyDamageString + " damage!";
		    SoundLoader.playSound(Sounds.ENEMY_HIT);
		}
		if (this.ede.weaponCrit()) {
		    enemyWhackString += "CRITICAL HIT!\n";
		    SoundLoader.playSound(Sounds.CRITICAL);
		}
		if (this.ede.weaponPierce()) {
		    enemyWhackString += "The " + enemyName + "'s attack pierces YOUR armor!\n";
		}
	    }
	    final var displayString = enemyWhackString + displayEnemyDamageString;
	    this.setStatusMessage(displayString);
	}
    }

    final void displayPlayerRoundResults() {
	// Display player round results
	if (this.result != BattleResult.ENEMY_FLED) {
	    final var enemyName = this.enemy.getName();
	    final var playerDamageString = Integer.toString(this.damage);
	    final var playerFumbleDamageString = Integer.toString(this.damage);
	    String displayPlayerDamageString = null;
	    var playerWhackString = new StringBuilder();
	    if (this.pde.weaponFumble()) {
		displayPlayerDamageString = "FUMBLE! You drop your weapon, doing " + playerFumbleDamageString
			+ " damage to yourself!";
		SoundLoader.playSound(Sounds.FUMBLE);
	    } else {
		if (this.damage == 0) {
		    displayPlayerDamageString = "You try to hit the " + enemyName + ", but MISS!";
		    SoundLoader.playSound(Sounds.MISSED);
		} else if (this.damage < 0) {
		    displayPlayerDamageString = "You try to hit the " + enemyName + ", but are RIPOSTED for "
			    + -this.damage + " damage!";
		    SoundLoader.playSound(Sounds.ENEMY_COUNTER);
		} else {
		    displayPlayerDamageString = "You hit the " + enemyName + " for " + playerDamageString + " damage!";
		    SoundLoader.playSound(Sounds.ATTACK_PUNCH);
		}
		if (this.pde.weaponCrit()) {
		    playerWhackString.append("CRITICAL HIT!\n");
		    SoundLoader.playSound(Sounds.CRITICAL);
		}
		if (this.pde.weaponPierce()) {
		    playerWhackString.append("Your attack pierces the ").append(enemyName).append("'s armor!\n");
		}
	    }
	    final var displayString = playerWhackString.append(displayPlayerDamageString).toString();
	    this.setStatusMessage(displayString);
	}
    }

    // Methods
    @Override
    public void doBattle() {
	try {
	    final var app = DungeonDiver7.getStuffBag();
	    final var gm = app.getGame();
	    if (app.getMode() != StuffBag.STATUS_BATTLE) {
		SoundLoader.playSound(Sounds.DRAW_SWORD);
	    }
	    this.battleType = BattleType.createBattle(0, 0);
	    app.setMode(StuffBag.STATUS_BATTLE);
	    gm.hideOutput();
	    gm.stopMovement();
	    this.enemyBC = this.battleType.getBattlers();
	    this.enemy = this.enemyBC.getCreature();
	    this.enemy.healAndRegenerateFully();
	    this.enemy.loadCreature();
	    this.battleGUI.setMaxPlayerActionBarValue(PartyManager.getParty().getLeader().getActionBarSpeed());
	    this.battleGUI.setMaxEnemyActionBarValue(this.enemy.getActionBarSpeed());
	    this.enemyDidDamage = false;
	    this.playerDidDamage = false;
	    this.setResult(BattleResult.IN_PROGRESS);
	    this.battleGUI.initBattle(this.enemy.getImage());
	    this.firstUpdateMessageArea();
	} catch (final Throwable t) {
	    DungeonDiver7.logError(t);
	}
    }

    @Override
    public void doBattleByProxy() {
	this.enemy = MonsterFactory.getNewMonsterInstance(Prefs.getGameDifficulty());
	this.enemy.loadCreature();
	final var playerCharacter = PartyManager.getParty().getLeader();
	final var m = this.enemy;
	playerCharacter.offsetExperience(m.getExperience());
	playerCharacter.offsetGold(m.getGold());
	// Level Up Check
	if (playerCharacter.checkLevelUp()) {
	    playerCharacter.levelUp();
	    DungeonDiver7.getStuffBag().getGame().keepNextMessage();
	    DungeonDiver7.getStuffBag().showMessage("You reached level " + playerCharacter.getLevel() + ".");
	}
    }

    @Override
    public void doFinalBossBattle() {
	try {
	    final var app = DungeonDiver7.getStuffBag();
	    final var gm = app.getGame();
	    if (app.getMode() != StuffBag.STATUS_BATTLE) {
		SoundLoader.playSound(Sounds.DRAW_SWORD);
	    }
	    this.battleType = BattleType.createFinalBossBattle(0, 0);
	    app.setMode(StuffBag.STATUS_BATTLE);
	    gm.hideOutput();
	    gm.stopMovement();
	    this.enemyBC = this.battleType.getBattlers();
	    this.enemy = this.enemyBC.getCreature();
	    this.enemy.healAndRegenerateFully();
	    this.enemy.loadCreature();
	    this.enemyDidDamage = false;
	    this.playerDidDamage = false;
	    this.setResult(BattleResult.IN_PROGRESS);
	    this.battleGUI.initBattle(this.enemy.getImage());
	    this.firstUpdateMessageArea();
	} catch (final Throwable t) {
	    DungeonDiver7.logError(t);
	}
    }

    @Override
    public final boolean doPlayerActions(final BattleAction actionToPerform) {
	var success = true;
	final var playerCharacter = PartyManager.getParty().getLeader();
	switch (actionToPerform) {
	case BattleAction.ATTACK:
	    final var actions = playerCharacter.getWindowBattleActionsPerRound();
	    for (var x = 0; x < actions; x++) {
		this.computePlayerDamage();
		this.displayPlayerRoundResults();
	    }
	    break;
	case BattleAction.CAST_SPELL:
	    success = this.castSpell();
	    break;
	case BattleAction.FLEE:
	    final var rf = new RandomRange(0, 100);
	    final var runChance = rf.generate();
	    if (runChance <= this.computeRunChance()) {
		// Success
		this.setResult(BattleResult.FLED);
	    } else {
		// Failure
		success = false;
		this.updateMessageAreaFleeFailed();
	    }
	    break;
	case BattleAction.STEAL:
	    success = this.steal();
	    if (success) {
		SoundLoader.playSound(Sounds.EFFECT_DRAIN);
		this.updateMessageAreaPostSteal();
	    } else {
		SoundLoader.playSound(Sounds.ACTION_FAILED);
		this.updateMessageAreaStealFailed();
	    }
	    break;
	case BattleAction.DRAIN:
	    success = this.drain();
	    if (success) {
		SoundLoader.playSound(Sounds.EFFECT_DRAIN);
		this.updateMessageAreaPostDrain();
	    } else {
		SoundLoader.playSound(Sounds.ACTION_FAILED);
		this.updateMessageAreaDrainFailed();
	    }
	    break;
	default:
	    break;
	}
	if (success) {
	    this.battleGUI.resetPlayerActionBar();
	}
	return success;
    }

    @Override
    public void doResult() {
	final var playerCharacter = PartyManager.getParty().getLeader();
	final var m = this.enemy;
	if (m instanceof FinalBossMonster) {
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
		playerCharacter.healPercentage(Creature.FULL_HEAL_PERCENTAGE);
		playerCharacter.regeneratePercentage(Creature.FULL_HEAL_PERCENTAGE);
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
		this.setStatusMessage("You gain " + m.getExperience() + " experience and " + m.getGold() + " Gold.");
		playerCharacter.offsetExperience(m.getExperience());
		playerCharacter.offsetGold(m.getGold());
		SoundLoader.playSound(Sounds.VICTORY);
		break;
	    case BattleResult.PERFECT:
		this.setStatusMessage("You gain " + m.getExperience() + " experience and " + m.getGold()
			+ " Gold,\nplus " + m.getPerfectBonusGold() + " extra gold for a perfect fight!");
		playerCharacter.offsetExperience(m.getExperience());
		playerCharacter.offsetGold(m.getGold() + m.getPerfectBonusGold());
		SoundLoader.playSound(Sounds.VICTORY);
		break;
	    case BattleResult.LOST:
		this.setStatusMessage("You lost...");
		SoundLoader.playSound(Sounds.GAME_OVER);
		PartyManager.getParty().getLeader().onDeath(-10);
		break;
	    case BattleResult.ANNIHILATED:
		this.setStatusMessage("You lost without hurting your foe... you were annihilated!");
		SoundLoader.playSound(Sounds.GAME_OVER);
		PartyManager.getParty().getLeader().onDeath(-20);
		break;
	    case BattleResult.DRAW:
		this.setStatusMessage("The battle was a draw. You are fully healed!");
		playerCharacter.healPercentage(Creature.FULL_HEAL_PERCENTAGE);
		playerCharacter.regeneratePercentage(Creature.FULL_HEAL_PERCENTAGE);
		break;
	    case BattleResult.FLED:
		this.setStatusMessage("You ran away successfully!");
		break;
	    case BattleResult.ENEMY_FLED:
		this.setStatusMessage("The enemy runs away!");
		this.setStatusMessage("Since the enemy ran away, you gain nothing for this battle.");
		break;
	    default:
		break;
	    }
	}
	// Cleanup
	this.battleGUI.doResultCleanup();
	playerCharacter.stripAllEffects();
	this.enemy.stripAllEffects();
	// Level Up Check
	if (playerCharacter.checkLevelUp()) {
	    playerCharacter.levelUp();
	    SoundLoader.playSound(Sounds.GAIN_LEVEL);
	    this.setStatusMessage("You reached level " + playerCharacter.getLevel() + ".");
	}
	// Final Cleanup
	this.battleGUI.doResultFinalCleanup();
    }

    @Override
    public final boolean drain() {
	final var playerCharacter = PartyManager.getParty().getLeader();
	final var drainChance = StatConstants.CHANCE_DRAIN;
	final var chance = new RandomRange(0, 100);
	final var randomChance = chance.generate();
	if (randomChance <= drainChance) {
	    // Succeeded
	    final var drained = new RandomRange(0, this.enemy.getCurrentMP());
	    final var drainAmount = drained.generate();
	    this.enemy.offsetCurrentMP(-drainAmount);
	    playerCharacter.offsetCurrentMP(drainAmount);
	    return true;
	} else {
	    // Failed
	    return false;
	}
    }

    @Override
    public void endTurn() {
	// Do nothing
    }

    @Override
    public final void executeNextAIAction() {
	final var actionToPerform = this.enemyBC.getAI().getNextAction(this.enemy);
	switch (actionToPerform) {
	case BattleAction.ATTACK:
	    final var actions = this.enemy.getWindowBattleActionsPerRound();
	    for (var x = 0; x < actions; x++) {
		this.computeEnemyDamage();
		this.displayEnemyRoundResults();
	    }
	    break;
	case BattleAction.CAST_SPELL:
	    SpellCaster.castSpell(this.enemyBC.getAI().getSpellToCast(), this.enemy,
		    PartyManager.getParty().getLeader());
	    break;
	case BattleAction.FLEE:
	    final var rf = new RandomRange(0, 100);
	    final var runChance = rf.generate();
	    if (runChance <= this.computeEnemyRunChance()) {
		// Success
		this.setResult(BattleResult.ENEMY_FLED);
	    } else {
		// Failure
		this.updateMessageAreaEnemyFleeFailed();
	    }
	    break;
	default:
	    break;
	}
    }

    final void firstUpdateMessageArea() {
	this.clearMessageArea();
	this.setStatusMessage("*** Beginning of Round ***");
	this.displayBattleStats();
	this.setStatusMessage("*** Beginning of Round ***\n");
	// Determine initiative
	var enemyGotJump = false;
	if (this.enemy.getSpeed(GameDifficulty.NORMAL) > PartyManager.getParty().getLeader()
		.getSpeed(GameDifficulty.NORMAL)) {
	    // Enemy acts first!
	    enemyGotJump = true;
	} else if (this.enemy.getSpeed(GameDifficulty.NORMAL) < PartyManager.getParty().getLeader()
		.getSpeed(GameDifficulty.NORMAL)) {
	    // You act first!
	    enemyGotJump = false;
	} else {
	    // Equal, decide randomly
	    final var jump = new RandomRange(0, 1);
	    final var whoFirst = jump.generate();
	    if (whoFirst == 1) {
		// Enemy acts first!
		enemyGotJump = true;
	    } else {
		// You act first!
		enemyGotJump = false;
	    }
	}
	if (enemyGotJump) {
	    this.setStatusMessage("The enemy acts first!");
	    this.executeNextAIAction();
	    // Display Active Effects
	    this.displayActiveEffects();
	    // Maintain Effects
	    this.maintainEffects(true);
	    this.maintainEffects(false);
	    // Check result
	    this.setResult(this.getResult());
	    if (this.result != BattleResult.IN_PROGRESS) {
		this.doResult();
		return;
	    }
	} else {
	    this.setStatusMessage("You act first!");
	}
	this.setStatusMessage("\n*** End of Round ***");
	this.displayBattleStats();
	this.setStatusMessage("*** End of Round ***");
	this.stripExtraNewLine();
	this.battleGUI.getOutputFrame().pack();
    }

    @Override
    public Creature getEnemy() {
	return this.enemy;
    }

    @Override
    public boolean getLastAIActionResult() {
	return true;
    }

    @Override
    public final BattleResult getResult() {
	final var playerCharacter = PartyManager.getParty().getLeader();
	BattleResult currResult;
	if (this.result != BattleResult.IN_PROGRESS) {
	    return this.result;
	}
	if (this.enemy.isAlive() && !playerCharacter.isAlive()) {
	    if (!this.playerDidDamage) {
		currResult = BattleResult.ANNIHILATED;
	    } else {
		currResult = BattleResult.LOST;
	    }
	} else if (!this.enemy.isAlive() && playerCharacter.isAlive()) {
	    if (!this.enemyDidDamage) {
		currResult = BattleResult.PERFECT;
	    } else {
		currResult = BattleResult.WON;
	    }
	} else if (!this.enemy.isAlive() && !playerCharacter.isAlive()) {
	    currResult = BattleResult.DRAW;
	} else {
	    currResult = BattleResult.IN_PROGRESS;
	}
	return currResult;
    }

    @Override
    public boolean isWaitingForAI() {
	return false;
    }

    @Override
    public final void maintainEffects(final boolean player) {
	if (player) {
	    final var playerCharacter = PartyManager.getParty().getLeader();
	    playerCharacter.useEffects();
	    playerCharacter.cullInactiveEffects();
	} else {
	    this.enemy.useEffects();
	    this.enemy.cullInactiveEffects();
	}
    }

    @Override
    public void resetGUI() {
	// Destroy old GUI
	this.battleGUI.getOutputFrame().dispose();
	// Create new GUI
	this.battleGUI = new WindowTimeBattleGUI();
    }

    @Override
    public final void setResult(final BattleResult newResult) {
	this.result = newResult;
    }

    @Override
    public final void setStatusMessage(final String s) {
	this.battleGUI.setStatusMessage(s);
    }

    @Override
    public final boolean steal() {
	final var playerCharacter = PartyManager.getParty().getLeader();
	final var stealChance = StatConstants.CHANCE_STEAL;
	final var chance = new RandomRange(0, 100);
	final var randomChance = chance.generate();
	if (randomChance <= stealChance) {
	    // Succeeded
	    final var stole = new RandomRange(0, this.enemy.getGold());
	    this.stealAmount = stole.generate();
	    playerCharacter.offsetGold(this.stealAmount);
	    return true;
	} else {
	    // Failed
	    this.stealAmount = 0;
	    return false;
	}
    }

    final void stripExtraNewLine() {
	this.battleGUI.stripExtraNewLine();
    }

    final void updateMessageAreaDrainFailed() {
	this.setStatusMessage("You try to drain the enemy's MP, but the attempt fails!");
    }

    final void updateMessageAreaEnemyFleeFailed() {
	this.setStatusMessage("The enemy tries to run away, but doesn't quite make it!");
    }

    final void updateMessageAreaFleeFailed() {
	this.setStatusMessage("You try to run away, but don't quite make it!");
    }

    final void updateMessageAreaPostDrain() {
	this.setStatusMessage("You try to drain the enemy, and succeed!");
    }

    final void updateMessageAreaPostSteal() {
	this.setStatusMessage("You try to steal money, and successfully steal " + this.stealAmount + " Gold!");
    }

    final void updateMessageAreaStealFailed() {
	this.setStatusMessage("You try to steal money from the enemy, but the attempt fails!");
    }

    @Override
    public boolean updatePosition(final int x, final int y) {
	return false;
    }
}
