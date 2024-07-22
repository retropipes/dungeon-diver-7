package org.retropipes.dungeondiver7.loader.image.ui;

import java.net.URL;

import org.retropipes.diane.asset.image.DianeImageIndex;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;

public enum UiImageId implements DianeImageIndex {
	ICONLOGO, LOGO, MICROLOGO, MINILOGO, _NONE;

	@Override
	public String getName() {
		return UiImageCatalogLoader.getFilename(this.ordinal());
	}

	@Override
	public URL getURL() {
		return this.getClass().getResource(Strings.untranslated(Untranslated.UI_IMAGE_LOAD_PATH) + this.getName()
				+ Strings.fileExtension(FileExtension.IMAGE));
	}
}
