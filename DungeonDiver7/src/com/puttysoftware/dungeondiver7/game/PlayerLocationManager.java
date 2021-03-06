/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.game;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.utilities.DungeonConstants;
import com.puttysoftware.storage.NumberStorage;

public final class PlayerLocationManager {
    // Fields
    private int playerInstance;
    private NumberStorage playerData;
    private NumberStorage savedPlayerData;
    private NumberStorage savedRemoteData;

    // Constructors
    public PlayerLocationManager() {
	this.playerInstance = 0;
	this.playerData = new NumberStorage(DungeonConstants.PLAYER_DIMS, DungeonConstants.NUM_PLAYERS);
	this.playerData.fill(-1);
	this.savedPlayerData = new NumberStorage(DungeonConstants.PLAYER_DIMS, DungeonConstants.NUM_PLAYERS);
	this.savedPlayerData.fill(-1);
	this.savedRemoteData = new NumberStorage(DungeonConstants.PLAYER_DIMS, DungeonConstants.NUM_PLAYERS);
	this.savedRemoteData.fill(-1);
    }

    // Methods
    public int getActivePlayerNumber() {
	return this.playerInstance;
    }

    public void setActivePlayerNumber(final int value) {
	this.playerInstance = value;
    }

    public int getPlayerLocationX() {
	return this.playerData.getCell(1, this.playerInstance);
    }

    public int getPlayerLocationY() {
	return this.playerData.getCell(0, this.playerInstance);
    }

    public int getPlayerLocationZ() {
	return this.playerData.getCell(2, this.playerInstance);
    }

    private void setPlayerLocationX(final int val) {
	this.playerData.setCell(val, 1, this.playerInstance);
    }

    private void setPlayerLocationY(final int val) {
	this.playerData.setCell(val, 0, this.playerInstance);
    }

    private void setPlayerLocationZ(final int val) {
	this.playerData.setCell(val, 2, this.playerInstance);
    }

    public void resetPlayerLocation() {
	final AbstractDungeon a = DungeonDiver7.getApplication().getDungeonManager().getDungeon();
	for (int pi = 0; pi < DungeonConstants.NUM_PLAYERS; pi++) {
	    final int[] found = a.findPlayer(pi);
	    if (found != null) {
		final int valX = found[0];
		final int valY = found[1];
		final int valZ = found[2];
		this.initPlayerLocation(valX, valY, valZ, pi);
	    }
	}
    }

    private void initPlayerLocation(final int valX, final int valY, final int valZ, final int pi) {
	this.playerData.setCell(valX, 1, pi);
	this.playerData.setCell(valY, 0, pi);
	this.playerData.setCell(valZ, 2, pi);
    }

    public void setPlayerLocation(final int valX, final int valY, final int valZ) {
	this.setPlayerLocationX(valX);
	this.setPlayerLocationY(valY);
	this.setPlayerLocationZ(valZ);
    }

    void offsetPlayerLocationX(final int val) {
	this.playerData.setCell(this.getPlayerLocationX() + val, 1, this.playerInstance);
    }

    void offsetPlayerLocationY(final int val) {
	this.playerData.setCell(this.getPlayerLocationY() + val, 0, this.playerInstance);
    }

    public void togglePlayerInstance() {
	boolean doesNotExist = true;
	while (doesNotExist) {
	    this.playerInstance++;
	    if (this.playerInstance >= DungeonConstants.NUM_PLAYERS) {
		this.playerInstance = 0;
	    }
	    final int px = this.getPlayerLocationX();
	    final int py = this.getPlayerLocationY();
	    final int pz = this.getPlayerLocationZ();
	    if (px != -1 && py != -1 && pz != -1) {
		doesNotExist = false;
	    }
	}
    }

    void savePlayerLocation() {
	this.savedPlayerData = new NumberStorage(this.playerData);
    }

    void restorePlayerLocation() {
	this.playerData = new NumberStorage(this.savedPlayerData);
    }

    void saveRemoteLocation() {
	this.savedRemoteData = new NumberStorage(this.playerData);
    }

    void restoreRemoteLocation() {
	this.playerData = new NumberStorage(this.savedRemoteData);
    }
}
