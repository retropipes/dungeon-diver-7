/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.prefs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.EditorLayout;
import com.puttysoftware.dungeondiver7.locale.PrefString;
import com.puttysoftware.dungeondiver7.locale.Strings;

class PrefsGUI {
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
    private static String[] DIFFICULTY_NAMES = Strings.allDifficulties();
    private static final int GRID_LENGTH = 14;

    // Constructors
    PrefsGUI() {
	this.setUpGUI();
	this.setDefaultPrefs();
    }

    // Methods
    void activeLanguageChanged() {
	// Dispose of old GUI
	if (this.prefFrame != null) {
	    this.prefFrame.dispose();
	}
	PrefsGUI.DIFFICULTY_NAMES = Strings.allDifficulties();
	this.setUpGUI();
	Prefs.writePrefs();
	this.loadPrefs();
    }

    public JFrame getPrefFrame() {
	if (this.prefFrame != null && this.prefFrame.isVisible()) {
	    return this.prefFrame;
	}
	return null;
    }

    public void showPrefs() {
	final var app = DungeonDiver7.getStuffBag();
	app.setInPrefs();
	this.prefFrame.setVisible(true);
	final var formerMode = app.getFormerMode();
	switch (formerMode) {
	case StuffBag.STATUS_GUI:
	    app.getGUIManager().hideGUI();
	    break;
	case StuffBag.STATUS_GAME:
	    app.getGameLogic().hideOutput();
	    break;
	case StuffBag.STATUS_EDITOR:
	    app.getEditor().hideOutput();
	    break;
	default:
	    break;
	}
    }

    void hidePrefs() {
	final var app = DungeonDiver7.getStuffBag();
	this.prefFrame.setVisible(false);
	Prefs.writePrefs();
	final var formerMode = app.getFormerMode();
	switch (formerMode) {
	case StuffBag.STATUS_GUI:
	    app.getGUIManager().showGUI();
	    break;
	case StuffBag.STATUS_GAME:
	    app.getGameLogic().showOutput();
	    break;
	case StuffBag.STATUS_EDITOR:
	    app.getEditor().showOutput();
	    break;
	default:
	    break;
	}
    }

    private void loadPrefs() {
	this.enableAnimation.setSelected(Prefs.enableAnimation());
	this.sounds.setSelected(Prefs.getSoundsEnabled());
	this.music.setSelected(Prefs.getMusicEnabled());
	this.checkUpdatesStartup.setSelected(Prefs.shouldCheckUpdatesAtStartup());
	this.moveOneAtATime.setSelected(Prefs.oneMove());
	this.actionDelay.setSelectedIndex(Prefs.getActionDelay());
	this.languageList.setSelectedIndex(Prefs.getLanguageID());
	this.editorLayoutList.setSelectedIndex(Prefs.getEditorLayout().ordinal());
	this.editorShowAllObjects.setSelected(Prefs.getEditorShowAllObjects());
	this.difficultyPicker.setSelectedIndex(Prefs.getGameDifficulty());
    }

    void setPrefs() {
	Prefs.setEnableAnimation(this.enableAnimation.isSelected());
	Prefs.setSoundsEnabled(this.sounds.isSelected());
	Prefs.setMusicEnabled(this.music.isSelected());
	Prefs.setCheckUpdatesAtStartup(this.checkUpdatesStartup.isSelected());
	Prefs.setOneMove(this.moveOneAtATime.isSelected());
	Prefs.setActionDelay(this.actionDelay.getSelectedIndex());
	Prefs.setLanguageID(this.languageList.getSelectedIndex());
	Prefs.setEditorLayout(EditorLayout.values()[this.editorLayoutList.getSelectedIndex()]);
	Prefs.setEditorShowAllObjects(this.editorShowAllObjects.isSelected());
	Prefs.setGameDifficulty(this.difficultyPicker.getSelectedIndex());
	this.hidePrefs();
    }

    private void setDefaultPrefs() {
	Prefs.readPrefs();
	this.loadPrefs();
    }

    private void setUpGUI() {
	final var handler = new EventHandler();
	this.prefFrame = new JFrame(Strings.prefs(PrefString.TITLE));
	final var mainPrefPane = new Container();
	final var buttonPane = new Container();
	final var settingsPane = new Container();
	final var prefsOK = new JButton(Strings.dialog(DialogString.OK_BUTTON));
	prefsOK.setDefaultCapable(true);
	this.prefFrame.getRootPane().setDefaultButton(prefsOK);
	final var prefsCancel = new JButton(Strings.dialog(DialogString.CANCEL_BUTTON));
	prefsCancel.setDefaultCapable(false);
	this.sounds = new JCheckBox(Strings.prefs(PrefString.ENABLE_SOUNDS), true);
	this.music = new JCheckBox(Strings.prefs(PrefString.ENABLE_MUSIC), true);
	this.checkUpdatesStartup = new JCheckBox(Strings.prefs(PrefString.STARTUP_UPDATES), true);
	this.moveOneAtATime = new JCheckBox(Strings.prefs(PrefString.ONE_MOVE), true);
	this.enableAnimation = new JCheckBox(Strings.prefs(PrefString.ENABLE_ANIMATION), true);
	this.actionDelay = new JComboBox<>(new String[] { Strings.prefs(PrefString.SPEED_1),
		Strings.prefs(PrefString.SPEED_2), Strings.prefs(PrefString.SPEED_3), Strings.prefs(PrefString.SPEED_4),
		Strings.prefs(PrefString.SPEED_5) });
	this.languageList = new JComboBox<>(Strings.allLanguages());
	this.editorLayoutList = new JComboBox<>(Strings.allEditorLayouts());
	this.editorShowAllObjects = new JCheckBox(Strings.prefs(PrefString.SHOW_ALL_OBJECTS), true);
	this.difficultyPicker = new JComboBox<>(PrefsGUI.DIFFICULTY_NAMES);
	this.prefFrame.setContentPane(mainPrefPane);
	this.prefFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.prefFrame.addWindowListener(handler);
	mainPrefPane.setLayout(new BorderLayout());
	this.prefFrame.setResizable(false);
	settingsPane.setLayout(new GridLayout(PrefsGUI.GRID_LENGTH, 1));
	settingsPane.add(this.sounds);
	settingsPane.add(this.music);
	settingsPane.add(this.enableAnimation);
	settingsPane.add(this.checkUpdatesStartup);
	settingsPane.add(this.moveOneAtATime);
	settingsPane.add(new JLabel(Strings.prefs(PrefString.SPEED_LABEL)));
	settingsPane.add(this.actionDelay);
	settingsPane.add(new JLabel(Strings.prefs(PrefString.ACTIVE_LANGUAGE_LABEL)));
	settingsPane.add(this.languageList);
	settingsPane.add(new JLabel(Strings.prefs(PrefString.EDITOR_LAYOUT_LABEL)));
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
	final var iconlogo = LogoLoader.getIconLogo();
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
		final var pm = PrefsGUI.this;
		final var cmd = e.getActionCommand();
		if (cmd.equals(Strings.dialog(DialogString.OK_BUTTON))) {
		    pm.setPrefs();
		} else if (cmd.equals(Strings.dialog(DialogString.CANCEL_BUTTON))) {
		    pm.hidePrefs();
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	@Override
	public void windowOpened(final WindowEvent e) {
	    // Do nothing
	}

	@Override
	public void windowClosing(final WindowEvent e) {
	    final var pm = PrefsGUI.this;
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
