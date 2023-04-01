/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.manager.dungeon;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeon;
import com.puttysoftware.dungeondiver7.dungeon.ltv4.LaserTankV4LoadTask;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.EditorString;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.dungeondiver7.manager.file.DungeonLoadTask;
import com.puttysoftware.dungeondiver7.manager.file.DungeonSaveTask;
import com.puttysoftware.dungeondiver7.manager.file.GameFinder;
import com.puttysoftware.dungeondiver7.manager.file.GameLoadTask;
import com.puttysoftware.dungeondiver7.manager.file.GameSaveTask;
import com.puttysoftware.dungeondiver7.prefs.Prefs;
import com.puttysoftware.dungeondiver7.utility.CleanupTask;
import com.puttysoftware.diane.fileio.utility.FilenameChecker;

public final class DungeonManager {
	// Fields
	private AbstractDungeon gameDungeon;
	private boolean loaded, isDirty;
	private String scoresFileName;
	private String lastUsedDungeonFile;
	private String lastUsedGameFile;
	private boolean dungeonProtected;
	private static final String MAC_PREFIX = "HOME";
	private static final String WIN_PREFIX = "APPDATA";
	private static final String UNIX_PREFIX = "HOME";
	private static final String MAC_DIR = "/Library/Application Support/Ignition Igloo Games/Chrystalz/Games/";
	private static final String WIN_DIR = "\\Ignition Igloo Games\\Chrystalz\\Games\\";
	private static final String UNIX_DIR = "/.ignitionigloogames/chrystalz/games/";

	// Constructors
	public DungeonManager() {
		this.loaded = false;
		this.isDirty = false;
		this.gameDungeon = null;
		this.lastUsedDungeonFile = Strings.EMPTY;
		this.lastUsedGameFile = Strings.EMPTY;
		this.scoresFileName = Strings.EMPTY;
	}

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

	public void handleDeferredSuccess(final boolean value, final boolean versionError, final File triedToLoad) {
		if (value) {
			this.setLoaded(true);
		}
		if (versionError) {
			triedToLoad.delete();
		}
		this.setDirty(false);
		DungeonDiver7.getStuffBag().getGameLogic().stateChanged();
		DungeonDiver7.getStuffBag().getMenuManager().checkFlags();
	}

	public static int showSaveDialog() {
		String type, source;
		final var app = DungeonDiver7.getStuffBag();
		final var mode = app.getMode();
		if (mode == StuffBag.STATUS_EDITOR) {
			type = Strings.dialog(DialogString.PROMPT_SAVE_ARENA);
			source = Strings.editor(EditorString.EDITOR);
		} else if (mode == StuffBag.STATUS_GAME) {
			type = Strings.dialog(DialogString.PROMPT_SAVE_GAME);
			source = Strings.untranslated(Untranslated.PROGRAM_NAME);
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
		final var app = DungeonDiver7.getStuffBag();
		this.loaded = status;
		app.getMenuManager().checkFlags();
	}

	public boolean getDirty() {
		return this.isDirty;
	}

	public void setDirty(final boolean newDirty) {
		final var app = DungeonDiver7.getStuffBag();
		this.isDirty = newDirty;
		final var frame = MainWindow.mainWindow();
		if (frame != null) {
			frame.setDirty(newDirty);
		}
		app.getMenuManager().checkFlags();
	}

	public void clearLastUsedFilenames() {
		this.lastUsedDungeonFile = Strings.EMPTY;
		this.lastUsedGameFile = Strings.EMPTY;
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

	public boolean loadGame() {
		var status = 0;
		var saved = true;
		String filename;
		final var gf = new GameFinder();
		if (this.getDirty()) {
			status = DungeonManager.showSaveDialog();
			if (status == JOptionPane.YES_OPTION) {
				saved = DungeonManager.saveGame();
			} else if (status == JOptionPane.CANCEL_OPTION) {
				saved = false;
			} else {
				this.setDirty(false);
			}
		}
		if (saved) {
			final var gameDir = DungeonManager.getGameDirectory();
			final var rawChoices = new File(gameDir).list(gf);
			if (rawChoices != null && rawChoices.length > 0) {
				final var choices = new String[rawChoices.length];
				// Strip extension
				for (var x = 0; x < choices.length; x++) {
					choices[x] = DungeonManager.getNameWithoutExtension(rawChoices[x]);
				}
				final var returnVal = CommonDialogs.showInputDialog("Select a Game", "Load Game", choices, choices[0]);
				if (returnVal != null) {
					var index = -1;
					for (var x = 0; x < choices.length; x++) {
						if (returnVal.equals(choices[x])) {
							index = x;
							break;
						}
					}
					if (index != -1) {
						final var file = new File(gameDir + rawChoices[index]);
						filename = file.getAbsolutePath();
						DungeonManager.loadGameFile(filename);
					} else // Result not found
					if (this.loaded) {
						return true;
					}
				} else // User cancelled
				if (this.loaded) {
					return true;
				}
			} else {
				CommonDialogs.showErrorDialog("No Games Found!", "Load Game");
				if (this.loaded) {
					return true;
				}
			}
		}
		return false;
	}

	private static void loadGameFile(final String filename) {
		if (!FilenameChecker
				.isFilenameOK(DungeonManager.getNameWithoutExtension(DungeonManager.getFileNameOnly(filename)))) {
			CommonDialogs.showErrorDialog("The file you selected contains illegal characters in its\n"
					+ "name. These characters are not allowed: /?<>\\:|\"\n"
					+ "Files named con, nul, or prn are illegal, as are files\n"
					+ "named com1 through com9 and lpt1 through lpt9.", "Load");
		} else {
			final var llt = new GameLoadTask(filename);
			llt.start();
		}
	}

	public static boolean saveGame() {
		var filename = "";
		String extension;
		var returnVal = "\\";
		while (!FilenameChecker.isFilenameOK(returnVal)) {
			returnVal = CommonDialogs.showTextInputDialog("Name?", "Save Game");
			if (returnVal == null) {
				break;
			}
			extension = Strings.fileExtension(FileExtension.SUSPEND);
			final var file = new File(DungeonManager.getGameDirectory() + returnVal + extension);
			filename = file.getAbsolutePath();
			if (!FilenameChecker.isFilenameOK(returnVal)) {
				CommonDialogs.showErrorDialog("The file name you entered contains illegal characters.\n"
						+ "These characters are not allowed: /?<>\\:|\"\n"
						+ "Files named con, nul, or prn are illegal, as are files\n"
						+ "named com1 through com9 and lpt1 through lpt9.", "Save Game");
			} else {
				// Make sure folder exists
				if (!file.getParentFile().exists()) {
					final var okay = file.getParentFile().mkdirs();
					if (!okay) {
						DungeonDiver7.logError(new IOException("Cannot create game folder!"));
					}
				}
				DungeonManager.saveGameFile(filename);
			}
		}
		return false;
	}

	private static void saveGameFile(final String filename) {
		final var sg = "Saved Game";
		DungeonDiver7.getStuffBag().showMessage("Saving " + sg + " file...");
		final var lst = new GameSaveTask(filename);
		lst.start();
	}

	public boolean loadDungeon() {
		return this.loadDungeonImpl(Prefs.getLastDirOpen());
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
		var status = 0;
		var saved = true;
		String filename, extension, file, dir;
		final var fd = new FileDialog((JFrame) null, Strings.dialog(DialogString.LOAD), FileDialog.LOAD);
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
				Prefs.setLastDirOpen(dir);
				filename = dir + file;
				extension = DungeonManager.getExtension(filename);
				if (extension.equals(Strings.fileExtension(FileExtension.DUNGEON))) {
					this.lastUsedDungeonFile = filename;
					this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
					DungeonManager.loadDungeonFile(filename, false, false);
				} else if (extension.equals(Strings.fileExtension(FileExtension.PROTECTED_DUNGEON))) {
					this.lastUsedDungeonFile = filename;
					this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
					DungeonManager.loadDungeonFile(filename, false, true);
				} else if (extension.equals(Strings.fileExtension(FileExtension.SUSPEND))) {
					this.lastUsedGameFile = filename;
					DungeonManager.loadDungeonFile(filename, true, false);
				} else if (extension.equals(Strings.fileExtension(FileExtension.OLD_LEVEL))) {
					this.lastUsedDungeonFile = filename;
					this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
					final var ollt = new LaserTankV4LoadTask(filename);
					ollt.start();
				} else {
					CommonDialogs.showDialog(Strings.dialog(DialogString.NON_DUNGEON_FILE));
				}
			} else // User cancelled
			if (this.loaded) {
				return true;
			}
		}
		return false;
	}

	private static void loadDungeonFile(final String filename, final boolean isSavedGame, final boolean protect) {
		if (!FilenameChecker
				.isFilenameOK(DungeonManager.getNameWithoutExtension(DungeonManager.getFileNameOnly(filename)))) {
			CommonDialogs.showErrorDialog(Strings.dialog(DialogString.ILLEGAL_CHARACTERS),
					Strings.dialog(DialogString.LOAD));
		} else {
			// Run cleanup task
			CleanupTask.cleanUp();
			// Load file
			final var xlt = new DungeonLoadTask(filename, isSavedGame, protect);
			xlt.start();
		}
	}

	public boolean saveDungeon(final boolean protect) {
		final var app = DungeonDiver7.getStuffBag();
		if (app.getMode() == StuffBag.STATUS_GAME) {
			if (this.lastUsedGameFile == null || this.lastUsedGameFile.equals(Strings.EMPTY)) {
				return this.saveDungeonAs(protect);
			}
			final var extension = DungeonManager.getExtension(this.lastUsedGameFile);
			if (extension != null) {
				if (!extension.equals(Strings.fileExtension(FileExtension.SUSPEND))) {
					this.lastUsedGameFile = DungeonManager.getNameWithoutExtension(this.lastUsedGameFile)
							+ Strings.fileExtension(FileExtension.SUSPEND);
				}
			} else {
				this.lastUsedGameFile += Strings.fileExtension(FileExtension.SUSPEND);
			}
			DungeonManager.saveDungeonFile(this.lastUsedGameFile, true, false);
		} else {
			if (protect) {
				if (this.lastUsedDungeonFile == null || this.lastUsedDungeonFile.equals(Strings.EMPTY)) {
					return this.saveDungeonAs(protect);
				}
				final var extension = DungeonManager.getExtension(this.lastUsedDungeonFile);
				if (extension != null) {
					if (!extension.equals(Strings.fileExtension(FileExtension.PROTECTED_DUNGEON))) {
						this.lastUsedDungeonFile = DungeonManager.getNameWithoutExtension(this.lastUsedDungeonFile)
								+ Strings.fileExtension(FileExtension.PROTECTED_DUNGEON);
					}
				} else {
					this.lastUsedDungeonFile += Strings.fileExtension(FileExtension.PROTECTED_DUNGEON);
				}
			} else {
				if (this.lastUsedDungeonFile == null || this.lastUsedDungeonFile.equals(Strings.EMPTY)) {
					return this.saveDungeonAs(protect);
				}
				final var extension = DungeonManager.getExtension(this.lastUsedDungeonFile);
				if (extension != null) {
					if (!extension.equals(Strings.fileExtension(FileExtension.DUNGEON))) {
						this.lastUsedDungeonFile = DungeonManager.getNameWithoutExtension(this.lastUsedDungeonFile)
								+ Strings.fileExtension(FileExtension.DUNGEON);
					}
				} else {
					this.lastUsedDungeonFile += Strings.fileExtension(FileExtension.DUNGEON);
				}
			}
			DungeonManager.saveDungeonFile(this.lastUsedDungeonFile, false, protect);
		}
		return false;
	}

	public boolean saveDungeonAs(final boolean protect) {
		final var app = DungeonDiver7.getStuffBag();
		var filename = Strings.EMPTY;
		var fileOnly = Strings.EMPTY;
		String extension, file, dir;
		final var lastSave = Prefs.getLastDirSave();
		final var fd = new FileDialog((JFrame) null, Strings.dialog(DialogString.SAVE), FileDialog.SAVE);
		fd.setDirectory(lastSave);
		while (!FilenameChecker.isFilenameOK(fileOnly)) {
			fd.setVisible(true);
			file = fd.getFile();
			dir = fd.getDirectory();
			if (file == null || dir == null) {
				break;
			}
			extension = DungeonManager.getExtension(file);
			filename = dir + file;
			fileOnly = filename.substring(dir.length() + 1);
			if (!FilenameChecker.isFilenameOK(fileOnly)) {
				CommonDialogs.showErrorDialog(Strings.dialog(DialogString.ILLEGAL_CHARACTERS),
						Strings.dialog(DialogString.SAVE));
			} else {
				Prefs.setLastDirSave(dir);
				if (app.getMode() == StuffBag.STATUS_GAME) {
					if (extension != null) {
						if (!extension.equals(Strings.fileExtension(FileExtension.SUSPEND))) {
							filename = DungeonManager.getNameWithoutExtension(file)
									+ Strings.fileExtension(FileExtension.SUSPEND);
						}
					} else {
						filename += Strings.fileExtension(FileExtension.SUSPEND);
					}
					this.lastUsedGameFile = filename;
					DungeonManager.saveDungeonFile(filename, true, false);
				} else {
					if (protect) {
						if (extension != null) {
							if (!extension.equals(Strings.fileExtension(FileExtension.PROTECTED_DUNGEON))) {
								filename = DungeonManager.getNameWithoutExtension(file)
										+ Strings.fileExtension(FileExtension.PROTECTED_DUNGEON);
							}
						} else {
							filename += Strings.fileExtension(FileExtension.PROTECTED_DUNGEON);
						}
					} else if (extension != null) {
						if (!extension.equals(Strings.fileExtension(FileExtension.DUNGEON))) {
							filename = DungeonManager.getNameWithoutExtension(file)
									+ Strings.fileExtension(FileExtension.DUNGEON);
						}
					} else {
						filename += Strings.fileExtension(FileExtension.DUNGEON);
					}
					this.lastUsedDungeonFile = filename;
					this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
					DungeonManager.saveDungeonFile(filename, false, protect);
				}
			}
		}
		return false;
	}

	private static void saveDungeonFile(final String filename, final boolean isSavedGame, final boolean protect) {
		if (isSavedGame) {
			DungeonDiver7.getStuffBag().showMessage(Strings.dialog(DialogString.SUSPENDING_GAME));
		} else {
			DungeonDiver7.getStuffBag().showMessage(Strings.dialog(DialogString.SAVING_DUNGEON));
		}
		final var xst = new DungeonSaveTask(filename, isSavedGame, protect);
		xst.start();
	}

	private static String getGameDirectoryPrefix() {
		final var osName = System.getProperty("os.name");
		if (osName.indexOf("Mac OS X") != -1) {
			// Mac OS X
			return System.getenv(DungeonManager.MAC_PREFIX);
		}
		if (osName.indexOf("Windows") != -1) {
			// Windows
			return System.getenv(DungeonManager.WIN_PREFIX);
		}
		// Other - assume UNIX-like
		return System.getenv(DungeonManager.UNIX_PREFIX);
	}

	private static String getGameDirectoryName() {
		final var osName = System.getProperty("os.name");
		if (osName.indexOf("Mac OS X") != -1) {
			// Mac OS X
			return DungeonManager.MAC_DIR;
		}
		if (osName.indexOf("Windows") != -1) {
			// Windows
			return DungeonManager.WIN_DIR;
		}
		// Other - assume UNIX-like
		return DungeonManager.UNIX_DIR;
	}

	private static String getGameDirectory() {
		final var b = new StringBuilder();
		b.append(DungeonManager.getGameDirectoryPrefix());
		b.append(DungeonManager.getGameDirectoryName());
		return b.toString();
	}

	private static String getExtension(final String s) {
		String ext = null;
		final var i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i).toLowerCase();
		}
		return ext;
	}

	private static String getNameWithoutExtension(final String s) {
		String ext = null;
		final var i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(0, i);
		} else {
			ext = s;
		}
		return ext;
	}

	private static String getFileNameOnly(final String s) {
		String fno = null;
		final var i = s.lastIndexOf(File.pathSeparatorChar);
		if (i > 0 && i < s.length() - 1) {
			fno = s.substring(i + 1);
		} else {
			fno = s;
		}
		return fno;
	}
}
