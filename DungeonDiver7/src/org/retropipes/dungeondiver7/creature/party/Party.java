/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.creature.party;

import java.io.IOException;

import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.asset.SoundLoader;
import org.retropipes.dungeondiver7.asset.Sounds;
import org.retropipes.dungeondiver7.dungeon.AbstractDungeon;
import org.retropipes.dungeondiver7.dungeon.objects.BattleCharacter;
import org.retropipes.dungeondiver7.locale.Strings;

public class Party {
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

	boolean addPartyMember(final PartyMember member) {
		this.members = member;
		return true;
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

	private void generateBattleCharacters() {
		this.battlers = new BattleCharacter(this.members);
	}

	public BattleCharacter getBattleCharacters() {
		if (this.battlers == null) {
			this.generateBattleCharacters();
		}
		return this.battlers;
	}

	public PartyMember getLeader() {
		return this.members;
	}

	public long getPartyMaxToNextLevel() {
		return this.members.getToNextLevelValue();
	}

	public int getZone() {
		return this.zone;
	}

	public String getZoneString() {
		return "Zone Name: " + Strings.zone(this.zone);
	}

	public boolean isAlive() {
		return this.members.isAlive();
	}

	public void offsetZone(final int offset) {
		if (this.zone + offset > AbstractDungeon.getMaxLevels() || this.zone + offset < 0) {
			return;
		}
		this.zone += offset;
	}

	void resetZone() {
		this.zone = 0;
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
