package org.retropipes.dungeondiver7.loader.image.halo;

import java.net.URL;

import org.retropipes.diane.asset.image.DianeImageIndex;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;

public enum HaloImageId implements DianeImageIndex {
	BLESSED,
	BLESSMORE,
	BLESSMOST,
	CURSED,
	CURSEMORE,
	CURSEMOST,
	_NONE;

	@Override
	public String getName() {
		return HaloImageCatalogLoader.getFilename(this.ordinal());
	}

	@Override
	public URL getURL() {
		return this.getClass().getResource(Strings.untranslated(Untranslated.HALO_IMAGE_LOAD_PATH) + this.getName()
				+ Strings.fileExtension(FileExtension.IMAGE));
	}
}
