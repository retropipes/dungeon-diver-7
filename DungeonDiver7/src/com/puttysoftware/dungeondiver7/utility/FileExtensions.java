/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

public class FileExtensions {
    private FileExtensions() {
	// Do nothing
    }

    // Constants
    private static final String STRING_EXTENSION = "properties";

    // Methods
    public static String getStringExtensionWithPeriod() {
	return "." + FileExtensions.STRING_EXTENSION;
    }
}