package org.retropipes.dungeondiver7.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.retropipes.dungeondiver7.DungeonDiver7;

class ObjectEditorBaseActionHandler implements ActionListener {
    /**
     * 
     */
    private final ObjectEditorBase objectEditorBase;

    public ObjectEditorBaseActionHandler(ObjectEditorBase objectEditorBase) {
        this.objectEditorBase = objectEditorBase;
        // Do nothing
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final var ge = this.objectEditorBase;
        try {
    	final var cmd = e.getActionCommand().substring(0, ge.actionCmdLen);
    	final var num = Integer.parseInt(e.getActionCommand().substring(ge.actionCmdLen));
    	ge.handleButtonClick(cmd, num);
    	if (ge.autoStore) {
    	    if (ge.guiEntryType(num) == ObjectEditorBase.ENTRY_TYPE_LIST) {
    		final var list = ge.getEntryList(num);
    		ge.autoStoreEntryListValue(list, num);
    	    } else if (ge.guiEntryType(num) == ObjectEditorBase.ENTRY_TYPE_TEXT) {
    		final var entry = ge.getEntryField(num);
    		ge.autoStoreEntryFieldValue(entry, num);
    	    }
    	}
        } catch (final NumberFormatException nfe) {
    	// Ignore
        } catch (final Exception ex) {
    	DungeonDiver7.logError(ex);
        }
    }
}