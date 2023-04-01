/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.party;

import java.io.IOException;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.objects.BattleCharacter;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;

public class Party {
    // Fields
    private PartyMember members;
    private BattleCharacter battlers;
    private int leaderID;
    private int activePCs;
    private int zone;

    // Constructors
    public Party() {
	this.members = null;
	this.leaderID = 0;
	this.activePCs = 0;
	this.zone = 0;
    }

    // Methods
    private void generateBattleCharacters() {
	this.battlers = new BattleCharacter(this.members);
    }

    public BattleCharacter getBattleCharacters() {
	if (this.battlers == null) {
	    this.generateBattleCharacters();
	}
	return this.battlers;
    }

    public long getPartyMaxToNextLevel() {
	return this.members.getToNextLevelValue();
    }

    public int getZone() {
	return this.zone;
    }

    void resetZone() {
	this.zone = 0;
    }

    public void offsetZone(final int offset) {
	if (this.zone + offset > AbstractDungeon.getMaxLevels() || this.zone + offset < 0) {
	    return;
	}
	this.zone += offset;
    }

    public String getZoneString() {
	return "Zone Name: " + Strings.zone(this.zone);
    }

    public PartyMember getLeader() {
	return this.members;
    }

    public void checkPartyLevelUp() {
	// Level Up Check
	if (this.members.checkLevelUp()) {
	    this.members.levelUp();
	    SoundLoader.playSound(Sounds.LEVEL_UP);
	    CommonDialogs.showTitledDialog(this.members.getName() + " reached level " + this.members.getLevel() + "!",
		    "Level Up");
	}
    }

    public boolean isAlive() {
	return this.members.isAlive();
    }

    boolean addPartyMember(final PartyMember member) {
	this.members = member;
	return true;
    }

    static Party read(final DataIOReader worldFile) throws IOException {
	worldFile.readInt();
	final var lid = worldFile.readInt();
	final var apc = worldFile.readInt();
	final var lvl = worldFile.readInt();
	final var pty = new Party();
	pty.leaderID = lid;
	pty.activePCs = apc;
	pty.zone = lvl;
	final var present = worldFile.readBoolean();
	if (present) {
	    pty.members = PartyMember.read(worldFile);
	}
	return pty;
    }

    void write(final DataIOWriter worldFile) throws IOException {
	worldFile.writeInt(1);
	worldFile.writeInt(this.leaderID);
	worldFile.writeInt(this.activePCs);
	worldFile.writeInt(this.zone);
	if (this.members == null) {
	    worldFile.writeBoolean(false);
	} else {
	    worldFile.writeBoolean(true);
	    this.members.write(worldFile);
	}
    }
}
