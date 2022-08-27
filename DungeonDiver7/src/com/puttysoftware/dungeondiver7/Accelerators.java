/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import javax.swing.KeyStroke;

import com.puttysoftware.dungeondiver7.prefs.Prefs;

public abstract class Accelerators {
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
    
    public static Accelerators getAcceleratorModel() {
	if (Prefs.useClassicAccelerators()) {
	    return new ClassicAccelerators();
	} else {
	    return new ModernAccelerators();
	}
    }
}
