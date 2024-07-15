package com.puttysoftware.dungeondiver7.loader;

import java.io.IOException;
import java.util.ArrayList;

import org.retropipes.diane.fileio.utility.ResourceStreamReader;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;

public class DataLoader {
	public static String[] loadFamilyNameData() {
		try (final var rsr = new ResourceStreamReader(DataLoader.class
				.getResourceAsStream("/asset/data/name/family" + Strings.fileExtension(FileExtension.INTERNAL_DATA)))) {
			// Fetch data
			final var data = new ArrayList<String>();
			var raw = "0";
			while (raw != null) {
				raw = rsr.readString();
				data.add(raw);
			}
			return data.toArray(new String[data.size()]);
		} catch (final IOException e) {
			DungeonDiver7.logError(e);
			return null;
		}
	}

	public static String[] loadGivenNameData() {
		try (final var rsr = new ResourceStreamReader(DataLoader.class
				.getResourceAsStream("/asset/data/name/given" + Strings.fileExtension(FileExtension.INTERNAL_DATA)))) {
			// Fetch data
			final var data = new ArrayList<String>();
			var raw = "0";
			while (raw != null) {
				raw = rsr.readString();
				data.add(raw);
			}
			return data.toArray(new String[data.size()]);
		} catch (final IOException e) {
			DungeonDiver7.logError(e);
			return null;
		}
	}

	private DataLoader() {
		// Do nothing
	}
}
