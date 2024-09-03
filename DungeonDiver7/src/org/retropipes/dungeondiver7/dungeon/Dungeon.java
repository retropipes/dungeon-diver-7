/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon;

import java.io.File;
import java.io.IOException;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractButton;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractButtonDoor;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractCharacter;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovingObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractTunnel;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.dungeon.current.CurrentDungeon;
import org.retropipes.dungeondiver7.locale.Difficulty;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;
import org.retropipes.dungeondiver7.manager.file.AbstractPrefixIO;
import org.retropipes.dungeondiver7.manager.file.AbstractSuffixIO;

public abstract class Dungeon {
    // Constants
    private static final int MIN_LEVELS = 1;
    protected static final int MAX_LEVELS = Integer.MAX_VALUE;
    protected static final int ERA_COUNT = 5;
    private static final int MAX_COLUMNS = 250;
    private static final int MAX_ROWS = 250;

    // Static methods
    public static String getDungeonTempFolder() {
	return System.getProperty(Strings.untranslated(Untranslated.TEMP_DIR)) + File.separator
		+ Strings.untranslated(Untranslated.PROGRAM_NAME);
    }

    public static int getMaxColumns() {
	return Dungeon.MAX_ROWS;
    }

    public static int getMaxFloors() {
	return DungeonData.getMaxFloors();
    }

    public static int getMaxLevels() {
	return Dungeon.MAX_LEVELS;
    }

    public static int getMaxRows() {
	return Dungeon.MAX_ROWS;
    }

    public static int getMinColumns() {
	return Dungeon.MAX_COLUMNS;
    }

    public static int getMinFloors() {
	return DungeonData.getMinFloors();
    }

    public static int getMinLevels() {
	return Dungeon.MIN_LEVELS;
    }

    public static int getMinRows() {
	return DungeonData.getMinRows();
    }

    public static Dungeon getTemporaryBattleCopy() throws IOException {
	final var temp = new CurrentDungeon();
	temp.addFixedSizeLevel(DungeonDiver7.getBattleDungeonSize(), DungeonDiver7.getBattleDungeonSize(), 1);
	temp.fillDefault();
	return temp;
    }

    public static boolean radialScan(final int cx, final int cy, final int r, final int tx, final int ty) {
	return Math.abs(tx - cx) <= r && Math.abs(ty - cy) <= r;
    }

    // Constructors
    /**
     * @throws IOException
     */
    public Dungeon() throws IOException {
	// Do nothing
    }

    public abstract boolean addFixedSizeLevel(final int rows, final int cols, final int floors);

    public abstract boolean addLevel();

    public abstract void checkForEnemies(final int floor, final int ex, final int ey, final AbstractCharacter e);

    public abstract int checkForMagnetic(int floor, int centerX, int centerY, Direction dir);

    public abstract int[] circularScan(final int x, final int y, final int z, final int maxR, final String targetName,
	    final boolean moved);

    public abstract boolean circularScanPlayer(final int x, final int y, final int z, final int maxR);

    public abstract void circularScanRange(final int x, final int y, final int z, final int maxR, final int rangeType,
	    final int forceUnits);

    public abstract int[] circularScanTunnel(final int x, final int y, final int z, final int maxR, final int tx,
	    final int ty, final AbstractTunnel target, final boolean moved);

    public abstract void clearDirtyFlags(int floor);

    public abstract void clearVirtualGrid();

    public abstract Direction computeFinalBossMoveDirection(final int locX, final int locY, final int locZ,
	    final int pi);

    public abstract void copyLevel();

    public abstract void cutLevel();

    public abstract void disableHorizontalWraparound();

    public abstract void disableThirdDimensionWraparound();

    public abstract void disableVerticalWraparound();

    public abstract boolean doesLevelExist(int level);

    public abstract boolean doesLevelExistOffset(int level);

    public abstract boolean doesPlayerExist(final int pi);

    public abstract void enableHorizontalWraparound();

    public abstract void enableThirdDimensionWraparound();

    public abstract void enableVerticalWraparound();

    public abstract void fillDefault();

    public abstract int[] findObject(int z, String targetName);

    public abstract int[] findPlayer(final int number);

    public abstract void fullScanAllButtonClose(int z, AbstractButton source);

    public abstract void fullScanAllButtonOpen(int z, AbstractButton source);

    public abstract void fullScanButtonBind(int dx, int dy, int z, AbstractButtonDoor source);

    public abstract void fullScanButtonCleanup(int px, int py, int z, AbstractButton button);

    public abstract void fullScanFindButtonLostDoor(int z, AbstractButtonDoor door);

    public abstract void fullScanFreezeGround();

    public abstract void fullScanKillPlayers();

    public abstract void generateLevelInfoList();

    public abstract int getActiveEra();

    public abstract int getActiveLevel();

    public abstract String getAuthor();

    public abstract String getBasePath();

    public abstract GameObject getCell(final int row, final int col, final int floor, final int layer);

    public abstract int getColumns();

    public abstract Difficulty getDifficulty();

    public abstract String getDungeonTempMusicFolder();

    public abstract int getFloors();

    public abstract String getHint();

    public abstract String[] getLevelInfoList();

    public abstract int getLevels();

    public abstract String getMusicFilename();

    public abstract String getName();

    public abstract int getPlayerLocationX(final int pi);

    public abstract int getPlayerLocationY(final int pi);

    public abstract int getPlayerLocationZ(final int pi);

    public abstract int getRows();

    public abstract int getStartColumn(final int pi);

    public abstract int getStartFloor(final int pi);

    public abstract int getStartLevel(final int pi);

    public abstract int getStartRow(final int pi);

    public abstract GameObject getVirtualCell(final int row, final int col, final int floor, final int layer);

    public abstract HistoryStatus getWhatWas();

    public abstract boolean insertLevelFromClipboard();

    public abstract boolean isCellDirty(final int row, final int col, final int floor);

    public abstract boolean isCutBlocked();

    public abstract boolean isHorizontalWraparoundEnabled();

    public abstract boolean isMoveShootAllowed();

    public abstract boolean isMoveShootAllowedGlobally();

    public abstract boolean isMoveShootAllowedThisLevel();

    public abstract boolean isPasteBlocked();

    public abstract boolean isSquareVisible(final int x1, final int y1, final int x2, final int y2, final int zp);

    public abstract boolean isThirdDimensionWraparoundEnabled();

    public abstract boolean isVerticalWraparoundEnabled();

    public abstract void markAsDirty(final int row, final int col, final int floor);

    public abstract void offsetPlayerLocationX(final int pi, final int newPlayerLocationX);

    public abstract void offsetPlayerLocationY(final int pi, final int newPlayerLocationY);

    public abstract void offsetPlayerLocationZ(final int pi, final int newPlayerLocationZ);

    public abstract void pasteLevel();

    public abstract void postBattle(final AbstractMovingObject m, final int xLoc, final int yLoc, final boolean player);

    public abstract Dungeon readDungeon() throws IOException;

    public abstract void redo();

    protected abstract boolean removeActiveLevel();

    public final boolean removeLevel(final int num) {
	final var saveLevel = this.getActiveLevel();
	this.switchLevel(num);
	final var success = this.removeActiveLevel();
	if (success) {
	    if (saveLevel == 0) {
		// Was at first level
		this.switchLevel(0);
	    } else // Was at level other than first
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
	} else {
	    this.switchLevel(saveLevel);
	}
	return success;
    }

    public abstract void resetHistoryEngine();

    public abstract void resetVisibleSquares(final int floor);

    public abstract void resize(int z, GameObject nullFill);

    public abstract void restore();

    public abstract void restorePlayerLocation();

    public abstract void save();

    public abstract void savePlayerLocation();

    public abstract void setAuthor(String newAuthor);

    public abstract void setCell(final GameObject mo, final int row, final int col, final int floor,
	    final int layer);

    public abstract void setData(DungeonData newData, int count);

    public abstract void setDifficulty(Difficulty newDifficulty);

    public abstract void setDirtyFlags(int floor);

    public abstract void setHint(String newHint);

    public abstract void setMoveShootAllowedGlobally(boolean value);

    public abstract void setMoveShootAllowedThisLevel(boolean value);

    public abstract void setMusicFilename(final String newMusicFilename);

    public abstract void setName(String newName);

    public abstract void setPlayerLocationX(final int pi, final int newPlayerLocationX);

    public abstract void setPlayerLocationY(final int pi, final int newPlayerLocationY);

    public abstract void setPlayerLocationZ(final int pi, final int newPlayerLocationZ);

    public abstract void setPlayerToStart();

    public abstract void setPrefixHandler(AbstractPrefixIO xph);

    public abstract void setStartColumn(final int pi, final int newStartColumn);

    public abstract void setStartFloor(final int pi, final int newStartFloor);

    public abstract void setStartRow(final int pi, final int newStartRow);

    public abstract void setSuffixHandler(AbstractSuffixIO xsh);

    public abstract void setVirtualCell(final GameObject mo, final int row, final int col, final int floor,
	    final int layer);

    public abstract void switchEra(final int era);

    public abstract void switchEraOffset(final int era);

    protected abstract void switchInternal(int level, int era);

    public abstract void switchLevel(int level);

    public abstract void switchLevelOffset(int level);

    public final boolean switchToNextLevelWithDifficulty(final int[] difficulty) {
	var keepGoing = true;
	while (keepGoing) {
	    final var diff = this.getDifficulty().ordinal();
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
	var keepGoing = true;
	while (keepGoing) {
	    final var diff = this.getDifficulty().ordinal();
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

    public abstract void tickTimers();

    public abstract void tickTimers(final int floor, final int actionType);

    public abstract boolean tryRedo();

    public abstract boolean tryUndo();

    public abstract void undo();

    public abstract void updateMonsterPosition(final Direction move, final int xLoc, final int yLoc,
	    final AbstractMovingObject monster, final int pi);

    public abstract void updateRedoHistory(final HistoryStatus whatIs);

    public abstract void updateUndoHistory(final HistoryStatus whatIs);

    public abstract void updateVisibleSquares(final int xp, final int yp, final int zp);

    public abstract void writeDungeon() throws IOException;
}