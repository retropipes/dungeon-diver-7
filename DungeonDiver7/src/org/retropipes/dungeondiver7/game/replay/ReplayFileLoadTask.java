/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.game.replay;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JProgressBar;

import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.GameString;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;

class ReplayFileLoadTask extends Thread {
	// Fields
	private final String filename;
	private final MainWindow mainWindow;
	private final MainContent loadContent;

	// Constructors
	ReplayFileLoadTask(final String file) {
		JProgressBar loadBar;
		this.filename = file;
		this.setName(Strings.untranslated(Untranslated.REPLAY_LOADER_NAME));
		this.mainWindow = MainWindow.mainWindow();
		loadBar = new JProgressBar();
		loadBar.setIndeterminate(true);
		this.loadContent = this.mainWindow.createContent();
		this.loadContent.add(loadBar);
	}

	@Override
	public void run() {
		this.mainWindow.setAndSave(this.loadContent, Strings.dialog(DialogString.LOADING));
		final var app = DungeonDiver7.getStuffBag();
		app.getGameLogic().setSavedGameFlag(false);
		try (var dungeonFile = new FileInputStream(this.filename)) {
			ReplayFile.loadLPB(dungeonFile);
			dungeonFile.close();
		} catch (final FileNotFoundException fnfe) {
			CommonDialogs.showDialog(Strings.game(GameString.PLAYBACK_LOAD_FAILED));
		} catch (final IOException ie) {
			CommonDialogs.showDialog(ie.getMessage());
		} catch (final Exception ex) {
			DungeonDiver7.logError(ex);
		} finally {
			this.mainWindow.restoreSaved();
		}
	}
}
