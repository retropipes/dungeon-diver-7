package com.puttysoftware.dungeondiver7.locales;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.loaders.ImageLoader;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utilities.DungeonConstants;
import com.puttysoftware.dungeondiver7.utilities.DifficultyConstants;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class LocaleLoader {
	private static final String LOAD_PATH = "/locale/";
	private static Class<?> LOAD_CLASS = LocaleLoader.class;
	private static ArrayList<HashMap<Integer, String>> STRING_CACHE;
	private static ArrayList<HashMap<Integer, String>> LANGUAGE_STRING_CACHE;
	private static ArrayList<String> LOCALIZED_LANGUAGES;
	private static int LANGUAGE_ID = 0;
	private static String LANGUAGE_NAME = null;

	public static void setDefaultLanguage() {
		LocaleLoader.LOCALIZED_LANGUAGES = null;
		LocaleLoader.LANGUAGE_ID = 0;
		LocaleLoader.LANGUAGE_NAME = LocaleLoader.loadLanguageString(LocaleConstants.LANGUAGE_STRINGS_FILE,
				LocaleLoader.LANGUAGE_ID) + "/";
		// Initialize Image String Cache
		LocaleLoader.STRING_CACHE = new ArrayList<>();
		LocaleLoader.cacheStringFile(LocaleConstants.OBJECT_STRINGS_FILE);
		LocaleLoader.cacheStringFile(LocaleConstants.GENERIC_STRINGS_FILE);
	}

	public static void activeLanguageChanged(final int newLanguageID) {
		LocaleLoader.STRING_CACHE = null;
		LocaleLoader.LOCALIZED_LANGUAGES = null;
		LocaleLoader.LANGUAGE_ID = newLanguageID;
		LocaleLoader.LANGUAGE_NAME = LocaleLoader.loadLanguageString(LocaleConstants.LANGUAGE_STRINGS_FILE,
				LocaleLoader.LANGUAGE_ID) + "/";
		DifficultyConstants.reloadDifficultyNames();
		DungeonConstants.activeLanguageChanged();
		DungeonDiver7.getApplication().activeLanguageChanged();
		PrefsManager.activeLanguageChanged();
		ImageLoader.activeLanguageChanged();
	}

	public static String[] loadLocalizedLanguagesList() {
		if (LocaleLoader.LOCALIZED_LANGUAGES == null) {
			LocaleLoader.LOCALIZED_LANGUAGES = new ArrayList<>();
			final String filename = LocaleConstants.LOCALIZED_LANGUAGE_FILE_NAME;
			try (final InputStream is = LocaleLoader.LOAD_CLASS
					.getResourceAsStream(LocaleLoader.LOAD_PATH + LocaleLoader.LANGUAGE_NAME + filename);
					final ResourceStreamReader rsr = new ResourceStreamReader(is)) {
				String line = LocaleConstants.COMMON_STRING_EMPTY;
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
				DungeonDiver7.getErrorLoggerDirectly().logError(ioe);
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
			String line = LocaleConstants.COMMON_STRING_EMPTY;
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
			DungeonDiver7.getErrorLoggerDirectly().logError(ioe);
		}
	}

	private static void cacheLanguageStringFile(final int fileID) {
		final String filename = LocaleConstants.LANGUAGE_STRINGS_FILES[fileID];
		try (final InputStream is = LocaleLoader.LOAD_CLASS
				.getResourceAsStream(LocaleLoader.LOAD_PATH + filename + LocaleConstants.STRINGS_EXTENSION);
				final ResourceStreamReader rsr = new ResourceStreamReader(is)) {
			String line = LocaleConstants.COMMON_STRING_EMPTY;
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
			DungeonDiver7.getErrorLoggerDirectly().logError(ioe);
		}
	}
}
