/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 * 
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import java.awt.event.WindowListener;
import java.lang.ref.WeakReference;

import javax.swing.JPanel;

public abstract class ScreenController implements WindowListener {
    // Fields
    private ScreenModel model;
    private ScreenView view;
    private boolean viewReady;

    // Constructors
    protected ScreenController() {
	super();
	this.viewReady = false;
    }

    // Methods
    private void checkView() {
	if (this.model == null || this.view == null) {
	    throw new IllegalStateException();
	}
	if (!this.viewReady) {
	    this.view.setUpView(this.model);
	    this.viewReady = true;
	}
    }

    public final void setModel(final ScreenModel screenModel) {
	this.model = screenModel;
    }

    public final void setView(final ScreenView screenView) {
	this.view = screenView;
    }

    public final void showScreen() {
	this.checkView();
	this.view.showScreen(this.model, new WeakReference<>(this));
    }

    protected final void hideScreen() {
	this.checkView();
	this.view.hideScreen(this.model, new WeakReference<>(this));
    }

    JPanel content() {
	this.checkView();
	return this.view.content();
    }
    
    String title() {
	if (this.model == null) {
	    throw new IllegalStateException();
	}
	return this.model.getTitle();
    }
}
