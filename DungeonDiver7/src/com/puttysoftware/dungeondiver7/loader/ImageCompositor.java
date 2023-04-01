/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import java.awt.Color;

import com.puttysoftware.diane.assets.image.BufferedImageIcon;

public class ImageCompositor {
	public static final int MAX_WINDOW_SIZE = 700;
	private static final int TRANSPARENT = 0;

	public static BufferedImageIcon getCompositeImage(final BufferedImageIcon icon1, final BufferedImageIcon icon2,
			final int imageSize) {
		final var result = new BufferedImageIcon(icon2);
		if (icon1 == null || icon2 == null) {
			return null;
		}
		for (var x = 0; x < imageSize; x++) {
			for (var y = 0; y < imageSize; y++) {
				final var pixel = icon2.getRGB(x, y);
				final var c = new Color(pixel, true);
				if (c.getAlpha() == ImageCompositor.TRANSPARENT) {
					result.setRGB(x, y, icon1.getRGB(x, y));
				}
			}
		}
		return result;
	}

	public static BufferedImageIcon getVirtualCompositeImage(final BufferedImageIcon icon1,
			final BufferedImageIcon icon2, final BufferedImageIcon icon3, final int imageSize) {
		final var icon4 = ImageCompositor.getCompositeImage(icon1, icon2, imageSize);
		final var result = new BufferedImageIcon(icon3);
		if (icon3 == null || icon4 == null) {
			return null;
		}
		for (var x = 0; x < imageSize; x++) {
			for (var y = 0; y < imageSize; y++) {
				final var pixel = icon3.getRGB(x, y);
				final var c = new Color(pixel, true);
				if (c.getAlpha() == ImageCompositor.TRANSPARENT) {
					result.setRGB(x, y, icon4.getRGB(x, y));
				}
			}
		}
		return result;
	}

	public static String normalizeName(final String name) {
		final var sb = new StringBuilder(name);
		for (var x = 0; x < sb.length(); x++) {
			if (!Character.isLetter(sb.charAt(x)) && !Character.isDigit(sb.charAt(x))) {
				sb.setCharAt(x, '_');
			}
		}
		return sb.toString().toLowerCase();
	}

	public static int getGraphicSize() {
		return 64;
	}
}
