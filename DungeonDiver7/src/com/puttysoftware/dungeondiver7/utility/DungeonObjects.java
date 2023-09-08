/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.puttysoftware.diane.assets.image.BufferedImageIcon;
import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.current.CurrentDungeon;
import com.puttysoftware.dungeondiver7.dungeon.objects.*;
import com.puttysoftware.dungeondiver7.loader.ImageLoader;

public class DungeonObjects {
    // Fields
    private final AbstractDungeonObject[] allObjects = { new UpperGroundEmpty(), new Empty(), new UpperObjectsEmpty(),
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
	    new BossMonsterTile(), new FinalBossMonsterTile(), new OpenDoor(), new Regenerator(), new SpellShop(),
	    new Tile(), new WeaponsShop() };

    public void enableAllObjects() {
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    allObject.setEnabled(true);
	}
    }

    public String[] getAllDescriptions() {
	final var allDescriptions = new String[this.allObjects.length];
	for (var x = 0; x < this.allObjects.length; x++) {
	    allDescriptions[x] = this.allObjects[x].getDescription();
	}
	return allDescriptions;
    }

    public BufferedImageIcon[] getAllEditorAppearances() {
	final var allEditorAppearances = new BufferedImageIcon[this.allObjects.length];
	for (var x = 0; x < allEditorAppearances.length; x++) {
	    allEditorAppearances[x] = ImageLoader.getImage(this.allObjects[x], false);
	}
	return allEditorAppearances;
    }

    public BufferedImageIcon[] getAllEditorAppearancesOnLayer(final int layer, final boolean useDisable) {
	if (useDisable) {
	    final var allEditorAppearancesOnLayer = new BufferedImageIcon[this.allObjects.length];
	    for (var x = 0; x < this.allObjects.length; x++) {
		if (this.allObjects[x].getLayer() == layer) {
		    this.allObjects[x].setEnabled(true);
		} else {
		    this.allObjects[x].setEnabled(false);
		}
		allEditorAppearancesOnLayer[x] = ImageLoader.getImage(this.allObjects[x], false);
	    }
	    return allEditorAppearancesOnLayer;
	}
	final var tempAllEditorAppearancesOnLayer = new BufferedImageIcon[this.allObjects.length];
	var objectCount = 0;
	for (var x = 0; x < this.allObjects.length; x++) {
	    if (this.allObjects[x].getLayer() == layer) {
		tempAllEditorAppearancesOnLayer[x] = ImageLoader.getImage(this.allObjects[x], false);
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
		tempAllNamesOnLayer[x] = this.allObjects[x].getBaseName();
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

    public AbstractDungeonObject[] getAllObjects() {
	return this.allObjects;
    }

    public AbstractDungeonObject[] getAllObjectsOnLayer(final int layer, final boolean useDisable) {
	if (useDisable) {
	    for (final AbstractDungeonObject allObject : this.allObjects) {
		if (allObject.getLayer() == layer) {
		    allObject.setEnabled(true);
		} else {
		    allObject.setEnabled(false);
		}
	    }
	    return this.allObjects;
	}
	final var tempAllObjectsOnLayer = new AbstractDungeonObject[this.allObjects.length];
	var objectCount = 0;
	for (var x = 0; x < this.allObjects.length; x++) {
	    if (this.allObjects[x].getLayer() == layer) {
		tempAllObjectsOnLayer[x] = this.allObjects[x];
	    }
	}
	for (final AbstractDungeonObject element : tempAllObjectsOnLayer) {
	    if (element != null) {
		objectCount++;
	    }
	}
	final var allObjectsOnLayer = new AbstractDungeonObject[objectCount];
	objectCount = 0;
	for (final AbstractDungeonObject element : tempAllObjectsOnLayer) {
	    if (element != null) {
		allObjectsOnLayer[objectCount] = element;
		objectCount++;
	    }
	}
	return allObjectsOnLayer;
    }

    public final AbstractDungeonObject[] getAllRequired(final CurrentDungeon dungeon, final int layer) {
	final var objects = this.getAllObjects();
	final var tempAllRequired = new AbstractDungeonObject[objects.length];
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
	final var allRequired = new AbstractDungeonObject[count];
	for (x = 0; x < count; x++) {
	    allRequired[x] = tempAllRequired[x];
	}
	return allRequired;
    }

    public final AbstractDungeonObject[] getAllWithoutPrerequisiteAndNotRequired(final CurrentDungeon dungeon,
	    final int layer) {
	final var objects = this.getAllObjects();
	final var tempAllWithoutPrereq = new AbstractDungeonObject[objects.length];
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
	final var allWithoutPrereq = new AbstractDungeonObject[count];
	for (x = 0; x < count; x++) {
	    allWithoutPrereq[x] = tempAllWithoutPrereq[x];
	}
	return allWithoutPrereq;
    }

    public final AbstractDungeonObject getNewInstanceByName(final String name) {
	final var objects = this.getAllObjects();
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

    public AbstractDungeonObject readV2(final DataIOReader reader, final int formatVersion) throws IOException {
	AbstractDungeonObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration1(formatVersion)
		&& !FileFormats.isFormatVersionValidGeneration2(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    try {
		final AbstractDungeonObject instance = allObject.getClass().getConstructor().newInstance();
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

    public AbstractDungeonObject readV3(final DataIOReader reader, final int formatVersion) throws IOException {
	AbstractDungeonObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration3(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    try {
		final AbstractDungeonObject instance = allObject.getClass().getConstructor().newInstance();
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

    public AbstractDungeonObject readV4(final DataIOReader reader, final int formatVersion) throws IOException {
	AbstractDungeonObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration4(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    try {
		final AbstractDungeonObject instance = allObject.getClass().getConstructor().newInstance();
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

    public AbstractDungeonObject readV5(final DataIOReader reader, final int formatVersion) throws IOException {
	AbstractDungeonObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration5(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    try {
		final AbstractDungeonObject instance = allObject.getClass().getConstructor().newInstance();
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

    public AbstractDungeonObject readV6(final DataIOReader reader, final int formatVersion) throws IOException {
	AbstractDungeonObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration6(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    try {
		final AbstractDungeonObject instance = allObject.getClass().getConstructor().newInstance();
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

    public AbstractDungeonObject readV7(final DataIOReader reader, final int formatVersion) throws IOException {
	AbstractDungeonObject o = null;
	if (!FileFormats.isFormatVersionValidGeneration7(formatVersion)) {
	    return null;
	}
	final var UID = reader.readString();
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    try {
		final AbstractDungeonObject instance = allObject.getClass().getConstructor().newInstance();
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
