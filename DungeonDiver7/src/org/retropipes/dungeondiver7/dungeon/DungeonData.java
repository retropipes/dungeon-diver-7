package org.retropipes.dungeondiver7.dungeon;

import java.io.IOException;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractButton;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractButtonDoor;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractCharacter;
import org.retropipes.dungeondiver7.dungeon.abc.DungeonObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovingObject;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractTunnel;

public abstract class DungeonData implements Cloneable {
    // Constants
    protected final static int MIN_FLOORS = 1;
    protected final static int MAX_FLOORS = 9;
    protected final static int MIN_COLUMNS = 24;
    protected final static int MIN_ROWS = 24;

    public static int getMaxFloors() {
	return DungeonData.MAX_FLOORS;
    }

    public static int getMinColumns() {
	return DungeonData.MIN_COLUMNS;
    }

    public static int getMinFloors() {
	return DungeonData.MIN_FLOORS;
    }

    // Static methods
    public static int getMinRows() {
	return DungeonData.MIN_ROWS;
    }

    public abstract void checkForEnemies(final Dungeon dungeon, final int floorIn, final int enemyLocXIn,
	    final int enemyLocYIn, final AbstractCharacter enemy);

    public abstract int checkForMagnetic(final Dungeon dungeon, final int floor, final int centerX,
	    final int centerY, final Direction dir);

    public abstract int[] circularScan(final Dungeon dungeon, final int xIn, final int yIn, final int zIn,
	    final int r, final String targetName, boolean moved);

    public abstract boolean circularScanPlayer(final Dungeon dungeon, final int x, final int y, final int z,
	    final int r);

    public abstract void circularScanRange(final Dungeon dungeon, final int xIn, final int yIn, final int zIn,
	    final int r, final int rangeType, final int forceUnits);

    public abstract int[] circularScanTunnel(final Dungeon dungeon, final int x, final int y, final int z,
	    final int maxR, final int tx, final int ty, final AbstractTunnel target, final boolean moved);

    public abstract void clearDirtyFlags(final int floor);

    public abstract void clearRedoHistory();

    public abstract void clearUndoHistory();

    public abstract void clearVirtualGrid(final Dungeon dungeon);

    @Override
    public abstract DungeonData clone();

    public abstract void fill(final Dungeon dungeon, final DungeonObject fillWith);

    public abstract void fillNulls(final Dungeon dungeon, final DungeonObject fill1,
	    final DungeonObject fill2, final boolean was16);

    public abstract void fillSTSNulls(final DungeonObject fillWith);

    public abstract void fillVirtual();

    public abstract int[] findObject(final Dungeon dungeon, final int z, final String targetName);

    public abstract int[] findPlayer(final Dungeon dungeon, final int number);

    public abstract void fullScanAllButtonClose(final Dungeon dungeon, final int zIn,
	    final AbstractButton source);

    public abstract void fullScanAllButtonOpen(final Dungeon dungeon, final int zIn,
	    final AbstractButton source);

    public abstract void fullScanButtonBind(final Dungeon dungeon, final int dx, final int dy, final int zIn,
	    final AbstractButtonDoor source);

    public abstract void fullScanButtonCleanup(final Dungeon dungeon, final int px, final int py, final int zIn,
	    final AbstractButton button);

    public abstract void fullScanFindButtonLostDoor(final Dungeon dungeon, final int zIn,
	    final AbstractButtonDoor door);

    public abstract void fullScanFreezeGround(final Dungeon dungeon);

    public abstract void fullScanKillPlayers(final Dungeon dungeon);

    public abstract DungeonObject getCell(final Dungeon dungeon, final int row, final int col,
	    final int floor, final int layer);

    public abstract int getColumns();

    public abstract int getFloors();

    public abstract int getRows();

    public abstract DungeonObject getVirtualCell(final Dungeon dungeon, final int row, final int col,
	    final int floor, final int layer);

    public abstract HistoryStatus getWhatWas();

    public abstract boolean isCellDirty(final Dungeon dungeon, final int row, final int col, final int floor);

    public abstract boolean isSquareVisible(final Dungeon dungeon, final int x1, final int y1, final int x2,
	    final int y2, final int zp);

    public abstract boolean linearScan(final Dungeon dungeon, final int xIn, final int yIn, final int zIn,
	    final Direction d);

    public abstract int linearScanMagnetic(final Dungeon dungeon, final int xIn, final int yIn, final int zIn,
	    final Direction d);

    public abstract void markAsDirty(final Dungeon dungeon, final int row, final int col, final int floor);

    protected final int normalizeColumn(final int column) {
	var fC = column;
	if (fC < 0) {
	    fC += this.getColumns();
	    while (fC < 0) {
		fC += this.getColumns();
	    }
	} else if (fC > this.getColumns() - 1) {
	    fC -= this.getColumns();
	    while (fC > this.getColumns() - 1) {
		fC -= this.getColumns();
	    }
	}
	return fC;
    }

    protected final int normalizeFloor(final int floor) {
	var fF = floor;
	if (fF < 0) {
	    fF += this.getFloors();
	    while (fF < 0) {
		fF += this.getFloors();
	    }
	} else if (fF > this.getFloors() - 1) {
	    fF -= this.getFloors();
	    while (fF > this.getFloors() - 1) {
		fF -= this.getFloors();
	    }
	}
	return fF;
    }

    protected final int normalizeRow(final int row) {
	var fR = row;
	if (fR < 0) {
	    fR += this.getRows();
	    while (fR < 0) {
		fR += this.getRows();
	    }
	} else if (fR > this.getRows() - 1) {
	    fR -= this.getRows();
	    while (fR > this.getRows() - 1) {
		fR -= this.getRows();
	    }
	}
	return fR;
    }

    public abstract void postBattle(final Dungeon dungeon, final AbstractMovingObject m, final int xLoc,
	    final int yLoc, final boolean player);

    public abstract DungeonData readData(final Dungeon dungeon, final DataIOReader reader,
	    final int ver) throws IOException;

    public abstract void readSavedState(final DataIOReader reader, final int formatVersion) throws IOException;

    public abstract void redo(final Dungeon dungeon);

    public abstract void resetHistoryEngine();

    public abstract void resetVisibleSquares(final int floor);

    public abstract void resize(final Dungeon dungeon, final int zIn, final DungeonObject nullFill);

    public abstract void resizeSavedState(final int z, final DungeonObject nullFill);

    public abstract void restore(final Dungeon dungeon);

    public abstract void save(final Dungeon dungeon);

    public abstract void setAllDirtyFlags();

    public abstract void setCell(final Dungeon dungeon, final DungeonObject mo, final int row,
	    final int col, final int floor, final int layer);

    public abstract void setDirtyFlags(final int floor);

    public abstract void setVirtualCell(final Dungeon dungeon, final DungeonObject mo, final int row,
	    final int col, final int floor, final int layer);

    public abstract void tickTimers(final Dungeon dungeon);

    public abstract void tickTimers(final Dungeon dungeon, final int floor, final int actionType);

    public abstract boolean tryRedo();

    public abstract boolean tryUndo();

    public abstract void undo(final Dungeon dungeon);

    public abstract void updateMonsterPosition(final Dungeon dungeon, final Direction move, final int xLoc,
	    final int yLoc, final AbstractMovingObject monster, final int pi);

    public abstract void updateRedoHistory(final HistoryStatus whatIs);

    public abstract void updateUndoHistory(final HistoryStatus whatIs);

    public abstract void updateVisibleSquares(final Dungeon dungeon, final int xp, final int yp, final int zp);

    public abstract void writeData(final Dungeon dungeon, final DataIOWriter writer) throws IOException;

    public abstract void writeSavedState(final DataIOWriter writer) throws IOException;
}
