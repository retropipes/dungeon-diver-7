/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import com.puttysoftware.images.BufferedImageIcon;

public class EffectImageCache {
    // Fields
    private static CacheEntry[] cache;
    private static int CACHE_INCREMENT = 20;
    private static int CACHE_SIZE = 0;

    // Methods
    static BufferedImageIcon getCachedImage(final String name) {
	if (!EffectImageCache.isInCache(name)) {
	    final BufferedImageIcon bii = EffectImageManager.getUncachedImage(name);
	    EffectImageCache.addToCache(name, bii);
	}
	for (final CacheEntry element : EffectImageCache.cache) {
	    if (name.equals(element.getName())) {
		return element.getImage();
	    }
	}
	return null;
    }

    private static void expandCache() {
	final CacheEntry[] tempCache = new CacheEntry[EffectImageCache.cache.length + EffectImageCache.CACHE_INCREMENT];
	for (int x = 0; x < EffectImageCache.CACHE_SIZE; x++) {
	    tempCache[x] = EffectImageCache.cache[x];
	}
	EffectImageCache.cache = tempCache;
    }

    static synchronized void addToCache(final String name, final BufferedImageIcon bii) {
	if (EffectImageCache.cache == null) {
	    EffectImageCache.cache = new CacheEntry[EffectImageCache.CACHE_INCREMENT];
	}
	if (EffectImageCache.CACHE_SIZE == EffectImageCache.cache.length) {
	    EffectImageCache.expandCache();
	}
	EffectImageCache.cache[EffectImageCache.CACHE_SIZE] = new CacheEntry(bii, name);
	EffectImageCache.CACHE_SIZE++;
    }

    static synchronized boolean isInCache(final String name) {
	if (EffectImageCache.cache == null) {
	    EffectImageCache.cache = new CacheEntry[EffectImageCache.CACHE_INCREMENT];
	}
	for (int x = 0; x < EffectImageCache.CACHE_SIZE; x++) {
	    if (name.equals(EffectImageCache.cache[x].getName())) {
		return true;
	    }
	}
	return false;
    }
}