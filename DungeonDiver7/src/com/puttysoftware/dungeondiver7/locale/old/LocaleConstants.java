package com.puttysoftware.dungeondiver7.locale.old;

public class LocaleConstants {
    // File Constants
    static final String STRINGS_EXTENSION = ".properties";
    public static final int OBJECT_STRINGS_FILE = 0;
    public static final int IMAGE_STRINGS_FILE = -1;
    private static final String OBJECT_STRINGS_FILE_NAME = "object";
    static final String[] STRINGS_FILES = new String[] { LocaleConstants.OBJECT_STRINGS_FILE_NAME };
    private static final String IMAGE_STRINGS_FILE_NAME = "image";
    static final String[] LANGUAGE_STRINGS_FILES = new String[] { LocaleConstants.IMAGE_STRINGS_FILE_NAME };
    // Common String Constants
    public static final String COMMON_STRING_SPACE = " ";
    public static final String COMMON_STRING_UNDERSCORE = "_";
    public static final String COMMON_STRING_NOTL_PERIOD = ".";
    public static final String COMMON_STRING_ZERO = "0";
    public static final String COMMON_STRING_COLON = ":";
    public static final String COMMON_STRING_CLOSE_PARENTHESES = ")";
    public static final String COMMON_STRING_OPEN_PARENTHESES = "(";
    public static final String COMMON_STRING_SPACE_DASH_SPACE = " - ";

    // Private constructor
    private LocaleConstants() {
	// Do nothing
    }
}
