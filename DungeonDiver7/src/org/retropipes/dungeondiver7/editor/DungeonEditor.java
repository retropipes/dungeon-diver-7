/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.editor;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JToggleButton;

import org.retropipes.diane.LocaleUtils;
import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.asset.image.ImageCompositor;
import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.diane.gui.picker.PicturePicker;
import org.retropipes.diane.gui.picker.SXSPicturePicker;
import org.retropipes.diane.gui.picker.StackedPicturePicker;
import org.retropipes.diane.integration.Integration;
import org.retropipes.dungeondiver7.Accelerators;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.MenuSection;
import org.retropipes.dungeondiver7.StuffBag;
import org.retropipes.dungeondiver7.asset.ImageConstants;
import org.retropipes.dungeondiver7.dungeon.Dungeon;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractJumpObject;
import org.retropipes.dungeondiver7.dungeon.objects.Ground;
import org.retropipes.dungeondiver7.dungeon.objects.Party;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageLoader;
import org.retropipes.dungeondiver7.locale.EditorLayout;
import org.retropipes.dungeondiver7.locale.EditorString;
import org.retropipes.dungeondiver7.locale.Menu;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.TimeTravel;
import org.retropipes.dungeondiver7.manager.dungeon.DungeonManager;
import org.retropipes.dungeondiver7.prefs.Prefs;
import org.retropipes.dungeondiver7.utility.DungeonConstants;
import org.retropipes.dungeondiver7.utility.RCLGenerator;

public class DungeonEditor implements MenuSection {
    private class EventHandler implements MouseListener, MouseMotionListener, WindowListener {
	// handle scroll bars
	public EventHandler() {
	    // Do nothing
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	    try {
		final var me = DungeonEditor.this;
		final var x = e.getX();
		final var y = e.getY();
		if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		    me.editObjectProperties(x, y);
		} else if (e.isShiftDown()) {
		    me.probeObjectProperties(x, y);
		} else {
		    me.editObject(x, y);
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
	    try {
		final var me = DungeonEditor.this;
		final var x = e.getX();
		final var y = e.getY();
		me.editObject(x, y);
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

	@Override
	public void mouseMoved(final MouseEvent e) {
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
	    DungeonEditor.this.handleCloseWindow();
	    DungeonDiver7.getStuffBag().getGUIManager().showGUI();
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
    }

    private class MenuHandler implements ActionListener {
	public MenuHandler() {
	    // Do nothing
	}

	// Handle menus
	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final var app = DungeonDiver7.getStuffBag();
		final var cmd = e.getActionCommand();
		final var editor = DungeonEditor.this;
		if (cmd.equals(Strings.menu(Menu.UNDO))) {
		    // Undo most recent action
		    if (app.getMode() == StuffBag.STATUS_EDITOR) {
			editor.undo();
		    } else if (app.getMode() == StuffBag.STATUS_GAME) {
			app.getGameLogic().abortAndWaitForMLOLoop();
			app.getGameLogic().undoLastMove();
		    }
		} else if (cmd.equals(Strings.menu(Menu.REDO))) {
		    // Redo most recent undone action
		    if (app.getMode() == StuffBag.STATUS_EDITOR) {
			editor.redo();
		    } else if (app.getMode() == StuffBag.STATUS_GAME) {
			app.getGameLogic().abortAndWaitForMLOLoop();
			app.getGameLogic().redoLastMove();
		    }
		} else if (cmd.equals(Strings.menu(Menu.CUT_LEVEL))) {
		    // Cut Level
		    final var level = editor.getLocationManager().getEditorLocationU();
		    app.getDungeonManager().getDungeon().cutLevel();
		    editor.fixLimits();
		    editor.updateEditorLevelAbsolute(level);
		} else if (cmd.equals(Strings.menu(Menu.COPY_LEVEL))) {
		    // Copy Level
		    app.getDungeonManager().getDungeon().copyLevel();
		} else if (cmd.equals(Strings.menu(Menu.PASTE_LEVEL))) {
		    // Paste Level
		    app.getDungeonManager().getDungeon().pasteLevel();
		    editor.fixLimits();
		    editor.redrawEditor();
		} else if (cmd.equals(Strings.menu(Menu.INSERT_LEVEL_FROM_CLIPBOARD))) {
		    // Insert Level From Clipboard
		    app.getDungeonManager().getDungeon().insertLevelFromClipboard();
		    editor.fixLimits();
		} else if (cmd.equals(Strings.menu(Menu.CLEAR_HISTORY))) {
		    // Clear undo/redo history, confirm first
		    final var res = CommonDialogs.showConfirmDialog(Strings.menu(Menu.CONFIRM_CLEAR_HISTORY),
			    Strings.editor(EditorString.EDITOR));
		    if (res == CommonDialogs.YES_OPTION) {
			editor.clearHistory();
		    }
		} else if (cmd.equals(Strings.menu(Menu.GO_TO_LEVEL))) {
		    // Go To Level
		    editor.goToLevelHandler();
		} else if (cmd.equals(Strings.menu(Menu.UP_ONE_FLOOR))) {
		    // Go up one floor
		    editor.updateEditorPosition(1, 0);
		} else if (cmd.equals(Strings.menu(Menu.DOWN_ONE_FLOOR))) {
		    // Go down one floor
		    editor.updateEditorPosition(-1, 0);
		} else if (cmd.equals(Strings.menu(Menu.UP_ONE_LEVEL))) {
		    // Go up one level
		    editor.updateEditorPosition(0, 1);
		} else if (cmd.equals(Strings.menu(Menu.DOWN_ONE_LEVEL))) {
		    // Go down one level
		    editor.updateEditorPosition(0, -1);
		} else if (cmd.equals(Strings.menu(Menu.ADD_A_LEVEL))) {
		    // Add a level
		    editor.addLevel();
		} else if (cmd.equals(Strings.menu(Menu.REMOVE_A_LEVEL))) {
		    // Remove a level
		    editor.removeLevel();
		} else if (cmd.equals(Strings.menu(Menu.FILL_CURRENT_LEVEL))) {
		    // Fill level
		    editor.fillLevel();
		} else if (cmd.equals(Strings.menu(Menu.RESIZE_CURRENT_LEVEL))) {
		    // Resize level
		    editor.resizeLevel();
		} else if (cmd.equals(Strings.menu(Menu.LEVEL_PREFERENCES))) {
		    // Set Level Preferences
		    editor.setLevelPrefs();
		} else if (cmd.equals(Strings.menu(Menu.SET_START_POINT))) {
		    // Set Start Point
		    editor.editPlayerLocation();
		} else if (cmd.equals(Strings.menu(Menu.SET_MUSIC))) {
		    // Set Music
		    editor.defineDungeonMusic();
		} else if (cmd.equals(Strings.menu(Menu.CHANGE_LAYER))) {
		    // Change Layer
		    editor.changeLayer();
		} else if (cmd.equals(Strings.menu(Menu.ENABLE_GLOBAL_MOVE_SHOOT))) {
		    // Enable Global Move-Shoot
		    editor.enableGlobalMoveShoot();
		} else if (cmd.equals(Strings.menu(Menu.DISABLE_GLOBAL_MOVE_SHOOT))) {
		    // Disable Global Move-Shoot
		    editor.disableGlobalMoveShoot();
		} else if (cmd.equals(Strings.timeTravel(TimeTravel.FAR_PAST))) {
		    // Time Travel: Distant Past
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_DISTANT_PAST);
		    editor.editorEraDistantPast.setSelected(true);
		    editor.editorEraPast.setSelected(false);
		    editor.editorEraPresent.setSelected(false);
		    editor.editorEraFuture.setSelected(false);
		    editor.editorEraDistantFuture.setSelected(false);
		} else if (cmd.equals(Strings.timeTravel(TimeTravel.PAST))) {
		    // Time Travel: Past
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_PAST);
		    editor.editorEraDistantPast.setSelected(false);
		    editor.editorEraPast.setSelected(true);
		    editor.editorEraPresent.setSelected(false);
		    editor.editorEraFuture.setSelected(false);
		    editor.editorEraDistantFuture.setSelected(false);
		} else if (cmd.equals(Strings.timeTravel(TimeTravel.PRESENT))) {
		    // Time Travel: Present
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_PRESENT);
		    editor.editorEraDistantPast.setSelected(false);
		    editor.editorEraPast.setSelected(false);
		    editor.editorEraPresent.setSelected(true);
		    editor.editorEraFuture.setSelected(false);
		    editor.editorEraDistantFuture.setSelected(false);
		} else if (cmd.equals(Strings.timeTravel(TimeTravel.FUTURE))) {
		    // Time Travel: Future
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_FUTURE);
		    editor.editorEraDistantPast.setSelected(false);
		    editor.editorEraPast.setSelected(false);
		    editor.editorEraPresent.setSelected(false);
		    editor.editorEraFuture.setSelected(true);
		    editor.editorEraDistantFuture.setSelected(false);
		} else if (cmd.equals(Strings.timeTravel(TimeTravel.FAR_FUTURE))) {
		    // Time Travel: Distant Future
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_DISTANT_FUTURE);
		    editor.editorEraDistantPast.setSelected(false);
		    editor.editorEraPast.setSelected(false);
		    editor.editorEraPresent.setSelected(false);
		    editor.editorEraFuture.setSelected(false);
		    editor.editorEraDistantFuture.setSelected(true);
		}
		app.getMenus().checkFlags();
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}
    }

    private class StartEventHandler implements MouseListener {
	// handle scroll bars
	public StartEventHandler() {
	    // Do nothing
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	    try {
		final var x = e.getX();
		final var y = e.getY();
		DungeonEditor.this.setPlayerLocation(x, y);
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

	// handle mouse
	@Override
	public void mousePressed(final MouseEvent e) {
	    // Do nothing
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
	    // Do nothing
	}
    }

    private class SwitcherHandler implements ActionListener {
	SwitcherHandler() {
	    // Do nothing
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final var cmd = e.getActionCommand();
		final var ae = DungeonEditor.this;
		if (cmd.equals(Strings.editor(EditorString.LOWER_GROUND_LAYER))) {
		    ae.changeLayerImpl(DungeonConstants.LAYER_LOWER_GROUND);
		} else if (cmd.equals(Strings.editor(EditorString.UPPER_GROUND_LAYER))) {
		    ae.changeLayerImpl(DungeonConstants.LAYER_UPPER_GROUND);
		} else if (cmd.equals(Strings.editor(EditorString.LOWER_OBJECTS_LAYER))) {
		    ae.changeLayerImpl(DungeonConstants.LAYER_LOWER_OBJECTS);
		} else if (cmd.equals(Strings.editor(EditorString.UPPER_OBJECTS_LAYER))) {
		    ae.changeLayerImpl(DungeonConstants.LAYER_UPPER_OBJECTS);
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}
    }

    private static final int STACK_COUNT = 10;
    private static final String[] JUMP_LIST = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    // Declarations
    private MainWindow mainWindow;
    private MainContent secondaryPane, borderPane, outerOutputPane, switcherPane;
    private EditorDraw outputPane;
    private JToggleButton lowerGround, upperGround, lowerObjects, upperObjects;
    private JLabel messageLabel;
    private DungeonObject savedDungeonObject;
    private JScrollBar vertScroll, horzScroll;
    private final EventHandler mhandler;
    private final StartEventHandler shandler;
    private final LevelPreferencesManager lPrefs;
    private PicturePicker oldPicker;
    private StackedPicturePicker newPicker11;
    private SXSPicturePicker newPicker12;
    private String[] names;
    private DungeonObject[] objects;
    private BufferedImageIcon[] editorAppearances;
    private boolean[] objectsEnabled;
    private EditorUndoRedoEngine engine;
    private EditorLocationManager elMgr;
    private boolean dungeonChanged;
    private final ExternalMusicEditor eme;
    private final int activePlayer;
    private JMenu editorTimeTravelSubMenu;
    JCheckBoxMenuItem editorEraDistantPast, editorEraPast, editorEraPresent, editorEraFuture, editorEraDistantFuture;
    private JMenuItem editorUndo, editorRedo, editorCutLevel, editorCopyLevel, editorPasteLevel,
	    editorInsertLevelFromClipboard, editorClearHistory, editorGoToLevel, editorUpOneFloor, editorDownOneFloor,
	    editorUpOneLevel, editorDownOneLevel, editorAddLevel, editorRemoveLevel, editorLevelPreferences,
	    editorSetStartPoint, editorFillLevel, editorResizeLevel, editorSetMusic, editorChangeLayer,
	    editorGlobalMoveShoot;

    public DungeonEditor() {
	this.savedDungeonObject = new Ground();
	this.lPrefs = new LevelPreferencesManager();
	this.mhandler = new EventHandler();
	this.shandler = new StartEventHandler();
	this.engine = new EditorUndoRedoEngine();
	final var objectList = DungeonDiver7.getStuffBag().getObjects();
	this.names = objectList.getAllNamesOnLayer(DungeonConstants.LAYER_LOWER_GROUND);
	this.objects = objectList.getAllObjectsOnLayer(DungeonConstants.LAYER_LOWER_GROUND,
		Prefs.getEditorShowAllObjects());
	this.editorAppearances = objectList.getAllEditorAppearancesOnLayer(DungeonConstants.LAYER_LOWER_GROUND,
		Prefs.getEditorShowAllObjects());
	this.objectsEnabled = objectList.getObjectEnabledStatuses(DungeonConstants.LAYER_LOWER_GROUND);
	this.dungeonChanged = true;
	this.eme = new ExternalMusicEditor();
	this.activePlayer = 0;
    }

    public void activeLanguageChanged() {
	this.updatePicker();
    }

    public boolean addLevel() {
	final var success = this.addLevelInternal();
	if (success) {
	    CommonDialogs.showDialog(Strings.editor(EditorString.LEVEL_ADDED));
	} else {
	    CommonDialogs.showDialog(Strings.editor(EditorString.LEVEL_ADDING_FAILED));
	}
	return success;
    }

    private boolean addLevelInternal() {
	final var app = DungeonDiver7.getStuffBag();
	final var saveLevel = app.getDungeonManager().getDungeon().getActiveLevel();
	final var success = app.getDungeonManager().getDungeon().addLevel();
	if (success) {
	    this.fixLimits();
	    app.getDungeonManager().getDungeon().fillDefault();
	    // Save the entire level
	    app.getDungeonManager().getDungeon().save();
	    app.getDungeonManager().getDungeon().switchLevel(saveLevel);
	    this.checkMenus();
	}
	return success;
    }

    @Override
    public void attachAccelerators(final Accelerators accel) {
	this.editorUndo.setAccelerator(accel.editorUndoAccel);
	this.editorRedo.setAccelerator(accel.editorRedoAccel);
	this.editorCutLevel.setAccelerator(accel.editorCutLevelAccel);
	this.editorCopyLevel.setAccelerator(accel.editorCopyLevelAccel);
	this.editorPasteLevel.setAccelerator(accel.editorPasteLevelAccel);
	this.editorInsertLevelFromClipboard.setAccelerator(accel.editorInsertLevelFromClipboardAccel);
	this.editorClearHistory.setAccelerator(accel.editorClearHistoryAccel);
	this.editorGoToLevel.setAccelerator(accel.editorGoToLocationAccel);
	this.editorUpOneLevel.setAccelerator(accel.editorUpOneLevelAccel);
	this.editorDownOneLevel.setAccelerator(accel.editorDownOneLevelAccel);
    }

    public void attachMenus() {
	final var app = DungeonDiver7.getStuffBag();
	Integration.integrate().setDefaultMenuBar(app.getMenus().getMainMenuBar());
	app.getMenus().checkFlags();
    }

    public void changeLayer() {
	final var list = DungeonConstants.getLayerList();
	final var choice = CommonDialogs.showInputDialog(Strings.editor(EditorString.CHANGE_LAYER_PROMPT),
		Strings.editor(EditorString.EDITOR), list, list[this.elMgr.getEditorLocationW()]);
	if (choice != null) {
	    final var len = list.length;
	    var index = -1;
	    for (var z = 0; z < len; z++) {
		if (choice.equals(list[z])) {
		    index = z;
		    break;
		}
	    }
	    if (index != -1) {
		// Update selected button
		switch (index) {
		case DungeonConstants.LAYER_LOWER_GROUND:
		    this.lowerGround.setSelected(true);
		    break;
		case DungeonConstants.LAYER_UPPER_GROUND:
		    this.upperGround.setSelected(true);
		    break;
		case DungeonConstants.LAYER_LOWER_OBJECTS:
		    this.lowerObjects.setSelected(true);
		    break;
		case DungeonConstants.LAYER_UPPER_OBJECTS:
		    this.upperObjects.setSelected(true);
		    break;
		default:
		    break;
		}
		this.changeLayerImpl(index);
	    }
	}
    }

    void changeLayerImpl(final int layer) {
	this.elMgr.setEditorLocationW(layer);
	this.updatePicker();
	this.redrawEditor();
    }

    private void checkMenus() {
	final var app = DungeonDiver7.getStuffBag();
	if (app.getMode() == StuffBag.STATUS_EDITOR) {
	    final var m = app.getDungeonManager().getDungeon();
	    if (m.getLevels() == Dungeon.getMinLevels()) {
		this.disableRemoveLevel();
	    } else {
		this.enableRemoveLevel();
	    }
	    if (m.getLevels() == Dungeon.getMaxLevels()) {
		this.disableAddLevel();
	    } else {
		this.enableAddLevel();
	    }
	    if (this.elMgr != null) {
		if (this.elMgr.getEditorLocationZ() == this.elMgr.getMinEditorLocationZ()) {
		    this.disableDownOneFloor();
		} else {
		    this.enableDownOneFloor();
		}
		if (this.elMgr.getEditorLocationZ() == this.elMgr.getMaxEditorLocationZ()) {
		    this.disableUpOneFloor();
		} else {
		    this.enableUpOneFloor();
		}
		if (this.elMgr.getEditorLocationU() == this.elMgr.getMinEditorLocationU()) {
		    this.disableDownOneLevel();
		} else {
		    this.enableDownOneLevel();
		}
		if (this.elMgr.getEditorLocationU() == this.elMgr.getMaxEditorLocationU()) {
		    this.disableUpOneLevel();
		} else {
		    this.enableUpOneLevel();
		}
	    } else {
		this.disableDownOneFloor();
		this.disableUpOneFloor();
		this.disableDownOneLevel();
		this.disableUpOneLevel();
	    }
	    if (this.elMgr != null) {
		this.enableSetStartPoint();
	    } else {
		this.disableSetStartPoint();
	    }
	    if (!this.engine.tryUndo()) {
		this.disableUndo();
	    } else {
		this.enableUndo();
	    }
	    if (!this.engine.tryRedo()) {
		this.disableRedo();
	    } else {
		this.enableRedo();
	    }
	    if (this.engine.tryBoth()) {
		this.disableClearHistory();
	    } else {
		this.enableClearHistory();
	    }
	}
	if (app.getDungeonManager().getDungeon().isPasteBlocked()) {
	    this.disablePasteLevel();
	    this.disableInsertLevelFromClipboard();
	} else {
	    this.enablePasteLevel();
	    this.enableInsertLevelFromClipboard();
	}
	if (app.getDungeonManager().getDungeon().isCutBlocked()) {
	    this.disableCutLevel();
	} else {
	    this.enableCutLevel();
	}
    }

    public void clearHistory() {
	this.engine = new EditorUndoRedoEngine();
	this.checkMenus();
    }

    private boolean confirmNonUndoable() {
	final var confirm = CommonDialogs.showConfirmDialog(Strings.editor(EditorString.CONFIRM_CANNOT_BE_UNDONE),
		Strings.editor(EditorString.EDITOR));
	if (confirm == CommonDialogs.YES_OPTION) {
	    this.clearHistory();
	    return true;
	}
	return false;
    }

    @Override
    public JMenu createCommandsMenu() {
	final var menuHandler = new MenuHandler();
	final var editorMenu = new JMenu(Strings.menu(Menu.EDITOR));
	this.editorUndo = new JMenuItem(Strings.menu(Menu.UNDO));
	this.editorRedo = new JMenuItem(Strings.menu(Menu.REDO));
	this.editorCutLevel = new JMenuItem(Strings.menu(Menu.CUT_LEVEL));
	this.editorCopyLevel = new JMenuItem(Strings.menu(Menu.COPY_LEVEL));
	this.editorPasteLevel = new JMenuItem(Strings.menu(Menu.PASTE_LEVEL));
	this.editorInsertLevelFromClipboard = new JMenuItem(Strings.menu(Menu.INSERT_LEVEL_FROM_CLIPBOARD));
	this.editorClearHistory = new JMenuItem(Strings.menu(Menu.CLEAR_HISTORY));
	this.editorGoToLevel = new JMenuItem(Strings.menu(Menu.GO_TO_LEVEL));
	this.editorUpOneFloor = new JMenuItem(Strings.menu(Menu.UP_ONE_FLOOR));
	this.editorDownOneFloor = new JMenuItem(Strings.menu(Menu.DOWN_ONE_FLOOR));
	this.editorUpOneLevel = new JMenuItem(Strings.menu(Menu.UP_ONE_LEVEL));
	this.editorDownOneLevel = new JMenuItem(Strings.menu(Menu.DOWN_ONE_LEVEL));
	this.editorAddLevel = new JMenuItem(Strings.menu(Menu.ADD_A_LEVEL));
	this.editorRemoveLevel = new JMenuItem(Strings.menu(Menu.REMOVE_A_LEVEL));
	this.editorFillLevel = new JMenuItem(Strings.menu(Menu.FILL_CURRENT_LEVEL));
	this.editorResizeLevel = new JMenuItem(Strings.menu(Menu.RESIZE_CURRENT_LEVEL));
	this.editorLevelPreferences = new JMenuItem(Strings.menu(Menu.LEVEL_PREFERENCES));
	this.editorSetStartPoint = new JMenuItem(Strings.menu(Menu.SET_START_POINT));
	this.editorSetMusic = new JMenuItem(Strings.menu(Menu.SET_MUSIC));
	this.editorChangeLayer = new JMenuItem(Strings.menu(Menu.CHANGE_LAYER));
	this.editorGlobalMoveShoot = new JMenuItem(Strings.menu(Menu.ENABLE_GLOBAL_MOVE_SHOOT));
	this.editorTimeTravelSubMenu = new JMenu(Strings.menu(Menu.TIME_TRAVEL));
	this.editorEraDistantPast = new JCheckBoxMenuItem(Strings.timeTravel(TimeTravel.FAR_PAST), false);
	this.editorEraPast = new JCheckBoxMenuItem(Strings.timeTravel(TimeTravel.PAST), false);
	this.editorEraPresent = new JCheckBoxMenuItem(Strings.timeTravel(TimeTravel.PRESENT), true);
	this.editorEraFuture = new JCheckBoxMenuItem(Strings.timeTravel(TimeTravel.FUTURE), false);
	this.editorEraDistantFuture = new JCheckBoxMenuItem(Strings.timeTravel(TimeTravel.FAR_FUTURE), false);
	this.editorUndo.addActionListener(menuHandler);
	this.editorRedo.addActionListener(menuHandler);
	this.editorCutLevel.addActionListener(menuHandler);
	this.editorCopyLevel.addActionListener(menuHandler);
	this.editorPasteLevel.addActionListener(menuHandler);
	this.editorInsertLevelFromClipboard.addActionListener(menuHandler);
	this.editorClearHistory.addActionListener(menuHandler);
	this.editorGoToLevel.addActionListener(menuHandler);
	this.editorUpOneFloor.addActionListener(menuHandler);
	this.editorDownOneFloor.addActionListener(menuHandler);
	this.editorUpOneLevel.addActionListener(menuHandler);
	this.editorDownOneLevel.addActionListener(menuHandler);
	this.editorAddLevel.addActionListener(menuHandler);
	this.editorRemoveLevel.addActionListener(menuHandler);
	this.editorFillLevel.addActionListener(menuHandler);
	this.editorResizeLevel.addActionListener(menuHandler);
	this.editorLevelPreferences.addActionListener(menuHandler);
	this.editorSetStartPoint.addActionListener(menuHandler);
	this.editorSetMusic.addActionListener(menuHandler);
	this.editorChangeLayer.addActionListener(menuHandler);
	this.editorGlobalMoveShoot.addActionListener(menuHandler);
	this.editorEraDistantPast.addActionListener(menuHandler);
	this.editorEraPast.addActionListener(menuHandler);
	this.editorEraPresent.addActionListener(menuHandler);
	this.editorEraFuture.addActionListener(menuHandler);
	this.editorEraDistantFuture.addActionListener(menuHandler);
	this.editorTimeTravelSubMenu.add(this.editorEraDistantPast);
	this.editorTimeTravelSubMenu.add(this.editorEraPast);
	this.editorTimeTravelSubMenu.add(this.editorEraPresent);
	this.editorTimeTravelSubMenu.add(this.editorEraFuture);
	this.editorTimeTravelSubMenu.add(this.editorEraDistantFuture);
	editorMenu.add(this.editorUndo);
	editorMenu.add(this.editorRedo);
	editorMenu.add(this.editorCutLevel);
	editorMenu.add(this.editorCopyLevel);
	editorMenu.add(this.editorPasteLevel);
	editorMenu.add(this.editorInsertLevelFromClipboard);
	editorMenu.add(this.editorClearHistory);
	editorMenu.add(this.editorGoToLevel);
	editorMenu.add(this.editorUpOneFloor);
	editorMenu.add(this.editorDownOneFloor);
	editorMenu.add(this.editorUpOneLevel);
	editorMenu.add(this.editorDownOneLevel);
	editorMenu.add(this.editorAddLevel);
	editorMenu.add(this.editorRemoveLevel);
	editorMenu.add(this.editorFillLevel);
	editorMenu.add(this.editorResizeLevel);
	editorMenu.add(this.editorLevelPreferences);
	editorMenu.add(this.editorSetStartPoint);
	editorMenu.add(this.editorSetMusic);
	editorMenu.add(this.editorChangeLayer);
	editorMenu.add(this.editorGlobalMoveShoot);
	editorMenu.add(this.editorTimeTravelSubMenu);
	return editorMenu;
    }

    public void defineDungeonMusic() {
	this.hideOutput();
	this.eme.edit();
    }

    private void disableAddLevel() {
	this.editorAddLevel.setEnabled(false);
    }

    private void disableClearHistory() {
	this.editorClearHistory.setEnabled(false);
    }

    private void disableCutLevel() {
	this.editorCutLevel.setEnabled(false);
    }

    @Override
    public void disableDirtyCommands() {
	// Do nothing
    }

    private void disableDownOneFloor() {
	this.editorDownOneFloor.setEnabled(false);
    }

    private void disableDownOneLevel() {
	this.editorDownOneLevel.setEnabled(false);
    }

    void disableGlobalMoveShoot() {
	DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().setMoveShootAllowedGlobally(false);
	this.editorGlobalMoveShoot.setText(Strings.menu(Menu.ENABLE_GLOBAL_MOVE_SHOOT));
    }

    private void disableInsertLevelFromClipboard() {
	this.editorInsertLevelFromClipboard.setEnabled(false);
    }

    @Override
    public void disableLoadedCommands() {
	// Do nothing
    }

    @Override
    public void disableModeCommands() {
	this.editorUndo.setEnabled(false);
	this.editorRedo.setEnabled(false);
	this.editorCutLevel.setEnabled(false);
	this.editorCopyLevel.setEnabled(false);
	this.editorPasteLevel.setEnabled(false);
	this.editorInsertLevelFromClipboard.setEnabled(false);
	this.editorClearHistory.setEnabled(false);
	this.editorGoToLevel.setEnabled(false);
	this.editorUpOneFloor.setEnabled(false);
	this.editorDownOneFloor.setEnabled(false);
	this.editorUpOneLevel.setEnabled(false);
	this.editorDownOneLevel.setEnabled(false);
	this.editorAddLevel.setEnabled(false);
	this.editorRemoveLevel.setEnabled(false);
	this.editorFillLevel.setEnabled(false);
	this.editorResizeLevel.setEnabled(false);
	this.editorLevelPreferences.setEnabled(false);
	this.editorSetStartPoint.setEnabled(false);
	this.editorSetMusic.setEnabled(false);
	this.editorChangeLayer.setEnabled(false);
	this.editorGlobalMoveShoot.setEnabled(false);
	this.editorEraDistantPast.setEnabled(false);
	this.editorEraPast.setEnabled(false);
	this.editorEraPresent.setEnabled(false);
	this.editorEraFuture.setEnabled(false);
	this.editorEraDistantFuture.setEnabled(false);
    }

    void disableOutput() {
	this.mainWindow.setEnabled(false);
    }

    private void disablePasteLevel() {
	this.editorPasteLevel.setEnabled(false);
    }

    public void disableRedo() {
	this.editorRedo.setEnabled(false);
    }

    private void disableRemoveLevel() {
	this.editorRemoveLevel.setEnabled(false);
    }

    private void disableSetStartPoint() {
	this.editorSetStartPoint.setEnabled(false);
    }

    public void disableUndo() {
	this.editorUndo.setEnabled(false);
    }

    private void disableUpOneFloor() {
	this.editorUpOneFloor.setEnabled(false);
    }

    private void disableUpOneLevel() {
	this.editorUpOneLevel.setEnabled(false);
    }

    public void dungeonChanged() {
	this.dungeonChanged = true;
    }

    public void editDungeon() {
	final var app = DungeonDiver7.getStuffBag();
	if (app.getDungeonManager().getLoaded()) {
	    app.getGUIManager().hideGUI();
	    app.setInEditor();
	    // Reset game state
	    app.getGameLogic().resetGameState();
	    // Create the managers
	    if (this.dungeonChanged) {
		this.elMgr = new EditorLocationManager();
		this.elMgr.setLimitsFromDungeon(app.getDungeonManager().getDungeon());
		this.dungeonChanged = false;
	    }
	    this.setUpGUI();
	    this.updatePicker();
	    this.clearHistory();
	    this.redrawEditor();
	    this.updatePickerLayout();
	    this.resetBorderPane();
	    this.checkMenus();
	} else {
	    CommonDialogs.showDialog(Strings.menu(Menu.ERROR_NO_DUNGEON_OPENED));
	}
    }

    public void editJumpBox(final AbstractJumpObject jumper) {
	final var currentX = jumper.getJumpCols();
	final var currentY = jumper.getJumpRows();
	final var newXStr = CommonDialogs.showInputDialog(Strings.editor(EditorString.HORZ_JUMP),
		Strings.editor(EditorString.EDITOR), DungeonEditor.JUMP_LIST, DungeonEditor.JUMP_LIST[currentX]);
	if (newXStr != null) {
	    final var newYStr = CommonDialogs.showInputDialog(Strings.editor(EditorString.VERT_JUMP),
		    Strings.editor(EditorString.EDITOR), DungeonEditor.JUMP_LIST, DungeonEditor.JUMP_LIST[currentY]);
	    if (newYStr != null) {
		final var newX = Integer.parseInt(newXStr);
		final var newY = Integer.parseInt(newYStr);
		jumper.setJumpCols(newX);
		jumper.setJumpRows(newY);
	    }
	}
    }

    void editObject(final int x, final int y) {
	final var app = DungeonDiver7.getStuffBag();
	var currentObjectIndex = 0;
	if (Prefs.getEditorLayout() == EditorLayout.VERTICAL) {
	    currentObjectIndex = this.oldPicker.getPicked();
	} else if (Prefs.getEditorLayout() == EditorLayout.HORIZONTAL_STACKED) {
	    currentObjectIndex = this.newPicker11.getPicked();
	} else if (Prefs.getEditorLayout() == EditorLayout.HORIZONTAL_SIDE_BY_SIDE) {
	    currentObjectIndex = this.newPicker12.getPicked();
	}
	final var xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final var yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final var gridX = x / ImageConstants.SIZE + EditorViewingWindowManager.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final var gridY = y / ImageConstants.SIZE + EditorViewingWindowManager.getViewingWindowLocationY() + xOffset
		- yOffset;
	try {
	    this.savedDungeonObject = app.getDungeonManager().getDungeon().getCell(gridX, gridY,
		    this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    return;
	}
	final var choices = this.objects;
	final var mo = choices[currentObjectIndex];
	final var instance = mo.clone();
	this.elMgr.setEditorLocationX(gridX);
	this.elMgr.setEditorLocationY(gridY);
	this.savedDungeonObject.editorRemoveHook(gridX, gridY, this.elMgr.getEditorLocationZ());
	mo.editorPlaceHook(gridX, gridY, this.elMgr.getEditorLocationZ());
	try {
	    this.updateUndoHistory(this.savedDungeonObject, gridX, gridY, this.elMgr.getEditorLocationZ(),
		    this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationU());
	    app.getDungeonManager().getDungeon().setCell(instance, gridX, gridY, this.elMgr.getEditorLocationZ(),
		    this.elMgr.getEditorLocationW());
	    app.getDungeonManager().setDirty(true);
	    this.checkMenus();
	    this.redrawEditor();
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    app.getDungeonManager().getDungeon().setCell(this.savedDungeonObject, gridX, gridY,
		    this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
	    this.redrawEditor();
	}
    }

    void editObjectProperties(final int x, final int y) {
	final var app = DungeonDiver7.getStuffBag();
	final var xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final var yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final var gridX = x / ImageConstants.SIZE + EditorViewingWindowManager.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final var gridY = y / ImageConstants.SIZE + EditorViewingWindowManager.getViewingWindowLocationY() + xOffset
		- yOffset;
	try {
	    final var mo = app.getDungeonManager().getDungeon().getCell(gridX, gridY, this.elMgr.getEditorLocationZ(),
		    this.elMgr.getEditorLocationW());
	    this.elMgr.setEditorLocationX(gridX);
	    this.elMgr.setEditorLocationY(gridY);
	    if (!mo.defersSetProperties()) {
		final var mo2 = mo.editorPropertiesHook();
		if (mo2 == null) {
		    DungeonDiver7.getStuffBag().showMessage(Strings.editor(EditorString.NO_PROPERTIES));
		} else {
		    this.updateUndoHistory(this.savedDungeonObject, gridX, gridY, this.elMgr.getEditorLocationZ(),
			    this.elMgr.getEditorLocationW(), this.elMgr.getEditorLocationU());
		    app.getDungeonManager().getDungeon().setCell(mo2, gridX, gridY, this.elMgr.getEditorLocationZ(),
			    this.elMgr.getEditorLocationW());
		    this.checkMenus();
		    app.getDungeonManager().setDirty(true);
		}
	    } else {
		mo.editorPropertiesHook();
	    }
	    this.redrawEditor();
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Do nothing
	}
    }

    public void editPlayerLocation() {
	// Swap event handlers
	this.secondaryPane.removeMouseListener(this.mhandler);
	this.secondaryPane.addMouseListener(this.shandler);
	DungeonDiver7.getStuffBag().showMessage(Strings.editor(EditorString.SET_START_POINT));
    }

    private void enableAddLevel() {
	this.editorAddLevel.setEnabled(true);
    }

    private void enableClearHistory() {
	this.editorClearHistory.setEnabled(true);
    }

    private void enableCutLevel() {
	this.editorCutLevel.setEnabled(true);
    }

    @Override
    public void enableDirtyCommands() {
	// Do nothing
    }

    private void enableDownOneFloor() {
	this.editorDownOneFloor.setEnabled(true);
    }

    private void enableDownOneLevel() {
	this.editorDownOneLevel.setEnabled(true);
    }

    void enableGlobalMoveShoot() {
	DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().setMoveShootAllowedGlobally(true);
	this.editorGlobalMoveShoot.setText(Strings.menu(Menu.DISABLE_GLOBAL_MOVE_SHOOT));
    }

    private void enableInsertLevelFromClipboard() {
	this.editorInsertLevelFromClipboard.setEnabled(true);
    }

    @Override
    public void enableLoadedCommands() {
	// Do nothing
    }

    @Override
    public void enableModeCommands() {
	this.editorUndo.setEnabled(false);
	this.editorRedo.setEnabled(false);
	this.editorCutLevel.setEnabled(true);
	this.editorCopyLevel.setEnabled(true);
	this.editorPasteLevel.setEnabled(true);
	this.editorInsertLevelFromClipboard.setEnabled(true);
	this.editorGoToLevel.setEnabled(true);
	this.editorFillLevel.setEnabled(true);
	this.editorResizeLevel.setEnabled(true);
	this.editorLevelPreferences.setEnabled(true);
	this.editorSetStartPoint.setEnabled(true);
	this.editorSetMusic.setEnabled(true);
	this.editorChangeLayer.setEnabled(true);
	this.editorGlobalMoveShoot.setEnabled(true);
	this.editorEraDistantPast.setEnabled(true);
	this.editorEraPast.setEnabled(true);
	this.editorEraPresent.setEnabled(true);
	this.editorEraFuture.setEnabled(true);
	this.editorEraDistantFuture.setEnabled(true);
    }

    void enableOutput() {
	this.mainWindow.setEnabled(true);
	this.checkMenus();
    }

    private void enablePasteLevel() {
	this.editorPasteLevel.setEnabled(true);
    }

    public void enableRedo() {
	this.editorRedo.setEnabled(true);
    }

    private void enableRemoveLevel() {
	this.editorRemoveLevel.setEnabled(true);
    }

    private void enableSetStartPoint() {
	this.editorSetStartPoint.setEnabled(true);
    }

    public void enableUndo() {
	this.editorUndo.setEnabled(true);
    }

    private void enableUpOneFloor() {
	this.editorUpOneFloor.setEnabled(true);
    }

    private void enableUpOneLevel() {
	this.editorUpOneLevel.setEnabled(true);
    }

    public void exitEditor() {
	final var app = DungeonDiver7.getStuffBag();
	// Hide the editor
	this.hideOutput();
	final var mm = app.getDungeonManager();
	// Save the entire level
	mm.getDungeon().save();
	// Reset the player location
	GameLogic.resetPlayerLocation(0);
    }

    public void fillLevel() {
	if (this.confirmNonUndoable()) {
	    DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().fillDefault();
	    DungeonDiver7.getStuffBag().showMessage(Strings.editor(EditorString.LEVEL_FILLED));
	    DungeonDiver7.getStuffBag().getDungeonManager().setDirty(true);
	    this.redrawEditor();
	}
    }

    public void fixLimits() {
	// Fix limits
	final var app = DungeonDiver7.getStuffBag();
	if (app.getDungeonManager().getDungeon() != null && this.elMgr != null) {
	    this.elMgr.setLimitsFromDungeon(app.getDungeonManager().getDungeon());
	}
    }

    public EditorLocationManager getLocationManager() {
	return this.elMgr;
    }

    public void goToLevelHandler() {
	int locW;
	final var msg = Strings.editor(EditorString.GO_TO_LEVEL);
	String input;
	final var choices = DungeonDiver7.getStuffBag().getLevelInfoList();
	input = CommonDialogs.showInputDialog(Strings.editor(EditorString.GO_TO_WHICH_LEVEL), msg, choices, choices[0]);
	if (input != null) {
	    for (locW = 0; locW < choices.length; locW++) {
		if (input.equals(choices[locW])) {
		    this.updateEditorLevelAbsolute(locW);
		    break;
		}
	    }
	}
    }

    public void handleCloseWindow() {
	try {
	    final var app = DungeonDiver7.getStuffBag();
	    var success = false;
	    var status = CommonDialogs.DEFAULT_OPTION;
	    if (app.getDungeonManager().getDirty()) {
		status = DungeonManager.showSaveDialog();
		if (status == CommonDialogs.YES_OPTION) {
		    success = app.getDungeonManager().saveDungeon(app.getDungeonManager().isDungeonProtected());
		    if (success) {
			this.exitEditor();
		    }
		} else if (status == CommonDialogs.NO_OPTION) {
		    app.getDungeonManager().setDirty(false);
		    this.exitEditor();
		}
	    } else {
		this.exitEditor();
	    }
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	}
    }

    public void hideOutput() {
	if (this.mainWindow != null) {
	    this.mainWindow.removeWindowListener(this.mhandler);
	    this.mainWindow.restoreSaved();
	}
    }

    public boolean newDungeon() {
	final var app = DungeonDiver7.getStuffBag();
	var success = true;
	var saved = true;
	var status = 0;
	if (app.getDungeonManager().getDirty()) {
	    status = DungeonManager.showSaveDialog();
	    if (status == CommonDialogs.YES_OPTION) {
		saved = app.getDungeonManager().saveDungeon(app.getDungeonManager().isDungeonProtected());
	    } else if (status == CommonDialogs.CANCEL_OPTION) {
		saved = false;
	    } else {
		app.getDungeonManager().setDirty(false);
	    }
	}
	if (saved) {
	    app.getGameLogic().getPlayerManager().resetPlayerLocation();
	    Dungeon a = null;
	    try {
		a = DungeonManager.createDungeon();
	    } catch (final IOException ioe) {
		success = false;
	    }
	    if (success) {
		app.getDungeonManager().setDungeon(a);
		success = this.addLevelInternal();
		if (success) {
		    app.getDungeonManager().clearLastUsedFilenames();
		    this.clearHistory();
		}
	    }
	} else {
	    success = false;
	}
	if (success) {
	    this.dungeonChanged = true;
	    CommonDialogs.showDialog(Strings.editor(EditorString.DUNGEON_CREATED));
	} else {
	    CommonDialogs.showDialog(Strings.editor(EditorString.DUNGEON_CREATION_FAILED));
	}
	return success;
    }

    void probeObjectProperties(final int x, final int y) {
	final var app = DungeonDiver7.getStuffBag();
	final var xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final var yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final var gridX = x / ImageConstants.SIZE + EditorViewingWindowManager.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final var gridY = y / ImageConstants.SIZE + EditorViewingWindowManager.getViewingWindowLocationY() + xOffset
		- yOffset;
	final var mo = app.getDungeonManager().getDungeon().getCell(gridX, gridY, this.elMgr.getEditorLocationZ(),
		this.elMgr.getEditorLocationW());
	this.elMgr.setEditorLocationX(gridX);
	this.elMgr.setEditorLocationY(gridY);
	final var gameName = mo.getIdentityName();
	final var desc = mo.getDescription();
	CommonDialogs.showTitledDialog(desc, gameName);
    }

    public void redo() {
	final var app = DungeonDiver7.getStuffBag();
	this.engine.redo();
	final var obj = this.engine.getObject();
	final var x = this.engine.getX();
	final var y = this.engine.getY();
	final var z = this.engine.getZ();
	final var w = this.engine.getW();
	final var u = this.engine.getU();
	this.elMgr.setEditorLocationX(x);
	this.elMgr.setEditorLocationY(y);
	if (x != -1 && y != -1 && z != -1 && u != -1) {
	    final var oldObj = app.getDungeonManager().getDungeon().getCell(x, y, z, w);
	    app.getDungeonManager().getDungeon().setCell(obj, x, y, z, w);
	    this.updateUndoHistory(oldObj, x, y, z, w, u);
	    this.checkMenus();
	    this.redrawEditor();
	} else {
	    DungeonDiver7.getStuffBag().showMessage(Strings.editor(EditorString.NOTHING_TO_REDO));
	}
    }

    public void redrawEditor() {
	final var z = this.elMgr.getEditorLocationZ();
	final var w = this.elMgr.getEditorLocationW();
	final var u = this.elMgr.getEditorLocationU();
	final var e = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getActiveEra();
	switch (w) {
	case DungeonConstants.LAYER_LOWER_GROUND:
	    this.redrawEditorBottomGround();
	    break;
	case DungeonConstants.LAYER_UPPER_GROUND:
	    this.redrawEditorGround();
	    break;
	case DungeonConstants.LAYER_LOWER_OBJECTS:
	    this.redrawEditorGroundBottomObjects();
	    break;
	case DungeonConstants.LAYER_UPPER_OBJECTS:
	    this.redrawEditorGroundObjects();
	    break;
	default:
	    break;
	}
	this.mainWindow.checkAndSetTitle(LocaleUtils.subst(Strings.editor(EditorString.EDITOR_TITLE),
		Integer.toString(z + 1), Integer.toString(u + 1), Strings.timeTravel(e)));
	this.outputPane.repaint();
	this.showOutput();
    }

    private void redrawEditorBottomGround() {
	// Draw the dungeon in edit mode
	final var app = DungeonDiver7.getStuffBag();
	int x, y;
	int xFix, yFix;
	final var drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final var lgobj = app.getDungeonManager().getDungeon().getCell(y, x, this.elMgr.getEditorLocationZ(),
			DungeonConstants.LAYER_LOWER_GROUND);
		final var lgimg = ObjectImageLoader.load(lgobj.getImageName(), lgobj.getBaseID());
		final var img = lgimg;
		drawGrid.setImageCell(img, xFix, yFix);
	    }
	}
    }

    private void redrawEditorGround() {
	// Draw the dungeon in edit mode
	final var app = DungeonDiver7.getStuffBag();
	int x, y;
	int xFix, yFix;
	final var drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final var lgobj = app.getDungeonManager().getDungeon().getCell(y, x, this.elMgr.getEditorLocationZ(),
			DungeonConstants.LAYER_LOWER_GROUND);
		final var ugobj = app.getDungeonManager().getDungeon().getCell(y, x, this.elMgr.getEditorLocationZ(),
			DungeonConstants.LAYER_UPPER_GROUND);
		final var lgimg = ObjectImageLoader.load(lgobj.getImageName(), lgobj.getBaseID());
		final var ugimg = ObjectImageLoader.load(ugobj.getImageName(), ugobj.getBaseID());
		final var cacheName = Strings.compositeCacheName(lgobj.getCacheName(), ugobj.getCacheName());
		final var img = ImageCompositor.composite(cacheName, lgimg, ugimg);
		drawGrid.setImageCell(img, xFix, yFix);
	    }
	}
    }

    private void redrawEditorGroundBottomObjects() {
	// Draw the dungeon in edit mode
	final var app = DungeonDiver7.getStuffBag();
	int x, y;
	int xFix, yFix;
	final var drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final var lgobj = app.getDungeonManager().getDungeon().getCell(y, x, this.elMgr.getEditorLocationZ(),
			DungeonConstants.LAYER_LOWER_GROUND);
		final var ugobj = app.getDungeonManager().getDungeon().getCell(y, x, this.elMgr.getEditorLocationZ(),
			DungeonConstants.LAYER_UPPER_GROUND);
		final var loobj = app.getDungeonManager().getDungeon().getCell(y, x, this.elMgr.getEditorLocationZ(),
			DungeonConstants.LAYER_LOWER_OBJECTS);
		final var lgimg = ObjectImageLoader.load(lgobj.getImageName(), lgobj.getBaseID());
		final var ugimg = ObjectImageLoader.load(ugobj.getImageName(), ugobj.getBaseID());
		final var loimg = ObjectImageLoader.load(loobj.getImageName(), loobj.getBaseID());
		final var cacheName = Strings.compositeCacheName(lgobj.getCacheName(), ugobj.getCacheName(),
			loobj.getCacheName());
		final var img = ImageCompositor.composite(cacheName, lgimg, ugimg, loimg);
		drawGrid.setImageCell(img, xFix, yFix);
	    }
	}
    }

    private void redrawEditorGroundObjects() {
	// Draw the dungeon in edit mode
	final var app = DungeonDiver7.getStuffBag();
	int x, y;
	int xFix, yFix;
	final var drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final var lgobj = app.getDungeonManager().getDungeon().getCell(y, x, this.elMgr.getEditorLocationZ(),
			DungeonConstants.LAYER_LOWER_GROUND);
		final var ugobj = app.getDungeonManager().getDungeon().getCell(y, x, this.elMgr.getEditorLocationZ(),
			DungeonConstants.LAYER_UPPER_GROUND);
		final var loobj = app.getDungeonManager().getDungeon().getCell(y, x, this.elMgr.getEditorLocationZ(),
			DungeonConstants.LAYER_LOWER_OBJECTS);
		final var uoobj = app.getDungeonManager().getDungeon().getCell(y, x, this.elMgr.getEditorLocationZ(),
			DungeonConstants.LAYER_UPPER_OBJECTS);
		final var lvobj = app.getDungeonManager().getDungeon().getVirtualCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_VIRTUAL);
		final var lgimg = ObjectImageLoader.load(lgobj.getImageName(), lgobj.getBaseID());
		final var ugimg = ObjectImageLoader.load(ugobj.getImageName(), ugobj.getBaseID());
		final var loimg = ObjectImageLoader.load(loobj.getImageName(), loobj.getBaseID());
		final var uoimg = ObjectImageLoader.load(uoobj.getImageName(), uoobj.getBaseID());
		final var lvimg = ObjectImageLoader.load(lvobj.getImageName(), lvobj.getBaseID());
		final var cacheName = Strings.compositeCacheName(lgobj.getCacheName(), ugobj.getCacheName(),
			loobj.getCacheName(), uoobj.getCacheName(), lvobj.getCacheName());
		final var img = ImageCompositor.composite(cacheName, lgimg, ugimg, loimg, uoimg, lvimg);
		drawGrid.setImageCell(img, xFix, yFix);
	    }
	}
    }

    public boolean removeLevel() {
	final var app = DungeonDiver7.getStuffBag();
	int level;
	var success = true;
	var choices = app.getLevelInfoList();
	if (choices == null) {
	    choices = app.getLevelInfoList();
	}
	String input;
	input = CommonDialogs.showInputDialog(Strings.editor(EditorString.WHICH_LEVEL_TO_REMOVE),
		Strings.editor(EditorString.REMOVE_LEVEL), choices, choices[0]);
	if (input != null) {
	    for (level = 0; level < choices.length; level++) {
		if (input.equals(choices[level])) {
		    success = app.getDungeonManager().getDungeon().removeLevel(level);
		    if (success) {
			this.fixLimits();
			if (level == this.elMgr.getEditorLocationU()) {
			    // Deleted current level - go to level 1
			    this.updateEditorLevelAbsolute(0);
			}
			this.checkMenus();
			app.getDungeonManager().setDirty(true);
		    }
		    break;
		}
	    }
	} else {
	    // User canceled
	    success = false;
	}
	return success;
    }

    public void resetBorderPane() {
	if (this.borderPane != null) {
	    this.updatePicker();
	    this.borderPane.removeAll();
	    this.borderPane.add(this.outerOutputPane, BorderLayout.CENTER);
	    this.borderPane.add(this.messageLabel, BorderLayout.NORTH);
	    if (Prefs.getEditorLayout() == EditorLayout.VERTICAL) {
		this.borderPane.add(this.oldPicker.getPicker(), BorderLayout.EAST);
	    } else if (Prefs.getEditorLayout() == EditorLayout.HORIZONTAL_STACKED) {
		this.borderPane.add(this.newPicker11.getPicker(), BorderLayout.EAST);
	    } else if (Prefs.getEditorLayout() == EditorLayout.HORIZONTAL_SIDE_BY_SIDE) {
		this.borderPane.add(this.newPicker12.getPicker(), BorderLayout.EAST);
	    }
	    this.borderPane.add(this.switcherPane, BorderLayout.SOUTH);
	}
    }

    public boolean resizeLevel() {
	final var app = DungeonDiver7.getStuffBag();
	int levelSizeZ;
	final var maxF = Dungeon.getMaxFloors();
	final var minF = Dungeon.getMinFloors();
	final var msg = Strings.editor(EditorString.RESIZE_LEVEL);
	var success = true;
	String input3;
	input3 = CommonDialogs.showTextInputDialogWithDefault(Strings.editor(EditorString.NUMBER_OF_FLOORS), msg,
		Integer.toString(app.getDungeonManager().getDungeon().getFloors()));
	if (input3 != null) {
	    try {
		levelSizeZ = Integer.parseInt(input3);
		if (levelSizeZ < minF) {
		    throw new NumberFormatException(Strings.editor(EditorString.FLOORS_TOO_LOW));
		}
		if (levelSizeZ > maxF) {
		    throw new NumberFormatException(Strings.editor(EditorString.FLOORS_TOO_HIGH));
		}
		app.getDungeonManager().getDungeon().resize(levelSizeZ, new Ground());
		this.fixLimits();
		// Save the entire level
		app.getDungeonManager().getDungeon().save();
		this.checkMenus();
		// Redraw
		this.redrawEditor();
	    } catch (final NumberFormatException nf) {
		CommonDialogs.showDialog(nf.getMessage());
		success = false;
	    }
	} else {
	    // User canceled
	    success = false;
	}
	return success;
    }

    @Override
    public void setInitialState() {
	this.disableModeCommands();
    }

    public void setLevelPrefs() {
	this.lPrefs.showPrefs();
    }

    public void setMusicFilename(final String fn) {
	this.eme.setMusicFilename(fn);
    }

    public void setPlayerLocation() {
	final var template = new Party(this.activePlayer + 1);
	final var app = DungeonDiver7.getStuffBag();
	final var oldX = app.getDungeonManager().getDungeon().getStartColumn(this.activePlayer);
	final var oldY = app.getDungeonManager().getDungeon().getStartRow(this.activePlayer);
	final var oldZ = app.getDungeonManager().getDungeon().getStartFloor(this.activePlayer);
	// Erase old player
	try {
	    app.getDungeonManager().getDungeon().setCell(new Ground(), oldX, oldY, oldZ, template.getLayer());
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Ignore
	}
	// Set new player
	app.getDungeonManager().getDungeon().setStartRow(this.activePlayer, this.elMgr.getEditorLocationY());
	app.getDungeonManager().getDungeon().setStartColumn(this.activePlayer, this.elMgr.getEditorLocationX());
	app.getDungeonManager().getDungeon().setStartFloor(this.activePlayer, this.elMgr.getEditorLocationZ());
	app.getDungeonManager().getDungeon().setCell(template, this.elMgr.getEditorLocationX(),
		this.elMgr.getEditorLocationY(), this.elMgr.getEditorLocationZ(), template.getLayer());
    }

    void setPlayerLocation(final int x, final int y) {
	final var template = new Party(this.activePlayer + 1);
	final var app = DungeonDiver7.getStuffBag();
	final var xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final var yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final var destX = x / ImageConstants.SIZE + EditorViewingWindowManager.getViewingWindowLocationX() - xOffset
		+ yOffset;
	final var destY = y / ImageConstants.SIZE + EditorViewingWindowManager.getViewingWindowLocationY() + xOffset
		- yOffset;
	final var oldX = app.getDungeonManager().getDungeon().getStartColumn(this.activePlayer);
	final var oldY = app.getDungeonManager().getDungeon().getStartRow(this.activePlayer);
	final var oldZ = app.getDungeonManager().getDungeon().getStartFloor(this.activePlayer);
	// Erase old player
	try {
	    app.getDungeonManager().getDungeon().setCell(new Ground(), oldX, oldY, oldZ, template.getLayer());
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Ignore
	}
	// Set new player
	try {
	    app.getDungeonManager().getDungeon().setStartRow(this.activePlayer, destY);
	    app.getDungeonManager().getDungeon().setStartColumn(this.activePlayer, destX);
	    app.getDungeonManager().getDungeon().setStartFloor(this.activePlayer, this.elMgr.getEditorLocationZ());
	    app.getDungeonManager().getDungeon().setCell(template, destX, destY, this.elMgr.getEditorLocationZ(),
		    template.getLayer());
	    DungeonDiver7.getStuffBag().showMessage(Strings.editor(EditorString.START_POINT_SET));
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    try {
		app.getDungeonManager().getDungeon().setStartRow(this.activePlayer, oldY);
		app.getDungeonManager().getDungeon().setStartColumn(this.activePlayer, oldX);
		app.getDungeonManager().getDungeon().setCell(template, oldX, oldY, oldZ, template.getLayer());
	    } catch (final ArrayIndexOutOfBoundsException aioob2) {
		// Ignore
	    }
	    DungeonDiver7.getStuffBag().showMessage(Strings.editor(EditorString.AIM_WITHIN_THE_ARENA));
	}
	// Swap event handlers
	this.secondaryPane.removeMouseListener(this.shandler);
	this.secondaryPane.addMouseListener(this.mhandler);
	// Set dirty flag
	app.getDungeonManager().setDirty(true);
	this.redrawEditor();
    }

    public void setStatusMessage(final String msg) {
	this.messageLabel.setText(msg);
    }

    private void setUpGUI() {
	this.messageLabel = new JLabel(Strings.SPACE);
	this.mainWindow = MainWindow.mainWindow();
	this.outputPane = new EditorDraw();
	this.secondaryPane = this.mainWindow.createContent();
	this.borderPane = this.mainWindow.createContent();
	this.messageLabel.setLabelFor(this.outputPane);
	this.outerOutputPane = RCLGenerator.generateRowColumnLabels();
	this.outerOutputPane.add(this.outputPane, BorderLayout.CENTER);
	this.outputPane.setLayout(new GridLayout(1, 1));
	this.secondaryPane.setLayout(new GridLayout(EditorViewingWindowManager.getViewingWindowSizeX(),
		EditorViewingWindowManager.getViewingWindowSizeY()));
	this.horzScroll = new JScrollBar(Adjustable.HORIZONTAL,
		EditorViewingWindowManager.getMinimumViewingWindowLocationY(),
		EditorViewingWindowManager.getViewingWindowSizeY(),
		EditorViewingWindowManager.getMinimumViewingWindowLocationY(),
		EditorViewingWindowManager.getViewingWindowSizeY());
	this.vertScroll = new JScrollBar(Adjustable.VERTICAL,
		EditorViewingWindowManager.getMinimumViewingWindowLocationX(),
		EditorViewingWindowManager.getViewingWindowSizeX(),
		EditorViewingWindowManager.getMinimumViewingWindowLocationX(),
		EditorViewingWindowManager.getViewingWindowSizeX());
	this.outputPane.add(this.secondaryPane);
	this.secondaryPane.addMouseListener(this.mhandler);
	this.secondaryPane.addMouseMotionListener(this.mhandler);
	this.switcherPane = this.mainWindow.createContent();
	final var switcherHandler = new SwitcherHandler();
	final var switcherGroup = new ButtonGroup();
	this.lowerGround = new JToggleButton(Strings.editor(EditorString.LOWER_GROUND_LAYER));
	this.upperGround = new JToggleButton(Strings.editor(EditorString.UPPER_GROUND_LAYER));
	this.lowerObjects = new JToggleButton(Strings.editor(EditorString.LOWER_OBJECTS_LAYER));
	this.upperObjects = new JToggleButton(Strings.editor(EditorString.UPPER_OBJECTS_LAYER));
	this.lowerGround.setSelected(true);
	this.lowerGround.addActionListener(switcherHandler);
	this.upperGround.addActionListener(switcherHandler);
	this.lowerObjects.addActionListener(switcherHandler);
	this.upperObjects.addActionListener(switcherHandler);
	switcherGroup.add(this.lowerGround);
	switcherGroup.add(this.upperGround);
	switcherGroup.add(this.lowerObjects);
	switcherGroup.add(this.upperObjects);
	this.switcherPane.setLayout(new FlowLayout());
	this.switcherPane.add(this.lowerGround);
	this.switcherPane.add(this.upperGround);
	this.switcherPane.add(this.lowerObjects);
	this.switcherPane.add(this.upperObjects);
    }

    public void showOutput() {
	final var app = DungeonDiver7.getStuffBag();
	Integration.integrate().setDefaultMenuBar(app.getMenus().getMainMenuBar());
	app.getMenus().checkFlags();
	this.mainWindow.setAndSave(this.borderPane, Strings.editor(EditorString.EDITOR));
	this.mainWindow.addWindowListener(this.mhandler);
    }

    public void undo() {
	final var app = DungeonDiver7.getStuffBag();
	this.engine.undo();
	final var obj = this.engine.getObject();
	final var x = this.engine.getX();
	final var y = this.engine.getY();
	final var z = this.engine.getZ();
	final var w = this.engine.getW();
	final var u = this.engine.getU();
	this.elMgr.setEditorLocationX(x);
	this.elMgr.setEditorLocationY(y);
	if (x != -1 && y != -1 && z != -1 && u != -1) {
	    final var oldObj = app.getDungeonManager().getDungeon().getCell(x, y, z, w);
	    app.getDungeonManager().getDungeon().setCell(obj, x, y, z, w);
	    this.updateRedoHistory(oldObj, x, y, z, w, u);
	    this.checkMenus();
	    this.redrawEditor();
	} else {
	    DungeonDiver7.getStuffBag().showMessage(Strings.editor(EditorString.NOTHING_TO_UNDO));
	}
    }

    public void updateEditorLevelAbsolute(final int w) {
	this.elMgr.setEditorLocationU(w);
	// Level Change
	DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().switchLevel(w);
	this.fixLimits();
	this.setUpGUI();
	this.checkMenus();
	this.redrawEditor();
    }

    public void updateEditorPosition(final int z, final int w) {
	this.elMgr.offsetEditorLocationU(w);
	this.elMgr.offsetEditorLocationZ(z);
	if (w != 0) {
	    // Level Change
	    DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().switchLevelOffset(w);
	    this.fixLimits();
	    this.setUpGUI();
	}
	this.checkMenus();
	this.redrawEditor();
    }

    private void updateNewPicker11() {
	final var newImages = this.editorAppearances;
	final var enabled = this.objectsEnabled;
	if (this.newPicker11 != null) {
	    this.newPicker11.updatePicker(newImages, enabled);
	} else {
	    this.newPicker11 = new StackedPicturePicker(newImages, enabled, DungeonEditor.STACK_COUNT,
		    ImageConstants.SIZE);
	}
    }

    private void updateNewPicker11Layout() {
	if (this.newPicker11 != null) {
	    this.newPicker11
		    .updatePickerLayout(this.outputPane.getLayout().preferredLayoutSize(this.outputPane).height);
	}
    }

    private void updateNewPicker12() {
	final var newImages = this.editorAppearances;
	final var enabled = this.objectsEnabled;
	if (this.newPicker12 != null) {
	    this.newPicker12.updatePicker(newImages, enabled);
	} else {
	    this.newPicker12 = new SXSPicturePicker(newImages, enabled, DungeonEditor.STACK_COUNT);
	}
    }

    private void updateNewPicker12Layout() {
	if (this.newPicker12 != null) {
	    this.newPicker12
		    .updatePickerLayout(this.outputPane.getLayout().preferredLayoutSize(this.outputPane).height);
	}
    }

    private void updateOldPicker() {
	final var newImages = this.editorAppearances;
	final var newNames = this.names;
	final var enabled = this.objectsEnabled;
	if (this.oldPicker != null) {
	    this.oldPicker.updatePicker(newImages, newNames, enabled);
	} else {
	    this.oldPicker = new PicturePicker(newImages, newNames, enabled);
	}
    }

    private void updateOldPickerLayout() {
	if (this.oldPicker != null) {
	    this.oldPicker.updatePickerLayout(this.outputPane.getLayout().preferredLayoutSize(this.outputPane).height);
	}
    }

    private void updatePicker() {
	if (this.elMgr != null) {
	    final var objectList = DungeonDiver7.getStuffBag().getObjects();
	    this.names = objectList.getAllNamesOnLayer(this.elMgr.getEditorLocationW());
	    this.objects = objectList.getAllObjectsOnLayer(this.elMgr.getEditorLocationW(),
		    Prefs.getEditorShowAllObjects());
	    this.editorAppearances = objectList.getAllEditorAppearancesOnLayer(this.elMgr.getEditorLocationW(),
		    Prefs.getEditorShowAllObjects());
	    this.objectsEnabled = objectList.getObjectEnabledStatuses(this.elMgr.getEditorLocationW());
	    if (Prefs.getEditorLayout() == EditorLayout.VERTICAL) {
		this.updateOldPicker();
	    } else if (Prefs.getEditorLayout() == EditorLayout.HORIZONTAL_STACKED) {
		this.updateNewPicker11();
	    } else if (Prefs.getEditorLayout() == EditorLayout.HORIZONTAL_SIDE_BY_SIDE) {
		this.updateNewPicker12();
	    }
	    this.updatePickerLayout();
	}
    }

    private void updatePickerLayout() {
	if (Prefs.getEditorLayout() == EditorLayout.VERTICAL) {
	    this.updateOldPickerLayout();
	} else if (Prefs.getEditorLayout() == EditorLayout.HORIZONTAL_STACKED) {
	    this.updateNewPicker11Layout();
	} else if (Prefs.getEditorLayout() == EditorLayout.HORIZONTAL_SIDE_BY_SIDE) {
	    this.updateNewPicker12Layout();
	}
    }

    private void updateRedoHistory(final DungeonObject obj, final int x, final int y, final int z, final int w,
	    final int u) {
	this.engine.updateRedoHistory(obj, x, y, z, w, u);
    }

    private void updateUndoHistory(final DungeonObject obj, final int x, final int y, final int z, final int w,
	    final int u) {
	this.engine.updateUndoHistory(obj, x, y, z, w, u);
    }
}
