/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.current;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.locale.Difficulty;
import com.puttysoftware.dungeondiver7.locale.Generic;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;
import com.puttysoftware.storage.NumberStorage;

public final class DungeonLevelInfo {
    // Properties
    private final NumberStorage playerStartData;
    private NumberStorage playerLocationData;
    private NumberStorage savedPlayerLocationData;
    private boolean horizontalWraparoundEnabled;
    private boolean verticalWraparoundEnabled;
    private boolean thirdDimensionWraparoundEnabled;
    private String name;
    private String hint;
    private String author;
    private Difficulty difficulty;
    private boolean moveShootAllowed;

    // Constructors
    public DungeonLevelInfo() {
	this.playerStartData = new NumberStorage(DungeonConstants.PLAYER_DIMS, DungeonConstants.NUM_PLAYERS);
	this.playerStartData.fill(-1);
	this.playerLocationData = new NumberStorage(DungeonConstants.PLAYER_DIMS, DungeonConstants.NUM_PLAYERS);
	this.playerLocationData.fill(-1);
	this.savedPlayerLocationData = new NumberStorage(DungeonConstants.PLAYER_DIMS, DungeonConstants.NUM_PLAYERS);
	this.savedPlayerLocationData.fill(-1);
	this.horizontalWraparoundEnabled = false;
	this.verticalWraparoundEnabled = false;
	this.thirdDimensionWraparoundEnabled = false;
	this.name = Strings.generic(Generic.UNNAMED_LEVEL);
	this.hint = Strings.EMPTY;
	this.author = Strings.generic(Generic.UNKNOWN_AUTHOR);
	this.difficulty = Difficulty.KIDS;
	this.moveShootAllowed = false;
    }

    public DungeonLevelInfo(final DungeonLevelInfo source) {
	this.playerStartData = new NumberStorage(source.playerStartData);
	this.horizontalWraparoundEnabled = source.horizontalWraparoundEnabled;
	this.verticalWraparoundEnabled = source.verticalWraparoundEnabled;
	this.thirdDimensionWraparoundEnabled = source.thirdDimensionWraparoundEnabled;
	this.name = source.name;
	this.hint = source.hint;
	this.author = source.author;
	this.difficulty = source.difficulty;
	this.moveShootAllowed = source.moveShootAllowed;
    }

    // Methods
    public boolean isMoveShootAllowed() {
	return this.moveShootAllowed;
    }

    public void setMoveShootAllowed(final boolean value) {
	this.moveShootAllowed = value;
    }

    public String getName() {
	return this.name;
    }

    public void setName(final String newName) {
	this.name = newName;
    }

    public String getHint() {
	return this.hint;
    }

    public void setHint(final String newHint) {
	this.hint = newHint;
    }

    public String getAuthor() {
	return this.author;
    }

    public void setAuthor(final String newAuthor) {
	this.author = newAuthor;
    }

    public Difficulty getDifficulty() {
	return this.difficulty;
    }

    public void setDifficulty(final Difficulty newDifficulty) {
	this.difficulty = newDifficulty;
    }

    public int getStartRow(final int pi) {
	return this.playerStartData.getCell(1, pi);
    }

    public int getStartColumn(final int pi) {
	return this.playerStartData.getCell(0, pi);
    }

    public int getStartFloor(final int pi) {
	return this.playerStartData.getCell(2, pi);
    }

    public void setStartRow(final int pi, final int value) {
	this.playerStartData.setCell(value, 1, pi);
    }

    public void setStartColumn(final int pi, final int value) {
	this.playerStartData.setCell(value, 0, pi);
    }

    public void setStartFloor(final int pi, final int value) {
	this.playerStartData.setCell(value, 2, pi);
    }

    public boolean doesPlayerStartExist(final int pi) {
	for (var y = 0; y < DungeonConstants.PLAYER_DIMS; y++) {
	    if (this.playerStartData.getCell(y, pi) == -1) {
		return false;
	    }
	}
	return true;
    }

    public int getPlayerLocationX(final int pi) {
	return this.playerLocationData.getCell(1, pi);
    }

    public int getPlayerLocationY(final int pi) {
	return this.playerLocationData.getCell(0, pi);
    }

    public int getPlayerLocationZ(final int pi) {
	return this.playerLocationData.getCell(2, pi);
    }

    public void offsetPlayerLocationX(final int pi, final int value) {
	this.playerLocationData.offsetCell(value, 1, pi);
    }

    public void offsetPlayerLocationY(final int pi, final int value) {
	this.playerLocationData.offsetCell(value, 0, pi);
    }

    public void offsetPlayerLocationZ(final int pi, final int value) {
	this.playerLocationData.offsetCell(value, 2, pi);
    }

    public void setPlayerLocationX(final int pi, final int value) {
	this.playerLocationData.setCell(value, 1, pi);
    }

    public void setPlayerLocationY(final int pi, final int value) {
	this.playerLocationData.setCell(value, 0, pi);
    }

    public void setPlayerLocationZ(final int pi, final int value) {
	this.playerLocationData.setCell(value, 2, pi);
    }

    public boolean doesPlayerLocationExist(final int pi) {
	for (var y = 0; y < DungeonConstants.PLAYER_DIMS; y++) {
	    if (this.playerLocationData.getCell(y, pi) == -1) {
		return false;
	    }
	}
	return true;
    }

    public void savePlayerLocation() {
	this.savedPlayerLocationData = new NumberStorage(this.playerLocationData);
    }

    public void restorePlayerLocation() {
	this.playerLocationData = new NumberStorage(this.savedPlayerLocationData);
    }

    public void setPlayerToStart() {
	this.playerLocationData = new NumberStorage(this.playerStartData);
    }

    public void enableHorizontalWraparound() {
	this.horizontalWraparoundEnabled = true;
    }

    public void disableHorizontalWraparound() {
	this.horizontalWraparoundEnabled = false;
    }

    public void enableVerticalWraparound() {
	this.verticalWraparoundEnabled = true;
    }

    public void disableVerticalWraparound() {
	this.verticalWraparoundEnabled = false;
    }

    public void enableThirdDimensionWraparound() {
	this.thirdDimensionWraparoundEnabled = true;
    }

    public void disableThirdDimensionWraparound() {
	this.thirdDimensionWraparoundEnabled = false;
    }

    public boolean isHorizontalWraparoundEnabled() {
	return this.horizontalWraparoundEnabled;
    }

    public boolean isVerticalWraparoundEnabled() {
	return this.verticalWraparoundEnabled;
    }

    public boolean isThirdDimensionWraparoundEnabled() {
	return this.thirdDimensionWraparoundEnabled;
    }

    public void writeLevelInfo(final FileIOWriter writer) throws IOException {
	int x, y;
	for (y = 0; y < DungeonConstants.PLAYER_DIMS; y++) {
	    for (x = 0; x < DungeonConstants.NUM_PLAYERS; x++) {
		writer.writeInt(this.playerStartData.getCell(y, x));
	    }
	}
	writer.writeBoolean(this.horizontalWraparoundEnabled);
	writer.writeBoolean(this.verticalWraparoundEnabled);
	writer.writeBoolean(this.thirdDimensionWraparoundEnabled);
	writer.writeString(this.name);
	writer.writeString(this.hint);
	writer.writeString(this.author);
	writer.writeInt(this.difficulty.ordinal());
	writer.writeBoolean(this.moveShootAllowed);
    }

    public static DungeonLevelInfo readLevelInfo(final FileIOReader reader) throws IOException {
	final var li = new DungeonLevelInfo();
	int x, y;
	for (y = 0; y < 3; y++) {
	    for (x = 0; x < DungeonConstants.NUM_PLAYERS; x++) {
		li.playerStartData.setCell(reader.readInt(), y, x);
	    }
	}
	li.horizontalWraparoundEnabled = reader.readBoolean();
	li.verticalWraparoundEnabled = reader.readBoolean();
	li.thirdDimensionWraparoundEnabled = reader.readBoolean();
	li.name = reader.readString();
	li.hint = reader.readString();
	li.author = reader.readString();
	li.difficulty = Difficulty.values()[reader.readInt()];
	li.moveShootAllowed = reader.readBoolean();
	return li;
    }
}
