/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.puttysoftware.dungeondiver7.locale.Menu;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.prefs.Prefs;

public class MenuManager implements MenuSection {
    // Fields
    private final JMenuBar mainMenuBar;
    private final ArrayList<MenuSection> modeMgrs;
    private JMenuItem playPlay, playEdit;
    private JCheckBoxMenuItem playToggleAccelerators;
    private Accelerators accel;

    // Constructors
    public MenuManager() {
	this.mainMenuBar = new JMenuBar();
	this.modeMgrs = new ArrayList<>();
	this.accel = Accelerators.getAcceleratorModel();
    }

    // Methods
    public JMenuBar getMainMenuBar() {
	return this.mainMenuBar;
    }

    public void initMenus() {
	final var menu = this.createCommandsMenu();
	this.attachAccelerators(this.accel);
	this.setInitialState();
	this.mainMenuBar.add(menu);
    }

    public void unregisterAllModeManagers() {
	this.modeMgrs.clear();
	this.mainMenuBar.removeAll();
    }

    public void registerModeManager(final MenuSection mgr) {
	this.modeMgrs.add(mgr);
	final var menu = mgr.createCommandsMenu();
	mgr.attachAccelerators(this.accel);
	mgr.setInitialState();
	this.mainMenuBar.add(menu);
    }

    public void modeChanged(final MenuSection currentMgr) {
	for (final MenuSection mgr : this.modeMgrs) {
	    if (currentMgr == null || !currentMgr.getClass().equals(mgr.getClass())) {
		mgr.disableModeCommands();
	    } else {
		mgr.enableModeCommands();
	    }
	}
    }

    void toggleAccelerators() {
	if (this.accel instanceof ClassicAccelerators) {
	    this.accel = new ModernAccelerators();
	    Prefs.setClassicAccelerators(false);
	} else {
	    this.accel = new ClassicAccelerators();
	    Prefs.setClassicAccelerators(true);
	}
    }

    public void checkFlags() {
	final var app = DungeonDiver7.getStuffBag();
	if (app.getDungeonManager().getLoaded()) {
	    for (final MenuSection mgr : this.modeMgrs) {
		mgr.enableLoadedCommands();
	    }
	} else {
	    for (final MenuSection mgr : this.modeMgrs) {
		mgr.disableLoadedCommands();
	    }
	}
	if (app.getDungeonManager().getDirty()) {
	    for (final MenuSection mgr : this.modeMgrs) {
		mgr.enableDirtyCommands();
	    }
	} else {
	    for (final MenuSection mgr : this.modeMgrs) {
		mgr.disableDirtyCommands();
	    }
	}
    }

    private class EventHandler implements ActionListener {
	public EventHandler() {
	    // Do nothing
	}

	// Handle menus
	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final var app = DungeonDiver7.getStuffBag();
		final var cmd = e.getActionCommand();
		if (cmd.equals(Strings.menu(Menu.PLAY_DUNGEON))) {
		    // Play the current dungeon
		    final var proceed = app.getGameLogic().newGame();
		    if (proceed) {
			app.exitCurrentMode();
			app.getGameLogic().playDungeon();
		    }
		} else if (cmd.equals(Strings.menu(Menu.EDIT_DUNGEON))) {
		    // Edit the current dungeon
		    app.exitCurrentMode();
		    app.getEditor().editDungeon();
		} else if (cmd.equals(Strings.menu(Menu.USE_CLASSIC_ACCELERATORS))) {
		    // Toggle accelerators
		    MenuManager.this.toggleAccelerators();
		}
		app.getMenuManager().checkFlags();
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}
    }

    @Override
    public void enableModeCommands() {
	// Do nothing
    }

    @Override
    public void disableModeCommands() {
	// Do nothing
    }

    @Override
    public void setInitialState() {
	this.playPlay.setEnabled(false);
	this.playEdit.setEnabled(false);
	this.playToggleAccelerators.setEnabled(true);
    }

    @Override
    public JMenu createCommandsMenu() {
	final var mhandler = new EventHandler();
	final var playMenu = new JMenu(Strings.menu(Menu.PLAY));
	this.playPlay = new JMenuItem(Strings.menu(Menu.PLAY_DUNGEON));
	this.playEdit = new JMenuItem(Strings.menu(Menu.EDIT_DUNGEON));
	this.playToggleAccelerators = new JCheckBoxMenuItem(Strings.menu(Menu.USE_CLASSIC_ACCELERATORS));
	this.playPlay.addActionListener(mhandler);
	this.playEdit.addActionListener(mhandler);
	this.playToggleAccelerators.addActionListener(mhandler);
	playMenu.add(this.playPlay);
	playMenu.add(this.playEdit);
	playMenu.add(this.playToggleAccelerators);
	return playMenu;
    }

    @Override
    public void attachAccelerators(final Accelerators newAccel) {
	this.playPlay.setAccelerator(this.accel.playPlayDungeonAccel);
	this.playEdit.setAccelerator(this.accel.playEditDungeonAccel);
    }

    @Override
    public void enableLoadedCommands() {
	final var app = DungeonDiver7.getStuffBag();
	if (app.getDungeonManager().getDungeon().doesPlayerExist(0)) {
	    this.playPlay.setEnabled(true);
	} else {
	    this.playPlay.setEnabled(false);
	}
	this.playEdit.setEnabled(true);
    }

    @Override
    public void disableLoadedCommands() {
	this.playPlay.setEnabled(false);
	this.playEdit.setEnabled(false);
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
