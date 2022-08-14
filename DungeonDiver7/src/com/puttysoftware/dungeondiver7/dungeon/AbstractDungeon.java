/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractButton;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractButtonDoor;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractCharacter;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractTunnel;
import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeon;
import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.utility.Direction;

public abstract class AbstractDungeon {
    // Constants
    private static final int MIN_LEVELS = 1;
    protected static final int MAX_LEVELS = Integer.MAX_VALUE;
    protected static final int ERA_COUNT = 5;
    private static final int MAX_COLUMNS = 250;
    private static final int MAX_ROWS = 250;

    // Constructors
    /**
     * @throws IOException
     */
    public AbstractDungeon() throws IOException {
	// Do nothing
    }

    // Static methods
    public static String getDungeonTempFolder() {
	return System
		.getProperty(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_TEMP_DIR))
		+ File.separator
		+ LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_PROGRAM_NAME);
    }

    public static int getMinLevels() {
	return AbstractDungeon.MIN_LEVELS;
    }

    public static int getMaxLevels() {
	return AbstractDungeon.MAX_LEVELS;
    }

    public static int getMaxFloors() {
	return AbstractDungeonData.getMaxFloors();
    }

    public static int getMinFloors() {
	return AbstractDungeonData.getMinFloors();
    }

    public static int getMaxRows() {
	return AbstractDungeon.MAX_ROWS;
    }

    public static int getMinRows() {
	return AbstractDungeonData.getMinRows();
    }

    public static int getMaxColumns() {
	return AbstractDungeon.MAX_ROWS;
    }

    public static int getMinColumns() {
	return AbstractDungeon.MAX_COLUMNS;
    }

    public static AbstractDungeon getTemporaryBattleCopy() throws IOException {
	final CurrentDungeon temp = new CurrentDungeon();
	temp.addFixedSizeLevel(DungeonDiver7.getBattleDungeonSize(), DungeonDiver7.getBattleDungeonSize(), 1);
	temp.fillDefault();
	return temp;
    }

    // Methods
    public abstract String getDungeonTempMusicFolder();

    public abstract boolean isMoveShootAllowed();

    public abstract boolean isMoveShootAllowedGlobally();

    public abstract boolean isMoveShootAllowedThisLevel();

    public abstract void setMoveShootAllowedGlobally(boolean value);

    public abstract void setMoveShootAllowedThisLevel(boolean value);

    public abstract String getMusicFilename();

    public abstract void setMusicFilename(final String newMusicFilename);

    public abstract String getName();

    public abstract void setName(String newName);

    public abstract String getHint();

    public abstract void setHint(String newHint);

    public abstract String getAuthor();

    public abstract void setAuthor(String newAuthor);

    public abstract int getDifficulty();

    public abstract void setDifficulty(int newDifficulty);

    public abstract String getBasePath();

    public abstract void setPrefixHandler(AbstractPrefixIO xph);

    public abstract void setSuffixHandler(AbstractSuffixIO xsh);

    public abstract void tickTimers();

    public abstract void resetVisibleSquares(final int floor);

    public abstract void updateVisibleSquares(final int xp, final int yp, final int zp);

    public abstract boolean isSquareVisible(final int x1, final int y1, final int x2, final int y2, final int zp);

    public abstract int getActiveLevel();

    public abstract int getActiveEra();

    public final boolean switchToNextLevelWithDifficulty(final int[] difficulty) {
	boolean keepGoing = true;
	while (keepGoing) {
	    final int diff = this.getDifficulty();
	    for (final int element : difficulty) {
		if (diff - 1 == element) {
		    keepGoing = false;
		    return true;
		}
	    }
	    if (!this.doesLevelExistOffset(1)) {
		keepGoing = false;
		return false;
	    }
	    if (keepGoing) {
		this.switchLevelOffset(1);
	    }
	}
	return false;
    }

    public final boolean switchToPreviousLevelWithDifficulty(final int[] difficulty) {
	boolean keepGoing = true;
	while (keepGoing) {
	    final int diff = this.getDifficulty();
	    for (final int element : difficulty) {
		if (diff - 1 == element) {
		    keepGoing = false;
		    return true;
		}
	    }
	    if (!this.doesLevelExistOffset(-1)) {
		keepGoing = false;
		return false;
	    }
	    if (keepGoing) {
		this.switchLevelOffset(-1);
	    }
	}
	return false;
    }

    public abstract String[] getLevelInfoList();

    public abstract void generateLevelInfoList();

    public abstract void switchLevel(int level);

    public abstract void switchLevelOffset(int level);

    public abstract void switchEra(final int era);

    public abstract void switchEraOffset(final int era);

    protected abstract void switchInternal(int level, int era);

    public abstract boolean doesLevelExist(int level);

    public abstract boolean doesLevelExistOffset(int level);

    public abstract void cutLevel();

    public abstract void copyLevel();

    public abstract void pasteLevel();

    public abstract boolean isPasteBlocked();

    public abstract boolean isCutBlocked();

    public abstract boolean insertLevelFromClipboard();

    public abstract boolean addLevel();

    public abstract boolean addFixedSizeLevel(final int rows, final int cols, final int floors);

    public final boolean removeLevel(final int num) {
	final int saveLevel = this.getActiveLevel();
	this.switchLevel(num);
	final boolean success = this.removeActiveLevel();
	if (success) {
	    if (saveLevel == 0) {
		// Was at first level
		this.switchLevel(0);
	    } else {
		// Was at level other than first
		if (saveLevel > num) {
		    // Saved level was shifted down
		    this.switchLevel(saveLevel - 1);
		} else if (saveLevel < num) {
		    // Saved level was NOT shifted down
		    this.switchLevel(saveLevel);
		} else {
		    // Saved level was deleted
		    this.switchLevel(0);
		}
	    }
	} else {
	    this.switchLevel(saveLevel);
	}
	return success;
    }

    protected abstract boolean removeActiveLevel();

    public abstract boolean isCellDirty(final int row, final int col, final int floor);

    public abstract AbstractDungeonObject getCell(final int row, final int col, final int floor, final int layer);

    public abstract AbstractDungeonObject getVirtualCell(final int row, final int col, final int floor,
	    final int layer);

    public abstract int getStartRow(final int pi);

    public abstract int getStartColumn(final int pi);

    public abstract int getStartFloor(final int pi);

    public abstract int getStartLevel(final int pi);

    public abstract int getPlayerLocationX(final int pi);

    public abstract int getPlayerLocationY(final int pi);

    public abstract int getPlayerLocationZ(final int pi);

    public abstract int getRows();

    public abstract int getColumns();

    public abstract int getFloors();

    public abstract int getLevels();

    public abstract boolean doesPlayerExist(final int pi);

    public abstract int[] findPlayer(final int number);

    public abstract void tickTimers(final int floor, final int actionType);

    public static boolean radialScan(final int cx, final int cy, final int r, final int tx, final int ty) {
	return Math.abs(tx - cx) <= r && Math.abs(ty - cy) <= r;
    }

    public abstract void checkForEnemies(final int floor, final int ex, final int ey, final AbstractCharacter e);

    public abstract int checkForMagnetic(int floor, int centerX, int centerY, Direction dir);

    public abstract int[] circularScan(final int x, final int y, final int z, final int maxR, final String targetName,
	    final boolean moved);

    public abstract int[] circularScanTunnel(final int x, final int y, final int z, final int maxR, final int tx,
	    final int ty, final AbstractTunnel target, final boolean moved);

    public abstract void circularScanRange(final int x, final int y, final int z, final int maxR, final int rangeType,
	    final int forceUnits);

    public abstract int[] findObject(int z, String targetName);

    public abstract boolean circularScanPlayer(final int x, final int y, final int z, final int maxR);

    public abstract void fullScanKillPlayers();

    public abstract void fullScanFreezeGround();

    public abstract void fullScanAllButtonOpen(int z, AbstractButton source);

    public abstract void fullScanAllButtonClose(int z, AbstractButton source);

    public abstract void fullScanButtonBind(int dx, int dy, int z, AbstractButtonDoor source);

    public abstract void fullScanButtonCleanup(int px, int py, int z, AbstractButton button);

    public abstract void fullScanFindButtonLostDoor(int z, AbstractButtonDoor door);

    public abstract void setCell(final AbstractDungeonObject mo, final int row, final int col, final int floor,
	    final int layer);

    public abstract void setVirtualCell(final AbstractDungeonObject mo, final int row, final int col, final int floor,
	    final int layer);

    public abstract void markAsDirty(final int row, final int col, final int floor);

    public abstract void clearDirtyFlags(int floor);

    public abstract void setDirtyFlags(int floor);

    public abstract void clearVirtualGrid();

    public abstract void setStartRow(final int pi, final int newStartRow);

    public abstract void setStartColumn(final int pi, final int newStartColumn);

    public abstract void setStartFloor(final int pi, final int newStartFloor);

    public abstract void offsetPlayerLocationX(final int pi, final int newPlayerLocationX);

    public abstract void offsetPlayerLocationY(final int pi, final int newPlayerLocationY);

    public abstract void offsetPlayerLocationZ(final int pi, final int newPlayerLocationZ);

    public abstract void setPlayerLocationX(final int pi, final int newPlayerLocationX);

    public abstract void setPlayerLocationY(final int pi, final int newPlayerLocationY);

    public abstract void setPlayerLocationZ(final int pi, final int newPlayerLocationZ);

    public abstract void savePlayerLocation();

    public abstract void restorePlayerLocation();

    public abstract void setPlayerToStart();

    public abstract void fillDefault();

    public abstract void save();

    public abstract void restore();

    public abstract void resize(int z, AbstractDungeonObject nullFill);

    public abstract void setData(AbstractDungeonData newData, int count);

    public abstract void enableHorizontalWraparound();

    public abstract void disableHorizontalWraparound();

    public abstract void enableVerticalWraparound();

    public abstract void disableVerticalWraparound();

    public abstract void enableThirdDimensionWraparound();

    public abstract void disableThirdDimensionWraparound();

    public abstract boolean isHorizontalWraparoundEnabled();

    public abstract boolean isVerticalWraparoundEnabled();

    public abstract boolean isThirdDimensionWraparoundEnabled();

    public abstract AbstractDungeon readDungeon() throws IOException;

    public abstract void writeDungeon() throws IOException;

    public abstract void undo();

    public abstract void redo();

    public abstract boolean tryUndo();

    public abstract boolean tryRedo();

    public abstract void updateUndoHistory(final HistoryStatus whatIs);

    public abstract void updateRedoHistory(final HistoryStatus whatIs);

    public abstract HistoryStatus getWhatWas();

    public abstract void resetHistoryEngine();
}