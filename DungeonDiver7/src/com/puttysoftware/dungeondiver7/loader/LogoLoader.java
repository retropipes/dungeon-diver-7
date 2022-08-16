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

import com.puttysoftware.dungeondiver7.StuffBag;
import com.puttysoftware.dungeondiver7.locale.old.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;
import com.puttysoftware.images.BufferedImageIcon;

public class LogoLoader {
    private static final String DEFAULT_LOAD_PATH = LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
	    LocaleConstants.NOTL_STRING_GRAPHICS_PATH);
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

    static BufferedImageIcon getUncachedLogo(final String name, final boolean drawing) {
	try {
	    final URL url = LogoLoader.LOAD_CLASS.getResource(LogoLoader.DEFAULT_LOAD_PATH
		    + LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
			    LocaleConstants.NOTL_STRING_LOGO_SUBPATH)
		    + name + LocaleConstants.COMMON_STRING_NOTL_IMAGE_EXTENSION_PNG);
	    final BufferedImage image = ImageIO.read(url);
	    if (drawing) {
		if (LogoLoader.LOGO_DRAW_FONT == null) {
		    try (InputStream is = LogoLoader.class.getResourceAsStream(LocaleLoader
			    .loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_FONT_PATH)
			    + LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
				    LocaleConstants.NOTL_STRING_FONT_FILENAME))) {
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
	    }
	    return new BufferedImageIcon(image);
	} catch (final IOException ie) {
	    return null;
	} catch (final NullPointerException np) {
	    return null;
	} catch (final IllegalArgumentException ia) {
	    return null;
	}
    }

    public static BufferedImageIcon getLogo() {
	return LogoCache.getCachedLogo(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_LOGO), true);
    }

    public static BufferedImageIcon getMiniatureLogo() {
	return LogoCache.getCachedLogo(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_MINILOGO),
		false);
    }

    public static BufferedImageIcon getMicroLogo() {
	return LogoCache.getCachedLogo(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_MICROLOGO),
		false);
    }

    public static Image getIconLogo() {
	return LogoCache.getCachedLogo(
		LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_ICONLOGO),
		false);
    }
}
