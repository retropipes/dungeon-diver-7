/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.loader;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.retropipes.diane.asset.image.BufferedImageIcon;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractTunnel;
import com.puttysoftware.dungeondiver7.dungeon.objects.Tunnel;
import com.puttysoftware.dungeondiver7.locale.Colors;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;

public class ImageLoader {
	public static final int MAX_WINDOW_SIZE = 700;
	private static final Color TRANSPARENT = new Color(200, 100, 100);
	private static Class<?> LOAD_CLASS = ImageLoader.class;
	private static Font DRAW_FONT = null;
	private static final String DRAW_FONT_FALLBACK = "Times-BOLD-14";
	private static final int DRAW_HORZ = 10;
	private static final int DRAW_VERT = 22;
	private static final float DRAW_SIZE = 14;

	public static void activeLanguageChanged() {
		ImageCache.flushCache();
	}

	public static BufferedImageIcon getCompositeImage(final AbstractDungeonObject obj1,
			final AbstractDungeonObject obj2, final boolean useText) {
		final var icon1 = ImageLoader.getImage(obj1, useText);
		final var icon2 = ImageLoader.getImage(obj2, useText);
		return ImageLoader.getCompositeImageDirectly(icon1, icon2);
	}

	private static BufferedImageIcon getCompositeImageDirectly(final BufferedImageIcon icon1,
			final BufferedImageIcon icon2) {
		try {
			final var result = new BufferedImageIcon(icon1);
			if (icon1 != null && icon2 != null) {
				final var g2 = result.createGraphics();
				g2.drawImage(icon2, 0, 0, null);
				return result;
			}
			return null;
		} catch (final IllegalArgumentException ia) {
			return null;
		}
	}

	public static int getGraphicSize() {
		return 32;
	}

	public static BufferedImageIcon getImage(final AbstractDungeonObject obj, final boolean useText) {
		if (obj instanceof AbstractTunnel) {
			return ImageLoader.getTransformedTunnel(obj.getColor(), useText);
		}
		return ImageCache.getCachedImage(obj, useText);
	}

	private static BufferedImageIcon getTransformedTunnel(final Colors cc, final boolean useText) {
		try {
			final var icon = ImageCache.getCachedImage(new Tunnel(), useText);
			Color color;
			if (cc == Colors.BLUE) {
				color = Color.blue;
			} else if (cc == Colors.CYAN) {
				color = Color.cyan;
			} else if (cc == Colors.GREEN) {
				color = Color.green;
			} else if (cc == Colors.MAGENTA) {
				color = Color.magenta;
			} else if (cc == Colors.RED) {
				color = Color.red;
			} else if (cc == Colors.WHITE) {
				color = Color.white;
			} else if (cc == Colors.YELLOW) {
				color = Color.yellow;
			} else {
				color = Color.gray;
			}
			if (icon == null) {
				return null;
			}
			final var result = new BufferedImageIcon(icon);
			for (var x = 0; x < ImageLoader.getGraphicSize(); x++) {
				for (var y = 0; y < ImageLoader.getGraphicSize(); y++) {
					final var pixel = icon.getRGB(x, y);
					final var c = new Color(pixel);
					if (c.equals(ImageLoader.TRANSPARENT)) {
						result.setRGB(x, y, color.getRGB());
					}
				}
			}
			return result;
		} catch (final IllegalArgumentException ia) {
			return null;
		}
	}

	static BufferedImageIcon getUncachedImage(final AbstractDungeonObject obj, final boolean useText) {
		try {
			String name, extraPath;
			if (obj.isOfType(DungeonObjectTypes.TYPE_TUNNEL)) {
				name = obj.getBaseImageName();
			} else {
				name = obj.getImageName();
			}
			if (obj.isEnabled()) {
				extraPath = "enabled/";
			} else {
				extraPath = "disabled/";
			}
			final var normalName = ImageLoader.normalizeName(name);
			final var url = ImageLoader.LOAD_CLASS.getResource(Strings.untranslated(Untranslated.OBJECT_IMAGE_LOAD_PATH)
					+ extraPath + normalName + Strings.fileExtension(FileExtension.IMAGE));
			final var image = ImageIO.read(url);
			final var customText = obj.getCustomText();
			if (useText && customText != null) {
				if (ImageLoader.DRAW_FONT == null) {
					try (var is = ImageLoader.class
							.getResourceAsStream(Strings.untranslated(Untranslated.FONT_LOAD_PATH))) {
						final var baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
						ImageLoader.DRAW_FONT = baseFont.deriveFont(ImageLoader.DRAW_SIZE);
					} catch (final Exception ex) {
						ImageLoader.DRAW_FONT = Font.decode(ImageLoader.DRAW_FONT_FALLBACK);
					}
				}
				final var g2 = image.createGraphics();
				g2.setFont(ImageLoader.DRAW_FONT);
				g2.setColor(obj.getCustomTextColor());
				g2.drawString(customText, ImageLoader.DRAW_HORZ, ImageLoader.DRAW_VERT);
			}
			return new BufferedImageIcon(image);
		} catch (final IOException | NullPointerException | IllegalArgumentException ia) {
			return null;
		}
	}

	public static BufferedImageIcon getVirtualCompositeImage(final AbstractDungeonObject obj1,
			final AbstractDungeonObject obj2, final AbstractDungeonObject... otherObjs) {
		var result = ImageLoader.getCompositeImage(obj1, obj2, true);
		for (final AbstractDungeonObject otherObj : otherObjs) {
			final var img = ImageLoader.getImage(otherObj, true);
			result = ImageLoader.getCompositeImageDirectly(result, img);
		}
		return result;
	}

	private static String normalizeName(final String name) {
		final var sb = new StringBuilder(name);
		for (var x = 0; x < sb.length(); x++) {
			if (!Character.isLetter(sb.charAt(x)) && !Character.isDigit(sb.charAt(x))) {
				sb.setCharAt(x, '_');
			}
		}
		return sb.toString().toLowerCase();
	}

	private ImageLoader() {
		// Do nothing
	}
}
