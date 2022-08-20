package com.puttysoftware.dungeondiver7.locale.old;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.utility.FileExtensions;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class LocaleLoader {
    private static final String LOAD_PATH = FilePaths.BASE;
    private static Class<?> LOAD_CLASS = LocaleLoader.class;
    private static ArrayList<HashMap<Integer, String>> STRING_CACHE;
    private static ArrayList<HashMap<Integer, String>> LANGUAGE_STRING_CACHE;
    private static ArrayList<String> LOCALIZED_LANGUAGES;
    private static String LANGUAGE_NAME = null;
    private static final Properties GLOBAL = new Properties();
    private static final String ERROR = "Error!";

    public static String[] loadAllGlobalStrings(final GlobalFile file, final int max) {
	final String[] retVal = new String[max];
	try (InputStream stream = LocaleLoader.class.getResourceAsStream(
		FilePaths.BASE + GlobalFileList.LIST[file.ordinal()] + FileExtensions.getStringExtensionWithPeriod())) {
	    LocaleLoader.GLOBAL.load(stream);
	    for (int k = 0; k < max; k++) {
		final String key = Integer.toString(k);
		retVal[k] = LocaleLoader.GLOBAL.getProperty(key);
	    }
	} catch (final IOException ioe) {
	    for (int k = 0; k < max; k++) {
		retVal[k] = LocaleLoader.ERROR;
	    }
	}
	return retVal;
    }

    public static String loadGlobalString(final GlobalFile file, final int key) {
	return LocaleLoader.loadGlobalString(file, Integer.toString(key));
    }

    public static String loadGlobalString(final GlobalFile file, final String key) {
	try (InputStream stream = LocaleLoader.class.getResourceAsStream(
		FilePaths.BASE + GlobalFileList.LIST[file.ordinal()] + FileExtensions.getStringExtensionWithPeriod())) {
	    LocaleLoader.GLOBAL.load(stream);
	    return LocaleLoader.GLOBAL.getProperty(key);
	} catch (final IOException ioe) {
	    return LocaleLoader.ERROR;
	}
    }

    public static String[] loadLocalizedLanguagesList() {
	if (LocaleLoader.LOCALIZED_LANGUAGES == null) {
	    LocaleLoader.LOCALIZED_LANGUAGES = new ArrayList<>();
	    final String filename = LocaleConstants.LOCALIZED_LANGUAGE_FILE_NAME;
	    try (final InputStream is = LocaleLoader.LOAD_CLASS
		    .getResourceAsStream(LocaleLoader.LOAD_PATH + LocaleLoader.LANGUAGE_NAME + filename);
		    final ResourceStreamReader rsr = new ResourceStreamReader(is)) {
		String line = Strings.EMPTY;
		while (line != null) {
		    // Read line
		    line = rsr.readString();
		    if (line != null) {
			// Parse line
			LocaleLoader.LOCALIZED_LANGUAGES.add(line);
		    }
		}
	    } catch (final IOException ioe) {
		CommonDialogs.showErrorDialog("Something has gone horribly wrong trying to load the language data!",
			"FATAL ERROR");
		DungeonDiver7.logErrorDirectly(ioe);
	    }
	}
	return LocaleLoader.LOCALIZED_LANGUAGES.toArray(new String[LocaleLoader.LOCALIZED_LANGUAGES.size()]);
    }

    private static String loadLanguageString(final int fileID, final int strID) {
	if (LocaleLoader.LANGUAGE_STRING_CACHE == null) {
	    // Create string cache
	    LocaleLoader.LANGUAGE_STRING_CACHE = new ArrayList<>();
	}
	if (LocaleLoader.LANGUAGE_STRING_CACHE.size() <= fileID
		|| LocaleLoader.LANGUAGE_STRING_CACHE.get(fileID) == null) {
	    // Cache string file
	    LocaleLoader.cacheLanguageStringFile(fileID);
	}
	String value = LocaleLoader.LANGUAGE_STRING_CACHE.get(fileID).get(Integer.valueOf(strID));
	if (value == null) {
	    // Cache string file
	    LocaleLoader.cacheLanguageStringFile(fileID);
	    // Get correct value
	    value = LocaleLoader.LANGUAGE_STRING_CACHE.get(fileID).get(Integer.valueOf(strID));
	}
	return value;
    }

    public static String loadString(final int fileID, final int strID) {
	if (fileID < 0) {
	    return LocaleLoader.loadLanguageString(-LocaleConstants.NOTL_STRINGS_FILE, strID);
	} else {
	    if (LocaleLoader.STRING_CACHE == null) {
		// Create string cache
		LocaleLoader.STRING_CACHE = new ArrayList<>();
	    }
	    if (LocaleLoader.STRING_CACHE.size() <= fileID || LocaleLoader.STRING_CACHE.get(fileID) == null) {
		// Cache string file
		LocaleLoader.cacheStringFile(fileID);
	    }
	    String value = LocaleLoader.STRING_CACHE.get(fileID).get(Integer.valueOf(strID));
	    if (value == null) {
		// Cache string file
		LocaleLoader.cacheStringFile(fileID);
		// Get correct value
		value = LocaleLoader.STRING_CACHE.get(fileID).get(Integer.valueOf(strID));
	    }
	    return value;
	}
    }

    private static void cacheStringFile(final int fileID) {
	final String filename = LocaleConstants.STRINGS_FILES[fileID];
	try (final InputStream is = LocaleLoader.LOAD_CLASS.getResourceAsStream(
		LocaleLoader.LOAD_PATH + LocaleLoader.LANGUAGE_NAME + filename + LocaleConstants.STRINGS_EXTENSION);
		final ResourceStreamReader rsr = new ResourceStreamReader(is)) {
	    String line = Strings.EMPTY;
	    while (line != null) {
		// Read line
		line = rsr.readString();
		if (line != null) {
		    // Parse line
		    final String[] splitLine = line.split(" = ");
		    if (LocaleLoader.STRING_CACHE.size() <= fileID || LocaleLoader.STRING_CACHE.get(fileID) == null) {
			// Entry for string file doesn't exist, so create it
			for (int x = 0; x <= fileID; x++) {
			    if (LocaleLoader.STRING_CACHE.size() <= x || LocaleLoader.STRING_CACHE.get(x) == null) {
				LocaleLoader.STRING_CACHE.add(new HashMap<Integer, String>());
			    }
			}
		    }
		    LocaleLoader.STRING_CACHE.get(fileID).put(Integer.valueOf(Integer.parseInt(splitLine[0])),
			    splitLine[1]);
		}
	    }
	} catch (final IOException ioe) {
	    CommonDialogs.showErrorDialog("Something has gone horribly wrong trying to load the string data!",
		    "FATAL ERROR");
	    DungeonDiver7.logErrorDirectly(ioe);
	}
    }

    private static void cacheLanguageStringFile(final int fileID) {
	final String filename = LocaleConstants.LANGUAGE_STRINGS_FILES[fileID];
	try (final InputStream is = LocaleLoader.LOAD_CLASS
		.getResourceAsStream(LocaleLoader.LOAD_PATH + filename + LocaleConstants.STRINGS_EXTENSION);
		final ResourceStreamReader rsr = new ResourceStreamReader(is)) {
	    String line = Strings.EMPTY;
	    while (line != null) {
		// Read line
		line = rsr.readString();
		if (line != null) {
		    // Parse line
		    final String[] splitLine = line.split(" = ");
		    if (LocaleLoader.LANGUAGE_STRING_CACHE.size() <= fileID
			    || LocaleLoader.LANGUAGE_STRING_CACHE.get(fileID) == null) {
			// Entry for string file doesn't exist, so create it
			for (int x = 0; x <= fileID; x++) {
			    if (LocaleLoader.LANGUAGE_STRING_CACHE.size() <= x
				    || LocaleLoader.LANGUAGE_STRING_CACHE.get(x) == null) {
				LocaleLoader.LANGUAGE_STRING_CACHE.add(new HashMap<Integer, String>());
			    }
			}
		    }
		    LocaleLoader.LANGUAGE_STRING_CACHE.get(fileID).put(Integer.valueOf(Integer.parseInt(splitLine[0])),
			    splitLine[1]);
		}
	    }
	} catch (final IOException ioe) {
	    CommonDialogs.showErrorDialog("Something has gone horribly wrong trying to load the language string data!",
		    "FATAL ERROR");
	    DungeonDiver7.logErrorDirectly(ioe);
	}
    }
}
