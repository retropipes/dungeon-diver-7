/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.party;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.creature.AbstractCreature;
import com.puttysoftware.dungeondiver7.creature.StatConstants;
import com.puttysoftware.dungeondiver7.creature.caste.Caste;
import com.puttysoftware.dungeondiver7.creature.caste.CasteManager;
import com.puttysoftware.dungeondiver7.creature.gender.Gender;
import com.puttysoftware.dungeondiver7.integration1.VersionException;
import com.puttysoftware.dungeondiver7.integration1.dungeon.GenerateTask;
import com.puttysoftware.dungeondiver7.item.ItemInventory;
import com.puttysoftware.dungeondiver7.loader.AvatarImageManager;
import com.puttysoftware.dungeondiver7.manager.dungeon.FormatConstants;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.spell.SpellBook;
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
    private final int hairID;
    private final int skinID;
    private static final int START_GOLD = 0;
    private static final double BASE_COEFF = 10.0;

    // Constructors
    PartyMember(final Caste c, final Gender g, final String n) {
	super(0);
	this.hairID = 0;
	this.skinID = 0;
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
	final PolyTable nextLevelEquation = new PolyTable(3, 1, 0, true);
	final double value = PartyMember.BASE_COEFF;
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
	final SpellBook book = CasteManager.getSpellBookByID(bookID);
	for (int x = 0; x < known.length; x++) {
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
	final int difficulty = PrefsManager.getGameDifficulty();
	final int base = this.getBaseSpeed();
	if (difficulty == PrefsManager.DIFFICULTY_VERY_EASY) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_FASTEST);
	} else if (difficulty == PrefsManager.DIFFICULTY_EASY) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_FAST);
	} else if (difficulty == PrefsManager.DIFFICULTY_NORMAL) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_NORMAL);
	} else if (difficulty == PrefsManager.DIFFICULTY_HARD) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_SLOW);
	} else if (difficulty == PrefsManager.DIFFICULTY_VERY_HARD) {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_SLOWEST);
	} else {
	    return (int) (base * AbstractCreature.SPEED_ADJUST_NORMAL);
	}
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
	final PolyTable nextLevelEquation = new PolyTable(3, 1, 0, true);
	final double value = PartyMember.BASE_COEFF;
	nextLevelEquation.setCoefficient(1, value);
	nextLevelEquation.setCoefficient(2, value);
	nextLevelEquation.setCoefficient(3, value);
	this.setToNextLevel(nextLevelEquation);
	this.setSpellBook(CasteManager.getSpellBookByID(this.caste.getCasteID()));
	PartyManager.getParty().resetZone();
	new GenerateTask(true).start();
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
	if (version < FormatConstants.CHARACTER_FORMAT_2) {
	    throw new VersionException("Invalid character version found: " + version);
	}
	final int k = worldFile.readInt();
	final int pAtk = worldFile.readInt();
	final int pDef = worldFile.readInt();
	final int pHP = worldFile.readInt();
	final int pMP = worldFile.readInt();
	final int strength = worldFile.readInt();
	final int block = worldFile.readInt();
	final int agility = worldFile.readInt();
	final int vitality = worldFile.readInt();
	final int intelligence = worldFile.readInt();
	final int luck = worldFile.readInt();
	final int lvl = worldFile.readInt();
	final int cHP = worldFile.readInt();
	final int cMP = worldFile.readInt();
	final int gld = worldFile.readInt();
	final int apr = worldFile.readInt();
	final int spr = worldFile.readInt();
	final int load = worldFile.readInt();
	final long exp = worldFile.readLong();
	final int c = worldFile.readInt();
	final int g = worldFile.readInt();
	final int max = worldFile.readInt();
	final boolean[] known = new boolean[max];
	for (int x = 0; x < max; x++) {
	    known[x] = worldFile.readBoolean();
	}
	final String n = worldFile.readString();
	final PartyMember pm = PartyManager.getNewPCInstance(c, g, n);
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
	worldFile.writeByte(FormatConstants.CHARACTER_FORMAT_LATEST);
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
	final int max = this.getSpellBook().getSpellCount();
	worldFile.writeInt(max);
	for (int x = 0; x < max; x++) {
	    worldFile.writeBoolean(this.getSpellBook().isSpellKnown(x));
	}
	worldFile.writeString(this.getName());
	this.getItems().writeItemInventory(worldFile);
    }

    @Override
    protected BufferedImageIcon getInitialImage() {
	return AvatarImageManager.getImage(this.getGender().getGenderID(), this.hairID, this.skinID);
    }

    @Override
    public void loadCreature() {
	// Do nothing
    }
}
