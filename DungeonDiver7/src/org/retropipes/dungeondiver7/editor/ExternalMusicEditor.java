/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.editor;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JTextField;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.asset.ExternalMusic;
import org.retropipes.dungeondiver7.asset.ExternalMusicImporter;
import org.retropipes.dungeondiver7.asset.ExternalMusicLoader;
import org.retropipes.dungeondiver7.locale.EditorString;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.utility.Importer;

public class ExternalMusicEditor extends GenericObjectEditor {
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
			ExternalMusicLoader.stopExternalMusic();
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

	// Declarations
	ExternalMusic cachedExternalMusic;
	private final EventHandler handler;

	public ExternalMusicEditor() {
		super(Strings.editor(EditorString.MUSIC_EDITOR), 2, 5, 1, true);
		this.handler = new EventHandler();
	}

	@Override
	protected void autoStoreEntryFieldValue(final JTextField entry, final int num) {
		// Do nothing
	}

	@Override
	protected void autoStoreEntryListValue(final JComboBox<String> list, final int num) {
		// Do nothing
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
	protected boolean doesObjectExist() {
		return this.cachedExternalMusic != null;
	}

	@Override
	public void enableEditorCommands() {
		// Do nothing
	}

	@Override
	protected String guiActionButtonActionCommand(final int row, final int col) {
		return switch (col) {
		case 0 -> "pl" + row;
		case 1 -> "st" + row;
		case 2 -> "md" + row;
		default -> /* Invalid */ null;
		};
	}

	@Override
	protected void guiActionButtonProperties(final JButton actBtn, final int row, final int col) {
		if (actBtn != null) {
			switch (col) {
			case 0:
				actBtn.setText(Strings.editor(EditorString.MUSIC_PLAY));
				break;
			case 1:
				actBtn.setText(Strings.editor(EditorString.MUSIC_STOP));
				break;
			case 2:
				actBtn.setText(Strings.editor(EditorString.MUSIC_MODIFY));
				break;
			default:
				break;
			}
		}
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
	protected boolean guiEntryType(final int num) {
		return GenericObjectEditor.ENTRY_TYPE_TEXT;
	}

	@Override
	protected WindowListener guiHookWindow() {
		return this.handler;
	}

	@Override
	protected void guiNameLabelProperties(final JLabel nameLbl, final int num) {
		// Do nothing
	}

	@Override
	protected void handleButtonClick(final String cmd, final int num) {
		if (cmd.equals("pl")) {
			// Play the music
			if (this.cachedExternalMusic == null) {
				this.loadObject();
			}
			if (this.cachedExternalMusic != null) {
				ExternalMusicLoader.loadPlayExternalMusic(this.cachedExternalMusic.getName());
			}
		} else if (cmd.equals("st")) {
			// Stop the music
			ExternalMusicLoader.stopExternalMusic();
		} else if (cmd.equals("md")) {
			// Set new music
			this.create();
		}
	}

	@Override
	public void handleCloseWindow() {
		this.exitEditor();
		DungeonDiver7.getStuffBag().getEditor().showOutput();
	}

	@Override
	protected void loadObject() {
		this.cachedExternalMusic = ExternalMusicLoader.getExternalMusic();
	}

	@Override
	protected boolean newObjectCreate() {
		final var file = ExternalMusicImporter.getDestinationFile();
		if (file != null) {
			this.cachedExternalMusic.setName(file.getName());
			this.cachedExternalMusic.setPath(file.getParent() + File.separator);
			this.saveObject();
			ExternalMusicLoader.saveExternalMusic();
			file.deleteOnExit();
			DungeonDiver7.getStuffBag().getDungeonManager().setDirty(true);
		}
		return false;
	}

	@Override
	protected boolean newObjectOptions() {
		this.cachedExternalMusic = new ExternalMusic();
		new Thread() {
			@Override
			public void run() {
				Importer.showImporter();
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
	protected void saveObject() {
		ExternalMusicLoader.setExternalMusic(this.cachedExternalMusic);
	}

	public void setMusicFilename(final String fn) {
		DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().setMusicFilename(fn);
	}

	@Override
	public boolean usesImporter() {
		return true;
	}
}
