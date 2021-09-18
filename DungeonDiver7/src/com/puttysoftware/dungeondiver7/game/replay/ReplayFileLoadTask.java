/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game.replay;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loaders.LogoLoader;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;

class ReplayFileLoadTask extends Thread {
	// Fields
	private final String filename;
	private final JFrame loadFrame;

	// Constructors
	ReplayFileLoadTask(final String file) {
		JProgressBar loadBar;
		this.filename = file;
		this.setName(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
				LocaleConstants.NOTL_STRING_PLAYBACK_LOADER_NAME));
		this.loadFrame = new JFrame(
				LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE, LocaleConstants.DIALOG_STRING_LOADING));
		this.loadFrame.setIconImage(LogoLoader.getIconLogo());
		loadBar = new JProgressBar();
		loadBar.setIndeterminate(true);
		this.loadFrame.getContentPane().add(loadBar);
		this.loadFrame.setResizable(false);
		this.loadFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.loadFrame.pack();
	}

	// Methods
	@Override
	public void run() {
		this.loadFrame.setVisible(true);
		final Application app = DungeonDiver7.getApplication();
		app.getGameManager().setSavedGameFlag(false);
		try (FileInputStream dungeonFile = new FileInputStream(this.filename)) {
			ReplayFile.loadLPB(dungeonFile);
			dungeonFile.close();
		} catch (final FileNotFoundException fnfe) {
			CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
					LocaleConstants.GAME_STRING_PLAYBACK_LOAD_FAILED));
		} catch (final IOException ie) {
			CommonDialogs.showDialog(ie.getMessage());
		} catch (final Exception ex) {
			DungeonDiver7.getErrorLogger().logError(ex);
		} finally {
			this.loadFrame.setVisible(false);
		}
	}
}
