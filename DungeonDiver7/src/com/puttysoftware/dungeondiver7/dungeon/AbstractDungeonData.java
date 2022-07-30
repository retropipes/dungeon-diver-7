package com.puttysoftware.dungeondiver7.dungeon;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractButton;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractButtonDoor;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractCharacter;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractTunnel;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public abstract class AbstractDungeonData implements Cloneable {
    // Constants
    protected final static int MIN_FLOORS = 1;
    protected final static int MAX_FLOORS = 9;
    protected final static int MIN_COLUMNS = 24;
    protected final static int MIN_ROWS = 24;

    // Static methods
    public static int getMinRows() {
	return AbstractDungeonData.MIN_ROWS;
    }

    public static int getMinColumns() {
	return AbstractDungeonData.MIN_COLUMNS;
    }

    public static int getMaxFloors() {
	return AbstractDungeonData.MAX_FLOORS;
    }

    public static int getMinFloors() {
	return AbstractDungeonData.MIN_FLOORS;
    }

    @Override
    public abstract AbstractDungeonData clone();

    public abstract boolean isCellDirty(final AbstractDungeon dungeon, final int row, final int col, final int floor);

    public abstract AbstractDungeonObject getCell(final AbstractDungeon dungeon, final int row, final int col,
	    final int floor, final int layer);

    public abstract AbstractDungeonObject getVirtualCell(final AbstractDungeon dungeon, final int row, final int col,
	    final int floor, final int layer);

    public abstract int getRows();

    public abstract int getColumns();

    public abstract int getFloors();

    public abstract int[] findPlayer(final AbstractDungeon dungeon, final int number);

    public abstract void tickTimers(final AbstractDungeon dungeon, final int floor, final int actionType);

    public abstract void checkForEnemies(final AbstractDungeon dungeon, final int floorIn, final int enemyLocXIn,
	    final int enemyLocYIn, final AbstractCharacter enemy);

    public abstract int checkForMagnetic(final AbstractDungeon dungeon, final int floor, final int centerX,
	    final int centerY, final Direction dir);

    public abstract boolean linearScan(final AbstractDungeon dungeon, final int xIn, final int yIn, final int zIn,
	    final Direction d);

    public abstract int linearScanMagnetic(final AbstractDungeon dungeon, final int xIn, final int yIn, final int zIn,
	    final Direction d);

    public abstract int[] findObject(final AbstractDungeon dungeon, final int z, final String targetName);

    public abstract int[] circularScan(final AbstractDungeon dungeon, final int xIn, final int yIn, final int zIn,
	    final int r, final String targetName, boolean moved);

    public abstract int[] circularScanTunnel(final AbstractDungeon dungeon, final int x, final int y, final int z,
	    final int maxR, final int tx, final int ty, final AbstractTunnel target, final boolean moved);

    public abstract void circularScanRange(final AbstractDungeon dungeon, final int xIn, final int yIn, final int zIn,
	    final int r, final int rangeType, final int forceUnits);

    public abstract boolean circularScanTank(final AbstractDungeon dungeon, final int x, final int y, final int z,
	    final int r);

    public abstract void fullScanKillTanks(final AbstractDungeon dungeon);

    public abstract void fullScanFreezeGround(final AbstractDungeon dungeon);

    public abstract void fullScanAllButtonOpen(final AbstractDungeon dungeon, final int zIn,
	    final AbstractButton source);

    public abstract void fullScanAllButtonClose(final AbstractDungeon dungeon, final int zIn,
	    final AbstractButton source);

    public abstract void fullScanButtonBind(final AbstractDungeon dungeon, final int dx, final int dy, final int zIn,
	    final AbstractButtonDoor source);

    public abstract void fullScanButtonCleanup(final AbstractDungeon dungeon, final int px, final int py, final int zIn,
	    final AbstractButton button);

    public abstract void fullScanFindButtonLostDoor(final AbstractDungeon dungeon, final int zIn,
	    final AbstractButtonDoor door);

    public abstract void setCell(final AbstractDungeon dungeon, final AbstractDungeonObject mo, final int row,
	    final int col, final int floor, final int layer);

    public abstract void markAsDirty(final AbstractDungeon dungeon, final int row, final int col, final int floor);

    public abstract void setVirtualCell(final AbstractDungeon dungeon, final AbstractDungeonObject mo, final int row,
	    final int col, final int floor, final int layer);

    public abstract void setAllDirtyFlags();

    public abstract void clearDirtyFlags(final int floor);

    public abstract void setDirtyFlags(final int floor);

    public abstract void clearVirtualGrid(final AbstractDungeon dungeon);

    public abstract void fill(final AbstractDungeon dungeon, final AbstractDungeonObject fillWith);

    public abstract void fillVirtual();

    public abstract void save(final AbstractDungeon dungeon);

    public abstract void restore(final AbstractDungeon dungeon);

    public abstract void resize(final AbstractDungeon dungeon, final int zIn, final AbstractDungeonObject nullFill);

    public abstract void resizeSavedState(final int z, final AbstractDungeonObject nullFill);

    public abstract void fillNulls(final AbstractDungeon dungeon, final AbstractDungeonObject fill1,
	    final AbstractDungeonObject fill2, final boolean was16);

    public abstract void fillSTSNulls(final AbstractDungeonObject fillWith);

    protected final int normalizeRow(final int row) {
	int fR = row;
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

    protected final int normalizeColumn(final int column) {
	int fC = column;
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
	int fF = floor;
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

    public abstract void writeData(final AbstractDungeon dungeon, final XDataWriter writer) throws IOException;

    public abstract AbstractDungeonData readData(final AbstractDungeon dungeon, final XDataReader reader, final int ver)
	    throws IOException;

    public abstract void writeSavedState(final XDataWriter writer) throws IOException;

    public abstract void readSavedState(final XDataReader reader, final int formatVersion) throws IOException;

    public abstract void undo(final AbstractDungeon dungeon);

    public abstract void redo(final AbstractDungeon dungeon);

    public abstract boolean tryUndo();

    public abstract boolean tryRedo();

    public abstract void clearUndoHistory();

    public abstract void clearRedoHistory();

    public abstract void updateUndoHistory(final HistoryStatus whatIs);

    public abstract void updateRedoHistory(final HistoryStatus whatIs);

    public abstract HistoryStatus getWhatWas();

    public abstract void resetHistoryEngine();
}
