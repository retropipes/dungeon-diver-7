/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.party;

import java.io.IOException;

import com.puttysoftware.ack.AvatarConstructionKit;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.VersionException;
import com.puttysoftware.dungeondiver7.creature.AbstractCreature;
import com.puttysoftware.dungeondiver7.creature.StatConstants;
import com.puttysoftware.dungeondiver7.creature.caste.Caste;
import com.puttysoftware.dungeondiver7.creature.caste.CasteManager;
import com.puttysoftware.dungeondiver7.creature.gender.Gender;
import com.puttysoftware.dungeondiver7.dungeon.current.GenerateDungeonTask;
import com.puttysoftware.dungeondiver7.item.ItemInventory;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utility.FileFormats;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.polytable.PolyTable;

public class PartyMember extends AbstractCreature {
    // Fields
    private Caste caste;
    private Gender gender;
    private final String name;
    private int permanentAttack;
    private int permanentDefense;
    private int permanentHP;
    private int permanentMP;
    private int kills;
    private final String avatarID;
    private static final int START_GOLD = 0;
    private static final double BASE_COEFF = 10.0;

    // Constructors
    PartyMember(final Caste c, final Gender g, final String n, final String aid) {
	super(0);
	this.avatarID = aid;
	this.name = n;
	this.caste = c;
	this.gender = g;
	this.permanentAttack = 0;
	this.permanentDefense = 0;
	this.permanentHP = 0;
	this.permanentMP = 0;
	this.kills = 0;
	this.setLevel(1);
	this.setStrength(StatConstants.GAIN_STRENGTH);
	this.setBlock(StatConstants.GAIN_BLOCK);
	this.setVitality(StatConstants.GAIN_VITALITY);
	this.setIntelligence(StatConstants.GAIN_INTELLIGENCE);
	this.setAgility(StatConstants.GAIN_AGILITY);
	this.setLuck(StatConstants.GAIN_LUCK);
	this.setAttacksPerRound(1);
	this.setSpellsPerRound(1);
	this.healAndRegenerateFully();
	this.setGold(PartyMember.START_GOLD);
	this.setExperience(0L);
	final var nextLevelEquation = new PolyTable(3, 1, 0, true);
	final var value = PartyMember.BASE_COEFF;
	nextLevelEquation.setCoefficient(1, value);
	nextLevelEquation.setCoefficient(2, value);
	nextLevelEquation.setCoefficient(3, value);
	this.setToNextLevel(nextLevelEquation);
	this.setSpellBook(CasteManager.getSpellBookByID(this.caste.getCasteID()));
    }

    // Methods
    public String getXPString() {
	return this.getExperience() + "/" + this.getToNextLevelValue();
    }

    // Transformers
    @Override
    protected void levelUpHook() {
	this.offsetStrength(StatConstants.GAIN_STRENGTH);
	this.offsetBlock(StatConstants.GAIN_BLOCK);
	this.offsetVitality(StatConstants.GAIN_VITALITY);
	this.offsetIntelligence(StatConstants.GAIN_INTELLIGENCE);
	this.offsetAgility(StatConstants.GAIN_AGILITY);
	this.offsetLuck(StatConstants.GAIN_LUCK);
	this.healAndRegenerateFully();
    }

    private void loadPartyMember(final int newLevel, final int chp, final int cmp, final int newGold, final int newLoad,
	    final long newExperience, final int bookID, final boolean[] known) {
	this.setLevel(newLevel);
	this.setCurrentHP(chp);
	this.setCurrentMP(cmp);
	this.setGold(newGold);
	this.setLoad(newLoad);
	this.setExperience(newExperience);
	final var book = CasteManager.getSpellBookByID(bookID);
	for (var x = 0; x < known.length; x++) {
	    if (known[x]) {
		book.learnSpellByID(x);
	    }
	}
	this.setSpellBook(book);
    }

    @Override
    public String getName() {
	return this.name;
    }

    public Caste getCaste() {
	return this.caste;
    }

    protected Gender getGender() {
	return this.gender;
    }

    @Override
    public int getSpeed() {
	final var difficulty = PrefsManager.getGameDifficulty();
	final var base = this.getBaseSpeed();
	if (difficulty == PrefsManager.DIFFICULTY_VERY_EASY) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_FASTEST);
	}
	if (difficulty == PrefsManager.DIFFICULTY_EASY) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_FAST);
	}
	if (difficulty == PrefsManager.DIFFICULTY_NORMAL) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_NORMAL);
	}
	if (difficulty == PrefsManager.DIFFICULTY_HARD) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_SLOW);
	}
	if (difficulty == PrefsManager.DIFFICULTY_VERY_HARD) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_SLOWEST);
	}
	return (int) (base * AbstractCreature.SPEED_ADJUST_NORMAL);
    }

    public void initPostKill(final Caste c, final Gender g) {
	this.caste = c;
	this.gender = g;
	this.setLevel(1);
	this.setStrength(StatConstants.GAIN_STRENGTH);
	this.setBlock(StatConstants.GAIN_BLOCK);
	this.setVitality(StatConstants.GAIN_VITALITY);
	this.setIntelligence(StatConstants.GAIN_INTELLIGENCE);
	this.setAgility(StatConstants.GAIN_AGILITY);
	this.setLuck(StatConstants.GAIN_LUCK);
	this.setAttacksPerRound(1);
	this.setSpellsPerRound(1);
	this.healAndRegenerateFully();
	this.setGold(PartyMember.START_GOLD);
	this.setExperience(0L);
	this.getItems().resetInventory();
	final var nextLevelEquation = new PolyTable(3, 1, 0, true);
	final var value = PartyMember.BASE_COEFF;
	nextLevelEquation.setCoefficient(1, value);
	nextLevelEquation.setCoefficient(2, value);
	nextLevelEquation.setCoefficient(3, value);
	this.setToNextLevel(nextLevelEquation);
	this.setSpellBook(CasteManager.getSpellBookByID(this.caste.getCasteID()));
	PartyManager.getParty().resetZone();
	new GenerateDungeonTask(true).start();
    }

    @Override
    public int getAttack() {
	return super.getAttack() + this.getPermanentAttackPoints();
    }

    @Override
    public int getDefense() {
	return super.getDefense() + this.getPermanentDefensePoints();
    }

    @Override
    public int getMaximumHP() {
	return super.getMaximumHP() + this.getPermanentHPPoints();
    }

    @Override
    public int getMaximumMP() {
	return super.getMaximumMP() + this.getPermanentMPPoints();
    }

    public int getPermanentAttackPoints() {
	return this.permanentAttack;
    }

    public int getPermanentDefensePoints() {
	return this.permanentDefense;
    }

    public int getPermanentHPPoints() {
	return this.permanentHP;
    }

    public int getPermanentMPPoints() {
	return this.permanentMP;
    }

    public void spendPointOnAttack() {
	this.kills++;
	this.permanentAttack++;
    }

    public void spendPointOnDefense() {
	this.kills++;
	this.permanentDefense++;
    }

    public void spendPointOnHP() {
	this.kills++;
	this.permanentHP++;
    }

    public void spendPointOnMP() {
	this.kills++;
	this.permanentMP++;
    }

    public void onDeath(final int penalty) {
	this.offsetExperiencePercentage(penalty);
	this.healAndRegenerateFully();
	this.setGold(0);
    }

    public static PartyMember read(final FileIOReader worldFile) throws IOException {
	final int version = worldFile.readByte();
	if (version < FileFormats.CHARACTER_2) {
	    throw new VersionException("Invalid character version found: " + version);
	}
	final var k = worldFile.readInt();
	final var pAtk = worldFile.readInt();
	final var pDef = worldFile.readInt();
	final var pHP = worldFile.readInt();
	final var pMP = worldFile.readInt();
	final var strength = worldFile.readInt();
	final var block = worldFile.readInt();
	final var agility = worldFile.readInt();
	final var vitality = worldFile.readInt();
	final var intelligence = worldFile.readInt();
	final var luck = worldFile.readInt();
	final var lvl = worldFile.readInt();
	final var cHP = worldFile.readInt();
	final var cMP = worldFile.readInt();
	final var gld = worldFile.readInt();
	final var apr = worldFile.readInt();
	final var spr = worldFile.readInt();
	final var load = worldFile.readInt();
	final var exp = worldFile.readLong();
	final var c = worldFile.readInt();
	final var g = worldFile.readInt();
	final var max = worldFile.readInt();
	final var known = new boolean[max];
	for (var x = 0; x < max; x++) {
	    known[x] = worldFile.readBoolean();
	}
	final var n = worldFile.readString();
	final var aid = worldFile.readString();
	final var pm = PartyManager.getNewPCInstance(c, g, n, aid);
	pm.setStrength(strength);
	pm.setBlock(block);
	pm.setAgility(agility);
	pm.setVitality(vitality);
	pm.setIntelligence(intelligence);
	pm.setLuck(luck);
	pm.setAttacksPerRound(apr);
	pm.setSpellsPerRound(spr);
	pm.setItems(ItemInventory.readItemInventory(worldFile));
	pm.kills = k;
	pm.permanentAttack = pAtk;
	pm.permanentDefense = pDef;
	pm.permanentHP = pHP;
	pm.permanentMP = pMP;
	pm.loadPartyMember(lvl, cHP, cMP, gld, load, exp, c, known);
	return pm;
    }

    public void write(final FileIOWriter worldFile) throws IOException {
	worldFile.writeByte(FileFormats.CHARACTER_LATEST);
	worldFile.writeInt(this.kills);
	worldFile.writeInt(this.getPermanentAttackPoints());
	worldFile.writeInt(this.getPermanentDefensePoints());
	worldFile.writeInt(this.getPermanentHPPoints());
	worldFile.writeInt(this.getPermanentMPPoints());
	worldFile.writeInt(this.getStrength());
	worldFile.writeInt(this.getBlock());
	worldFile.writeInt(this.getAgility());
	worldFile.writeInt(this.getVitality());
	worldFile.writeInt(this.getIntelligence());
	worldFile.writeInt(this.getLuck());
	worldFile.writeInt(this.getLevel());
	worldFile.writeInt(this.getCurrentHP());
	worldFile.writeInt(this.getCurrentMP());
	worldFile.writeInt(this.getGold());
	worldFile.writeInt(this.getAttacksPerRound());
	worldFile.writeInt(this.getSpellsPerRound());
	worldFile.writeInt(this.getLoad());
	worldFile.writeLong(this.getExperience());
	worldFile.writeInt(this.getCaste().getCasteID());
	worldFile.writeInt(this.getGender().getGenderID());
	final var max = this.getSpellBook().getSpellCount();
	worldFile.writeInt(max);
	for (var x = 0; x < max; x++) {
	    worldFile.writeBoolean(this.getSpellBook().isSpellKnown(x));
	}
	worldFile.writeString(this.getName());
	worldFile.writeString(this.avatarID);
	this.getItems().writeItemInventory(worldFile);
    }

    @Override
    protected BufferedImageIcon getInitialImage() {
	try {
	    return AvatarConstructionKit.constructFromAvatarID(this.avatarID).generateAvatarImage();
	} catch (final IOException e) {
	    DungeonDiver7.logError(e);
	    return null;
	}
    }

    @Override
    public void loadCreature() {
	// Do nothing
    }
}
