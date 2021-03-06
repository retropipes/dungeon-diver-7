/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utilities;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.abstractobjects.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.objects.*;
import com.puttysoftware.dungeondiver7.loaders.ImageLoader;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.xio.XDataReader;

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
	    new ArrowBelt() };

    public String[] getAllDescriptions() {
	final String[] allDescriptions = new String[this.allObjects.length];
	for (int x = 0; x < this.allObjects.length; x++) {
	    allDescriptions[x] = this.allObjects[x].getDescription();
	}
	return allDescriptions;
    }

    public BufferedImageIcon[] getAllEditorAppearances() {
	final BufferedImageIcon[] allEditorAppearances = new BufferedImageIcon[this.allObjects.length];
	for (int x = 0; x < allEditorAppearances.length; x++) {
	    allEditorAppearances[x] = ImageLoader.getImage(this.allObjects[x], false);
	}
	return allEditorAppearances;
    }

    public void enableAllObjects() {
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    allObject.setEnabled(true);
	}
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
	} else {
	    final AbstractDungeonObject[] tempAllObjectsOnLayer = new AbstractDungeonObject[this.allObjects.length];
	    int objectCount = 0;
	    for (int x = 0; x < this.allObjects.length; x++) {
		if (this.allObjects[x].getLayer() == layer) {
		    tempAllObjectsOnLayer[x] = this.allObjects[x];
		}
	    }
	    for (final AbstractDungeonObject element : tempAllObjectsOnLayer) {
		if (element != null) {
		    objectCount++;
		}
	    }
	    final AbstractDungeonObject[] allObjectsOnLayer = new AbstractDungeonObject[objectCount];
	    objectCount = 0;
	    for (final AbstractDungeonObject element : tempAllObjectsOnLayer) {
		if (element != null) {
		    allObjectsOnLayer[objectCount] = element;
		    objectCount++;
		}
	    }
	    return allObjectsOnLayer;
	}
    }

    public String[] getAllNamesOnLayer(final int layer) {
	final String[] tempAllNamesOnLayer = new String[this.allObjects.length];
	int objectCount = 0;
	for (int x = 0; x < this.allObjects.length; x++) {
	    if (this.allObjects[x].getLayer() == layer) {
		tempAllNamesOnLayer[x] = this.allObjects[x].getBaseName();
	    }
	}
	for (final String element : tempAllNamesOnLayer) {
	    if (element != null) {
		objectCount++;
	    }
	}
	final String[] allNamesOnLayer = new String[objectCount];
	objectCount = 0;
	for (final String element : tempAllNamesOnLayer) {
	    if (element != null) {
		allNamesOnLayer[objectCount] = element;
		objectCount++;
	    }
	}
	return allNamesOnLayer;
    }

    public boolean[] getObjectEnabledStatuses(final int layer) {
	final boolean[] allObjectEnabledStatuses = new boolean[this.allObjects.length];
	for (int x = 0; x < this.allObjects.length; x++) {
	    if (this.allObjects[x].getLayer() == layer) {
		allObjectEnabledStatuses[x] = true;
	    } else {
		allObjectEnabledStatuses[x] = false;
	    }
	}
	return allObjectEnabledStatuses;
    }

    public BufferedImageIcon[] getAllEditorAppearancesOnLayer(final int layer, final boolean useDisable) {
	if (useDisable) {
	    final BufferedImageIcon[] allEditorAppearancesOnLayer = new BufferedImageIcon[this.allObjects.length];
	    for (int x = 0; x < this.allObjects.length; x++) {
		if (this.allObjects[x].getLayer() == layer) {
		    this.allObjects[x].setEnabled(true);
		} else {
		    this.allObjects[x].setEnabled(false);
		}
		allEditorAppearancesOnLayer[x] = ImageLoader.getImage(this.allObjects[x], false);
	    }
	    return allEditorAppearancesOnLayer;
	} else {
	    final BufferedImageIcon[] tempAllEditorAppearancesOnLayer = new BufferedImageIcon[this.allObjects.length];
	    int objectCount = 0;
	    for (int x = 0; x < this.allObjects.length; x++) {
		if (this.allObjects[x].getLayer() == layer) {
		    tempAllEditorAppearancesOnLayer[x] = ImageLoader.getImage(this.allObjects[x], false);
		}
	    }
	    for (final BufferedImageIcon element : tempAllEditorAppearancesOnLayer) {
		if (element != null) {
		    objectCount++;
		}
	    }
	    final BufferedImageIcon[] allEditorAppearancesOnLayer = new BufferedImageIcon[objectCount];
	    objectCount = 0;
	    for (final BufferedImageIcon element : tempAllEditorAppearancesOnLayer) {
		if (element != null) {
		    allEditorAppearancesOnLayer[objectCount] = element;
		    objectCount++;
		}
	    }
	    return allEditorAppearancesOnLayer;
	}
    }

    public AbstractDungeonObject readDungeonObjectG2(final XDataReader reader, final int formatVersion)
	    throws IOException {
	AbstractDungeonObject o = null;
	String UID = LocaleConstants.COMMON_STRING_SPACE;
	if (FormatConstants.isFormatVersionValidGeneration1(formatVersion)
		|| FormatConstants.isFormatVersionValidGeneration2(formatVersion)) {
	    UID = reader.readString();
	} else {
	    return null;
	}
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    try {
		final AbstractDungeonObject instance = allObject.getClass().getConstructor().newInstance();
		if (FormatConstants.isFormatVersionValidGeneration1(formatVersion)
			|| FormatConstants.isFormatVersionValidGeneration2(formatVersion)) {
		    o = instance.readDungeonObjectG2(reader, UID, formatVersion);
		} else {
		    return null;
		}
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		DungeonDiver7.getErrorLogger().logError(e);
	    }
	}
	return null;
    }

    public AbstractDungeonObject readDungeonObjectG3(final XDataReader reader, final int formatVersion)
	    throws IOException {
	AbstractDungeonObject o = null;
	String UID = LocaleConstants.COMMON_STRING_SPACE;
	if (FormatConstants.isFormatVersionValidGeneration3(formatVersion)) {
	    UID = reader.readString();
	} else {
	    return null;
	}
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    try {
		final AbstractDungeonObject instance = allObject.getClass().getConstructor().newInstance();
		if (FormatConstants.isFormatVersionValidGeneration3(formatVersion)) {
		    o = instance.readDungeonObjectG3(reader, UID, formatVersion);
		} else {
		    return null;
		}
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		DungeonDiver7.getErrorLogger().logError(e);
	    }
	}
	return null;
    }

    public AbstractDungeonObject readDungeonObjectG4(final XDataReader reader, final int formatVersion)
	    throws IOException {
	AbstractDungeonObject o = null;
	String UID = LocaleConstants.COMMON_STRING_SPACE;
	if (FormatConstants.isFormatVersionValidGeneration4(formatVersion)) {
	    UID = reader.readString();
	} else {
	    return null;
	}
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    try {
		final AbstractDungeonObject instance = allObject.getClass().getConstructor().newInstance();
		if (FormatConstants.isFormatVersionValidGeneration4(formatVersion)) {
		    o = instance.readDungeonObjectG4(reader, UID, formatVersion);
		} else {
		    return null;
		}
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		DungeonDiver7.getErrorLogger().logError(e);
	    }
	}
	return null;
    }

    public AbstractDungeonObject readDungeonObjectG5(final XDataReader reader, final int formatVersion)
	    throws IOException {
	AbstractDungeonObject o = null;
	String UID = LocaleConstants.COMMON_STRING_SPACE;
	if (FormatConstants.isFormatVersionValidGeneration5(formatVersion)) {
	    UID = reader.readString();
	} else {
	    return null;
	}
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    try {
		final AbstractDungeonObject instance = allObject.getClass().getConstructor().newInstance();
		if (FormatConstants.isFormatVersionValidGeneration5(formatVersion)) {
		    o = instance.readDungeonObjectG5(reader, UID, formatVersion);
		} else {
		    return null;
		}
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		DungeonDiver7.getErrorLogger().logError(e);
	    }
	}
	return null;
    }

    public AbstractDungeonObject readDungeonObjectG6(final XDataReader reader, final int formatVersion)
	    throws IOException {
	AbstractDungeonObject o = null;
	String UID = LocaleConstants.COMMON_STRING_SPACE;
	if (FormatConstants.isFormatVersionValidGeneration6(formatVersion)) {
	    UID = reader.readString();
	} else {
	    return null;
	}
	for (final AbstractDungeonObject allObject : this.allObjects) {
	    try {
		final AbstractDungeonObject instance = allObject.getClass().getConstructor().newInstance();
		if (FormatConstants.isFormatVersionValidGeneration6(formatVersion)) {
		    o = instance.readDungeonObjectG6(reader, UID, formatVersion);
		} else {
		    return null;
		}
		if (o != null) {
		    return o;
		}
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
		DungeonDiver7.getErrorLogger().logError(e);
	    }
	}
	return null;
    }
}
