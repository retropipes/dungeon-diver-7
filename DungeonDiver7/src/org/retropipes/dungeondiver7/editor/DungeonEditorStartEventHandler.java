package org.retropipes.dungeondiver7.editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.retropipes.dungeondiver7.DungeonDiver7;

class DungeonEditorStartEventHandler implements MouseListener {
    private final DungeonEditor editor;

    // handle scroll bars
    public DungeonEditorStartEventHandler(DungeonEditor dungeonEditor) {
	editor = dungeonEditor;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
	try {
	    final var x = e.getX();
	    final var y = e.getY();
	    editor.setPlayerLocation(x, y);
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	}
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
	// Do nothing
    }

    @Override
    public void mouseExited(final MouseEvent e) {
	// Do nothing
    }

    // handle mouse
    @Override
    public void mousePressed(final MouseEvent e) {
	// Do nothing
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
	// Do nothing
    }
}