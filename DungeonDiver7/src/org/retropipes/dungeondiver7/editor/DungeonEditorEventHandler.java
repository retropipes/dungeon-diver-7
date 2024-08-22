package org.retropipes.dungeondiver7.editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.retropipes.dungeondiver7.DungeonDiver7;

class DungeonEditorEventHandler implements MouseListener, MouseMotionListener, WindowListener {
    /**
     * 
     */
    private final DungeonEditor editor;

    // handle scroll bars
    public DungeonEditorEventHandler(DungeonEditor dungeonEditor) {
	editor = dungeonEditor;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
	try {
	    final var x = e.getX();
	    final var y = e.getY();
	    if (e.isAltDown() || e.isAltGraphDown() || e.isControlDown()) {
		editor.editObjectProperties(x, y);
	    } else if (e.isShiftDown()) {
		editor.probeObjectProperties(x, y);
	    } else {
		editor.editObject(x, y);
	    }
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	}
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
	try {
	    final var x = e.getX();
	    final var y = e.getY();
	    editor.editObject(x, y);
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

    @Override
    public void mouseMoved(final MouseEvent e) {
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

    // Handle windows
    @Override
    public void windowActivated(final WindowEvent we) {
	// Do nothing
    }

    @Override
    public void windowClosed(final WindowEvent we) {
	// Do nothing
    }

    @Override
    public void windowClosing(final WindowEvent we) {
	editor.handleCloseWindow();
	DungeonDiver7.getStuffBag().getGUIManager().showGUI();
    }

    @Override
    public void windowDeactivated(final WindowEvent we) {
	// Do nothing
    }

    @Override
    public void windowDeiconified(final WindowEvent we) {
	// Do nothing
    }

    @Override
    public void windowIconified(final WindowEvent we) {
	// Do nothing
    }

    @Override
    public void windowOpened(final WindowEvent we) {
	// Do nothing
    }
}