/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.puttysoftware.diane.assets.image.BufferedImageIcon;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.dungeondiver7.battle.AbstractBattle;
import com.puttysoftware.dungeondiver7.battle.MapBattleLogic;
import com.puttysoftware.dungeondiver7.editor.DungeonEditor;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.manager.dungeon.DungeonManager;
import com.puttysoftware.dungeondiver7.shop.Shop;
import com.puttysoftware.dungeondiver7.shop.ShopType;
import com.puttysoftware.dungeondiver7.utility.DungeonObjects;

public final class StuffBag {
    private static final int VERSION_MAJOR = 17;
    private static final int VERSION_MINOR = 0;
    private static final int VERSION_BUGFIX = 0;
    private static final int VERSION_BETA = 1;
    public static final int STATUS_GUI = 0;
    public static final int STATUS_GAME = 1;
    public static final int STATUS_EDITOR = 2;
    public static final int STATUS_PREFS = 3;
    public static final int STATUS_HELP = 4;
    public static final int STATUS_BATTLE = 5;
    private static final int STATUS_NULL = 6;

    public static Image getIconLogo() {
	return LogoLoader.getIconLogo();
    }

    public static String getLogoVersionString() {
	if (StuffBag.isBetaModeEnabled()) {
	    return Strings.VERSION + StuffBag.VERSION_MAJOR + Strings.VERSION_DELIM + StuffBag.VERSION_MINOR
		    + Strings.VERSION_DELIM + StuffBag.VERSION_BUGFIX + Strings.BETA + StuffBag.VERSION_BETA;
	}
	return Strings.VERSION + StuffBag.VERSION_MAJOR + Strings.VERSION_DELIM + StuffBag.VERSION_MINOR
		+ Strings.VERSION_DELIM + StuffBag.VERSION_BUGFIX;
    }

    public static BufferedImageIcon getMicroLogo() {
	return LogoLoader.getMicroLogo();
    }

    private static String getVersionString() {
	if (StuffBag.isBetaModeEnabled()) {
	    return Strings.VERSION + StuffBag.VERSION_MAJOR + Strings.VERSION_DELIM + StuffBag.VERSION_MINOR
		    + Strings.VERSION_DELIM + StuffBag.VERSION_BUGFIX + Strings.BETA + StuffBag.VERSION_BETA;
	}
	return Strings.VERSION + StuffBag.VERSION_MAJOR + Strings.VERSION_DELIM + StuffBag.VERSION_MINOR
		+ Strings.VERSION_DELIM + StuffBag.VERSION_BUGFIX;
    }

    private static boolean isBetaModeEnabled() {
	return StuffBag.VERSION_BETA > 0;
    }

    // Fields
    private AboutDialog about;
    private GameLogic gameLogic;
    private DungeonManager dungeonMgr;
    private MenuManager menuMgr;
    private DungeonEditor editor;
    private GUIManager guiMgr;
    private int mode, formerMode;
    private final DungeonObjects objects;
    private final Shop weapons, armor, healer, regenerator, spells;
    private MapBattleLogic battle;

    // Constructors
    public StuffBag() {
	this.objects = new DungeonObjects();
	this.mode = StuffBag.STATUS_NULL;
	this.formerMode = StuffBag.STATUS_NULL;
	// Create Shops
	this.weapons = new Shop(ShopType.WEAPONS);
	this.armor = new Shop(ShopType.ARMOR);
	this.healer = new Shop(ShopType.HEALER);
	this.regenerator = new Shop(ShopType.REGENERATOR);
	this.spells = new Shop(ShopType.SPELLS);
    }

    public void activeLanguageChanged() {
	// Rebuild menus
	this.getMenuManager().unregisterAllModeManagers();
	this.getMenuManager().registerModeManager(this.getGUIManager());
	this.getMenuManager().initMenus();
	this.getMenuManager().registerModeManager(this.getGameLogic());
	this.getMenuManager().registerModeManager(this.getEditor());
	this.getMenuManager().registerModeManager(this.getAboutDialog());
	// Fire hooks
	this.getGameLogic().activeLanguageChanged();
	this.getEditor().activeLanguageChanged();
    }

    void exitCurrentMode() {
	switch (this.mode) {
	case StuffBag.STATUS_GUI:
	    this.getGUIManager().hideGUI();
	    break;
	case StuffBag.STATUS_GAME:
	    this.getGameLogic().exitGame();
	    break;
	case StuffBag.STATUS_EDITOR:
	    this.getEditor().exitEditor();
	    break;
	default:
	    break;
	}
    }

    AboutDialog getAboutDialog() {
	if (this.about == null) {
	    this.about = new AboutDialog(StuffBag.getVersionString());
	}
	return this.about;
    }

    public AbstractBattle getBattle() {
	if (this.battle == null) {
	    this.battle = new MapBattleLogic();
	}
	return this.battle;
    }

    public DungeonManager getDungeonManager() {
	if (this.dungeonMgr == null) {
	    this.dungeonMgr = new DungeonManager();
	}
	return this.dungeonMgr;
    }

    public DungeonEditor getEditor() {
	if (this.editor == null) {
	    this.editor = new DungeonEditor();
	}
	return this.editor;
    }

    public int getFormerMode() {
	return this.formerMode;
    }

    public GameLogic getGameLogic() {
	if (this.gameLogic == null) {
	    this.gameLogic = new GameLogic();
	}
	return this.gameLogic;
    }

    public GUIManager getGUIManager() {
	if (this.guiMgr == null) {
	    this.guiMgr = new GUIManager();
	    this.guiMgr.updateLogo();
	}
	return this.guiMgr;
    }

    public String[] getLevelInfoList() {
	return this.getDungeonManager().getDungeon().getLevelInfoList();
    }

    public MenuManager getMenuManager() {
	if (this.menuMgr == null) {
	    this.menuMgr = new MenuManager();
	}
	return this.menuMgr;
    }

    public int getMode() {
	return this.mode;
    }

    public DungeonObjects getObjects() {
	return this.objects;
    }

    public Shop getShopByType(final ShopType shopType) {
	this.getGameLogic().stopMovement();
	return switch (shopType) {
	case ARMOR -> this.armor;
	case HEALER -> this.healer;
	case REGENERATOR -> this.regenerator;
	case SPELLS -> this.spells;
	case WEAPONS -> this.weapons;
	default -> /* Invalid shop type */ null;
	};
    }

    public boolean modeChanged() {
	return this.formerMode != this.mode;
    }

    public void resetBattleGUI() {
	this.getBattle().resetGUI();
    }

    public void restoreFormerMode() {
	this.mode = this.formerMode;
    }

    public void saveFormerMode() {
	this.formerMode = this.mode;
    }

    public void setInEditor() {
	this.mode = StuffBag.STATUS_EDITOR;
	this.getMenuManager().modeChanged(this.getEditor());
    }

    public void setInGame() {
	this.mode = StuffBag.STATUS_GAME;
	this.getMenuManager().modeChanged(this.getGameLogic());
    }

    void setInGUI() {
	this.mode = StuffBag.STATUS_GUI;
	this.getMenuManager().modeChanged(this.getGUIManager());
    }

    public void setInHelp() {
	this.formerMode = this.mode;
	this.mode = StuffBag.STATUS_HELP;
	this.getMenuManager().modeChanged(null);
    }

    public void setInPrefs() {
	this.formerMode = this.mode;
	this.mode = StuffBag.STATUS_PREFS;
	this.getMenuManager().modeChanged(null);
    }

    public void setMode(final int newMode) {
	this.formerMode = this.mode;
	this.mode = newMode;
    }

    public void showMessage(final String msg) {
	if (this.mode == StuffBag.STATUS_EDITOR) {
	    this.getEditor().setStatusMessage(msg);
	} else if (this.mode == StuffBag.STATUS_BATTLE) {
	    this.getBattle().setStatusMessage(msg);
	} else {
	    CommonDialogs.showDialog(msg);
	}
    }

    public void updateLevelInfoList() {
	MainWindow mainWindow;
	JProgressBar loadBar;
	mainWindow = MainWindow.mainWindow();
	loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	loadBar.setPreferredSize(new Dimension(600, 20));
	final var loadContent = new JPanel();
	loadContent.add(loadBar);
	mainWindow.setAndSave(loadContent, Strings.dialog(DialogString.UPDATING_LEVEL_INFO));
	this.getDungeonManager().getDungeon().generateLevelInfoList();
	mainWindow.restoreSaved();
    }
}
