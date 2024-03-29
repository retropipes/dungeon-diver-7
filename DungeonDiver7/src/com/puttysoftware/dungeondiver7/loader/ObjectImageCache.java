/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import com.puttysoftware.diane.assets.image.BufferedImageIcon;

public class ObjectImageCache {
    // Fields
    private static CacheEntry[] cache;
    private static int CACHE_INCREMENT = 20;
    private static int CACHE_SIZE = 0;

    static BufferedImageIcon getCachedImage(final String name, final String baseName) {
        if (!ObjectImageCache.isInCache(name)) {
            final var bii = ObjectImageManager.getUncachedImage(baseName);
            ObjectImageCache.addToCache(name, bii);
        }
        for (final CacheEntry element : ObjectImageCache.cache) {
            if (name.equals(element.getName())) {
                return element.getImage();
            }
        }
        return null;
    }

    private static void expandCache() {
        final var tempCache = new CacheEntry[ObjectImageCache.cache.length + ObjectImageCache.CACHE_INCREMENT];
        for (var x = 0; x < ObjectImageCache.CACHE_SIZE; x++) {
            tempCache[x] = ObjectImageCache.cache[x];
        }
        ObjectImageCache.cache = tempCache;
    }

    static synchronized void addToCache(final String name, final BufferedImageIcon bii) {
        if (ObjectImageCache.cache == null) {
            ObjectImageCache.cache = new CacheEntry[ObjectImageCache.CACHE_INCREMENT];
        }
        if (ObjectImageCache.CACHE_SIZE == ObjectImageCache.cache.length) {
            ObjectImageCache.expandCache();
        }
        ObjectImageCache.cache[ObjectImageCache.CACHE_SIZE] = new CacheEntry(bii, name);
        ObjectImageCache.CACHE_SIZE++;
    }

    static synchronized boolean isInCache(final String name) {
        if (ObjectImageCache.cache == null) {
            ObjectImageCache.cache = new CacheEntry[ObjectImageCache.CACHE_INCREMENT];
        }
        for (var x = 0; x < ObjectImageCache.CACHE_SIZE; x++) {
            if (name.equals(ObjectImageCache.cache[x].getName())) {
                return true;
            }
        }
        return false;
    }
}