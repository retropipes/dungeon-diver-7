package org.retropipes.dungeondiver7.loader.image.battle;

import java.net.URL;

import org.retropipes.diane.asset.image.DianeImageIndex;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;

public enum BattleImageId implements DianeImageIndex {
	TEAM0, TEAM0_NOACTIONS, TEAM1, TEAM1_NOACTIONS, TEAM2, TEAM2_NOACTIONS, TEAM3, TEAM3_NOACTIONS, TEAM4,
	TEAM4_NOACTIONS, TEAM5, TEAM5_NOACTIONS, TEAM6, TEAM6_NOACTIONS, TEAM7, TEAM7_NOACTIONS, _NONE;

	@Override
	public String getName() {
		return BattleImageCatalogLoader.getFilename(this.ordinal());
	}

	@Override
	public URL getURL() {
		return this.getClass().getResource(Strings.untranslated(Untranslated.BATTLE_IMAGE_LOAD_PATH) + this.getName()
				+ Strings.fileExtension(FileExtension.IMAGE));
	}
}
