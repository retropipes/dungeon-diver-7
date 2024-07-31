package org.retropipes.dungeondiver7.gameobject;

import java.util.ResourceBundle;

import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageId;

final class GameObjectDataLoader {
    static boolean sightBlocking(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.sightblocking")
		.getObject(Integer.toString(index.ordinal()));
    }

    static boolean friction(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.friction")
		.getObject(Integer.toString(index.ordinal()));
    }

    static int height(final ObjectImageId index) {
	return (int) ResourceBundle.getBundle("asset.data.gameobject.height")
		.getObject(Integer.toString(index.ordinal()));
    }

    static int layer(final ObjectImageId index) {
	return (int) ResourceBundle.getBundle("asset.data.gameobject.layer")
		.getObject(Integer.toString(index.ordinal()));
    }

    static boolean solid(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.solid")
		.getObject(Integer.toString(index.ordinal()));
    }

    private GameObjectDataLoader() {
    }
}
