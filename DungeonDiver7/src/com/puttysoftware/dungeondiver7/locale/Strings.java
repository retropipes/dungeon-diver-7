package com.puttysoftware.dungeondiver7.locale;

import java.util.Locale;
import java.util.ResourceBundle;

import com.puttysoftware.diane.strings.DianeStrings;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loader.ImageLoader;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utility.Difficulties;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;

public final class Strings {
    public static final String EMPTY = "";
    private static final String NAMED_DELIM = "$$";
    public static final int ARMOR_TYPES_COUNT = 6;
    public static final int WEAPON_TYPES_COUNT = 6;
    public static final int COLOR_COUNT = 8;
    public static final int SLOTS_COUNT = 12;
    private static Locale ACTIVE = Locale.getDefault();

    private Strings() {
    }

    public static void init() {
	Strings.ACTIVE = Locale.getDefault();
	Strings.activeLanguageChanged();
    }

    public static void changeLanguage(final Locale newLang) {
	Strings.ACTIVE = newLang;
	Strings.activeLanguageChanged();
    }

    public static void activeLanguageChanged() {
	Difficulties.activeLanguageChanged();
	DungeonConstants.activeLanguageChanged();
	DungeonDiver7.getStuffBag().activeLanguageChanged();
	PrefsManager.activeLanguageChanged();
	ImageLoader.activeLanguageChanged();
    }

    public static String[] allArmorTypes() {
	String[] result = new String[Strings.ARMOR_TYPES_COUNT];
	for (int index = 0; index < result.length; index++) {
	    result[index] = ResourceBundle.getBundle("locale.armortype", Strings.ACTIVE)
		    .getString(Integer.toString(index));
	}
	return result;
    }

    public static String[] allSlots() {
	String[] result = new String[Strings.SLOTS_COUNT];
	for (int index = 0; index < result.length; index++) {
	    result[index] = ResourceBundle.getBundle("locale.slot", Strings.ACTIVE).getString(Integer.toString(index));
	}
	return result;
    }

    public static String[] allWeaponTypes() {
	String[] result = new String[Strings.WEAPON_TYPES_COUNT];
	for (int index = 0; index < result.length; index++) {
	    result[index] = ResourceBundle.getBundle("locale.weapontype", Strings.ACTIVE)
		    .getString(Integer.toString(index));
	}
	return result;
    }

    public static String armor(final int index) {
	return ResourceBundle.getBundle("locale.armor", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String armorType(final int index) {
	return ResourceBundle.getBundle("locale.armortype", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String boss(final int index) {
	return ResourceBundle.getBundle("locale.boss", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String cheat(final int index) {
	return ResourceBundle.getBundle("locale.cheat", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String color(final int index) {
	return ResourceBundle.getBundle("locale.color", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String color(final Colors item) {
	return ResourceBundle.getBundle("locale.color", Strings.ACTIVE).getString(Integer.toString(item.ordinal()));
    }

    public static String difficulty(final Difficulty item) {
	return ResourceBundle.getBundle("locale.difficulty", Strings.ACTIVE)
		.getString(Integer.toString(item.ordinal()));
    }

    public static String error(final ErrorString item) {
	return ResourceBundle.getBundle("locale.error", Strings.ACTIVE).getString(Integer.toString(item.ordinal()));
    }

    public static String generic(final Generic item) {
	return ResourceBundle.getBundle("locale.generic", Strings.ACTIVE).getString(Integer.toString(item.ordinal()));
    }

    public static String item(final ItemString item) {
	return ResourceBundle.getBundle("locale.item", Strings.ACTIVE).getString(Integer.toString(item.ordinal()));
    }

    public static String menu(final Menu item) {
	return ResourceBundle.getBundle("locale.menu", Strings.ACTIVE).getString(Integer.toString(item.ordinal()));
    }

    public static String monster(final int index) {
	return ResourceBundle.getBundle("locale.monster", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String slot(final int index) {
	return ResourceBundle.getBundle("locale.slot", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String slot(final Slot item) {
	return ResourceBundle.getBundle("locale.slot", Strings.ACTIVE).getString(Integer.toString(item.ordinal()));
    }

    public static String stat(final int index) {
	return ResourceBundle.getBundle("locale.stat", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String timeTravel(final TimeTravel item) {
	return ResourceBundle.getBundle("locale.timetravel", Strings.ACTIVE)
		.getString(Integer.toString(item.ordinal()));
    }

    public static String timeTravel(final int index) {
	return ResourceBundle.getBundle("locale.timetravel", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String untranslated(final Untranslated item) {
	return ResourceBundle.getBundle("locale.untranslated", Strings.ACTIVE)
		.getString(Integer.toString(item.ordinal()));
    }

    public static String weapon(final int index) {
	return ResourceBundle.getBundle("locale.weapon", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String weaponType(final int index) {
	return ResourceBundle.getBundle("locale.weapontype", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String zone(final int index) {
	return ResourceBundle.getBundle("locale.zone", Strings.ACTIVE).getString(Integer.toString(index));
    }

    public static String group(final Group item) {
	return ResourceBundle.getBundle("locale.group", Strings.ACTIVE).getString(Integer.toString(item.ordinal()));
    }

    public static String armorName(final int mID, final int aID) {
	return DianeStrings.subst(Strings.group(Group.PAIR), Strings.armorType(mID), Strings.armor(aID));
    }

    public static String weaponName(final int mID, final int wID) {
	return DianeStrings.subst(Strings.group(Group.PAIR), Strings.weaponType(mID), Strings.weapon(wID));
    }

    public static String monsterzone(final int zoneID, final int monID) {
	return DianeStrings.subst(Strings.group(Group.PAIR), Strings.zone(zoneID), Strings.monster(monID));
    }

    public static String namedSubst(final String orig, final NamedSubst param, final String value) {
	return orig.replace(Strings.NAMED_DELIM + param.toString() + Strings.NAMED_DELIM, value);
    }
}
