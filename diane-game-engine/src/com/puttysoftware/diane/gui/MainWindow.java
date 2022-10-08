/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 * 
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.strings.DianeStrings;

public final class MainWindow {
    private static MainWindow window;
    private final JFrame frame;
    private String savedTitle;
    private JComponent content;
    private JComponent savedContent;

    private MainWindow() {
	super();
	this.frame = new JFrame();
	this.frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
	this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.frame.setResizable(false);
	this.content = MainContentFactory.content();
	this.savedContent = MainContentFactory.content();
	this.savedTitle = DianeStrings.EMPTY;
	this.frame.setContentPane(this.content);
	this.frame.setVisible(true);
    }

    static JFrame owner() {
	return MainWindow.mainWindow().frame;
    }

    JComponent content() {
	return this.content;
    }

    void pack() {
	this.frame.pack();
    }

    public static MainWindow mainWindow() {
	if (MainWindow.window == null) {
	    MainWindow.window = new MainWindow();
	}
	return MainWindow.window;
    }

    public boolean checkContent(final JComponent customContent) {
	return this.content.equals(customContent);
    }

    public void setAndSave(final JComponent customContent, final String title) {
	this.savedContent = this.content;
	this.savedTitle = this.frame.getTitle();
	this.content = customContent;
	this.frame.setContentPane(this.content);
	this.frame.setTitle(title);
    }

    public void restoreSaved() {
	this.content = this.savedContent;
	this.frame.setContentPane(this.content);
	this.frame.setTitle(this.savedTitle);
    }

    public void checkAndSetTitle(final String title) {
	if (this.savedTitle != DianeStrings.EMPTY) {
	    this.frame.setTitle(title);
	}
    }

    public void setMenus(final JMenuBar menus) {
	this.frame.setJMenuBar(menus);
    }

    public void setSystemIcon(final Image icon) {
	this.frame.setIconImage(icon);
    }

    public void setDirty(final boolean newDirty) {
	this.frame.getRootPane().putClientProperty("Window.documentModified", Boolean.valueOf(newDirty));
    }

    public int getWidth() {
	return this.content.getWidth();
    }

    public int getHeight() {
	return this.content.getHeight();
    }

    public Dimension getPreferredSize() {
	return this.content.getPreferredSize();
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

    public void setTransferHandler(final TransferHandler h) {
	this.frame.setTransferHandler(h);
    }

    public void removeTransferHandler() {
	this.frame.setTransferHandler(null);
    }

    public void setDefaultButton(final JButton defaultButton) {
	this.frame.getRootPane().setDefaultButton(defaultButton);
    }

    public boolean isEnabled() {
	return this.frame.isEnabled();
    }

    public void setEnabled(final boolean value) {
	this.frame.setEnabled(value);
    }
}
