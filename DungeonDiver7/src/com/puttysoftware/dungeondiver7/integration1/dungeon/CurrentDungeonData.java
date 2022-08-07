/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon;

import java.io.IOException;
import java.util.Arrays;

import com.puttysoftware.dungeondiver7.dungeon.utility.DirectionResolver;
import com.puttysoftware.dungeondiver7.dungeon.utility.GameObjectList;
import com.puttysoftware.dungeondiver7.dungeon.utility.RandomGenerationRule;
import com.puttysoftware.dungeondiver7.integration1.Application;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractMovingObject;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.BossMonsterTile;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.FinalBossMonsterTile;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.MonsterTile;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.Tile;
import com.puttysoftware.dungeondiver7.manager.dungeon.FormatConstants;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.VisionModeConstants;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;
import com.puttysoftware.randomrange.RandomRange;
import com.puttysoftware.storage.FlagStorage;

final class CurrentDungeonData implements Cloneable {
    // Properties
    private CurrentDungeonStorage data;
    private CurrentDungeonStorage savedTowerState;
    private FlagStorage visionData;
    private final int[] playerStartData;
    private final int[] playerLocationData;
    private final int[] savedPlayerLocationData;
    private final int[] findResult;
    private boolean horizontalWraparoundEnabled;
    private boolean verticalWraparoundEnabled;
    private int visionMode;
    private int visionModeExploreRadius;
    private int visionRadius;
    private int initialVisionRadius;
    private int regionSize;
    private static final int MAX_VISION_RADIUS = 16;
    private static final int MAX_COLUMNS = 250;
    private static final int MAX_ROWS = 250;

    // Constructors
    public CurrentDungeonData(final int rows, final int cols) {
	this.data = new CurrentDungeonStorage(cols, rows, DungeonConstants.NUM_LAYERS);
	this.savedTowerState = new CurrentDungeonStorage(cols, rows, DungeonConstants.NUM_LAYERS);
	this.visionData = new FlagStorage(cols, rows);
	this.playerStartData = new int[2];
	Arrays.fill(this.playerStartData, -1);
	this.playerLocationData = new int[2];
	Arrays.fill(this.playerLocationData, -1);
	this.savedPlayerLocationData = new int[2];
	Arrays.fill(this.savedPlayerLocationData, -1);
	this.findResult = new int[2];
	Arrays.fill(this.findResult, -1);
	this.horizontalWraparoundEnabled = false;
	this.verticalWraparoundEnabled = false;
	this.visionMode = VisionModeConstants.VISION_MODE_EXPLORE_AND_LOS;
	this.visionModeExploreRadius = 2;
	this.visionRadius = CurrentDungeonData.MAX_VISION_RADIUS;
	this.regionSize = 8;
    }

    // Static methods
    public static int getMaxColumns() {
	return CurrentDungeonData.MAX_COLUMNS;
    }

    public static int getMaxRows() {
	return CurrentDungeonData.MAX_ROWS;
    }

    // Methods
    @Override
    public CurrentDungeonData clone() {
	final CurrentDungeonData copy = new CurrentDungeonData(this.getRows(), this.getColumns());
	copy.data = this.data.clone();
	copy.visionData = new FlagStorage(this.visionData);
	copy.savedTowerState = this.savedTowerState.clone();
	System.arraycopy(this.playerStartData, 0, copy.playerStartData, 0, this.playerStartData.length);
	System.arraycopy(this.findResult, 0, copy.findResult, 0, this.findResult.length);
	copy.horizontalWraparoundEnabled = this.horizontalWraparoundEnabled;
	copy.verticalWraparoundEnabled = this.verticalWraparoundEnabled;
	return copy;
    }

    public void updateMonsterPosition(final int move, final int xLoc, final int yLoc,
	    final AbstractMovingObject monster) {
	final Application app = Integration1.getApplication();
	final int[] dirMove = DirectionResolver.unresolveRelativeDirection(move);
	final int pLocX = this.getPlayerRow();
	final int pLocY = this.getPlayerColumn();
	try {
	    final AbstractDungeonObject there = this.getCell(xLoc + dirMove[0], yLoc + dirMove[1],
		    DungeonConstants.LAYER_LOWER_OBJECTS);
	    final AbstractDungeonObject ground = this.getCell(xLoc + dirMove[0], yLoc + dirMove[1],
		    DungeonConstants.LAYER_LOWER_GROUND);
	    if (!there.isSolid() && !(there instanceof AbstractMovingObject)) {
		if (CurrentDungeonData.radialScan(xLoc, yLoc, 0, pLocX, pLocY)) {
		    if (app.getMode() != Application.STATUS_BATTLE) {
			app.getGameLogic().stopMovement();
			if (monster instanceof FinalBossMonsterTile) {
			    app.getBattle().doFinalBossBattle();
			} else if (monster instanceof BossMonsterTile) {
			    app.getBattle().doBossBattle();
			} else {
			    app.getBattle().doBattle();
			    this.postBattle(monster, xLoc, yLoc, false);
			}
		    }
		} else {
		    // Move the monster
		    this.setCell(monster.getSavedObject(), xLoc, yLoc, DungeonConstants.LAYER_LOWER_OBJECTS);
		    monster.setSavedObject(there);
		    this.setCell(monster, xLoc + dirMove[0], yLoc + dirMove[1], DungeonConstants.LAYER_LOWER_OBJECTS);
		    // Does the ground have friction?
		    if (!ground.hasFriction()) {
			// No - move the monster again
			this.updateMonsterPosition(move, xLoc + dirMove[0], yLoc + dirMove[1], monster);
		    }
		}
	    }
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Do nothing
	}
    }

    public void postBattle(final AbstractMovingObject m, final int xLoc, final int yLoc, final boolean player) {
	final AbstractDungeonObject saved = m.getSavedObject();
	if (!player) {
	    this.setCell(saved, xLoc, yLoc, DungeonConstants.LAYER_LOWER_OBJECTS);
	}
	this.generateOneMonster();
    }

    private void generateOneMonster() {
	final RandomRange row = new RandomRange(0, this.getRows() - 1);
	final RandomRange column = new RandomRange(0, this.getColumns() - 1);
	int randomRow, randomColumn;
	randomRow = row.generate();
	randomColumn = column.generate();
	AbstractDungeonObject currObj = this.getCell(randomRow, randomColumn, DungeonConstants.LAYER_LOWER_OBJECTS);
	if (!currObj.isSolid()) {
	    final AbstractMovingObject m = new MonsterTile();
	    m.setSavedObject(currObj);
	    this.setCell(m, randomRow, randomColumn, DungeonConstants.LAYER_LOWER_OBJECTS);
	} else {
	    while (currObj.isSolid()) {
		randomRow = row.generate();
		randomColumn = column.generate();
		currObj = this.getCell(randomRow, randomColumn, DungeonConstants.LAYER_LOWER_OBJECTS);
	    }
	    final AbstractMovingObject m = new MonsterTile();
	    m.setSavedObject(currObj);
	    this.setCell(m, randomRow, randomColumn, DungeonConstants.LAYER_LOWER_OBJECTS);
	}
    }

    public AbstractDungeonObject getCell(final int row, final int col, final int extra) {
	int fR = row;
	int fC = col;
	if (this.verticalWraparoundEnabled) {
	    fC = this.normalizeColumn(fC);
	}
	if (this.horizontalWraparoundEnabled) {
	    fR = this.normalizeRow(fR);
	}
	return this.data.getCell(fC, fR, extra);
    }

    public int getPlayerRow() {
	return this.playerLocationData[1];
    }

    public int getPlayerColumn() {
	return this.playerLocationData[0];
    }

    public int getRows() {
	return this.data.getShape()[1];
    }

    public int getColumns() {
	return this.data.getShape()[0];
    }

    public boolean doesPlayerExist() {
	boolean res = true;
	for (final int element : this.playerStartData) {
	    res = res && element != -1;
	}
	return res;
    }

    public void resetVisibleSquares() {
	for (int x = 0; x < this.getRows(); x++) {
	    for (int y = 0; y < this.getColumns(); y++) {
		this.visionData.setCell(false, x, y);
	    }
	}
    }

    public void updateVisibleSquares(final int xp, final int yp) {
	if ((this.visionMode | VisionModeConstants.VISION_MODE_EXPLORE) == this.visionMode) {
	    for (int x = xp - this.visionModeExploreRadius; x <= xp + this.visionModeExploreRadius; x++) {
		for (int y = yp - this.visionModeExploreRadius; y <= yp + this.visionModeExploreRadius; y++) {
		    int fx, fy;
		    if (this.isHorizontalWraparoundEnabled()) {
			fx = this.normalizeColumn(x);
		    } else {
			fx = x;
		    }
		    if (this.isVerticalWraparoundEnabled()) {
			fy = this.normalizeRow(y);
		    } else {
			fy = y;
		    }
		    boolean alreadyVisible = false;
		    try {
			alreadyVisible = this.visionData.getCell(fx, fy);
		    } catch (final ArrayIndexOutOfBoundsException aioobe) {
			// Ignore
		    }
		    if (!alreadyVisible) {
			if ((this.visionMode | VisionModeConstants.VISION_MODE_LOS) == this.visionMode) {
			    if (this.isSquareVisibleLOS(x, y, xp, yp)) {
				try {
				    this.visionData.setCell(true, fx, fy);
				} catch (final ArrayIndexOutOfBoundsException aioobe) {
				    // Ignore
				}
			    }
			} else {
			    try {
				this.visionData.setCell(true, fx, fy);
			    } catch (final ArrayIndexOutOfBoundsException aioobe) {
				// Ignore
			    }
			}
		    }
		}
	    }
	}
    }

    public boolean isSquareVisible(final int x1, final int y1, final int x2, final int y2) {
	if (this.visionMode == VisionModeConstants.VISION_MODE_NONE) {
	    return true;
	} else {
	    boolean result = false;
	    if ((this.visionMode | VisionModeConstants.VISION_MODE_EXPLORE) == this.visionMode) {
		result = result || this.isSquareVisibleExplore(x2, y2);
		if (result && (this.visionMode | VisionModeConstants.VISION_MODE_LOS) == this.visionMode) {
		    if (this.areCoordsInBounds(x1, y1, x2, y2)) {
			// In bounds
			result = result || this.isSquareVisibleLOS(x1, y1, x2, y2);
		    } else {
			// Out of bounds
			result = result && this.isSquareVisibleLOS(x1, y1, x2, y2);
		    }
		}
	    } else {
		if (this.areCoordsInBounds(x1, y1, x2, y2)) {
		    // In bounds
		    result = result || this.isSquareVisibleLOS(x1, y1, x2, y2);
		} else {
		    // Out of bounds
		    result = result && this.isSquareVisibleLOS(x1, y1, x2, y2);
		}
	    }
	    return result;
	}
    }

    private boolean areCoordsInBounds(final int x1, final int y1, final int x2, final int y2) {
	int fx1, fx2, fy1, fy2;
	if (this.isHorizontalWraparoundEnabled()) {
	    fx1 = this.normalizeColumn(x1);
	    fx2 = this.normalizeColumn(x2);
	} else {
	    fx1 = x1;
	    fx2 = x2;
	}
	if (this.isVerticalWraparoundEnabled()) {
	    fy1 = this.normalizeRow(y1);
	    fy2 = this.normalizeRow(y2);
	} else {
	    fy1 = y1;
	    fy2 = y2;
	}
	return fx1 >= 0 && fx1 <= this.getRows() && fx2 >= 0 && fx2 <= this.getRows() && fy1 >= 0
		&& fy1 <= this.getColumns() && fy2 >= 0 && fy2 <= this.getColumns();
    }

    private boolean isSquareVisibleExplore(final int x2, final int y2) {
	int fx2, fy2;
	if (this.isHorizontalWraparoundEnabled()) {
	    fx2 = this.normalizeColumn(x2);
	} else {
	    fx2 = x2;
	}
	if (this.isVerticalWraparoundEnabled()) {
	    fy2 = this.normalizeRow(y2);
	} else {
	    fy2 = y2;
	}
	try {
	    return this.visionData.getCell(fx2, fy2);
	} catch (final ArrayIndexOutOfBoundsException aioobe) {
	    return true;
	}
    }

    private boolean isSquareVisibleLOS(final int x1, final int y1, final int x2, final int y2) {
	int fx1, fx2, fy1, fy2;
	fx1 = x1;
	fx2 = x2;
	fy1 = y1;
	fy2 = y2;
	final int dx = Math.abs(fx2 - fx1);
	final int dy = Math.abs(fy2 - fy1);
	int sx, sy;
	if (fx1 < fx2) {
	    sx = 1;
	} else {
	    sx = -1;
	}
	if (fy1 < fy2) {
	    sy = 1;
	} else {
	    sy = -1;
	}
	int err = dx - dy;
	int e2;
	do {
	    if (fx1 == fx2 && fy1 == fy2) {
		break;
	    }
	    // Does object block LOS?
	    try {
		final AbstractDungeonObject obj = this.getCell(fx1, fy1, DungeonConstants.LAYER_LOWER_OBJECTS);
		if (obj.isSightBlocking()) {
		    // This object blocks LOS
		    if (fx1 != x1 || fy1 != y1) {
			return false;
		    }
		}
	    } catch (final ArrayIndexOutOfBoundsException aioobe) {
		// Void blocks LOS
		return false;
	    }
	    e2 = 2 * err;
	    if (e2 > -dy) {
		err = err - dy;
		fx1 = fx1 + sx;
	    }
	    if (e2 < dx) {
		err = err + dx;
		fy1 = fy1 + sy;
	    }
	} while (true);
	// No objects block LOS
	return true;
    }

    public void setCell(final AbstractDungeonObject mo, final int row, final int col, final int extra) {
	int fR = row;
	int fC = col;
	if (this.verticalWraparoundEnabled) {
	    fC = this.normalizeColumn(fC);
	}
	if (this.horizontalWraparoundEnabled) {
	    fR = this.normalizeRow(fR);
	}
	this.data.setCell(mo, fC, fR, extra);
    }

    public void savePlayerLocation() {
	System.arraycopy(this.playerLocationData, 0, this.savedPlayerLocationData, 0, this.playerLocationData.length);
    }

    public void restorePlayerLocation() {
	System.arraycopy(this.savedPlayerLocationData, 0, this.playerLocationData, 0, this.playerLocationData.length);
    }

    public void setPlayerToStart() {
	System.arraycopy(this.playerStartData, 0, this.playerLocationData, 0, this.playerStartData.length);
    }

    public void setStartRow(final int newStartRow) {
	this.playerStartData[1] = newStartRow;
    }

    public void setStartColumn(final int newStartColumn) {
	this.playerStartData[0] = newStartColumn;
    }

    public void setPlayerRow(final int newPlayerRow) {
	this.playerLocationData[1] = newPlayerRow;
    }

    public void setPlayerColumn(final int newPlayerColumn) {
	this.playerLocationData[0] = newPlayerColumn;
    }

    public void offsetPlayerRow(final int newPlayerRow) {
	this.playerLocationData[1] += newPlayerRow;
    }

    public void offsetPlayerColumn(final int newPlayerColumn) {
	this.playerLocationData[0] += newPlayerColumn;
    }

    public void fill(final AbstractDungeonObject bottom, final AbstractDungeonObject top) {
	int x, y, e;
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (e = 0; e < DungeonConstants.NUM_LAYERS; e++) {
		    if (e == DungeonConstants.LAYER_LOWER_GROUND) {
			this.setCell(bottom, y, x, e);
		    } else {
			this.setCell(top, y, x, e);
		    }
		}
	    }
	}
    }

    public void fillRandomly(final CurrentDungeon dungeon, final int w) {
	// Pre-Pass
	final GameObjectList objects = Integration1.getApplication().getObjects();
	final AbstractDungeonObject pass1FillBottom = new Tile();
	final AbstractDungeonObject pass1FillTop = new Empty();
	RandomRange r = null;
	int x, y, e;
	// Pass 1
	this.fill(pass1FillBottom, pass1FillTop);
	// Pass 2
	final int columns = this.getColumns();
	final int rows = this.getRows();
	for (e = 0; e < DungeonConstants.NUM_LAYERS; e++) {
	    final AbstractDungeonObject[] objectsWithoutPrerequisites = objects
		    .getAllWithoutPrerequisiteAndNotRequired(dungeon, e);
	    if (objectsWithoutPrerequisites != null) {
		r = new RandomRange(0, objectsWithoutPrerequisites.length - 1);
		for (x = 0; x < columns; x++) {
		    for (y = 0; y < rows; y++) {
			final AbstractDungeonObject placeObj = objectsWithoutPrerequisites[r.generate()];
			final boolean okay = placeObj.shouldGenerateObject(dungeon, x, y, w, e);
			if (okay) {
			    this.setCell(objects.getNewInstanceByName(placeObj.getName()), y, x, e);
			    placeObj.editorGenerateHook(y, x);
			}
		    }
		}
	    }
	}
	// Pass 3
	for (int layer = 0; layer < DungeonConstants.NUM_LAYERS; layer++) {
	    final AbstractDungeonObject[] requiredObjects = objects.getAllRequired(dungeon, layer);
	    if (requiredObjects != null) {
		final RandomRange row = new RandomRange(0, this.getRows() - 1);
		final RandomRange column = new RandomRange(0, this.getColumns() - 1);
		int randomColumn, randomRow;
		for (x = 0; x < requiredObjects.length; x++) {
		    final AbstractDungeonObject currObj = requiredObjects[x];
		    final int min = currObj.getMinimumRequiredQuantity(dungeon);
		    int max = currObj.getMaximumRequiredQuantity(dungeon);
		    if (max == RandomGenerationRule.NO_LIMIT) {
			// Maximum undefined, so define it relative to this
			// dungeon
			max = this.getRows() * this.getColumns() / 10;
			// Make sure max is valid
			if (max < min) {
			    max = min;
			}
		    }
		    final RandomRange howMany = new RandomRange(min, max);
		    final int generateHowMany = howMany.generate();
		    for (y = 0; y < generateHowMany; y++) {
			randomRow = row.generate();
			randomColumn = column.generate();
			if (currObj.shouldGenerateObject(dungeon, randomRow, randomColumn, w, layer)) {
			    this.setCell(objects.getNewInstanceByName(currObj.getName()), randomColumn, randomRow,
				    layer);
			    currObj.editorGenerateHook(y, x);
			} else {
			    while (!currObj.shouldGenerateObject(dungeon, randomColumn, randomRow, w, layer)) {
				randomRow = row.generate();
				randomColumn = column.generate();
			    }
			    this.setCell(objects.getNewInstanceByName(currObj.getName()), randomColumn, randomRow,
				    layer);
			    currObj.editorGenerateHook(y, x);
			}
		    }
		}
	    }
	}
    }

    public void save() {
	int y, x, e;
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (e = 0; e < DungeonConstants.NUM_LAYERS; e++) {
		    this.savedTowerState.setCell(this.getCell(y, x, e), x, y, e);
		}
	    }
	}
    }

    public void restore() {
	int y, x, e;
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (e = 0; e < DungeonConstants.NUM_LAYERS; e++) {
		    this.setCell(this.savedTowerState.getCell(x, y, e), y, x, e);
		}
	    }
	}
    }

    private int normalizeRow(final int row) {
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

    private int normalizeColumn(final int column) {
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

    public boolean isHorizontalWraparoundEnabled() {
	return this.horizontalWraparoundEnabled;
    }

    public boolean isVerticalWraparoundEnabled() {
	return this.verticalWraparoundEnabled;
    }

    public void tickTimers() {
	int x, y;
	// Tick all GameObject timers
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		final AbstractDungeonObject mo = this.getCell(y, x, DungeonConstants.LAYER_LOWER_OBJECTS);
		if (mo != null) {
		    mo.tickTimer(y, x);
		}
	    }
	}
    }

    public static boolean radialScan(final int cx, final int cy, final int r, final int tx, final int ty) {
	return Math.abs(tx - cx) <= r && Math.abs(ty - cy) <= r;
    }

    public void writeLayeredTower(final FileIOWriter writer) throws IOException {
	int y, x, e;
	writer.writeInt(this.getColumns());
	writer.writeInt(this.getRows());
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (e = 0; e < DungeonConstants.NUM_LAYERS; e++) {
		    this.getCell(y, x, e).write(writer);
		}
		writer.writeBoolean(this.visionData.getCell(y, x));
	    }
	}
	for (y = 0; y < 3; y++) {
	    writer.writeInt(this.playerStartData[y]);
	    writer.writeInt(this.playerLocationData[y]);
	    writer.writeInt(this.savedPlayerLocationData[y]);
	    writer.writeInt(this.findResult[y]);
	}
	writer.writeBoolean(this.horizontalWraparoundEnabled);
	writer.writeBoolean(this.verticalWraparoundEnabled);
	writer.writeInt(this.visionMode);
	writer.writeInt(this.visionModeExploreRadius);
	writer.writeInt(this.visionMode);
	writer.writeInt(this.visionModeExploreRadius);
	writer.writeInt(this.visionRadius);
	writer.writeInt(this.initialVisionRadius);
	writer.writeInt(this.regionSize);
    }

    public static CurrentDungeonData readLayeredTowerV1(final FileIOReader reader) throws IOException {
	int y, x, e, dungeonSizeX, dungeonSizeY;
	dungeonSizeX = reader.readInt();
	dungeonSizeY = reader.readInt();
	final CurrentDungeonData lt = new CurrentDungeonData(dungeonSizeX, dungeonSizeY);
	for (x = 0; x < lt.getColumns(); x++) {
	    for (y = 0; y < lt.getRows(); y++) {
		for (e = 0; e < DungeonConstants.NUM_LAYERS; e++) {
		    lt.setCell(Integration1.getApplication().getObjects().read(reader,
			    FormatConstants.MAZE_FORMAT_LATEST), y, x, e);
		    if (lt.getCell(y, x, e) == null) {
			return null;
		    }
		}
		lt.visionData.setCell(reader.readBoolean(), y, x);
	    }
	}
	for (y = 0; y < 3; y++) {
	    lt.playerStartData[y] = reader.readInt();
	    lt.playerLocationData[y] = reader.readInt();
	    lt.savedPlayerLocationData[y] = reader.readInt();
	    lt.findResult[y] = reader.readInt();
	}
	lt.horizontalWraparoundEnabled = reader.readBoolean();
	lt.verticalWraparoundEnabled = reader.readBoolean();
	lt.visionMode = reader.readInt();
	lt.visionModeExploreRadius = reader.readInt();
	lt.visionMode = reader.readInt();
	lt.visionModeExploreRadius = reader.readInt();
	lt.visionRadius = reader.readInt();
	lt.initialVisionRadius = reader.readInt();
	lt.regionSize = reader.readInt();
	return lt;
    }

    public void writeSavedTowerState(final FileIOWriter writer) throws IOException {
	int x, y, e;
	writer.writeInt(this.getColumns());
	writer.writeInt(this.getRows());
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (e = 0; e < DungeonConstants.NUM_LAYERS; e++) {
		    this.savedTowerState.getCell(y, x, e).write(writer);
		}
	    }
	}
    }

    public void readSavedTowerState(final FileIOReader reader, final int formatVersion) throws IOException {
	int x, y, e, sizeX, sizeY;
	sizeX = reader.readInt();
	sizeY = reader.readInt();
	this.savedTowerState = new CurrentDungeonStorage(sizeY, sizeX, DungeonConstants.NUM_LAYERS);
	for (x = 0; x < sizeY; x++) {
	    for (y = 0; y < sizeX; y++) {
		for (e = 0; e < DungeonConstants.NUM_LAYERS; e++) {
		    this.savedTowerState.setCell(
			    Integration1.getApplication().getObjects().read(reader, formatVersion), y, x, e);
		}
	    }
	}
    }
}
