/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.editor;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.retropipes.dungeondiver7.DungeonDiver7;

class ObjectEditorBaseFocusHandler implements FocusListener {
    /**
     * 
     */
    private final ObjectEditorBase self;

    public ObjectEditorBaseFocusHandler(ObjectEditorBase objectEditorBase) {
	this.self = objectEditorBase;
	// Do nothing
    }

    @Override
    public void focusGained(final FocusEvent fe) {
	// Do nothing
    }

    @Override
    public void focusLost(final FocusEvent fe) {
	final var ge = this.self;
	try {
	    final var comp = fe.getComponent();
	    if (comp.getClass().equals(JTextField.class)) {
		final var entry = (JTextField) comp;
		final var num = Integer.parseInt(entry.getName());
		ge.autoStoreEntryFieldValue(entry, num);
	    } else if (comp.getClass().equals(JComboBox.class)) {
		@SuppressWarnings("unchecked")
		final var list = (JComboBox<String>) comp;
		final var num = Integer.parseInt(list.getName());
		ge.autoStoreEntryListValue(list, num);
	    }
	} catch (final NumberFormatException nfe) {
	    // Ignore
	} catch (final Exception ex) {
	    DungeonDiver7.logError(ex);
	}
    }
}