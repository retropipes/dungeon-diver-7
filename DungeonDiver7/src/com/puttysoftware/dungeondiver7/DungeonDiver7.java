/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import com.puttysoftware.diane.ErrorLogger;
import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.loaders.LogoLoader;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;

public class DungeonDiver7 {
	private DungeonDiver7() {
		// Do nothing
	}

	// Constants
	private static Application application;
	private static String PROGRAM_NAME = "Dungeon Diver 7";
	private static String ERROR_MESSAGE = null;
	private static String ERROR_TITLE = null;
	private static ErrorLogger errorLogger;

	// Methods
	public static Application getApplication() {
		return DungeonDiver7.application;
	}

	public static ErrorLogger getErrorLogger() {
		CommonDialogs.showErrorDialog(DungeonDiver7.ERROR_MESSAGE, DungeonDiver7.ERROR_TITLE);
		return DungeonDiver7.errorLogger;
	}

	public static ErrorLogger getErrorLoggerDirectly() {
		return DungeonDiver7.errorLogger;
	}

	public static void main(final String[] args) {
		try {
			// Integrate with host platform
			// Platform.hookLAF(DungeonDiver7.PROGRAM_NAME);
			try {
				// Initialize strings
				DungeonDiver7.initStrings();
				// Initialize error logger
				DungeonDiver7.errorLogger = new ErrorLogger(DungeonDiver7.PROGRAM_NAME);
			} catch (final RuntimeException re) {
				// Something has gone horribly wrong
				CommonDialogs.showErrorDialog("Something has gone horribly wrong trying to load the string data!",
						"FATAL ERROR");
				System.exit(1);
			}
			// Create and initialize application
			DungeonDiver7.application = new Application();
			DungeonDiver7.application.postConstruct();
			// Set Up Common Dialogs
			CommonDialogs.setDefaultTitle(DungeonDiver7.PROGRAM_NAME);
			CommonDialogs.setIcon(LogoLoader.getMicroLogo());
			// Initialize preferences
			PrefsManager.readPrefs();
			LocaleLoader.activeLanguageChanged(PrefsManager.getLanguageID());
			// Register platform hooks
//			Platform.hookAbout(DungeonDiver7.application.getAboutDialog(),
//					DungeonDiver7.application.getAboutDialog().getClass().getDeclaredMethod(StringLoader.loadString(
//							StringConstants.NOTL_STRINGS_FILE, StringConstants.NOTL_STRING_SHOW_ABOUT_DIALOG_METHOD)));
//			Platform.hookPreferences(PreferencesManager.class,
//					PreferencesManager.class.getDeclaredMethod(StringLoader.loadString(
//							StringConstants.NOTL_STRINGS_FILE, StringConstants.NOTL_STRING_SHOW_PREFERENCES_METHOD)));
//			Platform.hookQuit(DungeonDiver7.application.getGUIManager(),
//					DungeonDiver7.application.getGUIManager().getClass().getDeclaredMethod(StringLoader.loadString(
//							StringConstants.NOTL_STRINGS_FILE, StringConstants.NOTL_STRING_QUIT_HANDLER_METHOD)));
//			Platform.hookDockIcon(LogoManager.getMiniatureLogo());
			// Display GUI
			DungeonDiver7.application.getGUIManager().showGUI();
		} catch (final Throwable t) {
			DungeonDiver7.getErrorLogger().logError(t);
		}
	}

	private static void initStrings() {
		LocaleLoader.setDefaultLanguage();
		DungeonDiver7.ERROR_TITLE = LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE,
				LocaleConstants.ERROR_STRING_ERROR_TITLE);
		DungeonDiver7.ERROR_MESSAGE = LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE,
				LocaleConstants.ERROR_STRING_ERROR_MESSAGE);
	}
}
