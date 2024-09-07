/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7;

import javax.swing.KeyStroke;

import org.retropipes.dungeondiver7.settings.Settings;

public abstract class Accelerators {
    public static Accelerators getAcceleratorModel() {
	if (Settings.useClassicAccelerators()) {
	    return new ClassicAccelerators();
	}
	return new ModernAccelerators();
    }

    public KeyStroke fileNewAccel, fileOpenAccel, fileCloseAccel, fileSaveAccel, fileSaveAsAccel, filePreferencesAccel,
	    filePrintAccel, fileExitAccel;
    public KeyStroke playPlayDungeonAccel, playEditDungeonAccel;
    public KeyStroke gameResetAccel, gameShowTableAccel;
    public KeyStroke editorUndoAccel, editorRedoAccel, editorCutLevelAccel, editorCopyLevelAccel, editorPasteLevelAccel,
	    editorInsertLevelFromClipboardAccel, editorClearHistoryAccel, editorGoToLocationAccel,
	    editorUpOneLevelAccel, editorDownOneLevelAccel;

    Accelerators() {
	// Do nothing
    }
}
