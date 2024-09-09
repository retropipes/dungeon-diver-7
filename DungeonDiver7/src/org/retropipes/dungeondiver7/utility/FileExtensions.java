/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.utility;

public class FileExtensions {
    // Constants
    private static final String STRING_EXTENSION = "properties";

    public static String getStringExtensionWithPeriod() {
	return "." + FileExtensions.STRING_EXTENSION;
    }

    private FileExtensions() {
	// Do nothing
    }
}