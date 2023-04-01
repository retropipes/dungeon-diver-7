/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowListener;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loader.ImageLoader;
import com.puttysoftware.diane.integration.Integration;

public abstract class GenericEditor {
    // Fields
    private final String source;
    private MainWindow mainWindow;
    private JPanel borderPane;
    private JPanel outputPane;
    private JLabel messageLabel;
    private JScrollPane scrollPane;
    private boolean objectChanged;
    private boolean readOnly;

    protected GenericEditor(final String newSource) {
        this.source = newSource;
        this.objectChanged = true;
    }

    // Methods from EditorProperties
    public final String getEditorSource() {
        return this.source;
    }

    public final void edit() {
        final var app = DungeonDiver7.getStuffBag();
        app.getGUIManager().hideGUI();
        // Create the managers
        if (this.objectChanged) {
            this.loadObject();
            this.editObjectChanged();
            this.objectChanged = false;
        }
        this.setUpGUI();
        // Make sure message area is attached to border pane
        this.borderPane.removeAll();
        this.borderPane.add(this.messageLabel, BorderLayout.NORTH);
        this.borderPane.add(this.outputPane, BorderLayout.CENTER);
        this.borderPaneHook();
        this.showOutput();
        this.redrawEditor();
    }

    public final boolean create() {
        if (this.usesImporter()) {
            this.newObjectOptions();
            return true;
        }
        var success = true;
        if (this.newObjectOptions()) {
            success = this.newObjectCreate();
            if (success) {
                this.saveObject();
                this.objectChanged = true;
            }
            return success;
        }
        return false;
    }

    public final void showOutput() {
        final var app = DungeonDiver7.getStuffBag();
        Integration.integrate().setDefaultMenuBar(app.getMenuManager().getMainMenuBar());
        this.mainWindow.setAndSave(this.scrollPane, this.getEditorSource());
        final var wl = this.guiHookWindow();
        if (wl != null) {
            this.mainWindow.addWindowListener(wl);
        }
    }

    public final void hideOutput() {
        if (this.mainWindow != null) {
            final var wl = this.guiHookWindow();
            if (wl != null) {
                this.mainWindow.removeWindowListener(wl);
            }
            this.mainWindow.restoreSaved();
        }
    }

    public final boolean isReadOnly() {
        return this.readOnly;
    }

    public final void exitEditor() {
        // Save changes
        this.saveObject();
        // Hide the editor
        this.hideOutput();
    }

    public boolean usesImporter() {
        return false;
    }

    public abstract JMenu createEditorCommandsMenu();

    public abstract void enableEditorCommands();

    public abstract void disableEditorCommands();

    public abstract void handleCloseWindow();

    protected abstract boolean doesObjectExist();

    protected abstract boolean newObjectOptions();

    protected abstract boolean newObjectCreate();

    protected abstract void editObjectChanged();

    protected abstract void borderPaneHook();

    protected abstract void loadObject();

    protected abstract void saveObject();

    protected abstract void setUpGUIHook(JPanel output);

    protected abstract void reSetUpGUIHook(JPanel output);

    protected abstract WindowListener guiHookWindow();

    public abstract void switchToSubEditor();

    public abstract void switchFromSubEditor();

    public abstract void redrawEditor();

    protected void setUpGUI() {
        this.messageLabel = new JLabel(" ");
        this.mainWindow = MainWindow.mainWindow();
        this.outputPane = new JPanel();
        this.borderPane = new JPanel();
        this.borderPane.setLayout(new BorderLayout());
        this.setUpGUIHook(this.outputPane);
        this.scrollPane = new JScrollPane(this.borderPane);
        this.borderPane.add(this.outputPane, BorderLayout.CENTER);
        this.borderPane.add(this.messageLabel, BorderLayout.NORTH);
        if (this.mainWindow.getWidth() > ImageLoader.MAX_WINDOW_SIZE
                || this.mainWindow.getHeight() > ImageLoader.MAX_WINDOW_SIZE) {
            int pw, ph;
            if (this.mainWindow.getWidth() > ImageLoader.MAX_WINDOW_SIZE) {
                pw = ImageLoader.MAX_WINDOW_SIZE;
            } else {
                pw = this.scrollPane.getWidth();
            }
            if (this.mainWindow.getHeight() > ImageLoader.MAX_WINDOW_SIZE) {
                ph = ImageLoader.MAX_WINDOW_SIZE;
            } else {
                ph = this.scrollPane.getHeight();
            }
            this.scrollPane.setPreferredSize(new Dimension(pw, ph));
        }
    }
}
