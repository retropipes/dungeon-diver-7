/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 * 
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.screens;

import java.awt.event.WindowListener;
import java.lang.ref.WeakReference;

public abstract class ScreenController implements WindowListener {
  // Fields
  private final ScreenModel model;
  private final ScreenView view;
  private boolean viewReady;

  // Constructors
  protected ScreenController(final ScreenModel theModel, final ScreenView theView) {
    super();
    this.model = theModel;
    this.view = theView;
    this.viewReady = false;
  }

  // Methods
  public final void showScreen() {
    if (!this.viewReady) {
      this.view.setUpView(this.model);
      this.viewReady = true;
    }
    this.view.showScreen(this.model, new WeakReference<>(this));
  }

  protected final void hideScreen() {
    if (!this.viewReady) {
      this.view.setUpView(this.model);
      this.viewReady = true;
    }
    this.view.hideScreen(this.model, new WeakReference<>(this));
  }
}
