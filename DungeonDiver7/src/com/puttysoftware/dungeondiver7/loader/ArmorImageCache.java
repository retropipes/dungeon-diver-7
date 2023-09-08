/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import com.puttysoftware.diane.assets.image.BufferedImageIcon;

public class ArmorImageCache {
    // Fields
    private static CacheEntry[] cache;
    private static int CACHE_INCREMENT = 20;
    private static int CACHE_SIZE = 0;

    static synchronized void addToCache(final String name, final BufferedImageIcon bii) {
	if (ArmorImageCache.cache == null) {
	    ArmorImageCache.cache = new CacheEntry[ArmorImageCache.CACHE_INCREMENT];
	}
	if (ArmorImageCache.CACHE_SIZE == ArmorImageCache.cache.length) {
	    ArmorImageCache.expandCache();
	}
	ArmorImageCache.cache[ArmorImageCache.CACHE_SIZE] = new CacheEntry(bii, name);
	ArmorImageCache.CACHE_SIZE++;
    }

    private static void expandCache() {
	final var tempCache = new CacheEntry[ArmorImageCache.cache.length + ArmorImageCache.CACHE_INCREMENT];
	for (var x = 0; x < ArmorImageCache.CACHE_SIZE; x++) {
	    tempCache[x] = ArmorImageCache.cache[x];
	}
	ArmorImageCache.cache = tempCache;
    }

    static BufferedImageIcon getCachedImage(final String name) {
	if (!ArmorImageCache.isInCache(name)) {
	    final var bii = ArmorImageManager.getUncachedImage(name);
	    ArmorImageCache.addToCache(name, bii);
	}
	for (final CacheEntry element : ArmorImageCache.cache) {
	    if (name.equals(element.getName())) {
		return element.getImage();
	    }
	}
	return null;
    }

    static synchronized boolean isInCache(final String name) {
	if (ArmorImageCache.cache == null) {
	    ArmorImageCache.cache = new CacheEntry[ArmorImageCache.CACHE_INCREMENT];
	}
	for (var x = 0; x < ArmorImageCache.CACHE_SIZE; x++) {
	    if (name.equals(ArmorImageCache.cache[x].getName())) {
		return true;
	    }
	}
	return false;
    }
}