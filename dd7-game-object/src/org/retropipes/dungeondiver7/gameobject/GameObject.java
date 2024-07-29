package org.retropipes.dungeondiver7.gameobject;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.objectmodel.ObjectModel;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageId;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageLoader;

public final class GameObject {
    private final ObjectModel model;
    private final ObjectImageId id;
    private BufferedImageIcon image;
    private boolean lazyLoaded;
    private boolean solid;
    private boolean friction;

    public GameObject(final ObjectImageId oid) {
	this.id = oid;
	this.model = new ObjectModel();
	this.model.setId(oid);
	this.lazyLoaded = false;
	this.friction = true;
    }

    public final void addCounters(final int count) {
	this.model.addCounters(count);
    }

    public final void addFlags(final int count) {
	this.model.addFlags(count);
    }

    public final void addOneCounter() {
	this.model.addOneCounter();
    }

    public final void addOneFlag() {
	this.model.addOneFlag();
    }

    public final void decrementCounter(final int index) {
	this.model.decrementCounter(index);
    }

    public final String getCacheName() {
	return Integer.toString(this.id.ordinal());
    }

    public final int getCounter(final int index) {
	return this.model.getCounter(index);
    }

    public final boolean getFlag(final int index) {
	return this.model.getFlag(index);
    }

    public final ObjectImageId getId() {
	return this.id;
    }

    public final BufferedImageIcon getImage() {
	this.lazyLoad();
	return this.image;
    }

    public final boolean hasFriction() {
	this.lazyLoad();
	return this.friction;
    }

    public final void incrementCounter(final int index) {
	this.model.incrementCounter(index);
    }

    public final boolean isSolid() {
	this.lazyLoad();
	return this.solid;
    }

    private void lazyLoad() {
	if (!this.lazyLoaded) {
	    this.image = ObjectImageLoader.load(this.id);
	    this.solid = GameObjectDataLoader.solid(this.id);
	    this.friction = GameObjectDataLoader.friction(this.id);
	    this.lazyLoaded = true;
	}
    }

    public final int maxCounters() {
	return this.model.maxCounters();
    }

    public final int maxFlags() {
	return this.model.maxFlags();
    }

    public final void offsetCounter(final int index, final int value) {
	this.model.offsetCounter(index, value);
    }

    public final void setCounter(final int index, final int value) {
	this.model.setCounter(index, value);
    }

    public final void setFlag(final int index, final boolean value) {
	this.model.setFlag(index, value);
    }

    public final void toggleFlag(final int index) {
	this.model.toggleFlag(index);
    }
}
