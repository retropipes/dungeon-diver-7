/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.dungeon.utility;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.objects.ArmorShop;
import com.puttysoftware.dungeondiver7.dungeon.objects.BossMonsterTile;
import com.puttysoftware.dungeondiver7.dungeon.objects.ClosedDoor;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.dungeon.objects.FinalBossMonsterTile;
import com.puttysoftware.dungeondiver7.dungeon.objects.HealShop;
import com.puttysoftware.dungeondiver7.dungeon.objects.Ice;
import com.puttysoftware.dungeondiver7.dungeon.objects.MonsterTile;
import com.puttysoftware.dungeondiver7.dungeon.objects.OpenDoor;
import com.puttysoftware.dungeondiver7.dungeon.objects.Regenerator;
import com.puttysoftware.dungeondiver7.dungeon.objects.SpellShop;
import com.puttysoftware.dungeondiver7.dungeon.objects.Tile;
import com.puttysoftware.dungeondiver7.dungeon.objects.Wall;
import com.puttysoftware.dungeondiver7.dungeon.objects.WeaponsShop;
import com.puttysoftware.dungeondiver7.integration1.dungeon.CurrentDungeon;
import com.puttysoftware.dungeondiver7.manager.dungeon.FormatConstants;
import com.puttysoftware.fileio.FileIOReader;

public class GameObjectList {
    // Fields
    private final ArrayList<AbstractDungeonObject> allObjectList;

    // Constructor
    public GameObjectList() {
	final AbstractDungeonObject[] allObjects = { new ArmorShop(), new ClosedDoor(), new Empty(), new HealShop(),
		new Ice(), new MonsterTile(), new BossMonsterTile(), new FinalBossMonsterTile(), new OpenDoor(),
		new Regenerator(), new SpellShop(), new Tile(), new Wall(), new WeaponsShop() };
	this.allObjectList = new ArrayList<>();
	// Add all predefined objects to the list
	for (final AbstractDungeonObject allObject : allObjects) {
	    this.allObjectList.add(allObject);
	}
    }

    // Methods
    public AbstractDungeonObject[] getAllObjects() {
	return this.allObjectList.toArray(new AbstractDungeonObject[this.allObjectList.size()]);
    }

    public String[] getAllDescriptions() {
	final AbstractDungeonObject[] objects = this.getAllObjects();
	final String[] allDescriptions = new String[objects.length];
	for (int x = 0; x < objects.length; x++) {
	    allDescriptions[x] = objects[x].getDescription();
	}
	return allDescriptions;
    }

    public final AbstractDungeonObject[] getAllRequired(final CurrentDungeon dungeon, final int layer) {
	final AbstractDungeonObject[] objects = this.getAllObjects();
	final AbstractDungeonObject[] tempAllRequired = new AbstractDungeonObject[objects.length];
	int x;
	int count = 0;
	for (x = 0; x < objects.length; x++) {
	    if (objects[x].getLayer() == layer && objects[x].isRequired(dungeon)) {
		tempAllRequired[count] = objects[x];
		count++;
	    }
	}
	if (count == 0) {
	    return null;
	} else {
	    final AbstractDungeonObject[] allRequired = new AbstractDungeonObject[count];
	    for (x = 0; x < count; x++) {
		allRequired[x] = tempAllRequired[x];
	    }
	    return allRequired;
	}
    }

    public final AbstractDungeonObject[] getAllWithoutPrerequisiteAndNotRequired(final CurrentDungeon dungeon, final int layer) {
	final AbstractDungeonObject[] objects = this.getAllObjects();
	final AbstractDungeonObject[] tempAllWithoutPrereq = new AbstractDungeonObject[objects.length];
	int x;
	int count = 0;
	for (x = 0; x < objects.length; x++) {
	    if (objects[x].getLayer() == layer && !objects[x].isRequired(dungeon)) {
		tempAllWithoutPrereq[count] = objects[x];
		count++;
	    }
	}
	if (count == 0) {
	    return null;
	} else {
	    final AbstractDungeonObject[] allWithoutPrereq = new AbstractDungeonObject[count];
	    for (x = 0; x < count; x++) {
		allWithoutPrereq[x] = tempAllWithoutPrereq[x];
	    }
	    return allWithoutPrereq;
	}
    }

    public final AbstractDungeonObject getNewInstanceByName(final String name) {
	final AbstractDungeonObject[] objects = this.getAllObjects();
	AbstractDungeonObject instance = null;
	int x;
	for (x = 0; x < objects.length; x++) {
	    if (objects[x].getName().equals(name)) {
		instance = objects[x];
		break;
	    }
	}
	if (instance == null) {
	    return null;
	} else {
	    return instance.clone();
	}
    }

    public AbstractDungeonObject read(final FileIOReader reader, final int formatVersion) throws IOException {
	final AbstractDungeonObject[] objects = this.getAllObjects();
	AbstractDungeonObject o = null;
	String UID = "";
	if (formatVersion == FormatConstants.MAZE_FORMAT_LATEST) {
	    UID = reader.readString();
	}
	for (final AbstractDungeonObject object : objects) {
	    AbstractDungeonObject instance;
	    instance = object.clone();
	    if (formatVersion == FormatConstants.MAZE_FORMAT_LATEST) {
		o = instance.readV7(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    }
	}
	return null;
    }

    public AbstractDungeonObject readSavedGameObject(final FileIOReader reader, final String UID, final int formatVersion)
	    throws IOException {
	final AbstractDungeonObject[] objects = this.getAllObjects();
	AbstractDungeonObject o = null;
	for (final AbstractDungeonObject object : objects) {
	    AbstractDungeonObject instance;
	    instance = object.clone();
	    if (formatVersion == FormatConstants.MAZE_FORMAT_LATEST) {
		o = instance.readV7(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    }
	}
	return null;
    }
}
