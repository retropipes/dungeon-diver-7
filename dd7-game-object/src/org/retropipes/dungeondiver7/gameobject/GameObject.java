package org.retropipes.dungeondiver7.gameobject;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.objectmodel.ObjectModel;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageId;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageLoader;

public final class GameObject {
    private static final int COUNTER_LAYER = 0;
    private static final int COUNTER_HEIGHT = 1;
    private static final int MAX_COUNTERS = 2;
    private static final int FLAG_SOLID = 0;
    private static final int FLAG_FRICTION = 1;
    private static final int FLAG_SIGHT_BLOCK = 2;
    private static final int MAX_FLAGS = 3;
    private final ObjectModel model;
    private final ObjectImageId id;
    private BufferedImageIcon image;
    private boolean lazyLoaded;

    public GameObject(final ObjectImageId oid) {
	this.id = oid;
	this.model = new ObjectModel();
	this.model.setId(oid);
	this.lazyLoaded = false;
	this.model.addCounters(MAX_COUNTERS);
	this.model.addFlags(MAX_FLAGS);
    }

    public final String getCacheName() {
	return Integer.toString(this.id.ordinal());
    }

    public final ObjectImageId getId() {
	return this.id;
    }

    public final BufferedImageIcon getImage() {
	this.lazyLoad();
	return this.image;
    }

    public final int getHeight() {
	this.lazyLoad();
	return this.model.getCounter(COUNTER_HEIGHT);
    }

    public final int getLayer() {
	this.lazyLoad();
	return this.model.getCounter(COUNTER_LAYER);
    }

    public final boolean hasFriction() {
	this.lazyLoad();
	return this.model.getFlag(FLAG_FRICTION);
    }

    public final boolean isSightBlocking() {
	this.lazyLoad();
	return this.model.getFlag(FLAG_SIGHT_BLOCK);
    }

    public final boolean isSolid() {
	this.lazyLoad();
	return this.model.getFlag(FLAG_SOLID);
    }

    private void lazyLoad() {
	if (!this.lazyLoaded) {
	    this.image = ObjectImageLoader.load(this.id);
	    this.model.setFlag(FLAG_SOLID, GameObjectDataLoader.solid(this.id));
	    this.model.setFlag(FLAG_FRICTION, GameObjectDataLoader.friction(this.id));
	    this.model.setFlag(FLAG_SIGHT_BLOCK, GameObjectDataLoader.sightBlocking(this.id));
	    this.model.setCounter(COUNTER_LAYER, GameObjectDataLoader.layer(this.id));
	    this.model.setCounter(COUNTER_HEIGHT, GameObjectDataLoader.height(this.id));
	    this.lazyLoaded = true;
	}
    }
}
