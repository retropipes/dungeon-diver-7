/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import org.retropipes.diane.asset.image.BufferedImageIcon;

public class CrystalImageCache {
	// Fields
	private static CacheEntry[] cache;
	private static int CACHE_INCREMENT = 20;
	private static int CACHE_SIZE = 0;

	static synchronized void addToCache(final String name, final BufferedImageIcon bii) {
		if (CrystalImageCache.cache == null) {
			CrystalImageCache.cache = new CacheEntry[CrystalImageCache.CACHE_INCREMENT];
		}
		if (CrystalImageCache.CACHE_SIZE == CrystalImageCache.cache.length) {
			CrystalImageCache.expandCache();
		}
		CrystalImageCache.cache[CrystalImageCache.CACHE_SIZE] = new CacheEntry(bii, name);
		CrystalImageCache.CACHE_SIZE++;
	}

	private static void expandCache() {
		final var tempCache = new CacheEntry[CrystalImageCache.cache.length + CrystalImageCache.CACHE_INCREMENT];
		for (var x = 0; x < CrystalImageCache.CACHE_SIZE; x++) {
			tempCache[x] = CrystalImageCache.cache[x];
		}
		CrystalImageCache.cache = tempCache;
	}

	static BufferedImageIcon getCachedImage(final String name) {
		if (!CrystalImageCache.isInCache(name)) {
			final var bii = CrystalImageManager.getUncachedImage(name);
			CrystalImageCache.addToCache(name, bii);
		}
		for (final CacheEntry element : CrystalImageCache.cache) {
			if (name.equals(element.getName())) {
				return element.getImage();
			}
		}
		return null;
	}

	static synchronized boolean isInCache(final String name) {
		if (CrystalImageCache.cache == null) {
			CrystalImageCache.cache = new CacheEntry[CrystalImageCache.CACHE_INCREMENT];
		}
		for (var x = 0; x < CrystalImageCache.CACHE_SIZE; x++) {
			if (name.equals(CrystalImageCache.cache[x].getName())) {
				return true;
			}
		}
		return false;
	}
}