/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.utility;

import java.util.ArrayList;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.dungeondiver7.dungeon.current.CurrentDungeon;
import org.retropipes.dungeondiver7.gameobject.GameObject;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageId;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageLoader;

public class DungeonObjects {
    // Fields
    private GameObject[] allObjects;

    public DungeonObjects() {
	var objList = new ArrayList<GameObject>();
	var vals = ObjectImageId.values();
	var len = vals.length - 1;
	for (var x = 0; x < len; x++) {
	    var obj = new GameObject(vals[x]);
	    objList.add(obj);
	}
	allObjects = objList.toArray(new GameObject[objList.size()]);
    }

    public BufferedImageIcon[] getAllEditorAppearances() {
	final var allEditorAppearances = new BufferedImageIcon[this.allObjects.length];
	for (var x = 0; x < allEditorAppearances.length; x++) {
	    allEditorAppearances[x] = ObjectImageLoader.load(this.allObjects[x].getCacheName(),
		    this.allObjects[x].getIdValue());
	}
	return allEditorAppearances;
    }

    public BufferedImageIcon[] getAllEditorAppearancesOnLayer(final int layer) {
	final var tempAllEditorAppearancesOnLayer = new BufferedImageIcon[this.allObjects.length];
	var objectCount = 0;
	for (var x = 0; x < this.allObjects.length; x++) {
	    if (this.allObjects[x].getLayer() == layer) {
		tempAllEditorAppearancesOnLayer[x] = ObjectImageLoader.load(this.allObjects[x].getCacheName(),
			this.allObjects[x].getIdValue());
	    }
	}
	for (final BufferedImageIcon element : tempAllEditorAppearancesOnLayer) {
	    if (element != null) {
		objectCount++;
	    }
	}
	final var allEditorAppearancesOnLayer = new BufferedImageIcon[objectCount];
	objectCount = 0;
	for (final BufferedImageIcon element : tempAllEditorAppearancesOnLayer) {
	    if (element != null) {
		allEditorAppearancesOnLayer[objectCount] = element;
		objectCount++;
	    }
	}
	return allEditorAppearancesOnLayer;
    }

    public String[] getAllNamesOnLayer(final int layer) {
	final var tempAllNamesOnLayer = new String[this.allObjects.length];
	var objectCount = 0;
	for (var x = 0; x < this.allObjects.length; x++) {
	    if (this.allObjects[x].getLayer() == layer) {
		tempAllNamesOnLayer[x] = this.allObjects[x].getName();
	    }
	}
	for (final String element : tempAllNamesOnLayer) {
	    if (element != null) {
		objectCount++;
	    }
	}
	final var allNamesOnLayer = new String[objectCount];
	objectCount = 0;
	for (final String element : tempAllNamesOnLayer) {
	    if (element != null) {
		allNamesOnLayer[objectCount] = element;
		objectCount++;
	    }
	}
	return allNamesOnLayer;
    }

    public GameObject[] getAllObjects() {
	return this.allObjects;
    }

    public GameObject[] getAllObjectsOnLayer(final int layer) {
	final var tempAllObjectsOnLayer = new GameObject[this.allObjects.length];
	var objectCount = 0;
	for (var x = 0; x < this.allObjects.length; x++) {
	    if (this.allObjects[x].getLayer() == layer) {
		tempAllObjectsOnLayer[x] = this.allObjects[x];
	    }
	}
	for (final GameObject element : tempAllObjectsOnLayer) {
	    if (element != null) {
		objectCount++;
	    }
	}
	final var allObjectsOnLayer = new GameObject[objectCount];
	objectCount = 0;
	for (final GameObject element : tempAllObjectsOnLayer) {
	    if (element != null) {
		allObjectsOnLayer[objectCount] = element;
		objectCount++;
	    }
	}
	return allObjectsOnLayer;
    }

    public final GameObject[] getAllRequired(final CurrentDungeon dungeon, final int layer) {
	final var objects = this.getAllObjects();
	final var tempAllRequired = new GameObject[objects.length];
	int x;
	var count = 0;
	for (x = 0; x < objects.length; x++) {
	    if (objects[x].getLayer() == layer && objects[x].isRequired(dungeon)) {
		tempAllRequired[count] = objects[x];
		count++;
	    }
	}
	if (count == 0) {
	    return null;
	}
	final var allRequired = new GameObject[count];
	for (x = 0; x < count; x++) {
	    allRequired[x] = tempAllRequired[x];
	}
	return allRequired;
    }

    public final GameObject[] getAllWithoutPrerequisiteAndNotRequired(final CurrentDungeon dungeon, final int layer) {
	final var objects = this.getAllObjects();
	final var tempAllWithoutPrereq = new GameObject[objects.length];
	int x;
	var count = 0;
	for (x = 0; x < objects.length; x++) {
	    if (objects[x].getLayer() == layer && !objects[x].isRequired(dungeon)) {
		tempAllWithoutPrereq[count] = objects[x];
		count++;
	    }
	}
	if (count == 0) {
	    return null;
	}
	final var allWithoutPrereq = new GameObject[count];
	for (x = 0; x < count; x++) {
	    allWithoutPrereq[x] = tempAllWithoutPrereq[x];
	}
	return allWithoutPrereq;
    }

    public final GameObject getNewInstanceByName(final String name) {
	final var objects = this.getAllObjects();
	GameObject instance = null;
	int x;
	for (x = 0; x < objects.length; x++) {
	    if (objects[x].getCacheName().equals(name)) {
		instance = objects[x];
		break;
	    }
	}
	if (instance == null) {
	    return null;
	}
	return new GameObject(instance.getId());
    }

    public boolean[] getObjectEnabledStatuses(final int layer) {
	final var allObjectEnabledStatuses = new boolean[this.allObjects.length];
	for (var x = 0; x < this.allObjects.length; x++) {
	    if (this.allObjects[x].getLayer() == layer) {
		allObjectEnabledStatuses[x] = true;
	    } else {
		allObjectEnabledStatuses[x] = false;
	    }
	}
	return allObjectEnabledStatuses;
    }
}
