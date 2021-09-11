/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 * 
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;

public final class MainWindow {
  private static MainWindow window;
  private final JFrame frame;
  private JPanel content;
  private JPanel savedContent;
  private boolean customUI;

  private MainWindow(final boolean isCustom) {
    super();
    this.customUI = isCustom;
    this.frame = new JFrame();
    if (isCustom) {
      this.frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    } else {
      this.frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
    }
    this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.frame.setResizable(false);
    this.content = new JPanel();
    this.savedContent = this.content;
    this.frame.setContentPane(this.content);
    this.frame.setVisible(true);
  }

  static JFrame owner() {
    return MainWindow.getOutputFrame().frame;
  }

  public static MainWindow getOutputFrame() {
    if (MainWindow.window == null || MainWindow.window.customUI) {
      MainWindow.window = new MainWindow(false);
    }
    return MainWindow.window;
  }

  public static MainWindow getOutputFrame(final boolean isCustom) {
    if (MainWindow.window == null || MainWindow.window.customUI != isCustom) {
      MainWindow.window = new MainWindow(isCustom);
    }
    return MainWindow.window;
  }

  public void attachContent(final JPanel customContent) {
    this.content = customContent;
    this.frame.setContentPane(this.content);
  }

  public void attachAndSave(final JPanel customContent) {
    this.savedContent = this.content;
    this.content = customContent;
    this.frame.setContentPane(this.content);
  }

  public void restoreSaved() {
    this.content = this.savedContent;
    this.frame.setContentPane(this.content);
  }

  public void setTitle(final String title) {
    this.frame.setTitle(title);
  }

  public void setSystemIcon(final Image icon) {
    this.frame.setIconImage(icon);
  }

  public void pack() {
    this.frame.pack();
  }

  public void addWindowListener(final WindowListener l) {
    this.frame.addWindowListener(l);
  }

  public void removeWindowListener(final WindowListener l) {
    this.frame.removeWindowListener(l);
  }

  public void addKeyListener(final KeyListener l) {
    this.frame.addKeyListener(l);
  }

  public void removeKeyListener(final KeyListener l) {
    this.frame.removeKeyListener(l);
  }

  public void setDefaultButton(final JButton defaultButton) {
    this.frame.getRootPane().setDefaultButton(defaultButton);
  }
}
