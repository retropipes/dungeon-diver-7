package org.retropipes.dungeondiver7.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.StuffBag;
import org.retropipes.dungeondiver7.locale.EditorString;
import org.retropipes.dungeondiver7.locale.Menu;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.TimeTravel;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

class EditorMenuHandler implements ActionListener {
    private final Editor editor;

    public EditorMenuHandler(Editor theEditor) {
	editor = theEditor;
    }

    // Handle menus
    @Override
    public void actionPerformed(final ActionEvent e) {
	try {
	    final var app = DungeonDiver7.getStuffBag();
	    final var cmd = e.getActionCommand();
	    if (cmd.equals(Strings.menu(Menu.UNDO))) {
		// Undo most recent action
		if (app.getMode() == StuffBag.STATUS_EDITOR) {
		    editor.undo();
		} else if (app.getMode() == StuffBag.STATUS_GAME) {
		    app.getGame().abortAndWaitForMLOLoop();
		    app.getGame().undoLastMove();
		}
	    } else if (cmd.equals(Strings.menu(Menu.REDO))) {
		// Redo most recent undone action
		if (app.getMode() == StuffBag.STATUS_EDITOR) {
		    editor.redo();
		} else if (app.getMode() == StuffBag.STATUS_GAME) {
		    app.getGame().abortAndWaitForMLOLoop();
		    app.getGame().redoLastMove();
		}
	    } else if (cmd.equals(Strings.menu(Menu.CUT_LEVEL))) {
		// Cut Level
		final var level = editor.getLocationManager().getEditorLocationU();
		app.getDungeonManager().getDungeonBase().cutLevel();
		editor.fixLimits();
		editor.updateEditorLevelAbsolute(level);
	    } else if (cmd.equals(Strings.menu(Menu.COPY_LEVEL))) {
		// Copy Level
		app.getDungeonManager().getDungeonBase().copyLevel();
	    } else if (cmd.equals(Strings.menu(Menu.PASTE_LEVEL))) {
		// Paste Level
		app.getDungeonManager().getDungeonBase().pasteLevel();
		editor.fixLimits();
		editor.redrawEditor();
	    } else if (cmd.equals(Strings.menu(Menu.INSERT_LEVEL_FROM_CLIPBOARD))) {
		// Insert Level From Clipboard
		app.getDungeonManager().getDungeonBase().insertLevelFromClipboard();
		editor.fixLimits();
	    } else if (cmd.equals(Strings.menu(Menu.CLEAR_HISTORY))) {
		// Clear undo/redo history, confirm first
		final var res = CommonDialogs.showConfirmDialog(Strings.menu(Menu.CONFIRM_CLEAR_HISTORY),
			Strings.editor(EditorString.EDITOR));
		if (res == CommonDialogs.YES_OPTION) {
		    editor.clearHistory();
		}
	    } else if (cmd.equals(Strings.menu(Menu.GO_TO_LEVEL))) {
		// Go To Level
		editor.goToLevelHandler();
	    } else if (cmd.equals(Strings.menu(Menu.UP_ONE_FLOOR))) {
		// Go up one floor
		editor.updateEditorPosition(1, 0);
	    } else if (cmd.equals(Strings.menu(Menu.DOWN_ONE_FLOOR))) {
		// Go down one floor
		editor.updateEditorPosition(-1, 0);
	    } else if (cmd.equals(Strings.menu(Menu.UP_ONE_LEVEL))) {
		// Go up one level
		editor.updateEditorPosition(0, 1);
	    } else if (cmd.equals(Strings.menu(Menu.DOWN_ONE_LEVEL))) {
		// Go down one level
		editor.updateEditorPosition(0, -1);
	    } else if (cmd.equals(Strings.menu(Menu.ADD_A_LEVEL))) {
		// Add a level
		editor.addLevel();
	    } else if (cmd.equals(Strings.menu(Menu.REMOVE_A_LEVEL))) {
		// Remove a level
		editor.removeLevel();
	    } else if (cmd.equals(Strings.menu(Menu.FILL_CURRENT_LEVEL))) {
		// Fill level
		editor.fillLevel();
	    } else if (cmd.equals(Strings.menu(Menu.RESIZE_CURRENT_LEVEL))) {
		// Resize level
		editor.resizeLevel();
	    } else if (cmd.equals(Strings.menu(Menu.LEVEL_PREFERENCES))) {
		// Set Level Preferences
		editor.setLevelPrefs();
	    } else if (cmd.equals(Strings.menu(Menu.SET_START_POINT))) {
		// Set Start Point
		editor.editPlayerLocation();
	    } else if (cmd.equals(Strings.menu(Menu.SET_MUSIC))) {
		// Set Music
		editor.defineDungeonMusic();
	    } else if (cmd.equals(Strings.menu(Menu.CHANGE_LAYER))) {
		// Change Layer
		editor.changeLayer();
	    } else if (cmd.equals(Strings.menu(Menu.ENABLE_GLOBAL_MOVE_SHOOT))) {
		// Enable Global Move-Shoot
		editor.enableGlobalMoveShoot();
	    } else if (cmd.equals(Strings.menu(Menu.DISABLE_GLOBAL_MOVE_SHOOT))) {
		// Disable Global Move-Shoot
		editor.disableGlobalMoveShoot();
	    } else if (cmd.equals(Strings.timeTravel(TimeTravel.FAR_PAST))) {
		// Time Travel: Distant Past
		app.getDungeonManager().getDungeonBase().switchEra(DungeonConstants.ERA_DISTANT_PAST);
		editor.editorEraDistantPast.setSelected(true);
		editor.editorEraPast.setSelected(false);
		editor.editorEraPresent.setSelected(false);
		editor.editorEraFuture.setSelected(false);
		editor.editorEraDistantFuture.setSelected(false);
	    } else if (cmd.equals(Strings.timeTravel(TimeTravel.PAST))) {
		// Time Travel: Past
		app.getDungeonManager().getDungeonBase().switchEra(DungeonConstants.ERA_PAST);
		editor.editorEraDistantPast.setSelected(false);
		editor.editorEraPast.setSelected(true);
		editor.editorEraPresent.setSelected(false);
		editor.editorEraFuture.setSelected(false);
		editor.editorEraDistantFuture.setSelected(false);
	    } else if (cmd.equals(Strings.timeTravel(TimeTravel.PRESENT))) {
		// Time Travel: Present
		app.getDungeonManager().getDungeonBase().switchEra(DungeonConstants.ERA_PRESENT);
		editor.editorEraDistantPast.setSelected(false);
		editor.editorEraPast.setSelected(false);
		editor.editorEraPresent.setSelected(true);
		editor.editorEraFuture.setSelected(false);
		editor.editorEraDistantFuture.setSelected(false);
	    } else if (cmd.equals(Strings.timeTravel(TimeTravel.FUTURE))) {
		// Time Travel: Future
		app.getDungeonManager().getDungeonBase().switchEra(DungeonConstants.ERA_FUTURE);
		editor.editorEraDistantPast.setSelected(false);
		editor.editorEraPast.setSelected(false);
		editor.editorEraPresent.setSelected(false);
		editor.editorEraFuture.setSelected(true);
		editor.editorEraDistantFuture.setSelected(false);
	    } else if (cmd.equals(Strings.timeTravel(TimeTravel.FAR_FUTURE))) {
		// Time Travel: Distant Future
		app.getDungeonManager().getDungeonBase().switchEra(DungeonConstants.ERA_DISTANT_FUTURE);
		editor.editorEraDistantPast.setSelected(false);
		editor.editorEraPast.setSelected(false);
		editor.editorEraPresent.setSelected(false);
		editor.editorEraFuture.setSelected(false);
		editor.editorEraDistantFuture.setSelected(true);
	    }
	    app.getMenus().checkFlags();
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	}
    }
}