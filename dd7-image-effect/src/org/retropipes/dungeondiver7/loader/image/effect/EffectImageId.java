package org.retropipes.dungeondiver7.loader.image.effect;

import java.net.URL;

import org.retropipes.diane.asset.image.DianeImageIndex;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;

public enum EffectImageId implements DianeImageIndex {
	BLIND, CLAIRVOYANT, FAST, FASTER, FASTEST, FORTIFIED, GATHERING, LEARNING, POISONED, POOR, REGENERATING, RESISTANT,
	RICH, SLOW, SLOWER, SLOWEST, SPEED, STRONG, SUSCEPTIBLE, VULNERABLE, WEAK, WITHERING, YEARNING, _NONE;

	@Override
	public String getName() {
		return EffectImageCatalogLoader.getFilename(this.ordinal());
	}

	@Override
	public URL getURL() {
		return this.getClass().getResource(Strings.untranslated(Untranslated.EFFECT_IMAGE_LOAD_PATH) + this.getName()
				+ Strings.fileExtension(FileExtension.IMAGE));
	}
}
