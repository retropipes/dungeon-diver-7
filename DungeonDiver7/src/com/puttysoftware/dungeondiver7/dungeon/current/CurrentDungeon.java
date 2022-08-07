/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.current;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeonData;
import com.puttysoftware.dungeondiver7.dungeon.AbstractPrefixIO;
import com.puttysoftware.dungeondiver7.dungeon.AbstractSuffixIO;
import com.puttysoftware.dungeondiver7.dungeon.HistoryStatus;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractButton;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractButtonDoor;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractCharacter;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractTunnel;
import com.puttysoftware.dungeondiver7.locale.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.LocaleLoader;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utility.DifficultyConstants;
import com.puttysoftware.dungeondiver7.utility.Direction;
import com.puttysoftware.dungeondiver7.utility.Extension;
import com.puttysoftware.dungeondiver7.utility.FormatConstants;
import com.puttysoftware.fileio.XDataReader;
import com.puttysoftware.fileio.XDataWriter;
import com.puttysoftware.fileutils.FileUtilities;
import com.puttysoftware.randomrange.RandomLongRange;

public class CurrentDungeon extends AbstractDungeon {
    // Properties
    private CurrentDungeonData dungeonData;
    private CurrentDungeonData clipboard;
    private DungeonLevelInfo infoClipboard;
    private int levelCount;
    private int activeLevel;
    private int activeEra;
    private String basePath;
    private AbstractPrefixIO prefixHandler;
    private AbstractSuffixIO suffixHandler;
    private String musicFilename;
    private boolean moveShootAllowed;
    private final ArrayList<DungeonLevelInfo> levelInfoData;
    private ArrayList<String> levelInfoList;

    // Constructors
    public CurrentDungeon() throws IOException {
	super();
	this.dungeonData = null;
	this.clipboard = null;
	this.levelCount = 0;
	this.activeLevel = 0;
	this.activeEra = 0;
	this.prefixHandler = null;
	this.suffixHandler = null;
	this.musicFilename = "null";
	this.moveShootAllowed = false;
	this.levelInfoData = new ArrayList<>();
	this.levelInfoList = new ArrayList<>();
	final long random = new RandomLongRange(0, Long.MAX_VALUE).generate();
	final String randomID = Long.toHexString(random);
	this.basePath = System
		.getProperty(LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_TEMP_DIR))
		+ File.separator
		+ LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_PROGRAM_NAME)
		+ File.separator + randomID + LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_ARENA_FORMAT_FOLDER);
	final File base = new File(this.basePath);
	final boolean res = base.mkdirs();
	if (!res) {
	    throw new IOException(
		    LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE, LocaleConstants.ERROR_STRING_TEMP_DIR));
	}
    }

    // Methods
    @Override
    public String getDungeonTempMusicFolder() {
	return this.basePath + File.separator
		+ LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_MUSIC_FOLDER)
		+ File.separator;
    }

    @Override
    public boolean isMoveShootAllowed() {
	return this.isMoveShootAllowedGlobally() && this.isMoveShootAllowedThisLevel();
    }

    @Override
    public boolean isMoveShootAllowedGlobally() {
	return this.moveShootAllowed;
    }

    @Override
    public boolean isMoveShootAllowedThisLevel() {
	return this.levelInfoData.get(this.activeLevel).isMoveShootAllowed();
    }

    @Override
    public void setMoveShootAllowedGlobally(final boolean value) {
	this.moveShootAllowed = value;
    }

    @Override
    public void setMoveShootAllowedThisLevel(final boolean value) {
	this.levelInfoData.get(this.activeLevel).setMoveShootAllowed(value);
    }

    @Override
    public String getMusicFilename() {
	return this.musicFilename;
    }

    @Override
    public void setMusicFilename(final String newMusicFilename) {
	this.musicFilename = newMusicFilename;
    }

    @Override
    public String getName() {
	return this.levelInfoData.get(this.activeLevel).getName();
    }

    @Override
    public void setName(final String newName) {
	this.levelInfoData.get(this.activeLevel).setName(newName);
	this.levelInfoList.set(this.activeLevel, this.generateCurrentLevelInfo());
    }

    @Override
    public String getHint() {
	return this.levelInfoData.get(this.activeLevel).getHint();
    }

    @Override
    public void setHint(final String newHint) {
	this.levelInfoData.get(this.activeLevel).setHint(newHint);
    }

    @Override
    public String getAuthor() {
	return this.levelInfoData.get(this.activeLevel).getAuthor();
    }

    @Override
    public void setAuthor(final String newAuthor) {
	this.levelInfoData.get(this.activeLevel).setAuthor(newAuthor);
	this.levelInfoList.set(this.activeLevel, this.generateCurrentLevelInfo());
    }

    @Override
    public int getDifficulty() {
	return this.levelInfoData.get(this.activeLevel).getDifficulty();
    }

    @Override
    public void setDifficulty(final int newDifficulty) {
	this.levelInfoData.get(this.activeLevel).setDifficulty(newDifficulty);
	this.levelInfoList.set(this.activeLevel, this.generateCurrentLevelInfo());
    }

    @Override
    public String getBasePath() {
	return this.basePath;
    }

    @Override
    public void setPrefixHandler(final AbstractPrefixIO xph) {
	this.prefixHandler = xph;
    }

    @Override
    public void setSuffixHandler(final AbstractSuffixIO xsh) {
	this.suffixHandler = xsh;
    }

    @Override
    public int getActiveLevelNumber() {
	return this.activeLevel;
    }

    @Override
    public int getActiveEraNumber() {
	return this.activeEra;
    }

    @Override
    public String[] getLevelInfoList() {
	return this.levelInfoList.toArray(new String[this.levelInfoList.size()]);
    }

    @Override
    public void generateLevelInfoList() {
	final int saveLevel = this.getActiveLevelNumber();
	final ArrayList<String> tempStorage = new ArrayList<>();
	for (int x = 0; x < this.levelCount; x++) {
	    this.switchLevel(x);
	    tempStorage.add(this.generateCurrentLevelInfo());
	}
	this.switchLevel(saveLevel);
	this.levelInfoList = tempStorage;
    }

    private String generateCurrentLevelInfo() {
	final StringBuilder sb = new StringBuilder();
	sb.append(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
		LocaleConstants.DIALOG_STRING_ARENA_LEVEL));
	sb.append(LocaleConstants.COMMON_STRING_SPACE);
	sb.append(this.getActiveLevelNumber() + 1);
	sb.append(LocaleConstants.COMMON_STRING_COLON + LocaleConstants.COMMON_STRING_SPACE);
	sb.append(this.getName().trim());
	sb.append(LocaleConstants.COMMON_STRING_SPACE);
	sb.append(LocaleLoader.loadString(LocaleConstants.DIALOG_STRINGS_FILE,
		LocaleConstants.DIALOG_STRING_ARENA_LEVEL_BY));
	sb.append(LocaleConstants.COMMON_STRING_SPACE);
	sb.append(this.getAuthor().trim());
	sb.append(LocaleConstants.COMMON_STRING_SPACE);
	sb.append(LocaleConstants.COMMON_STRING_OPEN_PARENTHESES);
	sb.append(CurrentDungeon.convertDifficultyNumberToName(this.getDifficulty()));
	sb.append(LocaleConstants.COMMON_STRING_CLOSE_PARENTHESES);
	return sb.toString();
    }

    private static String convertDifficultyNumberToName(final int number) {
	return DifficultyConstants.getDifficultyNames()[number - 1];
    }

    @Override
    public void switchLevel(final int level) {
	this.switchInternal(level, this.activeEra);
    }

    @Override
    public void switchLevelOffset(final int level) {
	this.switchInternal(this.activeLevel + level, this.activeEra);
    }

    @Override
    public void switchEra(final int era) {
	this.switchInternal(this.activeLevel, era);
    }

    @Override
    public void switchEraOffset(final int era) {
	this.switchInternal(this.activeLevel, this.activeEra + era);
    }

    @Override
    protected void switchInternal(final int level, final int era) {
	if (this.activeLevel != level || this.activeEra != era || this.dungeonData == null) {
	    if (this.dungeonData != null) {
		try (XDataWriter writer = this.getLevelWriter()) {
		    // Save old level
		    this.writeDungeonLevel(writer);
		    writer.close();
		} catch (final IOException io) {
		    // Ignore
		}
	    }
	    this.activeLevel = level;
	    this.activeEra = era;
	    try (XDataReader reader = this.getLevelReaderG6()) {
		// Load new level
		this.readDungeonLevel(reader);
		reader.close();
	    } catch (final IOException io) {
		// Ignore
	    }
	}
    }

    @Override
    public boolean doesLevelExist(final int level) {
	return level < this.levelCount && level >= 0;
    }

    @Override
    public boolean doesLevelExistOffset(final int level) {
	return this.activeLevel + level < this.levelCount && this.activeLevel + level >= 0;
    }

    @Override
    public void cutLevel() {
	if (this.levelCount > 1) {
	    this.clipboard = this.dungeonData;
	    this.infoClipboard = this.levelInfoData.get(this.activeLevel);
	    this.removeActiveLevel();
	}
    }

    @Override
    public void copyLevel() {
	this.clipboard = this.dungeonData.clone();
	this.infoClipboard = this.levelInfoData.get(this.activeLevel).clone();
    }

    @Override
    public void pasteLevel() {
	if (this.clipboard != null) {
	    this.dungeonData = this.clipboard.clone();
	    this.levelInfoData.set(this.activeLevel, this.infoClipboard.clone());
	    this.levelInfoList.set(this.activeLevel, this.generateCurrentLevelInfo());
	    DungeonDiver7.getApplication().getDungeonManager().setDirty(true);
	}
    }

    @Override
    public boolean isPasteBlocked() {
	return this.clipboard == null;
    }

    @Override
    public boolean isCutBlocked() {
	return this.levelCount <= 1;
    }

    @Override
    public boolean insertLevelFromClipboard() {
	if (this.levelCount < AbstractDungeon.MAX_LEVELS) {
	    this.dungeonData = this.clipboard.clone();
	    this.levelCount++;
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean addLevel() {
	if (this.levelCount < AbstractDungeon.MAX_LEVELS) {
	    if (this.dungeonData != null) {
		try (XDataWriter writer = this.getLevelWriter()) {
		    // Save old level
		    this.writeDungeonLevel(writer);
		    writer.close();
		} catch (final IOException io) {
		    // Ignore
		}
	    }
	    // Add all eras for the new level
	    final int saveEra = this.activeEra;
	    this.dungeonData = new CurrentDungeonData();
	    for (int e = 0; e < AbstractDungeon.ERA_COUNT; e++) {
		this.switchEra(e);
		this.dungeonData = new CurrentDungeonData();
	    }
	    this.switchEra(saveEra);
	    // Clean up
	    this.levelCount++;
	    this.activeLevel = this.levelCount - 1;
	    this.levelInfoData.add(new DungeonLevelInfo());
	    this.levelInfoList.add(this.generateCurrentLevelInfo());
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    protected boolean removeActiveLevel() {
	if (this.levelCount > 1) {
	    if (this.activeLevel >= 0 && this.activeLevel <= this.levelCount) {
		this.dungeonData = null;
		// Delete all files corresponding to current level
		for (int e = 0; e < AbstractDungeon.ERA_COUNT; e++) {
		    final boolean res = this.getLevelFile(this.activeLevel, e).delete();
		    if (!res) {
			return false;
		    }
		}
		// Shift all higher-numbered levels down
		for (int x = this.activeLevel; x < this.levelCount - 1; x++) {
		    for (int e = 0; e < AbstractDungeon.ERA_COUNT; e++) {
			final File sourceLocation = this.getLevelFile(x + 1, e);
			final File targetLocation = this.getLevelFile(x, e);
			try {
			    FileUtilities.moveFile(sourceLocation, targetLocation);
			} catch (final IOException io) {
			    // Ignore
			}
		    }
		}
		this.levelCount--;
		this.levelInfoData.remove(this.activeLevel);
		this.levelInfoList.remove(this.activeLevel);
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    @Override
    public boolean isCellDirty(final int row, final int col, final int floor) {
	return this.dungeonData.isCellDirty(this, row, col, floor);
    }

    @Override
    public AbstractDungeonObject getCell(final int row, final int col, final int floor, final int layer) {
	return this.dungeonData.getCell(this, row, col, floor, layer);
    }

    @Override
    public AbstractDungeonObject getVirtualCell(final int row, final int col, final int floor, final int layer) {
	return this.dungeonData.getVirtualCell(this, row, col, floor, layer);
    }

    @Override
    public int getStartRow(final int pi) {
	return this.levelInfoData.get(this.activeLevel).getStartRow(pi);
    }

    @Override
    public int getStartColumn(final int pi) {
	return this.levelInfoData.get(this.activeLevel).getStartColumn(pi);
    }

    @Override
    public int getStartFloor(final int pi) {
	return this.levelInfoData.get(this.activeLevel).getStartFloor(pi);
    }

    public static int getStartLevel() {
	return 0;
    }

    @Override
    public int getRows() {
	return this.dungeonData.getRows();
    }

    @Override
    public int getColumns() {
	return this.dungeonData.getColumns();
    }

    @Override
    public int getFloors() {
	return this.dungeonData.getFloors();
    }

    @Override
    public int getLevels() {
	return this.levelCount;
    }

    @Override
    public boolean doesPlayerExist(final int pi) {
	return this.levelInfoData.get(this.activeLevel).doesPlayerExist(pi);
    }

    @Override
    public int[] findPlayer(final int number) {
	return this.dungeonData.findPlayer(this, number);
    }

    @Override
    public void tickTimers(final int floor, final int actionType) {
	this.dungeonData.tickTimers(this, floor, actionType);
    }

    @Override
    public void checkForEnemies(final int floor, final int ex, final int ey, final AbstractCharacter e) {
	this.dungeonData.checkForEnemies(this, floor, ex, ey, e);
    }

    @Override
    public int checkForMagnetic(final int floor, final int centerX, final int centerY, final Direction dir) {
	return this.dungeonData.checkForMagnetic(this, floor, centerX, centerY, dir);
    }

    @Override
    public int[] circularScan(final int x, final int y, final int z, final int maxR, final String targetName,
	    final boolean moved) {
	return this.dungeonData.circularScan(this, x, y, z, maxR, targetName, moved);
    }

    @Override
    public int[] circularScanTunnel(final int x, final int y, final int z, final int maxR, final int tx, final int ty,
	    final AbstractTunnel target, final boolean moved) {
	return this.dungeonData.circularScanTunnel(this, x, y, z, maxR, tx, ty, target, moved);
    }

    @Override
    public void circularScanRange(final int x, final int y, final int z, final int maxR, final int rangeType,
	    final int forceUnits) {
	this.dungeonData.circularScanRange(this, x, y, z, maxR, rangeType, forceUnits);
    }

    @Override
    public int[] findObject(final int z, final String targetName) {
	return this.dungeonData.findObject(this, z, targetName);
    }

    @Override
    public boolean circularScanTank(final int x, final int y, final int z, final int maxR) {
	return this.dungeonData.circularScanTank(this, x, y, z, maxR);
    }

    @Override
    public void fullScanKillTanks() {
	this.dungeonData.fullScanKillTanks(this);
    }

    @Override
    public void fullScanFreezeGround() {
	this.dungeonData.fullScanFreezeGround(this);
    }

    @Override
    public void fullScanAllButtonOpen(final int z, final AbstractButton source) {
	this.dungeonData.fullScanAllButtonOpen(this, z, source);
    }

    @Override
    public void fullScanAllButtonClose(final int z, final AbstractButton source) {
	this.dungeonData.fullScanAllButtonClose(this, z, source);
    }

    @Override
    public void fullScanButtonBind(final int dx, final int dy, final int z, final AbstractButtonDoor source) {
	this.dungeonData.fullScanButtonBind(this, dx, dy, z, source);
    }

    @Override
    public void fullScanButtonCleanup(final int px, final int py, final int z, final AbstractButton button) {
	this.dungeonData.fullScanButtonCleanup(this, px, py, z, button);
    }

    @Override
    public void fullScanFindButtonLostDoor(final int z, final AbstractButtonDoor door) {
	this.dungeonData.fullScanFindButtonLostDoor(this, z, door);
    }

    @Override
    public void setCell(final AbstractDungeonObject mo, final int row, final int col, final int floor,
	    final int layer) {
	this.dungeonData.setCell(this, mo, row, col, floor, layer);
    }

    @Override
    public void setVirtualCell(final AbstractDungeonObject mo, final int row, final int col, final int floor,
	    final int layer) {
	this.dungeonData.setVirtualCell(this, mo, row, col, floor, layer);
    }

    @Override
    public void markAsDirty(final int row, final int col, final int floor) {
	this.dungeonData.markAsDirty(this, row, col, floor);
    }

    @Override
    public void clearDirtyFlags(final int floor) {
	this.dungeonData.clearDirtyFlags(floor);
    }

    @Override
    public void setDirtyFlags(final int floor) {
	this.dungeonData.setDirtyFlags(floor);
    }

    @Override
    public void clearVirtualGrid() {
	this.dungeonData.clearVirtualGrid(this);
    }

    @Override
    public void setStartRow(final int pi, final int newStartRow) {
	this.levelInfoData.get(this.activeLevel).setStartRow(pi, newStartRow);
    }

    @Override
    public void setStartColumn(final int pi, final int newStartColumn) {
	this.levelInfoData.get(this.activeLevel).setStartColumn(pi, newStartColumn);
    }

    @Override
    public void setStartFloor(final int pi, final int newStartFloor) {
	this.levelInfoData.get(this.activeLevel).setStartFloor(pi, newStartFloor);
    }

    @Override
    public void fillDefault() {
	final AbstractDungeonObject fill = PrefsManager.getEditorDefaultFill();
	this.dungeonData.fill(this, fill);
    }

    @Override
    public void save() {
	this.dungeonData.save(this);
    }

    @Override
    public void restore() {
	this.dungeonData.restore(this);
    }

    @Override
    public void resize(final int z, final AbstractDungeonObject nullFill) {
	this.dungeonData.resize(this, z, nullFill);
    }

    @Override
    public void setData(final AbstractDungeonData newData, final int count) {
	if (newData instanceof CurrentDungeonData) {
	    this.dungeonData = (CurrentDungeonData) newData;
	    this.levelCount = count;
	}
    }

    @Override
    public void enableHorizontalWraparound() {
	this.levelInfoData.get(this.activeLevel).enableHorizontalWraparound();
    }

    @Override
    public void disableHorizontalWraparound() {
	this.levelInfoData.get(this.activeLevel).disableHorizontalWraparound();
    }

    @Override
    public void enableVerticalWraparound() {
	this.levelInfoData.get(this.activeLevel).enableVerticalWraparound();
    }

    @Override
    public void disableVerticalWraparound() {
	this.levelInfoData.get(this.activeLevel).disableVerticalWraparound();
    }

    @Override
    public void enableThirdDimensionWraparound() {
	this.levelInfoData.get(this.activeLevel).enableThirdDimensionWraparound();
    }

    @Override
    public void disableThirdDimensionWraparound() {
	this.levelInfoData.get(this.activeLevel).disableThirdDimensionWraparound();
    }

    @Override
    public boolean isHorizontalWraparoundEnabled() {
	return this.levelInfoData.get(this.activeLevel).isHorizontalWraparoundEnabled();
    }

    @Override
    public boolean isVerticalWraparoundEnabled() {
	return this.levelInfoData.get(this.activeLevel).isVerticalWraparoundEnabled();
    }

    @Override
    public boolean isThirdDimensionWraparoundEnabled() {
	return this.levelInfoData.get(this.activeLevel).isThirdDimensionWraparoundEnabled();
    }

    @Override
    public CurrentDungeon readDungeon() throws IOException {
	final CurrentDungeon m = new CurrentDungeon();
	// Attach handlers
	m.setPrefixHandler(this.prefixHandler);
	m.setSuffixHandler(this.suffixHandler);
	// Make base paths the same
	m.basePath = this.basePath;
	int version = -1;
	// Create metafile reader
	try (XDataReader metaReader = new XDataReader(
		m.basePath + File.separator
			+ LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
				LocaleConstants.NOTL_STRING_ARENA_FORMAT_METAFILE)
			+ Extension.getDungeonLevelExtensionWithPeriod(),
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_ARENA_FORMAT_ARENA))) {
	    // Read metafile
	    version = m.readDungeonMetafileVersion(metaReader);
	    if (FormatConstants.isFormatVersionValidGeneration6(version)) {
		m.readDungeonMetafileG6(metaReader, version);
	    } else if (FormatConstants.isFormatVersionValidGeneration4(version)
		    || FormatConstants.isFormatVersionValidGeneration5(version)) {
		m.readDungeonMetafileG4(metaReader, version);
	    } else {
		m.readDungeonMetafileG3(metaReader, version);
	    }
	} catch (final IOException ioe) {
	    throw ioe;
	}
	if (!FormatConstants.isLevelListStored(version)) {
	    // Create data reader
	    try (XDataReader dataReader = m.getLevelReaderG5()) {
		// Read data
		m.readDungeonLevel(dataReader, version);
	    } catch (final IOException ioe) {
		throw ioe;
	    }
	    // Update level info
	    m.generateLevelInfoList();
	} else {
	    // Create data reader
	    try (XDataReader dataReader = m.getLevelReaderG6()) {
		// Read data
		m.readDungeonLevel(dataReader, version);
	    } catch (final IOException ioe) {
		throw ioe;
	    }
	}
	return m;
    }

    private XDataReader getLevelReaderG5() throws IOException {
	return new XDataReader(
		this.basePath + File.separator
			+ LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
				LocaleConstants.NOTL_STRING_ARENA_FORMAT_LEVEL)
			+ this.activeLevel + Extension.getDungeonLevelExtensionWithPeriod(),
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_ARENA_FORMAT_LEVEL));
    }

    private XDataReader getLevelReaderG6() throws IOException {
	return new XDataReader(
		this.basePath + File.separator
			+ LocaleLoader.loadString(
				LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_ARENA_FORMAT_LEVEL)
			+ this.activeLevel
			+ LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
				LocaleConstants.NOTL_STRING_ARENA_FORMAT_ERA)
			+ this.activeEra + Extension.getDungeonLevelExtensionWithPeriod(),
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_ARENA_FORMAT_LEVEL));
    }

    private int readDungeonMetafileVersion(final XDataReader reader) throws IOException {
	int ver = FormatConstants.ARENA_FORMAT_LATEST;
	if (this.prefixHandler != null) {
	    ver = this.prefixHandler.readPrefix(reader);
	}
	this.moveShootAllowed = FormatConstants.isMoveShootAllowed(ver);
	return ver;
    }

    private void readDungeonMetafileG3(final XDataReader reader, final int ver) throws IOException {
	this.levelCount = reader.readInt();
	this.musicFilename = "null";
	if (this.suffixHandler != null) {
	    this.suffixHandler.readSuffix(reader, ver);
	}
    }

    private void readDungeonMetafileG4(final XDataReader reader, final int ver) throws IOException {
	this.levelCount = reader.readInt();
	this.musicFilename = reader.readString();
	if (this.suffixHandler != null) {
	    this.suffixHandler.readSuffix(reader, ver);
	}
    }

    private void readDungeonMetafileG6(final XDataReader reader, final int ver) throws IOException {
	this.levelCount = reader.readInt();
	this.musicFilename = reader.readString();
	this.moveShootAllowed = reader.readBoolean();
	for (int l = 0; l < this.levelCount; l++) {
	    this.levelInfoData.add(DungeonLevelInfo.readLevelInfo(reader));
	    this.levelInfoList.add(reader.readString());
	}
	if (this.suffixHandler != null) {
	    this.suffixHandler.readSuffix(reader, ver);
	}
    }

    private void readDungeonLevel(final XDataReader reader) throws IOException {
	this.readDungeonLevel(reader, FormatConstants.ARENA_FORMAT_LATEST);
    }

    private void readDungeonLevel(final XDataReader reader, final int formatVersion) throws IOException {
	this.dungeonData = (CurrentDungeonData) new CurrentDungeonData().readData(this, reader, formatVersion);
	this.dungeonData.readSavedState(reader, formatVersion);
    }

    private File getLevelFile(final int level, final int era) {
	return new File(this.basePath + File.separator
		+ LocaleLoader
			.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_ARENA_FORMAT_LEVEL)
		+ level
		+ LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_ARENA_FORMAT_ERA)
		+ era + Extension.getDungeonLevelExtensionWithPeriod());
    }

    @Override
    public void writeDungeon() throws IOException {
	// Create metafile writer
	try (XDataWriter metaWriter = new XDataWriter(
		this.basePath + File.separator
			+ LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
				LocaleConstants.NOTL_STRING_ARENA_FORMAT_METAFILE)
			+ Extension.getDungeonLevelExtensionWithPeriod(),
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_ARENA_FORMAT_ARENA))) {
	    // Write metafile
	    this.writeDungeonMetafile(metaWriter);
	} catch (final IOException ioe) {
	    throw ioe;
	}
	// Create data writer
	try (XDataWriter dataWriter = this.getLevelWriter()) {
	    // Write data
	    this.writeDungeonLevel(dataWriter);
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    private XDataWriter getLevelWriter() throws IOException {
	return new XDataWriter(
		this.basePath + File.separator
			+ LocaleLoader.loadString(
				LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_ARENA_FORMAT_LEVEL)
			+ this.activeLevel
			+ LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
				LocaleConstants.NOTL_STRING_ARENA_FORMAT_ERA)
			+ this.activeEra + Extension.getDungeonLevelExtensionWithPeriod(),
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			LocaleConstants.NOTL_STRING_ARENA_FORMAT_LEVEL));
    }

    private void writeDungeonMetafile(final XDataWriter writer) throws IOException {
	if (this.prefixHandler != null) {
	    this.prefixHandler.writePrefix(writer);
	}
	writer.writeInt(this.levelCount);
	writer.writeString(this.musicFilename);
	writer.writeBoolean(this.moveShootAllowed);
	for (int l = 0; l < this.levelCount; l++) {
	    this.levelInfoData.get(l).writeLevelInfo(writer);
	    writer.writeString(this.levelInfoList.get(l));
	}
	if (this.suffixHandler != null) {
	    this.suffixHandler.writeSuffix(writer);
	}
    }

    private void writeDungeonLevel(final XDataWriter writer) throws IOException {
	// Write the level
	this.dungeonData.writeData(this, writer);
	this.dungeonData.writeSavedState(writer);
    }

    @Override
    public void undo() {
	this.dungeonData.undo(this);
    }

    @Override
    public void redo() {
	this.dungeonData.redo(this);
    }

    @Override
    public boolean tryUndo() {
	return this.dungeonData.tryUndo();
    }

    @Override
    public boolean tryRedo() {
	return this.dungeonData.tryRedo();
    }

    @Override
    public void updateUndoHistory(final HistoryStatus whatIs) {
	this.dungeonData.updateUndoHistory(whatIs);
    }

    @Override
    public void updateRedoHistory(final HistoryStatus whatIs) {
	this.dungeonData.updateRedoHistory(whatIs);
    }

    @Override
    public HistoryStatus getWhatWas() {
	return this.dungeonData.getWhatWas();
    }

    @Override
    public void resetHistoryEngine() {
	this.dungeonData.resetHistoryEngine();
    }
}