package org.retropipes.dungeondiver7.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.locale.EditorString;
import org.retropipes.dungeondiver7.locale.Layer;
import org.retropipes.dungeondiver7.locale.Strings;

class DungeonEditorSwitcherHandler implements ActionListener {
    private final DungeonEditor editor;

    DungeonEditorSwitcherHandler(DungeonEditor dungeonEditor) {
	editor = dungeonEditor;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	try {
	    final var cmd = e.getActionCommand();
	    final var ae = editor;
	    if (cmd.equals(Strings.editor(EditorString.LOWER_GROUND_LAYER))) {
		ae.changeLayerImpl(Layer.GROUND.ordinal());
	    } else if (cmd.equals(Strings.editor(EditorString.UPPER_GROUND_LAYER))) {
		ae.changeLayerImpl(Layer.OBJECT.ordinal());
	    } else if (cmd.equals(Strings.editor(EditorString.LOWER_OBJECTS_LAYER))) {
		ae.changeLayerImpl(Layer.STATUS.ordinal());
	    } else if (cmd.equals(Strings.editor(EditorString.UPPER_OBJECTS_LAYER))) {
		ae.changeLayerImpl(Layer.MARKER.ordinal());
	    }
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	}
    }
}