/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.diane.gui.DrawGrid;
import com.puttysoftware.dungeondiver7.Accelerators;
import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.objects.Darkness;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.dungeon.objects.Player;
import com.puttysoftware.dungeondiver7.dungeon.objects.Wall;
import com.puttysoftware.dungeondiver7.editor.DungeonEditor;
import com.puttysoftware.dungeondiver7.game.replay.ReplayManager;
import com.puttysoftware.dungeondiver7.loader.ImageCompositor;
import com.puttysoftware.dungeondiver7.loader.ImageLoader;
import com.puttysoftware.dungeondiver7.loader.MusicConstants;
import com.puttysoftware.dungeondiver7.loader.MusicLoader;
import com.puttysoftware.dungeondiver7.loader.ObjectImageManager;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.manager.dungeon.DungeonManager;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utility.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utility.DifficultyConstants;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.DirectionResolver;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.PartyInventory;
import com.puttysoftware.dungeondiver7.utility.RCLGenerator;
import com.puttysoftware.images.BufferedImageIcon;

class GameGUI {
    // Fields
    private JFrame outputFrame;
    private Container borderPane, scorePane, infoPane, outerOutputPane;
    private JLabel messageLabel;
    private JLabel scoreMoves;
    private JLabel scoreShots;
    private JLabel scoreOthers;
    private JLabel otherAmmoLeft;
    private JLabel otherToolsLeft;
    private JLabel otherRangesLeft;
    private JLabel levelInfo;
    private GameViewingWindowManager vwMgr = null;
    private final StatGUI sg;
    private DrawGrid drawGrid;
    private GameDraw outputPane;
    private boolean knm;
    private boolean deferredRedraw;
    boolean eventFlag;
    private boolean newGameResult;
    private JDialog difficultyFrame;
    private JList<String> difficultyList;
    private static Darkness DARK = new Darkness();
    private JMenu gameTimeTravelSubMenu;
    JCheckBoxMenuItem gameEraDistantPast, gameEraPast, gameEraPresent, gameEraFuture, gameEraDistantFuture;
    private JMenuItem gameReset, gameShowTable, gameReplaySolution, gameLoadLPB, gamePreviousLevel, gameSkipLevel,
	    gameLoadLevel, gameShowHint, gameCheats, gameChangeOtherAmmoMode, gameChangeOtherToolMode,
	    gameChangeOtherRangeMode;
    private JCheckBoxMenuItem gameRecordSolution;

    // Constructors
    public GameGUI() {
	this.deferredRedraw = false;
	this.eventFlag = true;
	this.newGameResult = false;
	this.sg = new StatGUI();
    }

    // Methods
    public void activeLanguageChanged() {
	this.setUpDifficultyDialog();
    }

    boolean newGame() {
	DungeonDiver7.getStuffBag().getObjects().enableAllObjects();
	this.difficultyList.clearSelection();
	final int[] retVal = GameGUI.getEnabledDifficulties();
	this.difficultyList.setSelectedIndices(retVal);
	this.difficultyFrame.setVisible(true);
	return this.newGameResult;
    }

    static int[] getEnabledDifficulties() {
	final ArrayList<Integer> temp = new ArrayList<>();
	if (PrefsManager.isKidsDifficultyEnabled()) {
	    temp.add(Integer.valueOf(DifficultyConstants.DIFFICULTY_KIDS - 1));
	}
	if (PrefsManager.isEasyDifficultyEnabled()) {
	    temp.add(Integer.valueOf(DifficultyConstants.DIFFICULTY_EASY - 1));
	}
	if (PrefsManager.isMediumDifficultyEnabled()) {
	    temp.add(Integer.valueOf(DifficultyConstants.DIFFICULTY_MEDIUM - 1));
	}
	if (PrefsManager.isHardDifficultyEnabled()) {
	    temp.add(Integer.valueOf(DifficultyConstants.DIFFICULTY_HARD - 1));
	}
	if (PrefsManager.isDeadlyDifficultyEnabled()) {
	    temp.add(Integer.valueOf(DifficultyConstants.DIFFICULTY_DEADLY - 1));
	}
	final Integer[] temp2 = temp.toArray(new Integer[temp.size()]);
	final int[] retVal = new int[temp2.length];
	for (int x = 0; x < temp2.length; x++) {
	    retVal[x] = temp2[x].intValue();
	}
	return retVal;
    }

    public void updateStats() {
	this.sg.updateStats();
    }

    public void enableEvents() {
	this.outputFrame.setEnabled(true);
	this.eventFlag = true;
    }

    public void disableEvents() {
	this.outputFrame.setEnabled(false);
	this.eventFlag = false;
    }

    void disableRecording() {
	this.gameRecordSolution.setSelected(false);
    }

    void initViewManager() {
	if (this.vwMgr == null) {
	    this.vwMgr = DungeonDiver7.getStuffBag().getGameLogic().getViewManager();
	    this.setUpGUI();
	}
    }

    void viewingWindowSizeChanged() {
	this.setUpGUI();
	this.updateGameGUI();
	this.deferredRedraw = true;
    }

    public JFrame getOutputFrame() {
	return this.outputFrame;
    }

    public void showOutput() {
	if (MusicLoader.isMusicPlaying()) {
	    MusicLoader.stopMusic();
	}
	int zoneID = PartyManager.getParty().getZone();
	if (zoneID == AbstractDungeon.getMaxLevels() - 1) {
	    MusicLoader.playMusic(MusicConstants.MUSIC_LAIR);
	} else {
	    MusicLoader.playMusic(MusicConstants.MUSIC_DUNGEON);
	}
	this.showOutputCommon();
    }

    public void showOutputAndKeepMusic() {
	this.showOutputCommon();
    }

    private void showOutputCommon() {
	final StuffBag app = DungeonDiver7.getStuffBag();
	if (!this.outputFrame.isVisible()) {
	    this.outputFrame.setVisible(true);
	    this.outputFrame.setJMenuBar(app.getMenuManager().getMainMenuBar());
	    if (this.deferredRedraw) {
		this.deferredRedraw = false;
		this.redrawDungeon();
	    }
	    app.getMenuManager().checkFlags();
	    GameGUI.checkMenus();
	    this.updateStats();
	}
    }

    public void hideOutput() {
	if (this.outputFrame != null) {
	    this.outputFrame.setVisible(false);
	}
    }

    public void setStatusMessage(final String msg) {
	this.messageLabel.setText(msg);
    }

    private void resetBorderPane() {
	this.borderPane.removeAll();
	this.borderPane.add(this.outputPane, BorderLayout.CENTER);
	this.borderPane.add(this.messageLabel, BorderLayout.NORTH);
	this.borderPane.add(this.sg.getStatsPane(), BorderLayout.EAST);
    }

    public void redrawDungeon() {
	// Draw the maze, if it is visible
	if (this.outputFrame.isVisible()) {
	    final StuffBag app = DungeonDiver7.getStuffBag();
	    final AbstractDungeon m = app.getDungeonManager().getDungeon();
	    int x, y, u, v;
	    int xFix, yFix;
	    boolean visible;
	    u = m.getPlayerLocationX(0);
	    v = m.getPlayerLocationY(0);
	    final AbstractDungeonObject wall = new Wall();
	    for (x = this.vwMgr.getViewingWindowLocationX(); x <= this.vwMgr
		    .getLowerRightViewingWindowLocationX(); x++) {
		for (y = this.vwMgr.getViewingWindowLocationY(); y <= this.vwMgr
			.getLowerRightViewingWindowLocationY(); y++) {
		    xFix = x - this.vwMgr.getViewingWindowLocationX();
		    yFix = y - this.vwMgr.getViewingWindowLocationY();
		    visible = app.getDungeonManager().getDungeon().isSquareVisible(u, v, y, x, 0);
		    try {
			if (visible) {
			    final AbstractDungeonObject obj1 = m.getCell(y, x, 0, DungeonConstants.LAYER_LOWER_GROUND);
			    final AbstractDungeonObject obj2 = m.getCell(y, x, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
			    final BufferedImageIcon img1 = obj1.gameRenderHook(y, x);
			    final BufferedImageIcon img2 = obj2.gameRenderHook(y, x);
			    if (u == y && v == x) {
				final AbstractDungeonObject obj3 = new Player();
				final BufferedImageIcon img3 = obj3.gameRenderHook(y, x);
				this.drawGrid.setImageCell(ImageCompositor.getVirtualCompositeImage(img1, img2, img3,
					ImageCompositor.getGraphicSize()), xFix, yFix);
			    } else {
				this.drawGrid.setImageCell(
					ImageCompositor.getCompositeImage(img1, img2, ImageCompositor.getGraphicSize()),
					xFix, yFix);
			    }
			} else {
			    this.drawGrid.setImageCell(
				    ObjectImageManager.getImage(GameGUI.DARK.getName(), GameGUI.DARK.getBaseID()), xFix,
				    yFix);
			}
		    } catch (final ArrayIndexOutOfBoundsException ae) {
			this.drawGrid.setImageCell(wall.gameRenderHook(y, x), xFix, yFix);
		    }
		}
	    }
	    if (this.knm) {
		this.knm = false;
	    } else {
		this.setStatusMessage(" ");
	    }
	    this.outputPane.repaint();
	    this.outputFrame.pack();
	    this.showOutputAndKeepMusic();
	}
    }

    public void keepNextMessage() {
	this.knm = true;
    }

    void updateGameGUI() {
	this.resetBorderPane();
	this.sg.updateImages();
	this.sg.updateStats();
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

    private void setUpGUI() {
	final EventHandler handler = new EventHandler();
	this.borderPane = new Container();
	this.borderPane.setLayout(new BorderLayout());
	this.messageLabel = new JLabel(" ");
	this.messageLabel.setOpaque(true);
	this.outputFrame = new JFrame("Chrystalz");
	final Image iconlogo = StuffBag.getIconLogo();
	this.outputFrame.setIconImage(iconlogo);
	this.drawGrid = new DrawGrid(PrefsManager.getViewingWindowSize());
	this.outputPane = new GameDraw(this.drawGrid);
	this.outputFrame.setContentPane(this.borderPane);
	this.outputFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.outputFrame.setResizable(false);
	this.outputFrame.addKeyListener(handler);
	this.outputFrame.addWindowListener(handler);
	
	// Pasted code
	final FocusHandler fHandler = new FocusHandler();
	this.borderPane = new Container();
	this.borderPane.setLayout(new BorderLayout());
	this.outputFrame = new JFrame(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_PROGRAM_NAME));
	this.outputFrame.setIconImage(iconlogo);
	this.outerOutputPane = RCLGenerator.generateRowColumnLabels();
	this.outputPane = new GameDraw();
	this.outputFrame.setContentPane(this.borderPane);
	this.outputFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.outputPane.setLayout(new GridLayout(GameViewingWindowManager.getFixedViewingWindowSizeX(),
		GameViewingWindowManager.getFixedViewingWindowSizeY()));
	this.outputFrame.setResizable(false);
	this.outputFrame.addKeyListener(handler);
	this.outputFrame.addWindowListener(handler);
	this.outputFrame.addWindowFocusListener(fHandler);
	this.outputPane.addMouseListener(handler);
	this.scoreMoves = new JLabel(
		LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE, LocaleConstants.GAME_STRING_MOVES)
			+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
			+ LocaleConstants.COMMON_STRING_ZERO);
	this.scoreShots = new JLabel(
		LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE, LocaleConstants.GAME_STRING_SHOTS)
			+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
			+ LocaleConstants.COMMON_STRING_ZERO);
	this.scoreOthers = new JLabel(
		LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE, LocaleConstants.GAME_STRING_OTHERS)
			+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
			+ LocaleConstants.COMMON_STRING_ZERO);
	this.otherAmmoLeft = new JLabel(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES
		+ LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE, LocaleConstants.GAME_STRING_MISSILES)
		+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
		+ LocaleConstants.COMMON_STRING_ZERO + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	this.otherToolsLeft = new JLabel(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES
		+ LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE, LocaleConstants.GAME_STRING_BOOSTS)
		+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
		+ LocaleConstants.COMMON_STRING_ZERO + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	this.otherRangesLeft = new JLabel(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES
		+ LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE, LocaleConstants.GAME_STRING_BOMBS)
		+ LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE
		+ LocaleConstants.COMMON_STRING_ZERO + LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	this.scorePane = new Container();
	this.scorePane.setLayout(new FlowLayout());
	this.scorePane.add(this.scoreMoves);
	this.scorePane.add(this.scoreShots);
	this.scorePane.add(this.scoreOthers);
	this.scorePane.add(this.otherAmmoLeft);
	this.scorePane.add(this.otherToolsLeft);
	this.scorePane.add(this.otherRangesLeft);
	this.levelInfo = new JLabel(LocaleConstants.COMMON_STRING_SPACE);
	this.infoPane = new Container();
	this.infoPane.setLayout(new FlowLayout());
	this.infoPane.add(this.levelInfo);
	this.scoreMoves.setLabelFor(this.outputPane);
	this.scoreShots.setLabelFor(this.outputPane);
	this.scoreOthers.setLabelFor(this.outputPane);
	this.otherAmmoLeft.setLabelFor(this.outputPane);
	this.otherToolsLeft.setLabelFor(this.outputPane);
	this.otherRangesLeft.setLabelFor(this.outputPane);
	this.levelInfo.setLabelFor(this.outputPane);
	this.outerOutputPane.add(this.outputPane, BorderLayout.CENTER);
	this.borderPane.add(this.outerOutputPane, BorderLayout.CENTER);
	this.borderPane.add(this.scorePane, BorderLayout.NORTH);
	this.borderPane.add(this.infoPane, BorderLayout.SOUTH);
	this.setUpDifficultyDialog();
    }

    private void setUpDifficultyDialog() {
	// Set up Difficulty Dialog
	final DifficultyEventHandler dhandler = new DifficultyEventHandler();
	this.difficultyFrame = new JDialog(DungeonDiver7.getStuffBag().getOutputFrame(), LocaleLoader
		.loadString(LocaleConstants.GAME_STRINGS_FILE, LocaleConstants.GAME_STRING_SELECT_DIFFICULTY));
	final Container difficultyPane = new Container();
	final Container listPane = new Container();
	final Container buttonPane = new Container();
	this.difficultyList = new JList<>(DifficultyConstants.getDifficultyNames());
	final JButton okButton = new JButton(
		LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE, LocaleConstants.DIALOG_STRING_OK_BUTTON));
	final JButton cancelButton = new JButton(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
		LocaleConstants.DIALOG_STRING_CANCEL_BUTTON));
	buttonPane.setLayout(new FlowLayout());
	buttonPane.add(okButton);
	buttonPane.add(cancelButton);
	listPane.setLayout(new FlowLayout());
	listPane.add(this.difficultyList);
	difficultyPane.setLayout(new BorderLayout());
	difficultyPane.add(listPane, BorderLayout.CENTER);
	difficultyPane.add(buttonPane, BorderLayout.SOUTH);
	this.difficultyFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.difficultyFrame.setModal(true);
	this.difficultyFrame.setResizable(false);
	okButton.setDefaultCapable(true);
	cancelButton.setDefaultCapable(false);
	this.difficultyFrame.getRootPane().setDefaultButton(okButton);
	this.difficultyFrame.addWindowListener(dhandler);
	okButton.addActionListener(dhandler);
	cancelButton.addActionListener(dhandler);
	this.difficultyFrame.setContentPane(difficultyPane);
	this.difficultyFrame.pack();
    }

    void difficultyDialogCancelButtonClicked() {
	this.difficultyFrame.setVisible(false);
	this.newGameResult = false;
    }

    void difficultyDialogOKButtonClicked() {
	this.difficultyFrame.setVisible(false);
	if (this.difficultyList.isSelectedIndex(DifficultyConstants.DIFFICULTY_KIDS - 1)) {
	    PrefsManager.setKidsDifficultyEnabled(true);
	} else {
	    PrefsManager.setKidsDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(DifficultyConstants.DIFFICULTY_EASY - 1)) {
	    PrefsManager.setEasyDifficultyEnabled(true);
	} else {
	    PrefsManager.setEasyDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(DifficultyConstants.DIFFICULTY_MEDIUM - 1)) {
	    PrefsManager.setMediumDifficultyEnabled(true);
	} else {
	    PrefsManager.setMediumDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(DifficultyConstants.DIFFICULTY_HARD - 1)) {
	    PrefsManager.setHardDifficultyEnabled(true);
	} else {
	    PrefsManager.setHardDifficultyEnabled(false);
	}
	if (this.difficultyList.isSelectedIndex(DifficultyConstants.DIFFICULTY_DEADLY - 1)) {
	    PrefsManager.setDeadlyDifficultyEnabled(true);
	} else {
	    PrefsManager.setDeadlyDifficultyEnabled(false);
	}
	this.newGameResult = true;
    }

    public void attachMenus() {
	final StuffBag app = DungeonDiver7.getStuffBag();
	this.outputFrame.setJMenuBar(app.getMenuManager().getMainMenuBar());
	app.getMenuManager().checkFlags();
    }

    public void enableModeCommands() {
	this.gameReset.setEnabled(true);
	this.gameShowTable.setEnabled(true);
	this.gameReplaySolution.setEnabled(true);
	this.gameRecordSolution.setEnabled(true);
	this.gameLoadLPB.setEnabled(true);
	this.gamePreviousLevel.setEnabled(true);
	this.gameSkipLevel.setEnabled(true);
	this.gameLoadLevel.setEnabled(true);
	this.gameShowHint.setEnabled(true);
	this.gameCheats.setEnabled(true);
	this.gameChangeOtherAmmoMode.setEnabled(true);
	this.gameChangeOtherToolMode.setEnabled(true);
	this.gameChangeOtherRangeMode.setEnabled(true);
	this.gameEraDistantPast.setEnabled(true);
	this.gameEraPast.setEnabled(true);
	this.gameEraPresent.setEnabled(true);
	this.gameEraFuture.setEnabled(true);
	this.gameEraDistantFuture.setEnabled(true);
    }

    public void disableModeCommands() {
	this.gameReset.setEnabled(false);
	this.gameShowTable.setEnabled(false);
	this.gameReplaySolution.setEnabled(false);
	this.gameRecordSolution.setEnabled(false);
	this.gameLoadLPB.setEnabled(false);
	this.gamePreviousLevel.setEnabled(false);
	this.gameSkipLevel.setEnabled(false);
	this.gameLoadLevel.setEnabled(false);
	this.gameShowHint.setEnabled(false);
	this.gameCheats.setEnabled(false);
	this.gameChangeOtherAmmoMode.setEnabled(false);
	this.gameChangeOtherToolMode.setEnabled(false);
	this.gameChangeOtherRangeMode.setEnabled(false);
	this.gameEraDistantPast.setEnabled(false);
	this.gameEraPast.setEnabled(false);
	this.gameEraPresent.setEnabled(false);
	this.gameEraFuture.setEnabled(false);
	this.gameEraDistantFuture.setEnabled(false);
    }

    public void setInitialState() {
	this.disableModeCommands();
    }

    public JMenu createCommandsMenu() {
	final MenuHandler mhandler = new MenuHandler();
	final JMenu gameMenu = new JMenu(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_MENU_GAME));
	this.gameReset = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_RESET_CURRENT_LEVEL));
	this.gameShowTable = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_SHOW_SCORE_TABLE));
	this.gameReplaySolution = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_REPLAY_SOLUTION));
	this.gameRecordSolution = new JCheckBoxMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_RECORD_SOLUTION));
	this.gameLoadLPB = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_LOAD_PLAYBACK_FILE));
	this.gamePreviousLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_PREVIOUS_LEVEL));
	this.gameSkipLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_SKIP_LEVEL));
	this.gameLoadLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_LOAD_LEVEL));
	this.gameShowHint = new JMenuItem(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_SHOW_HINT));
	this.gameCheats = new JMenuItem(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_CHEATS));
	this.gameChangeOtherAmmoMode = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_CHANGE_OTHER_AMMO));
	this.gameChangeOtherToolMode = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_CHANGE_OTHER_TOOL));
	this.gameChangeOtherRangeMode = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_CHANGE_OTHER_RANGE));
	this.gameEraDistantPast = new JCheckBoxMenuItem(
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_DISTANT_PAST), false);
	this.gameEraPast = new JCheckBoxMenuItem(
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_PAST), false);
	this.gameEraPresent = new JCheckBoxMenuItem(
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_PRESENT), true);
	this.gameEraFuture = new JCheckBoxMenuItem(
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_FUTURE), false);
	this.gameEraDistantFuture = new JCheckBoxMenuItem(
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_DISTANT_FUTURE), false);
	this.gameReset.addActionListener(mhandler);
	this.gameShowTable.addActionListener(mhandler);
	this.gameReplaySolution.addActionListener(mhandler);
	this.gameRecordSolution.addActionListener(mhandler);
	this.gameLoadLPB.addActionListener(mhandler);
	this.gamePreviousLevel.addActionListener(mhandler);
	this.gameSkipLevel.addActionListener(mhandler);
	this.gameLoadLevel.addActionListener(mhandler);
	this.gameShowHint.addActionListener(mhandler);
	this.gameCheats.addActionListener(mhandler);
	this.gameChangeOtherAmmoMode.addActionListener(mhandler);
	this.gameChangeOtherToolMode.addActionListener(mhandler);
	this.gameChangeOtherRangeMode.addActionListener(mhandler);
	this.gameEraDistantPast.addActionListener(mhandler);
	this.gameEraPast.addActionListener(mhandler);
	this.gameEraPresent.addActionListener(mhandler);
	this.gameEraFuture.addActionListener(mhandler);
	this.gameEraDistantFuture.addActionListener(mhandler);
	this.gameTimeTravelSubMenu.add(this.gameEraDistantPast);
	this.gameTimeTravelSubMenu.add(this.gameEraPast);
	this.gameTimeTravelSubMenu.add(this.gameEraPresent);
	this.gameTimeTravelSubMenu.add(this.gameEraFuture);
	this.gameTimeTravelSubMenu.add(this.gameEraDistantFuture);
	gameMenu.add(this.gameReset);
	gameMenu.add(this.gameShowTable);
	gameMenu.add(this.gameReplaySolution);
	gameMenu.add(this.gameRecordSolution);
	gameMenu.add(this.gameLoadLPB);
	gameMenu.add(this.gamePreviousLevel);
	gameMenu.add(this.gameSkipLevel);
	gameMenu.add(this.gameLoadLevel);
	gameMenu.add(this.gameShowHint);
	gameMenu.add(this.gameCheats);
	gameMenu.add(this.gameChangeOtherAmmoMode);
	gameMenu.add(this.gameChangeOtherToolMode);
	gameMenu.add(this.gameChangeOtherRangeMode);
	gameMenu.add(this.gameTimeTravelSubMenu);
	return gameMenu;
    }

    public void attachAccelerators(final Accelerators accel) {
	this.gameReset.setAccelerator(accel.gameResetAccel);
	this.gameShowTable.setAccelerator(accel.gameShowTableAccel);
    }

    public void enableLoadedCommands() {
	// Do nothing
    }

    public void disableLoadedCommands() {
	// Do nothing
    }

    public void enableDirtyCommands() {
	// Do nothing
    }

    public void disableDirtyCommands() {
	// Do nothing
    }

    private class EventHandler implements KeyListener, WindowListener, MouseListener {
	EventHandler() {
	    // Do nothing
	}

	@Override
	public void keyPressed(final KeyEvent e) {
	    if (GameGUI.this.eventFlag) {
		if (!PrefsManager.oneMove()) {
		    this.handleKeystrokes(e);
		}
	    }
	}

	@Override
	public void keyReleased(final KeyEvent e) {
	    if (GameGUI.this.eventFlag) {
		if (PrefsManager.oneMove()) {
		    this.handleKeystrokes(e);
		}
	    }
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	    // Do nothing
	}
	
	private void handleKeystrokes(final KeyEvent e) {
	    final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
	    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
		if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		    if (gm.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_MISSILES) {
			this.handleMissiles();
		    } else if (gm.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_STUNNERS) {
			this.handleStunners();
		    } else if (gm.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_BLUE_LASERS) {
			this.handleBlueLasers();
		    } else if (gm.otherAmmoMode == GameLogic.OTHER_AMMO_MODE_DISRUPTORS) {
			this.handleDisruptors();
		    }
		} else {
		    this.handleLasers();
		}
	    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		    if (gm.otherRangeMode == GameLogic.OTHER_RANGE_MODE_BOMBS) {
			this.handleBombs();
		    } else if (gm.otherRangeMode == GameLogic.OTHER_RANGE_MODE_HEAT_BOMBS) {
			this.handleHeatBombs();
		    } else if (gm.otherRangeMode == GameLogic.OTHER_RANGE_MODE_ICE_BOMBS) {
			this.handleIceBombs();
		    }
		}
	    } else {
		final Direction currDir = gm.player.getDirection();
		final Direction newDir = this.mapKeyToDirection(e);
		if (currDir != newDir) {
		    this.handleTurns(newDir);
		} else {
		    if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
			if (gm.otherToolMode == GameLogic.OTHER_TOOL_MODE_BOOSTS) {
			    this.handleBoosts(e);
			} else if (gm.otherToolMode == GameLogic.OTHER_TOOL_MODE_MAGNETS) {
			    this.handleMagnets(e);
			}
		    } else {
			this.handleMovement(e);
		    }
		}
	    }
	}

	public void handleBoosts(final KeyEvent e) {
	    try {
		final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
		if (!gm.getCheatStatus(GameLogic.CHEAT_BOOSTS) && PartyInventory.getBoostsLeft() > 0
			|| gm.getCheatStatus(GameLogic.CHEAT_BOOSTS)) {
		    PartyInventory.fireBoost();
		    final int keyCode = e.getKeyCode();
		    switch (keyCode) {
		    case KeyEvent.VK_LEFT:
			gm.updatePositionRelative(-2, 0);
			break;
		    case KeyEvent.VK_DOWN:
			gm.updatePositionRelative(0, 2);
			break;
		    case KeyEvent.VK_RIGHT:
			gm.updatePositionRelative(2, 0);
			break;
		    case KeyEvent.VK_UP:
			gm.updatePositionRelative(0, -2);
			break;
		    default:
			break;
		    }
		} else {
		    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
			    LocaleConstants.GAME_STRING_OUT_OF_BOOSTS));
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleMagnets(final KeyEvent e) {
	    try {
		final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
		if (!gm.getCheatStatus(GameLogic.CHEAT_MAGNETS) && PartyInventory.getMagnetsLeft() > 0
			|| gm.getCheatStatus(GameLogic.CHEAT_MAGNETS)) {
		    PartyInventory.fireMagnet();
		    final int keyCode = e.getKeyCode();
		    switch (keyCode) {
		    case KeyEvent.VK_LEFT:
			gm.updatePositionRelative(-3, 0);
			break;
		    case KeyEvent.VK_DOWN:
			gm.updatePositionRelative(0, 3);
			break;
		    case KeyEvent.VK_RIGHT:
			gm.updatePositionRelative(3, 0);
			break;
		    case KeyEvent.VK_UP:
			gm.updatePositionRelative(0, -3);
			break;
		    default:
			break;
		    }
		} else {
		    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
			    LocaleConstants.GAME_STRING_OUT_OF_MAGNETS));
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleBombs() {
	    try {
		final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
		if (!gm.getCheatStatus(GameLogic.CHEAT_BOMBS) && PartyInventory.getBombsLeft() > 0
			|| gm.getCheatStatus(GameLogic.CHEAT_BOMBS)) {
		    PartyInventory.fireBomb();
		    gm.fireRange();
		} else {
		    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
			    LocaleConstants.GAME_STRING_OUT_OF_BOMBS));
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleHeatBombs() {
	    try {
		final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
		if (!gm.getCheatStatus(GameLogic.CHEAT_HEAT_BOMBS) && PartyInventory.getHeatBombsLeft() > 0
			|| gm.getCheatStatus(GameLogic.CHEAT_HEAT_BOMBS)) {
		    PartyInventory.fireHeatBomb();
		    gm.fireRange();
		} else {
		    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
			    LocaleConstants.GAME_STRING_OUT_OF_HEAT_BOMBS));
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleIceBombs() {
	    try {
		final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
		if (!gm.getCheatStatus(GameLogic.CHEAT_ICE_BOMBS) && PartyInventory.getIceBombsLeft() > 0
			|| gm.getCheatStatus(GameLogic.CHEAT_ICE_BOMBS)) {
		    PartyInventory.fireIceBomb();
		    gm.fireRange();
		} else {
		    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
			    LocaleConstants.GAME_STRING_OUT_OF_ICE_BOMBS));
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleTurns(final Direction dir) {
	    try {
		final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
		boolean fired = false;
		switch (dir) {
		case WEST:
		    gm.player.setDirection(Direction.WEST);
		    if (!gm.isReplaying()) {
			gm.updateReplay(false, -1, 0);
		    }
		    fired = true;
		    break;
		case SOUTH:
		    gm.player.setDirection(Direction.SOUTH);
		    if (!gm.isReplaying()) {
			gm.updateReplay(false, 0, 1);
		    }
		    fired = true;
		    break;
		case EAST:
		    gm.player.setDirection(Direction.EAST);
		    if (!gm.isReplaying()) {
			gm.updateReplay(false, 1, 0);
		    }
		    fired = true;
		    break;
		case NORTH:
		    gm.player.setDirection(Direction.NORTH);
		    if (!gm.isReplaying()) {
			gm.updateReplay(false, 0, -1);
		    }
		    fired = true;
		    break;
		default:
		    break;
		}
		if (fired) {
		    SoundLoader.playSound(SoundConstants.TURN);
		    gm.markPlayerAsDirty();
		    gm.redrawDungeon();
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleLasers() {
	    try {
		final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
		gm.setLaserType(ArrowTypeConstants.LASER_TYPE_GREEN);
		final int px = gm.getPlayerManager().getPlayerLocationX();
		final int py = gm.getPlayerManager().getPlayerLocationY();
		gm.fireLaser(px, py, gm.player);
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleMissiles() {
	    try {
		final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
		gm.setLaserType(ArrowTypeConstants.LASER_TYPE_MISSILE);
		final int px = gm.getPlayerManager().getPlayerLocationX();
		final int py = gm.getPlayerManager().getPlayerLocationY();
		gm.fireLaser(px, py, gm.player);
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleStunners() {
	    try {
		final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
		gm.setLaserType(ArrowTypeConstants.LASER_TYPE_STUNNER);
		final int px = gm.getPlayerManager().getPlayerLocationX();
		final int py = gm.getPlayerManager().getPlayerLocationY();
		gm.fireLaser(px, py, gm.player);
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleBlueLasers() {
	    try {
		final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
		gm.setLaserType(ArrowTypeConstants.LASER_TYPE_BLUE);
		final int px = gm.getPlayerManager().getPlayerLocationX();
		final int py = gm.getPlayerManager().getPlayerLocationY();
		gm.fireLaser(px, py, gm.player);
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public void handleDisruptors() {
	    try {
		final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
		gm.setLaserType(ArrowTypeConstants.LASER_TYPE_DISRUPTOR);
		final int px = gm.getPlayerManager().getPlayerLocationX();
		final int py = gm.getPlayerManager().getPlayerLocationY();
		gm.fireLaser(px, py, gm.player);
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	public Direction mapMouseToDirection(final MouseEvent me) {
	    final GameLogic gm = DungeonDiver7.getStuffBag().getGameLogic();
	    final int x = me.getX();
	    final int y = me.getY();
	    final int px = gm.getPlayerManager().getPlayerLocationX();
	    final int py = gm.getPlayerManager().getPlayerLocationY();
	    final int destX = (int) Math.signum(x / ImageLoader.getGraphicSize() - px);
	    final int destY = (int) Math.signum(y / ImageLoader.getGraphicSize() - py);
	    return DirectionResolver.resolveRelativeDirection(destX, destY);
	}

	public Direction mapKeyToDirection(final KeyEvent e) {
	    final int keyCode = e.getKeyCode();
	    switch (keyCode) {
	    case KeyEvent.VK_LEFT:
		return Direction.WEST;
	    case KeyEvent.VK_DOWN:
		return Direction.SOUTH;
	    case KeyEvent.VK_RIGHT:
		return Direction.EAST;
	    case KeyEvent.VK_UP:
		return Direction.NORTH;
	    default:
		return Direction.INVALID;
	    }
	}

	public void handleMovement(final KeyEvent e) {
	    try {
		final GameLogic glm = DungeonDiver7.getStuffBag().getGameLogic();
		final int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(-1, -1);
		    } else {
			glm.updatePositionRelative(-1, 0);
		    }
		    break;
		case KeyEvent.VK_DOWN:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(-1, 1);
		    } else {
			glm.updatePositionRelative(0, 1);
		    }
		    break;
		case KeyEvent.VK_RIGHT:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(1, 1);
		    } else {
			glm.updatePositionRelative(1, 0);
		    }
		    break;
		case KeyEvent.VK_UP:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(1, -1);
		    } else {
			glm.updatePositionRelative(0, -1);
		    }
		    break;
		case KeyEvent.VK_ENTER:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(0, 0);
		    }
		    break;
		case KeyEvent.VK_SPACE:
		    final StuffBag app = DungeonDiver7.getStuffBag();
		    final AbstractDungeon m = app.getDungeonManager().getDungeon();
		    int px = m.getPlayerLocationX(0);
		    int py = m.getPlayerLocationY(0);
		    AbstractDungeonObject there = new Empty();
		    try {
			there = m.getCell(px, py, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
		    } catch (final ArrayIndexOutOfBoundsException ae) {
			// Ignore
		    }
		    there.interactAction();
		    break;
		case KeyEvent.VK_NUMPAD7:
		case KeyEvent.VK_Q:
		    glm.updatePositionRelative(-1, -1);
		    break;
		case KeyEvent.VK_NUMPAD8:
		case KeyEvent.VK_W:
		    glm.updatePositionRelative(0, -1);
		    break;
		case KeyEvent.VK_NUMPAD9:
		case KeyEvent.VK_E:
		    glm.updatePositionRelative(1, -1);
		    break;
		case KeyEvent.VK_NUMPAD4:
		case KeyEvent.VK_A:
		    glm.updatePositionRelative(-1, 0);
		    break;
		case KeyEvent.VK_NUMPAD5:
		case KeyEvent.VK_S:
		    glm.updatePositionRelative(0, 0);
		    break;
		case KeyEvent.VK_NUMPAD6:
		case KeyEvent.VK_D:
		    glm.updatePositionRelative(1, 0);
		    break;
		case KeyEvent.VK_NUMPAD1:
		case KeyEvent.VK_Z:
		    glm.updatePositionRelative(-1, 1);
		    break;
		case KeyEvent.VK_NUMPAD2:
		case KeyEvent.VK_X:
		    glm.updatePositionRelative(0, 1);
		    break;
		case KeyEvent.VK_NUMPAD3:
		case KeyEvent.VK_C:
		    glm.updatePositionRelative(1, 1);
		    break;
		default:
		    break;
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	// Handle windows
	@Override
	public void windowActivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowClosed(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowClosing(final WindowEvent we) {
	    try {
		final StuffBag app = DungeonDiver7.getStuffBag();
		boolean success = false;
		int status = 0;
		if (app.getDungeonManager().getDirty()) {
		    app.getDungeonManager();
		    status = DungeonManager.showSaveDialog();
		    if (status == JOptionPane.YES_OPTION) {
			app.getDungeonManager();
			success = DungeonManager.saveGame();
			if (success) {
			    app.getGameLogic().exitGame();
			}
		    } else if (status == JOptionPane.NO_OPTION) {
			app.getGameLogic().exitGame();
		    }
		} else {
		    app.getGameLogic().exitGame();
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	@Override
	public void windowDeactivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowDeiconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowIconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowOpened(final WindowEvent we) {
	    // Do nothing
	}

	// handle mouse
	@Override
	public void mousePressed(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	    try {
		final GameLogic game = DungeonDiver7.getStuffBag().getGameLogic();
		if (e.isShiftDown()) {
		    final int x = e.getX();
		    final int y = e.getY();
		    game.identifyObject(x, y);
		} else {
		    if (e.getButton() == MouseEvent.BUTTON1) {
			// Move
			final Direction dir = this.mapMouseToDirection(e);
			final Direction tankDir = game.player.getDirection();
			if (tankDir != dir) {
			    this.handleTurns(dir);
			} else {
			    final int x = e.getX();
			    final int y = e.getY();
			    final int px = game.getPlayerManager().getPlayerLocationX();
			    final int py = game.getPlayerManager().getPlayerLocationY();
			    final int destX = (int) Math.signum(x / ImageLoader.getGraphicSize() - px);
			    final int destY = (int) Math.signum(y / ImageLoader.getGraphicSize() - py);
			    game.updatePositionRelative(destX, destY);
			}
		    } else if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
			// Fire Laser
			game.setLaserType(ArrowTypeConstants.LASER_TYPE_GREEN);
			final int px = game.getPlayerManager().getPlayerLocationX();
			final int py = game.getPlayerManager().getPlayerLocationY();
			game.fireLaser(px, py, game.player);
		    }
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	    // Do nothing
	}
    }

    private class MenuHandler implements ActionListener {
	public MenuHandler() {
	    // Do nothing
	}

	// Handle menus
	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final StuffBag app = DungeonDiver7.getStuffBag();
		final String cmd = e.getActionCommand();
		final GameLogic game = app.getGameLogic();
		final GameGUI gui = GameGUI.this;
		if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_RESET_CURRENT_LEVEL))) {
		    final int result = CommonDialogs.showConfirmDialog(
			    LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
				    LocaleConstants.MENU_STRING_CONFIRM_RESET_CURRENT_LEVEL),
			    LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
				    LocaleConstants.NOTL_STRING_PROGRAM_NAME));
		    if (result == JOptionPane.YES_OPTION) {
			game.abortAndWaitForMLOLoop();
			game.resetCurrentLevel();
		    }
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_SHOW_SCORE_TABLE))) {
		    game.showScoreTable();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_REPLAY_SOLUTION))) {
		    game.abortAndWaitForMLOLoop();
		    game.replaySolution();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_RECORD_SOLUTION))) {
		    game.toggleRecording();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_LOAD_PLAYBACK_FILE))) {
		    game.abortAndWaitForMLOLoop();
		    ReplayManager.loadLPB();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_PREVIOUS_LEVEL))) {
		    game.abortAndWaitForMLOLoop();
		    game.previousLevel();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_SKIP_LEVEL))) {
		    game.abortAndWaitForMLOLoop();
		    game.solvedLevel(false);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_LOAD_LEVEL))) {
		    game.abortAndWaitForMLOLoop();
		    game.loadLevel();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_SHOW_HINT))) {
		    CommonDialogs.showDialog(app.getDungeonManager().getDungeon().getHint().trim());
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_CHEATS))) {
		    game.enterCheatCode();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_CHANGE_OTHER_AMMO))) {
		    game.changeOtherAmmoMode();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_CHANGE_OTHER_TOOL))) {
		    game.changeOtherToolMode();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_CHANGE_OTHER_RANGE))) {
		    game.changeOtherRangeMode();
		} else if (cmd.equals(
			LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_DISTANT_PAST))) {
		    // Time Travel: Distant Past
		    SoundLoader.playSound(SoundConstants.ERA_CHANGE);
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_DISTANT_PAST);
		    gui.gameEraDistantPast.setSelected(true);
		    gui.gameEraPast.setSelected(false);
		    gui.gameEraPresent.setSelected(false);
		    gui.gameEraFuture.setSelected(false);
		    gui.gameEraDistantFuture.setSelected(false);
		} else if (cmd
			.equals(LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_PAST))) {
		    // Time Travel: Past
		    SoundLoader.playSound(SoundConstants.ERA_CHANGE);
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_PAST);
		    gui.gameEraDistantPast.setSelected(false);
		    gui.gameEraPast.setSelected(true);
		    gui.gameEraPresent.setSelected(false);
		    gui.gameEraFuture.setSelected(false);
		    gui.gameEraDistantFuture.setSelected(false);
		} else if (cmd.equals(
			LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_PRESENT))) {
		    // Time Travel: Present
		    SoundLoader.playSound(SoundConstants.ERA_CHANGE);
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_PRESENT);
		    gui.gameEraDistantPast.setSelected(false);
		    gui.gameEraPast.setSelected(false);
		    gui.gameEraPresent.setSelected(true);
		    gui.gameEraFuture.setSelected(false);
		    gui.gameEraDistantFuture.setSelected(false);
		} else if (cmd.equals(
			LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_FUTURE))) {
		    // Time Travel: Future
		    SoundLoader.playSound(SoundConstants.ERA_CHANGE);
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_FUTURE);
		    gui.gameEraDistantPast.setSelected(false);
		    gui.gameEraPast.setSelected(false);
		    gui.gameEraPresent.setSelected(false);
		    gui.gameEraFuture.setSelected(true);
		    gui.gameEraDistantFuture.setSelected(false);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE,
			DungeonConstants.ERA_DISTANT_FUTURE))) {
		    // Time Travel: Distant Future
		    SoundLoader.playSound(SoundConstants.ERA_CHANGE);
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_DISTANT_FUTURE);
		    gui.gameEraDistantPast.setSelected(false);
		    gui.gameEraPast.setSelected(false);
		    gui.gameEraPresent.setSelected(false);
		    gui.gameEraFuture.setSelected(false);
		    gui.gameEraDistantFuture.setSelected(true);
		}
		app.getMenuManager().checkFlags();
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}
    }

    private class DifficultyEventHandler implements ActionListener, WindowListener {
	public DifficultyEventHandler() {
	    // Do nothing
	}

	@Override
	public void windowOpened(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowClosing(final WindowEvent e) {
	    GameGUI.this.difficultyDialogCancelButtonClicked();
	}

	@Override
	public void windowClosed(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowIconified(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowActivated(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
	    // Ignore
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    final String cmd = e.getActionCommand();
	    final GameGUI gm = GameGUI.this;
	    if (cmd.equals(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
		    LocaleConstants.DIALOG_STRING_OK_BUTTON))) {
		gm.difficultyDialogOKButtonClicked();
	    } else {
		gm.difficultyDialogCancelButtonClicked();
	    }
	}
    }

    private class FocusHandler implements WindowFocusListener {
	public FocusHandler() {
	    // Do nothing
	}

	@Override
	public void windowGainedFocus(final WindowEvent e) {
	    GameGUI.this.attachMenus();
	}

	@Override
	public void windowLostFocus(final WindowEvent e) {
	    // Do nothing
	}
    }
}
