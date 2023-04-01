/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.characterfiles;

import java.io.File;
import java.io.IOException;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.VersionException;
import com.puttysoftware.dungeondiver7.creature.party.PartyMember;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.diane.fileio.DataIOException;
import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;
import com.puttysoftware.diane.fileio.XDataReader;
import com.puttysoftware.diane.fileio.XDataWriter;

public class CharacterLoader {
	private static PartyMember loadCharacter(final String name) {
		final var basePath = CharacterRegistration.getBasePath();
		final var loadPath = basePath + File.separator + name + Strings.fileExtension(FileExtension.CHARACTER);
		try (DataIOReader loader = new XDataReader(loadPath, "character")) {
			return PartyMember.read(loader);
		} catch (VersionException | DataIOException e) {
			CharacterRegistration.autoremoveCharacter(name);
			return null;
		} catch (final IOException e) {
			DungeonDiver7.logError(e);
			return null;
		}
	}

	public static PartyMember[] loadAllRegisteredCharacters() {
		final var registeredNames = CharacterRegistration.getCharacterNameList();
		if (registeredNames != null) {
			final var res = new PartyMember[registeredNames.length];
			// Load characters
			for (var x = 0; x < registeredNames.length; x++) {
				final var name = registeredNames[x];
				final var characterWithName = CharacterLoader.loadCharacter(name);
				if (characterWithName == null) {
					// Auto-removed character
					return CharacterLoader.loadAllRegisteredCharacters();
				}
				res[x] = characterWithName;
			}
			return res;
		}
		return null;
	}

	public static void saveCharacter(final PartyMember character) {
		final var basePath = CharacterRegistration.getBasePath();
		final var name = character.getName();
		final var characterFile = basePath + File.separator + name + Strings.fileExtension(FileExtension.CHARACTER);
		try (DataIOWriter saver = new XDataWriter(characterFile, "character")) {
			character.write(saver);
		} catch (final IOException e) {
			DungeonDiver7.logError(e);
		}
	}

	static void deleteCharacter(final String name, final boolean showResults) {
		final var basePath = CharacterRegistration.getBasePath();
		final var characterFile = basePath + File.separator + name + Strings.fileExtension(FileExtension.CHARACTER);
		final var toDelete = new File(characterFile);
		if (toDelete.exists()) {
			final var success = toDelete.delete();
			if (success) {
				if (showResults) {
					CommonDialogs.showDialog("Character removed.");
				} else {
					CommonDialogs.showDialog("Character " + name + " autoremoved due to version change.");
				}
			} else if (showResults) {
				CommonDialogs.showDialog("Character removal failed!");
			} else {
				CommonDialogs.showDialog("Character " + name + " failed to autoremove!");
			}
		} else if (showResults) {
			CommonDialogs.showDialog("The character to be removed does not have a corresponding file.");
		} else {
			CommonDialogs.showDialog("The character to be autoremoved does not have a corresponding file.");
		}
	}
}
