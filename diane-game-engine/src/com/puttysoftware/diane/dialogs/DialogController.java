/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 * 
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.dialogs;

import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;

public abstract class DialogController implements ActionListener {
  // Fields
  private final DialogModel model;
  private DialogView view;

  // Constructors
  protected DialogController(final DialogModel dialogModel) {
    super();
    this.model = dialogModel;
  }

  // Methods
  private void checkView() {
    if (this.view == null) {
      this.view = new DialogView();
      this.view.setUpGUI(this.model, new WeakReference<>(this));
    }
  }

  public final void showDialog() {
    this.checkView();
    this.view.showDialog(this.model);
  }

  protected final void hideDialog() {
    this.checkView();
    this.view.hideDialog();
  }
}
