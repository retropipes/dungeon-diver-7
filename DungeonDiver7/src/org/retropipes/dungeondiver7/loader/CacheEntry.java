/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.loader;

import org.retropipes.diane.asset.image.BufferedImageIcon;

final class CacheEntry {
	// Fields
	private final BufferedImageIcon image;
	private final String name;

	// Constructor
	CacheEntry(final BufferedImageIcon newImage, final String newName) {
		this.image = newImage;
		this.name = newName;
	}

	BufferedImageIcon getImage() {
		return this.image;
	}

	String getName() {
		return this.name;
	}
}