package org.retropipes.dungeondiver7.gameobject;

import java.util.ResourceBundle;

import org.retropipes.diane.direction.Direction;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageId;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.locale.Colors;

final class GameObjectDataLoader {
    static ObjectImageId bound(final ObjectImageId index) {
	var maxOid = ObjectImageId.values().length;
	var oid = (int) ResourceBundle.getBundle("asset.data.gameobject.bound")
		.getObject(Integer.toString(index.ordinal()));
	if (oid < 0 || oid > maxOid) {
	    return null;
	}
	return ObjectImageId.values()[oid];
    }

    static Colors color(final ObjectImageId index) {
	var maxCid = Colors.values().length;
	var cid = (int) ResourceBundle.getBundle("asset.data.gameobject.color")
		.getObject(Integer.toString(index.ordinal()));
	if (cid < 0 || cid > maxCid) {
	    return Colors._NONE;
	}
	return Colors.values()[cid];
    }

    static int damage(final ObjectImageId index) {
	return (int) ResourceBundle.getBundle("asset.data.gameobject.damaging")
		.getObject(Integer.toString(index.ordinal()));
    }

    static boolean deferSetProperties(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.defer_set")
		.getObject(Integer.toString(index.ordinal()));
    }

    static Direction direction(final ObjectImageId index) {
	var maxDid = Direction.values().length;
	var did = (int) ResourceBundle.getBundle("asset.data.gameobject.direction")
		.getObject(Integer.toString(index.ordinal()));
	if (did < 0 || did > maxDid) {
	    return Direction.NONE;
	}
	return Direction.values()[did];
    }

    static boolean isField(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.field")
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

    static int initialTimer(final ObjectImageId index) {
	return (int) ResourceBundle.getBundle("asset.data.gameobject.initial_timer")
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
	var maxOid = ObjectImageId.values().length;
	var oid = (int) ResourceBundle.getBundle("asset.data.gameobject.interact_morph")
		.getObject(Integer.toString(index.ordinal()));
	if (oid < 0 || oid > maxOid) {
	    return null;
	}
	return ObjectImageId.values()[oid];
    }

    static int interactionMessageIndex(final ObjectImageId index) {
	return (int) ResourceBundle.getBundle("asset.data.gameobject.interact_message")
		.getObject(Integer.toString(index.ordinal()));
    }

    static Sounds interactionSound(final ObjectImageId index) {
	return Sounds.values()[((int) ResourceBundle.getBundle("asset.data.gameobject.interact_sound")
		.getObject(Integer.toString(index.ordinal())))];
    }

    static boolean isPassThrough(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.pass_through")
		.getObject(Integer.toString(index.ordinal()));
    }

    static boolean killsOnMove(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.move_kill")
		.getObject(Integer.toString(index.ordinal()));
    }

    static int layer(final ObjectImageId index) {
	return (int) ResourceBundle.getBundle("asset.data.gameobject.layer")
		.getObject(Integer.toString(index.ordinal()));
    }

    static Material material(final ObjectImageId index) {
	return Material.values()[((int) ResourceBundle.getBundle("asset.data.gameobject.material")
		.getObject(Integer.toString(index.ordinal())))];
    }

    static int maxFrame(final ObjectImageId index) {
	return (int) ResourceBundle.getBundle("asset.data.gameobject.max_frame")
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
	var maxSid = ShopType.values().length;
	var sid = (int) ResourceBundle.getBundle("asset.data.gameobject.shop")
		.getObject(Integer.toString(index.ordinal()));
	if (sid < 0 || sid > maxSid) {
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

    static boolean solvesOnMove(final ObjectImageId index) {
	return (boolean) ResourceBundle.getBundle("asset.data.gameobject.move_solve")
		.getObject(Integer.toString(index.ordinal()));
    }

    private GameObjectDataLoader() {
    }
}
