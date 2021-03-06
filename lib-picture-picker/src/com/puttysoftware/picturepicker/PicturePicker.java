/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.picturepicker;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.puttysoftware.images.BufferedImageIcon;

public final class PicturePicker {
    /**
     * @version 1.0.0
     */
    // Fields
    private BufferedImageIcon[] choices;
    private String[] choiceNames;
    private JLabel[] choiceArray;
    private final Container pickerContainer;
    private final Container choiceContainer;
    private final Container radioContainer;
    private final Container choiceRadioContainer;
    private final ButtonGroup radioGroup;
    private JRadioButton[] radioButtons;
    private final JScrollPane scrollPane;
    int index;
    private final EventHandler handler;

    // Constructor
    public PicturePicker(final BufferedImageIcon[] pictures, final String[] names) {
	this.handler = new EventHandler();
	this.pickerContainer = new Container();
	this.pickerContainer.setLayout(new BorderLayout());
	this.choiceContainer = new Container();
	this.radioContainer = new Container();
	this.radioGroup = new ButtonGroup();
	this.choiceRadioContainer = new Container();
	this.choiceRadioContainer.setLayout(new BorderLayout());
	this.choiceRadioContainer.add(this.radioContainer, BorderLayout.WEST);
	this.choiceRadioContainer.add(this.choiceContainer, BorderLayout.CENTER);
	this.scrollPane = new JScrollPane(this.choiceRadioContainer);
	this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	this.pickerContainer.add(this.scrollPane, BorderLayout.CENTER);
	this.updatePicker(pictures, names);
	this.index = 0;
    }

    public PicturePicker(final BufferedImageIcon[] pictures, final String[] names, final boolean[] enabled) {
	this.handler = new EventHandler();
	this.pickerContainer = new Container();
	this.pickerContainer.setLayout(new BorderLayout());
	this.choiceContainer = new Container();
	this.radioContainer = new Container();
	this.radioGroup = new ButtonGroup();
	this.choiceRadioContainer = new Container();
	this.choiceRadioContainer.setLayout(new BorderLayout());
	this.choiceRadioContainer.add(this.radioContainer, BorderLayout.WEST);
	this.choiceRadioContainer.add(this.choiceContainer, BorderLayout.CENTER);
	this.scrollPane = new JScrollPane(this.choiceRadioContainer);
	this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	this.pickerContainer.add(this.scrollPane, BorderLayout.CENTER);
	this.updatePicker(pictures, names, enabled);
	this.index = 0;
    }

    // Methods
    public Container getPicker() {
	return this.pickerContainer;
    }

    public void disablePicker() {
	this.pickerContainer.setEnabled(false);
	for (final JRadioButton radioButton : this.radioButtons) {
	    radioButton.setEnabled(false);
	}
    }

    public void enablePicker() {
	this.pickerContainer.setEnabled(true);
	for (final JRadioButton radioButton : this.radioButtons) {
	    radioButton.setEnabled(true);
	}
    }

    public void updatePicker(final BufferedImageIcon[] newImages, final String[] newNames) {
	this.choices = newImages;
	this.choiceNames = newNames;
	this.choiceContainer.removeAll();
	this.radioContainer.removeAll();
	this.radioButtons = new JRadioButton[this.choices.length];
	this.choiceContainer.setLayout(new GridLayout(this.choices.length, 1));
	this.radioContainer.setLayout(new GridLayout(this.choices.length, 1));
	this.choiceArray = new JLabel[this.choices.length];
	for (int x = 0; x < this.choices.length; x++) {
	    this.choiceArray[x] = new JLabel(this.choiceNames[x], this.choices[x], SwingConstants.LEFT);
	    this.choiceArray[x].setOpaque(true);
	    this.choiceContainer.add(this.choiceArray[x]);
	    this.radioButtons[x] = new JRadioButton();
	    this.radioButtons[x].setOpaque(true);
	    this.radioButtons[x].setActionCommand(Integer.valueOf(x).toString());
	    this.radioGroup.add(this.radioButtons[x]);
	    this.radioButtons[x].addActionListener(this.handler);
	    this.radioButtons[x].setEnabled(true);
	    this.radioContainer.add(this.radioButtons[x]);
	}
	for (int x = 0; x < this.choices.length; x++) {
	    this.radioButtons[x].setSelected(true);
	    this.index = x;
	}
    }

    public void updatePicker(final BufferedImageIcon[] newImages, final String[] newNames, final boolean[] enabled) {
	this.choices = newImages;
	this.choiceNames = newNames;
	this.choiceContainer.removeAll();
	this.radioContainer.removeAll();
	this.radioButtons = new JRadioButton[this.choices.length];
	this.choiceContainer.setLayout(new GridLayout(this.choices.length, 1));
	this.radioContainer.setLayout(new GridLayout(this.choices.length, 1));
	this.choiceArray = new JLabel[this.choices.length];
	for (int x = 0; x < this.choices.length; x++) {
	    this.choiceArray[x] = new JLabel(this.choiceNames[x], this.choices[x], SwingConstants.LEFT);
	    this.choiceArray[x].setOpaque(true);
	    this.choiceContainer.add(this.choiceArray[x]);
	    this.radioButtons[x] = new JRadioButton();
	    this.radioButtons[x].setOpaque(true);
	    this.radioButtons[x].setActionCommand(Integer.valueOf(x).toString());
	    this.radioGroup.add(this.radioButtons[x]);
	    this.radioButtons[x].addActionListener(this.handler);
	    this.radioButtons[x].setEnabled(enabled[x]);
	    this.radioContainer.add(this.radioButtons[x]);
	}
	for (int x = 0; x < this.choices.length; x++) {
	    if (enabled[x]) {
		this.radioButtons[x].setSelected(true);
		this.index = x;
		break;
	    }
	}
    }

    public void updatePickerLayout(final int maxHeight) {
	final int newPreferredWidth = this.pickerContainer.getLayout().preferredLayoutSize(this.pickerContainer).width
		+ this.scrollPane.getVerticalScrollBar().getWidth();
	final int newPreferredHeight = Math.min(maxHeight,
		this.pickerContainer.getLayout().preferredLayoutSize(this.pickerContainer).height);
	this.pickerContainer.setPreferredSize(new Dimension(newPreferredWidth, newPreferredHeight));
    }

    public void selectLastPickedChoice(final int lastPicked) {
	this.radioButtons[lastPicked].setSelected(true);
	this.index = lastPicked;
    }

    /**
     *
     * @return the index of the picture picked
     */
    public int getPicked() {
	return this.index;
    }

    private class EventHandler implements ActionListener {
	public EventHandler() {
	    // Do nothing
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    final String cmd = e.getActionCommand();
	    // A radio button
	    PicturePicker.this.index = Integer.parseInt(cmd);
	}
    }
}
