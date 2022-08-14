/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.prefs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import com.puttysoftware.dungeondiver7.BagOStuff;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.utility.EditorLayoutConstants;

class PrefsGUIManager {
    // Fields
    private JFrame prefFrame;
    private JCheckBox sounds;
    private JCheckBox music;
    private JCheckBox checkUpdatesStartup;
    private JCheckBox enableAnimation;
    private JCheckBox moveOneAtATime;
    private JComboBox<String> actionDelay;
    private JComboBox<String> languageList;
    private JComboBox<String> editorLayoutList;
    private JCheckBox editorShowAllObjects;
    private JComboBox<String> difficultyPicker;
    private static final String[] DIFFICULTY_NAMES = new String[] { "Very Easy", "Easy", "Normal", "Hard",
	    "Very Hard" };
    private static final int GRID_LENGTH = 14;

    // Constructors
    PrefsGUIManager() {
	this.setUpGUI();
	this.setDefaultPrefs();
    }

    // Methods
    void activeLanguageChanged() {
	// Dispose of old GUI
	if (this.prefFrame != null) {
	    this.prefFrame.dispose();
	}
	this.setUpGUI();
	PrefsManager.writePrefs();
	this.loadPrefs();
    }

    public JFrame getPrefFrame() {
	if (this.prefFrame != null && this.prefFrame.isVisible()) {
	    return this.prefFrame;
	} else {
	    return null;
	}
    }

    public void showPrefs() {
	final BagOStuff app = DungeonDiver7.getApplication();
	app.setInPrefs();
	this.prefFrame.setVisible(true);
	final int formerMode = app.getFormerMode();
	if (formerMode == BagOStuff.STATUS_GUI) {
	    app.getGUIManager().hideGUI();
	} else if (formerMode == BagOStuff.STATUS_GAME) {
	    app.getGameManager().hideOutput();
	} else if (formerMode == BagOStuff.STATUS_EDITOR) {
	    app.getEditor().hideOutput();
	}
    }

    void hidePrefs() {
	final BagOStuff app = DungeonDiver7.getApplication();
	this.prefFrame.setVisible(false);
	PrefsManager.writePrefs();
	final int formerMode = app.getFormerMode();
	if (formerMode == BagOStuff.STATUS_GUI) {
	    app.getGUIManager().showGUI();
	} else if (formerMode == BagOStuff.STATUS_GAME) {
	    app.getGameManager().showOutput();
	} else if (formerMode == BagOStuff.STATUS_EDITOR) {
	    app.getEditor().showOutput();
	}
    }

    private void loadPrefs() {
	this.enableAnimation.setSelected(PrefsManager.enableAnimation());
	this.sounds.setSelected(PrefsManager.getSoundsEnabled());
	this.music.setSelected(PrefsManager.getMusicEnabled());
	this.checkUpdatesStartup.setSelected(PrefsManager.shouldCheckUpdatesAtStartup());
	this.moveOneAtATime.setSelected(PrefsManager.oneMove());
	this.actionDelay.setSelectedIndex(PrefsManager.getActionDelay());
	this.languageList.setSelectedIndex(PrefsManager.getLanguageID());
	this.editorLayoutList.setSelectedIndex(PrefsManager.getEditorLayoutID());
	this.editorShowAllObjects.setSelected(PrefsManager.getEditorShowAllObjects());
	this.difficultyPicker.setSelectedIndex(PrefsManager.getGameDifficulty());
    }

    void setPrefs() {
	PrefsManager.setEnableAnimation(this.enableAnimation.isSelected());
	PrefsManager.setSoundsEnabled(this.sounds.isSelected());
	PrefsManager.setMusicEnabled(this.music.isSelected());
	PrefsManager.setCheckUpdatesAtStartup(this.checkUpdatesStartup.isSelected());
	PrefsManager.setOneMove(this.moveOneAtATime.isSelected());
	PrefsManager.setActionDelay(this.actionDelay.getSelectedIndex());
	PrefsManager.setLanguageID(this.languageList.getSelectedIndex());
	PrefsManager.setEditorLayoutID(this.editorLayoutList.getSelectedIndex());
	PrefsManager.setEditorShowAllObjects(this.editorShowAllObjects.isSelected());
	PrefsManager.setGameDifficulty(this.difficultyPicker.getSelectedIndex());
	this.hidePrefs();
    }

    private void setDefaultPrefs() {
	PrefsManager.readPrefs();
	this.loadPrefs();
    }

    private void setUpGUI() {
	final EventHandler handler = new EventHandler();
	this.prefFrame = new JFrame(
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE, LocaleConstants.PREFS_STRING_TITLE));
	final Container mainPrefPane = new Container();
	final Container buttonPane = new Container();
	final Container settingsPane = new Container();
	final JButton prefsOK = new JButton(
		LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE, LocaleConstants.DIALOG_STRING_OK_BUTTON));
	prefsOK.setDefaultCapable(true);
	this.prefFrame.getRootPane().setDefaultButton(prefsOK);
	final JButton prefsCancel = new JButton(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
		LocaleConstants.DIALOG_STRING_CANCEL_BUTTON));
	prefsCancel.setDefaultCapable(false);
	this.sounds = new JCheckBox(
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE, LocaleConstants.PREFS_STRING_ENABLE_SOUNDS),
		true);
	this.music = new JCheckBox(
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE, LocaleConstants.PREFS_STRING_ENABLE_MUSIC),
		true);
	this.checkUpdatesStartup = new JCheckBox(LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE,
		LocaleConstants.PREFS_STRING_STARTUP_UPDATES), true);
	this.moveOneAtATime = new JCheckBox(
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE, LocaleConstants.PREFS_STRING_ONE_MOVE),
		true);
	this.enableAnimation = new JCheckBox(LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE,
		LocaleConstants.PREFS_STRING_ENABLE_ANIMATION), true);
	this.actionDelay = new JComboBox<>(new String[] {
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE, LocaleConstants.PREFS_STRING_SPEED_1),
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE, LocaleConstants.PREFS_STRING_SPEED_2),
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE, LocaleConstants.PREFS_STRING_SPEED_3),
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE, LocaleConstants.PREFS_STRING_SPEED_4),
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE, LocaleConstants.PREFS_STRING_SPEED_5) });
	this.languageList = new JComboBox<>(LocaleLoader.loadLocalizedLanguagesList());
	this.editorLayoutList = new JComboBox<>(EditorLayoutConstants.getEditorLayoutList());
	this.editorShowAllObjects = new JCheckBox(LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE,
		LocaleConstants.PREFS_STRING_SHOW_ALL_OBJECTS), true);
	this.difficultyPicker = new JComboBox<>(PrefsGUIManager.DIFFICULTY_NAMES);
	this.prefFrame.setContentPane(mainPrefPane);
	this.prefFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.prefFrame.addWindowListener(handler);
	mainPrefPane.setLayout(new BorderLayout());
	this.prefFrame.setResizable(false);
	settingsPane.setLayout(new GridLayout(PrefsGUIManager.GRID_LENGTH, 1));
	settingsPane.add(this.sounds);
	settingsPane.add(this.music);
	settingsPane.add(this.enableAnimation);
	settingsPane.add(this.checkUpdatesStartup);
	settingsPane.add(this.moveOneAtATime);
	settingsPane.add(new JLabel(
		LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE, LocaleConstants.PREFS_STRING_SPEED_LABEL)));
	settingsPane.add(this.actionDelay);
	settingsPane.add(new JLabel(LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE,
		LocaleConstants.PREFS_STRING_ACTIVE_LANGUAGE_LABEL)));
	settingsPane.add(this.languageList);
	settingsPane.add(new JLabel(LocaleLoader.loadString(LocaleConstants.PREFS_STRINGS_FILE,
		LocaleConstants.PREFS_STRING_EDITOR_LAYOUT_LABEL)));
	settingsPane.add(this.editorLayoutList);
	settingsPane.add(this.editorShowAllObjects);
	settingsPane.add(new JLabel("Game Difficulty"));
	settingsPane.add(this.difficultyPicker);
	buttonPane.setLayout(new FlowLayout());
	buttonPane.add(prefsOK);
	buttonPane.add(prefsCancel);
	mainPrefPane.add(settingsPane, BorderLayout.CENTER);
	mainPrefPane.add(buttonPane, BorderLayout.SOUTH);
	prefsOK.addActionListener(handler);
	prefsCancel.addActionListener(handler);
	final Image iconlogo = LogoLoader.getIconLogo();
	this.prefFrame.setIconImage(iconlogo);
	this.prefFrame.pack();
    }

    private class EventHandler implements ActionListener, WindowListener {
	public EventHandler() {
	    // Do nothing
	}

	// Handle buttons
	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final PrefsGUIManager pm = PrefsGUIManager.this;
		final String cmd = e.getActionCommand();
		if (cmd.equals(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_OK_BUTTON))) {
		    pm.setPrefs();
		} else if (cmd.equals(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
			LocaleConstants.DIALOG_STRING_CANCEL_BUTTON))) {
		    pm.hidePrefs();
		}
	    } catch (final Exception ex) {
		DungeonDiver7.getErrorLogger().logError(ex);
	    }
	}

	@Override
	public void windowOpened(final WindowEvent e) {
	    // Do nothing
	}

	@Override
	public void windowClosing(final WindowEvent e) {
	    final PrefsGUIManager pm = PrefsGUIManager.this;
	    pm.hidePrefs();
	}

	@Override
	public void windowClosed(final WindowEvent e) {
	    // Do nothing
	}

	@Override
	public void windowIconified(final WindowEvent e) {
	    // Do nothing
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
	    // Do nothing
	}

	@Override
	public void windowActivated(final WindowEvent e) {
	    // Do nothing
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
	    // Do nothing
	}
    }
}
