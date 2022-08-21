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
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.GameString;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.dungeondiver7.locale.old.LocaleConstants;

class ScoreTracker {
    // Fields
    private SavedScoreManager ssMgr;
    private long moves;
    private long shots;
    private long others;
    private boolean trackScores;

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
			Strings.untranslated(Untranslated.PROGRAM_NAME) + LocaleConstants.COMMON_STRING_SPACE
				+ Strings.game(GameString.SCORES),
			new String[] { Strings.game(GameString.SCORE_MOVES), Strings.game(GameString.SCORE_SHOTS),
				Strings.game(GameString.SCORE_OTHERS) },
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
	    CommonDialogs.showDialog(Strings.game(GameString.SCORES_UNAVAILABLE));
	}
    }

    private static String getScoreDirPrefix() {
	final String osName = System.getProperty(Strings.untranslated(Untranslated.OS_NAME));
	if (osName.indexOf(Strings.untranslated(Untranslated.MACOS)) != -1) {
	    // Mac OS X
	    return System.getenv(Strings.untranslated(Untranslated.UNIX_HOME));
	} else if (osName.indexOf(Strings.untranslated(Untranslated.WINDOWS)) != -1) {
	    // Windows
	    return System.getenv(Strings.untranslated(Untranslated.WINDOWS_SUPPORT));
	} else {
	    // Other - assume UNIX-like
	    return System.getenv(Strings.untranslated(Untranslated.UNIX_HOME));
	}
    }

    private static String getScoreDirectory() {
	final String osName = System.getProperty(Strings.untranslated(Untranslated.OS_NAME));
	String base;
	if (osName.indexOf(Strings.untranslated(Untranslated.MACOS)) != -1) {
	    // Mac OS X
	    base = Strings.untranslated(Untranslated.MACOS_SUPPORT);
	} else if (osName.indexOf(Strings.untranslated(Untranslated.WINDOWS)) != -1) {
	    // Windows
	    base = Strings.EMPTY;
	} else {
	    // Other - assume UNIX-like
	    base = Strings.untranslated(Untranslated.UNIX_SUPPORT);
	}
	if (base != Strings.EMPTY) {
	    return base + File.pathSeparator + Strings.untranslated(Untranslated.COMPANY_SUBFOLDER) + File.pathSeparator
		    + Strings.untranslated(Untranslated.PROGRAM_NAME);
	} else {
	    return Strings.untranslated(Untranslated.COMPANY_SUBFOLDER) + File.pathSeparator
		    + Strings.untranslated(Untranslated.PROGRAM_NAME);
	}
    }

    private static File getScoresFile(final String filename) {
	final StringBuilder b = new StringBuilder();
	b.append(ScoreTracker.getScoreDirPrefix());
	b.append(ScoreTracker.getScoreDirectory());
	b.append(filename);
	b.append(LocaleConstants.COMMON_STRING_UNDERSCORE);
	b.append(DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getActiveLevel() + 1);
	b.append(Strings.fileExtension(FileExtension.SCORES));
	return new File(b.toString());
    }
}
