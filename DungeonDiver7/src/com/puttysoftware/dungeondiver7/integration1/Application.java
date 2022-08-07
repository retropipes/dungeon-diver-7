/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1;

import java.awt.Image;

import javax.swing.JFrame;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.integration1.battle.AbstractBattle;
import com.puttysoftware.dungeondiver7.integration1.battle.MapBattleLogic;
import com.puttysoftware.dungeondiver7.integration1.dungeon.utility.GameObjectList;
import com.puttysoftware.dungeondiver7.integration1.game.GameLogic;
import com.puttysoftware.dungeondiver7.integration1.loader.LogoManager;
import com.puttysoftware.dungeondiver7.integration1.manager.dungeon.DungeonManager;
import com.puttysoftware.dungeondiver7.integration1.prefs.PreferencesManager;
import com.puttysoftware.dungeondiver7.integration1.shop.Shop;
import com.puttysoftware.dungeondiver7.integration1.shop.ShopType;
import com.puttysoftware.images.BufferedImageIcon;

public final class Application {
    // Fields
    private GameLogic gameLogic;
    private DungeonManager mazeMgr;
    private MenuManager menuMgr;
    private GUIManager guiMgr;
    private final GameObjectList objects;
    private Shop weapons, armor, healer, regenerator, spells;
    private MapBattleLogic battle;
    private int currentMode;
    private int formerMode;
    public static final int STATUS_GUI = 0;
    public static final int STATUS_GAME = 1;
    public static final int STATUS_BATTLE = 2;
    public static final int STATUS_PREFS = 3;
    public static final int STATUS_NULL = 4;

    // Constructors
    public Application() {
	this.objects = new GameObjectList();
	this.currentMode = Application.STATUS_NULL;
	this.formerMode = Application.STATUS_NULL;
    }

    // Methods
    void postConstruct() {
	// Create Managers
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

    public void setMode(final int newMode) {
	this.formerMode = this.currentMode;
	this.currentMode = newMode;
    }

    public int getMode() {
	return this.currentMode;
    }

    public int getFormerMode() {
	return this.formerMode;
    }

    public boolean modeChanged() {
	return this.formerMode != this.currentMode;
    }

    public void saveFormerMode() {
	this.formerMode = this.currentMode;
    }

    public void restoreFormerMode() {
	this.currentMode = this.formerMode;
    }

    public void showMessage(final String msg) {
	if (this.currentMode == Application.STATUS_GAME) {
	    this.getGameLogic().setStatusMessage(msg);
	} else if (this.currentMode == Application.STATUS_BATTLE) {
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
	if (this.gameLogic == null) {
	    this.gameLogic = new GameLogic();
	}
	return this.gameLogic;
    }

    public DungeonManager getDungeonManager() {
	if (this.mazeMgr == null) {
	    this.mazeMgr = new DungeonManager();
	}
	return this.mazeMgr;
    }

    public static BufferedImageIcon getMicroLogo() {
	return LogoManager.getMicroLogo();
    }

    public static Image getIconLogo() {
	return LogoManager.getIconLogo();
    }

    public JFrame getOutputFrame() {
	if (this.getMode() == Application.STATUS_PREFS) {
	    return PreferencesManager.getPrefFrame();
	} else if (this.getMode() == Application.STATUS_GUI) {
	    return this.getGUIManager().getGUIFrame();
	} else if (this.getMode() == Application.STATUS_GAME) {
	    return this.getGameLogic().getOutputFrame();
	} else if (this.getMode() == Application.STATUS_BATTLE) {
	    return this.getBattle().getOutputFrame();
	} else {
	    return null;
	}
    }

    public GameObjectList getObjects() {
	return this.objects;
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
