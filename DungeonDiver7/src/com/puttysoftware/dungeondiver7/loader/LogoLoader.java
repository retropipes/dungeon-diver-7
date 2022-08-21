/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.loader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.images.BufferedImageIcon;

public class LogoLoader {
    private static Class<?> LOAD_CLASS = LogoLoader.class;
    private static Font LOGO_DRAW_FONT = null;
    private static final String LOGO_DRAW_FONT_FALLBACK = "Times-BOLD-12";
    private static final int LOGO_DRAW_HORZ = 98;
    private static final int LOGO_DRAW_HORZ_MAX = 8;
    private static final int LOGO_DRAW_HORZ_PCO = 4;
    private static final int LOGO_DRAW_VERT = 76;

    private LogoLoader() {
	// Do nothing
    }

    public static BufferedImageIcon getLogo() {
	try {
	    final URL url = LogoLoader.LOAD_CLASS.getResource(Strings.untranslated(Untranslated.LOGO_IMAGE_LOAD_PATH));
	    final BufferedImage image = ImageIO.read(url);
	    if (LogoLoader.LOGO_DRAW_FONT == null) {
		try (InputStream is = LogoLoader.class
			.getResourceAsStream(Strings.untranslated(Untranslated.FONT_LOAD_PATH))) {
		    final Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
		    LogoLoader.LOGO_DRAW_FONT = baseFont.deriveFont((float) 18);
		} catch (final Exception ex) {
		    LogoLoader.LOGO_DRAW_FONT = Font.decode(LogoLoader.LOGO_DRAW_FONT_FALLBACK);
		}
	    }
	    final Graphics2D g2 = image.createGraphics();
	    g2.setFont(LogoLoader.LOGO_DRAW_FONT);
	    g2.setColor(Color.yellow);
	    final String logoVer = StuffBag.getLogoVersionString();
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

    public static BufferedImageIcon getMiniatureLogo() {
	try {
	    final URL url = LogoLoader.LOAD_CLASS
		    .getResource(Strings.untranslated(Untranslated.MINILOGO_IMAGE_LOAD_PATH));
	    final BufferedImage image = ImageIO.read(url);
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    DungeonDiver7.logWarningDirectly(ie);
	    return null;
	}
    }

    public static BufferedImageIcon getMicroLogo() {
	try {
	    final URL url = LogoLoader.LOAD_CLASS
		    .getResource(Strings.untranslated(Untranslated.MICROLOGO_IMAGE_LOAD_PATH));
	    final BufferedImage image = ImageIO.read(url);
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    DungeonDiver7.logWarningDirectly(ie);
	    return null;
	}
    }

    public static Image getIconLogo() {
	try {
	    final URL url = LogoLoader.LOAD_CLASS
		    .getResource(Strings.untranslated(Untranslated.ICONLOGO_IMAGE_LOAD_PATH));
	    final BufferedImage image = ImageIO.read(url);
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    DungeonDiver7.logWarningDirectly(ie);
	    return null;
	}
    }
}
