/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionResolver;
import org.retropipes.diane.direction.DirectionStrings;
import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;
import org.retropipes.diane.random.RandomRange;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.asset.ObjectImageConstants;
import org.retropipes.dungeondiver7.dungeon.Dungeon;
import org.retropipes.dungeondiver7.dungeon.objects.Empty;
import org.retropipes.dungeondiver7.game.GameLogic;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageLoader;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.locale.Colors;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.utility.DungeonConstants;
import org.retropipes.dungeondiver7.utility.ImageColors;
import org.retropipes.dungeondiver7.utility.Materials;
import org.retropipes.dungeondiver7.utility.RandomGenerationRule;
import org.retropipes.dungeondiver7.utility.RangeTypes;
import org.retropipes.dungeondiver7.utility.ShotTypes;

public abstract class DungeonObject implements RandomGenerationRule {
    private static int templateColor = ImageColors.NONE;
    protected static final int DEFAULT_CUSTOM_VALUE = 0;
    protected static final int CUSTOM_FORMAT_MANUAL_OVERRIDE = -1;
    private static final int PLASTIC_MINIMUM_REACTION_FORCE = 0;
    private static final int DEFAULT_MINIMUM_REACTION_FORCE = 1;
    private static final int METAL_MINIMUM_REACTION_FORCE = 2;

    public static final int getImbuedRangeForce(final int material) {
	if (material == Materials.PLASTIC) {
	    return DungeonObject.PLASTIC_MINIMUM_REACTION_FORCE;
	}
	if (material == Materials.METALLIC) {
	    return DungeonObject.METAL_MINIMUM_REACTION_FORCE;
	}
	return DungeonObject.DEFAULT_MINIMUM_REACTION_FORCE;
    }

    public static int getTemplateColor() {
	return DungeonObject.templateColor;
    }

    public static boolean hitReflectiveSide(final Direction dir) {
	Direction trigger1, trigger2;
	trigger1 = DungeonConstants.previousDir(dir);
	trigger2 = DungeonConstants.nextDir(dir);
	return dir == trigger1 || dir == trigger2;
    }

    public static void setTemplateColor(final int newTC) {
	DungeonObject.templateColor = newTC;
    }

    // Properties
    private final boolean solid;
    private boolean pushable;
    private final boolean friction;
    private int timerValue;
    private boolean timerActive;
    private int frameNumber;
    private Direction direction;
    private Colors color;
    private int material;
    private final boolean blocksLOS;
    private DungeonObject saved;
    private DungeonObject previousState;
    private int teamID;

    public DungeonObject() {
	this.solid = false;
	this.blocksLOS = false;
	this.pushable = false;
	this.friction = true;
	this.timerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.direction = Direction.NONE;
	this.color = Colors._NONE;
	this.material = Materials.DEFAULT;
    }

    // Constructors
    DungeonObject(final boolean isSolid) {
	this.solid = isSolid;
	this.blocksLOS = isSolid;
	this.pushable = false;
	this.friction = true;
	this.timerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.direction = Direction.NONE;
	this.color = Colors._NONE;
	this.material = Materials.DEFAULT;
    }

    public DungeonObject(final boolean isSolid, final boolean sightBlock) {
	this.solid = isSolid;
	this.friction = true;
	this.blocksLOS = sightBlock;
	this.timerValue = 0;
	this.timerActive = false;
    }

    public DungeonObject(final boolean isSolid, final boolean hasFriction, final boolean sightBlock) {
	this.solid = isSolid;
	this.friction = hasFriction;
	this.blocksLOS = sightBlock;
	this.timerValue = 0;
	this.timerActive = false;
    }

    DungeonObject(final boolean isSolid, final boolean isPushable, final boolean hasFriction,
	    final boolean sightBlock) {
	this.solid = isSolid;
	this.blocksLOS = sightBlock;
	this.pushable = isPushable;
	this.friction = hasFriction;
	this.timerValue = 0;
	this.timerActive = false;
	this.frameNumber = 0;
	this.direction = Direction.NONE;
	this.color = Colors._NONE;
	this.material = Materials.DEFAULT;
    }

    public DungeonObject(final DungeonObject source) {
	this.solid = source.solid;
	this.blocksLOS = source.blocksLOS;
	this.pushable = source.pushable;
	this.friction = source.friction;
	this.timerValue = source.timerValue;
	this.timerActive = source.timerActive;
	this.frameNumber = source.frameNumber;
	this.direction = source.direction;
	this.color = source.color;
	this.material = source.material;
    }

    /**
     *
     * @param actionType
     * @return
     */
    public boolean acceptsTick(final int actionType) {
	return true;
    }

    public final void activateTimer(final int ticks) {
	this.timerActive = true;
	this.timerValue = ticks;
    }

    public boolean arrowHitBattleCheck() {
	return !this.isSolid();
    }

    public DungeonObject attributeGameRenderHook() {
	return null;
    }

    public BufferedImageIcon battleRenderHook() {
	return ObjectImageLoader.load(this.getCacheName(), this.getBattleBaseID());
    }

    public boolean canMove() {
	return false;
    }

    public boolean canMoveBoxes() {
	return false;
    }

    public boolean canMoveMirrors() {
	return false;
    }

    public boolean canMoveParty() {
	return false;
    }

    public boolean canShoot() {
	return false;
    }

    /**
     *
     * @param materialID
     * @return
     */
    public DungeonObject changesToOnExposure(final int materialID) {
	return this;
    }

    @Override
    public DungeonObject clone() {
	try {
	    return this.getClass().getConstructor(this.getClass()).newInstance(this);
	} catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		| InvocationTargetException | NoSuchMethodException | SecurityException e) {
	    try {
		return this.getClass().getConstructor().newInstance();
	    } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException
		    | InvocationTargetException | NoSuchMethodException | SecurityException e2) {
		DungeonDiver7.logError(e);
		return null;
	    }
	}
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

    public boolean doLasersPassThrough() {
	return true;
    }

    /**
     *
     * @param x
     * @param y
     */
    public void editorGenerateHook(final int x, final int y) {
	// Do nothing
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

    public DungeonObject editorPropertiesHook() {
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
     * @param z
     */
    public void editorRemoveHook(final int x, final int y, final int z) {
	// Do nothing
    }

    public boolean enabledInBattle() {
	return true;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null || !(obj instanceof final DungeonObject other) || this.friction != other.friction) {
	    return false;
	}
	if (this.pushable != other.pushable || this.solid != other.solid
		|| this.direction != other.direction) {
	    return false;
	}
	if (this.color != other.color || this.material != other.material || this.blocksLOS != other.blocksLOS) {
	    return false;
	}
	return true;
    }

    public BufferedImageIcon getImage() {
	return ObjectImageLoader.load(this.getCacheName(), this.getIdValue());
    }

    public int getBattleBaseID() {
	if (this.enabledInBattle()) {
	    return this.getIdValue();
	}
	return ObjectImageConstants.NONE;
    }

    public String getCacheName() {
	return Integer.toString(this.getIdValue()) + this.getDirectionSuffix() + this.getFrameSuffix();
    }

    public final Colors getColor() {
	return this.color;
    }

    public int getCustomFormat() {
	return 0;
    }

    abstract public int getCustomProperty(int propID);

    public String getCustomText() {
	return null;
    }

    public Color getCustomTextColor() {
	return null;
    }

    public String getDescription() {
	return Strings.objectDescription(this.getIdValue());
    }

    public final Direction getDirection() {
	return this.direction;
    }

    private final String getDirectionSuffix() {
	if (this.hasDirection()) {
	    return Strings.SPACE + DirectionStrings.directionSuffix(this.direction);
	}
	return Strings.EMPTY;
    }

    public final int getFrameNumber() {
	return this.frameNumber;
    }

    private final String getFrameSuffix() {
	if (this.isAnimated()) {
	    return Strings.SPACE + this.frameNumber;
	}
	return Strings.EMPTY;
    }

    public int getHeight() {
	return 1;
    }

    public final String getIdentityName() {
	return this.getLocalColorPrefix() + Strings.objectName(this.getIdValue());
    }

    abstract public int getIdValue();

    abstract public int getLayer();

    private final String getLocalColorPrefix() {
	if (this.hasColor()) {
	    return Strings.color(this.color) + Strings.SPACE;
	}
	return Strings.EMPTY;
    }

    public final int getMaterial() {
	return this.material;
    }

    @Override
    public int getMaximumRequiredQuantity(final Dungeon dungeon) {
	return RandomGenerationRule.NO_LIMIT;
    }

    public final int getMinimumReactionForce() {
	if (this.material == Materials.PLASTIC) {
	    return DungeonObject.PLASTIC_MINIMUM_REACTION_FORCE;
	}
	if (this.material == Materials.METALLIC) {
	    return DungeonObject.METAL_MINIMUM_REACTION_FORCE;
	}
	return DungeonObject.DEFAULT_MINIMUM_REACTION_FORCE;
    }

    @Override
    public int getMinimumRequiredQuantity(final Dungeon dungeon) {
	return RandomGenerationRule.NO_LIMIT;
    }

    public final String getName() {
	return Strings.objectName(this.getIdValue());
    }

    public final DungeonObject getPreviousStateObject() {
	return this.previousState;
    }

    public final DungeonObject getSavedObject() {
	return this.saved;
    }

    public final int getTeamID() {
	return this.teamID;
    }

    private final boolean hasColor() {
	return this.color != null && this.color != Colors._NONE;
    }

    private final boolean hasDirection() {
	return this.direction != null && this.direction != Direction.NONE && this.direction != Direction.NONE;
    }

    public final boolean hasFriction() {
	return this.friction;
    }

    @Override
    public int hashCode() {
	final var prime = 31;
	var result = 1;
	result = prime * result + (this.friction ? 1231 : 1237);
	result = prime * result + (this.pushable ? 1231 : 1237);
	result = prime * result + (this.solid ? 1231 : 1237);
	result = prime * result + (this.timerActive ? 1231 : 1237);
	result = prime * result + this.timerValue;
	result = prime * result + this.direction.hashCode();
	result = prime * result + this.color.ordinal();
	result = prime * result + (this.blocksLOS ? 1231 : 1237);
	return prime * result + this.material;
    }

    public final boolean hasPreviousState() {
	return this.previousState != null;
    }

    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     */
    public void interactAction() {
	SoundLoader.playSound(Sounds.ACTION_FAILED);
	DungeonDiver7.getStuffBag().showMessage("Can't interact with that");
    }

    private final boolean isAnimated() {
	return this.frameNumber > 0;
    }

    public boolean isConditionallySolid() {
	return this.solid;
    }

    public boolean isField() {
	return false;
    }

    public boolean isMoving() {
	return false;
    }

    public boolean isPassThrough() {
	return false;
    }

    public boolean isPlayer() {
	return false;
    }

    public final boolean isPushable() {
	return this.pushable;
    }

    @Override
    public boolean isRequired(final Dungeon dungeon) {
	return false;
    }

    public boolean isSightBlocking() {
	return this.blocksLOS;
    }

    public final boolean isSolid() {
	return this.solid;
    }

    public boolean isSolidInBattle() {
	if (this.enabledInBattle()) {
	    return this.isSolid();
	}
	return false;
    }

    public boolean killsOnMove() {
	return false;
    }

    public void laserDoneAction() {
	// Do nothing
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
		// Do nothing
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

    /**
     *
     * @param locX
     * @param locY
     * @param locZ
     */
    public void moveFailedAction(final int locX, final int locY, final int locZ) {
	SoundLoader.playSound(Sounds.STEP_FAIL);
	DungeonDiver7.getStuffBag().showMessage("Can't go that way");
    }

    public boolean overridesDefaultPostMove() {
	return false;
    }

    public abstract void postMoveAction(final int dirX, final int dirY, int dirZ);

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

    /**
     *
     * @param pushed
     * @param x
     * @param y
     * @param z
     */
    public void pushCollideAction(final DungeonObject pushed, final int x, final int y, final int z) {
	// Do nothing
    }

    protected void pushCrushAction(final int x, final int y, final int z) {
	// Object crushed
	GameLogic.morph(new Empty(), x, y, z, this.getLayer());
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
    public void pushOutAction(final AbstractMovableObject pushed, final int x, final int y, final int z) {
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
	    GameLogic.morph(this.changesToOnExposure(Materials.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.ICE
		&& (this.getMaterial() == Materials.METALLIC || this.getMaterial() == Materials.WOODEN
			|| this.getMaterial() == Materials.PLASTIC)
		&& this.changesToOnExposure(Materials.ICE) != null) {
	    // Freeze metal, wooden, or plastic object
	    GameLogic.morph(this.changesToOnExposure(Materials.ICE), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.FIRE && this.getMaterial() == Materials.ICE
		&& this.changesToOnExposure(Materials.FIRE) != null) {
	    // Melt icy object
	    GameLogic.morph(this.changesToOnExposure(Materials.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.ICE && this.getMaterial() == Materials.FIRE
		&& this.changesToOnExposure(Materials.ICE) != null) {
	    // Cool hot object
	    GameLogic.morph(this.changesToOnExposure(Materials.ICE), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	if (RangeTypes.getMaterialForRangeType(rangeType) == Materials.FIRE && this.getMaterial() == Materials.METALLIC
		&& this.changesToOnExposure(Materials.FIRE) != null) {
	    // Melt metal object
	    GameLogic.morph(this.changesToOnExposure(Materials.FIRE), locX + dirX, locY + dirY, locZ, this.getLayer());
	    return true;
	}
	return false;
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected DungeonObject readHookV2(final DataIOReader reader, final int formatVersion) throws IOException {
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
    protected DungeonObject readHookV3(final DataIOReader reader, final int formatVersion) throws IOException {
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
    protected DungeonObject readHookV4(final DataIOReader reader, final int formatVersion) throws IOException {
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
    protected DungeonObject readHookV5(final DataIOReader reader, final int formatVersion) throws IOException {
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
    protected DungeonObject readHookV6(final DataIOReader reader, final int formatVersion) throws IOException {
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
    protected DungeonObject readHookV7(final DataIOReader reader, final int formatVersion) throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    public final DungeonObject readV2(final DataIOReader reader, final String ident, final int ver) throws IOException {
	if (!ident.equals(this.getCacheName())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == DungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    reader.readInt();
	    this.color = Colors.values()[reader.readInt()];
	    return this.readHookV2(reader, ver);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final DungeonObject readV3(final DataIOReader reader, final String ident, final int ver) throws IOException {
	if (!ident.equals(this.getCacheName())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == DungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = Colors.values()[reader.readInt()];
	    // Discard material
	    reader.readInt();
	    return this.readHookV3(reader, ver);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	// Discard material
	reader.readInt();
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final DungeonObject readV4(final DataIOReader reader, final String ident, final int ver) throws IOException {
	if (!ident.equals(this.getCacheName())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == DungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = Colors.values()[reader.readInt()];
	    return this.readHookV4(reader, ver);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final DungeonObject readV5(final DataIOReader reader, final String ident, final int ver) throws IOException {
	if (!ident.equals(this.getCacheName())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == DungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = Colors.values()[reader.readInt()];
	    return this.readHookV5(reader, ver);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final DungeonObject readV6(final DataIOReader reader, final String ident, final int ver) throws IOException {
	if (!ident.equals(this.getCacheName())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == DungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = Colors.values()[reader.readInt()];
	    return this.readHookV6(reader, ver);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final DungeonObject readV7(final DataIOReader reader, final String ident, final int ver) throws IOException {
	if (!ident.equals(this.getCacheName())) {
	    return null;
	}
	final var cc = this.getCustomFormat();
	if (cc == DungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.direction = Direction.values()[reader.readInt()];
	    this.color = Colors.values()[reader.readInt()];
	    return this.readHookV7(reader, ver);
	}
	this.direction = Direction.values()[reader.readInt()];
	this.color = Colors.values()[reader.readInt()];
	for (var x = 0; x < cc; x++) {
	    final var cx = reader.readInt();
	    this.setCustomProperty(x + 1, cx);
	}
	return this;
    }

    public final void setColor(final Colors col) {
	this.color = col;
    }

    abstract public void setCustomProperty(int propID, int value);

    public final void setDirection(final Direction dir) {
	this.direction = dir;
    }

    public final void setFrameNumber(final int frame) {
	this.frameNumber = frame;
    }

    protected final void setMaterial(final int mat) {
	this.material = mat;
    }

    public final void setPreviousStateObject(final DungeonObject obj) {
	this.previousState = obj;
    }

    public final void setSavedObject(final DungeonObject obj) {
	this.saved = obj;
    }

    public final void setTeamID(final int tid) {
	this.teamID = tid;
    }

    @Override
    public boolean shouldGenerateObject(final Dungeon dungeon, final int row, final int col, final int level,
	    final int layer) {
	if (layer == DungeonConstants.LAYER_LOWER_OBJECTS) {
	    // Handle object layer
	    // Limit generation of other objects to 20%, unless required
	    if (this.isPassThrough() || this.isRequired(dungeon)) {
		return true;
	    }
	    final var r = new RandomRange(1, 100);
	    if (r.generate() <= 20) {
		return true;
	    }
	    return false;
	}
	if (!this.isField()) {
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

    public boolean solvesOnMove() {
	return false;
    }

    public final void tickTimer(final int dirX, final int dirY, final int actionType) {
	if (this.timerActive && this.acceptsTick(actionType)) {
	    this.timerValue--;
	    if (this.timerValue == 0) {
		this.timerActive = false;
		this.timerExpiredAction(dirX, dirY);
	    }
	}
    }

    /**
     *
     * @param dirX
     * @param dirY
     */
    public void timerExpiredAction(final int dirX, final int dirY) {
	// Do nothing
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

    public final void toggleDirection() {
	this.direction = DungeonConstants.nextDirOrtho(this.direction);
    }

    public final void toggleFrameNumber() {
	if (this.isAnimated()) {
	    this.frameNumber++;
	    if (this.frameNumber > 3) {
		this.frameNumber = 1;
	    }
	}
    }

    public final void write(final DataIOWriter writer) throws IOException {
	writer.writeString(this.getCacheName());
	final var cc = this.getCustomFormat();
	if (cc == DungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    writer.writeInt(this.direction.ordinal());
	    writer.writeInt(this.color.ordinal());
	    this.writeHook(writer);
	} else {
	    writer.writeInt(this.direction.ordinal());
	    writer.writeInt(this.color.ordinal());
	    for (var x = 0; x < cc; x++) {
		final var cx = this.getCustomProperty(x + 1);
		writer.writeInt(cx);
	    }
	}
    }

    /**
     *
     * @param writer
     * @throws IOException
     */
    protected void writeHook(final DataIOWriter writer) throws IOException {
	// Do nothing - but let subclasses override
    }
}
