/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7;

import org.retropipes.diane.Diane;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.diane.integration.Integration;
import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.locale.ErrorString;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.settings.Settings;
import org.retropipes.dungeondiver7.settings.SettingsRequest;

public class DungeonDiver7 {
    // Constants
    private static StuffBag stuffBag;
    private static String PROGRAM_NAME = "Dungeon Diver 7";
    private static String ERROR_MESSAGE = null;
    private static String ERROR_TITLE = null;
    private static final int MAIN_WINDOW_SIZE = 512;
    private static final int BATTLE_MAP_SIZE = 10;
    private static final int DUNGEON_BASE_SIZE = 24;
    private static final int DUNGEON_SIZE_INCREMENT = 2;

    public static int getBattleDungeonSize() {
	return DungeonDiver7.BATTLE_MAP_SIZE;
    }

    public static int getDungeonLevelSize(final int zoneID) {
	return DungeonDiver7.DUNGEON_BASE_SIZE + zoneID * DungeonDiver7.DUNGEON_SIZE_INCREMENT;
    }

    public static StuffBag getStuffBag() {
	return DungeonDiver7.stuffBag;
    }

    public static void logError(final Throwable t) {
	CommonDialogs.showErrorDialog(DungeonDiver7.ERROR_MESSAGE, DungeonDiver7.ERROR_TITLE);
	t.printStackTrace();
	Diane.handleError(t);
    }

    public static void logErrorDirectly(final Throwable t) {
	t.printStackTrace();
	Diane.handleError(t);
    }

    public static void logWarningDirectly(final Throwable t) {
	t.printStackTrace(System.out);
	Diane.handleWarning(t);
    }

    public static void main(final String[] args) {
	try {
	    try {
		// Initialize strings
		DungeonDiver7.preInit();
		// Initialize error logger
		Diane.installDefaultErrorHandler(DungeonDiver7.PROGRAM_NAME);
	    } catch (final RuntimeException re) {
		// Something has gone horribly wrong
		System.err.println("Something has gone horribly wrong trying to load the string data!");
		System.exit(1);
	    }
	    // Integrate with host platform
	    final var i = Integration.integrate();
	    i.configureLookAndFeel();
	    // Create main window
	    MainWindow.createMainWindow(MAIN_WINDOW_SIZE, MAIN_WINDOW_SIZE);
	    // Create and initialize application
	    DungeonDiver7.stuffBag = new StuffBag();
	    // Set Up Common Dialogs
	    CommonDialogs.setDefaultTitle(DungeonDiver7.PROGRAM_NAME);
	    CommonDialogs.setIcon(LogoLoader.getIconLogo());
	    // Initialize preferences
	    Settings.init();
	    Settings.readPrefs();
	    // Register platform hooks
	    i.setAboutHandler(DungeonDiver7.stuffBag.getAboutDialog());
	    i.setPreferencesHandler(new SettingsRequest());
	    i.setQuitHandler(DungeonDiver7.stuffBag.getGUIManager());
	    // Display GUI
	    DungeonDiver7.stuffBag.getGUIManager().showGUI();
	} catch (final Throwable t) {
	    DungeonDiver7.logError(t);
	}
    }

    private static void preInit() {
	Creature.computeActionCap(DungeonDiver7.BATTLE_MAP_SIZE, DungeonDiver7.BATTLE_MAP_SIZE);
	DungeonDiver7.ERROR_TITLE = Strings.error(ErrorString.ERROR_TITLE);
	DungeonDiver7.ERROR_MESSAGE = Strings.error(ErrorString.ERROR_MESSAGE);
    }

    private DungeonDiver7() {
	// Do nothing
    }
}
