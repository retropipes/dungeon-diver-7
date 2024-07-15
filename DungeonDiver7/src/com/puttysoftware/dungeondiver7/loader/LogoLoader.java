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

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;

public class LogoLoader {
	private static Class<?> LOAD_CLASS = LogoLoader.class;
	private static Font LOGO_DRAW_FONT = null;
	private static final String LOGO_DRAW_FONT_FALLBACK = "Times-BOLD-12";
	private static final int LOGO_DRAW_HORZ = 98;
	private static final int LOGO_DRAW_HORZ_MAX = 8;
	private static final int LOGO_DRAW_HORZ_PCO = 4;
	private static final int LOGO_DRAW_VERT = 76;

	public static BufferedImageIcon getIconLogo() {
		try {
			final var url = LogoLoader.LOAD_CLASS
					.getResource(Strings.untranslated(Untranslated.ICONLOGO_IMAGE_LOAD_PATH));
			final var image = ImageIO.read(url);
			return new BufferedImageIcon(image);
		} catch (final IOException ie) {
			DungeonDiver7.logWarningDirectly(ie);
			return null;
		}
	}

	public static BufferedImageIcon getLogo() {
		try {
			final var url = LogoLoader.LOAD_CLASS.getResource(Strings.untranslated(Untranslated.LOGO_IMAGE_LOAD_PATH));
			final var image = ImageIO.read(url);
			if (LogoLoader.LOGO_DRAW_FONT == null) {
				try (var is = LogoLoader.class.getResourceAsStream(Strings.untranslated(Untranslated.FONT_LOAD_PATH))) {
					final var baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
					LogoLoader.LOGO_DRAW_FONT = baseFont.deriveFont((float) 18);
				} catch (final Exception ex) {
					LogoLoader.LOGO_DRAW_FONT = Font.decode(LogoLoader.LOGO_DRAW_FONT_FALLBACK);
				}
			}
			final var g2 = image.createGraphics();
			g2.setFont(LogoLoader.LOGO_DRAW_FONT);
			g2.setColor(Color.yellow);
			final var logoVer = StuffBag.getLogoVersionString();
			g2.drawString(logoVer,
					LogoLoader.LOGO_DRAW_HORZ
							+ (LogoLoader.LOGO_DRAW_HORZ_MAX - logoVer.length()) * LogoLoader.LOGO_DRAW_HORZ_PCO,
					LogoLoader.LOGO_DRAW_VERT);
			return new BufferedImageIcon(image);
		} catch (final IOException ie) {
			DungeonDiver7.logWarningDirectly(ie);
			return null;
		}
	}

	public static BufferedImageIcon getMicroLogo() {
		try {
			final var url = LogoLoader.LOAD_CLASS
					.getResource(Strings.untranslated(Untranslated.MICROLOGO_IMAGE_LOAD_PATH));
			final var image = ImageIO.read(url);
			return new BufferedImageIcon(image);
		} catch (final IOException ie) {
			DungeonDiver7.logWarningDirectly(ie);
			return null;
		}
	}

	public static BufferedImageIcon getMiniatureLogo() {
		try {
			final var url = LogoLoader.LOAD_CLASS
					.getResource(Strings.untranslated(Untranslated.MINILOGO_IMAGE_LOAD_PATH));
			final var image = ImageIO.read(url);
			return new BufferedImageIcon(image);
		} catch (final IOException ie) {
			DungeonDiver7.logWarningDirectly(ie);
			return null;
		}
	}

	private LogoLoader() {
		// Do nothing
	}
}
