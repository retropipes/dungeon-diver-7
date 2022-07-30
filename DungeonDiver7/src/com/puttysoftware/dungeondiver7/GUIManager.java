/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.dungeon.DungeonManager;
import com.puttysoftware.dungeondiver7.loaders.LogoLoader;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utilities.ScreenPrinter;
import com.puttysoftware.dungeondiver7.utilities.CleanupTask;
import com.puttysoftware.images.BufferedImageIcon;

public class GUIManager implements MenuSection {
    // Fields
    private final JFrame guiFrame;
    private final JLabel logoLabel;
    private JMenuItem fileNew, fileOpen, fileOpenDefault, fileClose, fileSave, fileSaveAs, fileSaveAsProtected,
	    filePrint, filePreferences, fileExit;

    // Constructors
    public GUIManager() {
	final CloseHandler cHandler = new CloseHandler();
	final FocusHandler fHandler = new FocusHandler();
	this.guiFrame = new JFrame(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_PROGRAM_NAME));
	final Container guiPane = this.guiFrame.getContentPane();
	this.guiFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.guiFrame.setLayout(new GridLayout(1, 1));
	this.logoLabel = new JLabel(LocaleConstants.COMMON_STRING_EMPTY, null, SwingConstants.CENTER);
	this.logoLabel.setLabelFor(null);
	this.logoLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
	guiPane.add(this.logoLabel);
	this.guiFrame.setResizable(false);
	this.guiFrame.addWindowListener(cHandler);
	this.guiFrame.addWindowFocusListener(fHandler);
    }

    // Methods
    JFrame getGUIFrame() {
	if (this.guiFrame.isVisible()) {
	    return this.guiFrame;
	} else {
	    return null;
	}
    }

    public void showGUI() {
	final Application app = DungeonDiver7.getApplication();
	app.setInGUI();
	this.guiFrame.setJMenuBar(app.getMenuManager().getMainMenuBar());
	this.guiFrame.setVisible(true);
	this.guiFrame.pack();
	app.getMenuManager().checkFlags();
    }

    public void attachMenus() {
	final Application app = DungeonDiver7.getApplication();
	this.guiFrame.setJMenuBar(app.getMenuManager().getMainMenuBar());
	app.getMenuManager().checkFlags();
    }

    public void hideGUI() {
	this.guiFrame.setVisible(false);
    }

    void updateLogo() {
	final BufferedImageIcon logo = LogoLoader.getLogo();
	this.logoLabel.setIcon(logo);
	final Image iconlogo = LogoLoader.getIconLogo();
	this.guiFrame.setIconImage(iconlogo);
	this.guiFrame.pack();
    }

    public boolean quitHandler() {
	final DungeonManager mm = DungeonDiver7.getApplication().getDungeonManager();
	boolean saved = true;
	int status = JOptionPane.DEFAULT_OPTION;
	if (mm.getDirty()) {
	    status = DungeonManager.showSaveDialog();
	    if (status == JOptionPane.YES_OPTION) {
		saved = mm.saveDungeon(mm.isDungeonProtected());
	    } else if (status == JOptionPane.CANCEL_OPTION) {
		saved = false;
	    } else {
		mm.setDirty(false);
	    }
	}
	if (saved) {
	    PrefsManager.writePrefs();
	    // Run cleanup task
	    CleanupTask.cleanUp();
	}
	return saved;
    }

    private class CloseHandler implements WindowListener {
	public CloseHandler() {
	    // Do nothing
	}

	@Override
	public void windowActivated(final WindowEvent arg0) {
	    // Do nothing
	}

	@Override
	public void windowClosed(final WindowEvent arg0) {
	    // Do nothing
	}

	@Override
	public void windowClosing(final WindowEvent arg0) {
	    if (GUIManager.this.quitHandler()) {
		System.exit(0);
	    }
	}

	@Override
	public void windowDeactivated(final WindowEvent arg0) {
	    // Do nothing
	}

	@Override
	public void windowDeiconified(final WindowEvent arg0) {
	    // Do nothing
	}

	@Override
	public void windowIconified(final WindowEvent arg0) {
	    // Do nothing
	}

	@Override
	public void windowOpened(final WindowEvent arg0) {
	    // Do nothing
	}
    }

    private class FocusHandler implements WindowFocusListener {
	public FocusHandler() {
	    // Do nothing
	}

	@Override
	public void windowGainedFocus(final WindowEvent e) {
	    GUIManager.this.attachMenus();
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
		boolean loaded = false;
		final String cmd = e.getActionCommand();
		if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_NEW))) {
		    loaded = app.getEditor().newDungeon();
		    app.getDungeonManager().setLoaded(loaded);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_OPEN))) {
		    loaded = app.getDungeonManager().loadDungeon();
		    app.getDungeonManager().setLoaded(loaded);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_OPEN_DEFAULT))) {
		    loaded = app.getDungeonManager().loadDungeonDefault();
		    app.getDungeonManager().setLoaded(loaded);
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_CLOSE))) {
		    // Close the window
		    if (app.getMode() == Application.STATUS_EDITOR) {
			app.getEditor().handleCloseWindow();
		    } else if (app.getMode() == Application.STATUS_GAME) {
			boolean saved = true;
			int status = 0;
			if (app.getDungeonManager().getDirty()) {
			    status = DungeonManager.showSaveDialog();
			    if (status == JOptionPane.YES_OPTION) {
				saved = app.getDungeonManager()
					.saveDungeon(app.getDungeonManager().isDungeonProtected());
			    } else if (status == JOptionPane.CANCEL_OPTION) {
				saved = false;
			    } else {
				app.getDungeonManager().setDirty(false);
			    }
			}
			if (saved) {
			    app.getGameManager().exitGame();
			}
		    }
		    app.getGUIManager().showGUI();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_SAVE))) {
		    if (app.getDungeonManager().getLoaded()) {
			app.getDungeonManager().saveDungeon(app.getDungeonManager().isDungeonProtected());
		    } else {
			CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
				LocaleConstants.MENU_STRING_ERROR_NO_ARENA_OPENED));
		    }
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_SAVE_AS))) {
		    if (app.getDungeonManager().getLoaded()) {
			app.getDungeonManager().saveDungeonAs(false);
		    } else {
			CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
				LocaleConstants.MENU_STRING_ERROR_NO_ARENA_OPENED));
		    }
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_SAVE_AS_PROTECTED))) {
		    if (app.getDungeonManager().getLoaded()) {
			app.getDungeonManager().saveDungeonAs(true);
		    } else {
			CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
				LocaleConstants.MENU_STRING_ERROR_NO_ARENA_OPENED));
		    }
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_PREFERENCES))) {
		    // Show preferences dialog
		    PrefsManager.showPrefs();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_PRINT_GAMEBOARD))) {
		    ScreenPrinter.printBoard(app.getOutputFrame());
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_EXIT))) {
		    // Exit program
		    if (app.getGUIManager().quitHandler()) {
			System.exit(0);
		    }
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			LocaleConstants.MENU_STRING_ITEM_QUIT))) {
		    // Quit program
		    if (app.getGUIManager().quitHandler()) {
			System.exit(0);
		    }
		}
		app.getMenuManager().checkFlags();
	    } catch (final Exception ex) {
		DungeonDiver7.getErrorLogger().logError(ex);
	    }
	}
    }

    @Override
    public void enableModeCommands() {
	this.fileNew.setEnabled(true);
	this.fileOpen.setEnabled(true);
	this.fileOpenDefault.setEnabled(true);
	DungeonDiver7.getApplication().getMenuManager().enableModeCommands();
    }

    @Override
    public void disableModeCommands() {
	this.fileNew.setEnabled(false);
	this.fileOpen.setEnabled(false);
	this.fileOpenDefault.setEnabled(false);
	DungeonDiver7.getApplication().getMenuManager().disableModeCommands();
    }

    @Override
    public void setInitialState() {
	this.fileNew.setEnabled(true);
	this.fileOpen.setEnabled(true);
	this.fileOpenDefault.setEnabled(true);
	this.fileClose.setEnabled(false);
	this.fileSave.setEnabled(false);
	this.fileSaveAs.setEnabled(false);
	this.fileSaveAsProtected.setEnabled(false);
	this.filePreferences.setEnabled(true);
	this.filePrint.setEnabled(true);
	this.fileExit.setEnabled(true);
    }

    @Override
    public JMenu createCommandsMenu() {
	final MenuHandler mhandler = new MenuHandler();
	final JMenu fileMenu = new JMenu(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_MENU_FILE));
	this.fileNew = new JMenuItem(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_NEW));
	this.fileOpen = new JMenuItem(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_OPEN));
	this.fileOpenDefault = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_OPEN_DEFAULT));
	this.fileClose = new JMenuItem(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_CLOSE));
	this.fileSave = new JMenuItem(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_SAVE));
	this.fileSaveAs = new JMenuItem(
		LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_SAVE_AS));
	this.fileSaveAsProtected = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_SAVE_AS_PROTECTED));
	this.filePreferences = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_PREFERENCES));
	this.filePrint = new JMenuItem(LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
		LocaleConstants.MENU_STRING_ITEM_PRINT_GAMEBOARD));
	if (System
		.getProperty(
			LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_OS_NAME))
		.contains(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_WINDOWS))) {
	    this.fileExit = new JMenuItem(
		    LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_EXIT));
	} else {
	    this.fileExit = new JMenuItem(
		    LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE, LocaleConstants.MENU_STRING_ITEM_QUIT));
	}
	this.fileNew.addActionListener(mhandler);
	this.fileOpen.addActionListener(mhandler);
	this.fileOpenDefault.addActionListener(mhandler);
	this.fileClose.addActionListener(mhandler);
	this.fileSave.addActionListener(mhandler);
	this.fileSaveAs.addActionListener(mhandler);
	this.fileSaveAsProtected.addActionListener(mhandler);
	this.filePreferences.addActionListener(mhandler);
	this.filePrint.addActionListener(mhandler);
	this.fileExit.addActionListener(mhandler);
	fileMenu.add(this.fileNew);
	fileMenu.add(this.fileOpen);
	fileMenu.add(this.fileOpenDefault);
	fileMenu.add(this.fileClose);
	fileMenu.add(this.fileSave);
	fileMenu.add(this.fileSaveAs);
	fileMenu.add(this.fileSaveAsProtected);
	if (!System
		.getProperty(
			LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_OS_NAME))
		.equalsIgnoreCase(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_MAC_OS_X))) {
	    fileMenu.add(this.filePreferences);
	}
	fileMenu.add(this.filePrint);
	if (!System
		.getProperty(
			LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_OS_NAME))
		.equalsIgnoreCase(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_MAC_OS_X))) {
	    fileMenu.add(this.fileExit);
	}
	return fileMenu;
    }

    @Override
    public void attachAccelerators(final Accelerators accel) {
	this.fileNew.setAccelerator(accel.fileNewAccel);
	this.fileOpen.setAccelerator(accel.fileOpenAccel);
	this.fileClose.setAccelerator(accel.fileCloseAccel);
	this.fileSave.setAccelerator(accel.fileSaveAccel);
	this.fileSaveAs.setAccelerator(accel.fileSaveAsAccel);
	this.filePreferences.setAccelerator(accel.filePreferencesAccel);
	this.filePrint.setAccelerator(accel.filePrintAccel);
	if (System
		.getProperty(
			LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_OS_NAME))
		.contains(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_WINDOWS))) {
	    this.fileExit.setAccelerator(null);
	} else {
	    this.fileExit.setAccelerator(accel.fileExitAccel);
	}
    }

    @Override
    public void enableLoadedCommands() {
	final Application app = DungeonDiver7.getApplication();
	if (app.getMode() == Application.STATUS_GUI) {
	    this.fileClose.setEnabled(false);
	    this.fileSaveAs.setEnabled(false);
	    this.fileSaveAsProtected.setEnabled(false);
	} else {
	    this.fileClose.setEnabled(true);
	    this.fileSaveAs.setEnabled(true);
	    this.fileSaveAsProtected.setEnabled(true);
	}
	DungeonDiver7.getApplication().getMenuManager().enableLoadedCommands();
    }

    @Override
    public void disableLoadedCommands() {
	this.fileClose.setEnabled(false);
	this.fileSaveAs.setEnabled(false);
	this.fileSaveAsProtected.setEnabled(false);
	DungeonDiver7.getApplication().getMenuManager().disableLoadedCommands();
    }

    @Override
    public void enableDirtyCommands() {
	this.fileSave.setEnabled(true);
	DungeonDiver7.getApplication().getMenuManager().enableDirtyCommands();
    }

    @Override
    public void disableDirtyCommands() {
	this.fileSave.setEnabled(false);
	DungeonDiver7.getApplication().getMenuManager().disableDirtyCommands();
    }
}
