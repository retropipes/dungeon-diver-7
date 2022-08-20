package com.puttysoftware.dungeondiver7.locale;

import java.util.ResourceBundle;

import com.puttysoftware.diane.strings.DianeStrings;

public final class Strings {
    public static final String EMPTY = "";
    public static final int ARMOR_TYPES_COUNT = 6;
    public static final int WEAPON_TYPES_COUNT = 6;

    private Strings() {
    }

    public static String[] allArmorTypes() {
	String[] result = new String[Strings.ARMOR_TYPES_COUNT];
	for (int index = 0; index < result.length; index++) {
	    result[index] = ResourceBundle.getBundle("locale.armortype").getString(Integer.toString(index));
	}
	return result;
    }

    public static String[] allWeaponTypes() {
	String[] result = new String[Strings.WEAPON_TYPES_COUNT];
	for (int index = 0; index < result.length; index++) {
	    result[index] = ResourceBundle.getBundle("locale.weapontype").getString(Integer.toString(index));
	}
	return result;
    }

    public static String armor(final int index) {
	return ResourceBundle.getBundle("locale.armor").getString(Integer.toString(index));
    }

    public static String armorType(final int index) {
	return ResourceBundle.getBundle("locale.armortype").getString(Integer.toString(index));
    }

    public static String boss(final int index) {
	return ResourceBundle.getBundle("locale.boss").getString(Integer.toString(index));
    }

    public static String difficulty(final Difficulty item) {
	return ResourceBundle.getBundle("locale.difficulty").getString(Integer.toString(item.ordinal()));
    }

    public static String error(final ErrorString item) {
	return ResourceBundle.getBundle("locale.error").getString(Integer.toString(item.ordinal()));
    }

    public static String menu(final Menu item) {
	return ResourceBundle.getBundle("locale.menu").getString(Integer.toString(item.ordinal()));
    }

    public static String monster(final int index) {
	return ResourceBundle.getBundle("locale.monster").getString(Integer.toString(index));
    }

    public static String slot(final int index) {
	return ResourceBundle.getBundle("locale.slot").getString(Integer.toString(index));
    }

    public static String stat(final int index) {
	return ResourceBundle.getBundle("locale.stat").getString(Integer.toString(index));
    }

    public static String timeTravel(final TimeTravel item) {
	return ResourceBundle.getBundle("locale.timetravel").getString(Integer.toString(item.ordinal()));
    }

    public static String timeTravel(final int index) {
	return ResourceBundle.getBundle("locale.timetravel").getString(Integer.toString(index));
    }

    public static String untranslated(final Untranslated item) {
	return ResourceBundle.getBundle("locale.untranslated").getString(Integer.toString(item.ordinal()));
    }

    public static String weapon(final int index) {
	return ResourceBundle.getBundle("locale.weapon").getString(Integer.toString(index));
    }

    public static String weaponType(final int index) {
	return ResourceBundle.getBundle("locale.weapontype").getString(Integer.toString(index));
    }

    public static String zone(final int index) {
	return ResourceBundle.getBundle("locale.zone").getString(Integer.toString(index));
    }

    public static String group() {
	return ResourceBundle.getBundle("locale.group").getString(Integer.toString(0));
    }

    public static String armorName(final int mID, final int aID) {
	return DianeStrings.subst(Strings.group(), Strings.armorType(mID), Strings.armor(aID));
    }

    public static String weaponName(final int mID, final int wID) {
	return DianeStrings.subst(Strings.group(), Strings.weaponType(mID), Strings.weapon(wID));
    }

    public static String monsterzone(final int zoneID, final int monID) {
	return DianeStrings.subst(Strings.group(), Strings.zone(zoneID), Strings.monster(monID));
    }
}
