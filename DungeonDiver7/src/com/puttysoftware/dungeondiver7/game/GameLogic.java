/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.game;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.diane.strings.DianeStrings;
import com.puttysoftware.diane.utilties.DirectionResolver;
import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.dungeondiver7.Accelerators;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.MenuSection;
import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.HistoryStatus;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractCharacter;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovableObject;
import com.puttysoftware.dungeondiver7.dungeon.current.GenerateDungeonTask;
import com.puttysoftware.dungeondiver7.dungeon.objects.ArrowTurretDisguise;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.dungeon.objects.Party;
import com.puttysoftware.dungeondiver7.dungeon.objects.PowerfulParty;
import com.puttysoftware.dungeondiver7.editor.DungeonEditor;
import com.puttysoftware.dungeondiver7.loader.ImageLoader;
import com.puttysoftware.dungeondiver7.loader.MusicLoader;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.ErrorString;
import com.puttysoftware.dungeondiver7.locale.GameString;
import com.puttysoftware.dungeondiver7.locale.Menu;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.dungeondiver7.locale.old.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utility.AlreadyDeadException;
import com.puttysoftware.dungeondiver7.utility.CustomDialogs;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.FileExtensions;
import com.puttysoftware.dungeondiver7.utility.GameActions;
import com.puttysoftware.dungeondiver7.utility.InvalidDungeonException;
import com.puttysoftware.dungeondiver7.utility.PartyInventory;
import com.puttysoftware.dungeondiver7.utility.RangeTypes;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;
import com.puttysoftware.fileio.XDataReader;
import com.puttysoftware.fileio.XDataWriter;

public final class GameLogic implements MenuSection {
    // Fields
    private boolean savedGameFlag;
    private final GameViewingWindowManager vwMgr;
    AbstractCharacter player;
    private boolean stateChanged;
    private final GameGUI gui;
    private final MovementTask mt;
    private int activeShotType;
    final PlayerLocationManager plMgr;
    private final CheatManager cMgr;
    private final ScoreTracker st;
    private JLabel scoreMoves;
    private JLabel scoreShots;
    private JLabel scoreOthers;
    private JLabel otherAmmoLeft;
    private JLabel otherToolsLeft;
    private JLabel otherRangesLeft;
    private JLabel levelInfo;
    private boolean delayedDecayActive;
    private AbstractDungeonObject delayedDecayObject;
    boolean shotActive;
    boolean moving;
    private boolean remoteDecay;
    private AnimationTask animator;
    private GameReplayEngine gre;
    private boolean recording;
    private boolean replaying;
    private MLOTask mlot;
    private boolean lpbLoaded;
    private final boolean[] cheatStatus;
    private boolean autoMove;
    private boolean dead;
    int otherAmmoMode;
    int otherToolMode;
    int otherRangeMode;
    static final int OTHER_AMMO_MODE_MISSILES = 0;
    static final int OTHER_AMMO_MODE_STUNNERS = 1;
    static final int OTHER_AMMO_MODE_BLUE_LASERS = 2;
    static final int OTHER_AMMO_MODE_DISRUPTORS = 3;
    static final int OTHER_TOOL_MODE_BOOSTS = 0;
    static final int OTHER_TOOL_MODE_MAGNETS = 1;
    static final int OTHER_RANGE_MODE_BOMBS = 0;
    static final int OTHER_RANGE_MODE_HEAT_BOMBS = 1;
    static final int OTHER_RANGE_MODE_ICE_BOMBS = 2;
    static final int CHEAT_SWIMMING = 0;
    static final int CHEAT_GHOSTLY = 1;
    static final int CHEAT_INVINCIBLE = 2;
    static final int CHEAT_MISSILES = 3;
    static final int CHEAT_STUNNERS = 4;
    static final int CHEAT_BOOSTS = 5;
    static final int CHEAT_MAGNETS = 6;
    static final int CHEAT_BLUE_LASERS = 7;
    static final int CHEAT_DISRUPTORS = 8;
    static final int CHEAT_BOMBS = 9;
    static final int CHEAT_HEAT_BOMBS = 10;
    static final int CHEAT_ICE_BOMBS = 11;
    private static String[] OTHER_AMMO_CHOICES = new String[] { Strings.game(GameString.MISSILES),
	    Strings.game(GameString.STUNNERS), Strings.game(GameString.BLUE_LASERS),
	    Strings.game(GameString.DISRUPTORS) };
    private static String[] OTHER_TOOL_CHOICES = new String[] { Strings.game(GameString.BOOSTS),
	    Strings.game(GameString.MAGNETS) };
    private static String[] OTHER_RANGE_CHOICES = new String[] { Strings.game(GameString.BOMBS),
	    Strings.game(GameString.HEAT_BOMBS), Strings.game(GameString.ICE_BOMBS) };

    // Constructors
    public GameLogic() {
	this.vwMgr = new GameViewingWindowManager();
	this.plMgr = new PlayerLocationManager();
	this.cMgr = new CheatManager();
	this.st = new ScoreTracker();
	this.gui = new GameGUI();
	this.mt = new MovementTask(this.vwMgr, this.gui);
	this.mt.start();
	this.savedGameFlag = false;
	this.stateChanged = true;
	this.savedGameFlag = false;
	this.delayedDecayActive = false;
	this.delayedDecayObject = null;
	this.shotActive = false;
	this.activeShotType = ShotTypes.GREEN;
	this.remoteDecay = false;
	this.moving = false;
	this.gre = new GameReplayEngine();
	this.recording = false;
	this.replaying = false;
	this.lpbLoaded = false;
	this.cheatStatus = new boolean[this.cMgr.getCheatCount()];
	this.autoMove = false;
	this.dead = false;
	this.otherAmmoMode = GameLogic.OTHER_AMMO_MODE_MISSILES;
	this.otherToolMode = GameLogic.OTHER_TOOL_MODE_BOOSTS;
	this.otherRangeMode = GameLogic.OTHER_RANGE_MODE_BOMBS;
    }

    // Methods
    public void activeLanguageChanged() {
	this.gui.activeLanguageChanged();
	GameLogic.OTHER_AMMO_CHOICES = new String[] { Strings.game(GameString.MISSILES),
		Strings.game(GameString.STUNNERS), Strings.game(GameString.BLUE_LASERS),
		Strings.game(GameString.DISRUPTORS) };
	GameLogic.OTHER_TOOL_CHOICES = new String[] { Strings.game(GameString.BOOSTS),
		Strings.game(GameString.MAGNETS) };
	GameLogic.OTHER_RANGE_CHOICES = new String[] { Strings.game(GameString.BOMBS),
		Strings.game(GameString.HEAT_BOMBS), Strings.game(GameString.ICE_BOMBS) };
    }

    void clearDead() {
	this.dead = false;
    }

    public void abortAndWaitForMLOLoop() {
	if (this.mlot != null && this.mlot.isAlive()) {
	    this.mlot.abortLoop();
	    boolean waiting = true;
	    while (waiting) {
		try {
		    this.mlot.join();
		    waiting = false;
		} catch (final InterruptedException ie) {
		    // Ignore
		}
	    }
	}
	this.moveLoopDone();
	this.laserDone();
    }

    void waitForMLOLoop() {
	if (this.mlot != null && this.mlot.isAlive()) {
	    boolean waiting = true;
	    while (waiting) {
		try {
		    this.mlot.join();
		    waiting = false;
		} catch (final InterruptedException ie) {
		    // Ignore
		}
	    }
	}
    }

    private void abortMovementLaserObjectLoop() {
	this.mlot.abortLoop();
	this.moveLoopDone();
	this.laserDone();
    }

    public void enterCheatCode() {
	final String rawCheat = this.cMgr.enterCheat();
	if (rawCheat != null) {
	    if (rawCheat.contains(Strings.game(GameString.ENABLE_CHEAT))) {
		// Enable cheat
		final String cheat = rawCheat.substring(7);
		for (int x = 0; x < this.cMgr.getCheatCount(); x++) {
		    if (this.cMgr.queryCheatCache(cheat) == x) {
			this.cheatStatus[x] = true;
			break;
		    }
		}
	    } else {
		// Disable cheat
		final String cheat = rawCheat.substring(8);
		for (int x = 0; x < this.cMgr.getCheatCount(); x++) {
		    if (this.cMgr.queryCheatCache(cheat) == x) {
			this.cheatStatus[x] = false;
			break;
		    }
		}
	    }
	}
    }

    boolean isRemoteDecayActive() {
	return this.remoteDecay;
    }

    private static void checkMenus() {
	final DungeonEditor edit = DungeonDiver7.getStuffBag().getEditor();
	final AbstractDungeon a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	if (a.tryUndo()) {
	    edit.enableUndo();
	} else {
	    edit.disableUndo();
	}
	if (a.tryRedo()) {
	    edit.enableRedo();
	} else {
	    edit.disableRedo();
	}
    }

    public PlayerLocationManager getPlayerManager() {
	return this.plMgr;
    }

    public void setLaserType(final int type) {
	this.activeShotType = type;
    }

    void laserDone() {
	this.shotActive = false;
	GameLogic.checkMenus();
    }

    private void suspendAnimator() {
	if (this.animator != null) {
	    this.animator.stopAnimator();
	    try {
		this.animator.join();
	    } catch (final InterruptedException ie) {
		// Ignore
	    }
	    this.animator = null;
	}
    }

    private void resumeAnimator() {
	if (this.animator == null) {
	    this.animator = new AnimationTask();
	    this.animator.start();
	}
    }

    void moveLoopDone() {
	this.moving = false;
	GameLogic.checkMenus();
    }

    void replayDone() {
	this.replaying = false;
    }

    boolean isDelayedDecayActive() {
	return this.delayedDecayActive;
    }

    boolean isReplaying() {
	return this.replaying;
    }

    public AbstractCharacter getPlayer() {
	return this.player;
    }

    public void setNormalPlayer() {
	final AbstractCharacter savePlayer = this.player;
	this.player = new Party(savePlayer.getDirection(), savePlayer.getNumber());
	this.resetPlayer();
    }

    public void setPowerfulPlayer() {
	final AbstractCharacter savePlayer = this.player;
	this.player = new PowerfulParty(savePlayer.getDirection(), savePlayer.getNumber());
	this.resetPlayer();
    }

    public void setDisguisedPlayer() {
	final AbstractCharacter savePlayer = this.player;
	this.player = new ArrowTurretDisguise(savePlayer.getDirection(), savePlayer.getNumber());
	this.resetPlayer();
    }

    private void resetPlayer() {
	DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().setCell(this.player,
		this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(), this.plMgr.getPlayerLocationZ(),
		this.player.getLayer());
	this.markPlayerAsDirty();
    }

    private void updateScore() {
	this.scoreMoves.setText(Strings.game(GameString.MOVES) + LocaleConstants.COMMON_STRING_COLON
		+ LocaleConstants.COMMON_STRING_SPACE + this.st.getMoves());
	this.scoreShots.setText(Strings.game(GameString.SHOTS) + LocaleConstants.COMMON_STRING_COLON
		+ LocaleConstants.COMMON_STRING_SPACE + this.st.getShots());
	this.scoreShots.setText(Strings.game(GameString.OTHERS) + LocaleConstants.COMMON_STRING_COLON
		+ LocaleConstants.COMMON_STRING_SPACE + this.st.getOthers());
	this.updateScoreText();
    }

    private void updateInfo() {
	final AbstractDungeon a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	this.levelInfo.setText(DianeStrings.subst(Strings.dialog(DialogString.CURRENT_LEVEL_INFO),
		Integer.toString(a.getActiveLevel() + 1), a.getName().trim(), a.getAuthor().trim(),
		Strings.difficulty(a.getDifficulty())));
    }

    void updateScore(final int moves, final int shots, final int others) {
	if (moves > 0) {
	    this.st.incrementMoves();
	} else if (moves < 0) {
	    this.st.decrementMoves();
	}
	if (shots > 0) {
	    this.st.incrementShots();
	} else if (shots < 0) {
	    this.st.decrementShots();
	}
	if (others > 0) {
	    this.st.incrementOthers();
	} else if (others < 0) {
	    this.st.decrementOthers();
	}
	this.scoreMoves.setText(Strings.game(GameString.MOVES) + LocaleConstants.COMMON_STRING_COLON
		+ LocaleConstants.COMMON_STRING_SPACE + this.st.getMoves());
	this.scoreShots.setText(Strings.game(GameString.SHOTS) + LocaleConstants.COMMON_STRING_COLON
		+ LocaleConstants.COMMON_STRING_SPACE + this.st.getShots());
	this.scoreOthers.setText(Strings.game(GameString.OTHERS) + LocaleConstants.COMMON_STRING_COLON
		+ LocaleConstants.COMMON_STRING_SPACE + this.st.getOthers());
	this.updateScoreText();
    }

    private void updateScoreText() {
	// Ammo
	if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_MISSILES) {
	    if (this.getCheatStatus(GameLogic.CHEAT_MISSILES)) {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.MISSILES)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.MISSILES)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ PartyInventory.getMissilesLeft() + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	} else if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_STUNNERS) {
	    if (this.getCheatStatus(GameLogic.CHEAT_STUNNERS)) {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.STUNNERS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.STUNNERS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ PartyInventory.getStunnersLeft() + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	} else if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_BLUE_LASERS) {
	    if (this.getCheatStatus(GameLogic.CHEAT_BLUE_LASERS)) {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.BLUE_LASERS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.BLUE_LASERS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ PartyInventory.getBlueLasersLeft() + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	} else if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_DISRUPTORS) {
	    if (this.getCheatStatus(GameLogic.CHEAT_DISRUPTORS)) {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.DISRUPTORS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.DISRUPTORS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ PartyInventory.getDisruptorsLeft() + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	}
	// Tools
	if (this.otherToolMode == GameLogic.OTHER_TOOL_MODE_BOOSTS) {
	    if (this.getCheatStatus(GameLogic.CHEAT_BOOSTS)) {
		this.otherToolsLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.BOOSTS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherToolsLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.BOOSTS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ PartyInventory.getBoostsLeft() + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	} else if (this.otherToolMode == GameLogic.OTHER_TOOL_MODE_MAGNETS) {
	    if (this.getCheatStatus(GameLogic.CHEAT_MAGNETS)) {
		this.otherToolsLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.MAGNETS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherToolsLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.MAGNETS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ PartyInventory.getMagnetsLeft() + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	}
	// Ranges
	if (this.otherRangeMode == GameLogic.OTHER_RANGE_MODE_BOMBS) {
	    if (this.getCheatStatus(GameLogic.CHEAT_BOMBS)) {
		this.otherRangesLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.BOMBS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherRangesLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.BOMBS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ PartyInventory.getBombsLeft() + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	} else if (this.otherRangeMode == GameLogic.OTHER_RANGE_MODE_HEAT_BOMBS) {
	    if (this.getCheatStatus(GameLogic.CHEAT_HEAT_BOMBS)) {
		this.otherRangesLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.HEAT_BOMBS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherRangesLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.HEAT_BOMBS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ PartyInventory.getHeatBombsLeft() + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	} else if (this.otherRangeMode == GameLogic.OTHER_RANGE_MODE_ICE_BOMBS) {
	    if (this.getCheatStatus(GameLogic.CHEAT_ICE_BOMBS)) {
		this.otherRangesLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.ICE_BOMBS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherRangesLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.ICE_BOMBS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ PartyInventory.getIceBombsLeft() + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	}
    }

    public void clearReplay() {
	this.gre = new GameReplayEngine();
	this.lpbLoaded = true;
    }

    public void loadReplay(final boolean laser, final int x, final int y) {
	this.gre.updateRedoHistory(laser, x, y);
    }

    public int[] getPlayerLocation() {
	return new int[] { this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(),
		this.plMgr.getPlayerLocationZ() };
    }

    void updatePlayer() {
	final Party template = new Party(this.plMgr.getActivePlayerNumber() + 1);
	this.player = (AbstractCharacter) DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getCell(
		this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(), this.plMgr.getPlayerLocationZ(),
		template.getLayer());
    }

    public void updatePositionRelativeFrozen() {
	if (this.mlot == null) {
	    this.mlot = new MLOTask();
	} else {
	    if (!this.mlot.isAlive()) {
		this.mlot = new MLOTask();
	    }
	}
	final Directions dir = this.getPlayer().getDirection();
	final int[] unres = DirectionResolver.unresolve(dir);
	final int x = unres[0];
	final int y = unres[1];
	this.mlot.activateFrozenMovement(x, y);
	if (!this.mlot.isAlive()) {
	    this.mlot.start();
	}
	if (this.replaying) {
	    // Wait
	    while (this.moving) {
		try {
		    Thread.sleep(100);
		} catch (final InterruptedException ie) {
		    // Ignore
		}
	    }
	}
    }

    void markPlayerAsDirty() {
	DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().markAsDirty(this.plMgr.getPlayerLocationX(),
		this.plMgr.getPlayerLocationY(), this.plMgr.getPlayerLocationZ());
    }

    public static boolean canObjectMove(final int locX, final int locY, final int dirX, final int dirY) {
	return MLOTask.checkSolid(locX + dirX, locY + dirY);
    }

    public boolean fireLaser(final int ox, final int oy, final AbstractDungeonObject shooter) {
	if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_MISSILES && this.activeShotType == ShotTypes.MISSILE
		&& PartyInventory.getMissilesLeft() == 0 && !this.getCheatStatus(GameLogic.CHEAT_MISSILES)) {
	    CommonDialogs.showDialog(Strings.game(GameString.OUT_OF_MISSILES));
	} else if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_STUNNERS && this.activeShotType == ShotTypes.STUNNER
		&& PartyInventory.getStunnersLeft() == 0 && !this.getCheatStatus(GameLogic.CHEAT_STUNNERS)) {
	    CommonDialogs.showDialog(Strings.game(GameString.OUT_OF_STUNNERS));
	} else if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_BLUE_LASERS && this.activeShotType == ShotTypes.BLUE
		&& PartyInventory.getBlueLasersLeft() == 0 && !this.getCheatStatus(GameLogic.CHEAT_BLUE_LASERS)) {
	    CommonDialogs.showDialog(Strings.game(GameString.OUT_OF_BLUE_LASERS));
	} else if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_DISRUPTORS
		&& this.activeShotType == ShotTypes.DISRUPTOR && PartyInventory.getDisruptorsLeft() == 0
		&& !this.getCheatStatus(GameLogic.CHEAT_DISRUPTORS)) {
	    CommonDialogs.showDialog(Strings.game(GameString.OUT_OF_DISRUPTORS));
	} else {
	    final AbstractDungeon a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	    if (!a.isMoveShootAllowed() && !this.shotActive || a.isMoveShootAllowed()) {
		this.shotActive = true;
		final int[] currDirection = DirectionResolver.unresolve(shooter.getDirection());
		final int x = currDirection[0];
		final int y = currDirection[1];
		if (this.mlot == null) {
		    this.mlot = new MLOTask();
		} else {
		    if (!this.mlot.isAlive()) {
			this.mlot = new MLOTask();
		    }
		}
		this.mlot.activateLasers(x, y, ox, oy, this.activeShotType, shooter);
		if (!this.mlot.isAlive()) {
		    this.mlot.start();
		}
		if (this.replaying) {
		    // Wait
		    while (this.shotActive) {
			try {
			    Thread.sleep(100);
			} catch (final InterruptedException ie) {
			    // Ignore
			}
		    }
		}
		return true;
	    }
	}
	return false;
    }

    void fireRange() {
	// Boom!
	SoundLoader.playSound(SoundConstants.BOOM);
	this.updateScore(0, 0, 1);
	if (this.otherRangeMode == GameLogic.OTHER_RANGE_MODE_BOMBS) {
	    GameLogic.updateUndo(false, false, false, false, false, false, false, true, false, false);
	} else if (this.otherRangeMode == GameLogic.OTHER_RANGE_MODE_HEAT_BOMBS) {
	    GameLogic.updateUndo(false, false, false, false, false, false, false, false, true, false);
	} else if (this.otherRangeMode == GameLogic.OTHER_RANGE_MODE_ICE_BOMBS) {
	    GameLogic.updateUndo(false, false, false, false, false, false, false, false, false, true);
	}
	final AbstractDungeon a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	final int px = this.plMgr.getPlayerLocationX();
	final int py = this.plMgr.getPlayerLocationY();
	final int pz = this.plMgr.getPlayerLocationZ();
	a.circularScanRange(px, py, pz, 1, this.otherRangeMode,
		AbstractDungeonObject.getImbuedRangeForce(RangeTypes.getMaterialForRangeType(this.otherRangeMode)));
	DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().tickTimers(pz, GameActions.NON_MOVE);
	this.updateScoreText();
    }

    public void haltMovingObjects() {
	if (this.mlot != null && this.mlot.isAlive()) {
	    this.mlot.haltMovingObjects();
	}
    }

    void updatePositionRelative(final int x, final int y) {
	if (!this.moving) {
	    this.moving = true;
	    if (this.mlot == null) {
		this.mlot = new MLOTask();
	    } else {
		if (!this.mlot.isAlive()) {
		    this.mlot = new MLOTask();
		}
	    }
	    this.mlot.activateMovement(x, y);
	    if (!this.mlot.isAlive()) {
		this.mlot.start();
	    }
	    if (this.replaying) {
		// Wait
		while (this.moving) {
		    try {
			Thread.sleep(100);
		    } catch (final InterruptedException ie) {
			// Ignore
		    }
		}
	    }
	}
    }

    public void updatePushedPositionLater(final int x, final int y, final int pushX, final int pushY,
	    final AbstractMovableObject o, final int x2, final int y2, final AbstractMovableObject other,
	    final int laserType, final int forceUnits) {
	new Thread() {
	    @Override
	    public void run() {
		try {
		    other.laserEnteredAction(x2, y2, GameLogic.this.plMgr.getPlayerLocationZ(), pushX, pushY, laserType,
			    forceUnits);
		    GameLogic.this.waitForMLOLoop();
		    GameLogic.this.updatePushedPosition(x, y, x + pushX, y + pushY, o);
		    GameLogic.this.waitForMLOLoop();
		} catch (final Throwable t) {
		    DungeonDiver7.logError(t);
		}
	    }
	}.start();
    }

    public synchronized void updatePushedPosition(final int x, final int y, final int pushX, final int pushY,
	    final AbstractMovableObject o) {
	if (this.mlot == null) {
	    this.mlot = new MLOTask();
	} else {
	    if (!this.mlot.isAlive()) {
		this.mlot = new MLOTask();
	    }
	}
	this.mlot.activateObjects(x, y, pushX, pushY, o);
	if (!this.mlot.isAlive()) {
	    this.mlot.start();
	}
    }

    public void updatePushedIntoPositionAbsolute(final int x, final int y, final int z, final int x2, final int y2,
	    final int z2, final AbstractMovableObject pushedInto, final AbstractDungeonObject source) {
	final Party template = new Party(this.plMgr.getActivePlayerNumber() + 1);
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	boolean needsFixup1 = false;
	boolean needsFixup2 = false;
	try {
	    if (!m.getCell(x, y, z, pushedInto.getLayer()).isConditionallySolid()) {
		final AbstractDungeonObject saved = m.getCell(x, y, z, pushedInto.getLayer());
		final AbstractDungeonObject there = m.getCell(x2, y2, z2, pushedInto.getLayer());
		if (there.isOfType(DungeonObjectTypes.TYPE_CHARACTER)) {
		    needsFixup1 = true;
		}
		if (saved.isOfType(DungeonObjectTypes.TYPE_CHARACTER)) {
		    needsFixup2 = true;
		}
		if (needsFixup2) {
		    m.setCell(this.player, x, y, z, template.getLayer());
		    pushedInto.setSavedObject(saved.getSavedObject());
		    this.player.setSavedObject(pushedInto);
		} else {
		    m.setCell(pushedInto, x, y, z, pushedInto.getLayer());
		    pushedInto.setSavedObject(saved);
		}
		if (needsFixup1) {
		    m.setCell(this.player, x2, y2, z2, template.getLayer());
		    this.player.setSavedObject(source);
		} else {
		    m.setCell(source, x2, y2, z2, pushedInto.getLayer());
		}
		app.getDungeonManager().setDirty(true);
	    }
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    final Empty e = new Empty();
	    m.setCell(e, x2, y2, z2, pushedInto.getLayer());
	}
    }

    public void updatePositionAbsoluteNoEvents(final int z) {
	final int x = this.plMgr.getPlayerLocationX();
	final int y = this.plMgr.getPlayerLocationY();
	this.updatePositionAbsoluteNoEvents(x, y, z);
    }

    public void updatePositionAbsoluteNoEvents(final int x, final int y, final int z) {
	final Party template = new Party(this.plMgr.getActivePlayerNumber() + 1);
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	this.plMgr.savePlayerLocation();
	try {
	    if (!m.getCell(x, y, z, template.getLayer()).isConditionallySolid()) {
		if (z != 0) {
		    this.suspendAnimator();
		    m.setDirtyFlags(this.plMgr.getPlayerLocationZ());
		    m.setDirtyFlags(z);
		}
		m.setCell(this.player.getSavedObject(), this.plMgr.getPlayerLocationX(),
			this.plMgr.getPlayerLocationY(), this.plMgr.getPlayerLocationZ(), template.getLayer());
		this.plMgr.setPlayerLocation(x, y, z);
		this.player.setSavedObject(m.getCell(this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(),
			this.plMgr.getPlayerLocationZ(), template.getLayer()));
		m.setCell(this.player, this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(),
			this.plMgr.getPlayerLocationZ(), template.getLayer());
		app.getDungeonManager().setDirty(true);
		if (z != 0) {
		    this.resumeAnimator();
		}
	    }
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    this.plMgr.restorePlayerLocation();
	    m.setCell(this.player, this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(),
		    this.plMgr.getPlayerLocationZ(), template.getLayer());
	    DungeonDiver7.getStuffBag().showMessage(Strings.game(GameString.OUTSIDE_ARENA));
	} catch (final NullPointerException np) {
	    this.plMgr.restorePlayerLocation();
	    m.setCell(this.player, this.plMgr.getPlayerLocationX(), this.plMgr.getPlayerLocationY(),
		    this.plMgr.getPlayerLocationZ(), template.getLayer());
	    DungeonDiver7.getStuffBag().showMessage(Strings.game(GameString.OUTSIDE_ARENA));
	}
    }

    public void showScoreTable() {
	this.st.showScoreTable();
    }

    public void changeOtherAmmoMode() {
	final String choice = CommonDialogs.showInputDialog(Strings.game(GameString.WHICH_AMMO),
		Strings.game(GameString.CHANGE_AMMO), GameLogic.OTHER_AMMO_CHOICES,
		GameLogic.OTHER_AMMO_CHOICES[this.otherAmmoMode]);
	if (choice != null) {
	    for (int z = 0; z < GameLogic.OTHER_AMMO_CHOICES.length; z++) {
		if (choice.equals(GameLogic.OTHER_AMMO_CHOICES[z])) {
		    this.otherAmmoMode = z;
		    break;
		}
	    }
	    this.updateScoreText();
	    CommonDialogs.showDialog(Strings.game(GameString.AMMO_CHANGED) + LocaleConstants.COMMON_STRING_SPACE
		    + GameLogic.OTHER_AMMO_CHOICES[this.otherAmmoMode] + LocaleConstants.COMMON_STRING_NOTL_PERIOD);
	}
    }

    public void changeOtherToolMode() {
	final String choice = CommonDialogs.showInputDialog(Strings.game(GameString.WHICH_TOOL),
		Strings.game(GameString.CHANGE_TOOL), GameLogic.OTHER_TOOL_CHOICES,
		GameLogic.OTHER_TOOL_CHOICES[this.otherToolMode]);
	if (choice != null) {
	    for (int z = 0; z < GameLogic.OTHER_TOOL_CHOICES.length; z++) {
		if (choice.equals(GameLogic.OTHER_TOOL_CHOICES[z])) {
		    this.otherToolMode = z;
		    break;
		}
	    }
	    this.updateScoreText();
	    CommonDialogs.showDialog(Strings.game(GameString.TOOL_CHANGED) + LocaleConstants.COMMON_STRING_SPACE
		    + GameLogic.OTHER_TOOL_CHOICES[this.otherToolMode] + LocaleConstants.COMMON_STRING_NOTL_PERIOD);
	}
    }

    public void changeOtherRangeMode() {
	final String choice = CommonDialogs.showInputDialog(Strings.game(GameString.WHICH_RANGE),
		Strings.game(GameString.CHANGE_RANGE), GameLogic.OTHER_RANGE_CHOICES,
		GameLogic.OTHER_RANGE_CHOICES[this.otherRangeMode]);
	if (choice != null) {
	    for (int z = 0; z < GameLogic.OTHER_RANGE_CHOICES.length; z++) {
		if (choice.equals(GameLogic.OTHER_RANGE_CHOICES[z])) {
		    this.otherRangeMode = z;
		    break;
		}
	    }
	    this.updateScoreText();
	    CommonDialogs.showDialog(Strings.game(GameString.RANGE_CHANGED) + LocaleConstants.COMMON_STRING_SPACE
		    + GameLogic.OTHER_RANGE_CHOICES[this.otherRangeMode] + LocaleConstants.COMMON_STRING_NOTL_PERIOD);
	}
    }

    public void resetCurrentLevel() throws InvalidDungeonException {
	this.resetLevel(true);
    }

    private void resetCurrentLevel(final boolean flag) throws InvalidDungeonException {
	this.resetLevel(flag);
    }

    public void resetGameState() {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	app.getDungeonManager().setDirty(false);
	m.restore();
	this.setSavedGameFlag(false);
	this.st.resetScore();
	final boolean playerExists = m.doesPlayerExist(this.plMgr.getActivePlayerNumber());
	if (playerExists) {
	    this.plMgr.setPlayerLocation(m.getStartColumn(0), m.getStartRow(0), m.getStartFloor(0));
	}
    }

    private void resetLevel(final boolean flag) throws InvalidDungeonException {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	if (flag) {
	    m.resetHistoryEngine();
	}
	app.getDungeonManager().setDirty(true);
	if (this.mlot != null) {
	    if (this.mlot.isAlive()) {
		this.abortMovementLaserObjectLoop();
	    }
	}
	this.moving = false;
	this.shotActive = false;
	PartyInventory.resetInventory();
	m.restore();
	m.setDirtyFlags(this.plMgr.getPlayerLocationZ());
	final boolean playerExists = m.doesPlayerExist(this.plMgr.getActivePlayerNumber());
	if (playerExists) {
	    this.st.resetScore(app.getDungeonManager().getScoresFileName());
	    this.resetPlayerLocation();
	    this.updatePlayer();
	    m.clearVirtualGrid();
	    this.updateScore();
	    this.decay();
	    this.redrawDungeon();
	}
	GameLogic.checkMenus();
    }

    private void processLevelExists() {
	final StuffBag app = DungeonDiver7.getStuffBag();
	try {
	    this.resetPlayerLocation();
	} catch (final InvalidDungeonException iae) {
	    CommonDialogs.showErrorDialog(Strings.error(ErrorString.PLAYER_LOCATION),
		    Strings.untranslated(Untranslated.PROGRAM_NAME));
	    this.exitGame();
	    return;
	}
	this.updatePlayer();
	this.st.resetScore(app.getDungeonManager().getScoresFileName());
	PartyInventory.resetInventory();
	this.scoreMoves.setText(Strings.game(GameString.MOVES) + LocaleConstants.COMMON_STRING_COLON
		+ LocaleConstants.COMMON_STRING_SPACE + LocaleConstants.COMMON_STRING_ZERO);
	this.scoreShots.setText(Strings.game(GameString.SHOTS) + LocaleConstants.COMMON_STRING_COLON
		+ LocaleConstants.COMMON_STRING_SPACE + LocaleConstants.COMMON_STRING_ZERO);
	this.scoreOthers.setText(Strings.game(GameString.OTHERS) + LocaleConstants.COMMON_STRING_COLON
		+ LocaleConstants.COMMON_STRING_SPACE + LocaleConstants.COMMON_STRING_ZERO);
	if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_MISSILES) {
	    if (this.getCheatStatus(GameLogic.CHEAT_MISSILES)) {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.MISSILES)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.MISSILES)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ LocaleConstants.COMMON_STRING_ZERO + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	} else if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_STUNNERS) {
	    if (this.getCheatStatus(GameLogic.CHEAT_STUNNERS)) {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.STUNNERS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.STUNNERS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ LocaleConstants.COMMON_STRING_ZERO + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	} else if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_BLUE_LASERS) {
	    if (this.getCheatStatus(GameLogic.CHEAT_BLUE_LASERS)) {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.BLUE_LASERS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.BLUE_LASERS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ LocaleConstants.COMMON_STRING_ZERO + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	} else if (this.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_DISRUPTORS) {
	    if (this.getCheatStatus(GameLogic.CHEAT_DISRUPTORS)) {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.DISRUPTORS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.INFINITE) + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    } else {
		this.otherAmmoLeft
			.setText(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES + Strings.game(GameString.DISRUPTORS)
				+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
				+ LocaleConstants.COMMON_STRING_ZERO + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	    }
	}
	this.updateInfo();
	this.redrawDungeon();
	this.resumeAnimator();
    }

    public void solvedLevel(final boolean playSound) {
	if (playSound) {
	    SoundLoader.playSound(SoundConstants.END_LEVEL);
	}
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	if (playSound) {
	    if (this.recording) {
		this.writeSolution();
	    }
	    if (this.st.checkScore()) {
		this.st.commitScore();
	    }
	}
	m.resetHistoryEngine();
	this.gre = new GameReplayEngine();
	GameLogic.checkMenus();
	this.suspendAnimator();
	m.restore();
	if (m.doesLevelExistOffset(1)) {
	    m.switchLevelOffset(1);
	    final boolean levelExists = m.switchToNextLevelWithDifficulty(GameGUI.getEnabledDifficulties());
	    if (levelExists) {
		m.setDirtyFlags(this.plMgr.getPlayerLocationZ());
		this.processLevelExists();
	    } else {
		this.solvedDungeon();
	    }
	} else {
	    this.solvedDungeon();
	}
    }

    public void previousLevel() {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	m.resetHistoryEngine();
	this.gre = new GameReplayEngine();
	GameLogic.checkMenus();
	this.suspendAnimator();
	m.restore();
	if (m.doesLevelExistOffset(-1)) {
	    m.switchLevelOffset(-1);
	    final boolean levelExists = m.switchToPreviousLevelWithDifficulty(GameGUI.getEnabledDifficulties());
	    if (levelExists) {
		m.setDirtyFlags(this.plMgr.getPlayerLocationZ());
		this.processLevelExists();
	    } else {
		CommonDialogs.showErrorDialog(Strings.error(ErrorString.NO_PREVIOUS_LEVEL),
			Strings.untranslated(Untranslated.PROGRAM_NAME));
	    }
	} else {
	    CommonDialogs.showErrorDialog(Strings.error(ErrorString.NO_PREVIOUS_LEVEL),
		    Strings.untranslated(Untranslated.PROGRAM_NAME));
	}
    }

    public void loadLevel() {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	final String[] choices = app.getLevelInfoList();
	final String res = CommonDialogs.showInputDialog(Strings.game(GameString.LOAD_LEVEL_PROMPT),
		Strings.game(GameString.LOAD_LEVEL), choices, choices[m.getActiveLevel()]);
	int number = -1;
	for (number = 0; number < m.getLevels(); number++) {
	    if (choices[number].equals(res)) {
		break;
	    }
	}
	if (m.doesLevelExist(number)) {
	    this.suspendAnimator();
	    m.restore();
	    m.switchLevel(number);
	    app.getDungeonManager().getDungeon().setDirtyFlags(this.plMgr.getPlayerLocationZ());
	    m.resetHistoryEngine();
	    this.gre = new GameReplayEngine();
	    GameLogic.checkMenus();
	    this.processLevelExists();
	}
    }

    private void solvedDungeon() {
	PartyInventory.resetInventory();
	this.exitGame();
    }

    public void gameOver() {
	// Check cheats
	if (this.getCheatStatus(GameLogic.CHEAT_INVINCIBLE)) {
	    return;
	}
	// Check dead
	if (this.dead) {
	    // Already dead
	    throw new AlreadyDeadException();
	}
	// We are dead
	this.dead = true;
	// Stop the movement/laser/object loop
	if (this.mlot != null) {
	    if (this.mlot.isAlive()) {
		this.abortMovementLaserObjectLoop();
	    }
	}
	this.mlot = null;
	SoundLoader.playSound(SoundConstants.DEAD);
	final int choice = CustomDialogs.showDeadDialog();
	if (choice == JOptionPane.CANCEL_OPTION) {
	    // End
	    this.exitGame();
	} else if (choice == JOptionPane.YES_OPTION) {
	    // Undo
	    this.undoLastMove();
	} else if (choice == JOptionPane.NO_OPTION) {
	    // Restart
	    try {
		this.resetCurrentLevel();
	    } catch (final InvalidDungeonException iae) {
		CommonDialogs.showErrorDialog(Strings.error(ErrorString.PLAYER_LOCATION),
			Strings.untranslated(Untranslated.PROGRAM_NAME));
		this.exitGame();
		return;
	    }
	} else {
	    // Closed Dialog
	    this.exitGame();
	}
    }

    static void updateUndo(final boolean las, final boolean mis, final boolean stu, final boolean boo,
	    final boolean mag, final boolean blu, final boolean dis, final boolean bom, final boolean hbm,
	    final boolean ibm) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon a = app.getDungeonManager().getDungeon();
	a.updateUndoHistory(new HistoryStatus(las, mis, stu, boo, mag, blu, dis, bom, hbm, ibm));
	GameLogic.checkMenus();
    }

    void updateReplay(final boolean laser, final int x, final int y) {
	this.gre.updateUndoHistory(laser, x, y);
    }

    private static void updateRedo(final boolean las, final boolean mis, final boolean stu, final boolean boo,
	    final boolean mag, final boolean blu, final boolean dis, final boolean bom, final boolean hbm,
	    final boolean ibm) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon a = app.getDungeonManager().getDungeon();
	a.updateRedoHistory(new HistoryStatus(las, mis, stu, boo, mag, blu, dis, bom, hbm, ibm));
	GameLogic.checkMenus();
    }

    public void undoLastMove() {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon a = app.getDungeonManager().getDungeon();
	if (a.tryUndo()) {
	    this.moving = false;
	    this.shotActive = false;
	    a.undo();
	    final boolean laser = a.getWhatWas().wasSomething(HistoryStatus.WAS_LASER);
	    final boolean missile = a.getWhatWas().wasSomething(HistoryStatus.WAS_MISSILE);
	    final boolean stunner = a.getWhatWas().wasSomething(HistoryStatus.WAS_STUNNER);
	    final boolean boost = a.getWhatWas().wasSomething(HistoryStatus.WAS_BOOST);
	    final boolean magnet = a.getWhatWas().wasSomething(HistoryStatus.WAS_MAGNET);
	    final boolean blue = a.getWhatWas().wasSomething(HistoryStatus.WAS_BLUE_LASER);
	    final boolean disrupt = a.getWhatWas().wasSomething(HistoryStatus.WAS_DISRUPTOR);
	    final boolean bomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_BOMB);
	    final boolean heatBomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_HEAT_BOMB);
	    final boolean iceBomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_ICE_BOMB);
	    final boolean other = missile || stunner || boost || magnet || blue || disrupt || bomb || heatBomb
		    || iceBomb;
	    if (other) {
		this.updateScore(0, 0, -1);
		if (boost) {
		    PartyInventory.addOneBoost();
		} else if (magnet) {
		    PartyInventory.addOneMagnet();
		} else if (missile) {
		    PartyInventory.addOneMissile();
		} else if (stunner) {
		    PartyInventory.addOneStunner();
		} else if (blue) {
		    PartyInventory.addOneBlueLaser();
		} else if (disrupt) {
		    PartyInventory.addOneDisruptor();
		} else if (bomb) {
		    PartyInventory.addOneBomb();
		} else if (heatBomb) {
		    PartyInventory.addOneHeatBomb();
		} else if (iceBomb) {
		    PartyInventory.addOneIceBomb();
		}
	    } else if (laser) {
		this.updateScore(0, -1, 0);
	    } else {
		this.updateScore(-1, 0, 0);
	    }
	    try {
		this.resetPlayerLocation();
	    } catch (final InvalidDungeonException iae) {
		CommonDialogs.showErrorDialog(Strings.error(ErrorString.PLAYER_LOCATION),
			Strings.untranslated(Untranslated.PROGRAM_NAME));
		this.exitGame();
		return;
	    }
	    this.updatePlayer();
	    GameLogic.updateRedo(laser, missile, stunner, boost, magnet, blue, disrupt, bomb, heatBomb, iceBomb);
	}
	GameLogic.checkMenus();
	this.updateScoreText();
	a.setDirtyFlags(this.plMgr.getPlayerLocationZ());
	this.redrawDungeon();
    }

    public void redoLastMove() {
	final AbstractDungeon a = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	if (a.tryRedo()) {
	    this.moving = false;
	    this.shotActive = false;
	    a.redo();
	    final boolean laser = a.getWhatWas().wasSomething(HistoryStatus.WAS_LASER);
	    final boolean missile = a.getWhatWas().wasSomething(HistoryStatus.WAS_MISSILE);
	    final boolean stunner = a.getWhatWas().wasSomething(HistoryStatus.WAS_STUNNER);
	    final boolean boost = a.getWhatWas().wasSomething(HistoryStatus.WAS_BOOST);
	    final boolean magnet = a.getWhatWas().wasSomething(HistoryStatus.WAS_MAGNET);
	    final boolean blue = a.getWhatWas().wasSomething(HistoryStatus.WAS_BLUE_LASER);
	    final boolean disrupt = a.getWhatWas().wasSomething(HistoryStatus.WAS_DISRUPTOR);
	    final boolean bomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_BOMB);
	    final boolean heatBomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_HEAT_BOMB);
	    final boolean iceBomb = a.getWhatWas().wasSomething(HistoryStatus.WAS_ICE_BOMB);
	    final boolean other = missile || stunner || boost || magnet || blue || disrupt || bomb || heatBomb
		    || iceBomb;
	    if (other) {
		this.updateScore(0, 0, -1);
		if (boost) {
		    PartyInventory.fireBoost();
		} else if (magnet) {
		    PartyInventory.fireMagnet();
		} else if (missile) {
		    PartyInventory.fireMissile();
		} else if (stunner) {
		    PartyInventory.fireStunner();
		} else if (blue) {
		    PartyInventory.fireBlueLaser();
		} else if (disrupt) {
		    PartyInventory.fireDisruptor();
		} else if (bomb) {
		    PartyInventory.fireBomb();
		} else if (heatBomb) {
		    PartyInventory.fireHeatBomb();
		} else if (iceBomb) {
		    PartyInventory.fireIceBomb();
		}
	    } else if (laser && !other) {
		this.updateScore(0, 1, 0);
	    } else {
		this.updateScore(1, 0, 0);
	    }
	    try {
		this.resetPlayerLocation();
	    } catch (final InvalidDungeonException iae) {
		CommonDialogs.showErrorDialog(Strings.error(ErrorString.PLAYER_LOCATION),
			Strings.untranslated(Untranslated.PROGRAM_NAME));
		this.exitGame();
		return;
	    }
	    this.updatePlayer();
	    GameLogic.updateUndo(laser, missile, stunner, boost, magnet, blue, disrupt, bomb, heatBomb, iceBomb);
	}
	GameLogic.checkMenus();
	this.updateScoreText();
	a.setDirtyFlags(this.plMgr.getPlayerLocationZ());
	this.redrawDungeon();
    }

    boolean replayLastMove() {
	if (this.gre.tryRedo()) {
	    this.gre.redo();
	    final boolean laser = this.gre.wasLaser();
	    final int x = this.gre.getX();
	    final int y = this.gre.getY();
	    final int px = this.plMgr.getPlayerLocationX();
	    final int py = this.plMgr.getPlayerLocationY();
	    if (laser) {
		this.fireLaser(px, py, this.player);
	    } else {
		final Directions currDir = this.player.getDirection();
		final Directions newDir = DirectionResolver.resolve(x, y);
		if (currDir != newDir) {
		    this.player.setDirection(newDir);
		    SoundLoader.playSound(SoundConstants.TURN);
		    this.redrawDungeon();
		} else {
		    this.updatePositionRelative(x, y);
		}
	    }
	    return true;
	}
	return false;
    }

    public void decay() {
	if (this.player != null) {
	    this.player.setSavedObject(new Empty());
	}
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	m.setCell(new Empty(), m.getPlayerLocationX(0), m.getPlayerLocationY(0), 0,
		DungeonConstants.LAYER_LOWER_OBJECTS);
    }

    void doDelayedDecay() {
	this.player.setSavedObject(this.delayedDecayObject);
	this.delayedDecayActive = false;
    }

    void doRemoteDelayedDecay(final AbstractMovableObject o) {
	o.setSavedObject(this.delayedDecayObject);
	this.remoteDecay = false;
	this.delayedDecayActive = false;
    }

    public void remoteDelayedDecayTo(final AbstractDungeonObject obj) {
	this.delayedDecayActive = true;
	this.delayedDecayObject = obj;
	this.remoteDecay = true;
    }

    private void disableRecording() {
	this.gui.disableRecording();
    }

    public boolean newGame() {
	final JFrame owner = DungeonDiver7.getStuffBag().getOutputFrame();
	boolean guiResult = this.gui.newGame();
	if (guiResult) {
	    if (this.savedGameFlag) {
		if (PartyManager.getParty() != null) {
		    return true;
		} else {
		    return PartyManager.createParty(owner);
		}
	    } else {
		return PartyManager.createParty(owner);
	    }
	} else {
	    // User cancelled
	    return false;
	}
    }

    void scheduleAutoMove() {
	this.autoMove = true;
    }

    void unscheduleAutoMove() {
	this.autoMove = false;
    }

    boolean isAutoMoveScheduled() {
	return this.autoMove;
    }

    boolean getCheatStatus(final int cheatID) {
	return this.cheatStatus[cheatID];
    }

    public void enableEvents() {
	this.mt.fireStepActions();
	this.gui.enableEvents();
    }

    public void disableEvents() {
	this.gui.disableEvents();
    }

    public void stopMovement() {
	this.mt.stopMovement();
    }

    public void viewingWindowSizeChanged() {
	this.gui.viewingWindowSizeChanged();
	this.resetViewingWindow();
    }

    public void stateChanged() {
	this.stateChanged = true;
    }

    public GameViewingWindowManager getViewManager() {
	return this.vwMgr;
    }

    public void setSavedGameFlag(final boolean value) {
	this.savedGameFlag = value;
    }

    public void setStatusMessage(final String msg) {
	this.gui.setStatusMessage(msg);
    }

    public void updatePositionAbsolute(final int x, final int y) {
	this.mt.moveAbsolute(x, y);
    }

    public void redrawDungeon() {
	this.gui.redrawDungeon();
    }

    public void resetViewingWindowAndPlayerLocation() {
	GameLogic.resetPlayerLocation(0);
	this.resetViewingWindow();
    }

    public void resetViewingWindow() {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	if (m != null && this.vwMgr != null) {
	    this.vwMgr.setViewingWindowLocationX(m.getPlayerLocationY(0) - GameViewingWindowManager.getOffsetFactorX());
	    this.vwMgr.setViewingWindowLocationY(m.getPlayerLocationX(0) - GameViewingWindowManager.getOffsetFactorY());
	}
    }

    public void resetPlayerLocation() throws InvalidDungeonException {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	final int[] found = m.findPlayer(1);
	if (found == null) {
	    throw new InvalidDungeonException(Strings.error(ErrorString.PLAYER_LOCATION));
	}
	this.plMgr.setPlayerLocation(found[0], found[1], found[2]);
    }

    public static void resetPlayerLocation(final int level) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	if (m != null) {
	    m.switchLevel(level);
	    m.setPlayerToStart();
	}
    }

    public void goToLevelOffset(final int level) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	final boolean levelExists = m.doesLevelExistOffset(level);
	this.stopMovement();
	if (levelExists) {
	    new LevelLoadTask(level).start();
	} else {
	    new GenerateDungeonTask(false).start();
	}
    }

    public void exitGame() {
	// Stop music
	MusicLoader.stopExternalMusic();
	// Halt the animator
	if (this.animator != null) {
	    this.animator.stopAnimator();
	    this.animator = null;
	}
	// Halt the movement/laser processor
	if (this.mlot != null) {
	    this.abortMovementLaserObjectLoop();
	}
	this.mlot = null;
	this.stateChanged = true;
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	// Restore the dungeon
	m.restore();
	m.resetVisibleSquares(0);
	final boolean playerExists = m.doesPlayerExist(this.plMgr.getActivePlayerNumber());
	if (playerExists) {
	    this.resetViewingWindowAndPlayerLocation();
	} else {
	    app.getDungeonManager().setLoaded(false);
	}
	// Reset saved game flag
	this.savedGameFlag = false;
	app.getDungeonManager().setDirty(false);
	// Exit game
	this.hideOutput();
	app.getGUIManager().showGUI();
    }

    void identifyObject(final int x, final int y) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	final int destX = x / ImageLoader.getGraphicSize();
	final int destY = y / ImageLoader.getGraphicSize();
	final int destZ = this.plMgr.getPlayerLocationZ();
	final AbstractDungeonObject target = m.getCell(destX, destY, destZ, DungeonConstants.LAYER_LOWER_OBJECTS);
	target.determineCurrentAppearance(destX, destY, destZ);
	final String gameName = target.getIdentityName();
	final String desc = target.getDescription();
	CommonDialogs.showTitledDialog(desc, gameName);
    }

    public void loadGameHookG1(final FileIOReader dungeonFile) throws IOException {
	final StuffBag app = DungeonDiver7.getStuffBag();
	app.getDungeonManager().setScoresFileName(dungeonFile.readString());
	this.st.setMoves(dungeonFile.readLong());
	this.st.setShots(dungeonFile.readLong());
	this.st.setOthers(dungeonFile.readLong());
    }

    public void loadGameHookG2(final FileIOReader dungeonFile) throws IOException {
	final StuffBag app = DungeonDiver7.getStuffBag();
	app.getDungeonManager().setScoresFileName(dungeonFile.readString());
	this.st.setMoves(dungeonFile.readLong());
	this.st.setShots(dungeonFile.readLong());
	this.st.setOthers(dungeonFile.readLong());
	PartyInventory.setRedKeysLeft(dungeonFile.readInt());
	PartyInventory.setGreenKeysLeft(dungeonFile.readInt());
	PartyInventory.setBlueKeysLeft(dungeonFile.readInt());
    }

    public void loadGameHookG3(final FileIOReader dungeonFile) throws IOException {
	final StuffBag app = DungeonDiver7.getStuffBag();
	app.getDungeonManager().setScoresFileName(dungeonFile.readString());
	this.st.setMoves(dungeonFile.readLong());
	this.st.setShots(dungeonFile.readLong());
	this.st.setOthers(dungeonFile.readLong());
	PartyInventory.readInventory(dungeonFile);
    }

    public void loadGameHookG4(final FileIOReader dungeonFile) throws IOException {
	final StuffBag app = DungeonDiver7.getStuffBag();
	app.getDungeonManager().setScoresFileName(dungeonFile.readString());
	this.st.setMoves(dungeonFile.readLong());
	this.st.setShots(dungeonFile.readLong());
	this.st.setOthers(dungeonFile.readLong());
	PartyInventory.readInventory(dungeonFile);
    }

    public void loadGameHookG5(final FileIOReader dungeonFile) throws IOException {
	final StuffBag app = DungeonDiver7.getStuffBag();
	app.getDungeonManager().setScoresFileName(dungeonFile.readString());
	this.st.setMoves(dungeonFile.readLong());
	this.st.setShots(dungeonFile.readLong());
	this.st.setOthers(dungeonFile.readLong());
	PartyInventory.readInventory(dungeonFile);
    }

    public void loadGameHookG6(final FileIOReader dungeonFile) throws IOException {
	final StuffBag app = DungeonDiver7.getStuffBag();
	app.getDungeonManager().setScoresFileName(dungeonFile.readString());
	this.st.setMoves(dungeonFile.readLong());
	this.st.setShots(dungeonFile.readLong());
	this.st.setOthers(dungeonFile.readLong());
	PartyInventory.readInventory(dungeonFile);
    }

    public void saveGameHook(final FileIOWriter dungeonFile) throws IOException {
	final StuffBag app = DungeonDiver7.getStuffBag();
	dungeonFile.writeString(app.getDungeonManager().getScoresFileName());
	dungeonFile.writeLong(this.st.getMoves());
	dungeonFile.writeLong(this.st.getShots());
	dungeonFile.writeLong(this.st.getOthers());
	PartyInventory.writeInventory(dungeonFile);
    }

    public void toggleRecording() {
	this.recording = !this.recording;
    }

    public void replaySolution() {
	if (this.lpbLoaded) {
	    this.replaying = true;
	    // Turn recording off
	    this.recording = false;
	    this.disableRecording();
	    try {
		this.resetCurrentLevel(false);
	    } catch (final InvalidDungeonException iae) {
		CommonDialogs.showErrorDialog(Strings.error(ErrorString.PLAYER_LOCATION),
			Strings.untranslated(Untranslated.PROGRAM_NAME));
		this.exitGame();
		return;
	    }
	    final ReplayTask rt = new ReplayTask();
	    rt.start();
	} else {
	    final boolean success = this.readSolution();
	    if (!success) {
		CommonDialogs.showErrorDialog(Strings.error(ErrorString.NO_SOLUTION_FILE),
			Strings.untranslated(Untranslated.PROGRAM_NAME));
	    } else {
		this.replaying = true;
		// Turn recording off
		this.recording = false;
		this.disableRecording();
		try {
		    this.resetCurrentLevel(false);
		} catch (final InvalidDungeonException iae) {
		    CommonDialogs.showErrorDialog(Strings.error(ErrorString.PLAYER_LOCATION),
			    Strings.untranslated(Untranslated.PROGRAM_NAME));
		    this.exitGame();
		    return;
		}
		final ReplayTask rt = new ReplayTask();
		rt.start();
	    }
	}
    }

    private boolean readSolution() {
	try {
	    final int activeLevel = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getActiveLevel();
	    final String levelFile = DungeonDiver7.getStuffBag().getDungeonManager().getLastUsedDungeon();
	    final String filename = levelFile + LocaleConstants.COMMON_STRING_UNDERSCORE + activeLevel
		    + FileExtensions.getSolutionExtensionWithPeriod();
	    try (XDataReader file = new XDataReader(filename,
		    LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_SOLUTION))) {
		this.gre = GameReplayEngine.readReplay(file);
	    }
	    return true;
	} catch (final IOException ioe) {
	    return false;
	}
    }

    private void writeSolution() {
	try {
	    final int activeLevel = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getActiveLevel();
	    final String levelFile = DungeonDiver7.getStuffBag().getDungeonManager().getLastUsedDungeon();
	    final String filename = levelFile + LocaleConstants.COMMON_STRING_UNDERSCORE + activeLevel
		    + FileExtensions.getSolutionExtensionWithPeriod();
	    try (XDataWriter file = new XDataWriter(filename,
		    LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_SOLUTION))) {
		this.gre.writeReplay(file);
	    }
	} catch (final IOException ioe) {
	    // Ignore
	}
    }

    public JFrame getOutputFrame() {
	return this.gui.getOutputFrame();
    }

    public static void morph(final AbstractDungeonObject morphInto) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	m.setCell(morphInto, m.getPlayerLocationX(0), m.getPlayerLocationY(0), 0, morphInto.getLayer());
    }

    public static void morph(final AbstractDungeonObject morphInto, final int x, final int y, final int z,
	    final int w) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	try {
	    m.setCell(morphInto, x, y, z, w);
	    app.getDungeonManager().setDirty(true);
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    // Do nothing
	} catch (final NullPointerException np) {
	    // Do nothing
	}
    }

    public void keepNextMessage() {
	this.gui.keepNextMessage();
    }

    public void playDungeon() {
	final StuffBag app = DungeonDiver7.getStuffBag();
	final AbstractDungeon m = app.getDungeonManager().getDungeon();
	if (app.getDungeonManager().getLoaded()) {
	    this.gui.initViewManager();
	    app.getGUIManager().hideGUI();
	    if (this.stateChanged) {
		// Initialize only if the maze state has changed
		app.getDungeonManager().getDungeon().switchLevel(app.getDungeonManager().getDungeon().getStartLevel(0));
		this.stateChanged = false;
	    }
	    app.setInGame();
	    app.getDungeonManager().getDungeon().switchLevel(0);
	    final boolean res = app.getDungeonManager().getDungeon()
		    .switchToNextLevelWithDifficulty(GameGUI.getEnabledDifficulties());
	    if (res) {
		try {
		    this.resetPlayerLocation();
		} catch (final InvalidDungeonException iae) {
		    CommonDialogs.showErrorDialog(Strings.error(ErrorString.PLAYER_LOCATION),
			    Strings.untranslated(Untranslated.PROGRAM_NAME));
		    this.exitGame();
		    return;
		}
		this.updatePlayer();
		this.player.setSavedObject(new Empty());
		this.st.setScoreFile(app.getDungeonManager().getScoresFileName());
		if (!this.savedGameFlag) {
		    this.st.resetScore(app.getDungeonManager().getScoresFileName());
		}
		this.updateInfo();
		// Make sure message area is attached to the border pane
		this.gui.updateGameGUI();
		// Make sure initial area player is in is visible
		final int px = m.getPlayerLocationX(0);
		final int py = m.getPlayerLocationY(0);
		m.updateVisibleSquares(px, py, 0);
		this.showOutput();
		// Start music
		if (PrefsManager.getMusicEnabled()) {
		    MusicLoader.playExternalMusic();
		}
		app.getDungeonManager().getDungeon().setDirtyFlags(this.plMgr.getPlayerLocationZ());
		this.updateScoreText();
		this.showOutput();
		this.redrawDungeon();
		this.replaying = false;
		// Start animator, if enabled
		if (PrefsManager.enableAnimation()) {
		    this.animator = new AnimationTask();
		    this.animator.start();
		}
	    } else {
		CommonDialogs.showDialog(Strings.game(GameString.NO_LEVEL_WITH_DIFFICULTY));
		DungeonDiver7.getStuffBag().getGUIManager().showGUI();
	    }
	} else {
	    CommonDialogs.showDialog(Strings.menu(Menu.ERROR_NO_DUNGEON_OPENED));
	}
    }

    public void showOutput() {
	DungeonDiver7.getStuffBag().setMode(StuffBag.STATUS_GAME);
	this.gui.showOutput();
    }

    public void showOutputAndKeepMusic() {
	DungeonDiver7.getStuffBag().setMode(StuffBag.STATUS_GAME);
	this.gui.showOutputAndKeepMusic();
    }

    public void hideOutput() {
	this.stopMovement();
	this.gui.hideOutput();
    }

    @Override
    public void enableModeCommands() {
	this.gui.enableModeCommands();
    }

    @Override
    public void disableModeCommands() {
	this.gui.disableModeCommands();
    }

    @Override
    public void setInitialState() {
	this.gui.setInitialState();
    }

    @Override
    public JMenu createCommandsMenu() {
	return this.gui.createCommandsMenu();
    }

    @Override
    public void attachAccelerators(Accelerators accel) {
	this.gui.attachAccelerators(accel);
    }

    @Override
    public void enableLoadedCommands() {
	this.gui.enableLoadedCommands();
    }

    @Override
    public void disableLoadedCommands() {
	this.gui.disableLoadedCommands();
    }

    @Override
    public void enableDirtyCommands() {
	this.gui.enableDirtyCommands();
    }

    @Override
    public void disableDirtyCommands() {
	this.gui.disableDirtyCommands();
    }
}
