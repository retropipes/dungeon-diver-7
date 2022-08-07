/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.creature.characterfiles;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.integration1.VersionException;
import com.puttysoftware.dungeondiver7.integration1.creature.party.PartyMember;
import com.puttysoftware.dungeondiver7.integration1.manager.file.Extension;
import com.puttysoftware.fileio.FileIOException;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;
import com.puttysoftware.fileio.XDataReader;
import com.puttysoftware.fileio.XDataWriter;

public class CharacterLoader {
    private static PartyMember loadCharacter(final String name) {
	final String basePath = CharacterRegistration.getBasePath();
	final String loadPath = basePath + File.separator + name + Extension.getCharacterExtensionWithPeriod();
	try (FileIOReader loader = new XDataReader(loadPath, "character")) {
	    return PartyMember.read(loader);
	} catch (VersionException | FileIOException e) {
	    CharacterRegistration.autoremoveCharacter(name);
	    return null;
	} catch (final IOException e) {
	    DungeonDiver7.getErrorLogger().logError(e);
	    return null;
	}
    }

    public static PartyMember[] loadAllRegisteredCharacters() {
	final String[] registeredNames = CharacterRegistration.getCharacterNameList();
	if (registeredNames != null) {
	    final PartyMember[] res = new PartyMember[registeredNames.length];
	    // Load characters
	    for (int x = 0; x < registeredNames.length; x++) {
		final String name = registeredNames[x];
		final PartyMember characterWithName = CharacterLoader.loadCharacter(name);
		if (characterWithName != null) {
		    res[x] = characterWithName;
		} else {
		    // Auto-removed character
		    return CharacterLoader.loadAllRegisteredCharacters();
		}
	    }
	    return res;
	}
	return null;
    }

    public static void saveCharacter(final PartyMember character) {
	final String basePath = CharacterRegistration.getBasePath();
	final String name = character.getName();
	final String characterFile = basePath + File.separator + name + Extension.getCharacterExtensionWithPeriod();
	try (FileIOWriter saver = new XDataWriter(characterFile, "character")) {
	    character.write(saver);
	} catch (final IOException e) {
	    DungeonDiver7.getErrorLogger().logError(e);
	}
    }

    static void deleteCharacter(final String name, final boolean showResults) {
	final String basePath = CharacterRegistration.getBasePath();
	final String characterFile = basePath + File.separator + name + Extension.getCharacterExtensionWithPeriod();
	final File toDelete = new File(characterFile);
	if (toDelete.exists()) {
	    final boolean success = toDelete.delete();
	    if (success) {
		if (showResults) {
		    CommonDialogs.showDialog("Character removed.");
		} else {
		    CommonDialogs.showDialog("Character " + name + " autoremoved due to version change.");
		}
	    } else {
		if (showResults) {
		    CommonDialogs.showDialog("Character removal failed!");
		} else {
		    CommonDialogs.showDialog("Character " + name + " failed to autoremove!");
		}
	    }
	} else {
	    if (showResults) {
		CommonDialogs.showDialog("The character to be removed does not have a corresponding file.");
	    } else {
		CommonDialogs.showDialog("The character to be autoremoved does not have a corresponding file.");
	    }
	}
    }
}
