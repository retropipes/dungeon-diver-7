/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.loader;

import org.retropipes.diane.asset.image.BufferedImageIcon;

class ImageCacheEntry {
	// Fields
	private BufferedImageIcon entry;
	private String nameEntry;

	// Constructor
	ImageCacheEntry() {
		// Do nothing
	}

	BufferedImageIcon getEntry() {
		return this.entry;
	}

	String getNameEntry() {
		return this.nameEntry;
	}

	void setEntry(final BufferedImageIcon entry1) {
		this.entry = entry1;
	}

	void setNameEntry(final String nameEntry1) {
		this.nameEntry = nameEntry1;
	}
}