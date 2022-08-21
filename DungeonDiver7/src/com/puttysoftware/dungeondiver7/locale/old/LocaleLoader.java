package com.puttysoftware.dungeondiver7.locale.old;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.puttysoftware.dungeondiver7.utility.FileExtensions;

public class LocaleLoader {
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
}
