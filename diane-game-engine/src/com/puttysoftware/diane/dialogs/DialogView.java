/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 * 
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.lang.ref.WeakReference;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.puttysoftware.diane.gui.MainWindow;

final class DialogView {
  // Fields
  private final MainWindow theFrame;
  private final JPanel thePane = new JPanel();

  // Constructors
  DialogView() {
    super();
    this.theFrame = MainWindow.getOutputFrame();
  }

  // Methods
  void showDialog(final DialogModel model) {
    this.theFrame.attachAndSave(this.thePane);
    this.theFrame.setTitle(model.getTitle());
    this.theFrame.setSystemIcon(model.getSystemIcon());
    this.theFrame.pack();
  }

  void hideDialog() {
    this.theFrame.restoreSaved();
  }

  void setUpGUI(final DialogModel model, final WeakReference<DialogController> controllerRef) {
    final JPanel textPane = new JPanel();
    final JPanel buttonPane = new JPanel();
    final JPanel logoPane = new JPanel();
    final JButton theOK = new JButton(model.getActionButtonText());
    final JLabel miniLabel = new JLabel("", model.getMainImage(), SwingConstants.LEFT);
    miniLabel.setLabelFor(null);
    theOK.setDefaultCapable(true);
    this.thePane.setLayout(new BorderLayout());
    logoPane.setLayout(new FlowLayout());
    logoPane.add(miniLabel);
    textPane.setLayout(new GridLayout(model.getMessageCount(), 1));
    Iterable<String> messages = model.getMessages();
    for (String message : messages) {
      textPane.add(new JLabel(message));
    }
    buttonPane.setLayout(new FlowLayout());
    buttonPane.add(theOK);
    this.thePane.add(logoPane, BorderLayout.WEST);
    this.thePane.add(textPane, BorderLayout.CENTER);
    this.thePane.add(buttonPane, BorderLayout.SOUTH);
    theOK.addActionListener(controllerRef.get());
  }
}
