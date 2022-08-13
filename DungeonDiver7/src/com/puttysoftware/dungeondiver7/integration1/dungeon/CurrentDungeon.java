/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractPrefixIO;
import com.puttysoftware.dungeondiver7.dungeon.AbstractSuffixIO;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractMovingObject;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.dungeon.objects.Tile;
import com.puttysoftware.dungeondiver7.integration1.VersionException;
import com.puttysoftware.dungeondiver7.manager.dungeon.FormatConstants;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.DirectionResolver;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;
import com.puttysoftware.fileio.XDataReader;
import com.puttysoftware.fileio.XDataWriter;
import com.puttysoftware.randomrange.RandomLongRange;

public class CurrentDungeon {
    // Properties
    private CurrentDungeonData dungeonData;
    private int startLevel;
    private int levelCount;
    private int activeLevel;
    private String basePath;
    private AbstractPrefixIO prefixHandler;
    private AbstractSuffixIO suffixHandler;
    private static final int MIN_LEVELS = 1;
    private static final int MAX_LEVELS = 13;

    // Constructors
    public CurrentDungeon() {
	this.dungeonData = null;
	this.levelCount = 0;
	this.startLevel = 0;
	this.activeLevel = 0;
	final long random = new RandomLongRange(0, Long.MAX_VALUE).generate();
	final String randomID = Long.toHexString(random);
	this.basePath = System.getProperty("java.io.tmpdir") + File.separator + "Chrystalz" + File.separator + randomID
		+ ".maze";
	final File base = new File(this.basePath);
	final boolean success = base.mkdirs();
	if (!success) {
	    CommonDialogs.showErrorDialog("Dungeon temporary folder creation failed!", "Chrystalz");
	}
    }

    // Static methods
    public static String getDungeonTempFolder() {
	return System.getProperty("java.io.tmpdir") + File.separator + "Chrystalz";
    }

    public static int getMinLevels() {
	return CurrentDungeon.MIN_LEVELS;
    }

    public static int getMaxLevels() {
	return CurrentDungeon.MAX_LEVELS;
    }

    public static int getMaxColumns() {
	return CurrentDungeonData.getMaxColumns();
    }

    public static int getMaxRows() {
	return CurrentDungeonData.getMaxRows();
    }

    // Methods
    public static CurrentDungeon getTemporaryBattleCopy() {
	final CurrentDungeon temp = new CurrentDungeon();
	temp.addLevel(DungeonDiver7.getBattleDungeonSize(), DungeonDiver7.getBattleDungeonSize());
	temp.fill(new Tile(), new Empty());
	return temp;
    }

    public Direction computeFinalBossMoveDirection(final int locX, final int locY, final int locZ) {
	int px = this.getPlayerLocationX();
	int py = this.getPlayerLocationY();
	int relX = px - locX;
	int relY = py - locY;
	int moveX = 0;
	int moveY = 0;
	if (relX != 0) {
	    moveX = relX / Math.abs(relX);
	}
	if (relY != 0) {
	    moveY = relY / Math.abs(relY);
	}
	boolean canMove = !this.getCell(locX + moveX, locY + moveY, locZ, DungeonConstants.LAYER_LOWER_OBJECTS)
		.isSolid();
	if (canMove) {
	    return DirectionResolver.resolveRelativeDirection(moveX, moveY);
	}
	int moveX1L = DirectionResolver.rotate45LeftX(moveX, moveY);
	int moveY1L = DirectionResolver.rotate45LeftY(moveX, moveY);
	boolean canMove1L = !this.getCell(locX + moveX1L, locY + moveY1L, locZ, DungeonConstants.LAYER_LOWER_OBJECTS)
		.isSolid();
	if (canMove1L) {
	    return DirectionResolver.resolveRelativeDirection(moveX1L, moveY1L);
	}
	int moveX1R = DirectionResolver.rotate45RightX(moveX, moveY);
	int moveY1R = DirectionResolver.rotate45RightY(moveX, moveY);
	boolean canMove1R = !this.getCell(locX + moveX1R, locY + moveY1R, locZ, DungeonConstants.LAYER_LOWER_OBJECTS)
		.isSolid();
	if (canMove1R) {
	    return DirectionResolver.resolveRelativeDirection(moveX1R, moveY1R);
	}
	int moveX2L = DirectionResolver.rotate45LeftX(moveX1L, moveY1L);
	int moveY2L = DirectionResolver.rotate45LeftY(moveX1L, moveY1L);
	boolean canMove2L = !this.getCell(locX + moveX2L, locY + moveY2L, locZ, DungeonConstants.LAYER_LOWER_OBJECTS)
		.isSolid();
	if (canMove2L) {
	    return DirectionResolver.resolveRelativeDirection(moveX2L, moveY2L);
	}
	int moveX2R = DirectionResolver.rotate45RightX(moveX1R, moveY1R);
	int moveY2R = DirectionResolver.rotate45RightY(moveX1R, moveY1R);
	boolean canMove2R = !this.getCell(locX + moveX2R, locY + moveY2R, locZ, DungeonConstants.LAYER_LOWER_OBJECTS)
		.isSolid();
	if (canMove2R) {
	    return DirectionResolver.resolveRelativeDirection(moveX2R, moveY2R);
	}
	int moveX3L = DirectionResolver.rotate45LeftX(moveX2L, moveY2L);
	int moveY3L = DirectionResolver.rotate45LeftY(moveX2L, moveY2L);
	boolean canMove3L = !this.getCell(locX + moveX3L, locY + moveY3L, locZ, DungeonConstants.LAYER_LOWER_OBJECTS)
		.isSolid();
	if (canMove3L) {
	    return DirectionResolver.resolveRelativeDirection(moveX3L, moveY3L);
	}
	int moveX3R = DirectionResolver.rotate45RightX(moveX2R, moveY2R);
	int moveY3R = DirectionResolver.rotate45RightY(moveX2R, moveY2R);
	boolean canMove3R = !this.getCell(locX + moveX3R, locY + moveY3R, locZ, DungeonConstants.LAYER_LOWER_OBJECTS)
		.isSolid();
	if (canMove3R) {
	    return DirectionResolver.resolveRelativeDirection(moveX3R, moveY3R);
	}
	int moveX4 = DirectionResolver.rotate45LeftX(moveX3L, moveY3L);
	int moveY4 = DirectionResolver.rotate45LeftY(moveX3L, moveY3L);
	return DirectionResolver.resolveRelativeDirection(moveX4, moveY4);
    }

    public void updateMonsterPosition(final Direction move, final int xLoc, final int yLoc,
	    final AbstractMovingObject monster) {
	this.dungeonData.updateMonsterPosition(move, xLoc, yLoc, monster);
    }

    public void postBattle(final AbstractMovingObject m, final int xLoc, final int yLoc, final boolean player) {
	this.dungeonData.postBattle(m, xLoc, yLoc, player);
    }

    public String getBasePath() {
	return this.basePath;
    }

    public void setPrefixHandler(final AbstractPrefixIO xph) {
	this.prefixHandler = xph;
    }

    public void setSuffixHandler(final AbstractSuffixIO xsh) {
	this.suffixHandler = xsh;
    }

    public void tickTimers() {
	this.dungeonData.tickTimers();
    }

    public void resetVisibleSquares() {
	this.dungeonData.resetVisibleSquares();
    }

    public void updateVisibleSquares(final int xp, final int yp) {
	this.dungeonData.updateVisibleSquares(xp, yp);
    }

    public void switchLevel(final int level) {
	this.switchLevelInternal(level);
    }

    public void switchLevelOffset(final int level) {
	this.switchLevelInternal(this.activeLevel + level);
    }

    private void switchLevelInternal(final int level) {
	if (this.activeLevel != level) {
	    this.activeLevel = level;
	}
    }

    public boolean doesLevelExistOffset(final int level) {
	return this.activeLevel + level < this.levelCount && this.activeLevel + level >= 0;
    }

    public int getActiveLevel() {
	return this.activeLevel;
    }

    public boolean addLevel(final int rows, final int cols) {
	this.dungeonData = new CurrentDungeonData(rows, cols);
	this.levelCount++;
	this.activeLevel = this.levelCount - 1;
	return true;
    }

    public AbstractDungeonObject getCell(final int row, final int col, int floor, final int extra) {
	return this.dungeonData.getCell(row, col, 0, extra);
    }

    public int getPlayerLocationX() {
	return this.dungeonData.getPlayerLocationX();
    }

    public int getPlayerLocationY() {
	return this.dungeonData.getPlayerLocationY();
    }

    public int getStartLevel() {
	return this.startLevel;
    }

    public int getRows() {
	return this.dungeonData.getRows();
    }

    public int getColumns() {
	return this.dungeonData.getColumns();
    }

    public boolean doesPlayerExist() {
	return this.dungeonData.doesPlayerExist();
    }

    public boolean isSquareVisible(final int x1, final int y1, final int x2, final int y2) {
	return this.dungeonData.isSquareVisible(x1, y1, x2, y2);
    }

    public void setCell(final AbstractDungeonObject mo, final int row, final int col, int floor, final int extra) {
	this.dungeonData.setCell(mo, row, col, 0, extra);
    }

    public void setStartRow(final int newStartRow) {
	this.dungeonData.setStartRow(newStartRow);
    }

    public void setStartColumn(final int newStartColumn) {
	this.dungeonData.setStartColumn(newStartColumn);
    }

    public void savePlayerLocation() {
	this.dungeonData.savePlayerLocation();
    }

    public void restorePlayerLocation() {
	this.dungeonData.restorePlayerLocation();
    }

    public void setPlayerToStart() {
	this.dungeonData.setPlayerToStart();
    }

    public void setPlayerLocationX(final int newPlayerRow) {
	this.dungeonData.setPlayerRow(newPlayerRow);
    }

    public void setPlayerLocationY(final int newPlayerColumn) {
	this.dungeonData.setPlayerColumn(newPlayerColumn);
    }

    public void offsetPlayerLocationX(final int newPlayerRow) {
	this.dungeonData.offsetPlayerRow(newPlayerRow);
    }

    public void offsetPlayerLocationY(final int newPlayerColumn) {
	this.dungeonData.offsetPlayerColumn(newPlayerColumn);
    }

    public void fillRandomly() {
	this.dungeonData.fillRandomly(this, this.activeLevel);
    }

    public void save() {
	this.dungeonData.save();
    }

    public void restore() {
	this.dungeonData.restore();
    }

    private void fill(final AbstractDungeonObject bottom, final AbstractDungeonObject top) {
	this.dungeonData.fill(bottom, top);
    }

    public CurrentDungeon readDungeon() throws IOException {
	final CurrentDungeon m = new CurrentDungeon();
	// Attach handlers
	m.setPrefixHandler(this.prefixHandler);
	m.setSuffixHandler(this.suffixHandler);
	// Make base paths the same
	m.basePath = this.basePath;
	int version = 0;
	// Create metafile reader
	try (FileIOReader metaReader = new XDataReader(m.basePath + File.separator + "metafile.xml", "maze")) {
	    // Read metafile
	    version = m.readDungeonMetafile(metaReader);
	} catch (final IOException ioe) {
	    throw ioe;
	}
	// Create data reader
	try (FileIOReader dataReader = m.getLevelReader()) {
	    // Read data
	    m.readDungeonLevel(dataReader, version);
	    return m;
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    private FileIOReader getLevelReader() throws IOException {
	return new XDataReader(this.basePath + File.separator + "level" + this.activeLevel + ".xml", "level");
    }

    private int readDungeonMetafile(final FileIOReader reader) throws IOException {
	int ver = FormatConstants.MAZE_FORMAT_LATEST;
	if (this.prefixHandler != null) {
	    ver = this.prefixHandler.readPrefix(reader);
	}
	this.levelCount = reader.readInt();
	this.startLevel = reader.readInt();
	this.activeLevel = reader.readInt();
	if (this.suffixHandler != null) {
	    this.suffixHandler.readSuffix(reader, ver);
	}
	return ver;
    }

    private void readDungeonLevel(final FileIOReader reader, final int formatVersion) throws IOException {
	if (formatVersion == FormatConstants.MAZE_FORMAT_LATEST) {
	    this.dungeonData = CurrentDungeonData.readLayeredTowerV1(reader);
	    this.dungeonData.readSavedTowerState(reader, formatVersion);
	} else {
	    throw new VersionException("Unknown maze format version: " + formatVersion + "!");
	}
    }

    public void writeDungeon() throws IOException {
	try {
	    // Create metafile writer
	    try (FileIOWriter metaWriter = new XDataWriter(this.basePath + File.separator + "metafile.xml", "maze")) {
		// Write metafile
		this.writeDungeonMetafile(metaWriter);
	    }
	    // Create data writer
	    try (FileIOWriter dataWriter = this.getLevelWriter()) {
		// Write data
		this.writeDungeonLevel(dataWriter);
	    }
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    private FileIOWriter getLevelWriter() throws IOException {
	return new XDataWriter(this.basePath + File.separator + "level" + this.activeLevel + ".xml", "level");
    }

    private void writeDungeonMetafile(final FileIOWriter writer) throws IOException {
	if (this.prefixHandler != null) {
	    this.prefixHandler.writePrefix(writer);
	}
	writer.writeInt(this.levelCount);
	writer.writeInt(this.startLevel);
	writer.writeInt(this.activeLevel);
	if (this.suffixHandler != null) {
	    this.suffixHandler.writeSuffix(writer);
	}
    }

    private void writeDungeonLevel(final FileIOWriter writer) throws IOException {
	// Write the level
	this.dungeonData.writeLayeredTower(writer);
	this.dungeonData.writeSavedTowerState(writer);
    }
}
