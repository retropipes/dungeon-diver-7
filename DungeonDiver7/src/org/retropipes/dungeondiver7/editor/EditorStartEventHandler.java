/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.retropipes.dungeondiver7.DungeonDiver7;

class EditorStartEventHandler implements MouseListener {
    private final Editor editor;

    // handle scroll bars
    public EditorStartEventHandler(Editor theEditor) {
	this.editor = theEditor;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
	try {
	    final var x = e.getX();
	    final var y = e.getY();
	    this.editor.setPlayerLocation(x, y);
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