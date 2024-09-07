/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.settings;

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
import org.retropipes.dungeondiver7.locale.SettingString;
import org.retropipes.dungeondiver7.locale.Strings;

class SettingsGUI {
    private static String[] DIFFICULTY_NAMES = Strings.allDifficulties();
    private static final int GRID_LENGTH = 18;
    // Fields
    private MainWindow mainWindow;
    private SettingsGUIActionHandler ahandler;
    private SettingsGUIWindowHandler whandler;
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
    SettingsGUI() {
	this.setUpGUI();
	this.setDefaultPrefs();
    }

    void activeLanguageChanged() {
	SettingsGUI.DIFFICULTY_NAMES = Strings.allDifficulties();
	this.setUpGUI();
	Settings.writePrefs();
	this.loadPrefs();
    }

    void hidePrefs() {
	final var app = DungeonDiver7.getStuffBag();
	app.restoreFormerMode();
	this.mainWindow.removeWindowListener(this.whandler);
	this.mainWindow.restoreSaved();
	Settings.writePrefs();
    }

    private void loadPrefs() {
	this.enableAnimation.setSelected(Settings.enableAnimation());
	this.sounds.setSelected(Settings.getSoundsEnabled());
	this.music.setSelected(Settings.getMusicEnabled());
	this.checkUpdatesStartup.setSelected(Settings.shouldCheckUpdatesAtStartup());
	this.moveOneAtATime.setSelected(Settings.oneMove());
	this.actionDelay.setSelectedIndex(Settings.getActionDelay());
	this.languageList.setSelectedIndex(Settings.getLanguageID());
	this.editorLayoutList.setSelectedIndex(Settings.getEditorLayout().ordinal());
	this.editorShowAllObjects.setSelected(Settings.getEditorShowAllObjects());
	this.difficultyPicker.setSelectedIndex(Settings.getGameDifficulty().ordinal());
    }

    private void setDefaultPrefs() {
	Settings.readPrefs();
	this.loadPrefs();
    }

    void setPrefs() {
	Settings.setEnableAnimation(this.enableAnimation.isSelected());
	Settings.setSoundsEnabled(this.sounds.isSelected());
	Settings.setMusicEnabled(this.music.isSelected());
	Settings.setCheckUpdatesAtStartup(this.checkUpdatesStartup.isSelected());
	Settings.setOneMove(this.moveOneAtATime.isSelected());
	Settings.setActionDelay(this.actionDelay.getSelectedIndex());
	Settings.setLanguageID(this.languageList.getSelectedIndex());
	Settings.setEditorLayout(EditorLayout.values()[this.editorLayoutList.getSelectedIndex()]);
	Settings.setEditorShowAllObjects(this.editorShowAllObjects.isSelected());
	Settings.setGameDifficulty(this.difficultyPicker.getSelectedIndex());
	this.hidePrefs();
    }

    private void setUpGUI() {
	this.ahandler = new SettingsGUIActionHandler(this);
	this.whandler = new SettingsGUIWindowHandler(this);
	this.mainWindow = MainWindow.mainWindow();
	this.mainPrefPane = this.mainWindow.createContent();
	final var buttonPane = this.mainWindow.createContent();
	final var settingsPane = this.mainWindow.createContent();
	final var prefsOK = new JButton(Strings.dialog(DialogString.OK_BUTTON));
	prefsOK.setDefaultCapable(true);
	this.mainWindow.setDefaultButton(prefsOK);
	final var prefsCancel = new JButton(Strings.dialog(DialogString.CANCEL_BUTTON));
	prefsCancel.setDefaultCapable(false);
	this.sounds = new JCheckBox(Strings.settings(SettingString.ENABLE_SOUNDS), true);
	this.music = new JCheckBox(Strings.settings(SettingString.ENABLE_MUSIC), true);
	this.checkUpdatesStartup = new JCheckBox(Strings.settings(SettingString.STARTUP_UPDATES), true);
	this.moveOneAtATime = new JCheckBox(Strings.settings(SettingString.ONE_MOVE), true);
	this.enableAnimation = new JCheckBox(Strings.settings(SettingString.ENABLE_ANIMATION), true);
	this.actionDelay = new JComboBox<>(new String[] { Strings.settings(SettingString.SPEED_1),
		Strings.settings(SettingString.SPEED_2), Strings.settings(SettingString.SPEED_3), Strings.settings(SettingString.SPEED_4),
		Strings.settings(SettingString.SPEED_5) });
	this.languageList = new JComboBox<>(Strings.allLanguages());
	this.editorLayoutList = new JComboBox<>(Strings.allEditorLayouts());
	this.editorShowAllObjects = new JCheckBox(Strings.settings(SettingString.SHOW_ALL_OBJECTS), true);
	this.difficultyPicker = new JComboBox<>(SettingsGUI.DIFFICULTY_NAMES);
	this.battleMechanicPicker = new JComboBox<>(Strings.allBattleMechanics());
	this.battleStylePicker = new JComboBox<>(Strings.allBattleStyles());
	this.mainPrefPane.setLayout(new BorderLayout());
	settingsPane.setLayout(new GridLayout(SettingsGUI.GRID_LENGTH, 1));
	settingsPane.add(this.sounds);
	settingsPane.add(this.music);
	settingsPane.add(this.enableAnimation);
	settingsPane.add(this.checkUpdatesStartup);
	settingsPane.add(this.moveOneAtATime);
	settingsPane.add(new JLabel(Strings.settings(SettingString.SPEED_LABEL)));
	settingsPane.add(this.actionDelay);
	settingsPane.add(new JLabel(Strings.settings(SettingString.ACTIVE_LANGUAGE_LABEL)));
	settingsPane.add(this.languageList);
	settingsPane.add(new JLabel(Strings.settings(SettingString.EDITOR_LAYOUT_LABEL)));
	settingsPane.add(this.editorLayoutList);
	settingsPane.add(this.editorShowAllObjects);
	settingsPane.add(new JLabel(Strings.settings(SettingString.GAME_DIFFICULTY)));
	settingsPane.add(this.difficultyPicker);
	settingsPane.add(new JLabel(Strings.settings(SettingString.BATTLE_MECHANIC)));
	settingsPane.add(this.battleMechanicPicker);
	settingsPane.add(new JLabel(Strings.settings(SettingString.BATTLE_STYLE)));
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
	this.mainWindow.setAndSave(this.mainPrefPane, Strings.settings(SettingString.TITLE));
	this.mainWindow.addWindowListener(this.whandler);
    }
}
