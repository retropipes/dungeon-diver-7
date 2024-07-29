package org.retropipes.dungeondiver7.gameobject;

import java.util.ResourceBundle;

import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageId;

final class GameObjectDataLoader {
    public static boolean solid(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.solid")
		.getObject(Integer.toString(index.ordinal()));
    }

    private GameObjectDataLoader() {
    }
}
