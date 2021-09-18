/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.Application;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeon;
import com.puttysoftware.dungeondiver7.dungeon.v4.V4LevelLoadTask;
import com.puttysoftware.dungeondiver7.loaders.MusicLoader;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utilities.CleanupTask;
import com.puttysoftware.dungeondiver7.utilities.Extension;
import com.puttysoftware.fileutils.FilenameChecker;

public class DungeonManager {
	// Fields
	private AbstractDungeon gameDungeon;
	private boolean loaded, isDirty;
	private String scoresFileName;
	private String lastUsedDungeonFile;
	private String lastUsedGameFile;
	private boolean dungeonProtected;

	// Constructors
	public DungeonManager() {
		this.loaded = false;
		this.isDirty = false;
		this.lastUsedDungeonFile = LocaleConstants.COMMON_STRING_EMPTY;
		this.lastUsedGameFile = LocaleConstants.COMMON_STRING_EMPTY;
		this.scoresFileName = LocaleConstants.COMMON_STRING_EMPTY;
	}

	// Methods
	public static AbstractDungeon createDungeon() throws IOException {
		return new CurrentDungeon();
	}

	public AbstractDungeon getDungeon() {
		return this.gameDungeon;
	}

	public void setDungeon(final AbstractDungeon newDungeon) {
		this.gameDungeon = newDungeon;
	}

	public boolean isDungeonProtected() {
		return this.dungeonProtected;
	}

	public void setDungeonProtected(final boolean value) {
		this.dungeonProtected = value;
	}

	public void handleDeferredSuccess(final boolean value) {
		if (value) {
			this.setLoaded(true);
		}
		this.setDirty(false);
		DungeonDiver7.getApplication().getEditor().dungeonChanged();
		MusicLoader.dungeonChanged();
		DungeonDiver7.getApplication().getMenuManager().checkFlags();
	}

	public static int showSaveDialog() {
		String type, source;
		final Application app = DungeonDiver7.getApplication();
		final int mode = app.getMode();
		if (mode == Application.STATUS_EDITOR) {
			type = LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
					LocaleConstants.DIALOG_STRING_PROMPT_SAVE_ARENA);
			source = LocaleLoader.loadString(LocaleConstants.EDITOR_STRINGS_FILE, LocaleConstants.EDITOR_STRING_EDITOR);
		} else if (mode == Application.STATUS_GAME) {
			type = LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
					LocaleConstants.DIALOG_STRING_PROMPT_SAVE_GAME);
			source = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
					LocaleConstants.NOTL_STRING_PROGRAM_NAME);
		} else {
			// Not in the game or editor, so abort
			return JOptionPane.NO_OPTION;
		}
		return CommonDialogs.showYNCConfirmDialog(type, source);
	}

	public boolean getLoaded() {
		return this.loaded;
	}

	public void setLoaded(final boolean status) {
		final Application app = DungeonDiver7.getApplication();
		this.loaded = status;
		app.getMenuManager().checkFlags();
	}

	public boolean getDirty() {
		return this.isDirty;
	}

	public void setDirty(final boolean newDirty) {
		final Application app = DungeonDiver7.getApplication();
		this.isDirty = newDirty;
		final JFrame frame = app.getOutputFrame();
		if (frame != null) {
			frame.getRootPane().putClientProperty(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
					LocaleConstants.NOTL_STRING_WINDOW_MODIFIED), Boolean.valueOf(newDirty));
		}
		app.getMenuManager().checkFlags();
	}

	public void clearLastUsedFilenames() {
		this.lastUsedDungeonFile = LocaleConstants.COMMON_STRING_EMPTY;
		this.lastUsedGameFile = LocaleConstants.COMMON_STRING_EMPTY;
	}

	public String getLastUsedDungeon() {
		return this.lastUsedDungeonFile;
	}

	public String getLastUsedGame() {
		return this.lastUsedGameFile;
	}

	public void setLastUsedDungeon(final String newFile) {
		this.lastUsedDungeonFile = newFile;
	}

	public void setLastUsedGame(final String newFile) {
		this.lastUsedGameFile = newFile;
	}

	public String getScoresFileName() {
		return this.scoresFileName;
	}

	public void setScoresFileName(final String filename) {
		this.scoresFileName = filename;
	}

	public boolean loadDungeon() {
		return this.loadDungeonImpl(PrefsManager.getLastDirOpen());
	}

	public boolean loadDungeonDefault() {
		try {
			return this.loadDungeonImpl(
					DungeonManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()
							+ File.separator + "Common" + File.separator + "Levels");
		} catch (final URISyntaxException e) {
			return this.loadDungeon();
		}
	}

	private boolean loadDungeonImpl(final String initialDirectory) {
		final Application app = DungeonDiver7.getApplication();
		int status = 0;
		boolean saved = true;
		String filename, extension, file, dir;
		final FileDialog fd = new FileDialog(app.getOutputFrame(),
				LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE, LocaleConstants.DIALOG_STRING_LOAD),
				FileDialog.LOAD);
		fd.setDirectory(initialDirectory);
		if (this.getDirty()) {
			status = DungeonManager.showSaveDialog();
			if (status == JOptionPane.YES_OPTION) {
				saved = this.saveDungeon(this.isDungeonProtected());
			} else if (status == JOptionPane.CANCEL_OPTION) {
				saved = false;
			} else {
				this.setDirty(false);
			}
		}
		if (saved) {
			fd.setVisible(true);
			file = fd.getFile();
			dir = fd.getDirectory();
			if (file != null && dir != null) {
				PrefsManager.setLastDirOpen(dir);
				filename = dir + file;
				extension = DungeonManager.getExtension(filename);
				if (extension.equals(Extension.getDungeonExtension())) {
					this.lastUsedDungeonFile = filename;
					this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
					DungeonManager.loadFile(filename, false, false);
				} else if (extension.equals(Extension.getProtectedDungeonExtension())) {
					this.lastUsedDungeonFile = filename;
					this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
					DungeonManager.loadFile(filename, false, true);
				} else if (extension.equals(Extension.getGameExtension())) {
					this.lastUsedGameFile = filename;
					DungeonManager.loadFile(filename, true, false);
				} else if (extension.equals(Extension.getOldLevelExtension())) {
					this.lastUsedDungeonFile = filename;
					this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
					final V4LevelLoadTask ollt = new V4LevelLoadTask(filename);
					ollt.start();
				} else {
					CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
							LocaleConstants.DIALOG_STRING_NON_ARENA_FILE));
				}
			} else {
				// User cancelled
				if (this.loaded) {
					return true;
				}
			}
		}
		return false;
	}

	private static void loadFile(final String filename, final boolean isSavedGame, final boolean protect) {
		if (!FilenameChecker
				.isFilenameOK(DungeonManager.getNameWithoutExtension(DungeonManager.getFileNameOnly(filename)))) {
			CommonDialogs.showErrorDialog(
					LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
							LocaleConstants.DIALOG_STRING_ILLEGAL_CHARACTERS),
					LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE, LocaleConstants.DIALOG_STRING_LOAD));
		} else {
			// Run cleanup task
			CleanupTask.cleanUp();
			// Load file
			final DungeonLoadTask xlt = new DungeonLoadTask(filename, isSavedGame, protect);
			xlt.start();
		}
	}

	public boolean saveDungeon(final boolean protect) {
		final Application app = DungeonDiver7.getApplication();
		if (app.getMode() == Application.STATUS_GAME) {
			if (this.lastUsedGameFile != null && !this.lastUsedGameFile.equals(LocaleConstants.COMMON_STRING_EMPTY)) {
				final String extension = DungeonManager.getExtension(this.lastUsedGameFile);
				if (extension != null) {
					if (!extension.equals(Extension.getGameExtension())) {
						this.lastUsedGameFile = DungeonManager.getNameWithoutExtension(this.lastUsedGameFile)
								+ Extension.getGameExtensionWithPeriod();
					}
				} else {
					this.lastUsedGameFile += Extension.getGameExtensionWithPeriod();
				}
				DungeonManager.saveFile(this.lastUsedGameFile, true, false);
			} else {
				return this.saveDungeonAs(protect);
			}
		} else {
			if (protect) {
				if (this.lastUsedDungeonFile != null
						&& !this.lastUsedDungeonFile.equals(LocaleConstants.COMMON_STRING_EMPTY)) {
					final String extension = DungeonManager.getExtension(this.lastUsedDungeonFile);
					if (extension != null) {
						if (!extension.equals(Extension.getProtectedDungeonExtension())) {
							this.lastUsedDungeonFile = DungeonManager.getNameWithoutExtension(this.lastUsedDungeonFile)
									+ Extension.getProtectedDungeonExtensionWithPeriod();
						}
					} else {
						this.lastUsedDungeonFile += Extension.getProtectedDungeonExtensionWithPeriod();
					}
					DungeonManager.saveFile(this.lastUsedDungeonFile, false, protect);
				} else {
					return this.saveDungeonAs(protect);
				}
			} else {
				if (this.lastUsedDungeonFile != null
						&& !this.lastUsedDungeonFile.equals(LocaleConstants.COMMON_STRING_EMPTY)) {
					final String extension = DungeonManager.getExtension(this.lastUsedDungeonFile);
					if (extension != null) {
						if (!extension.equals(Extension.getDungeonExtension())) {
							this.lastUsedDungeonFile = DungeonManager.getNameWithoutExtension(this.lastUsedDungeonFile)
									+ Extension.getDungeonExtensionWithPeriod();
						}
					} else {
						this.lastUsedDungeonFile += Extension.getDungeonExtensionWithPeriod();
					}
					DungeonManager.saveFile(this.lastUsedDungeonFile, false, protect);
				} else {
					return this.saveDungeonAs(protect);
				}
			}
		}
		return false;
	}

	public boolean saveDungeonAs(final boolean protect) {
		final Application app = DungeonDiver7.getApplication();
		String filename = LocaleConstants.COMMON_STRING_EMPTY;
		String fileOnly = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
				LocaleConstants.NOTL_STRING_DOUBLE_BACKSLASH);
		String extension, file, dir;
		final String lastSave = PrefsManager.getLastDirSave();
		final FileDialog fd = new FileDialog(app.getOutputFrame(),
				LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE, LocaleConstants.DIALOG_STRING_SAVE),
				FileDialog.SAVE);
		fd.setDirectory(lastSave);
		while (!FilenameChecker.isFilenameOK(fileOnly)) {
			fd.setVisible(true);
			file = fd.getFile();
			dir = fd.getDirectory();
			if (file != null && dir != null) {
				extension = DungeonManager.getExtension(file);
				filename = dir + file;
				fileOnly = filename.substring(dir.length() + 1);
				if (!FilenameChecker.isFilenameOK(fileOnly)) {
					CommonDialogs.showErrorDialog(
							LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
									LocaleConstants.DIALOG_STRING_ILLEGAL_CHARACTERS),
							LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
									LocaleConstants.DIALOG_STRING_SAVE));
				} else {
					PrefsManager.setLastDirSave(dir);
					if (app.getMode() == Application.STATUS_GAME) {
						if (extension != null) {
							if (!extension.equals(Extension.getGameExtension())) {
								filename = DungeonManager.getNameWithoutExtension(file)
										+ Extension.getGameExtensionWithPeriod();
							}
						} else {
							filename += Extension.getGameExtensionWithPeriod();
						}
						this.lastUsedGameFile = filename;
						DungeonManager.saveFile(filename, true, false);
					} else {
						if (protect) {
							if (extension != null) {
								if (!extension.equals(Extension.getProtectedDungeonExtension())) {
									filename = DungeonManager.getNameWithoutExtension(file)
											+ Extension.getProtectedDungeonExtensionWithPeriod();
								}
							} else {
								filename += Extension.getProtectedDungeonExtensionWithPeriod();
							}
							this.lastUsedDungeonFile = filename;
							this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
							DungeonManager.saveFile(filename, false, protect);
						} else {
							if (extension != null) {
								if (!extension.equals(Extension.getDungeonExtension())) {
									filename = DungeonManager.getNameWithoutExtension(file)
											+ Extension.getDungeonExtensionWithPeriod();
								}
							} else {
								filename += Extension.getDungeonExtensionWithPeriod();
							}
							this.lastUsedDungeonFile = filename;
							this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
							DungeonManager.saveFile(filename, false, protect);
						}
					}
				}
			} else {
				break;
			}
		}
		return false;
	}

	private static void saveFile(final String filename, final boolean isSavedGame, final boolean protect) {
		if (isSavedGame) {
			DungeonDiver7.getApplication().showMessage(LocaleLoader.loadString(LocaleConstants.MESSAGE_STRINGS_FILE,
					LocaleConstants.MESSAGE_STRING_SAVING_GAME));
		} else {
			DungeonDiver7.getApplication().showMessage(LocaleLoader.loadString(LocaleConstants.MESSAGE_STRINGS_FILE,
					LocaleConstants.MESSAGE_STRING_SAVING_ARENA));
		}
		final DungeonSaveTask xst = new DungeonSaveTask(filename, isSavedGame, protect);
		xst.start();
	}

	private static String getExtension(final String s) {
		String ext = null;
		final int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	private static String getNameWithoutExtension(final String s) {
		String ext = null;
		final int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(0, i);
		} else {
			ext = s;
		}
		return ext;
	}

	private static String getFileNameOnly(final String s) {
		String fno = null;
		final int i = s.lastIndexOf(File.separatorChar);
		if (i > 0 && i < s.length() - 1) {
			fno = s.substring(i + 1);
		} else {
			fno = s;
		}
		return fno;
	}
}
