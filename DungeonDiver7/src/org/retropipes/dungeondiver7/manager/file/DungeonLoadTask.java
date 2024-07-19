/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.manager.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipException;

import javax.swing.JProgressBar;

import org.retropipes.diane.fileio.utility.ZipUtilities;
import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.AbstractDungeon;
import org.retropipes.dungeondiver7.loader.MusicLoader;
import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.ErrorString;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;
import org.retropipes.dungeondiver7.manager.dungeon.DungeonManager;
import org.retropipes.dungeondiver7.utility.InvalidDungeonException;

public class DungeonLoadTask extends Thread {
	// Fields
	private final String filename;
	private final MainContent loadContent;
	private final boolean isSavedGame;
	private final MainWindow mainWindow;
	private final boolean dungeonProtected;

	// Constructors
	public DungeonLoadTask(final String file, final boolean saved, final boolean protect) {
		JProgressBar loadBar;
		this.filename = file;
		this.isSavedGame = saved;
		this.dungeonProtected = protect;
		this.setName(Strings.untranslated(Untranslated.FILE_LOADER_NEW_NAME));
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
		if (this.isSavedGame) {
			app.getGameLogic().setSavedGameFlag(true);
		} else {
			app.getGameLogic().setSavedGameFlag(false);
		}
		try {
			final var dungeonFile = new File(this.filename);
			final var tempLock = new File(AbstractDungeon.getDungeonTempFolder() + "lock.tmp");
			var gameDungeon = DungeonManager.createDungeon();
			if (this.dungeonProtected) {
				// Attempt to unprotect the file
				DungeonProtectionWrapper.unprotect(dungeonFile, tempLock);
				try {
					ZipUtilities.unzipDirectory(tempLock, new File(gameDungeon.getBasePath()));
					app.getDungeonManager().setDungeonProtected(true);
				} catch (final ZipException ze) {
					CommonDialogs.showErrorDialog(Strings.error(ErrorString.BAD_PROTECTION_KEY),
							Strings.dialog(DialogString.PROTECTION_TITLE));
					app.getDungeonManager().handleDeferredSuccess(false, false, null);
					return;
				} finally {
					tempLock.delete();
				}
			} else {
				ZipUtilities.unzipDirectory(dungeonFile, new File(gameDungeon.getBasePath()));
				app.getDungeonManager().setDungeonProtected(false);
			}
			// Set prefix handler
			gameDungeon.setPrefixHandler(new DungeonFilePrefixHandler());
			// Set suffix handler
			if (this.isSavedGame) {
				gameDungeon.setSuffixHandler(new DungeonFileSuffixHandler());
			} else {
				gameDungeon.setSuffixHandler(null);
			}
			gameDungeon = gameDungeon.readDungeon();
			if (gameDungeon == null) {
				throw new InvalidDungeonException(Strings.error(ErrorString.UNKNOWN_OBJECT));
			}
			app.getDungeonManager().setDungeon(gameDungeon);
			final var playerExists = gameDungeon.doesPlayerExist(0);
			if (playerExists) {
				app.getGameLogic().getPlayerManager().resetPlayerLocation();
			}
			if (!this.isSavedGame) {
				gameDungeon.save();
			}
			// Final cleanup
			final var lum = app.getDungeonManager().getLastUsedDungeon();
			final var lug = app.getDungeonManager().getLastUsedGame();
			app.getDungeonManager().clearLastUsedFilenames();
			if (this.isSavedGame) {
				app.getDungeonManager().setLastUsedGame(lug);
			} else {
				app.getDungeonManager().setLastUsedDungeon(lum);
			}
			app.getEditor().dungeonChanged();
			MusicLoader.dungeonChanged();
			if (this.isSavedGame) {
				CommonDialogs.showDialog(Strings.dialog(DialogString.GAME_LOADING_SUCCESS));
			} else {
				CommonDialogs.showDialog(Strings.dialog(DialogString.DUNGEON_LOADING_SUCCESS));
			}
			app.getDungeonManager().handleDeferredSuccess(true, false, null);
		} catch (final FileNotFoundException fnfe) {
			if (this.isSavedGame) {
				CommonDialogs.showDialog(Strings.dialog(DialogString.GAME_LOADING_FAILED));
			} else {
				CommonDialogs.showDialog(Strings.dialog(DialogString.DUNGEON_LOADING_FAILED));
			}
			app.getDungeonManager().handleDeferredSuccess(false, false, null);
		} catch (final ProtectionCancelException pce) {
			app.getDungeonManager().handleDeferredSuccess(false, false, null);
		} catch (final IOException ie) {
			if (this.isSavedGame) {
				CommonDialogs.showDialog(Strings.dialog(DialogString.GAME_LOADING_FAILED));
			} else {
				CommonDialogs.showDialog(Strings.dialog(DialogString.DUNGEON_LOADING_FAILED));
			}
			DungeonDiver7.logWarningDirectly(ie);
			app.getDungeonManager().handleDeferredSuccess(false, false, null);
		} catch (final Exception ex) {
			DungeonDiver7.logError(ex);
		} finally {
			this.mainWindow.restoreSaved();
		}
	}
}
