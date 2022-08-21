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

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeon;
import com.puttysoftware.dungeondiver7.dungeon.ltv4.LaserTankV4LoadTask;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.EditorString;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.dungeondiver7.locale.old.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;
import com.puttysoftware.dungeondiver7.manager.file.DungeonLoadTask;
import com.puttysoftware.dungeondiver7.manager.file.DungeonSaveTask;
import com.puttysoftware.dungeondiver7.manager.file.GameFinder;
import com.puttysoftware.dungeondiver7.manager.file.GameLoadTask;
import com.puttysoftware.dungeondiver7.manager.file.GameSaveTask;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utility.CleanupTask;
import com.puttysoftware.dungeondiver7.utility.FileExtensions;
import com.puttysoftware.fileutils.FilenameChecker;

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
	final StuffBag app = DungeonDiver7.getStuffBag();
	final int mode = app.getMode();
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
	final StuffBag app = DungeonDiver7.getStuffBag();
	this.loaded = status;
	app.getMenuManager().checkFlags();
    }

    public boolean getDirty() {
	return this.isDirty;
    }

    public void setDirty(final boolean newDirty) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	this.isDirty = newDirty;
	final JFrame frame = app.getOutputFrame();
	if (frame != null) {
	    frame.getRootPane().putClientProperty("Window.documentModified", Boolean.valueOf(newDirty));
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
	int status = 0;
	boolean saved = true;
	String filename;
	final GameFinder gf = new GameFinder();
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
	    final String gameDir = DungeonManager.getGameDirectory();
	    final String[] rawChoices = new File(gameDir).list(gf);
	    if (rawChoices != null && rawChoices.length > 0) {
		final String[] choices = new String[rawChoices.length];
		// Strip extension
		for (int x = 0; x < choices.length; x++) {
		    choices[x] = DungeonManager.getNameWithoutExtension(rawChoices[x]);
		}
		final String returnVal = CommonDialogs.showInputDialog("Select a Game", "Load Game", choices,
			choices[0]);
		if (returnVal != null) {
		    int index = -1;
		    for (int x = 0; x < choices.length; x++) {
			if (returnVal.equals(choices[x])) {
			    index = x;
			    break;
			}
		    }
		    if (index != -1) {
			final File file = new File(gameDir + rawChoices[index]);
			filename = file.getAbsolutePath();
			DungeonManager.loadGameFile(filename);
		    } else {
			// Result not found
			if (this.loaded) {
			    return true;
			}
		    }
		} else {
		    // User cancelled
		    if (this.loaded) {
			return true;
		    }
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
	    final GameLoadTask llt = new GameLoadTask(filename);
	    llt.start();
	}
    }

    public static boolean saveGame() {
	String filename = "";
	String extension;
	String returnVal = "\\";
	while (!FilenameChecker.isFilenameOK(returnVal)) {
	    returnVal = CommonDialogs.showTextInputDialog("Name?", "Save Game");
	    if (returnVal != null) {
		extension = FileExtensions.getGameExtensionWithPeriod();
		final File file = new File(DungeonManager.getGameDirectory() + returnVal + extension);
		filename = file.getAbsolutePath();
		if (!FilenameChecker.isFilenameOK(returnVal)) {
		    CommonDialogs.showErrorDialog("The file name you entered contains illegal characters.\n"
			    + "These characters are not allowed: /?<>\\:|\"\n"
			    + "Files named con, nul, or prn are illegal, as are files\n"
			    + "named com1 through com9 and lpt1 through lpt9.", "Save Game");
		} else {
		    // Make sure folder exists
		    if (!file.getParentFile().exists()) {
			final boolean okay = file.getParentFile().mkdirs();
			if (!okay) {
			    DungeonDiver7.logError(new IOException("Cannot create game folder!"));
			}
		    }
		    DungeonManager.saveGameFile(filename);
		}
	    } else {
		break;
	    }
	}
	return false;
    }

    private static void saveGameFile(final String filename) {
	final String sg = "Saved Game";
	DungeonDiver7.getStuffBag().showMessage("Saving " + sg + " file...");
	final GameSaveTask lst = new GameSaveTask(filename);
	lst.start();
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
	final StuffBag app = DungeonDiver7.getStuffBag();
	int status = 0;
	boolean saved = true;
	String filename, extension, file, dir;
	final FileDialog fd = new FileDialog(app.getOutputFrame(), Strings.dialog(DialogString.LOAD), FileDialog.LOAD);
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
		if (extension.equals(FileExtensions.getDungeonExtension())) {
		    this.lastUsedDungeonFile = filename;
		    this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
		    DungeonManager.loadDungeonFile(filename, false, false);
		} else if (extension.equals(FileExtensions.getProtectedDungeonExtension())) {
		    this.lastUsedDungeonFile = filename;
		    this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
		    DungeonManager.loadDungeonFile(filename, false, true);
		} else if (extension.equals(FileExtensions.getGameExtension())) {
		    this.lastUsedGameFile = filename;
		    DungeonManager.loadDungeonFile(filename, true, false);
		} else if (extension.equals(FileExtensions.getOldLevelExtension())) {
		    this.lastUsedDungeonFile = filename;
		    this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
		    final LaserTankV4LoadTask ollt = new LaserTankV4LoadTask(filename);
		    ollt.start();
		} else {
		    CommonDialogs.showDialog(Strings.dialog(DialogString.NON_DUNGEON_FILE));
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

    private static void loadDungeonFile(final String filename, final boolean isSavedGame, final boolean protect) {
	if (!FilenameChecker
		.isFilenameOK(DungeonManager.getNameWithoutExtension(DungeonManager.getFileNameOnly(filename)))) {
	    CommonDialogs.showErrorDialog(Strings.dialog(DialogString.ILLEGAL_CHARACTERS),
		    Strings.dialog(DialogString.LOAD));
	} else {
	    // Run cleanup task
	    CleanupTask.cleanUp();
	    // Load file
	    final DungeonLoadTask xlt = new DungeonLoadTask(filename, isSavedGame, protect);
	    xlt.start();
	}
    }

    public boolean saveDungeon(final boolean protect) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	if (app.getMode() == StuffBag.STATUS_GAME) {
	    if (this.lastUsedGameFile != null && !this.lastUsedGameFile.equals(Strings.EMPTY)) {
		final String extension = DungeonManager.getExtension(this.lastUsedGameFile);
		if (extension != null) {
		    if (!extension.equals(FileExtensions.getGameExtension())) {
			this.lastUsedGameFile = DungeonManager.getNameWithoutExtension(this.lastUsedGameFile)
				+ FileExtensions.getGameExtensionWithPeriod();
		    }
		} else {
		    this.lastUsedGameFile += FileExtensions.getGameExtensionWithPeriod();
		}
		DungeonManager.saveDungeonFile(this.lastUsedGameFile, true, false);
	    } else {
		return this.saveDungeonAs(protect);
	    }
	} else {
	    if (protect) {
		if (this.lastUsedDungeonFile != null && !this.lastUsedDungeonFile.equals(Strings.EMPTY)) {
		    final String extension = DungeonManager.getExtension(this.lastUsedDungeonFile);
		    if (extension != null) {
			if (!extension.equals(FileExtensions.getProtectedDungeonExtension())) {
			    this.lastUsedDungeonFile = DungeonManager.getNameWithoutExtension(this.lastUsedDungeonFile)
				    + FileExtensions.getProtectedDungeonExtensionWithPeriod();
			}
		    } else {
			this.lastUsedDungeonFile += FileExtensions.getProtectedDungeonExtensionWithPeriod();
		    }
		    DungeonManager.saveDungeonFile(this.lastUsedDungeonFile, false, protect);
		} else {
		    return this.saveDungeonAs(protect);
		}
	    } else {
		if (this.lastUsedDungeonFile != null && !this.lastUsedDungeonFile.equals(Strings.EMPTY)) {
		    final String extension = DungeonManager.getExtension(this.lastUsedDungeonFile);
		    if (extension != null) {
			if (!extension.equals(FileExtensions.getDungeonExtension())) {
			    this.lastUsedDungeonFile = DungeonManager.getNameWithoutExtension(this.lastUsedDungeonFile)
				    + FileExtensions.getDungeonExtensionWithPeriod();
			}
		    } else {
			this.lastUsedDungeonFile += FileExtensions.getDungeonExtensionWithPeriod();
		    }
		    DungeonManager.saveDungeonFile(this.lastUsedDungeonFile, false, protect);
		} else {
		    return this.saveDungeonAs(protect);
		}
	    }
	}
	return false;
    }

    public boolean saveDungeonAs(final boolean protect) {
	final StuffBag app = DungeonDiver7.getStuffBag();
	String filename = Strings.EMPTY;
	String fileOnly = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_DOUBLE_BACKSLASH);
	String extension, file, dir;
	final String lastSave = PrefsManager.getLastDirSave();
	final FileDialog fd = new FileDialog(app.getOutputFrame(), Strings.dialog(DialogString.SAVE), FileDialog.SAVE);
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
		    CommonDialogs.showErrorDialog(Strings.dialog(DialogString.ILLEGAL_CHARACTERS),
			    Strings.dialog(DialogString.SAVE));
		} else {
		    PrefsManager.setLastDirSave(dir);
		    if (app.getMode() == StuffBag.STATUS_GAME) {
			if (extension != null) {
			    if (!extension.equals(FileExtensions.getGameExtension())) {
				filename = DungeonManager.getNameWithoutExtension(file)
					+ FileExtensions.getGameExtensionWithPeriod();
			    }
			} else {
			    filename += FileExtensions.getGameExtensionWithPeriod();
			}
			this.lastUsedGameFile = filename;
			DungeonManager.saveDungeonFile(filename, true, false);
		    } else {
			if (protect) {
			    if (extension != null) {
				if (!extension.equals(FileExtensions.getProtectedDungeonExtension())) {
				    filename = DungeonManager.getNameWithoutExtension(file)
					    + FileExtensions.getProtectedDungeonExtensionWithPeriod();
				}
			    } else {
				filename += FileExtensions.getProtectedDungeonExtensionWithPeriod();
			    }
			    this.lastUsedDungeonFile = filename;
			    this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
			    DungeonManager.saveDungeonFile(filename, false, protect);
			} else {
			    if (extension != null) {
				if (!extension.equals(FileExtensions.getDungeonExtension())) {
				    filename = DungeonManager.getNameWithoutExtension(file)
					    + FileExtensions.getDungeonExtensionWithPeriod();
				}
			    } else {
				filename += FileExtensions.getDungeonExtensionWithPeriod();
			    }
			    this.lastUsedDungeonFile = filename;
			    this.scoresFileName = DungeonManager.getNameWithoutExtension(file);
			    DungeonManager.saveDungeonFile(filename, false, protect);
			}
		    }
		}
	    } else {
		break;
	    }
	}
	return false;
    }

    private static void saveDungeonFile(final String filename, final boolean isSavedGame, final boolean protect) {
	if (isSavedGame) {
	    DungeonDiver7.getStuffBag().showMessage(LocaleLoader.loadString(LocaleConstants.MESSAGE_STRINGS_FILE,
		    LocaleConstants.MESSAGE_STRING_SAVING_GAME));
	} else {
	    DungeonDiver7.getStuffBag().showMessage(LocaleLoader.loadString(LocaleConstants.MESSAGE_STRINGS_FILE,
		    LocaleConstants.MESSAGE_STRING_SAVING_ARENA));
	}
	final DungeonSaveTask xst = new DungeonSaveTask(filename, isSavedGame, protect);
	xst.start();
    }

    private static String getGameDirectoryPrefix() {
	final String osName = System.getProperty("os.name");
	if (osName.indexOf("Mac OS X") != -1) {
	    // Mac OS X
	    return System.getenv(DungeonManager.MAC_PREFIX);
	} else if (osName.indexOf("Windows") != -1) {
	    // Windows
	    return System.getenv(DungeonManager.WIN_PREFIX);
	} else {
	    // Other - assume UNIX-like
	    return System.getenv(DungeonManager.UNIX_PREFIX);
	}
    }

    private static String getGameDirectoryName() {
	final String osName = System.getProperty("os.name");
	if (osName.indexOf("Mac OS X") != -1) {
	    // Mac OS X
	    return DungeonManager.MAC_DIR;
	} else if (osName.indexOf("Windows") != -1) {
	    // Windows
	    return DungeonManager.WIN_DIR;
	} else {
	    // Other - assume UNIX-like
	    return DungeonManager.UNIX_DIR;
	}
    }

    private static String getGameDirectory() {
	final StringBuilder b = new StringBuilder();
	b.append(DungeonManager.getGameDirectoryPrefix());
	b.append(DungeonManager.getGameDirectoryName());
	return b.toString();
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
