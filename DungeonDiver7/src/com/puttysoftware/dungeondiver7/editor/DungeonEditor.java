/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.editor;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.Accelerators;
import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.MenuSection;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.DungeonManager;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractJumpObject;
import com.puttysoftware.dungeondiver7.dungeon.objects.Ground;
import com.puttysoftware.dungeondiver7.dungeon.objects.Party;
import com.puttysoftware.dungeondiver7.game.GameManager;
import com.puttysoftware.dungeondiver7.loaders.ImageLoader;
import com.puttysoftware.dungeondiver7.loaders.LogoLoader;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utilities.DungeonConstants;
import com.puttysoftware.dungeondiver7.utilities.DungeonObjects;
import com.puttysoftware.dungeondiver7.utilities.DrawGrid;
import com.puttysoftware.dungeondiver7.utilities.EditorLayoutConstants;
import com.puttysoftware.dungeondiver7.utilities.InvalidDungeonException;
import com.puttysoftware.dungeondiver7.utilities.RCLGenerator;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.picturepicker.PicturePicker;
import com.puttysoftware.picturepicker.SXSPicturePicker;
import com.puttysoftware.picturepicker.StackedPicturePicker;

public class DungeonEditor implements MenuSection {
    // Declarations
    private JFrame outputFrame;
    private Container secondaryPane, borderPane, outerOutputPane, switcherPane;
    private EditorDraw outputPane;
    private JToggleButton lowerGround, upperGround, lowerObjects, upperObjects;
    private JLabel messageLabel;
    private AbstractDungeonObject savedDungeonObject;
    private JScrollBar vertScroll, horzScroll;
    private final EventHandler mhandler;
    private final StartEventHandler shandler;
    private final LevelPreferencesManager lPrefs;
    private PicturePicker oldPicker;
    private StackedPicturePicker newPicker11;
    private SXSPicturePicker newPicker12;
    private String[] names;
    private AbstractDungeonObject[] objects;
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
    private static final int STACK_COUNT = 10;
    private static final String[] JUMP_LIST = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

    public DungeonEditor() {
	this.savedDungeonObject = new Ground();
	this.lPrefs = new LevelPreferencesManager();
	this.mhandler = new EventHandler();
	this.shandler = new StartEventHandler();
	this.engine = new EditorUndoRedoEngine();
	final DungeonObjects objectList = DungeonDiver7.getApplication().getObjects();
	this.names = objectList.getAllNamesOnLayer(DungeonConstants.LAYER_LOWER_GROUND);
	this.objects = objectList.getAllObjectsOnLayer(DungeonConstants.LAYER_LOWER_GROUND,
		PrefsManager.getEditorShowAllObjects());
	this.editorAppearances = objectList.getAllEditorAppearancesOnLayer(DungeonConstants.LAYER_LOWER_GROUND,
		PrefsManager.getEditorShowAllObjects());
	this.objectsEnabled = objectList.getObjectEnabledStatuses(DungeonConstants.LAYER_LOWER_GROUND);
	this.dungeonChanged = true;
	this.eme = new ExternalMusicEditor();
	this.activePlayer = 0;
    }

    public void activeLanguageChanged() {
	EditorLayoutConstants.activeLanguageChanged();
	this.updatePicker();
    }

    public void defineDungeonMusic() {
	this.hideOutput();
	this.eme.edit();
    }

    void enableGlobalMoveShoot() {
	DungeonDiver7.getApplication().getDungeonManager().getDungeon().setMoveShootAllowedGlobally(true);
	this.editorGlobalMoveShoot.setText(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_DISABLE_GLOBAL_MOVE_SHOOT));
    }

    void disableGlobalMoveShoot() {
	DungeonDiver7.getApplication().getDungeonManager().getDungeon().setMoveShootAllowedGlobally(false);
	this.editorGlobalMoveShoot.setText(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_ENABLE_GLOBAL_MOVE_SHOOT));
    }

    public void setMusicFilename(final String fn) {
	this.eme.setMusicFilename(fn);
    }

    public void dungeonChanged() {
	this.dungeonChanged = true;
    }

    public EditorLocationManager getLocationManager() {
	return this.elMgr;
    }

    public void changeLayer() {
	final String[] list = DungeonConstants.getLayerList();
	final String choice = CommonDialogs.showInputDialog(
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_CHANGE_LAYER_PROMPT),
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE, LocaleConstants.EDITOR_STRING_EDITOR),
		list, list[this.elMgr.getEditorLocationW()]);
	if (choice != null) {
	    final int len = list.length;
	    int index = -1;
	    for (int z = 0; z < len; z++) {
		if (choice.equals(list[z])) {
		    index = z;
		    break;
		}
	    }
	    if (index != -1) {
		// Update selected button
		if (index == DungeonConstants.LAYER_LOWER_GROUND) {
		    this.lowerGround.setSelected(true);
		} else if (index == DungeonConstants.LAYER_UPPER_GROUND) {
		    this.upperGround.setSelected(true);
		} else if (index == DungeonConstants.LAYER_LOWER_OBJECTS) {
		    this.lowerObjects.setSelected(true);
		} else if (index == DungeonConstants.LAYER_UPPER_OBJECTS) {
		    this.upperObjects.setSelected(true);
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

    public void updateEditorPosition(final int z, final int w) {
	this.elMgr.offsetEditorLocationU(w);
	this.elMgr.offsetEditorLocationZ(z);
	if (w != 0) {
	    // Level Change
	    DungeonDiver7.getApplication().getDungeonManager().getDungeon().switchLevelOffset(w);
	    this.fixLimits();
	    this.setUpGUI();
	}
	this.checkMenus();
	this.redrawEditor();
    }

    public void updateEditorLevelAbsolute(final int w) {
	this.elMgr.setEditorLocationU(w);
	// Level Change
	DungeonDiver7.getApplication().getDungeonManager().getDungeon().switchLevel(w);
	this.fixLimits();
	this.setUpGUI();
	this.checkMenus();
	this.redrawEditor();
    }

    private void checkMenus() {
	final Application app = DungeonDiver7.getApplication();
	if (app.getMode() == Application.STATUS_EDITOR) {
	    final AbstractDungeon m = app.getDungeonManager().getDungeon();
	    if (m.getLevels() == AbstractDungeon.getMinLevels()) {
		this.disableRemoveLevel();
	    } else {
		this.enableRemoveLevel();
	    }
	    if (m.getLevels() == AbstractDungeon.getMaxLevels()) {
		this.disableAddLevel();
	    } else {
		this.enableAddLevel();
	    }
	    try {
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
	    } catch (final NullPointerException npe) {
		this.disableDownOneFloor();
		this.disableUpOneFloor();
	    }
	    try {
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
	    } catch (final NullPointerException npe) {
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

    public void setLevelPrefs() {
	this.lPrefs.showPrefs();
    }

    public void redrawEditor() {
	final int z = this.elMgr.getEditorLocationZ();
	final int w = this.elMgr.getEditorLocationW();
	final int u = this.elMgr.getEditorLocationU();
	final int e = DungeonDiver7.getApplication().getDungeonManager().getDungeon().getActiveEraNumber();
	if (w == DungeonConstants.LAYER_LOWER_GROUND) {
	    this.redrawEditorBottomGround();
	} else if (w == DungeonConstants.LAYER_UPPER_GROUND) {
	    this.redrawEditorGround();
	} else if (w == DungeonConstants.LAYER_LOWER_OBJECTS) {
	    this.redrawEditorGroundBottomObjects();
	} else if (w == DungeonConstants.LAYER_UPPER_OBJECTS) {
	    this.redrawEditorGroundObjects();
	}
	this.outputFrame.pack();
	this.outputFrame.setTitle(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		LocaleConstants.EDITOR_STRING_EDITOR_TITLE_1)
		+ (z + 1)
		+ LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_EDITOR_TITLE_2)
		+ (u + 1) + LocaleConstants.COMMON_STRING_SPACE_DASH_SPACE
		+ LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, e));
	this.outputPane.repaint();
	this.showOutput();
    }

    private void redrawEditorBottomGround() {
	// Draw the dungeon in edit mode
	final Application app = DungeonDiver7.getApplication();
	int x, y;
	int xFix, yFix;
	final DrawGrid drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final AbstractDungeonObject lgobj = app.getDungeonManager().getDungeon().getCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_LOWER_GROUND);
		drawGrid.setImageCell(ImageLoader.getImage(lgobj, true), xFix, yFix);
	    }
	}
    }

    private void redrawEditorGround() {
	// Draw the dungeon in edit mode
	final Application app = DungeonDiver7.getApplication();
	int x, y;
	int xFix, yFix;
	final DrawGrid drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final AbstractDungeonObject lgobj = app.getDungeonManager().getDungeon().getCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_LOWER_GROUND);
		final AbstractDungeonObject ugobj = app.getDungeonManager().getDungeon().getCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_UPPER_GROUND);
		drawGrid.setImageCell(ImageLoader.getCompositeImage(lgobj, ugobj, true), xFix, yFix);
	    }
	}
    }

    private void redrawEditorGroundBottomObjects() {
	// Draw the dungeon in edit mode
	final Application app = DungeonDiver7.getApplication();
	int x, y;
	int xFix, yFix;
	final DrawGrid drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final AbstractDungeonObject lgobj = app.getDungeonManager().getDungeon().getCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_LOWER_GROUND);
		final AbstractDungeonObject ugobj = app.getDungeonManager().getDungeon().getCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_UPPER_GROUND);
		final AbstractDungeonObject loobj = app.getDungeonManager().getDungeon().getCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_LOWER_OBJECTS);
		drawGrid.setImageCell(ImageLoader.getVirtualCompositeImage(lgobj, ugobj, loobj), xFix, yFix);
	    }
	}
    }

    private void redrawEditorGroundObjects() {
	// Draw the dungeon in edit mode
	final Application app = DungeonDiver7.getApplication();
	int x, y;
	int xFix, yFix;
	final DrawGrid drawGrid = this.outputPane.getGrid();
	for (x = EditorViewingWindowManager.getViewingWindowLocationX(); x <= EditorViewingWindowManager
		.getLowerRightViewingWindowLocationX(); x++) {
	    for (y = EditorViewingWindowManager.getViewingWindowLocationY(); y <= EditorViewingWindowManager
		    .getLowerRightViewingWindowLocationY(); y++) {
		xFix = x - EditorViewingWindowManager.getViewingWindowLocationX();
		yFix = y - EditorViewingWindowManager.getViewingWindowLocationY();
		final AbstractDungeonObject lgobj = app.getDungeonManager().getDungeon().getCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_LOWER_GROUND);
		final AbstractDungeonObject ugobj = app.getDungeonManager().getDungeon().getCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_UPPER_GROUND);
		final AbstractDungeonObject loobj = app.getDungeonManager().getDungeon().getCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_LOWER_OBJECTS);
		final AbstractDungeonObject uoobj = app.getDungeonManager().getDungeon().getCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_UPPER_OBJECTS);
		final AbstractDungeonObject lvobj = app.getDungeonManager().getDungeon().getVirtualCell(y, x,
			this.elMgr.getEditorLocationZ(), DungeonConstants.LAYER_VIRTUAL);
		drawGrid.setImageCell(ImageLoader.getVirtualCompositeImage(lgobj, ugobj, loobj, uoobj, lvobj), xFix,
			yFix);
	    }
	}
    }

    void editObject(final int x, final int y) {
	final Application app = DungeonDiver7.getApplication();
	int currentObjectIndex = 0;
	if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_CLASSIC) {
	    currentObjectIndex = this.oldPicker.getPicked();
	} else if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_MODERN_V11) {
	    currentObjectIndex = this.newPicker11.getPicked();
	} else if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_MODERN_V12) {
	    currentObjectIndex = this.newPicker12.getPicked();
	}
	final int xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final int yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final int gridX = x / ImageLoader.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationX()
		- xOffset + yOffset;
	final int gridY = y / ImageLoader.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationY()
		+ xOffset - yOffset;
	try {
	    this.savedDungeonObject = app.getDungeonManager().getDungeon().getCell(gridX, gridY,
		    this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
	} catch (final ArrayIndexOutOfBoundsException ae) {
	    return;
	}
	final AbstractDungeonObject[] choices = this.objects;
	final AbstractDungeonObject mo = choices[currentObjectIndex];
	final AbstractDungeonObject instance = mo.clone();
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

    void probeObjectProperties(final int x, final int y) {
	final Application app = DungeonDiver7.getApplication();
	final int xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final int yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final int gridX = x / ImageLoader.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationX()
		- xOffset + yOffset;
	final int gridY = y / ImageLoader.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationY()
		+ xOffset - yOffset;
	final AbstractDungeonObject mo = app.getDungeonManager().getDungeon().getCell(gridX, gridY,
		this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
	this.elMgr.setEditorLocationX(gridX);
	this.elMgr.setEditorLocationY(gridY);
	final String gameName = mo.getIdentityName();
	final String desc = mo.getDescription();
	CommonDialogs.showTitledDialog(desc, gameName);
    }

    void editObjectProperties(final int x, final int y) {
	final Application app = DungeonDiver7.getApplication();
	final int xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final int yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final int gridX = x / ImageLoader.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationX()
		- xOffset + yOffset;
	final int gridY = y / ImageLoader.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationY()
		+ xOffset - yOffset;
	try {
	    final AbstractDungeonObject mo = app.getDungeonManager().getDungeon().getCell(gridX, gridY,
		    this.elMgr.getEditorLocationZ(), this.elMgr.getEditorLocationW());
	    this.elMgr.setEditorLocationX(gridX);
	    this.elMgr.setEditorLocationY(gridY);
	    if (!mo.defersSetProperties()) {
		final AbstractDungeonObject mo2 = mo.editorPropertiesHook();
		if (mo2 == null) {
		    DungeonDiver7.getApplication().showMessage(LocaleLoader.loadString(
			    LocaleConstants.EDITOR_STRINGS_FILE, LocaleConstants.EDITOR_STRING_NO_PROPERTIES));
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

    public void setStatusMessage(final String msg) {
	this.messageLabel.setText(msg);
    }

    public void editPlayerLocation() {
	// Swap event handlers
	this.secondaryPane.removeMouseListener(this.mhandler);
	this.secondaryPane.addMouseListener(this.shandler);
	DungeonDiver7.getApplication().showMessage(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		LocaleConstants.EDITOR_STRING_SET_START_POINT));
    }

    public void setPlayerLocation() {
	final Party template = new Party(this.activePlayer + 1);
	final Application app = DungeonDiver7.getApplication();
	final int oldX = app.getDungeonManager().getDungeon().getStartColumn(this.activePlayer);
	final int oldY = app.getDungeonManager().getDungeon().getStartRow(this.activePlayer);
	final int oldZ = app.getDungeonManager().getDungeon().getStartFloor(this.activePlayer);
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
	final Party template = new Party(this.activePlayer + 1);
	final Application app = DungeonDiver7.getApplication();
	final int xOffset = this.vertScroll.getValue() - this.vertScroll.getMinimum();
	final int yOffset = this.horzScroll.getValue() - this.horzScroll.getMinimum();
	final int destX = x / ImageLoader.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationX()
		- xOffset + yOffset;
	final int destY = y / ImageLoader.getGraphicSize() + EditorViewingWindowManager.getViewingWindowLocationY()
		+ xOffset - yOffset;
	final int oldX = app.getDungeonManager().getDungeon().getStartColumn(this.activePlayer);
	final int oldY = app.getDungeonManager().getDungeon().getStartRow(this.activePlayer);
	final int oldZ = app.getDungeonManager().getDungeon().getStartFloor(this.activePlayer);
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
	    DungeonDiver7.getApplication().showMessage(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		    LocaleConstants.EDITOR_STRING_START_POINT_SET));
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    try {
		app.getDungeonManager().getDungeon().setStartRow(this.activePlayer, oldY);
		app.getDungeonManager().getDungeon().setStartColumn(this.activePlayer, oldX);
		app.getDungeonManager().getDungeon().setCell(template, oldX, oldY, oldZ, template.getLayer());
	    } catch (final ArrayIndexOutOfBoundsException aioob2) {
		// Ignore
	    }
	    DungeonDiver7.getApplication().showMessage(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		    LocaleConstants.EDITOR_STRING_AIM_WITHIN_THE_ARENA));
	}
	// Swap event handlers
	this.secondaryPane.removeMouseListener(this.shandler);
	this.secondaryPane.addMouseListener(this.mhandler);
	// Set dirty flag
	app.getDungeonManager().setDirty(true);
	this.redrawEditor();
    }

    public void editJumpBox(final AbstractJumpObject jumper) {
	final int currentX = jumper.getJumpCols();
	final int currentY = jumper.getJumpRows();
	final String newXStr = CommonDialogs.showInputDialog(
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE, LocaleConstants.EDITOR_STRING_HORZ_JUMP),
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE, LocaleConstants.EDITOR_STRING_EDITOR),
		DungeonEditor.JUMP_LIST, DungeonEditor.JUMP_LIST[currentX]);
	if (newXStr != null) {
	    final String newYStr = CommonDialogs.showInputDialog(
		    LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			    LocaleConstants.EDITOR_STRING_VERT_JUMP),
		    LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE, LocaleConstants.EDITOR_STRING_EDITOR),
		    DungeonEditor.JUMP_LIST, DungeonEditor.JUMP_LIST[currentY]);
	    if (newYStr != null) {
		final int newX = Integer.parseInt(newXStr);
		final int newY = Integer.parseInt(newYStr);
		jumper.setJumpCols(newX);
		jumper.setJumpRows(newY);
	    }
	}
    }

    public void editDungeon() {
	final Application app = DungeonDiver7.getApplication();
	if (app.getDungeonManager().getLoaded()) {
	    app.getGUIManager().hideGUI();
	    app.setInEditor();
	    // Reset game state
	    app.getGameManager().resetGameState();
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
	    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		    LocaleConstants.MENU_STRING_ERROR_NO_ARENA_OPENED));
	}
    }

    public boolean newDungeon() {
	final Application app = DungeonDiver7.getApplication();
	boolean success = true;
	boolean saved = true;
	int status = 0;
	if (app.getDungeonManager().getDirty()) {
	    status = DungeonManager.showSaveDialog();
	    if (status == JOptionPane.YES_OPTION) {
		saved = app.getDungeonManager().saveDungeon(app.getDungeonManager().isDungeonProtected());
	    } else if (status == JOptionPane.CANCEL_OPTION) {
		saved = false;
	    } else {
		app.getDungeonManager().setDirty(false);
	    }
	}
	if (saved) {
	    app.getGameManager().getPlayerManager().resetPlayerLocation();
	    AbstractDungeon a = null;
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
	    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		    LocaleConstants.EDITOR_STRING_ARENA_CREATED));
	} else {
	    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		    LocaleConstants.EDITOR_STRING_ARENA_CREATION_FAILED));
	}
	return success;
    }

    public void fixLimits() {
	// Fix limits
	final Application app = DungeonDiver7.getApplication();
	if (app.getDungeonManager().getDungeon() != null && this.elMgr != null) {
	    this.elMgr.setLimitsFromDungeon(app.getDungeonManager().getDungeon());
	}
    }

    private boolean confirmNonUndoable() {
	final int confirm = CommonDialogs.showConfirmDialog(
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_CONFIRM_CANNOT_BE_UNDONE),
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE, LocaleConstants.EDITOR_STRING_EDITOR));
	if (confirm == JOptionPane.YES_OPTION) {
	    this.clearHistory();
	    return true;
	}
	return false;
    }

    public void fillLevel() {
	if (this.confirmNonUndoable()) {
	    DungeonDiver7.getApplication().getDungeonManager().getDungeon().fillDefault();
	    DungeonDiver7.getApplication().showMessage(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		    LocaleConstants.EDITOR_STRING_LEVEL_FILLED));
	    DungeonDiver7.getApplication().getDungeonManager().setDirty(true);
	    this.redrawEditor();
	}
    }

    public boolean addLevel() {
	final boolean success = this.addLevelInternal();
	if (success) {
	    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		    LocaleConstants.EDITOR_STRING_LEVEL_ADDED));
	} else {
	    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		    LocaleConstants.EDITOR_STRING_LEVEL_ADDING_FAILED));
	}
	return success;
    }

    private boolean addLevelInternal() {
	final Application app = DungeonDiver7.getApplication();
	boolean success = true;
	final int saveLevel = app.getDungeonManager().getDungeon().getActiveLevelNumber();
	success = app.getDungeonManager().getDungeon().addLevel();
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

    public boolean removeLevel() {
	final Application app = DungeonDiver7.getApplication();
	int level;
	boolean success = true;
	String[] choices = app.getLevelInfoList();
	if (choices == null) {
	    choices = app.getLevelInfoList();
	}
	String input;
	input = CommonDialogs.showInputDialog(
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_WHICH_LEVEL_TO_REMOVE),
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_REMOVE_LEVEL),
		choices, choices[0]);
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

    public boolean resizeLevel() {
	final Application app = DungeonDiver7.getApplication();
	int levelSizeZ;
	final int maxF = AbstractDungeon.getMaxFloors();
	final int minF = AbstractDungeon.getMinFloors();
	final String msg = LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		LocaleConstants.EDITOR_STRING_RESIZE_LEVEL);
	boolean success = true;
	String input3;
	input3 = CommonDialogs.showTextInputDialogWithDefault(
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_NUMBER_OF_FLOORS),
		msg, Integer.toString(app.getDungeonManager().getDungeon().getFloors()));
	if (input3 != null) {
	    try {
		levelSizeZ = Integer.parseInt(input3);
		if (levelSizeZ < minF) {
		    throw new NumberFormatException(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			    LocaleConstants.EDITOR_STRING_FLOORS_TOO_LOW));
		}
		if (levelSizeZ > maxF) {
		    throw new NumberFormatException(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			    LocaleConstants.EDITOR_STRING_FLOORS_TOO_HIGH));
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

    public void goToLevelHandler() {
	int locW;
	final String msg = LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		LocaleConstants.EDITOR_STRING_GO_TO_LEVEL);
	String input;
	final String[] choices = DungeonDiver7.getApplication().getLevelInfoList();
	input = CommonDialogs.showInputDialog(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		LocaleConstants.EDITOR_STRING_GO_TO_WHICH_LEVEL), msg, choices, choices[0]);
	if (input != null) {
	    for (locW = 0; locW < choices.length; locW++) {
		if (input.equals(choices[locW])) {
		    this.updateEditorLevelAbsolute(locW);
		    break;
		}
	    }
	}
    }

    public void showOutput() {
	final Application app = DungeonDiver7.getApplication();
	this.outputFrame.setJMenuBar(app.getMenuManager().getMainMenuBar());
	app.getMenuManager().checkFlags();
	this.outputFrame.setVisible(true);
	this.outputFrame.pack();
    }

    public void attachMenus() {
	final Application app = DungeonDiver7.getApplication();
	this.outputFrame.setJMenuBar(app.getMenuManager().getMainMenuBar());
	app.getMenuManager().checkFlags();
    }

    public void hideOutput() {
	if (this.outputFrame != null) {
	    this.outputFrame.setVisible(false);
	}
    }

    void disableOutput() {
	this.outputFrame.setEnabled(false);
    }

    void enableOutput() {
	this.outputFrame.setEnabled(true);
	this.checkMenus();
    }

    public JFrame getOutputFrame() {
	if (this.outputFrame != null && this.outputFrame.isVisible()) {
	    return this.outputFrame;
	} else {
	    return null;
	}
    }

    public void exitEditor() {
	final Application app = DungeonDiver7.getApplication();
	// Hide the editor
	this.hideOutput();
	final DungeonManager mm = app.getDungeonManager();
	final GameManager gm = app.getGameManager();
	// Save the entire level
	mm.getDungeon().save();
	// Reset the player location
	try {
	    gm.resetPlayerLocation();
	} catch (final InvalidDungeonException iae) {
	    // Harmless error, ignore it
	}
    }

    private void setUpGUI() {
	// Destroy the old GUI, if one exists
	if (this.outputFrame != null) {
	    this.outputFrame.dispose();
	}
	final FocusHandler fHandler = new FocusHandler();
	this.messageLabel = new JLabel(LocaleConstants.COMMON_STRING_SPACE);
	this.outputFrame = new JFrame(
		LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE, LocaleConstants.EDITOR_STRING_EDITOR));
	final Image iconlogo = LogoLoader.getIconLogo();
	this.outputFrame.setIconImage(iconlogo);
	this.outputPane = new EditorDraw();
	this.secondaryPane = new Container();
	this.borderPane = new Container();
	this.borderPane.setLayout(new BorderLayout());
	this.outputFrame.setContentPane(this.borderPane);
	this.outputFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.messageLabel.setLabelFor(this.outputPane);
	this.outerOutputPane = RCLGenerator.generateRowColumnLabels();
	this.outerOutputPane.add(this.outputPane, BorderLayout.CENTER);
	this.outputPane.setLayout(new GridLayout(1, 1));
	this.outputFrame.setResizable(false);
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
	this.outputFrame.addWindowListener(this.mhandler);
	this.outputFrame.addWindowFocusListener(fHandler);
	this.switcherPane = new Container();
	final SwitcherHandler switcherHandler = new SwitcherHandler();
	final ButtonGroup switcherGroup = new ButtonGroup();
	this.lowerGround = new JToggleButton(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		LocaleConstants.EDITOR_STRING_LOWER_GROUND_LAYER));
	this.upperGround = new JToggleButton(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		LocaleConstants.EDITOR_STRING_UPPER_GROUND_LAYER));
	this.lowerObjects = new JToggleButton(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		LocaleConstants.EDITOR_STRING_LOWER_OBJECTS_LAYER));
	this.upperObjects = new JToggleButton(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		LocaleConstants.EDITOR_STRING_UPPER_OBJECTS_LAYER));
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

    public void undo() {
	final Application app = DungeonDiver7.getApplication();
	this.engine.undo();
	final AbstractDungeonObject obj = this.engine.getObject();
	final int x = this.engine.getX();
	final int y = this.engine.getY();
	final int z = this.engine.getZ();
	final int w = this.engine.getW();
	final int u = this.engine.getU();
	this.elMgr.setEditorLocationX(x);
	this.elMgr.setEditorLocationY(y);
	if (x != -1 && y != -1 && z != -1 && u != -1) {
	    final AbstractDungeonObject oldObj = app.getDungeonManager().getDungeon().getCell(x, y, z, w);
	    app.getDungeonManager().getDungeon().setCell(obj, x, y, z, w);
	    this.updateRedoHistory(oldObj, x, y, z, w, u);
	    this.checkMenus();
	    this.redrawEditor();
	} else {
	    DungeonDiver7.getApplication().showMessage(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		    LocaleConstants.EDITOR_STRING_NOTHING_TO_UNDO));
	}
    }

    public void redo() {
	final Application app = DungeonDiver7.getApplication();
	this.engine.redo();
	final AbstractDungeonObject obj = this.engine.getObject();
	final int x = this.engine.getX();
	final int y = this.engine.getY();
	final int z = this.engine.getZ();
	final int w = this.engine.getW();
	final int u = this.engine.getU();
	this.elMgr.setEditorLocationX(x);
	this.elMgr.setEditorLocationY(y);
	if (x != -1 && y != -1 && z != -1 && u != -1) {
	    final AbstractDungeonObject oldObj = app.getDungeonManager().getDungeon().getCell(x, y, z, w);
	    app.getDungeonManager().getDungeon().setCell(obj, x, y, z, w);
	    this.updateUndoHistory(oldObj, x, y, z, w, u);
	    this.checkMenus();
	    this.redrawEditor();
	} else {
	    DungeonDiver7.getApplication().showMessage(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
		    LocaleConstants.EDITOR_STRING_NOTHING_TO_REDO));
	}
    }

    public void clearHistory() {
	this.engine = new EditorUndoRedoEngine();
	this.checkMenus();
    }

    private void updateUndoHistory(final AbstractDungeonObject obj, final int x, final int y, final int z, final int w,
	    final int u) {
	this.engine.updateUndoHistory(obj, x, y, z, w, u);
    }

    private void updateRedoHistory(final AbstractDungeonObject obj, final int x, final int y, final int z, final int w,
	    final int u) {
	this.engine.updateRedoHistory(obj, x, y, z, w, u);
    }

    private void updatePicker() {
	if (this.elMgr != null) {
	    final DungeonObjects objectList = DungeonDiver7.getApplication().getObjects();
	    this.names = objectList.getAllNamesOnLayer(this.elMgr.getEditorLocationW());
	    this.objects = objectList.getAllObjectsOnLayer(this.elMgr.getEditorLocationW(),
		    PrefsManager.getEditorShowAllObjects());
	    this.editorAppearances = objectList.getAllEditorAppearancesOnLayer(this.elMgr.getEditorLocationW(),
		    PrefsManager.getEditorShowAllObjects());
	    this.objectsEnabled = objectList.getObjectEnabledStatuses(this.elMgr.getEditorLocationW());
	    if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_CLASSIC) {
		this.updateOldPicker();
	    } else if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_MODERN_V11) {
		this.updateNewPicker11();
	    } else if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_MODERN_V12) {
		this.updateNewPicker12();
	    }
	    this.updatePickerLayout();
	}
    }

    private void updateNewPicker12() {
	final BufferedImageIcon[] newImages = this.editorAppearances;
	final boolean[] enabled = this.objectsEnabled;
	if (this.newPicker12 != null) {
	    this.newPicker12.updatePicker(newImages, enabled);
	} else {
	    this.newPicker12 = new SXSPicturePicker(newImages, enabled, DungeonEditor.STACK_COUNT);
	}
    }

    private void updateNewPicker11() {
	final BufferedImageIcon[] newImages = this.editorAppearances;
	final boolean[] enabled = this.objectsEnabled;
	if (this.newPicker11 != null) {
	    this.newPicker11.updatePicker(newImages, enabled);
	} else {
	    this.newPicker11 = new StackedPicturePicker(newImages, enabled, DungeonEditor.STACK_COUNT);
	}
    }

    private void updateOldPicker() {
	final BufferedImageIcon[] newImages = this.editorAppearances;
	final String[] newNames = this.names;
	final boolean[] enabled = this.objectsEnabled;
	if (this.oldPicker != null) {
	    this.oldPicker.updatePicker(newImages, newNames, enabled);
	} else {
	    this.oldPicker = new PicturePicker(newImages, newNames, enabled);
	}
    }

    private void updatePickerLayout() {
	if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_CLASSIC) {
	    this.updateOldPickerLayout();
	} else if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_MODERN_V11) {
	    this.updateNewPicker11Layout();
	} else if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_MODERN_V12) {
	    this.updateNewPicker12Layout();
	}
    }

    private void updateOldPickerLayout() {
	if (this.oldPicker != null) {
	    this.oldPicker.updatePickerLayout(this.outputPane.getLayout().preferredLayoutSize(this.outputPane).height);
	}
    }

    private void updateNewPicker11Layout() {
	if (this.newPicker11 != null) {
	    this.newPicker11
		    .updatePickerLayout(this.outputPane.getLayout().preferredLayoutSize(this.outputPane).height);
	}
    }

    private void updateNewPicker12Layout() {
	if (this.newPicker12 != null) {
	    this.newPicker12
		    .updatePickerLayout(this.outputPane.getLayout().preferredLayoutSize(this.outputPane).height);
	}
    }

    public void resetBorderPane() {
	if (this.borderPane != null) {
	    this.updatePicker();
	    this.borderPane.removeAll();
	    this.borderPane.add(this.outerOutputPane, BorderLayout.CENTER);
	    this.borderPane.add(this.messageLabel, BorderLayout.NORTH);
	    if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_CLASSIC) {
		this.borderPane.add(this.oldPicker.getPicker(), BorderLayout.EAST);
	    } else if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_MODERN_V11) {
		this.borderPane.add(this.newPicker11.getPicker(), BorderLayout.EAST);
	    } else if (PrefsManager.getEditorLayoutID() == EditorLayoutConstants.EDITOR_LAYOUT_MODERN_V12) {
		this.borderPane.add(this.newPicker12.getPicker(), BorderLayout.EAST);
	    }
	    this.borderPane.add(this.switcherPane, BorderLayout.SOUTH);
	    this.outputFrame.pack();
	}
    }

    private void enableUpOneFloor() {
	this.editorUpOneFloor.setEnabled(true);
    }

    private void disableUpOneFloor() {
	this.editorUpOneFloor.setEnabled(false);
    }

    private void enableDownOneFloor() {
	this.editorDownOneFloor.setEnabled(true);
    }

    private void disableDownOneFloor() {
	this.editorDownOneFloor.setEnabled(false);
    }

    private void enableUpOneLevel() {
	this.editorUpOneLevel.setEnabled(true);
    }

    private void disableUpOneLevel() {
	this.editorUpOneLevel.setEnabled(false);
    }

    private void enableDownOneLevel() {
	this.editorDownOneLevel.setEnabled(true);
    }

    private void disableDownOneLevel() {
	this.editorDownOneLevel.setEnabled(false);
    }

    private void enableAddLevel() {
	this.editorAddLevel.setEnabled(true);
    }

    private void disableAddLevel() {
	this.editorAddLevel.setEnabled(false);
    }

    private void enableRemoveLevel() {
	this.editorRemoveLevel.setEnabled(true);
    }

    private void disableRemoveLevel() {
	this.editorRemoveLevel.setEnabled(false);
    }

    public void enableUndo() {
	this.editorUndo.setEnabled(true);
    }

    public void disableUndo() {
	this.editorUndo.setEnabled(false);
    }

    public void enableRedo() {
	this.editorRedo.setEnabled(true);
    }

    public void disableRedo() {
	this.editorRedo.setEnabled(false);
    }

    private void enableClearHistory() {
	this.editorClearHistory.setEnabled(true);
    }

    private void disableClearHistory() {
	this.editorClearHistory.setEnabled(false);
    }

    private void enableCutLevel() {
	this.editorCutLevel.setEnabled(true);
    }

    private void disableCutLevel() {
	this.editorCutLevel.setEnabled(false);
    }

    private void enablePasteLevel() {
	this.editorPasteLevel.setEnabled(true);
    }

    private void disablePasteLevel() {
	this.editorPasteLevel.setEnabled(false);
    }

    private void enableInsertLevelFromClipboard() {
	this.editorInsertLevelFromClipboard.setEnabled(true);
    }

    private void disableInsertLevelFromClipboard() {
	this.editorInsertLevelFromClipboard.setEnabled(false);
    }

    private void enableSetStartPoint() {
	this.editorSetStartPoint.setEnabled(true);
    }

    private void disableSetStartPoint() {
	this.editorSetStartPoint.setEnabled(false);
    }

    public void handleCloseWindow() {
	try {
	    final Application app = DungeonDiver7.getApplication();
	    boolean success = false;
	    int status = JOptionPane.DEFAULT_OPTION;
	    if (app.getDungeonManager().getDirty()) {
		status = DungeonManager.showSaveDialog();
		if (status == JOptionPane.YES_OPTION) {
		    success = app.getDungeonManager().saveDungeon(app.getDungeonManager().isDungeonProtected());
		    if (success) {
			this.exitEditor();
		    }
		} else if (status == JOptionPane.NO_OPTION) {
		    app.getDungeonManager().setDirty(false);
		    this.exitEditor();
		}
	    } else {
		this.exitEditor();
	    }
	} catch (final Exception ex) {
	    DungeonDiver7.getErrorLogger().logError(ex);
	}
    }

    private class EventHandler implements MouseListener, MouseMotionListener, WindowListener {
	// handle scroll bars
	public EventHandler() {
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
		final DungeonEditor me = DungeonEditor.this;
		final int x = e.getX();
		final int y = e.getY();
		if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		    me.editObjectProperties(x, y);
		} else if (e.isShiftDown()) {
		    me.probeObjectProperties(x, y);
		} else {
		    me.editObject(x, y);
		}
	    } catch (final Exception ex) {
		DungeonDiver7.getErrorLogger().logError(ex);
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
	    DungeonDiver7.getApplication().getGUIManager().showGUI();
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

	@Override
	public void mouseDragged(final MouseEvent e) {
	    try {
		final DungeonEditor me = DungeonEditor.this;
		final int x = e.getX();
		final int y = e.getY();
		me.editObject(x, y);
	    } catch (final Exception ex) {
		DungeonDiver7.getErrorLogger().logError(ex);
	    }
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
	    // Do nothing
	}
    }

    private class StartEventHandler implements MouseListener {
	// handle scroll bars
	public StartEventHandler() {
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
		final int x = e.getX();
		final int y = e.getY();
		DungeonEditor.this.setPlayerLocation(x, y);
	    } catch (final Exception ex) {
		DungeonDiver7.getErrorLogger().logError(ex);
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

    private class FocusHandler implements WindowFocusListener {
	public FocusHandler() {
	    // Do nothing
	}

	@Override
	public void windowGainedFocus(final WindowEvent e) {
	    DungeonEditor.this.attachMenus();
	}

	@Override
	public void windowLostFocus(final WindowEvent e) {
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
		final Application app = DungeonDiver7.getApplication();
		final String cmd = e.getActionCommand();
		final DungeonEditor editor = DungeonEditor.this;
		if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_UNDO))) {
		    // Undo most recent action
		    if (app.getMode() == Application.STATUS_EDITOR) {
			editor.undo();
		    } else if (app.getMode() == Application.STATUS_GAME) {
			app.getGameManager().abortAndWaitForMLOLoop();
			app.getGameManager().undoLastMove();
		    }
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_REDO))) {
		    // Redo most recent undone action
		    if (app.getMode() == Application.STATUS_EDITOR) {
			editor.redo();
		    } else if (app.getMode() == Application.STATUS_GAME) {
			app.getGameManager().abortAndWaitForMLOLoop();
			app.getGameManager().redoLastMove();
		    }
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_CUT_LEVEL))) {
		    // Cut Level
		    final int level = editor.getLocationManager().getEditorLocationU();
		    app.getDungeonManager().getDungeon().cutLevel();
		    editor.fixLimits();
		    editor.updateEditorLevelAbsolute(level);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_COPY_LEVEL))) {
		    // Copy Level
		    app.getDungeonManager().getDungeon().copyLevel();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_PASTE_LEVEL))) {
		    // Paste Level
		    app.getDungeonManager().getDungeon().pasteLevel();
		    editor.fixLimits();
		    editor.redrawEditor();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_INSERT_LEVEL_FROM_CLIPBOARD))) {
		    // Insert Level From Clipboard
		    app.getDungeonManager().getDungeon().insertLevelFromClipboard();
		    editor.fixLimits();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_CLEAR_HISTORY))) {
		    // Clear undo/redo history, confirm first
		    final int res = CommonDialogs.showConfirmDialog(
			    LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
				    LocaleConstants.MENU_STRING_CONFIRM_CLEAR_HISTORY),
			    LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
				    LocaleConstants.EDITOR_STRING_EDITOR));
		    if (res == JOptionPane.YES_OPTION) {
			editor.clearHistory();
		    }
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_GO_TO_LEVEL))) {
		    // Go To Level
		    editor.goToLevelHandler();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_UP_ONE_FLOOR))) {
		    // Go up one floor
		    editor.updateEditorPosition(1, 0);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_DOWN_ONE_FLOOR))) {
		    // Go down one floor
		    editor.updateEditorPosition(-1, 0);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_UP_ONE_LEVEL))) {
		    // Go up one level
		    editor.updateEditorPosition(0, 1);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_DOWN_ONE_LEVEL))) {
		    // Go down one level
		    editor.updateEditorPosition(0, -1);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_ADD_A_LEVEL))) {
		    // Add a level
		    editor.addLevel();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_REMOVE_A_LEVEL))) {
		    // Remove a level
		    editor.removeLevel();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_FILL_CURRENT_LEVEL))) {
		    // Fill level
		    editor.fillLevel();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_RESIZE_CURRENT_LEVEL))) {
		    // Resize level
		    editor.resizeLevel();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_LEVEL_PREFERENCES))) {
		    // Set Level Preferences
		    editor.setLevelPrefs();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_SET_START_POINT))) {
		    // Set Start Point
		    editor.editPlayerLocation();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_SET_MUSIC))) {
		    // Set Music
		    editor.defineDungeonMusic();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_CHANGE_LAYER))) {
		    // Change Layer
		    editor.changeLayer();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_ENABLE_GLOBAL_MOVE_SHOOT))) {
		    // Enable Global Move-Shoot
		    editor.enableGlobalMoveShoot();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_DISABLE_GLOBAL_MOVE_SHOOT))) {
		    // Disable Global Move-Shoot
		    editor.disableGlobalMoveShoot();
		} else if (cmd.equals(
			LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_DISTANT_PAST))) {
		    // Time Travel: Distant Past
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_DISTANT_PAST);
		    editor.editorEraDistantPast.setSelected(true);
		    editor.editorEraPast.setSelected(false);
		    editor.editorEraPresent.setSelected(false);
		    editor.editorEraFuture.setSelected(false);
		    editor.editorEraDistantFuture.setSelected(false);
		} else if (cmd
			.equals(LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_PAST))) {
		    // Time Travel: Past
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_PAST);
		    editor.editorEraDistantPast.setSelected(false);
		    editor.editorEraPast.setSelected(true);
		    editor.editorEraPresent.setSelected(false);
		    editor.editorEraFuture.setSelected(false);
		    editor.editorEraDistantFuture.setSelected(false);
		} else if (cmd.equals(
			LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_PRESENT))) {
		    // Time Travel: Present
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_PRESENT);
		    editor.editorEraDistantPast.setSelected(false);
		    editor.editorEraPast.setSelected(false);
		    editor.editorEraPresent.setSelected(true);
		    editor.editorEraFuture.setSelected(false);
		    editor.editorEraDistantFuture.setSelected(false);
		} else if (cmd.equals(
			LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_FUTURE))) {
		    // Time Travel: Future
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_FUTURE);
		    editor.editorEraDistantPast.setSelected(false);
		    editor.editorEraPast.setSelected(false);
		    editor.editorEraPresent.setSelected(false);
		    editor.editorEraFuture.setSelected(true);
		    editor.editorEraDistantFuture.setSelected(false);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE,
			DungeonConstants.ERA_DISTANT_FUTURE))) {
		    // Time Travel: Distant Future
		    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_DISTANT_FUTURE);
		    editor.editorEraDistantPast.setSelected(false);
		    editor.editorEraPast.setSelected(false);
		    editor.editorEraPresent.setSelected(false);
		    editor.editorEraFuture.setSelected(false);
		    editor.editorEraDistantFuture.setSelected(true);
		}
		app.getMenuManager().checkFlags();
	    } catch (final Exception ex) {
		DungeonDiver7.getErrorLogger().logError(ex);
	    }
	}
    }

    private class SwitcherHandler implements ActionListener {
	SwitcherHandler() {
	    // Do nothing
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final String cmd = e.getActionCommand();
		final DungeonEditor ae = DungeonEditor.this;
		if (cmd.equals(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_LOWER_GROUND_LAYER))) {
		    ae.changeLayerImpl(DungeonConstants.LAYER_LOWER_GROUND);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_UPPER_GROUND_LAYER))) {
		    ae.changeLayerImpl(DungeonConstants.LAYER_UPPER_GROUND);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_LOWER_OBJECTS_LAYER))) {
		    ae.changeLayerImpl(DungeonConstants.LAYER_LOWER_OBJECTS);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_UPPER_OBJECTS_LAYER))) {
		    ae.changeLayerImpl(DungeonConstants.LAYER_UPPER_OBJECTS);
		}
	    } catch (final Exception ex) {
		DungeonDiver7.getErrorLogger().logError(ex);
	    }
	}
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

    @Override
    public void setInitialState() {
	this.disableModeCommands();
    }

    @Override
    public JMenu createCommandsMenu() {
	final MenuHandler menuHandler = new MenuHandler();
	final JMenu editorMenu = new JMenu(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_MENU_EDITOR));
	this.editorUndo = new JMenuItem(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_UNDO));
	this.editorRedo = new JMenuItem(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_REDO));
	this.editorCutLevel = new JMenuItem(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_CUT_LEVEL));
	this.editorCopyLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_COPY_LEVEL));
	this.editorPasteLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_PASTE_LEVEL));
	this.editorInsertLevelFromClipboard = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_INSERT_LEVEL_FROM_CLIPBOARD));
	this.editorClearHistory = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_CLEAR_HISTORY));
	this.editorGoToLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_GO_TO_LEVEL));
	this.editorUpOneFloor = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_UP_ONE_FLOOR));
	this.editorDownOneFloor = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_DOWN_ONE_FLOOR));
	this.editorUpOneLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_UP_ONE_LEVEL));
	this.editorDownOneLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_DOWN_ONE_LEVEL));
	this.editorAddLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_ADD_A_LEVEL));
	this.editorRemoveLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_REMOVE_A_LEVEL));
	this.editorFillLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_FILL_CURRENT_LEVEL));
	this.editorResizeLevel = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_RESIZE_CURRENT_LEVEL));
	this.editorLevelPreferences = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_LEVEL_PREFERENCES));
	this.editorSetStartPoint = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_SET_START_POINT));
	this.editorSetMusic = new JMenuItem(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_SET_MUSIC));
	this.editorChangeLayer = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_CHANGE_LAYER));
	this.editorGlobalMoveShoot = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_ENABLE_GLOBAL_MOVE_SHOOT));
	this.editorTimeTravelSubMenu = new JMenu(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_SUB_TIME_TRAVEL));
	this.editorEraDistantPast = new JCheckBoxMenuItem(
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_DISTANT_PAST), false);
	this.editorEraPast = new JCheckBoxMenuItem(
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_PAST), false);
	this.editorEraPresent = new JCheckBoxMenuItem(
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_PRESENT), true);
	this.editorEraFuture = new JCheckBoxMenuItem(
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_FUTURE), false);
	this.editorEraDistantFuture = new JCheckBoxMenuItem(
		LocaleLoader.loadString(LocaleConstants.ERA_STRINGS_FILE, DungeonConstants.ERA_DISTANT_FUTURE), false);
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

    @Override
    public void enableLoadedCommands() {
	// Do nothing
    }

    @Override
    public void disableLoadedCommands() {
	// Do nothing
    }

    @Override
    public void enableDirtyCommands() {
	// Do nothing
    }

    @Override
    public void disableDirtyCommands() {
	// Do nothing
    }
}
