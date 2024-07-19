/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowListener;

import javax.swing.JLabel;
import javax.swing.JMenu;

import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainContentFactory;
import org.retropipes.diane.gui.MainScrollingContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.diane.integration.Integration;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.asset.ImageLoader;

public abstract class GenericEditor {
	// Fields
	private final String source;
	private MainWindow mainWindow;
	private MainContent borderPane;
	private MainContent outputPane;
	private JLabel messageLabel;
	private MainScrollingContent scrollPane;
	private boolean objectChanged;
	private boolean readOnly;

	protected GenericEditor(final String newSource) {
		this.source = newSource;
		this.objectChanged = true;
	}

	protected abstract void borderPaneHook();

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

	public abstract JMenu createEditorCommandsMenu();

	public abstract void disableEditorCommands();

	protected abstract boolean doesObjectExist();

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

	protected abstract void editObjectChanged();

	public abstract void enableEditorCommands();

	public final void exitEditor() {
		// Save changes
		this.saveObject();
		// Hide the editor
		this.hideOutput();
	}

	// Methods from EditorProperties
	public final String getEditorSource() {
		return this.source;
	}

	protected abstract WindowListener guiHookWindow();

	public abstract void handleCloseWindow();

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

	protected abstract void loadObject();

	protected abstract boolean newObjectCreate();

	protected abstract boolean newObjectOptions();

	public abstract void redrawEditor();

	protected abstract void reSetUpGUIHook(MainContent output);

	protected abstract void saveObject();

	protected void setUpGUI() {
		this.messageLabel = new JLabel(" ");
		this.mainWindow = MainWindow.mainWindow();
		this.outputPane = this.mainWindow.createContent();
		this.borderPane = this.mainWindow.createContent();
		this.borderPane.setLayout(new BorderLayout());
		this.setUpGUIHook(this.outputPane);
		this.scrollPane = MainContentFactory.mainScrollingContent(this.borderPane);
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

	protected abstract void setUpGUIHook(MainContent output);

	public final void showOutput() {
		final var app = DungeonDiver7.getStuffBag();
		Integration.integrate().setDefaultMenuBar(app.getMenuManager().getMainMenuBar());
		this.mainWindow.setAndSave(this.scrollPane, this.getEditorSource());
		final var wl = this.guiHookWindow();
		if (wl != null) {
			this.mainWindow.addWindowListener(wl);
		}
	}

	public abstract void switchFromSubEditor();

	public abstract void switchToSubEditor();

	public boolean usesImporter() {
		return false;
	}
}
