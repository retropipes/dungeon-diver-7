/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.gui;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.Diane;
import com.puttysoftware.diane.assets.music.DianeMusicIndex;
import com.puttysoftware.diane.assets.music.DianeMusicPlayer;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.internal.DefaultAssets;

public final class MainWindow {
    private static MainWindow window;

    public static MainWindow mainWindow() {
        return MainWindow.window;
    }

    public static void createMainWindow(final int width, final int height) {
        if (MainWindow.window == null) {
            MainWindow.window = new MainWindow(width, height);
            MainWindow.window.frame.setIconImage(CommonDialogs.icon());
        }
    }

    static JFrame owner() {
        return MainWindow.mainWindow().frame;
    }

    private final JFrame frame;
    private final Dimension contentSize;
    private JComponent content;
    private DianeMusicIndex currentMusic;
    private JButton currentDefault;
    private int savedDepth;
    private int savedTitleDepth;
    private final LinkedList<JComponent> savedContentStack;
    private final LinkedList<String> savedTitleStack;
    private final LinkedList<DianeMusicIndex> savedMusicStack;
    private final LinkedList<JButton> savedDefaultButtonStack;

    private MainWindow(final int width, final int height) {
        this.frame = new JFrame();
        this.frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.frame.setResizable(false);
        this.contentSize = new Dimension(width, height);
        this.content = this.createContent();
        this.savedDepth = 0;
        this.savedTitleDepth = 0;
        this.savedContentStack = new LinkedList<>();
        this.savedTitleStack = new LinkedList<>();
        this.savedMusicStack = new LinkedList<>();
        this.savedDefaultButtonStack = new LinkedList<>();
        this.frame.setContentPane(this.content);
        this.frame.setVisible(true);
        this.frame.pack();
    }

    public void addKeyListener(final KeyListener l) {
        this.frame.addKeyListener(l);
    }

    public void addWindowListener(final WindowListener l) {
        this.frame.addWindowListener(l);
    }

    public void addWindowFocusListener(final WindowFocusListener l) {
        this.frame.addWindowFocusListener(l);
    }

    public void checkAndSetTitle(final String title) {
        if (!this.savedTitleStack.isEmpty()) {
            this.frame.setTitle(title);
        }
    }

    public boolean checkContent(final JComponent customContent) {
        return this.content.equals(customContent);
    }

    JComponent content() {
        return this.content;
    }

    public int getHeight() {
        return this.content.getHeight();
    }

    public Dimension getPreferredSize() {
        return this.contentSize;
    }

    public int getWidth() {
        return this.content.getWidth();
    }

    public boolean isEnabled() {
        return this.frame.isEnabled();
    }

    public void pack() {
        this.frame.pack();
    }

    public void removeKeyListener(final KeyListener l) {
        this.frame.removeKeyListener(l);
    }

    public void removeTransferHandler() {
        this.frame.setTransferHandler(null);
    }

    public void removeWindowListener(final WindowListener l) {
        this.frame.removeWindowListener(l);
    }

    public void removeWindowFocusListener(final WindowFocusListener l) {
        this.frame.removeWindowFocusListener(l);
    }

    public void restoreSaved() {
        this.restoreSavedOthers();
        this.restoreSavedTitle();
    }

    public void restoreSavedOthers() {
        if (this.savedDepth > 0) {
            this.savedDepth--;
            this.content = this.savedContentStack.pop();
            this.frame.setContentPane(this.content);
            var oldMusic = this.currentMusic;
            this.currentMusic = this.savedMusicStack.pop();
            if (this.currentMusic != null && this.currentMusic != oldMusic) {
                try {
                    DianeMusicPlayer.play(this.currentMusic);
                } catch (IOException e) {
                    Diane.handleError(e);
                }
            }
            this.currentDefault = this.savedDefaultButtonStack.pop();
            if (this.currentDefault != null) {
                this.frame.getRootPane().setDefaultButton(this.currentDefault);
            }
        }
    }

    public void restoreSavedTitle() {
        if (this.savedTitleDepth > 0) {
            this.savedTitleDepth--;
            this.frame.setTitle(this.savedTitleStack.pop());
        }
    }

    public void setAndSave(final JComponent customContent, final String title) {
        this.savedContentStack.push(this.content);
        this.savedTitleStack.push(this.frame.getTitle());
        this.savedMusicStack.push(this.currentMusic);
        this.savedDefaultButtonStack.push(this.currentDefault);
        this.savedDepth++;
        this.savedTitleDepth++;
        this.content = customContent;
        this.frame.setContentPane(this.content);
        this.frame.setTitle(title);
        this.currentMusic = DefaultAssets.NO_MUSIC;
        this.currentDefault = null;
    }

    public void setAndSave(final JComponent customContent) {
        this.savedContentStack.push(this.content);
        this.savedMusicStack.push(this.currentMusic);
        this.savedDefaultButtonStack.push(this.currentDefault);
        this.savedDepth++;
        this.savedTitleDepth++;
        this.content = customContent;
        this.frame.setContentPane(this.content);
        this.currentMusic = DefaultAssets.NO_MUSIC;
        this.currentDefault = null;
    }

    public void setAndSave(final JComponent customContent, final String title, final JButton defaultButton) {
        this.savedContentStack.push(this.content);
        this.savedTitleStack.push(this.frame.getTitle());
        this.savedMusicStack.push(this.currentMusic);
        this.savedDefaultButtonStack.push(this.currentDefault);
        this.savedDepth++;
        this.savedTitleDepth++;
        this.content = customContent;
        this.frame.setContentPane(this.content);
        this.frame.setTitle(title);
        this.currentMusic = DefaultAssets.NO_MUSIC;
        this.currentDefault = defaultButton;
        this.frame.getRootPane().setDefaultButton(defaultButton);
    }

    public void setAndSave(final JComponent customContent, final String title, final DianeMusicIndex music) {
        this.savedContentStack.push(this.content);
        this.savedTitleStack.push(this.frame.getTitle());
        this.savedMusicStack.push(this.currentMusic);
        this.savedDefaultButtonStack.push(this.currentDefault);
        this.savedDepth++;
        this.savedTitleDepth++;
        this.content = customContent;
        this.frame.setContentPane(this.content);
        this.frame.setTitle(title);
        var oldMusic = this.currentMusic;
        this.currentMusic = music;
        this.currentDefault = null;
        if (this.currentMusic != null && this.currentMusic != oldMusic) {
            try {
                DianeMusicPlayer.play(this.currentMusic);
            } catch (IOException e) {
                Diane.handleError(e);
            }
        }
    }

    public void setAndSave(final JComponent customContent, final String title, final DianeMusicIndex music,
            final JButton defaultButton) {
        this.savedContentStack.push(this.content);
        this.savedTitleStack.push(this.frame.getTitle());
        this.savedMusicStack.push(this.currentMusic);
        this.savedDefaultButtonStack.push(this.currentDefault);
        this.savedDepth++;
        this.savedTitleDepth++;
        this.content = customContent;
        this.frame.setContentPane(this.content);
        this.frame.setTitle(title);
        var oldMusic = this.currentMusic;
        this.currentMusic = music;
        if (this.currentMusic != null && this.currentMusic != oldMusic) {
            try {
                DianeMusicPlayer.play(this.currentMusic);
            } catch (IOException e) {
                Diane.handleError(e);
            }
        }
        this.currentDefault = defaultButton;
        this.frame.getRootPane().setDefaultButton(defaultButton);
    }

    public void setAndSave(final Screen screen) {
        this.savedContentStack.push(this.content);
        this.savedTitleStack.push(this.frame.getTitle());
        this.savedMusicStack.push(this.currentMusic);
        this.savedDefaultButtonStack.push(this.currentDefault);
        this.savedDepth++;
        this.savedTitleDepth++;
        this.content = screen.content();
        this.frame.setContentPane(this.content);
        this.frame.setTitle(screen.title());
        var oldMusic = this.currentMusic;
        this.currentMusic = screen.music();
        if (this.currentMusic != null && this.currentMusic != oldMusic) {
            try {
                DianeMusicPlayer.play(this.currentMusic);
            } catch (IOException e) {
                Diane.handleError(e);
            }
        }
        this.currentDefault = screen.defaultButton();
        this.frame.getRootPane().setDefaultButton(screen.defaultButton());
    }

    public void setDirty(final boolean newDirty) {
        this.frame.getRootPane().putClientProperty("Window.documentModified", Boolean.valueOf(newDirty));
    }

    public void setDefaultButton(final JButton button) {
        this.frame.getRootPane().setDefaultButton(button);
    }

    public void setEnabled(final boolean value) {
        this.frame.setEnabled(value);
    }

    public void setMenus(final JMenuBar menus) {
        this.frame.setJMenuBar(menus);
    }

    public void setTitle(final String title) {
        this.savedTitleStack.push(this.frame.getTitle());
        this.savedTitleDepth++;
        this.frame.setTitle(title);
    }

    public void setTransferHandler(final TransferHandler h) {
        this.frame.setTransferHandler(h);
    }

    public final JPanel createContent() {
        var newContent = new JPanel();
        newContent.setPreferredSize(this.contentSize);
        newContent.setMinimumSize(this.contentSize);
        newContent.setMaximumSize(this.contentSize);
        newContent.setSize(this.contentSize);
        return newContent;
    }
}
