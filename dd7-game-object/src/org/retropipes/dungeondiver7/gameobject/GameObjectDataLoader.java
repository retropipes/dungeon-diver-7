package org.retropipes.dungeondiver7.gameobject;

import java.util.ResourceBundle;

import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageId;
import org.retropipes.dungeondiver7.loader.sound.Sounds;

final class GameObjectDataLoader {
    static int damage(final ObjectImageId index) {
	return (int) ResourceBundle.getBundle("asset.data.gameobject.damaging")
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

    static boolean isInteractive(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.interact")
		.getObject(Integer.toString(index.ordinal()));
    }

    static boolean isMoving(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.moving")
		.getObject(Integer.toString(index.ordinal()));
    }

    static boolean isPlayer(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.player")
		.getObject(Integer.toString(index.ordinal()));
    }

    static ObjectImageId interactionMorph(final ObjectImageId index) {
	var mid = (int) ResourceBundle.getBundle("asset.data.gameobject.interact_sound")
		.getObject(Integer.toString(index.ordinal()));
	if (mid < 0 || mid > 250) {
	    return index;
	}
	return ObjectImageId.values()[mid];
    }

    static int interactionMessageIndex(final ObjectImageId index) {
	return (int) ResourceBundle.getBundle("asset.data.gameobject.interact_message")
		.getObject(Integer.toString(index.ordinal()));
    }

    static Sounds interactionSound(final ObjectImageId index) {
	return Sounds.values()[((int) ResourceBundle.getBundle("asset.data.gameobject.interact_sound")
		.getObject(Integer.toString(index.ordinal())))];
    }

    static int layer(final ObjectImageId index) {
	return (int) ResourceBundle.getBundle("asset.data.gameobject.layer")
		.getObject(Integer.toString(index.ordinal()));
    }

    static boolean pullable(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.pullable")
		.getObject(Integer.toString(index.ordinal()));
    }

    static boolean pushable(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.pushable")
		.getObject(Integer.toString(index.ordinal()));
    }

    static ShopType shopType(final ObjectImageId index) {
	var sid = (int) ResourceBundle.getBundle("asset.data.gameobject.shop")
		.getObject(Integer.toString(index.ordinal()));
	if (sid < 0 || sid > 6) {
	    return null;
	}
	return ShopType.values()[sid];
    }

    static boolean sightBlocking(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.sightblocking")
		.getObject(Integer.toString(index.ordinal()));
    }

    static boolean solid(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.solid")
		.getObject(Integer.toString(index.ordinal()));
    }

    private GameObjectDataLoader() {
    }
}
