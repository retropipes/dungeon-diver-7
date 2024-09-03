/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.utility;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.dungeon.abc.GameObject;
import org.retropipes.dungeondiver7.dungeon.current.CurrentDungeon;
import org.retropipes.dungeondiver7.dungeon.objects.*;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageLoader;

public class DungeonObjects {
    // Fields
    private final GameObject[] allObjects = { new UpperGroundEmpty(), new Empty(), new UpperObjectsEmpty(),
	    new Ground(), new PartyMover(), new Ice(), new Water(), new ThinIce(), new Bridge(), new Party(1),
	    new Party(2), new Party(3), new Party(4), new Party(5), new Party(6), new Party(7), new Party(8),
	    new Party(9), new Flag(), new Wall(), new ArrowTurret(), new DeadArrowTurret(), new CrystalBlock(),
	    new Bricks(), new Tunnel(), new Mirror(), new RotaryMirror(), new Box(), new ArrowTurretMover(),
	    new TenMissiles(), new MagneticBox(), new MagneticMirror(), new MirrorCrystalBlock(), new TenStunners(),
	    new TenBoosts(), new TenMagnets(), new MagneticWall(), new FrostField(), new StairsDown(), new StairsUp(),
	    new TenInverseArrows(), new IcyBox(), new BlueDoor(), new BlueKey(), new GreenDoor(), new GreenKey(),
	    new RedDoor(), new RedKey(), new Barrel(), new ExplodingBarrel(), new Ball(), new TenDisruptors(),
	    new TenBombs(), new TenHeatBombs(), new TenIceBombs(), new WoodenWall(), new IcyWall(), new HotWall(),
	    new Lava(), new HotBox(), new MetallicBricks(), new MetallicMirror(), new MetallicRotaryMirror(),
	    new DeepWater(), new DeeperWater(), new DeepestWater(), new WoodenBox(), new IceBridge(), new PlasticBox(),
	    new MetallicBox(), new FireAllButton(), new FireAllButtonDoor(), new FirePressureButton(),
	    new FirePressureButtonDoor(), new FireTriggerButton(), new FireTriggerButtonDoor(), new IceAllButton(),
	    new IceAllButtonDoor(), new IcePressureButton(), new IcePressureButtonDoor(), new IceTriggerButton(),
	    new IceTriggerButtonDoor(), new MagneticAllButton(), new MagneticAllButtonDoor(),
	    new MagneticPressureButton(), new MagneticPressureButtonDoor(), new MagneticTriggerButton(),
	    new MagneticTriggerButtonDoor(), new MetallicAllButton(), new MetallicAllButtonDoor(),
	    new MetallicPressureButton(), new MetallicPressureButtonDoor(), new MetallicTriggerButton(),
	    new MetallicTriggerButtonDoor(), new PlasticAllButton(), new PlasticAllButtonDoor(),
	    new PlasticPressureButton(), new PlasticPressureButtonDoor(), new PlasticTriggerButton(),
	    new PlasticTriggerButtonDoor(), new StoneAllButton(), new StoneAllButtonDoor(), new StonePressureButton(),
	    new StonePressureButtonDoor(), new StoneTriggerButton(), new StoneTriggerButtonDoor(),
	    new UniversalAllButton(), new UniversalAllButtonDoor(), new UniversalPressureButton(),
	    new UniversalPressureButtonDoor(), new UniversalTriggerButton(), new UniversalTriggerButtonDoor(),
	    new WoodenAllButton(), new WoodenAllButtonDoor(), new WoodenPressureButton(),
	    new WoodenPressureButtonDoor(), new WoodenTriggerButton(), new WoodenTriggerButtonDoor(), new BoxMover(),
	    new JumpBox(), new ReverseJumpBox(), new MirrorMover(), new HotCrystalBlock(), new IcyCrystalBlock(),
	    new Cracked(), new Crumbling(), new Damaged(), new Weakened(), new Cloak(), new Darkness(), new PowerBolt(),
	    new RollingBarrelHorizontal(), new RollingBarrelVertical(), new FreezeMagic(), new KillSkull(),
	    new ArrowBelt(), new ArmorShop(), new ClosedDoor(), new HealShop(), new MonsterTile(),
	    new FinalBossMonsterTile(), new OpenDoor(), new Regenerator(), new SpellShop(), new Tile(),
	    new WeaponsShop() };

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

    public final GameObject[] getAllWithoutPrerequisiteAndNotRequired(final CurrentDungeon dungeon,
	    final int layer) {
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
	return instance.clone();
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

    public GameObject readV2(final DataIOReader reader, final int formatVersion) throws IOException {
	GameObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration1(formatVersion)
		&& !FileFormats.isFormatVersionValidGeneration2(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final GameObject allObject : this.allObjects) {
	    try {
		final GameObject instance = allObject.getClass().getConstructor().newInstance();
		if (!FileFormats.isFormatVersionValidGeneration1(formatVersion)
			&& !FileFormats.isFormatVersionValidGeneration2(formatVersion)) {
		    return null;
		}
		o = instance.readV2(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		DungeonDiver7.logError(e);
	    }
	}
	return null;
    }

    public GameObject readV3(final DataIOReader reader, final int formatVersion) throws IOException {
	GameObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration3(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final GameObject allObject : this.allObjects) {
	    try {
		final GameObject instance = allObject.getClass().getConstructor().newInstance();
		if (!FileFormats.isFormatVersionValidGeneration3(formatVersion)) {
		    return null;
		}
		o = instance.readV3(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		DungeonDiver7.logError(e);
	    }
	}
	return null;
    }

    public GameObject readV4(final DataIOReader reader, final int formatVersion) throws IOException {
	GameObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration4(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final GameObject allObject : this.allObjects) {
	    try {
		final GameObject instance = allObject.getClass().getConstructor().newInstance();
		if (!FileFormats.isFormatVersionValidGeneration4(formatVersion)) {
		    return null;
		}
		o = instance.readV4(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		DungeonDiver7.logError(e);
	    }
	}
	return null;
    }

    public GameObject readV5(final DataIOReader reader, final int formatVersion) throws IOException {
	GameObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration5(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final GameObject allObject : this.allObjects) {
	    try {
		final GameObject instance = allObject.getClass().getConstructor().newInstance();
		if (!FileFormats.isFormatVersionValidGeneration5(formatVersion)) {
		    return null;
		}
		o = instance.readV5(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		DungeonDiver7.logError(e);
	    }
	}
	return null;
    }

    public GameObject readV6(final DataIOReader reader, final int formatVersion) throws IOException {
	GameObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration6(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final GameObject allObject : this.allObjects) {
	    try {
		final GameObject instance = allObject.getClass().getConstructor().newInstance();
		if (!FileFormats.isFormatVersionValidGeneration6(formatVersion)) {
		    return null;
		}
		o = instance.readV6(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		DungeonDiver7.logError(e);
	    }
	}
	return null;
    }

    public GameObject readV7(final DataIOReader reader, final int formatVersion) throws IOException {
	GameObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration7(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final GameObject allObject : this.allObjects) {
	    try {
		final GameObject instance = allObject.getClass().getConstructor().newInstance();
		if (!FileFormats.isFormatVersionValidGeneration7(formatVersion)) {
		    return null;
		}
		o = instance.readV7(reader, UID, formatVersion);
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		DungeonDiver7.logError(e);
	    }
	}
	return null;
    }
}
