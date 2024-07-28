package org.retropipes.dungeondiver7.loader.image.gameobject;

import java.net.URL;

import org.retropipes.diane.asset.image.DianeImageIndex;
import org.retropipes.diane.objectmodel.ObjectId;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.locale.Untranslated;

public enum ObjectImageId implements DianeImageIndex, ObjectId {
	EMPTY, WALL, DARKNESS, BOUNDS, PARTY, NOTE, NOTHING, EASY_MONSTER, NORMAL_MONSTER, HARD_MONSTER, TILE, ARMOR_SHOP,
	BANK, HEALER, ITEM_SHOP, SPELL_SHOP, SURGE_SHOP, WEAPON_SHOP, STAIRS_DOWN, STAIRS_UP, GRASS, TREE, CUT_TREE, AXE,
	DIRT, WATER, DEEP_WATER, DEEPER_WATER, DEEPEST_WATER, BOOTS, _NONE;

	@Override
	public String getName() {
		return Integer.toString(this.ordinal());
	}

	@Override
	public URL getURL() {
		return this.getClass().getResource(Strings.untranslated(Untranslated.OBJECT_IMAGE_LOAD_PATH) + this.getName()
				+ Strings.fileExtension(FileExtension.IMAGE));
	}
}
