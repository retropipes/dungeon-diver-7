/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.current;

import java.io.IOException;
import java.util.ArrayDeque;

import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;
import org.retropipes.diane.random.RandomRange;
import org.retropipes.diane.storage.FlagStorage;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.StuffBag;
import org.retropipes.dungeondiver7.dungeon.Dungeon;
import org.retropipes.dungeondiver7.dungeon.DungeonData;
import org.retropipes.dungeondiver7.dungeon.DungeonDataStorage;
import org.retropipes.dungeondiver7.dungeon.HistoryStatus;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractButton;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractButtonDoor;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractCharacter;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractMovingObject;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.dungeon.objects.ArrowTurret;
import org.retropipes.dungeondiver7.dungeon.objects.ArrowTurretDisguise;
import org.retropipes.dungeondiver7.dungeon.objects.DeadArrowTurret;
import org.retropipes.dungeondiver7.dungeon.objects.Empty;
import org.retropipes.dungeondiver7.dungeon.objects.FinalBossMonsterTile;
import org.retropipes.dungeondiver7.dungeon.objects.Ground;
import org.retropipes.dungeondiver7.dungeon.objects.MonsterTile;
import org.retropipes.dungeondiver7.dungeon.objects.Party;
import org.retropipes.dungeondiver7.dungeon.objects.Wall;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.gameobject.Material;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.locale.Difficulty;
import org.retropipes.dungeondiver7.locale.ErrorString;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.utility.DungeonConstants;
import org.retropipes.dungeondiver7.utility.FileFormats;
import org.retropipes.dungeondiver7.utility.VisionModes;

public final class CurrentDungeonData extends DungeonData {
    private class ImageUndoEngine {
	// Inner classes
	private class HistoryEntry {
	    // Fields
	    private final DungeonDataStorage histImage;
	    private final HistoryStatus histWhatWas;

	    HistoryEntry(final DungeonDataStorage i, final HistoryStatus hww) {
		this.histImage = i;
		this.histWhatWas = hww;
	    }

	    HistoryEntry(final HistoryEntry source) {
		this.histImage = source.histImage;
		this.histWhatWas = source.histWhatWas;
	    }

	    public DungeonDataStorage getImage() {
		return this.histImage;
	    }

	    public HistoryStatus getWhatWas() {
		return this.histWhatWas;
	    }
	}

	private class HistoryStack {
	    // Fields
	    private final ArrayDeque<HistoryEntry> stack;

	    HistoryStack() {
		this.stack = new ArrayDeque<>();
	    }

	    HistoryStack(final HistoryStack source) {
		this.stack = new ArrayDeque<>();
		for (final HistoryEntry entry : source.stack) {
		    this.stack.addFirst(new HistoryEntry(entry));
		}
	    }

	    public boolean isEmpty() {
		return this.stack.isEmpty();
	    }

	    public HistoryEntry pop() {
		return this.stack.removeFirst();
	    }

	    public void push(final DungeonDataStorage i, final HistoryStatus hww) {
		final var newEntry = new HistoryEntry(i, hww);
		this.stack.addFirst(newEntry);
	    }
	}

	// Fields
	private HistoryStack undoHistory, redoHistory;
	private HistoryStatus whatWas;
	private DungeonDataStorage image;

	// Constructors
	public ImageUndoEngine() {
	    this.undoHistory = new HistoryStack();
	    this.redoHistory = new HistoryStack();
	    this.image = null;
	    this.whatWas = null;
	}

	public ImageUndoEngine(final ImageUndoEngine source) {
	    this.undoHistory = new HistoryStack(source.undoHistory);
	    this.redoHistory = new HistoryStack(source.redoHistory);
	    this.image = source.image;
	    this.whatWas = source.whatWas;
	}

	public void clearRedoHistory() {
	    this.redoHistory = new HistoryStack();
	}

	public void clearUndoHistory() {
	    this.undoHistory = new HistoryStack();
	}

	public DungeonDataStorage getImage() {
	    return this.image;
	}

	public HistoryStatus getWhatWas() {
	    return this.whatWas;
	}

	public void redo() {
	    if (!this.redoHistory.isEmpty()) {
		final var entry = this.redoHistory.pop();
		this.image = entry.getImage();
		this.whatWas = entry.getWhatWas();
	    } else {
		this.image = null;
		this.whatWas = null;
	    }
	}

	public boolean tryRedo() {
	    return !this.redoHistory.isEmpty();
	}

	public boolean tryUndo() {
	    return !this.undoHistory.isEmpty();
	}

	// Public methods
	public void undo() {
	    if (!this.undoHistory.isEmpty()) {
		final var entry = this.undoHistory.pop();
		this.image = entry.getImage();
		this.whatWas = entry.getWhatWas();
	    } else {
		this.image = null;
		this.whatWas = null;
	    }
	}

	public void updateRedoHistory(final DungeonDataStorage newImage, final HistoryStatus newWhatWas) {
	    this.redoHistory.push(newImage, newWhatWas);
	}

	public void updateUndoHistory(final DungeonDataStorage newImage, final HistoryStatus newWhatWas) {
	    this.undoHistory.push(newImage, newWhatWas);
	}
    }

    public static final CurrentDungeonLock LOCK_OBJECT = new CurrentDungeonLock();

    private static CurrentDungeonData readDataG1(final Dungeon dungeon, final DataIOReader reader, final int ver)
	    throws IOException {
	int y, x, z, dungeonSizeX, dungeonSizeY, dungeonSizeZ;
	dungeonSizeX = reader.readInt();
	dungeonSizeY = reader.readInt();
	dungeonSizeZ = reader.readInt();
	final var lt = new CurrentDungeonData();
	for (x = 0; x < dungeonSizeX; x++) {
	    for (y = 0; y < dungeonSizeY; y++) {
		for (z = 0; z < dungeonSizeZ; z++) {
		    final var obj = DungeonDiver7.getStuffBag().getObjects().readV2(reader, ver);
		    lt.setCell(dungeon, obj, y, x, z, obj.getLayer());
		}
	    }
	}
	dungeon.setStartColumn(0, reader.readInt());
	dungeon.setStartRow(0, reader.readInt());
	dungeon.setStartFloor(0, reader.readInt());
	final var horzWrap = reader.readBoolean();
	if (horzWrap) {
	    dungeon.enableHorizontalWraparound();
	} else {
	    dungeon.disableHorizontalWraparound();
	}
	final var vertWrap = reader.readBoolean();
	if (vertWrap) {
	    dungeon.enableVerticalWraparound();
	} else {
	    dungeon.disableVerticalWraparound();
	}
	dungeon.disableThirdDimensionWraparound();
	dungeon.setName(reader.readString());
	dungeon.setHint(reader.readString());
	dungeon.setAuthor(reader.readString());
	dungeon.setDifficulty(Difficulty.values()[reader.readInt()]);
	dungeon.setMoveShootAllowedThisLevel(false);
	// Fill nulls
	lt.fillNulls(dungeon, new Ground(), new Wall(), true);
	lt.fillVirtual();
	return lt;
    }

    private static CurrentDungeonData readDataG2(final Dungeon dungeon, final DataIOReader reader, final int ver)
	    throws IOException {
	int y, x, z, dungeonSizeX, dungeonSizeY, dungeonSizeZ;
	dungeonSizeX = reader.readInt();
	dungeonSizeY = reader.readInt();
	dungeonSizeZ = reader.readInt();
	final var lt = new CurrentDungeonData();
	lt.resize(dungeon, dungeonSizeZ, new Ground());
	for (x = 0; x < dungeonSizeX; x++) {
	    for (y = 0; y < dungeonSizeY; y++) {
		for (z = 0; z < dungeonSizeZ; z++) {
		    final var obj = DungeonDiver7.getStuffBag().getObjects().readV2(reader, ver);
		    lt.setCell(dungeon, obj, y, x, z, obj.getLayer());
		}
	    }
	}
	dungeon.setStartColumn(0, reader.readInt());
	dungeon.setStartRow(0, reader.readInt());
	dungeon.setStartFloor(0, reader.readInt());
	final var horzWrap = reader.readBoolean();
	if (horzWrap) {
	    dungeon.enableHorizontalWraparound();
	} else {
	    dungeon.disableHorizontalWraparound();
	}
	final var vertWrap = reader.readBoolean();
	if (vertWrap) {
	    dungeon.enableVerticalWraparound();
	} else {
	    dungeon.disableVerticalWraparound();
	}
	final var thirdWrap = reader.readBoolean();
	if (thirdWrap) {
	    dungeon.enableThirdDimensionWraparound();
	} else {
	    dungeon.disableThirdDimensionWraparound();
	}
	dungeon.setName(reader.readString());
	dungeon.setHint(reader.readString());
	dungeon.setAuthor(reader.readString());
	dungeon.setDifficulty(Difficulty.values()[reader.readInt()]);
	dungeon.setMoveShootAllowedThisLevel(false);
	// Fill nulls
	lt.fillNulls(dungeon, new Ground(), null, false);
	lt.fillVirtual();
	return lt;
    }

    private static CurrentDungeonData readDataG3(final Dungeon dungeon, final DataIOReader reader, final int ver)
	    throws IOException {
	int y, x, z, dungeonSizeX, dungeonSizeY, dungeonSizeZ;
	dungeonSizeX = reader.readInt();
	dungeonSizeY = reader.readInt();
	dungeonSizeZ = reader.readInt();
	final var lt = new CurrentDungeonData();
	lt.resize(dungeon, dungeonSizeZ, new Ground());
	for (x = 0; x < dungeonSizeX; x++) {
	    for (y = 0; y < dungeonSizeY; y++) {
		for (z = 0; z < dungeonSizeZ; z++) {
		    final var obj = DungeonDiver7.getStuffBag().getObjects().readV3(reader, ver);
		    lt.setCell(dungeon, obj, y, x, z, obj.getLayer());
		}
	    }
	}
	dungeon.setStartColumn(0, reader.readInt());
	dungeon.setStartRow(0, reader.readInt());
	dungeon.setStartFloor(0, reader.readInt());
	final var horzWrap = reader.readBoolean();
	if (horzWrap) {
	    dungeon.enableHorizontalWraparound();
	} else {
	    dungeon.disableHorizontalWraparound();
	}
	final var vertWrap = reader.readBoolean();
	if (vertWrap) {
	    dungeon.enableVerticalWraparound();
	} else {
	    dungeon.disableVerticalWraparound();
	}
	final var thirdWrap = reader.readBoolean();
	if (thirdWrap) {
	    dungeon.enableThirdDimensionWraparound();
	} else {
	    dungeon.disableThirdDimensionWraparound();
	}
	dungeon.setName(reader.readString());
	dungeon.setHint(reader.readString());
	dungeon.setAuthor(reader.readString());
	dungeon.setDifficulty(Difficulty.values()[reader.readInt()]);
	dungeon.setMoveShootAllowedThisLevel(false);
	// Fill nulls
	lt.fillNulls(dungeon, new Ground(), null, false);
	lt.fillVirtual();
	return lt;
    }

    private static CurrentDungeonData readDataG4(final Dungeon dungeon, final DataIOReader reader, final int ver)
	    throws IOException {
	int y, x, z, dungeonSizeX, dungeonSizeY, dungeonSizeZ;
	dungeonSizeX = reader.readInt();
	dungeonSizeY = reader.readInt();
	dungeonSizeZ = reader.readInt();
	final var lt = new CurrentDungeonData();
	lt.resize(dungeon, dungeonSizeZ, new Ground());
	for (x = 0; x < dungeonSizeX; x++) {
	    for (y = 0; y < dungeonSizeY; y++) {
		for (z = 0; z < dungeonSizeZ; z++) {
		    final var obj = DungeonDiver7.getStuffBag().getObjects().readV4(reader, ver);
		    lt.setCell(dungeon, obj, y, x, z, obj.getLayer());
		}
	    }
	}
	dungeon.setStartColumn(0, reader.readInt());
	dungeon.setStartRow(0, reader.readInt());
	dungeon.setStartFloor(0, reader.readInt());
	final var horzWrap = reader.readBoolean();
	if (horzWrap) {
	    dungeon.enableHorizontalWraparound();
	} else {
	    dungeon.disableHorizontalWraparound();
	}
	final var vertWrap = reader.readBoolean();
	if (vertWrap) {
	    dungeon.enableVerticalWraparound();
	} else {
	    dungeon.disableVerticalWraparound();
	}
	final var thirdWrap = reader.readBoolean();
	if (thirdWrap) {
	    dungeon.enableThirdDimensionWraparound();
	} else {
	    dungeon.disableThirdDimensionWraparound();
	}
	dungeon.setName(reader.readString());
	dungeon.setHint(reader.readString());
	dungeon.setAuthor(reader.readString());
	dungeon.setDifficulty(Difficulty.values()[reader.readInt()]);
	dungeon.setMoveShootAllowedThisLevel(false);
	// Fill nulls
	lt.fillNulls(dungeon, new Ground(), null, false);
	lt.fillVirtual();
	return lt;
    }

    private static CurrentDungeonData readDataG5(final Dungeon dungeon, final DataIOReader reader, final int ver)
	    throws IOException {
	int y, x, z, w, dungeonSizeX, dungeonSizeY, dungeonSizeZ;
	dungeonSizeX = reader.readInt();
	dungeonSizeY = reader.readInt();
	dungeonSizeZ = reader.readInt();
	final var lt = new CurrentDungeonData();
	lt.resize(dungeon, dungeonSizeZ, new Ground());
	for (x = 0; x < dungeonSizeX; x++) {
	    for (y = 0; y < dungeonSizeY; y++) {
		for (z = 0; z < dungeonSizeZ; z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			lt.setCell(dungeon, DungeonDiver7.getStuffBag().getObjects().readV5(reader, ver), y, x, z, w);
		    }
		}
	    }
	}
	dungeon.setStartColumn(0, reader.readInt());
	dungeon.setStartRow(0, reader.readInt());
	dungeon.setStartFloor(0, reader.readInt());
	final var horzWrap = reader.readBoolean();
	if (horzWrap) {
	    dungeon.enableHorizontalWraparound();
	} else {
	    dungeon.disableHorizontalWraparound();
	}
	final var vertWrap = reader.readBoolean();
	if (vertWrap) {
	    dungeon.enableVerticalWraparound();
	} else {
	    dungeon.disableVerticalWraparound();
	}
	final var thirdWrap = reader.readBoolean();
	if (thirdWrap) {
	    dungeon.enableThirdDimensionWraparound();
	} else {
	    dungeon.disableThirdDimensionWraparound();
	}
	dungeon.setName(reader.readString());
	dungeon.setHint(reader.readString());
	dungeon.setAuthor(reader.readString());
	dungeon.setDifficulty(Difficulty.values()[reader.readInt()]);
	dungeon.setMoveShootAllowedThisLevel(reader.readBoolean());
	// Fill nulls
	lt.fillNulls(dungeon, new Ground(), null, false);
	lt.fillVirtual();
	return lt;
    }

    private static CurrentDungeonData readDataG6(final Dungeon dungeon, final DataIOReader reader, final int ver)
	    throws IOException {
	int y, x, z, w, dungeonSizeX, dungeonSizeY, dungeonSizeZ;
	dungeonSizeX = reader.readInt();
	dungeonSizeY = reader.readInt();
	dungeonSizeZ = reader.readInt();
	final var lt = new CurrentDungeonData();
	lt.resize(dungeon, dungeonSizeZ, new Ground());
	for (x = 0; x < dungeonSizeX; x++) {
	    for (y = 0; y < dungeonSizeY; y++) {
		for (z = 0; z < dungeonSizeZ; z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			lt.setCell(dungeon, DungeonDiver7.getStuffBag().getObjects().readV6(reader, ver), y, x, z, w);
		    }
		}
	    }
	}
	// Fill nulls
	lt.fillNulls(dungeon, new Ground(), null, false);
	lt.fillVirtual();
	return lt;
    }

    // Properties
    private DungeonDataStorage data;
    private DungeonDataStorage virtualData;
    private final FlagStorage visionData;
    private FlagStorage dirtyData;
    private DungeonDataStorage savedState;
    private int foundX, foundY;
    private ImageUndoEngine iue;
    private final int visionMode;
    private final int visionModeExploreRadius;

    // Constructors
    public CurrentDungeonData() {
	this.data = new DungeonDataStorage(DungeonData.MIN_COLUMNS, DungeonData.MIN_ROWS, DungeonData.MIN_FLOORS,
		DungeonConstants.NUM_LAYERS);
	this.virtualData = new DungeonDataStorage(DungeonData.MIN_COLUMNS, DungeonData.MIN_ROWS, DungeonData.MIN_FLOORS,
		DungeonConstants.NUM_VIRTUAL_LAYERS);
	this.fillVirtual();
	this.dirtyData = new FlagStorage(DungeonData.MIN_COLUMNS, DungeonData.MIN_ROWS, DungeonData.MIN_FLOORS);
	this.visionData = new FlagStorage(DungeonData.MIN_COLUMNS, DungeonData.MIN_ROWS, DungeonData.MIN_FLOORS);
	this.savedState = new DungeonDataStorage(DungeonData.MIN_ROWS, DungeonData.MIN_COLUMNS, DungeonData.MIN_FLOORS,
		DungeonConstants.NUM_LAYERS);
	this.foundX = -1;
	this.foundY = -1;
	this.iue = new ImageUndoEngine();
	this.visionMode = VisionModes.EXPLORE_AND_LOS;
	this.visionModeExploreRadius = 2;
    }

    public CurrentDungeonData(final CurrentDungeonData source) {
	this.data = new DungeonDataStorage(source.data);
	this.virtualData = new DungeonDataStorage(source.virtualData);
	this.dirtyData = new FlagStorage(source.dirtyData);
	this.visionData = new FlagStorage(source.visionData);
	this.savedState = new DungeonDataStorage(source.savedState);
	this.foundX = source.foundX;
	this.foundY = source.foundY;
	this.iue = new ImageUndoEngine(source.iue);
	this.visionMode = source.visionMode;
	this.visionModeExploreRadius = source.visionModeExploreRadius;
    }

    public CurrentDungeonData(final int rows, final int cols, final int floors) {
	this.data = new DungeonDataStorage(cols, rows, floors, DungeonConstants.NUM_LAYERS);
	this.virtualData = new DungeonDataStorage(cols, rows, floors, DungeonConstants.NUM_VIRTUAL_LAYERS);
	this.fillVirtual();
	this.dirtyData = new FlagStorage(cols, rows, floors);
	this.visionData = new FlagStorage(cols, rows, floors);
	this.savedState = new DungeonDataStorage(cols, rows, floors, DungeonConstants.NUM_LAYERS);
	this.foundX = -1;
	this.foundY = -1;
	this.iue = new ImageUndoEngine();
	this.visionMode = VisionModes.EXPLORE_AND_LOS;
	this.visionModeExploreRadius = 2;
    }

    private boolean areCoordsInBounds(final Dungeon dungeon, final int x1, final int y1, final int x2, final int y2) {
	int fx1, fx2, fy1, fy2;
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    fx1 = this.normalizeColumn(x1);
	    fx2 = this.normalizeColumn(x2);
	} else {
	    fx1 = x1;
	    fx2 = x2;
	}
	if (dungeon.isVerticalWraparoundEnabled()) {
	    fy1 = this.normalizeRow(y1);
	    fy2 = this.normalizeRow(y2);
	} else {
	    fy1 = y1;
	    fy2 = y2;
	}
	return fx1 >= 0 && fx1 <= this.getRows() && fx2 >= 0 && fx2 <= this.getRows() && fy1 >= 0
		&& fy1 <= this.getColumns() && fy2 >= 0 && fy2 <= this.getColumns();
    }

    @Override
    public void checkForEnemies(final Dungeon dungeon, final int floorIn, final int enemyLocXIn, final int enemyLocYIn,
	    final AbstractCharacter enemy) {
	if (enemy instanceof ArrowTurretDisguise) {
	    // Anti Players are fooled by disguises
	    return;
	}
	final var template = new ArrowTurret();
	var enemyLocX = enemyLocXIn;
	var enemyLocY = enemyLocYIn;
	var floor = floorIn;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    enemyLocX = this.normalizeColumn(enemyLocX);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    enemyLocY = this.normalizeRow(enemyLocY);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    floor = this.normalizeFloor(floor);
	}
	final var scanE = this.linearScan(dungeon, enemyLocX, enemyLocY, floor, Direction.EAST);
	if (scanE) {
	    try {
		final var at = (ArrowTurret) this.getCell(dungeon, this.foundX, this.foundY, floor,
			template.getLayer());
		at.kill(this.foundX, this.foundY);
	    } catch (final ClassCastException cce) {
		// Ignore
	    }
	}
	final var scanW = this.linearScan(dungeon, enemyLocX, enemyLocY, floor, Direction.WEST);
	if (scanW) {
	    try {
		final var at = (ArrowTurret) this.getCell(dungeon, this.foundX, this.foundY, floor,
			template.getLayer());
		at.kill(this.foundX, this.foundY);
	    } catch (final ClassCastException cce) {
		// Ignore
	    }
	}
	final var scanS = this.linearScan(dungeon, enemyLocX, enemyLocY, floor, Direction.SOUTH);
	if (scanS) {
	    try {
		final var at = (ArrowTurret) this.getCell(dungeon, this.foundX, this.foundY, floor,
			template.getLayer());
		at.kill(this.foundX, this.foundY);
	    } catch (final ClassCastException cce) {
		// Ignore
	    }
	}
	final var scanN = this.linearScan(dungeon, enemyLocX, enemyLocY, floor, Direction.NORTH);
	if (scanN) {
	    try {
		final var at = (ArrowTurret) this.getCell(dungeon, this.foundX, this.foundY, floor,
			template.getLayer());
		at.kill(this.foundX, this.foundY);
	    } catch (final ClassCastException cce) {
		// Ignore
	    }
	}
    }

    @Override
    public int checkForMagnetic(final Dungeon dungeon, final int floor, final int centerX, final int centerY,
	    final Direction dir) {
	if (dir == Direction.EAST) {
	    return this.linearScanMagnetic(dungeon, centerX, centerY, floor, Direction.EAST);
	}
	if (dir == Direction.WEST) {
	    return this.linearScanMagnetic(dungeon, centerX, centerY, floor, Direction.WEST);
	}
	if (dir == Direction.SOUTH) {
	    return this.linearScanMagnetic(dungeon, centerX, centerY, floor, Direction.SOUTH);
	}
	if (dir == Direction.NORTH) {
	    return this.linearScanMagnetic(dungeon, centerX, centerY, floor, Direction.NORTH);
	}
	return 0;
    }

    @Override
    public int[] circularScan(final Dungeon dungeon, final int xIn, final int yIn, final int zIn, final int r,
	    final String targetName, final boolean moved) {
	var xFix = xIn;
	var yFix = yIn;
	var zFix = zIn;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    xFix = this.normalizeColumn(xFix);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    yFix = this.normalizeRow(yFix);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    zFix = this.normalizeFloor(zFix);
	}
	int u, v, w;
	u = v = 0;
	// Perform the scan
	for (u = xFix - r; u <= xFix + r; u++) {
	    for (v = yFix - r; v <= yFix + r; v++) {
		for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
		    try {
			final var obj = this.getCell(dungeon, v, u, zFix, w);
			final var savedObj = obj.getSavedObject();
			String testName;
			if (!obj.isPlayer() || moved) {
			    testName = obj.getCacheName();
			} else {
			    testName = savedObj.getCacheName();
			}
			if (testName.equals(targetName)) {
			    return new int[] { v, u, zFix };
			}
		    } catch (final ArrayIndexOutOfBoundsException aioob) {
			// Do nothing
		    }
		}
	    }
	}
	return null;
    }

    @Override
    public boolean circularScanPlayer(final Dungeon dungeon, final int x, final int y, final int z, final int r) {
	final var tankLoc = DungeonDiver7.getStuffBag().getGameLogic().getPlayerLocation();
	var fX = x;
	var fY = y;
	var fZ = z;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    fX = this.normalizeColumn(fX);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    fY = this.normalizeRow(fY);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    fZ = this.normalizeFloor(fZ);
	}
	final var tx = tankLoc[0];
	final var ty = tankLoc[1];
	final var tz = tankLoc[2];
	return fZ == tz && Math.abs(fX - tx) <= r && Math.abs(fY - ty) <= r;
    }

    @Override
    public void circularScanRange(final Dungeon dungeon, final int xIn, final int yIn, final int zIn, final int r,
	    final int rangeType, final int forceUnits) {
	var xFix = xIn;
	var yFix = yIn;
	var zFix = zIn;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    xFix = this.normalizeColumn(xFix);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    yFix = this.normalizeRow(yFix);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    zFix = this.normalizeFloor(zFix);
	}
	int u, v, w;
	u = v = 0;
	// Perform the scan
	for (u = xFix - r; u <= xFix + r; u++) {
	    for (v = yFix - r; v <= yFix + r; v++) {
		if (u == xFix && v == yFix) {
		    continue;
		}
		for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
		    try {
			this.getCell(dungeon, u, v, zFix, w).rangeAction(xFix, yFix, zFix, u - xFix, v - yFix,
				rangeType, forceUnits);
		    } catch (final ArrayIndexOutOfBoundsException aioob) {
			// Do nothing
		    }
		}
	    }
	}
    }

    @Override
    public int[] circularScanTunnel(final Dungeon dungeon, final int xIn, final int yIn, final int zIn, final int r,
	    final int tx, final int ty, final GameObject target, final boolean moved) {
	var xFix = xIn;
	var yFix = yIn;
	var zFix = zIn;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    xFix = this.normalizeColumn(xFix);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    yFix = this.normalizeRow(yFix);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    zFix = this.normalizeFloor(zFix);
	}
	int u, v, w;
	w = DungeonConstants.LAYER_LOWER_OBJECTS;
	// Perform the scan
	for (u = xFix - r; u <= xFix + r; u++) {
	    for (v = yFix - r; v <= yFix + r; v++) {
		if (v == tx && u == ty && moved) {
		    continue;
		}
		if (v >= 0 && v < DungeonData.MIN_ROWS && u >= 0 && u < DungeonData.MIN_COLUMNS) {
		    final var obj = this.getCell(dungeon, v, u, zFix, w);
		    final var savedObj = obj.getSavedObject();
		    GameObject test;
		    if (obj.isPlayer()) {
			test = savedObj;
		    } else {
			test = obj;
		    }
		    if (target.equals(test)) {
			return new int[] { v, u, zFix };
		    }
		}
	    }
	}
	return null;
    }

    @Override
    public void clearDirtyFlags(final int floor) {
	for (var row = 0; row < this.getRows(); row++) {
	    for (var col = 0; col < this.getColumns(); col++) {
		this.dirtyData.setCell(false, col, row, floor);
	    }
	}
    }

    @Override
    public void clearRedoHistory() {
	this.iue.clearRedoHistory();
    }

    @Override
    public void clearUndoHistory() {
	this.iue.clearUndoHistory();
    }

    @Override
    public void clearVirtualGrid(final Dungeon dungeon) {
	for (var row = 0; row < this.getRows(); row++) {
	    for (var col = 0; col < this.getColumns(); col++) {
		for (var floor = 0; floor < this.getFloors(); floor++) {
		    for (var layer = 0; layer < DungeonConstants.NUM_VIRTUAL_LAYERS; layer++) {
			this.setVirtualCell(dungeon, new Empty(), row, col, floor, layer);
		    }
		}
	    }
	}
    }

    @Override
    public CurrentDungeonData clone() {
	return new CurrentDungeonData(this);
    }

    @Override
    public void fill(final Dungeon dungeon, final GameObject fill) {
	int y, x, z, w;
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (z = 0; z < this.getFloors(); z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			if (w == DungeonConstants.LAYER_LOWER_GROUND) {
			    this.setCell(dungeon, fill, y, x, z, w);
			} else {
			    this.setCell(dungeon, new Empty(), y, x, z, w);
			}
		    }
		}
	    }
	}
    }

    @Override
    public void fillNulls(final Dungeon dungeon, final GameObject fill1, final GameObject fill2,
	    final boolean was16) {
	int y, x, z, w;
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (z = 0; z < this.getFloors(); z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			if (this.getCell(dungeon, y, x, z, w) == null) {
			    if (w == DungeonConstants.LAYER_LOWER_GROUND) {
				this.setCell(dungeon, fill1, y, x, z, w);
			    } else if (w == DungeonConstants.LAYER_LOWER_OBJECTS && was16 && (x >= 16 || y >= 16)) {
				this.setCell(dungeon, fill2, y, x, z, w);
			    } else {
				this.setCell(dungeon, new Empty(), y, x, z, w);
			    }
			}
		    }
		}
	    }
	}
    }

    @Override
    public void fillSTSNulls(final GameObject fill) {
	int y, x, z, w;
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (z = 0; z < this.getFloors(); z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			if (this.savedState.getCell(y, x, z, w) == null) {
			    if (w == DungeonConstants.LAYER_LOWER_GROUND) {
				this.savedState.setCell(fill, y, x, z, w);
			    } else {
				this.savedState.setCell(new Empty(), y, x, z, w);
			    }
			}
		    }
		}
	    }
	}
    }

    @Override
    public void fillVirtual() {
	int y, x, z, w;
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (z = 0; z < this.getFloors(); z++) {
		    for (w = 0; w < DungeonConstants.NUM_VIRTUAL_LAYERS; w++) {
			this.virtualData.setCell(new Empty(), y, x, z, w);
		    }
		}
	    }
	}
    }

    @Override
    public int[] findObject(final Dungeon dungeon, final int z, final String targetName) {
	// Perform the scan
	for (var x = 0; x < DungeonData.MIN_COLUMNS; x++) {
	    for (var y = 0; y < DungeonData.MIN_ROWS; y++) {
		for (var w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
		    try {
			final var obj = this.getCell(dungeon, x, y, z, w);
			final var testName = obj.getName();
			if (testName.equals(targetName)) {
			    return new int[] { x, y };
			}
		    } catch (final ArrayIndexOutOfBoundsException aioob) {
			// Do nothing
		    }
		}
	    }
	}
	return null;
    }

    @Override
    public int[] findPlayer(final Dungeon dungeon, final int number) {
	final var t = new Party(number);
	int y, x, z;
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (z = 0; z < this.getFloors(); z++) {
		    final var mo = this.getCell(dungeon, y, x, z, t.getLayer());
		    if (mo != null && t.equals(mo)) {
			return new int[] { y, x, z };
		    }
		}
	    }
	}
	return null;
    }

    @Override
    public void fullScanAllButtonClose(final Dungeon dungeon, final int zIn, final AbstractButton source) {
	// Perform the scan
	var zFix = zIn;
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    zFix = this.normalizeFloor(zFix);
	}
	var flag = !source.isTriggered();
	for (var x = 0; x < DungeonData.MIN_COLUMNS; x++) {
	    if (flag) {
		break;
	    }
	    for (var y = 0; y < DungeonData.MIN_ROWS; y++) {
		if (flag) {
		    break;
		}
		final var obj = this.getCell(dungeon, y, x, zFix, source.getLayer());
		if (obj instanceof final AbstractButton button && source.hasSameBoundObject(button)
			&& !button.isTriggered()) {
		    flag = true;
		}
	    }
	}
	if (flag) {
	    // Scan said OK to proceed
	    final var dx = source.getBoundObjectX();
	    final var dy = source.getBoundObjectY();
	    if (!this.getCell(dungeon, dx, dy, zFix, source.getLayer()).getClass()
		    .equals(source.getBoundObject().getClass())) {
		this.setCell(dungeon, source.getBoundObject(), dx, dy, zFix, source.getLayer());
		SoundLoader.playSound(Sounds.DOOR_CLOSES);
	    }
	}
    }

    @Override
    public void fullScanAllButtonOpen(final Dungeon dungeon, final int zIn, final AbstractButton source) {
	// Perform the scan
	var zFix = zIn;
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    zFix = this.normalizeFloor(zFix);
	}
	var flag = true;
	for (var x = 0; x < DungeonData.MIN_COLUMNS; x++) {
	    if (!flag) {
		break;
	    }
	    for (var y = 0; y < DungeonData.MIN_ROWS; y++) {
		if (!flag) {
		    break;
		}
		final var obj = this.getCell(dungeon, y, x, zFix, source.getLayer());
		if (obj instanceof final AbstractButton button && source.hasSameBoundObject(button)
			&& !button.isTriggered()) {
		    flag = false;
		}
	    }
	}
	if (flag) {
	    // Scan said OK to proceed
	    final var dx = source.getBoundObjectX();
	    final var dy = source.getBoundObjectY();
	    if (!(this.getCell(dungeon, dx, dy, zFix, source.getLayer()) instanceof Ground)) {
		this.setCell(dungeon, new Ground(), dx, dy, zFix, source.getLayer());
		SoundLoader.playSound(Sounds.DOOR_OPENS);
	    }
	}
    }

    @Override
    public void fullScanButtonBind(final Dungeon dungeon, final int dx, final int dy, final int zIn,
	    final AbstractButtonDoor source) {
	// Perform the scan
	var z = zIn;
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    z = this.normalizeFloor(z);
	}
	for (var x = 0; x < DungeonData.MIN_COLUMNS; x++) {
	    for (var y = 0; y < DungeonData.MIN_ROWS; y++) {
		final var obj = this.getCell(dungeon, x, y, z, source.getLayer());
		if (obj instanceof final AbstractButton button
			&& source.getClass().equals(button.getBoundObject().getClass())) {
		    button.setBoundObjectX(dx);
		    button.setBoundObjectY(dy);
		    button.setTriggered(false);
		}
	    }
	}
	for (var x = 0; x < DungeonData.MIN_COLUMNS; x++) {
	    for (var y = 0; y < DungeonData.MIN_ROWS; y++) {
		final var obj = this.getCell(dungeon, x, y, z, source.getLayer());
		if (obj instanceof final AbstractButtonDoor door && source.getClass().equals(door.getClass())) {
		    this.setCell(dungeon, new Ground(), x, y, z, source.getLayer());
		}
	    }
	}
    }

    @Override
    public void fullScanButtonCleanup(final Dungeon dungeon, final int px, final int py, final int zIn,
	    final AbstractButton button) {
	// Perform the scan
	var zFix = zIn;
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    zFix = this.normalizeFloor(zFix);
	}
	for (var x = 0; x < DungeonData.MIN_COLUMNS; x++) {
	    for (var y = 0; y < DungeonData.MIN_ROWS; y++) {
		if (x == px && y == py) {
		    continue;
		}
		final var obj = this.getCell(dungeon, x, y, zFix, button.getLayer());
		if (obj instanceof AbstractButton && ((AbstractButton) obj).hasSameBoundObject(button)) {
		    this.setCell(dungeon, new Ground(), x, y, zFix, button.getLayer());
		}
	    }
	}
    }

    @Override
    public void fullScanFindButtonLostDoor(final Dungeon dungeon, final int zIn, final AbstractButtonDoor door) {
	// Perform the scan
	var zFix = zIn;
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    zFix = this.normalizeFloor(zFix);
	}
	for (var x = 0; x < DungeonData.MIN_COLUMNS; x++) {
	    for (var y = 0; y < DungeonData.MIN_ROWS; y++) {
		final var obj = this.getCell(dungeon, x, y, zFix, door.getLayer());
		if (obj instanceof final AbstractButton button && button.boundToSameObject(door)) {
		    button.setTriggered(true);
		    return;
		}
	    }
	}
    }

    @Override
    public void fullScanFreezeGround(final Dungeon dungeon) {
	// Perform the scan
	for (var x = 0; x < DungeonData.MIN_COLUMNS; x++) {
	    for (var y = 0; y < DungeonData.MIN_ROWS; y++) {
		for (var z = 0; z < this.getFloors(); z++) {
		    final var obj = this.getCell(dungeon, y, x, z, DungeonConstants.LAYER_LOWER_GROUND);
		    if (!(obj instanceof Ground)) {
			DungeonDiver7.getStuffBag().getGameLogic();
			// Freeze the ground
			GameLogic.morph(obj.changesToOnExposure(Material.ICE), y, x, z,
				DungeonConstants.LAYER_LOWER_GROUND);
		    }
		}
	    }
	}
    }

    @Override
    public void fullScanKillPlayers(final Dungeon dungeon) {
	// Perform the scan
	for (var x = 0; x < DungeonData.MIN_COLUMNS; x++) {
	    for (var y = 0; y < DungeonData.MIN_ROWS; y++) {
		for (var z = 0; z < this.getFloors(); z++) {
		    final var obj = this.getCell(dungeon, y, x, z, DungeonConstants.LAYER_LOWER_OBJECTS);
		    if (obj instanceof ArrowTurret) {
			final var dat = new DeadArrowTurret();
			dat.setSavedObject(obj.getSavedObject());
			dat.setDirection(obj.getDirection());
			GameLogic.morph(dat, y, x, z, DungeonConstants.LAYER_LOWER_OBJECTS);
		    }
		}
	    }
	}
    }

    private void generateOneMonster(final Dungeon dungeon) {
	final var row = new RandomRange(0, this.getRows() - 1);
	final var column = new RandomRange(0, this.getColumns() - 1);
	int randomRow, randomColumn;
	randomRow = row.generate();
	randomColumn = column.generate();
	var currObj = this.getCell(dungeon, randomRow, randomColumn, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	if (!currObj.isSolid()) {
	    final AbstractMovingObject m = new MonsterTile();
	    m.setSavedObject(currObj);
	    this.setCell(dungeon, m, randomRow, randomColumn, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	} else {
	    while (currObj.isSolid()) {
		randomRow = row.generate();
		randomColumn = column.generate();
		currObj = this.getCell(dungeon, randomRow, randomColumn, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	    }
	    final AbstractMovingObject m = new MonsterTile();
	    m.setSavedObject(currObj);
	    this.setCell(dungeon, m, randomRow, randomColumn, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	}
    }

    @Override
    public GameObject getCell(final Dungeon dungeon, final int row, final int col, final int floor,
	    final int layer) {
	var fR = row;
	var fC = col;
	var fF = floor;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    fC = this.normalizeColumn(fC);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    fR = this.normalizeRow(fR);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    fF = this.normalizeFloor(fF);
	}
	return this.data.getDungeonDataCell(fC, fR, fF, layer);
    }

    @Override
    public int getColumns() {
	return this.data.getShape()[0];
    }

    @Override
    public int getFloors() {
	return this.data.getShape()[2];
    }

    @Override
    public int getRows() {
	return this.data.getShape()[1];
    }

    @Override
    public GameObject getVirtualCell(final Dungeon dungeon, final int row, final int col, final int floor,
	    final int layer) {
	var fR = row;
	var fC = col;
	var fF = floor;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    fC = this.normalizeColumn(fC);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    fR = this.normalizeRow(fR);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    fF = this.normalizeFloor(fF);
	}
	return this.virtualData.getDungeonDataCell(fC, fR, fF, layer);
    }

    @Override
    public HistoryStatus getWhatWas() {
	return this.iue.getWhatWas();
    }

    @Override
    public boolean isCellDirty(final Dungeon dungeon, final int row, final int col, final int floor) {
	var fR = row;
	var fC = col;
	var fF = floor;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    fC = this.normalizeColumn(fC);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    fR = this.normalizeRow(fR);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    fF = this.normalizeFloor(fF);
	}
	return this.dirtyData.getCell(fC, fR, fF);
    }

    @Override
    public boolean isSquareVisible(final Dungeon dungeon, final int x1, final int y1, final int x2, final int y2,
	    final int zp) {
	if (this.visionMode == VisionModes.NONE) {
	    return true;
	}
	var result = false;
	if ((this.visionMode | VisionModes.EXPLORE) == this.visionMode) {
	    result = result || this.isSquareVisibleExplore(dungeon, x2, y2);
	    if (result && (this.visionMode | VisionModes.LOS) == this.visionMode) {
		if (this.areCoordsInBounds(dungeon, x1, y1, x2, y2)) {
		    // In bounds
		    result = result || this.isSquareVisibleLOS(dungeon, x1, y1, x2, y2, zp);
		} else {
		    // Out of bounds
		    result = result && this.isSquareVisibleLOS(dungeon, x1, y1, x2, y2, zp);
		}
	    }
	} else if (this.areCoordsInBounds(dungeon, x1, y1, x2, y2)) {
	    // In bounds
	    result = result || this.isSquareVisibleLOS(dungeon, x1, y1, x2, y2, zp);
	} else {
	    // Out of bounds
	    result = result && this.isSquareVisibleLOS(dungeon, x1, y1, x2, y2, zp);
	}
	return result;
    }

    private boolean isSquareVisibleExplore(final Dungeon dungeon, final int x2, final int y2) {
	int fx2, fy2;
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    fx2 = this.normalizeColumn(x2);
	} else {
	    fx2 = x2;
	}
	if (dungeon.isVerticalWraparoundEnabled()) {
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

    private boolean isSquareVisibleLOS(final Dungeon dungeon, final int x1, final int y1, final int x2, final int y2,
	    final int zp) {
	int fx1, fx2, fy1, fy2;
	fx1 = x1;
	fx2 = x2;
	fy1 = y1;
	fy2 = y2;
	final var dx = Math.abs(fx2 - fx1);
	final var dy = Math.abs(fy2 - fy1);
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
	var err = dx - dy;
	int e2;
	do {
	    if (fx1 == fx2 && fy1 == fy2) {
		break;
	    }
	    // Does object block LOS?
	    try {
		final var obj = this.getCell(dungeon, fx1, fy1, zp, DungeonConstants.LAYER_LOWER_OBJECTS);
		// This object blocks LOS
		if (obj.isSightBlocking() && (fx1 != x1 || fy1 != y1)) {
		    return false;
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

    @Override
    public boolean linearScan(final Dungeon dungeon, final int xIn, final int yIn, final int zIn, final Direction d) {
	// Perform the scan
	var xFix = xIn;
	var yFix = yIn;
	var zFix = zIn;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    xFix = this.normalizeColumn(xFix);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    yFix = this.normalizeRow(yFix);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    zFix = this.normalizeFloor(zFix);
	}
	int u, w;
	if (d == Direction.NORTH) {
	    final GameObject tank = DungeonDiver7.getStuffBag().getGameLogic().getPlayer();
	    if (tank.getSavedObject().isSolid()) {
		return false;
	    }
	    for (u = yFix - 1; u >= 0; u--) {
		for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
		    try {
			final var obj = this.getCell(dungeon, xFix, u, zFix, w);
			if (obj.canShoot()) {
			    final var unres = DirectionResolver.unresolve(obj.getDirection());
			    final var invert = DirectionResolver.resolveInvert(unres[0], unres[1]);
			    if (d == invert) {
				this.foundX = xFix;
				this.foundY = u;
				return true;
			    }
			}
			if (obj.isSolid()) {
			    return false;
			}
		    } catch (final ArrayIndexOutOfBoundsException aioobe) {
			return false;
		    }
		}
	    }
	    return false;
	}
	if (d == Direction.SOUTH) {
	    final GameObject tank = DungeonDiver7.getStuffBag().getGameLogic().getPlayer();
	    if (tank.getSavedObject().isSolid()) {
		return false;
	    }
	    for (u = yFix + 1; u < 24; u++) {
		for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
		    try {
			final var obj = this.getCell(dungeon, xFix, u, zFix, w);
			if (obj.canShoot()) {
			    final var unres = DirectionResolver.unresolve(obj.getDirection());
			    final var invert = DirectionResolver.resolveInvert(unres[0], unres[1]);
			    if (d == invert) {
				this.foundX = xFix;
				this.foundY = u;
				return true;
			    }
			}
			if (obj.isSolid()) {
			    return false;
			}
		    } catch (final ArrayIndexOutOfBoundsException aioobe) {
			return false;
		    }
		}
	    }
	} else if (d == Direction.WEST) {
	    final GameObject tank = DungeonDiver7.getStuffBag().getGameLogic().getPlayer();
	    if (tank.getSavedObject().isSolid()) {
		return false;
	    }
	    for (u = xFix - 1; u >= 0; u--) {
		for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
		    try {
			final var obj = this.getCell(dungeon, u, yFix, zFix, w);
			if (obj.canShoot()) {
			    final var unres = DirectionResolver.unresolve(obj.getDirection());
			    final var invert = DirectionResolver.resolveInvert(unres[0], unres[1]);
			    if (d == invert) {
				this.foundX = u;
				this.foundY = yFix;
				return true;
			    }
			}
			if (obj.isSolid()) {
			    return false;
			}
		    } catch (final ArrayIndexOutOfBoundsException aioobe) {
			return false;
		    }
		}
	    }
	} else if (d == Direction.EAST) {
	    final GameObject tank = DungeonDiver7.getStuffBag().getGameLogic().getPlayer();
	    if (tank.getSavedObject().isSolid()) {
		return false;
	    }
	    for (u = xFix + 1; u < 24; u++) {
		for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
		    try {
			final var obj = this.getCell(dungeon, u, yFix, zFix, w);
			if (obj.canShoot()) {
			    final var unres = DirectionResolver.unresolve(obj.getDirection());
			    final var invert = DirectionResolver.resolveInvert(unres[0], unres[1]);
			    if (d == invert) {
				this.foundX = u;
				this.foundY = yFix;
				return true;
			    }
			}
			if (obj.isSolid()) {
			    return false;
			}
		    } catch (final ArrayIndexOutOfBoundsException aioobe) {
			return false;
		    }
		}
	    }
	}
	return false;
    }

    @Override
    public int linearScanMagnetic(final Dungeon dungeon, final int xIn, final int yIn, final int zIn,
	    final Direction d) {
	// Perform the scan
	var xFix = xIn;
	var yFix = yIn;
	var zFix = zIn;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    xFix = this.normalizeColumn(xFix);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    yFix = this.normalizeRow(yFix);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    zFix = this.normalizeFloor(zFix);
	}
	int u, w;
	if (d == Direction.NORTH) {
	    for (u = yFix - 1; u >= 0; u--) {
		for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
		    try {
			final var obj = this.getCell(dungeon, xFix, u, zFix, w);
			if (obj.getMaterial() == Material.MAGNETIC) {
			    return yFix - u - 1;
			}
			if (obj.isSolid()) {
			    return 0;
			}
		    } catch (final ArrayIndexOutOfBoundsException aioobe) {
			return 0;
		    }
		}
	    }
	    return 0;
	}
	if (d == Direction.SOUTH) {
	    for (u = yFix + 1; u < 24; u++) {
		for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
		    try {
			final var obj = this.getCell(dungeon, xFix, u, zFix, w);
			if (obj.getMaterial() == Material.MAGNETIC) {
			    return u - yFix - 1;
			}
			if (obj.isSolid()) {
			    return 0;
			}
		    } catch (final ArrayIndexOutOfBoundsException aioobe) {
			return 0;
		    }
		}
	    }
	} else if (d == Direction.WEST) {
	    for (u = xFix - 1; u >= 0; u--) {
		for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
		    try {
			final var obj = this.getCell(dungeon, u, yFix, zFix, w);
			if (obj.getMaterial() == Material.MAGNETIC) {
			    return xFix - u - 1;
			}
			if (obj.isSolid()) {
			    return 0;
			}
		    } catch (final ArrayIndexOutOfBoundsException aioobe) {
			return 0;
		    }
		}
	    }
	} else if (d == Direction.EAST) {
	    for (u = xFix + 1; u < 24; u++) {
		for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
		    try {
			final var obj = this.getCell(dungeon, u, yFix, zFix, w);
			if (obj.getMaterial() == Material.MAGNETIC) {
			    return u - xFix - 1;
			}
			if (obj.isSolid()) {
			    return 0;
			}
		    } catch (final ArrayIndexOutOfBoundsException aioobe) {
			return 0;
		    }
		}
	    }
	}
	return 0;
    }

    @Override
    public void markAsDirty(final Dungeon dungeon, final int row, final int col, final int floor) {
	var fR = row;
	var fC = col;
	var fF = floor;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    fC = this.normalizeColumn(fC);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    fR = this.normalizeRow(fR);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    fF = this.normalizeFloor(fF);
	}
	this.dirtyData.setCell(true, fC, fR, fF);
    }

    @Override
    public void postBattle(final Dungeon dungeon, final AbstractMovingObject m, final int xLoc, final int yLoc,
	    final boolean player) {
	final var saved = m.getSavedObject();
	if (!player) {
	    this.setCell(dungeon, saved, xLoc, yLoc, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
	}
	this.generateOneMonster(dungeon);
    }

    @Override
    public DungeonData readData(final Dungeon dungeon, final DataIOReader reader, final int formatVersion)
	    throws IOException {
	if (FileFormats.isFormatVersionValidGeneration1(formatVersion)) {
	    return CurrentDungeonData.readDataG1(dungeon, reader, formatVersion);
	}
	if (FileFormats.isFormatVersionValidGeneration2(formatVersion)) {
	    return CurrentDungeonData.readDataG2(dungeon, reader, formatVersion);
	}
	if (FileFormats.isFormatVersionValidGeneration3(formatVersion)) {
	    return CurrentDungeonData.readDataG3(dungeon, reader, formatVersion);
	}
	if (FileFormats.isFormatVersionValidGeneration4(formatVersion)) {
	    return CurrentDungeonData.readDataG4(dungeon, reader, formatVersion);
	}
	if (FileFormats.isFormatVersionValidGeneration5(formatVersion)) {
	    return CurrentDungeonData.readDataG5(dungeon, reader, formatVersion);
	}
	if (FileFormats.isFormatVersionValidGeneration6(formatVersion)) {
	    return CurrentDungeonData.readDataG6(dungeon, reader, formatVersion);
	}
	throw new IOException(Strings.error(ErrorString.UNKNOWN_FILE_FORMAT));
    }

    @Override
    public void readSavedState(final DataIOReader reader, final int formatVersion) throws IOException {
	if (FileFormats.isFormatVersionValidGeneration1(formatVersion)
		|| FileFormats.isFormatVersionValidGeneration2(formatVersion)) {
	    this.readSavedStateG2(reader, formatVersion);
	} else if (FileFormats.isFormatVersionValidGeneration3(formatVersion)) {
	    this.readSavedStateG3(reader, formatVersion);
	} else if (FileFormats.isFormatVersionValidGeneration4(formatVersion)) {
	    this.readSavedStateG4(reader, formatVersion);
	} else if (FileFormats.isFormatVersionValidGeneration5(formatVersion)) {
	    this.readSavedStateG5(reader, formatVersion);
	} else if (FileFormats.isFormatVersionValidGeneration6(formatVersion)) {
	    this.readSavedStateG6(reader, formatVersion);
	} else {
	    throw new IOException(Strings.error(ErrorString.UNKNOWN_FILE_FORMAT));
	}
    }

    private void readSavedStateG2(final DataIOReader reader, final int formatVersion) throws IOException {
	int y, x, z, saveSizeX, saveSizeY, saveSizeZ;
	saveSizeX = reader.readInt();
	saveSizeY = reader.readInt();
	saveSizeZ = reader.readInt();
	this.savedState = new DungeonDataStorage(saveSizeY, saveSizeX, saveSizeZ, DungeonConstants.NUM_LAYERS);
	for (x = 0; x < saveSizeX; x++) {
	    for (y = 0; y < saveSizeY; y++) {
		for (z = 0; z < saveSizeZ; z++) {
		    this.savedState.setCell(DungeonDiver7.getStuffBag().getObjects().readV2(reader, formatVersion), y,
			    x, z, DungeonConstants.LAYER_LOWER_GROUND);
		}
	    }
	}
	if (saveSizeX != DungeonData.MIN_COLUMNS || saveSizeY != DungeonData.MIN_ROWS) {
	    this.resizeSavedState(saveSizeZ, new Ground());
	}
    }

    private void readSavedStateG3(final DataIOReader reader, final int formatVersion) throws IOException {
	int y, x, z, saveSizeX, saveSizeY, saveSizeZ;
	saveSizeX = reader.readInt();
	saveSizeY = reader.readInt();
	saveSizeZ = reader.readInt();
	this.savedState = new DungeonDataStorage(saveSizeY, saveSizeX, saveSizeZ, DungeonConstants.NUM_LAYERS);
	for (x = 0; x < saveSizeX; x++) {
	    for (y = 0; y < saveSizeY; y++) {
		for (z = 0; z < saveSizeZ; z++) {
		    this.savedState.setCell(DungeonDiver7.getStuffBag().getObjects().readV3(reader, formatVersion), y,
			    x, z, DungeonConstants.LAYER_LOWER_GROUND);
		}
	    }
	}
	if (saveSizeX != DungeonData.MIN_COLUMNS || saveSizeY != DungeonData.MIN_ROWS) {
	    this.resizeSavedState(saveSizeZ, new Ground());
	}
    }

    private void readSavedStateG4(final DataIOReader reader, final int formatVersion) throws IOException {
	int y, x, z, saveSizeX, saveSizeY, saveSizeZ;
	saveSizeX = reader.readInt();
	saveSizeY = reader.readInt();
	saveSizeZ = reader.readInt();
	this.savedState = new DungeonDataStorage(saveSizeY, saveSizeX, saveSizeZ, DungeonConstants.NUM_LAYERS);
	for (x = 0; x < saveSizeX; x++) {
	    for (y = 0; y < saveSizeY; y++) {
		for (z = 0; z < saveSizeZ; z++) {
		    this.savedState.setCell(DungeonDiver7.getStuffBag().getObjects().readV4(reader, formatVersion), y,
			    x, z, DungeonConstants.LAYER_LOWER_GROUND);
		}
	    }
	}
	if (saveSizeX != DungeonData.MIN_COLUMNS || saveSizeY != DungeonData.MIN_ROWS) {
	    this.resizeSavedState(saveSizeZ, new Ground());
	}
    }

    private void readSavedStateG5(final DataIOReader reader, final int formatVersion) throws IOException {
	int y, x, z, w, saveSizeX, saveSizeY, saveSizeZ;
	saveSizeX = reader.readInt();
	saveSizeY = reader.readInt();
	saveSizeZ = reader.readInt();
	this.savedState = new DungeonDataStorage(saveSizeY, saveSizeX, saveSizeZ, DungeonConstants.NUM_LAYERS);
	for (x = 0; x < saveSizeX; x++) {
	    for (y = 0; y < saveSizeY; y++) {
		for (z = 0; z < saveSizeZ; z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			this.savedState.setCell(DungeonDiver7.getStuffBag().getObjects().readV5(reader, formatVersion),
				y, x, z, w);
		    }
		}
	    }
	}
	if (saveSizeX != DungeonData.MIN_COLUMNS || saveSizeY != DungeonData.MIN_ROWS) {
	    this.resizeSavedState(saveSizeZ, new Ground());
	}
    }

    private void readSavedStateG6(final DataIOReader reader, final int formatVersion) throws IOException {
	int y, x, z, w, saveSizeX, saveSizeY, saveSizeZ;
	saveSizeX = reader.readInt();
	saveSizeY = reader.readInt();
	saveSizeZ = reader.readInt();
	this.savedState = new DungeonDataStorage(saveSizeY, saveSizeX, saveSizeZ, DungeonConstants.NUM_LAYERS);
	for (x = 0; x < saveSizeX; x++) {
	    for (y = 0; y < saveSizeY; y++) {
		for (z = 0; z < saveSizeZ; z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			this.savedState.setCell(DungeonDiver7.getStuffBag().getObjects().readV6(reader, formatVersion),
				y, x, z, w);
		    }
		}
	    }
	}
	if (saveSizeX != DungeonData.MIN_COLUMNS || saveSizeY != DungeonData.MIN_ROWS) {
	    this.resizeSavedState(saveSizeZ, new Ground());
	}
    }

    @Override
    public void redo(final Dungeon dungeon) {
	this.iue.redo();
	this.data = this.iue.getImage();
	this.setAllDirtyFlags();
	this.clearVirtualGrid(dungeon);
    }

    @Override
    public void resetHistoryEngine() {
	this.iue = new ImageUndoEngine();
    }

    @Override
    public void resetVisibleSquares(final int floor) {
	for (var row = 0; row < this.getRows(); row++) {
	    for (var col = 0; col < this.getColumns(); col++) {
		this.visionData.setCell(false, row, col, floor);
	    }
	}
    }

    @Override
    public void resize(final Dungeon dungeon, final int zIn, final GameObject nullFill) {
	final var x = DungeonData.MIN_ROWS;
	final var y = DungeonData.MIN_COLUMNS;
	var z = zIn;
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    z = this.normalizeFloor(z);
	}
	// Allocate temporary storage array
	final var tempStorage = new DungeonDataStorage(y, x, z, DungeonConstants.NUM_LAYERS);
	// Copy existing maze into temporary array
	int u, v, w, t;
	for (u = 0; u < y; u++) {
	    for (v = 0; v < x; v++) {
		for (w = 0; w < z; w++) {
		    for (t = 0; t < DungeonConstants.NUM_LAYERS; t++) {
			try {
			    tempStorage.setCell(this.getCell(dungeon, v, u, w, t), u, v, w, t);
			} catch (final ArrayIndexOutOfBoundsException aioob) {
			    // Do nothing
			}
		    }
		}
	    }
	}
	// Set the current data to the temporary array
	this.data = tempStorage;
	this.virtualData = new DungeonDataStorage(x, y, z, DungeonConstants.NUM_VIRTUAL_LAYERS);
	this.dirtyData = new FlagStorage(x, y, z);
	// Fill any blanks
	this.fillNulls(dungeon, nullFill, null, false);
	// Fix saved tower state
	this.resizeSavedState(z, nullFill);
    }

    @Override
    public void resizeSavedState(final int z, final GameObject nullFill) {
	final var x = DungeonData.MIN_ROWS;
	final var y = DungeonData.MIN_COLUMNS;
	// Allocate temporary storage array
	final var tempStorage = new DungeonDataStorage(y, x, z, DungeonConstants.NUM_LAYERS);
	// Copy existing maze into temporary array
	int u, v, w, t;
	for (u = 0; u < y; u++) {
	    for (v = 0; v < x; v++) {
		for (w = 0; w < z; w++) {
		    for (t = 0; t < DungeonConstants.NUM_LAYERS; t++) {
			try {
			    tempStorage.setCell(this.savedState.getCell(v, u, w, t), u, v, w, t);
			} catch (final ArrayIndexOutOfBoundsException aioob) {
			    // Do nothing
			}
		    }
		}
	    }
	}
	// Set the current data to the temporary array
	this.savedState = tempStorage;
	// Fill any blanks
	this.fillSTSNulls(nullFill);
    }

    @Override
    public void restore(final Dungeon dungeon) {
	int y, x, z, w;
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (z = 0; z < this.getFloors(); z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			this.setCell(dungeon, ((GameObject) this.savedState.getCell(x, y, z, w)).clone(), y, x, z,
				w);
		    }
		}
	    }
	}
    }

    @Override
    public void save(final Dungeon dungeon) {
	int y, x, z, w;
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (z = 0; z < this.getFloors(); z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			this.savedState.setCell(this.getCell(dungeon, y, x, z, w).clone(), x, y, z, w);
		    }
		}
	    }
	}
    }

    @Override
    public void setAllDirtyFlags() {
	for (var floor = 0; floor < this.getFloors(); floor++) {
	    this.setDirtyFlags(floor);
	}
    }

    @Override
    public void setCell(final Dungeon dungeon, final GameObject mo, final int row, final int col, final int floor,
	    final int layer) {
	var fR = row;
	var fC = col;
	var fF = floor;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    fC = this.normalizeColumn(fC);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    fR = this.normalizeRow(fR);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    fF = this.normalizeFloor(fF);
	}
	this.data.setDungeonDataCell(mo, fC, fR, fF, layer);
	this.dirtyData.setCell(true, fC, fR, fF);
    }

    @Override
    public void setDirtyFlags(final int floor) {
	for (var row = 0; row < this.getRows(); row++) {
	    for (var col = 0; col < this.getColumns(); col++) {
		this.dirtyData.setCell(true, col, row, floor);
	    }
	}
    }

    @Override
    public void setVirtualCell(final Dungeon dungeon, final GameObject mo, final int row, final int col,
	    final int floor, final int layer) {
	var fR = row;
	var fC = col;
	var fF = floor;
	if (dungeon.isVerticalWraparoundEnabled()) {
	    fC = this.normalizeColumn(fC);
	}
	if (dungeon.isHorizontalWraparoundEnabled()) {
	    fR = this.normalizeRow(fR);
	}
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    fF = this.normalizeFloor(fF);
	}
	this.virtualData.setDungeonDataCell(mo, fC, fR, fF, layer);
	this.dirtyData.setCell(true, fC, fR, fF);
    }

    @Override
    public void tickTimers(final Dungeon dungeon) {
	// Tick all object timers
	int y, x, z, w;
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (z = 0; z < this.getFloors(); z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			final var mo = this.getCell(dungeon, y, x, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
			if (mo != null) {
			    mo.tickTimer(y, x, 0);
			}
		    }
		}
	    }
	}
    }

    @Override
    public void tickTimers(final Dungeon dungeon, final int floor, final int actionType) {
	var floorFix = floor;
	if (dungeon.isThirdDimensionWraparoundEnabled()) {
	    floorFix = this.normalizeFloor(floorFix);
	}
	int x, y, z, w;
	// Tick all DungeonObject timers
	GameObject.checkTunnels();
	for (z = Direction.NORTH.ordinal(); z <= Direction.NORTH_WEST.ordinal(); z += 2) {
	    for (x = 0; x < this.getColumns(); x++) {
		for (y = 0; y < this.getRows(); y++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			final var mo = this.getCell(dungeon, y, x, floorFix, w);
			if (mo != null && z == Direction.NORTH.ordinal()) {
			    // Handle objects waiting for a tunnel to open
			    if (mo instanceof final GameObject gmo) {
				final var saved = gmo.getSavedObject();
				if (saved instanceof GameObject) {
				    final var color = saved.getColor();
				    if (gmo.waitingOnTunnel() && !GameObject.tunnelsFull(color)) {
					gmo.setWaitingOnTunnel(false);
					saved.pushIntoAction(gmo, y, x, floorFix);
				    }
				    if (GameObject.tunnelsFull(color)) {
					gmo.setWaitingOnTunnel(true);
				    }
				}
			    }
			    mo.tickTimer(y, x, actionType);
			}
		    }
		}
	    }
	}
    }

    @Override
    public boolean tryRedo() {
	return this.iue.tryRedo();
    }

    @Override
    public boolean tryUndo() {
	return this.iue.tryUndo();
    }

    @Override
    public void undo(final Dungeon dungeon) {
	this.iue.undo();
	this.data = this.iue.getImage();
	this.setAllDirtyFlags();
	this.clearVirtualGrid(dungeon);
    }

    @Override
    public void updateMonsterPosition(final Dungeon dungeon, final Direction move, final int xLoc, final int yLoc,
	    final AbstractMovingObject monster, final int pi) {
	final var app = DungeonDiver7.getStuffBag();
	final var dirMove = DirectionResolver.unresolve(move);
	final var pLocX = dungeon.getPlayerLocationX(pi);
	final var pLocY = dungeon.getPlayerLocationY(pi);
	try {
	    final var there = this.getCell(dungeon, xLoc + dirMove[0], yLoc + dirMove[1], 0,
		    DungeonConstants.LAYER_LOWER_OBJECTS);
	    final var ground = this.getCell(dungeon, xLoc + dirMove[0], yLoc + dirMove[1], 0,
		    DungeonConstants.LAYER_LOWER_GROUND);
	    if (!there.isSolid() && !(there instanceof AbstractMovingObject)) {
		if (Dungeon.radialScan(xLoc, yLoc, 0, pLocX, pLocY)) {
		    if (app.getMode() != StuffBag.STATUS_BATTLE) {
			app.getGameLogic().stopMovement();
			if (monster instanceof FinalBossMonsterTile) {
			    app.getBattle().doFinalBossBattle();
			} else {
			    app.getBattle().doBattle();
			    this.postBattle(dungeon, monster, xLoc, yLoc, false);
			}
		    }
		} else {
		    // Move the monster
		    this.setCell(dungeon, monster.getSavedObject(), xLoc, yLoc, 0,
			    DungeonConstants.LAYER_LOWER_OBJECTS);
		    monster.setSavedObject(there);
		    this.setCell(dungeon, monster, xLoc + dirMove[0], yLoc + dirMove[1], 0,
			    DungeonConstants.LAYER_LOWER_OBJECTS);
		    // Does the ground have friction?
		    if (!ground.hasFriction()) {
			// No - move the monster again
			this.updateMonsterPosition(dungeon, move, xLoc + dirMove[0], yLoc + dirMove[1], monster, pi);
		    }
		}
	    }
	} catch (final ArrayIndexOutOfBoundsException aioob) {
	    // Do nothing
	}
    }

    @Override
    public void updateRedoHistory(final HistoryStatus whatWas) {
	this.iue.updateRedoHistory(new DungeonDataStorage(this.data), whatWas);
    }

    @Override
    public void updateUndoHistory(final HistoryStatus whatWas) {
	this.iue.updateUndoHistory(new DungeonDataStorage(this.data), whatWas);
    }

    @Override
    public void updateVisibleSquares(final Dungeon dungeon, final int xp, final int yp, final int zp) {
	if ((this.visionMode | VisionModes.EXPLORE) == this.visionMode) {
	    for (var x = xp - this.visionModeExploreRadius; x <= xp + this.visionModeExploreRadius; x++) {
		for (var y = yp - this.visionModeExploreRadius; y <= yp + this.visionModeExploreRadius; y++) {
		    int fx, fy;
		    if (dungeon.isHorizontalWraparoundEnabled()) {
			fx = this.normalizeColumn(x);
		    } else {
			fx = x;
		    }
		    if (dungeon.isVerticalWraparoundEnabled()) {
			fy = this.normalizeRow(y);
		    } else {
			fy = y;
		    }
		    var alreadyVisible = false;
		    try {
			alreadyVisible = this.visionData.getCell(fx, fy);
		    } catch (final ArrayIndexOutOfBoundsException aioobe) {
			// Ignore
		    }
		    if (!alreadyVisible) {
			if ((this.visionMode | VisionModes.LOS) == this.visionMode) {
			    if (this.isSquareVisibleLOS(dungeon, x, y, xp, yp, zp)) {
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

    @Override
    public void writeData(final Dungeon dungeon, final DataIOWriter writer) throws IOException {
	int y, x, z, w;
	writer.writeInt(this.getColumns());
	writer.writeInt(this.getRows());
	writer.writeInt(this.getFloors());
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (z = 0; z < this.getFloors(); z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			this.getCell(dungeon, y, x, z, w).write(writer);
		    }
		}
	    }
	}
    }

    @Override
    public void writeSavedState(final DataIOWriter writer) throws IOException {
	int y, x, z, w;
	writer.writeInt(this.getColumns());
	writer.writeInt(this.getRows());
	writer.writeInt(this.getFloors());
	for (x = 0; x < this.getColumns(); x++) {
	    for (y = 0; y < this.getRows(); y++) {
		for (z = 0; z < this.getFloors(); z++) {
		    for (w = 0; w < DungeonConstants.NUM_LAYERS; w++) {
			((GameObject) this.savedState.getCell(y, x, z, w)).write(writer);
		    }
		}
	    }
	}
    }
}
