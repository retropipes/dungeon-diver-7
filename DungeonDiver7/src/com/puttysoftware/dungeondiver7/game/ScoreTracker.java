/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game;

import java.io.File;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.diane.scores.SavedScoreManager;
import com.puttysoftware.diane.scores.ScoreManager;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.utility.Extension;

class ScoreTracker {
    // Fields
    private SavedScoreManager ssMgr;
    private long moves;
    private long shots;
    private long others;
    private boolean trackScores;
    private static final String MAC_PREFIX = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_DIRECTORY_UNIX_HOME);
    private static final String WIN_PREFIX = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_DIRECTORY_WINDOWS_APPDATA);
    private static final String UNIX_PREFIX = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_DIRECTORY_UNIX_HOME);
    private static final String MAC_DIR = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_DIRECTORY_SCORES_MAC);
    private static final String WIN_DIR = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_DIRECTORY_SCORES_WINDOWS);
    private static final String UNIX_DIR = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_DIRECTORY_SCORES_UNIX);

    // Constructors
    public ScoreTracker() {
	this.moves = 0L;
	this.shots = 0L;
	this.others = 0L;
	this.ssMgr = null;
	this.trackScores = true;
    }

    // Methods
    boolean checkScore() {
	if (this.trackScores) {
	    return this.ssMgr.checkScore(new long[] { this.moves, this.shots, this.others });
	} else {
	    return false;
	}
    }

    void commitScore() {
	if (this.trackScores) {
	    final boolean result = this.ssMgr.addScore(new long[] { this.moves, this.shots, this.others });
	    if (result) {
		this.ssMgr.viewTable();
	    }
	}
    }

    void resetScore() {
	this.moves = 0L;
	this.shots = 0L;
	this.others = 0L;
    }

    void resetScore(final String filename) {
	this.setScoreFile(filename);
	this.moves = 0L;
	this.shots = 0L;
	this.others = 0L;
    }

    long getMoves() {
	return this.moves;
    }

    long getShots() {
	return this.shots;
    }

    long getOthers() {
	return this.others;
    }

    void setMoves(final long m) {
	this.moves = m;
    }

    void setShots(final long s) {
	this.shots = s;
    }

    void setOthers(final long o) {
	this.others = o;
    }

    void setScoreFile(final String filename) {
	this.trackScores = true;
	// Check filename argument
	if (filename != null) {
	    if (filename.isEmpty()) {
		this.trackScores = false;
	    }
	} else {
	    this.trackScores = false;
	}
	if (this.trackScores) {
	    // Make sure the needed directories exist first
	    final File sf = ScoreTracker.getScoresFile(filename);
	    final File parent = new File(sf.getParent());
	    if (!parent.exists()) {
		final boolean success = parent.mkdirs();
		if (!success) {
		    this.trackScores = false;
		}
	    }
	    if (this.trackScores) {
		final String scoresFile = sf.getAbsolutePath();
		this.ssMgr = new SavedScoreManager(3, 10, ScoreManager.SORT_ORDER_DESCENDING, 10000L,
			LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
				LocaleConstants.NOTL_STRING_PROGRAM_NAME)
				+ LocaleConstants.COMMON_STRING_SPACE
				+ LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
					LocaleConstants.GAME_STRING_SCORES),
			new String[] {
				LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
					LocaleConstants.GAME_STRING_SCORE_MOVES),
				LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
					LocaleConstants.GAME_STRING_SCORE_SHOTS),
				LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
					LocaleConstants.GAME_STRING_SCORE_OTHERS) },
			scoresFile);
	    }
	}
    }

    void incrementMoves() {
	this.moves++;
    }

    void incrementShots() {
	this.shots++;
    }

    void incrementOthers() {
	this.others++;
    }

    void decrementMoves() {
	this.moves--;
    }

    void decrementShots() {
	this.shots--;
    }

    void decrementOthers() {
	this.others--;
    }

    void showScoreTable() {
	if (this.trackScores) {
	    this.ssMgr.viewTable();
	} else {
	    CommonDialogs.showDialog(LocaleLoader.loadString(LocaleConstants.GAME_STRINGS_FILE,
		    LocaleConstants.GAME_STRING_SCORES_UNAVAILABLE));
	}
    }

    private static String getScoreDirPrefix() {
	final String osName = System.getProperty(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_OS_NAME));
	if (osName.indexOf(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_MAC_OS_X)) != -1) {
	    // Mac OS X
	    return System.getenv(ScoreTracker.MAC_PREFIX);
	} else if (osName.indexOf(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_WINDOWS)) != -1) {
	    // Windows
	    return System.getenv(ScoreTracker.WIN_PREFIX);
	} else {
	    // Other - assume UNIX-like
	    return System.getenv(ScoreTracker.UNIX_PREFIX);
	}
    }

    private static String getScoreDirectory() {
	final String osName = System.getProperty(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_OS_NAME));
	if (osName.indexOf(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_MAC_OS_X)) != -1) {
	    // Mac OS X
	    return ScoreTracker.MAC_DIR;
	} else if (osName.indexOf(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		LocaleConstants.NOTL_STRING_WINDOWS)) != -1) {
	    // Windows
	    return ScoreTracker.WIN_DIR;
	} else {
	    // Other - assume UNIX-like
	    return ScoreTracker.UNIX_DIR;
	}
    }

    private static File getScoresFile(final String filename) {
	final StringBuilder b = new StringBuilder();
	b.append(ScoreTracker.getScoreDirPrefix());
	b.append(ScoreTracker.getScoreDirectory());
	b.append(filename);
	b.append(LocaleConstants.COMMON_STRING_UNDERSCORE);
	b.append(DungeonDiver7.getApplication().getDungeonManager().getDungeon().getActiveLevel() + 1);
	b.append(Extension.getScoresExtensionWithPeriod());
	return new File(b.toString());
    }
}
