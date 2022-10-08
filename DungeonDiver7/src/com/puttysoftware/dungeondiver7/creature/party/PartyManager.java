/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.party;

import java.io.IOException;

import javax.swing.JFrame;

import com.puttysoftware.ack.AvatarConstructionKit;
import com.puttysoftware.ack.AvatarImageModel;
import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.diane.gui.ListWithDescDialog;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.creature.caste.CasteManager;
import com.puttysoftware.dungeondiver7.creature.characterfiles.CharacterLoader;
import com.puttysoftware.dungeondiver7.creature.characterfiles.CharacterRegistration;
import com.puttysoftware.dungeondiver7.creature.gender.GenderManager;
import com.puttysoftware.dungeondiver7.loader.MusicLoader;
import com.puttysoftware.dungeondiver7.locale.Music;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;

public class PartyManager {
    // Fields
    private static Party party;
    private static int bank = 0;
    private static final int PARTY_SIZE = 1;
    private final static String[] buttonNames = { "Done", "Create", "Pick" };

    // Constructors
    private PartyManager() {
	// Do nothing
    }

    // Methods
    public static boolean createParty() {
	if (MusicLoader.isMusicPlaying()) {
	    MusicLoader.stopMusic();
	}
	MusicLoader.playMusic(Music.CREATE);
	PartyManager.party = new Party();
	var mem = 0;
	final var pickMembers = CharacterLoader.loadAllRegisteredCharacters();
	for (var x = 0; x < PartyManager.PARTY_SIZE; x++) {
	    PartyMember pc = null;
	    if (pickMembers == null) {
		// No characters registered - must create one
		pc = PartyManager.createNewPC();
		if (pc != null) {
		    CharacterRegistration.autoregisterCharacter(pc.getName());
		    CharacterLoader.saveCharacter(pc);
		}
	    } else {
		final var response = CommonDialogs.showCustomDialog("Pick, Create, or Done?", "Create Party",
			PartyManager.buttonNames, PartyManager.buttonNames[2]);
		if (response == 2) {
		    pc = PartyManager.pickOnePartyMemberCreate(pickMembers);
		} else if (response == 1) {
		    pc = PartyManager.createNewPC();
		    if (pc != null) {
			CharacterRegistration.autoregisterCharacter(pc.getName());
			CharacterLoader.saveCharacter(pc);
		    }
		}
	    }
	    if (pc == null) {
		break;
	    }
	    PartyManager.party.addPartyMember(pc);
	    mem++;
	}
	if (mem == 0) {
	    return false;
	}
	return true;
    }

    public static Party getParty() {
	return PartyManager.party;
    }

    public static void addGoldToBank(final int newGold) {
	PartyManager.bank += newGold;
    }

    public static int getGoldInBank() {
	return PartyManager.bank;
    }

    public static void removeGoldFromBank(final int cost) {
	PartyManager.bank -= cost;
	if (PartyManager.bank < 0) {
	    PartyManager.bank = 0;
	}
    }

    private static void setGoldInBank(final int newGold) {
	PartyManager.bank = newGold;
    }

    public static void loadGameHook(final FileIOReader partyFile) throws IOException {
	final var containsPCData = partyFile.readBoolean();
	if (containsPCData) {
	    final var gib = partyFile.readInt();
	    PartyManager.setGoldInBank(gib);
	    PartyManager.party = Party.read(partyFile);
	}
    }

    public static void saveGameHook(final FileIOWriter partyFile) throws IOException {
	if (PartyManager.party != null) {
	    partyFile.writeBoolean(true);
	    partyFile.writeInt(PartyManager.getGoldInBank());
	    PartyManager.party.write(partyFile);
	} else {
	    partyFile.writeBoolean(false);
	}
    }

    public static PartyMember getNewPCInstance(final int c, final int g, final String n, final String aid) {
	final var caste = CasteManager.getCaste(c);
	final var gender = GenderManager.getGender(g);
	return new PartyMember(caste, gender, n, aid);
    }

    public static void updatePostKill() {
	final var leader = PartyManager.getParty().getLeader();
	leader.initPostKill(leader.getCaste(), leader.getGender());
    }

    private static PartyMember createNewPC() {
	final var name = CommonDialogs.showTextInputDialog("Character Name", "Create Character");
	if (name != null) {
	    final var caste = CasteManager.selectCaste();
	    if (caste != null) {
		final var gender = GenderManager.selectGender();
		if (gender != null) {
		    AvatarImageModel avatar = null;
		    try {
			avatar = AvatarConstructionKit.constructAvatar();
		    } catch (final IOException e) {
			DungeonDiver7.logError(e);
		    }
		    if (avatar != null) {
			final var aid = avatar.getAvatarImageID();
			return new PartyMember(caste, gender, name, aid);
		    }
		}
	    }
	}
	return null;
    }

    public static String showCreationDialog(final String labelText, final String title, final String[] input,
	    final String[] descriptions) {
	return ListWithDescDialog.showDialog((JFrame) null, labelText, title, input, input[0], descriptions[0],
		descriptions);
    }

    private static String[] buildNameList(final PartyMember[] members) {
	final var tempNames = new String[1];
	var nnc = 0;
	for (var x = 0; x < tempNames.length; x++) {
	    if (members != null) {
		tempNames[x] = members[x].getName();
		nnc++;
	    }
	}
	final var names = new String[nnc];
	nnc = 0;
	for (final String tempName : tempNames) {
	    if (tempName != null) {
		names[nnc] = tempName;
		nnc++;
	    }
	}
	return names;
    }

    private static PartyMember pickOnePartyMemberCreate(final PartyMember[] members) {
	final var pickNames = PartyManager.buildNameList(members);
	final var response = CommonDialogs.showInputDialog("Pick 1 Party Member", "Create Party", pickNames,
		pickNames[0]);
	if (response == null) {
	    return null;
	}
	for (final PartyMember member : members) {
	    if (member.getName().equals(response)) {
		return member;
	    }
	}
	return null;
    }
}
