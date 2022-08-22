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
import com.puttysoftware.dungeondiver7.creature.caste.Caste;
import com.puttysoftware.dungeondiver7.creature.caste.CasteManager;
import com.puttysoftware.dungeondiver7.creature.characterfiles.CharacterLoader;
import com.puttysoftware.dungeondiver7.creature.characterfiles.CharacterRegistration;
import com.puttysoftware.dungeondiver7.creature.gender.Gender;
import com.puttysoftware.dungeondiver7.creature.gender.GenderManager;
import com.puttysoftware.dungeondiver7.loader.MusicConstants;
import com.puttysoftware.dungeondiver7.loader.MusicLoader;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;

public class PartyManager {
    // Fields
    private static Party party;
    private static int bank = 0;
    private static final int PARTY_SIZE = 1;
    private final static String[] buttonNames = new String[] { "Done", "Create", "Pick" };

    // Constructors
    private PartyManager() {
	// Do nothing
    }

    // Methods
    public static boolean createParty(final JFrame owner) {
	if (MusicLoader.isMusicPlaying()) {
	    MusicLoader.stopMusic();
	}
	MusicLoader.playMusic(MusicConstants.MUSIC_CREATE);
	PartyManager.party = new Party();
	int mem = 0;
	final PartyMember[] pickMembers = CharacterLoader.loadAllRegisteredCharacters();
	for (int x = 0; x < PartyManager.PARTY_SIZE; x++) {
	    PartyMember pc = null;
	    if (pickMembers == null) {
		// No characters registered - must create one
		pc = PartyManager.createNewPC(owner);
		if (pc != null) {
		    CharacterRegistration.autoregisterCharacter(pc.getName());
		    CharacterLoader.saveCharacter(pc);
		}
	    } else {
		final int response = CommonDialogs.showCustomDialog("Pick, Create, or Done?", "Create Party",
			PartyManager.buttonNames, PartyManager.buttonNames[2]);
		if (response == 2) {
		    pc = PartyManager.pickOnePartyMemberCreate(pickMembers);
		} else if (response == 1) {
		    pc = PartyManager.createNewPC(owner);
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
	final boolean containsPCData = partyFile.readBoolean();
	if (containsPCData) {
	    final int gib = partyFile.readInt();
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
	final Caste caste = CasteManager.getCaste(c);
	final Gender gender = GenderManager.getGender(g);
	return new PartyMember(caste, gender, n, aid);
    }

    public static void updatePostKill() {
	final PartyMember leader = PartyManager.getParty().getLeader();
	leader.initPostKill(leader.getCaste(), leader.getGender());
    }

    private static PartyMember createNewPC(final JFrame owner) {
	final String name = CommonDialogs.showTextInputDialog("Character Name", "Create Character");
	if (name != null) {
	    final Caste caste = CasteManager.selectCaste(owner);
	    if (caste != null) {
		final Gender gender = GenderManager.selectGender();
		if (gender != null) {
		    AvatarImageModel avatar = null;
		    try {
			avatar = AvatarConstructionKit.constructAvatar();
		    } catch (IOException e) {
			DungeonDiver7.logError(e);
		    }
		    if (avatar != null) {
			String aid = avatar.getAvatarImageID();
			return new PartyMember(caste, gender, name, aid);
		    }
		}
	    }
	}
	return null;
    }

    public static String showCreationDialog(final JFrame owner, final String labelText, final String title,
	    final String[] input, final String[] descriptions) {
	return ListWithDescDialog.showDialog(owner, labelText, title, input, input[0], descriptions[0], descriptions);
    }

    private static String[] buildNameList(final PartyMember[] members) {
	final String[] tempNames = new String[1];
	int nnc = 0;
	for (int x = 0; x < tempNames.length; x++) {
	    if (members != null) {
		tempNames[x] = members[x].getName();
		nnc++;
	    }
	}
	final String[] names = new String[nnc];
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
	final String[] pickNames = PartyManager.buildNameList(members);
	final String response = CommonDialogs.showInputDialog("Pick 1 Party Member", "Create Party", pickNames,
		pickNames[0]);
	if (response != null) {
	    for (final PartyMember member : members) {
		if (member.getName().equals(response)) {
		    return member;
		}
	    }
	    return null;
	} else {
	    return null;
	}
    }
}
