/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abstractobjects;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;

import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.loaders.SoundConstants;
import com.puttysoftware.dungeondiver7.loaders.SoundLoader;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;
import com.puttysoftware.dungeondiver7.utilities.DungeonConstants;
import com.puttysoftware.dungeondiver7.utilities.ColorConstants;
import com.puttysoftware.dungeondiver7.utilities.ColorResolver;
import com.puttysoftware.dungeondiver7.utilities.Direction;
import com.puttysoftware.dungeondiver7.utilities.DirectionResolver;
import com.puttysoftware.dungeondiver7.utilities.ArrowTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.MaterialConstants;
import com.puttysoftware.dungeondiver7.utilities.RangeTypeConstants;
import com.puttysoftware.dungeondiver7.utilities.TypeConstants;
import com.puttysoftware.storage.CloneableObject;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public abstract class AbstractDungeonObject extends CloneableObject {
    // Properties
    private boolean solid;
    private boolean pushable;
    private boolean friction;
    protected BitSet type;
    private int timerValue;
    private int initialTimerValue;
    private boolean timerActive;
    private int frameNumber;
    private Direction direction;
    private boolean diagonalOnly;
    private int color;
    private int material;
    private boolean imageEnabled;
    static final int DEFAULT_CUSTOM_VALUE = 0;
    protected static final int CUSTOM_FORMAT_MANUAL_OVERRIDE = -1;
    private static final int PLASTIC_MINIMUM_REACTION_FORCE = 0;
    private static final int DEFAULT_MINIMUM_REACTION_FORCE = 1;
    private static final int METAL_MINIMUM_REACTION_FORCE = 2;
    private AbstractDungeonObject savedObject;
    private AbstractDungeonObject previousState;

    // Constructors
    AbstractDungeonObject(final boolean isSolid) {
	this.solid = isSolid;
	this.pushable = false;
	this.friction = true;
	this.type = new BitSet(TypeConstants.TYPES_COUNT);
	this.timerValue = 0;
	this.initialTimerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.direction = Direction.NONE;
	this.diagonalOnly = false;
	this.color = -1;
	this.material = MaterialConstants.MATERIAL_DEFAULT;
	this.imageEnabled = true;
    }

    AbstractDungeonObject(final boolean isSolid, final boolean isPushable, final boolean hasFriction) {
	this.solid = isSolid;
	this.pushable = isPushable;
	this.friction = hasFriction;
	this.type = new BitSet(TypeConstants.TYPES_COUNT);
	this.timerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.direction = Direction.NONE;
	this.diagonalOnly = false;
	this.color = -1;
	this.material = MaterialConstants.MATERIAL_DEFAULT;
	this.imageEnabled = true;
    }

    public AbstractDungeonObject() {
	this.solid = false;
	this.pushable = false;
	this.friction = true;
	this.type = new BitSet(TypeConstants.TYPES_COUNT);
	this.timerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.direction = Direction.NONE;
	this.diagonalOnly = false;
	this.color = -1;
	this.material = MaterialConstants.MATERIAL_DEFAULT;
	this.imageEnabled = true;
    }

    // Methods
    @Override
    public AbstractDungeonObject clone() {
	try {
	    final AbstractDungeonObject copy = this.getClass().getConstructor().newInstance();
	    copy.solid = this.solid;
	    copy.pushable = this.pushable;
	    copy.friction = this.friction;
	    copy.type = (BitSet) this.type.clone();
	    copy.timerValue = this.timerValue;
	    copy.initialTimerValue = this.initialTimerValue;
	    copy.timerActive = this.timerActive;
	    copy.frameNumber = this.frameNumber;
	    copy.direction = this.direction;
	    copy.diagonalOnly = this.diagonalOnly;
	    copy.color = this.color;
	    copy.material = this.material;
	    return copy;
	} catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		| InvocationTargetException | NoSuchMethodException | SecurityException e) {
	    DungeonDiver7.getErrorLogger().logError(e);
	    return null;
	}
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (this.friction ? 1231 : 1237);
	result = prime * result + this.initialTimerValue;
	result = prime * result + (this.pushable ? 1231 : 1237);
	result = prime * result + (this.solid ? 1231 : 1237);
	result = prime * result + (this.timerActive ? 1231 : 1237);
	result = prime * result + this.timerValue;
	result = prime * result + (this.type == null ? 0 : this.type.hashCode());
	result = prime * result + this.direction.hashCode();
	result = prime * result + this.color;
	return prime * result + this.material;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof AbstractDungeonObject)) {
	    return false;
	}
	final AbstractDungeonObject other = (AbstractDungeonObject) obj;
	if (this.friction != other.friction) {
	    return false;
	}
	if (this.initialTimerValue != other.initialTimerValue) {
	    return false;
	}
	if (this.pushable != other.pushable) {
	    return false;
	}
	if (this.solid != other.solid) {
	    return false;
	}
	if (this.type == null) {
	    if (other.type != null) {
		return false;
	    }
	} else if (!this.type.equals(other.type)) {
	    return false;
	}
	if (this.direction != other.direction) {
	    return false;
	}
	if (this.color != other.color) {
	    return false;
	}
	if (this.material != other.material) {
	    return false;
	}
	return true;
    }

    public boolean isEnabled() {
	return this.imageEnabled;
    }

    public void setEnabled(final boolean value) {
	this.imageEnabled = value;
    }

    public final AbstractDungeonObject getSavedObject() {
	return this.savedObject;
    }

    public final void setSavedObject(final AbstractDungeonObject obj) {
	this.savedObject = obj;
    }

    public final boolean hasPreviousState() {
	return this.previousState != null;
    }

    public final AbstractDungeonObject getPreviousState() {
	return this.previousState;
    }

    public final void setPreviousState(final AbstractDungeonObject obj) {
	this.previousState = obj;
    }

    public final int getFrameNumber() {
	return this.frameNumber;
    }

    public final void setFrameNumber(final int frame) {
	this.frameNumber = frame;
    }

    public final void toggleFrameNumber() {
	if (this.isAnimated()) {
	    this.frameNumber++;
	    if (this.frameNumber > 3) {
		this.frameNumber = 1;
	    }
	}
    }

    private final boolean isAnimated() {
	return this.frameNumber > 0;
    }

    public final Direction getDirection() {
	return this.direction;
    }

    public final void toggleDirection() {
	this.direction = DungeonConstants.nextDirOrtho(this.direction);
    }

    public static boolean hitReflectiveSide(final Direction dir) {
	Direction trigger1, trigger2;
	trigger1 = DungeonConstants.previousDir(dir);
	trigger2 = DungeonConstants.nextDir(dir);
	return dir == trigger1 || dir == trigger2;
    }

    public final void setDirection(final Direction dir) {
	this.direction = dir;
    }

    private final boolean hasDirection() {
	return this.direction != Direction.INVALID && this.direction != Direction.NONE;
    }

    public final int getMaterial() {
	return this.material;
    }

    protected final void setMaterial(final int mat) {
	this.material = mat;
    }

    /**
     *
     * @param materialID
     * @return
     */
    public AbstractDungeonObject changesToOnExposure(final int materialID) {
	return this;
    }

    public final int getColor() {
	return this.color;
    }

    public final void setColor(final int col) {
	this.color = col;
    }

    private final boolean hasColor() {
	return this.color >= 0;
    }

    private final void toggleColor() {
	if (this.hasColor()) {
	    this.color++;
	    if (this.color >= ColorConstants.COLOR_COUNT) {
		this.color = ColorConstants.COLOR_GRAY;
	    }
	}
    }

    public final void setDiagonalOnly(final boolean value) {
	this.diagonalOnly = value;
    }

    public final boolean isPushable() {
	return this.pushable;
    }

    public final boolean isSolid() {
	return this.solid;
    }

    public boolean isConditionallySolid() {
	return this.solid;
    }

    public final boolean isOfType(final int testType) {
	return this.type.get(testType);
    }

    public final boolean hasFriction() {
	return this.friction;
    }

    // Scripting
    public abstract void postMoveAction(final int dirX, final int dirY, int dirZ);

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     */
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	SoundLoader.playSound(SoundConstants.SOUND_BUMP_HEAD);
    }

    public AbstractDungeonObject attributeGameRenderHook() {
	return null;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public void editorPlaceHook(final int x, final int y, final int z) {
	// Do nothing
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public void editorRemoveHook(final int x, final int y, final int z) {
	// Do nothing
    }

    public AbstractDungeonObject editorPropertiesHook() {
	if (this.hasDirection()) {
	    this.toggleDirection();
	    return this;
	} else if (this.hasColor()) {
	    this.toggleColor();
	    return this;
	} else {
	    return null;
	}
    }

    /**
     *
     * @param pushed
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean pushIntoAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// Do nothing
	return true;
    }

    /**
     *
     * @param pushed
     * @param x
     * @param y
     * @param z
     */
    public void pushCollideAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// Do nothing
    }

    protected void pushCrushAction(final int x, final int y, final int z) {
	// Object crushed
	SoundLoader.playSound(SoundConstants.SOUND_CRUSH);
	DungeonDiver7.getApplication().getGameManager().morph(new Empty(), x, y, z, this.getLayer());
    }

    /**
     *
     * @param pushed
     * @param x
     * @param y
     * @param z
     */
    public void pushOutAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
	// Do nothing
    }

    public final void activateTimer(final int ticks) {
	this.timerActive = true;
	this.timerValue = ticks;
	this.initialTimerValue = ticks;
    }

    public final void tickTimer(final int dirX, final int dirY, final int actionType) {
	if (this.timerActive) {
	    if (this.acceptTick(actionType)) {
		this.timerValue--;
		if (this.timerValue == 0) {
		    this.timerActive = false;
		    this.initialTimerValue = 0;
		    this.timerExpiredAction(dirX, dirY);
		}
	    }
	}
    }

    /**
     *
     * @param actionType
     * @return
     */
    public boolean acceptTick(final int actionType) {
	return true;
    }

    /**
     *
     * @param dirX
     * @param dirY
     */
    public void timerExpiredAction(final int dirX, final int dirY) {
	// Do nothing
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     * @param dirX
     * @param dirY
     * @param rangeType
     * @param forceUnits
     * @return
     */
    public boolean rangeAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int rangeType, final int forceUnits) {
	if (RangeTypeConstants.getMaterialForRangeType(rangeType) == MaterialConstants.MATERIAL_FIRE
		&& this.getMaterial() == MaterialConstants.MATERIAL_WOODEN
		&& this.changesToOnExposure(MaterialConstants.MATERIAL_FIRE) != null) {
	    // Burn wooden object
	    SoundLoader.playSound(SoundConstants.SOUND_WOOD_BURN);
	    DungeonDiver7.getApplication().getGameManager().morph(
		    this.changesToOnExposure(MaterialConstants.MATERIAL_FIRE), locX + dirX, locY + dirY, locZ,
		    this.getLayer());
	    return true;
	} else if (RangeTypeConstants.getMaterialForRangeType(rangeType) == MaterialConstants.MATERIAL_ICE
		&& (this.getMaterial() == MaterialConstants.MATERIAL_METALLIC
			|| this.getMaterial() == MaterialConstants.MATERIAL_WOODEN
			|| this.getMaterial() == MaterialConstants.MATERIAL_PLASTIC)
		&& this.changesToOnExposure(MaterialConstants.MATERIAL_ICE) != null) {
	    // Freeze metal, wooden, or plastic object
	    SoundLoader.playSound(SoundConstants.SOUND_FROZEN);
	    DungeonDiver7.getApplication().getGameManager().morph(
		    this.changesToOnExposure(MaterialConstants.MATERIAL_ICE), locX + dirX, locY + dirY, locZ,
		    this.getLayer());
	    return true;
	} else if (RangeTypeConstants.getMaterialForRangeType(rangeType) == MaterialConstants.MATERIAL_FIRE
		&& this.getMaterial() == MaterialConstants.MATERIAL_ICE
		&& this.changesToOnExposure(MaterialConstants.MATERIAL_FIRE) != null) {
	    // Melt icy object
	    SoundLoader.playSound(SoundConstants.SOUND_DEFROST);
	    DungeonDiver7.getApplication().getGameManager().morph(
		    this.changesToOnExposure(MaterialConstants.MATERIAL_FIRE), locX + dirX, locY + dirY, locZ,
		    this.getLayer());
	    return true;
	} else if (RangeTypeConstants.getMaterialForRangeType(rangeType) == MaterialConstants.MATERIAL_ICE
		&& this.getMaterial() == MaterialConstants.MATERIAL_FIRE
		&& this.changesToOnExposure(MaterialConstants.MATERIAL_ICE) != null) {
	    // Cool hot object
	    SoundLoader.playSound(SoundConstants.SOUND_COOL_OFF);
	    DungeonDiver7.getApplication().getGameManager().morph(
		    this.changesToOnExposure(MaterialConstants.MATERIAL_ICE), locX + dirX, locY + dirY, locZ,
		    this.getLayer());
	    return true;
	} else if (RangeTypeConstants.getMaterialForRangeType(rangeType) == MaterialConstants.MATERIAL_FIRE
		&& this.getMaterial() == MaterialConstants.MATERIAL_METALLIC
		&& this.changesToOnExposure(MaterialConstants.MATERIAL_FIRE) != null) {
	    // Melt metal object
	    SoundLoader.playSound(SoundConstants.SOUND_MELT);
	    DungeonDiver7.getApplication().getGameManager().morph(
		    this.changesToOnExposure(MaterialConstants.MATERIAL_FIRE), locX + dirX, locY + dirY, locZ,
		    this.getLayer());
	    return true;
	}
	return false;
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     * @param dirX
     * @param dirY
     * @param laserType
     * @param forceUnits
     * @return
     */
    public Direction laserEnteredAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType, final int forceUnits) {
	if (this.isSolid()) {
	    if (forceUnits > this.getMinimumReactionForce() && this.canMove()) {
		try {
		    final AbstractDungeonObject nextObj = DungeonDiver7.getApplication().getDungeonManager()
			    .getDungeon().getCell(locX + dirX, locY + dirY, locZ, this.getLayer());
		    final AbstractDungeonObject nextObj2 = DungeonDiver7.getApplication().getDungeonManager()
			    .getDungeon().getCell(locX + dirX * 2, locY + dirY * 2, locZ, this.getLayer());
		    if (this instanceof AbstractMovableObject && nextObj != null
			    && nextObj instanceof AbstractMovableObject && nextObj.canMove()
			    && (nextObj2 != null && !nextObj2.isConditionallySolid() || forceUnits > 2)) {
			// Move BOTH this object and the one in front of it
			final AbstractMovableObject gmo = (AbstractMovableObject) this;
			final AbstractMovableObject gmo2 = (AbstractMovableObject) nextObj;
			DungeonDiver7.getApplication().getGameManager().updatePushedPositionLater(locX, locY, dirX,
				dirY, gmo, locX + dirX, locY + dirY, gmo2, laserType,
				forceUnits - Math.max(1, this.getMinimumReactionForce()));
		    } else {
			// Object crushed by impact
			this.pushCrushAction(locX, locY, locZ);
		    }
		} catch (final ArrayIndexOutOfBoundsException aioob) {
		    // Object crushed by impact
		    this.pushCrushAction(locX, locY, locZ);
		}
	    } else {
		final AbstractDungeonObject adj = DungeonDiver7.getApplication().getDungeonManager().getDungeon()
			.getCell(locX - dirX, locY - dirY, locZ, this.getLayer());
		if (adj != null && !adj.rangeAction(locX - 2 * dirX, locY - 2 * dirY, locZ, dirX, dirY,
			ArrowTypeConstants.getRangeTypeForLaserType(laserType), 1)) {
		    SoundLoader.playSound(SoundConstants.SOUND_LASER_DIE);
		}
	    }
	    return Direction.NONE;
	} else {
	    return DirectionResolver.resolveRelativeDirection(dirX, dirY);
	}
    }

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     * @param dirX
     * @param dirY
     * @param laserType
     * @return
     */
    public Direction laserExitedAction(final int locX, final int locY, final int locZ, final int dirX, final int dirY,
	    final int laserType) {
	return DirectionResolver.resolveRelativeDirection(dirX, dirY);
    }

    public void laserDoneAction() {
	// Do nothing
    }

    public boolean defersSetProperties() {
	return false;
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public final void determineCurrentAppearance(final int x, final int y, final int z) {
	// Do nothing
    }

    public final String getImageName() {
	return this.getColorPrefix() + this.getBaseImageName() + this.getDirectionSuffix() + this.getFrameSuffix();
    }

    public final String getBaseImageName() {
	return LocaleLoader.loadString(LocaleConstants.IMAGE_STRINGS_FILE, this.getStringBaseID());
    }

    private final String getColorPrefix() {
	if (this.hasColor()) {
	    return ColorResolver.resolveColorConstantToImageName(this.color) + LocaleConstants.COMMON_STRING_SPACE;
	} else {
	    return LocaleConstants.COMMON_STRING_EMPTY;
	}
    }

    private final String getLocalColorPrefix() {
	if (this.hasColor()) {
	    return ColorResolver.resolveColorConstantToName(this.color) + LocaleConstants.COMMON_STRING_SPACE;
	} else {
	    return LocaleConstants.COMMON_STRING_EMPTY;
	}
    }

    private final String getDirectionSuffix() {
	if (this.hasDirection()) {
	    return LocaleConstants.COMMON_STRING_SPACE
		    + DirectionResolver.resolveDirectionConstantToImageName(this.direction);
	} else {
	    return LocaleConstants.COMMON_STRING_EMPTY;
	}
    }

    private final String getFrameSuffix() {
	if (this.isAnimated()) {
	    return LocaleConstants.COMMON_STRING_SPACE + this.frameNumber;
	} else {
	    return LocaleConstants.COMMON_STRING_EMPTY;
	}
    }

    public static final int getImbuedRangeForce(final int material) {
	if (material == MaterialConstants.MATERIAL_PLASTIC) {
	    return AbstractDungeonObject.PLASTIC_MINIMUM_REACTION_FORCE;
	} else if (material == MaterialConstants.MATERIAL_METALLIC) {
	    return AbstractDungeonObject.METAL_MINIMUM_REACTION_FORCE;
	} else {
	    return AbstractDungeonObject.DEFAULT_MINIMUM_REACTION_FORCE;
	}
    }

    public final int getMinimumReactionForce() {
	if (this.material == MaterialConstants.MATERIAL_PLASTIC) {
	    return AbstractDungeonObject.PLASTIC_MINIMUM_REACTION_FORCE;
	} else if (this.material == MaterialConstants.MATERIAL_METALLIC) {
	    return AbstractDungeonObject.METAL_MINIMUM_REACTION_FORCE;
	} else {
	    return AbstractDungeonObject.DEFAULT_MINIMUM_REACTION_FORCE;
	}
    }

    public boolean canMove() {
	return false;
    }

    public boolean canShoot() {
	return false;
    }

    public boolean killsOnMove() {
	return false;
    }

    public boolean solvesOnMove() {
	return false;
    }

    public boolean doLasersPassThrough() {
	return true;
    }

    private final String getIdentifier() {
	return this.getBaseImageName();
    }

    abstract public int getStringBaseID();

    public int getBlockHeight() {
	return 1;
    }

    public final String getBaseName() {
	return LocaleLoader.loadString(LocaleConstants.OBJECT_STRINGS_FILE, this.getStringBaseID() * 3 + 0);
    }

    public final String getIdentityName() {
	return this.getLocalColorPrefix()
		+ LocaleLoader.loadString(LocaleConstants.OBJECT_STRINGS_FILE, this.getStringBaseID() * 3 + 0);
    }

    public final String getDescription() {
	return LocaleLoader.loadString(LocaleConstants.OBJECT_STRINGS_FILE, this.getStringBaseID() * 3 + 2);
    }

    abstract public int getLayer();

    abstract public int getCustomProperty(int propID);

    abstract public void setCustomProperty(int propID, int value);

    public int getCustomFormat() {
	return 0;
    }

    public String getCustomText() {
	return null;
    }

    public Color getCustomTextColor() {
	return null;
    }

    public final void writeDungeonObject(final XDataWriter writer) throws IOException {
	writer.writeString(this.getIdentifier());
	final int cc = this.getCustomFormat();
	if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    writer.writeInt(this.direction.ordinal());
	    writer.writeInt(this.color);
	    this.writeDungeonObjectHook(writer);
	} else {
	    writer.writeInt(this.direction.ordinal());
	    writer.writeInt(this.color);
	    for (int x = 0; x < cc; x++) {
		final int cx = this.getCustomProperty(x + 1);
		writer.writeInt(cx);
	    }
	}
    }

    public final AbstractDungeonObject readDungeonObjectG2(final XDataReader reader, final String ident, final int ver)
	    throws IOException {
	if (ident.equals(this.getIdentifier())) {
	    final int cc = this.getCustomFormat();
	    if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
		this.direction = Direction.values()[reader.readInt()];
		reader.readInt();
		this.color = reader.readInt();
		return this.readDungeonObjectHookG2(reader, ver);
	    } else {
		this.direction = Direction.values()[reader.readInt()];
		this.color = reader.readInt();
		for (int x = 0; x < cc; x++) {
		    final int cx = reader.readInt();
		    this.setCustomProperty(x + 1, cx);
		}
	    }
	    return this;
	} else {
	    return null;
	}
    }

    public final AbstractDungeonObject readDungeonObjectG3(final XDataReader reader, final String ident, final int ver)
	    throws IOException {
	if (ident.equals(this.getIdentifier())) {
	    final int cc = this.getCustomFormat();
	    if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
		this.direction = Direction.values()[reader.readInt()];
		this.color = reader.readInt();
		// Discard material
		reader.readInt();
		return this.readDungeonObjectHookG3(reader, ver);
	    } else {
		this.direction = Direction.values()[reader.readInt()];
		this.color = reader.readInt();
		// Discard material
		reader.readInt();
		for (int x = 0; x < cc; x++) {
		    final int cx = reader.readInt();
		    this.setCustomProperty(x + 1, cx);
		}
	    }
	    return this;
	} else {
	    return null;
	}
    }

    public final AbstractDungeonObject readDungeonObjectG4(final XDataReader reader, final String ident, final int ver)
	    throws IOException {
	if (ident.equals(this.getIdentifier())) {
	    final int cc = this.getCustomFormat();
	    if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
		this.direction = Direction.values()[reader.readInt()];
		this.color = reader.readInt();
		return this.readDungeonObjectHookG4(reader, ver);
	    } else {
		this.direction = Direction.values()[reader.readInt()];
		this.color = reader.readInt();
		for (int x = 0; x < cc; x++) {
		    final int cx = reader.readInt();
		    this.setCustomProperty(x + 1, cx);
		}
	    }
	    return this;
	} else {
	    return null;
	}
    }

    public final AbstractDungeonObject readDungeonObjectG5(final XDataReader reader, final String ident, final int ver)
	    throws IOException {
	if (ident.equals(this.getIdentifier())) {
	    final int cc = this.getCustomFormat();
	    if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
		this.direction = Direction.values()[reader.readInt()];
		this.color = reader.readInt();
		return this.readDungeonObjectHookG5(reader, ver);
	    } else {
		this.direction = Direction.values()[reader.readInt()];
		this.color = reader.readInt();
		for (int x = 0; x < cc; x++) {
		    final int cx = reader.readInt();
		    this.setCustomProperty(x + 1, cx);
		}
	    }
	    return this;
	} else {
	    return null;
	}
    }

    public final AbstractDungeonObject readDungeonObjectG6(final XDataReader reader, final String ident, final int ver)
	    throws IOException {
	if (ident.equals(this.getIdentifier())) {
	    final int cc = this.getCustomFormat();
	    if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
		this.direction = Direction.values()[reader.readInt()];
		this.color = reader.readInt();
		return this.readDungeonObjectHookG6(reader, ver);
	    } else {
		this.direction = Direction.values()[reader.readInt()];
		this.color = reader.readInt();
		for (int x = 0; x < cc; x++) {
		    final int cx = reader.readInt();
		    this.setCustomProperty(x + 1, cx);
		}
	    }
	    return this;
	} else {
	    return null;
	}
    }

    /**
     *
     * @param writer
     * @throws IOException
     */
    protected void writeDungeonObjectHook(final XDataWriter writer) throws IOException {
	// Do nothing - but let subclasses override
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractDungeonObject readDungeonObjectHookG2(final XDataReader reader, final int formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractDungeonObject readDungeonObjectHookG3(final XDataReader reader, final int formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractDungeonObject readDungeonObjectHookG4(final XDataReader reader, final int formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractDungeonObject readDungeonObjectHookG5(final XDataReader reader, final int formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractDungeonObject readDungeonObjectHookG6(final XDataReader reader, final int formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }
}
