/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.editor;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JTextField;

import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loaders.ExternalMusicImporter;
import com.puttysoftware.dungeondiver7.loaders.MusicLoader;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;
import com.puttysoftware.dungeondiver7.utilities.Importer;

public class ExternalMusicEditor extends GenericObjectEditor {
    // Declarations
    ExternalMusic cachedExternalMusic;
    private final EventHandler handler;

    public ExternalMusicEditor() {
	super(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE, LocaleConstants.EDITOR_STRING_MUSIC_EDITOR),
		2, 5, 1, true);
	this.handler = new EventHandler();
    }

    // Methods
    public void setMusicFilename(final String fn) {
	DungeonDiver7.getApplication().getDungeonManager().getDungeon().setMusicFilename(fn);
    }

    @Override
    public boolean usesImporter() {
	return true;
    }

    @Override
    protected boolean newObjectOptions() {
	this.cachedExternalMusic = new ExternalMusic();
	new Thread() {
	    @Override
	    public void run() {
		final Application app = DungeonDiver7.getApplication();
		Importer.showImporter(ExternalMusicEditor.this.getOutputFrame(), app.getMenuManager().getMainMenuBar());
		while (Importer.isImporterVisible()) {
		    // Wait
		    try {
			Thread.sleep(50);
		    } catch (final InterruptedException ie) {
			// Ignore
		    }
		}
		ExternalMusicEditor.this.newObjectCreate();
	    }
	}.start();
	return false;
    }

    @Override
    protected boolean newObjectCreate() {
	final File file = ExternalMusicImporter.getDestinationFile();
	if (file != null) {
	    this.cachedExternalMusic.setName(file.getName());
	    this.cachedExternalMusic.setPath(file.getParent() + File.separator);
	    this.saveObject();
	    MusicLoader.saveExternalMusic();
	    file.deleteOnExit();
	    DungeonDiver7.getApplication().getDungeonManager().setDirty(true);
	}
	return false;
    }

    @Override
    protected void loadObject() {
	this.cachedExternalMusic = MusicLoader.getExternalMusic();
    }

    @Override
    protected void saveObject() {
	MusicLoader.setExternalMusic(this.cachedExternalMusic);
    }

    @Override
    protected boolean doesObjectExist() {
	return this.cachedExternalMusic != null;
    }

    @Override
    protected void handleButtonClick(final String cmd, final int num) {
	if (cmd.equals("pl")) {
	    // Play the music
	    if (this.cachedExternalMusic == null) {
		this.loadObject();
	    }
	    if (this.cachedExternalMusic != null) {
		MusicLoader.loadPlayMusic(this.cachedExternalMusic.getName());
	    }
	} else if (cmd.equals("st")) {
	    // Stop the music
	    MusicLoader.stopMusic();
	} else if (cmd.equals("md")) {
	    // Set new music
	    this.create();
	}
    }

    @Override
    protected void guiNameLabelProperties(final JLabel nameLbl, final int num) {
	// Do nothing
    }

    @Override
    protected boolean guiEntryType(final int num) {
	return GenericObjectEditor.ENTRY_TYPE_TEXT;
    }

    @Override
    protected void guiEntryFieldProperties(final JTextField entry, final int num) {
	if (entry != null) {
	    entry.setEnabled(false);
	}
    }

    @Override
    protected String[] guiEntryListItems(final int num) {
	return null;
    }

    @Override
    protected void guiEntryListProperties(final JComboBox<String> list, final int num) {
	// Do nothing
    }

    @Override
    protected void guiActionButtonProperties(final JButton actBtn, final int row, final int col) {
	if (actBtn != null) {
	    if (col == 0) {
		actBtn.setText(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_MUSIC_PLAY));
	    } else if (col == 1) {
		actBtn.setText(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_MUSIC_STOP));
	    } else if (col == 2) {
		actBtn.setText(LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE,
			LocaleConstants.EDITOR_STRING_MUSIC_MODIFY));
	    }
	}
    }

    @Override
    protected String guiActionButtonActionCommand(final int row, final int col) {
	if (col == 0) {
	    return "pl" + row;
	} else if (col == 1) {
	    return "st" + row;
	} else if (col == 2) {
	    return "md" + row;
	} else {
	    // Invalid
	    return null;
	}
    }

    @Override
    public JMenu createEditorCommandsMenu() {
	return null;
    }

    @Override
    public void disableEditorCommands() {
	// Do nothing
    }

    @Override
    public void enableEditorCommands() {
	// Do nothing
    }

    @Override
    public void handleCloseWindow() {
	this.exitEditor();
	DungeonDiver7.getApplication().getEditor().showOutput();
    }

    @Override
    protected void autoStoreEntryFieldValue(final JTextField entry, final int num) {
	// Do nothing
    }

    @Override
    protected void autoStoreEntryListValue(final JComboBox<String> list, final int num) {
	// Do nothing
    }

    private class EventHandler implements WindowListener {
	// Handle menus
	public EventHandler() {
	    // Do nothing
	}

	@Override
	public void windowActivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowClosed(final WindowEvent we) {
	    MusicLoader.stopMusic();
	}

	@Override
	public void windowClosing(final WindowEvent we) {
	    ExternalMusicEditor.this.handleCloseWindow();
	}

	@Override
	public void windowDeactivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowDeiconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowIconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowOpened(final WindowEvent we) {
	    // Do nothing
	}
    }

    @Override
    protected WindowListener guiHookWindow() {
	return this.handler;
    }
}
