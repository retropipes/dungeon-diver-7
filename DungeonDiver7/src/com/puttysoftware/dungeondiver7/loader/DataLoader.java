package com.puttysoftware.dungeondiver7.loader;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.diane.fileio.utility.ResourceStreamReader;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;

public class DataLoader {
    private DataLoader() {
        // Do nothing
    }

    public static String[] loadGivenNameData() {
        try (final ResourceStreamReader rsr = new ResourceStreamReader(DataLoader.class.getResourceAsStream(
                "/asset/data/name/given" + Strings.fileExtension(FileExtension.INTERNAL_DATA)))) {
            // Fetch data
            final ArrayList<String> data = new ArrayList<>();
            String raw = "0";
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

    public static String[] loadFamilyNameData() {
        try (final ResourceStreamReader rsr = new ResourceStreamReader(DataLoader.class.getResourceAsStream(
                "/asset/data/name/family" + Strings.fileExtension(FileExtension.INTERNAL_DATA)))) {
            // Fetch data
            final ArrayList<String> data = new ArrayList<>();
            String raw = "0";
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
}
