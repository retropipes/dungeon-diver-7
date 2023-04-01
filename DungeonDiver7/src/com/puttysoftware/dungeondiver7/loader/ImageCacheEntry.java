/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.loader;

import com.puttysoftware.diane.assets.image.BufferedImageIcon;

class ImageCacheEntry {
    // Fields
    private BufferedImageIcon entry;
    private String nameEntry;

    // Constructor
    ImageCacheEntry() {
        // Do nothing
    }

    // Methods
    BufferedImageIcon getEntry() {
        return this.entry;
    }

    void setEntry(final BufferedImageIcon entry1) {
        this.entry = entry1;
    }

    String getNameEntry() {
        return this.nameEntry;
    }

    void setNameEntry(final String nameEntry1) {
        this.nameEntry = nameEntry1;
    }
}