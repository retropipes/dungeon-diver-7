/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 * 
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;

public final class MainWindow {
    private static MainWindow window;
    private final JFrame frame;
    private JComponent content;
    private JComponent savedContent;

    private MainWindow() {
	super();
	this.frame = new JFrame();
	this.frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
	this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.frame.setResizable(false);
	this.content = new JPanel();
	this.savedContent = this.content;
	this.frame.setContentPane(this.content);
	this.frame.setVisible(true);
    }

    static JFrame owner() {
	return MainWindow.mainWindow().frame;
    }

    JComponent content() {
	return this.content;
    }

    public static MainWindow mainWindow() {
	if (MainWindow.window == null) {
	    MainWindow.window = new MainWindow();
	}
	return MainWindow.window;
    }

    @Deprecated
    /** Call setContent(JComponent) instead of this method. **/
    public void setContentPane(final JComponent customContent) {
	this.setContent(customContent);
    }

    public void setContent(final JComponent customContent) {
	this.content = customContent;
	this.frame.setContentPane(this.content);
    }

    public void setAndSaveContent(final JComponent customContent) {
	this.savedContent = this.content;
	this.content = customContent;
	this.frame.setContentPane(this.content);
    }

    public void restoreSavedContent() {
	this.content = this.savedContent;
	this.frame.setContentPane(this.content);
    }

    @Deprecated
    /** Call setMenus(JMenuBar) instead of this method. **/
    public void setJMenuBar(final JMenuBar menus) {
	this.setMenus(menus);
    }

    public void setMenus(final JMenuBar menus) {
	this.frame.setJMenuBar(menus);
    }

    public void setTitle(final String title) {
	this.frame.setTitle(title);
    }

    @Deprecated
    /** Call setSystemIcon(Image) instead of this method. **/
    public void setIconImage(final Image icon) {
	this.setSystemIcon(icon);
    }

    public void setSystemIcon(final Image icon) {
	this.frame.setIconImage(icon);
    }

    public void setDirty(final boolean newDirty) {
	this.frame.getRootPane().putClientProperty("Window.documentModified", Boolean.valueOf(newDirty));
    }

    @SuppressWarnings("static-method")
    @Deprecated(forRemoval = true)
    /** Don't call this method. **/
    public Container getContentPane() {
	throw new UnsupportedOperationException();
    }

    @SuppressWarnings("static-method")
    @Deprecated
    /** Don't call this method. **/
    public void dispose() {
	// Do nothing
    }

    @SuppressWarnings("static-method")
    @Deprecated
    /** Don't call this method. **/
    public void pack() {
	// Do nothing
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

    @SuppressWarnings("static-method")
    @Deprecated
    /** Don't call this method. **/
    public void paintComponents(final Graphics2D g) {
	// Do nothing
    }

    @SuppressWarnings("static-method")
    @Deprecated
    /** Don't call this method. **/
    public boolean isVisible() {
	return true; // Always visible
    }

    @SuppressWarnings("static-method")
    @Deprecated(forRemoval = true)
    /** Don't call this method. **/
    public JRootPane getRootPane() {
	throw new UnsupportedOperationException();
    }

    @SuppressWarnings("static-method")
    @Deprecated
    /** Don't call this method. **/
    public void setDefaultCloseOperation(final int operation) {
	// Do nothing
    }

    @SuppressWarnings("static-method")
    @Deprecated
    /** Don't call this method. **/
    public void setLayout(final LayoutManager layout) {
	// Do nothing
    }

    @SuppressWarnings("static-method")
    @Deprecated
    /** Don't call this method. **/
    public void setResizable(final boolean value) {
	// Do nothing
    }

    @SuppressWarnings("static-method")
    @Deprecated
    /** Don't call this method. **/
    public void setVisible(final boolean value) {
	// Do nothing
    }
}
