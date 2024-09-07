package org.retropipes.dungeondiver7.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.locale.Layer;
import org.retropipes.dungeondiver7.locale.Strings;

class EditorSwitcherHandler implements ActionListener {
    private final Editor editor;

    EditorSwitcherHandler(Editor theEditor) {
	editor = theEditor;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
	try {
	    final var cmd = e.getActionCommand();
	    final var ae = editor;
	    if (cmd.equals(Strings.layer(Layer.GROUND))) {
		ae.changeLayerImpl(Layer.GROUND.ordinal());
	    } else if (cmd.equals(Strings.layer(Layer.OBJECT))) {
		ae.changeLayerImpl(Layer.OBJECT.ordinal());
	    } else if (cmd.equals(Strings.layer(Layer.STATUS))) {
		ae.changeLayerImpl(Layer.STATUS.ordinal());
	    } else if (cmd.equals(Strings.layer(Layer.MARKER))) {
		ae.changeLayerImpl(Layer.MARKER.ordinal());
	    }
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	}
    }
}