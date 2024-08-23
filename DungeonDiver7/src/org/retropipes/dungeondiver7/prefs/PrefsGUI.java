/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.prefs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.EditorLayout;
import org.retropipes.dungeondiver7.locale.PrefString;
import org.retropipes.dungeondiver7.locale.Strings;

class PrefsGUI {
    private static String[] DIFFICULTY_NAMES = Strings.allDifficulties();
    private static final int GRID_LENGTH = 18;
    // Fields
    private MainWindow mainWindow;
    private PrefsGUIActionHandler ahandler;
    private PrefsGUIWindowHandler whandler;
    private MainContent mainPrefPane;
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
    private JComboBox<String> battleMechanicPicker;
    private JComboBox<String> battleStylePicker;

    // Constructors
    PrefsGUI() {
	this.setUpGUI();
	this.setDefaultPrefs();
    }

    void activeLanguageChanged() {
	PrefsGUI.DIFFICULTY_NAMES = Strings.allDifficulties();
	this.setUpGUI();
	Prefs.writePrefs();
	this.loadPrefs();
    }

    void hidePrefs() {
	final var app = DungeonDiver7.getStuffBag();
	app.restoreFormerMode();
	this.mainWindow.removeWindowListener(this.whandler);
	this.mainWindow.restoreSaved();
	Prefs.writePrefs();
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
	this.difficultyPicker.setSelectedIndex(Prefs.getGameDifficulty().ordinal());
    }

    private void setDefaultPrefs() {
	Prefs.readPrefs();
	this.loadPrefs();
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

    private void setUpGUI() {
	this.ahandler = new PrefsGUIActionHandler(this);
	this.whandler = new PrefsGUIWindowHandler(this);
	this.mainWindow = MainWindow.mainWindow();
	this.mainPrefPane = this.mainWindow.createContent();
	final var buttonPane = this.mainWindow.createContent();
	final var settingsPane = this.mainWindow.createContent();
	final var prefsOK = new JButton(Strings.dialog(DialogString.OK_BUTTON));
	prefsOK.setDefaultCapable(true);
	this.mainWindow.setDefaultButton(prefsOK);
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
	this.battleMechanicPicker = new JComboBox<>(Strings.allBattleMechanics());
	this.battleStylePicker = new JComboBox<>(Strings.allBattleStyles());
	this.mainPrefPane.setLayout(new BorderLayout());
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
	settingsPane.add(new JLabel(Strings.prefs(PrefString.GAME_DIFFICULTY)));
	settingsPane.add(this.difficultyPicker);
	settingsPane.add(new JLabel(Strings.prefs(PrefString.BATTLE_MECHANIC)));
	settingsPane.add(this.battleMechanicPicker);
	settingsPane.add(new JLabel(Strings.prefs(PrefString.BATTLE_STYLE)));
	settingsPane.add(this.battleStylePicker);
	buttonPane.setLayout(new FlowLayout());
	buttonPane.add(prefsOK);
	buttonPane.add(prefsCancel);
	this.mainPrefPane.add(settingsPane, BorderLayout.CENTER);
	this.mainPrefPane.add(buttonPane, BorderLayout.SOUTH);
	prefsOK.addActionListener(this.ahandler);
	prefsCancel.addActionListener(this.ahandler);
    }

    public void showPrefs() {
	final var app = DungeonDiver7.getStuffBag();
	app.setInPrefs();
	this.mainWindow.setAndSave(this.mainPrefPane, Strings.prefs(PrefString.TITLE));
	this.mainWindow.addWindowListener(this.whandler);
    }
}
