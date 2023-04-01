/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.dungeon.abc;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;
import java.util.Objects;

import com.puttysoftware.diane.locale.DianeStrings;
import com.puttysoftware.diane.direction.DirectionResolver;
import com.puttysoftware.diane.direction.Direction;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.game.GameLogic;
import com.puttysoftware.dungeondiver7.loader.BattleImageManager;
import com.puttysoftware.dungeondiver7.loader.ObjectImageConstants;
import com.puttysoftware.dungeondiver7.loader.ObjectImageManager;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.locale.Colors;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;
import com.puttysoftware.dungeondiver7.utility.ImageColors;
import com.puttysoftware.dungeondiver7.utility.Materials;
import com.puttysoftware.dungeondiver7.utility.RandomGenerationRule;
import com.puttysoftware.dungeondiver7.utility.RangeTypes;
import com.puttysoftware.dungeondiver7.utility.ShotTypes;
import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;
import com.puttysoftware.diane.assets.image.BufferedImageIcon;
import com.puttysoftware.diane.random.RandomRange;
import com.puttysoftware.dungeondiver7.utility.CloneableObject;

public abstract class AbstractDungeonObject extends CloneableObject implements RandomGenerationRule {
    // Properties
    private boolean solid;
    private boolean pushable;
    private boolean friction;
    protected BitSet type;
    private int timerValue;
    private int initialTimerValue;
    private boolean timerActive;
    private int frameNumber;
    private Direction directions;
    private boolean diagonalOnly;
    private Colors color;
    private int material;
    private boolean imageEnabled;
    private final boolean blocksLOS;
    private static int templateColor = ImageColors.NONE;
    static final int DEFAULT_CUSTOM_VALUE = 0;
    protected static final int CUSTOM_FORMAT_MANUAL_OVERRIDE = -1;
    private static final int PLASTIC_MINIMUM_REACTION_FORCE = 0;
    private static final int DEFAULT_MINIMUM_REACTION_FORCE = 1;
    private static final int METAL_MINIMUM_REACTION_FORCE = 2;
    private AbstractDungeonObject saved;
    private AbstractDungeonObject previousState;

    // Constructors
    AbstractDungeonObject(final boolean isSolid) {
	this.solid = isSolid;
	this.blocksLOS = isSolid;
	this.pushable = false;
	this.friction = true;
	this.type = new BitSet(DungeonObjectTypes.TYPES_COUNT);
	this.timerValue = 0;
	this.initialTimerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.directions = Direction.NONE;
	this.diagonalOnly = false;
	this.color = Colors.NONE;
	this.material = Materials.DEFAULT;
	this.imageEnabled = true;
    }

    public AbstractDungeonObject(final boolean isSolid, final boolean sightBlock) {
	this.solid = isSolid;
	this.friction = true;
	this.blocksLOS = sightBlock;
	this.type = new BitSet(DungeonObjectTypes.TYPES_COUNT);
	this.timerValue = 0;
	this.initialTimerValue = 0;
	this.timerActive = false;
    }

    public AbstractDungeonObject(final boolean isSolid, final boolean hasFriction, final boolean sightBlock) {
	this.solid = isSolid;
	this.friction = hasFriction;
	this.blocksLOS = sightBlock;
	this.type = new BitSet(DungeonObjectTypes.TYPES_COUNT);
	this.timerValue = 0;
	this.initialTimerValue = 0;
	this.timerActive = false;
    }

    AbstractDungeonObject(final boolean isSolid, final boolean isPushable, final boolean hasFriction,
	    final boolean sightBlock) {
	this.solid = isSolid;
	this.blocksLOS = sightBlock;
	this.pushable = isPushable;
	this.friction = hasFriction;
	this.type = new BitSet(DungeonObjectTypes.TYPES_COUNT);
	this.timerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.directions = Direction.NONE;
	this.diagonalOnly = false;
	this.color = Colors.NONE;
	this.material = Materials.DEFAULT;
	this.imageEnabled = true;
    }

    public AbstractDungeonObject() {
	this.solid = false;
	this.blocksLOS = false;
	this.pushable = false;
	this.friction = true;
	this.type = new BitSet(DungeonObjectTypes.TYPES_COUNT);
	this.timerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.directions = Direction.NONE;
	this.diagonalOnly = false;
	this.color = Colors.NONE;
	this.material = Materials.DEFAULT;
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
	    copy.directions = this.directions;
	    copy.diagonalOnly = this.diagonalOnly;
	    copy.color = this.color;
	    copy.material = this.material;
	    return copy;
	} catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		| InvocationTargetException | NoSuchMethodException | SecurityException e) {
	    DungeonDiver7.logError(e);
	    return null;
	}
    }

    @Override
    public int hashCode() {
	final var prime = 31;
	var result = 1;
	result = prime * result + (this.friction ? 1231 : 1237);
	result = prime * result + this.initialTimerValue;
	result = prime * result + (this.pushable ? 1231 : 1237);
	result = prime * result + (this.solid ? 1231 : 1237);
	result = prime * result + (this.timerActive ? 1231 : 1237);
	result = prime * result + this.timerValue;
	result = prime * result + (this.type == null ? 0 : this.type.hashCode());
	result = prime * result + this.directions.hashCode();
	result = prime * result + this.color.ordinal();
	result = prime * result + (this.blocksLOS ? 1231 : 1237);
	return prime * result + this.material;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof final AbstractDungeonObject other) || this.friction != other.friction
		|| this.initialTimerValue != other.initialTimerValue) {
	    return false;
	}
	if (this.pushable != other.pushable || this.solid != other.solid || !Objects.equals(this.type, other.type)
		|| this.directions != other.directions) {
	    return false;
	}
	if (this.color != other.color || this.material != other.material || this.blocksLOS != other.blocksLOS) {
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
	return this.saved;
    }

    public final void setSavedObject(final AbstractDungeonObject obj) {
	this.saved = obj;
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
	return this.directions;
    }

    public final void toggleDirection() {
	this.directions = DungeonConstants.nextDirOrtho(this.directions);
    }

    public static boolean hitReflectiveSide(final Direction dir) {
	Direction trigger1, trigger2;
	trigger1 = DungeonConstants.previousDir(dir);
	trigger2 = DungeonConstants.nextDir(dir);
	return dir == trigger1 || dir == trigger2;
    }

    public final void setDirection(final Direction dir) {
	this.directions = dir;
    }

    private final boolean hasDirection() {
	return this.directions != null && this.directions != Direction.NONE && this.directions != Direction.NONE;
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

    public final Colors getColor() {
	return this.color;
    }

    public final void setColor(final Colors col) {
	this.color = col;
    }

    private final boolean hasColor() {
	return this.color != null && this.color != Colors.NONE;
    }

    private final void toggleColor() {
	if (this.hasColor()) {
	    var oldColorValue = this.color.ordinal();
	    final var newColorValue = oldColorValue;
	    oldColorValue++;
	    Colors newColor;
	    if (newColorValue >= Strings.COLOR_COUNT) {
		newColor = Colors.GRAY;
	    } else {
		newColor = Colors.values()[newColorValue];
	    }
	    this.color = newColor;
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

    public boolean isSolidInBattle() {
	if (this.enabledInBattle()) {
	    return this.isSolid();
	}
	return false;
    }

    public boolean isSightBlocking() {
	return this.blocksLOS;
    }

    public final boolean isOfType(final int testType) {
	return this.type.get(testType);
    }

    public final boolean hasFriction() {
	return this.friction;
    }

    public static int getTemplateColor() {
	return AbstractDungeonObject.templateColor;
    }

    public static void setTemplateColor(final int newTC) {
	AbstractDungeonObject.templateColor = newTC;
    }

    // Scripting
    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     * @return
     */
    public boolean preMoveAction(final boolean ie, final int dirX, final int dirY) {
	return true;
    }

    public abstract void postMoveAction(final int dirX, final int dirY, int dirZ);

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     */
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	SoundLoader.playSound(Sounds.FAILED);
	DungeonDiver7.getStuffBag().showMessage("Can't go that way");
    }

    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     */
    public void interactAction() {
	SoundLoader.playSound(Sounds.FAILED);
	DungeonDiver7.getStuffBag().showMessage("Can't interact with that");
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
	}
	if (this.hasColor()) {
	    this.toggleColor();
	    return this;
	}
	return null;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void editorGenerateHook(final int x, final int y) {
	// Do nothing
    }

    public boolean arrowHitBattleCheck() {
	return !this.isSolid();
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public BufferedImageIcon gameRenderHook(final int x, final int y) {
	return ObjectImageManager.getImage(this.getName(), this.getBaseID());
    }

    public BufferedImageIcon battleRenderHook() {
	return BattleImageManager.getImage(this.getName(), this.getBattleBaseID());
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
	SoundLoader.playSound(Sounds.CRUSH);
	DungeonDiver7.getStuffBag().getGameLogic();
	GameLogic.morph(new Empty(), x, y, z, this.getLayer());
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
	if (this.timerActive && this.acceptTick(actionType)) {
	    this.timerValue--;
	    if (this.timerValue == 0) {
		this.timerActive = false;
		this.initialTimerValue = 0;
		this.timerExpiredAction(dirX, dirY);
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
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.FIRE && this.getMaterial() == Materials.WOODEN
		&& this.changesToOnExposure(Materials.FIRE) != null) {
	    // Burn wooden object
	    SoundLoader.playSound(Sounds.WOOD_BURN);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Materials.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.ICE
		&& (this.getMaterial() == Materials.METALLIC || this.getMaterial() == Materials.WOODEN
			|| this.getMaterial() == Materials.PLASTIC)
		&& this.changesToOnExposure(Materials.ICE) != null) {
	    // Freeze metal, wooden, or plastic object
	    SoundLoader.playSound(Sounds.FROZEN);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Materials.ICE), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.FIRE && this.getMaterial() == Materials.ICE
		&& this.changesToOnExposure(Materials.FIRE) != null) {
	    // Melt icy object
	    SoundLoader.playSound(Sounds.DEFROST);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Materials.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.ICE && this.getMaterial() == Materials.FIRE
		&& this.changesToOnExposure(Materials.ICE) != null) {
	    // Cool hot object
	    SoundLoader.playSound(Sounds.COOL_OFF);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Materials.ICE), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.FIRE && this.getMaterial() == Materials.METALLIC
		&& this.changesToOnExposure(Materials.FIRE) != null) {
	    // Melt metal object
	    SoundLoader.playSound(Sounds.MELT);
	    DungeonDiver7.getStuffBag().getGameLogic();
	    GameLogic.morph(this.changesToOnExposure(Materials.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
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
	if (!this.isSolid()) {
	    return DirectionResolver.resolve(dirX, dirY);
	}
	if (forceUnits > this.getMinimumReactionForce() && this.canMove()) {
	    try {
		final var nextObj = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getCell(locX + dirX,
			locY + dirY, locZ, this.getLayer());
		final var nextObj2 = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon()
			.getCell(locX + dirX * 2, locY + dirY * 2, locZ, this.getLayer());
		if (this instanceof final AbstractMovableObject gmo
			&& nextObj instanceof final AbstractMovableObject gmo2 && nextObj.canMove()
			&& (nextObj2 != null && !nextObj2.isConditionallySolid() || forceUnits > 2)) {
		    DungeonDiver7.getStuffBag().getGameLogic().updatePushedPositionLater(locX, locY, dirX, dirY, gmo,
			    locX + dirX, locY + dirY, gmo2, laserType,
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
	    final var adj = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon().getCell(locX - dirX,
		    locY - dirY, locZ, this.getLayer());
	    if (adj != null && !adj.rangeAction(locX - 2 * dirX, locY - 2 * dirY, locZ, dirX, dirY,
		    ShotTypes.getRangeTypeForLaserType(laserType), 1)) {
		SoundLoader.playSound(Sounds.LASER_DIE);
	    }
	}
	return Direction.NONE;
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
	return DirectionResolver.resolve(dirX, dirY);
    }

    public void laserDoneAction() {
	// Do nothing
    }

    public boolean defersSetProperties() {
	return false;
    }

    public boolean overridesDefaultPostMove() {
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
	return this.getBaseImageName() + this.getDirectionSuffix() + this.getFrameSuffix();
    }

    public final String getBaseImageName() {
	return Strings.objectImage(this.getBaseID());
    }

    private final String getLocalColorPrefix() {
	if (this.hasColor()) {
	    return Strings.color(this.color) + Strings.SPACE;
	}
	return Strings.EMPTY;
    }

    private final String getDirectionSuffix() {
	if (this.hasDirection()) {
	    return Strings.SPACE + DianeStrings.directionSuffix(this.directions);
	}
	return Strings.EMPTY;
    }

    private final String getFrameSuffix() {
	if (this.isAnimated()) {
	    return Strings.SPACE + this.frameNumber;
	}
	return Strings.EMPTY;
    }

    public static final int getImbuedRangeForce(final int material) {
	if (material == Materials.PLASTIC) {
	    return AbstractDungeonObject.PLASTIC_MINIMUM_REACTION_FORCE;
	}
	if (material == Materials.METALLIC) {
	    return AbstractDungeonObject.METAL_MINIMUM_REACTION_FORCE;
	}
	return AbstractDungeonObject.DEFAULT_MINIMUM_REACTION_FORCE;
    }

    public final int getMinimumReactionForce() {
	if (this.material == Materials.PLASTIC) {
	    return AbstractDungeonObject.PLASTIC_MINIMUM_REACTION_FORCE;
	}
	if (this.material == Materials.METALLIC) {
	    return AbstractDungeonObject.METAL_MINIMUM_REACTION_FORCE;
	}
	return AbstractDungeonObject.DEFAULT_MINIMUM_REACTION_FORCE;
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

    public int getBattleBaseID() {
	if (this.enabledInBattle()) {
	    return this.getBaseID();
	}
	return ObjectImageConstants.NONE;
    }

    public boolean enabledInBattle() {
	return true;
    }

    public boolean isMoving() {
	return false;
    }

    abstract public int getBaseID();

    public int getBlockHeight() {
	return 1;
    }

    public final String getBaseName() {
	return Strings.object(this.getBaseID() * 3 + 0);
    }

    public final String getIdentityName() {
	return this.getLocalColorPrefix() + Strings.object(this.getBaseID() * 3 + 0);
    }

    public String getName() {
	return Strings.object(this.getBaseID() * 3 + 0);
    }

    public String getPluralName() {
	return Strings.object(this.getBaseID() * 3 + 1);
    }

    public String getDescription() {
	return Strings.object(this.getBaseID() * 3 + 2);
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

    @Override
    public boolean shouldGenerateObject(final AbstractDungeon dungeon, final int row, final int col, final int level,
	    final int layer) {
	if (layer == DungeonConstants.LAYER_LOWER_OBJECTS) {
	    // Handle object layer
	    // Limit generation of other objects to 20%, unless required
	    if (this.isOfType(DungeonObjectTypes.TYPE_PASS_THROUGH) || this.isRequired(dungeon)) {
		return true;
	    }
	    final var r = new RandomRange(1, 100);
	    if (r.generate() <= 20) {
		return true;
	    }
	    return false;
	}
	if (!this.isOfType(DungeonObjectTypes.TYPE_FIELD)) {
	    // Generate other ground at 100%
	    return true;
	}
	// Limit generation of fields to 20%
	final var r = new RandomRange(1, 100);
	if (r.generate() <= 20) {
	    return true;
	}
	return false;
    }

    @Override
    public int getMinimumRequiredQuantity(final AbstractDungeon dungeon) {
	return RandomGenerationRule.NO_LIMIT;
    }

    @Override
    public int getMaximumRequiredQuantity(final AbstractDungeon dungeon) {
	return RandomGenerationRule.NO_LIMIT;
    }

    @Override
    public boolean isRequired(final AbstractDungeon dungeon) {
	return false;
    }

    public final void write(final DataIOWriter writer) throws IOException {
	writer.writeString(this.getIdentifier());
	final var cc = this.getCustomFormat();
	if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    writer.writeInt(this.directions.ordinal());
	    writer.writeInt(this.color.ordinal());
	    this.writeHook(writer);
	} else {
	    writer.writeInt(this.directions.ordinal());
	    writer.writeInt(this.color.ordinal());
	    for (var x = 0; x < cc; x++) {
		final var cx = this.getCustomProperty(x + 1);
		writer.writeInt(cx);
	    }
	}
    }

    public final AbstractDungeonObject readV2(final DataIOReader reader, final String ident, final int ver)
	    throws IOException {
	if (!ident.equals(this.getIdentifier())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.directions = Direction.values()[reader.readInt()];
	    reader.readInt();
	    this.color = Colors.values()[reader.readInt()];
	    return this.readHookV2(reader, ver);
	}
	this.directions = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final AbstractDungeonObject readV3(final DataIOReader reader, final String ident, final int ver)
	    throws IOException {
	if (!ident.equals(this.getIdentifier())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.directions = Direction.values()[reader.readInt()];
	    this.color = Colors.values()[reader.readInt()];
	    // Discard material
	    reader.readInt();
	    return this.readHookV3(reader, ver);
	}
	this.directions = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	// Discard material
	reader.readInt();
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final AbstractDungeonObject readV4(final DataIOReader reader, final String ident, final int ver)
	    throws IOException {
	if (!ident.equals(this.getIdentifier())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.directions = Direction.values()[reader.readInt()];
	    this.color = Colors.values()[reader.readInt()];
	    return this.readHookV4(reader, ver);
	}
	this.directions = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final AbstractDungeonObject readV5(final DataIOReader reader, final String ident, final int ver)
	    throws IOException {
	if (!ident.equals(this.getIdentifier())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.directions = Direction.values()[reader.readInt()];
	    this.color = Colors.values()[reader.readInt()];
	    return this.readHookV5(reader, ver);
	}
	this.directions = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final AbstractDungeonObject readV6(final DataIOReader reader, final String ident, final int ver)
	    throws IOException {
	if (!ident.equals(this.getIdentifier())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.directions = Direction.values()[reader.readInt()];
	    this.color = Colors.values()[reader.readInt()];
	    return this.readHookV6(reader, ver);
	}
	this.directions = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final AbstractDungeonObject readV7(final DataIOReader reader, final String ident, final int ver)
	    throws IOException {
	if (!ident.equals(this.getIdentifier())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.directions = Direction.values()[reader.readInt()];
	    this.color = Colors.values()[reader.readInt()];
	    return this.readHookV7(reader, ver);
	}
	this.directions = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    /**
     *
     * @param writer
     * @throws IOException
     */
    protected void writeHook(final DataIOWriter writer) throws IOException {
	// Do nothing - but let subclasses override
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractDungeonObject readHookV2(final DataIOReader reader, final int formatVersion) throws IOException {
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
    protected AbstractDungeonObject readHookV3(final DataIOReader reader, final int formatVersion) throws IOException {
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
    protected AbstractDungeonObject readHookV4(final DataIOReader reader, final int formatVersion) throws IOException {
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
    protected AbstractDungeonObject readHookV5(final DataIOReader reader, final int formatVersion) throws IOException {
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
    protected AbstractDungeonObject readHookV6(final DataIOReader reader, final int formatVersion) throws IOException {
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
    protected AbstractDungeonObject readHookV7(final DataIOReader reader, final int formatVersion) throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }
}
