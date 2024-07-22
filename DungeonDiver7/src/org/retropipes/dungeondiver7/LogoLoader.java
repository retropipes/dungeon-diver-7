/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.dungeondiver7.loader.image.ui.UiImageId;
import org.retropipes.dungeondiver7.loader.image.ui.UiImageLoader;

public class LogoLoader {

	public static BufferedImageIcon getIconLogo() {
		return UiImageLoader.load(UiImageId.ICONLOGO);
	}

	public static BufferedImageIcon getLogo() {
		return UiImageLoader.load(UiImageId.LOGO);
	}

	public static BufferedImageIcon getMicroLogo() {
		return UiImageLoader.load(UiImageId.MICROLOGO);
	}

	public static BufferedImageIcon getMiniatureLogo() {
		return UiImageLoader.load(UiImageId.MINILOGO);
	}

	private LogoLoader() {
		// Do nothing
	}
}
