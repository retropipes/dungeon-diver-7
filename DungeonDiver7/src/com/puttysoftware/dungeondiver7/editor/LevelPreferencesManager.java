/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Difficulty;
import com.puttysoftware.dungeondiver7.locale.EditorString;
import com.puttysoftware.dungeondiver7.locale.Strings;

class LevelPreferencesManager {
    // Fields
    private JFrame prefFrame;
    private JCheckBox horizontalWrap;
    private JCheckBox verticalWrap;
    private JCheckBox thirdWrap;
    private JTextField name;
    private JTextField author;
    private JTextArea hint;
    private JComboBox<String> difficulty;
    private JCheckBox moveShoot;

    // Constructors
    public LevelPreferencesManager() {
	this.setUpGUI();
    }

    // Methods
    void showPrefs() {
	this.loadPrefs();
	DungeonDiver7.getStuffBag().getEditor().disableOutput();
	this.prefFrame.setVisible(true);
    }

    void hidePrefs() {
	this.prefFrame.setVisible(false);
	DungeonDiver7.getStuffBag().getEditor().enableOutput();
	DungeonDiver7.getStuffBag().getDungeonManager().setDirty(true);
	DungeonDiver7.getStuffBag().getEditor().redrawEditor();
    }

    void setPrefs() {
	final var m = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	if (this.horizontalWrap.isSelected()) {
	    m.enableHorizontalWraparound();
	} else {
	    m.disableHorizontalWraparound();
	}
	if (this.verticalWrap.isSelected()) {
	    m.enableVerticalWraparound();
	} else {
	    m.disableVerticalWraparound();
	}
	if (this.thirdWrap.isSelected()) {
	    m.enableThirdDimensionWraparound();
	} else {
	    m.disableThirdDimensionWraparound();
	}
	m.setName(this.name.getText());
	m.setAuthor(this.author.getText());
	m.setHint(this.hint.getText());
	m.setDifficulty(Difficulty.values()[this.difficulty.getSelectedIndex() + 1]);
	m.setMoveShootAllowedThisLevel(this.moveShoot.isSelected());
    }

    private void loadPrefs() {
	final var m = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
	this.horizontalWrap.setSelected(m.isHorizontalWraparoundEnabled());
	this.verticalWrap.setSelected(m.isVerticalWraparoundEnabled());
	this.thirdWrap.setSelected(m.isThirdDimensionWraparoundEnabled());
	this.name.setText(m.getName());
	this.author.setText(m.getAuthor());
	this.hint.setText(m.getHint());
	this.difficulty.setSelectedIndex(m.getDifficulty().ordinal() - 1);
	this.moveShoot.setSelected(m.isMoveShootAllowedThisLevel());
    }

    private void setUpGUI() {
	JPanel mainPrefPane, contentPane, buttonPane;
	JButton prefsOK, prefsCancel;
	final var handler = new EventHandler();
	this.prefFrame = new JFrame(Strings.editor(EditorString.LEVEL_PREFERENCES));
	final var iconlogo = LogoLoader.getIconLogo();
	this.prefFrame.setIconImage(iconlogo);
	mainPrefPane = new JPanel();
	contentPane = new JPanel();
	buttonPane = new JPanel();
	prefsOK = new JButton(Strings.dialog(DialogString.OK_BUTTON));
	prefsOK.setDefaultCapable(true);
	this.prefFrame.getRootPane().setDefaultButton(prefsOK);
	prefsCancel = new JButton(Strings.dialog(DialogString.CANCEL_BUTTON));
	prefsCancel.setDefaultCapable(false);
	this.horizontalWrap = new JCheckBox(Strings.editor(EditorString.ENABLE_HORIZONTAL_WRAP_AROUND), false);
	this.verticalWrap = new JCheckBox(Strings.editor(EditorString.ENABLE_VERTICAL_WRAP_AROUND), false);
	this.thirdWrap = new JCheckBox(Strings.editor(EditorString.ENABLE_THIRD_DIMENSION_WRAP_AROUND), false);
	this.name = new JTextField();
	this.author = new JTextField();
	this.hint = new JTextArea(8, 32);
	this.difficulty = new JComboBox<>(Strings.allDifficulties());
	this.moveShoot = new JCheckBox(Strings.editor(EditorString.ENABLE_MOVE_SHOOT), true);
	this.prefFrame.setContentPane(mainPrefPane);
	this.prefFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.prefFrame.addWindowListener(handler);
	mainPrefPane.setLayout(new BorderLayout());
	this.prefFrame.setResizable(false);
	contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
	contentPane.add(this.horizontalWrap);
	contentPane.add(this.verticalWrap);
	contentPane.add(this.thirdWrap);
	contentPane.add(new JLabel(Strings.editor(EditorString.LEVEL_NAME)));
	contentPane.add(this.name);
	contentPane.add(new JLabel(Strings.editor(EditorString.LEVEL_AUTHOR)));
	contentPane.add(this.author);
	contentPane.add(new JLabel(Strings.editor(EditorString.LEVEL_HINT)));
	contentPane.add(this.hint);
	contentPane.add(new JLabel(Strings.editor(EditorString.LEVEL_DIFFICULTY)));
	contentPane.add(this.difficulty);
	contentPane.add(this.moveShoot);
	buttonPane.setLayout(new FlowLayout());
	buttonPane.add(prefsOK);
	buttonPane.add(prefsCancel);
	mainPrefPane.add(contentPane, BorderLayout.CENTER);
	mainPrefPane.add(buttonPane, BorderLayout.SOUTH);
	prefsOK.addActionListener(handler);
	prefsCancel.addActionListener(handler);
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
		final var lpm = LevelPreferencesManager.this;
		final var cmd = e.getActionCommand();
		if (cmd.equals(Strings.dialog(DialogString.OK_BUTTON))) {
		    lpm.setPrefs();
		    lpm.hidePrefs();
		} else if (cmd.equals(Strings.dialog(DialogString.CANCEL_BUTTON))) {
		    lpm.hidePrefs();
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	// handle window
	@Override
	public void windowOpened(final WindowEvent e) {
	    // Do nothing
	}

	@Override
	public void windowClosing(final WindowEvent e) {
	    final var pm = LevelPreferencesManager.this;
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
