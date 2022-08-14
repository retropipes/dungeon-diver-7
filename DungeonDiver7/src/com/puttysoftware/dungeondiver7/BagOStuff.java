/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.battle.AbstractBattle;
import com.puttysoftware.dungeondiver7.battle.MapBattleLogic;
import com.puttysoftware.dungeondiver7.editor.DungeonEditor;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.manager.dungeon.DungeonManager;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.shop.Shop;
import com.puttysoftware.dungeondiver7.shop.ShopType;
import com.puttysoftware.dungeondiver7.utility.DungeonObjects;
import com.puttysoftware.images.BufferedImageIcon;

public final class BagOStuff {
    // Fields
    private AboutDialog about;
    private GameLogic gameLogic;
    private DungeonManager dungeonMgr;
    private MenuManager menuMgr;
    private HelpManager helpMgr;
    private DungeonEditor editor;
    private GUIManager guiMgr;
    private int mode, formerMode;
    private final DungeonObjects objects;
    private Shop weapons, armor, healer, regenerator, spells;
    private MapBattleLogic battle;
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

    // Constructors
    public BagOStuff() {
	this.objects = new DungeonObjects();
	this.mode = BagOStuff.STATUS_NULL;
	this.formerMode = BagOStuff.STATUS_NULL;
	// Create Managers
	this.about = new AboutDialog(BagOStuff.getVersionString());
	this.helpMgr = new HelpManager();
	this.editor = new DungeonEditor();
	this.guiMgr = new GUIManager();
	this.menuMgr = new MenuManager();
	this.battle = new MapBattleLogic();
	this.weapons = new Shop(ShopType.WEAPONS);
	this.armor = new Shop(ShopType.ARMOR);
	this.healer = new Shop(ShopType.HEALER);
	this.regenerator = new Shop(ShopType.REGENERATOR);
	this.spells = new Shop(ShopType.SPELLS);
	// Cache Logo
	this.guiMgr.updateLogo();
    }

    // Methods
    public void activeLanguageChanged() {
	// Rebuild menus
	this.menuMgr.unregisterAllModeManagers();
	this.menuMgr.registerModeManager(this.guiMgr);
	this.menuMgr.initMenus();
	this.menuMgr.registerModeManager(this.gameLogic);
	this.menuMgr.registerModeManager(this.editor);
	this.menuMgr.registerModeManager(this.about);
	// Fire hooks
	this.getHelpManager().activeLanguageChanged();
	this.getGameLogic().activeLanguageChanged();
	this.getEditor().activeLanguageChanged();
    }

    void setInGUI() {
	this.mode = BagOStuff.STATUS_GUI;
	this.menuMgr.modeChanged(this.guiMgr);
    }

    public void setInPrefs() {
	this.formerMode = this.mode;
	this.mode = BagOStuff.STATUS_PREFS;
	this.menuMgr.modeChanged(null);
    }

    public void setInGame() {
	this.mode = BagOStuff.STATUS_GAME;
	this.menuMgr.modeChanged(this.gameLogic);
    }

    public void setInEditor() {
	this.mode = BagOStuff.STATUS_EDITOR;
	this.menuMgr.modeChanged(this.editor);
    }

    public void setInHelp() {
	this.formerMode = this.mode;
	this.mode = BagOStuff.STATUS_HELP;
	this.menuMgr.modeChanged(null);
    }

    public void setMode(final int newMode) {
	this.formerMode = this.mode;
	this.mode = newMode;
    }

    public int getMode() {
	return this.mode;
    }

    public int getFormerMode() {
	return this.formerMode;
    }

    void exitCurrentMode() {
	if (this.mode == BagOStuff.STATUS_GUI) {
	    this.guiMgr.hideGUI();
	} else if (this.mode == BagOStuff.STATUS_GAME) {
	    this.gameLogic.exitGame();
	} else if (this.mode == BagOStuff.STATUS_EDITOR) {
	    this.editor.exitEditor();
	}
    }

    public boolean modeChanged() {
	return this.formerMode != this.mode;
    }

    public void saveFormerMode() {
	this.formerMode = this.mode;
    }

    public void restoreFormerMode() {
	this.mode = this.formerMode;
    }

    public void showMessage(final String msg) {
	if (this.mode == BagOStuff.STATUS_EDITOR) {
	    this.getEditor().setStatusMessage(msg);
	} else if (this.mode == BagOStuff.STATUS_BATTLE) {
	    this.getBattle().setStatusMessage(msg);
	} else {
	    CommonDialogs.showDialog(msg);
	}
    }

    public MenuManager getMenuManager() {
	return this.menuMgr;
    }

    public GUIManager getGUIManager() {
	return this.guiMgr;
    }

    public GameLogic getGameLogic() {
	return this.gameLogic;
    }

    public DungeonManager getDungeonManager() {
	if (this.dungeonMgr == null) {
	    this.dungeonMgr = new DungeonManager();
	}
	return this.dungeonMgr;
    }

    HelpManager getHelpManager() {
	return this.helpMgr;
    }

    public DungeonEditor getEditor() {
	return this.editor;
    }

    AboutDialog getAboutDialog() {
	return this.about;
    }

    private static String getVersionString() {
	if (BagOStuff.isBetaModeEnabled()) {
	    return LocaleConstants.COMMON_STRING_EMPTY + BagOStuff.VERSION_MAJOR
		    + LocaleConstants.COMMON_STRING_NOTL_PERIOD + BagOStuff.VERSION_MINOR
		    + LocaleConstants.COMMON_STRING_NOTL_PERIOD + BagOStuff.VERSION_BUGFIX
		    + LocaleLoader.loadString(LocaleConstants.MESSAGE_STRINGS_FILE, LocaleConstants.MESSAGE_STRING_BETA)
		    + BagOStuff.VERSION_BETA;
	} else {
	    return LocaleConstants.COMMON_STRING_EMPTY + BagOStuff.VERSION_MAJOR
		    + LocaleConstants.COMMON_STRING_NOTL_PERIOD + BagOStuff.VERSION_MINOR
		    + LocaleConstants.COMMON_STRING_NOTL_PERIOD + BagOStuff.VERSION_BUGFIX;
	}
    }

    public static String getLogoVersionString() {
	if (BagOStuff.isBetaModeEnabled()) {
	    return LocaleConstants.COMMON_STRING_EMPTY + BagOStuff.VERSION_MAJOR
		    + LocaleConstants.COMMON_STRING_NOTL_PERIOD + BagOStuff.VERSION_MINOR
		    + LocaleConstants.COMMON_STRING_NOTL_PERIOD + BagOStuff.VERSION_BUGFIX
		    + LocaleConstants.COMMON_STRING_BETA_SHORT + BagOStuff.VERSION_BETA;
	} else {
	    return LocaleConstants.COMMON_STRING_EMPTY + BagOStuff.VERSION_MAJOR
		    + LocaleConstants.COMMON_STRING_NOTL_PERIOD + BagOStuff.VERSION_MINOR
		    + LocaleConstants.COMMON_STRING_NOTL_PERIOD + BagOStuff.VERSION_BUGFIX;
	}
    }

    public JFrame getOutputFrame() {
	try {
	    if (this.getMode() == BagOStuff.STATUS_PREFS) {
		return PrefsManager.getPrefFrame();
	    } else if (this.getMode() == BagOStuff.STATUS_GUI) {
		return this.getGUIManager().getGUIFrame();
	    } else if (this.getMode() == BagOStuff.STATUS_GAME) {
		return this.getGameLogic().getOutputFrame();
	    } else if (this.getMode() == BagOStuff.STATUS_EDITOR) {
		return this.getEditor().getOutputFrame();
	    } else if (this.getMode() == BagOStuff.STATUS_BATTLE) {
		return this.getBattle().getOutputFrame();
	    } else {
		return null;
	    }
	} catch (final NullPointerException npe) {
	    return null;
	}
    }

    public static BufferedImageIcon getMicroLogo() {
	return LogoLoader.getMicroLogo();
    }

    public static Image getIconLogo() {
	return LogoLoader.getIconLogo();
    }

    public DungeonObjects getObjects() {
	return this.objects;
    }

    public String[] getLevelInfoList() {
	return this.dungeonMgr.getDungeon().getLevelInfoList();
    }

    public void updateLevelInfoList() {
	JFrame loadFrame;
	JProgressBar loadBar;
	loadFrame = new JFrame(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
		LocaleConstants.DIALOG_STRING_UPDATING_LEVEL_INFO));
	loadFrame.setIconImage(LogoLoader.getIconLogo());
	loadBar = new JProgressBar();
	loadBar.setIndeterminate(true);
	loadBar.setPreferredSize(new Dimension(600, 20));
	loadFrame.getContentPane().add(loadBar);
	loadFrame.setResizable(false);
	loadFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	loadFrame.pack();
	loadFrame.setVisible(true);
	this.dungeonMgr.getDungeon().generateLevelInfoList();
	loadFrame.setVisible(false);
    }

    private static boolean isBetaModeEnabled() {
	return BagOStuff.VERSION_BETA > 0;
    }

    public Shop getShopByType(final ShopType shopType) {
	this.getGameLogic().stopMovement();
	switch (shopType) {
	case ARMOR:
	    return this.armor;
	case HEALER:
	    return this.healer;
	case REGENERATOR:
	    return this.regenerator;
	case SPELLS:
	    return this.spells;
	case WEAPONS:
	    return this.weapons;
	default:
	    // Invalid shop type
	    return null;
	}
    }

    public AbstractBattle getBattle() {
	return this.battle;
    }

    public void resetBattleGUI() {
	this.battle.resetGUI();
    }
}
