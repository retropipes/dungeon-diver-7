/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.editor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.retropipes.diane.gui.MainContent;
import org.retropipes.dungeondiver7.DungeonDiver7;

public abstract class GenericObjectEditor extends GenericEditor {
    private class EventHandler implements ActionListener, FocusListener {
	public EventHandler() {
	    // Do nothing
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    final var ge = GenericObjectEditor.this;
	    try {
		final var cmd = e.getActionCommand().substring(0, ge.actionCmdLen);
		final var num = Integer.parseInt(e.getActionCommand().substring(ge.actionCmdLen));
		ge.handleButtonClick(cmd, num);
		if (ge.autoStore) {
		    if (ge.guiEntryType(num) == GenericObjectEditor.ENTRY_TYPE_LIST) {
			final var list = ge.getEntryList(num);
			ge.autoStoreEntryListValue(list, num);
		    } else if (ge.guiEntryType(num) == GenericObjectEditor.ENTRY_TYPE_TEXT) {
			final var entry = ge.getEntryField(num);
			ge.autoStoreEntryFieldValue(entry, num);
		    }
		}
	    } catch (final NumberFormatException nfe) {
		// Ignore
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}

	@Override
	public void focusGained(final FocusEvent fe) {
	    // Do nothing
	}

	@Override
	public void focusLost(final FocusEvent fe) {
	    final var ge = GenericObjectEditor.this;
	    try {
		final var comp = fe.getComponent();
		if (comp.getClass().equals(JTextField.class)) {
		    final var entry = (JTextField) comp;
		    final var num = Integer.parseInt(entry.getName());
		    ge.autoStoreEntryFieldValue(entry, num);
		} else if (comp.getClass().equals(JComboBox.class)) {
		    @SuppressWarnings("unchecked")
		    final var list = (JComboBox<String>) comp;
		    final var num = Integer.parseInt(list.getName());
		    ge.autoStoreEntryListValue(list, num);
		}
	    } catch (final NumberFormatException nfe) {
		// Ignore
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}
    }

    public static final boolean ENTRY_TYPE_TEXT = false;
    public static final boolean ENTRY_TYPE_LIST = true;
    // Fields
    final int actionCmdLen;
    private final int abRows;
    private final int abCols;
    private JLabel[] nameLabels;
    private JTextField[] entryFields;
    private ArrayList<JComboBox<String>> entryLists;
    private JButton[][] actionButtons;
    boolean autoStore;
    private EventHandler handler;

    protected GenericObjectEditor(final String newSource, final int actionCommandLength, final int actionButtonRows,
	    final int actionButtonCols, final boolean autoStoreEnabled) {
	super(newSource);
	this.actionCmdLen = actionCommandLength;
	this.abRows = actionButtonRows;
	this.abCols = actionButtonCols;
	this.autoStore = autoStoreEnabled;
    }

    protected abstract void autoStoreEntryFieldValue(JTextField entry, int num);

    protected abstract void autoStoreEntryListValue(JComboBox<String> list, int num);

    @Override
    protected void borderPaneHook() {
	// Do nothing
    }

    @Override
    protected void editObjectChanged() {
	// Do nothing
    }

    protected JTextField getEntryField(final int num) {
	return this.entryFields[num];
    }

    protected JComboBox<String> getEntryList(final int num) {
	return this.entryLists.get(num);
    }

    protected abstract String guiActionButtonActionCommand(int row, int col);

    protected abstract void guiActionButtonProperties(JButton actBtn, int row, int col);

    protected abstract void guiEntryFieldProperties(JTextField entry, int num);

    protected abstract String[] guiEntryListItems(int num);

    protected abstract void guiEntryListProperties(JComboBox<String> list, int num);

    protected abstract boolean guiEntryType(int num);

    protected abstract void guiNameLabelProperties(JLabel nameLbl, int num);

    protected abstract void handleButtonClick(String cmd, int num);

    @Override
    public void redrawEditor() {
	// Do nothing
    }

    @Override
    protected void reSetUpGUIHook(final MainContent outputPane) {
	this.setUpGUIHook(outputPane);
    }

    @Override
    protected void setUpGUIHook(final MainContent outputPane) {
	this.handler = new EventHandler();
	outputPane.setLayout(new GridLayout(this.abCols, this.abRows));
	this.nameLabels = new JLabel[this.abCols];
	this.entryFields = new JTextField[this.abCols];
	this.entryLists = new ArrayList<>(this.abCols);
	for (var x = 0; x < this.abCols; x++) {
	    this.entryLists.add(x, new JComboBox<>(new String[] {}));
	}
	this.actionButtons = new JButton[this.abRows - 2][this.abCols];
	// Grid rows
	for (var x = 0; x < this.abCols; x++) {
	    // Create controls
	    this.nameLabels[x] = new JLabel();
	    this.guiNameLabelProperties(this.nameLabels[x], x);
	    final var entryType = this.guiEntryType(x);
	    if (entryType == GenericObjectEditor.ENTRY_TYPE_LIST) {
		this.entryLists.set(x, new JComboBox<>(this.guiEntryListItems(x)));
		this.guiEntryListProperties(this.entryLists.get(x), x);
		if (this.isReadOnly()) {
		    this.entryLists.get(x).setEnabled(false);
		} else if (this.autoStore) {
		    this.entryLists.get(x).setName(Integer.toString(x));
		    this.entryLists.get(x).addFocusListener(this.handler);
		}
	    } else if (entryType == GenericObjectEditor.ENTRY_TYPE_TEXT) {
		this.entryFields[x] = new JTextField();
		this.guiEntryFieldProperties(this.entryFields[x], x);
		if (this.isReadOnly()) {
		    this.entryFields[x].setEnabled(false);
		} else if (this.autoStore) {
		    this.entryFields[x].setName(Integer.toString(x));
		    this.entryFields[x].addFocusListener(this.handler);
		}
	    }
	    for (var y = 0; y < this.abRows - 2; y++) {
		this.actionButtons[y][x] = new JButton();
		this.guiActionButtonProperties(this.actionButtons[y][x], x, y);
		this.actionButtons[y][x].setActionCommand(this.guiActionButtonActionCommand(x, y));
		// Add action listener for button
		this.actionButtons[y][x].addActionListener(this.handler);
		if (this.isReadOnly()) {
		    this.actionButtons[y][x].setEnabled(false);
		}
	    }
	    // Add controls
	    outputPane.add(this.nameLabels[x]);
	    if (entryType == GenericObjectEditor.ENTRY_TYPE_LIST) {
		outputPane.add(this.entryLists.get(x));
	    } else if (entryType == GenericObjectEditor.ENTRY_TYPE_TEXT) {
		outputPane.add(this.entryFields[x]);
	    }
	    for (var y = 0; y < this.abRows - 2; y++) {
		outputPane.add(this.actionButtons[y][x]);
	    }
	}
    }

    @Override
    public void switchFromSubEditor() {
	// Do nothing
    }

    @Override
    public void switchToSubEditor() {
	// Do nothing
    }
}
