package org.retropipes.dungeondiver7.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.creature.characterfile.CharacterRegistration;
import org.retropipes.dungeondiver7.dungeon.current.GenerateDungeonTask;
import org.retropipes.dungeondiver7.game.replay.ReplayManager;
import org.retropipes.dungeondiver7.locale.Menu;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.TimeTravel;
import org.retropipes.dungeondiver7.locale.Untranslated;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

class GameMenuEventHandler implements ActionListener {
    /**
     * 
     */
    private final GameMenuGUI gui;

    public GameMenuEventHandler(GameMenuGUI theGUI) {
        this.gui = theGUI;
        // Do nothing
    }

    // Handle menus
    @Override
    public void actionPerformed(final ActionEvent e) {
        try {
    	final var app = DungeonDiver7.getStuffBag();
    	final var cmd = e.getActionCommand();
    	final var game = app.getGameLogic();
    	if (cmd.equals(Strings.menu(Menu.RESET_CURRENT_LEVEL))) {
    	    final var result = CommonDialogs.showConfirmDialog(Strings.menu(Menu.CONFIRM_RESET_CURRENT_LEVEL),
    		    Strings.untranslated(Untranslated.PROGRAM_NAME));
    	    if (result == CommonDialogs.YES_OPTION) {
    		game.abortAndWaitForMLOLoop();
    		game.resetCurrentLevel();
    	    }
    	} else if (cmd.equals(Strings.menu(Menu.SHOW_SCORE_TABLE))) {
    	    game.showScoreTable();
    	} else if (cmd.equals(Strings.menu(Menu.REPLAY_SOLUTION))) {
    	    game.abortAndWaitForMLOLoop();
    	    game.replaySolution();
    	} else if (cmd.equals(Strings.menu(Menu.RECORD_SOLUTION))) {
    	    game.toggleRecording();
    	} else if (cmd.equals(Strings.menu(Menu.LOAD_REPLAY_FILE))) {
    	    game.abortAndWaitForMLOLoop();
    	    ReplayManager.loadReplay();
    	} else if (cmd.equals(Strings.menu(Menu.PREVIOUS_LEVEL))) {
    	    game.abortAndWaitForMLOLoop();
    	    game.previousLevel();
    	} else if (cmd.equals(Strings.menu(Menu.SKIP_LEVEL))) {
    	    game.abortAndWaitForMLOLoop();
    	    game.solvedLevel(false);
    	} else if (cmd.equals(Strings.menu(Menu.LOAD_LEVEL))) {
    	    game.abortAndWaitForMLOLoop();
    	    game.loadLevel();
    	} else if (cmd.equals(Strings.menu(Menu.SHOW_HINT))) {
    	    CommonDialogs.showDialog(app.getDungeonManager().getDungeon().getHint().trim());
    	} else if (cmd.equals(Strings.menu(Menu.CHEATS))) {
    	    game.enterCheatCode();
    	} else if (cmd.equals(Strings.menu(Menu.CHANGE_OTHER_AMMO))) {
    	    game.changeOtherAmmoMode();
    	} else if (cmd.equals(Strings.menu(Menu.CHANGE_OTHER_TOOL))) {
    	    game.changeOtherToolMode();
    	} else if (cmd.equals(Strings.menu(Menu.CHANGE_OTHER_RANGE))) {
    	    game.changeOtherRangeMode();
    	} else if (cmd.equals(Strings.timeTravel(TimeTravel.FAR_PAST))) {
    	    // Time Travel: Distant Past
    	    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_DISTANT_PAST);
    	    gui.gameEraDistantPast.setSelected(true);
    	    gui.gameEraPast.setSelected(false);
    	    gui.gameEraPresent.setSelected(false);
    	    gui.gameEraFuture.setSelected(false);
    	    gui.gameEraDistantFuture.setSelected(false);
    	} else if (cmd.equals(Strings.timeTravel(TimeTravel.PAST))) {
    	    // Time Travel: Past
    	    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_PAST);
    	    gui.gameEraDistantPast.setSelected(false);
    	    gui.gameEraPast.setSelected(true);
    	    gui.gameEraPresent.setSelected(false);
    	    gui.gameEraFuture.setSelected(false);
    	    gui.gameEraDistantFuture.setSelected(false);
    	} else if (cmd.equals(Strings.timeTravel(TimeTravel.PRESENT))) {
    	    // Time Travel: Present
    	    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_PRESENT);
    	    gui.gameEraDistantPast.setSelected(false);
    	    gui.gameEraPast.setSelected(false);
    	    gui.gameEraPresent.setSelected(true);
    	    gui.gameEraFuture.setSelected(false);
    	    gui.gameEraDistantFuture.setSelected(false);
    	} else if (cmd.equals(Strings.timeTravel(TimeTravel.FUTURE))) {
    	    // Time Travel: Future
    	    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_FUTURE);
    	    gui.gameEraDistantPast.setSelected(false);
    	    gui.gameEraPast.setSelected(false);
    	    gui.gameEraPresent.setSelected(false);
    	    gui.gameEraFuture.setSelected(true);
    	    gui.gameEraDistantFuture.setSelected(false);
    	} else if (cmd.equals(Strings.timeTravel(TimeTravel.FAR_FUTURE))) {
    	    // Time Travel: Distant Future
    	    app.getDungeonManager().getDungeon().switchEra(DungeonConstants.ERA_DISTANT_FUTURE);
    	    gui.gameEraDistantPast.setSelected(false);
    	    gui.gameEraPast.setSelected(false);
    	    gui.gameEraPresent.setSelected(false);
    	    gui.gameEraFuture.setSelected(false);
    	    gui.gameEraDistantFuture.setSelected(true);
    	} else if (cmd.equals(Strings.menu(Menu.NEW_GAME))) {
    	    // Start a new game
    	    final var proceed = app.getGameLogic().newGame();
    	    if (proceed) {
    		new GenerateDungeonTask(true).start();
    	    }
    	} else if (cmd.equals(Strings.menu(Menu.REGISTER_CHARACTER))) {
    	    // Register Character
    	    CharacterRegistration.registerCharacter();
    	} else if (cmd.equals(Strings.menu(Menu.UNREGISTER_CHARACTER))) {
    	    // Unregister Character
    	    CharacterRegistration.unregisterCharacter();
    	} else if (cmd.equals(Strings.menu(Menu.REMOVE_CHARACTER))) {
    	    // Confirm
    	    final var confirm = CommonDialogs
    		    .showConfirmDialog("WARNING: This will DELETE the character from disk,\n"
    			    + "and CANNOT be undone! Proceed anyway?", "Remove Character");
    	    if (confirm == CommonDialogs.YES_OPTION) {
    		// Remove Character
    		CharacterRegistration.removeCharacter();
    	    }
    	} else if (cmd.equals(Strings.menu(Menu.SHOW_EQUIPMENT))) {
    	    InventoryViewer.showEquipmentDialog();
    	} else if (cmd.equals(Strings.menu(Menu.VIEW_STATISTICS))) {
    	    // View Statistics
    	    StatisticsViewer.viewStatistics();
    	}
    	app.getMenus().checkFlags();
        } catch (final Exception ex) {
    	DungeonDiver7.logError(ex);
        }
    }
}