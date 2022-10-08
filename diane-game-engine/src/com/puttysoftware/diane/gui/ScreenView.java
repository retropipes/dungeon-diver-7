/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 * 
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import java.lang.ref.WeakReference;

import javax.swing.JPanel;

public abstract class ScreenView {
    // Fields
    protected final MainWindow theFrame;
    protected JPanel thePanel = new JPanel();

    // Constructors
    protected ScreenView() {
	super();
	this.theFrame = MainWindow.mainWindow();
    }

    // Methods
    final void showScreen(final ScreenModel model, final WeakReference<ScreenController> controllerRef) {
	if (!model.isCustomUI()) {
	    this.theFrame.setAndSave(this.thePanel, model.getTitle());
	    this.theFrame.setSystemIcon(model.getSystemIcon());
	    this.theFrame.addWindowListener(controllerRef.get());
	    this.theFrame.pack();
	}
    }

    final void hideScreen(final ScreenModel model, final WeakReference<ScreenController> controllerRef) {
	if (!model.isCustomUI()) {
	    this.theFrame.removeWindowListener(controllerRef.get());
	}
	this.theFrame.restoreSaved();
    }

    final void setUpView(final ScreenModel model) {
	this.thePanel = this.populateMainPanel(model);
	this.thePanel.setOpaque(true);
    }

    protected abstract JPanel populateMainPanel(final ScreenModel model);
}