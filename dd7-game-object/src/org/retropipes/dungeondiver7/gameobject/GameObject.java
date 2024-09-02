package org.retropipes.dungeondiver7.gameobject;

import java.io.IOException;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.direction.DirectionStrings;
import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;
import org.retropipes.diane.objectmodel.ObjectModel;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageId;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.locale.Colors;
import org.retropipes.dungeondiver7.locale.ObjectInteractMessage;
import org.retropipes.dungeondiver7.locale.Strings;

public final class GameObject {
    private static final int PLASTIC_MINIMUM_REACTION_FORCE = 0;
    private static final int DEFAULT_MINIMUM_REACTION_FORCE = 1;
    private static final int METAL_MINIMUM_REACTION_FORCE = 2;
    private final ObjectModel model;
    private final ObjectImageId id;
    private transient boolean solid;
    private transient boolean friction;
    private transient boolean sightBlock;
    private transient boolean interactive;
    private transient boolean canMove;
    private transient boolean canPush;
    private transient boolean canPull;
    private transient boolean isField;
    private transient boolean isPassThrough;
    private transient boolean isPlayer;
    private transient int layer;
    private transient int blockHeight;
    private transient int damageDealt;
    private transient int timerValue;
    private transient int frameNumber;
    private transient int maxFrameNumber;
    private transient int interactMessageIndex;
    private transient BufferedImageIcon image;
    private transient String interactMessage;
    private transient Sounds interactSound;
    private transient ObjectImageId interactMorph;
    private transient ShopType shop;
    private transient boolean lazyLoaded;
    private transient int teamId;
    private transient boolean imageOverridden;
    private transient boolean timerActive;
    private transient boolean deferSetProperties;
    private transient boolean killsOnMove;
    private transient boolean solvesOnMove;
    private transient Direction direction;
    private transient Colors color;
    private transient Material material;
    private transient ObjectImageId saved;
    private transient ObjectImageId bound;
    private transient ObjectImageId previousState;
    private transient int boundX;
    private transient int boundY;
    private transient boolean triggered;

    public GameObject(final ObjectImageId oid) {
	this.id = oid;
	this.imageOverridden = false;
	this.triggered = false;
	this.model = new ObjectModel();
	this.model.setId(oid);
	this.lazyLoaded = false;
    }

    private GameObject(final ObjectImageId oid, final ObjectImageId savedOid) {
	this.id = oid;
	this.saved = savedOid;
	this.imageOverridden = false;
	this.triggered = false;
	this.model = new ObjectModel();
	this.model.setId(oid);
	this.lazyLoaded = false;
    }

    public static final int getImbuedRangeForce(final Material material) {
	if (material == Material.PLASTIC) {
	    return PLASTIC_MINIMUM_REACTION_FORCE;
	}
	if (material == Material.METALLIC) {
	    return METAL_MINIMUM_REACTION_FORCE;
	}
	return DEFAULT_MINIMUM_REACTION_FORCE;
    }

    public static GameObject read(final DataIOReader reader) throws IOException {
	int nid = reader.readInt();
	return new GameObject(ObjectImageId.values()[nid]);
    }

    public final void activateTimer(final int ticks) {
	this.timerActive = true;
	this.timerValue = ticks;
    }

    public final boolean canMove() {
	return this.isMoving() || this.isPlayer() || this.isPullable() || this.isPushable();
    }

    public final boolean canMoveBoxes() {
	return false;
    }

    public final boolean canMoveMirrors() {
	return false;
    }

    public final boolean canMoveParty() {
	return false;
    }

    public final boolean canShoot() {
	return false;
    }

    public GameObject changesToOnExposure(final Material materialID) {
	return this;
    }

    public final boolean defersSetProperties() {
	this.lazyLoad();
	return this.deferSetProperties;
    }

    public GameObject editorPropertiesHook() {
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

    public final String getCacheName() {
	return Integer.toString(this.id.ordinal()) + this.getDirectionSuffix() + this.getFrameSuffix();
    }

    public final Colors getColor() {
	this.lazyLoad();
	return this.color;
    }

    public final int getDamage() {
	this.lazyLoad();
	return this.damageDealt;
    }

    public final String getDescription() {
	return Strings.objectDescription(this.id.ordinal());
    }

    public final Direction getDirection() {
	this.lazyLoad();
	return this.direction;
    }

    private final String getDirectionSuffix() {
	if (this.hasDirection()) {
	    return Strings.SPACE + DirectionStrings.directionSuffix(this.direction);
	}
	return Strings.EMPTY;
    }

    public final int getFrameNumber() {
	this.lazyLoad();
	return this.frameNumber;
    }

    private final String getFrameSuffix() {
	if (this.isAnimated()) {
	    return Strings.SPACE + this.frameNumber;
	}
	return Strings.EMPTY;
    }

    public final int getHeight() {
	this.lazyLoad();
	return this.blockHeight;
    }

    public final ObjectImageId getId() {
	return this.id;
    }

    public final String getIdentityName() {
	return this.getLocalColorPrefix() + Strings.objectName(this.getIdValue());
    }

    public final int getIdValue() {
	return this.id.ordinal();
    }

    public final BufferedImageIcon getImage() {
	this.lazyLoad();
	return this.image;
    }

    public final String getInteractMessage() {
	this.lazyLoad();
	return this.interactMessage;
    }

    public final ObjectImageId getInteractMorph() {
	this.lazyLoad();
	return this.interactMorph;
    }

    public final Sounds getInteractSound() {
	this.lazyLoad();
	return this.interactSound;
    }

    public final int getLayer() {
	this.lazyLoad();
	return this.layer;
    }

    private final String getLocalColorPrefix() {
	if (this.hasColor()) {
	    return Strings.color(this.color) + Strings.SPACE;
	}
	return Strings.EMPTY;
    }

    public final Material getMaterial() {
	this.lazyLoad();
	return this.material;
    }

    public final String getName() {
	return Strings.objectName(this.id.ordinal());
    }

    public final GameObject getBoundObject() {
	this.lazyLoad();
	if (this.bound == null || this.bound == this.id) {
	    return this;
	}
	return new GameObject(this.bound, this.id);
    }

    public final int getBoundObjectX() {
	return this.boundX;
    }

    public final int getBoundObjectY() {
	return this.boundY;
    }

    public final GameObject getPreviousStateObject() {
	if (this.previousState == null || this.previousState == this.id) {
	    return this;
	}
	return new GameObject(this.previousState, this.id);
    }

    public final GameObject getSavedObject() {
	if (this.saved == null || this.saved == this.id) {
	    return this;
	}
	return new GameObject(this.saved, this.id);
    }

    public final ShopType getShopType() {
	this.lazyLoad();
	return this.shop;
    }

    public final int getTeamID() {
	return this.teamId;
    }

    private final boolean hasColor() {
	return this.color != null && this.color != Colors._NONE;
    }

    private final boolean hasDirection() {
	return this.direction != null && this.direction != Direction.NONE;
    }

    public final boolean hasFriction() {
	this.lazyLoad();
	return this.friction;
    }

    public final boolean hasSameBoundObject(final GameObject testObject) {
	if (this.bound == null && testObject.bound == null) {
	    return true;
	}
	return this.bound == testObject.bound;
    }

    public final boolean isAnimated() {
	this.lazyLoad();
	return this.maxFrameNumber > 0;
    }

    public final boolean isDamaging() {
	this.lazyLoad();
	return this.damageDealt > 0;
    }

    public final boolean isField() {
	this.lazyLoad();
	return this.isField;
    }

    public final boolean isInteractive() {
	this.lazyLoad();
	return this.interactive;
    }

    public final boolean isMoving() {
	this.lazyLoad();
	return this.canMove;
    }

    public final boolean isPassThrough() {
	this.lazyLoad();
	return this.isPassThrough;
    }

    public final boolean isPlayer() {
	this.lazyLoad();
	return this.isPlayer;
    }

    public final boolean isPullable() {
	this.lazyLoad();
	return this.canPull;
    }

    public final boolean isPushable() {
	this.lazyLoad();
	return this.canPush;
    }

    public final boolean isShop() {
	return this.getShopType() != null;
    }

    public final boolean isSightBlocking() {
	this.lazyLoad();
	return this.sightBlock;
    }

    public final boolean isSolid() {
	this.lazyLoad();
	return this.solid;
    }

    public boolean isTriggered() {
	return this.triggered;
    }

    public boolean killsOnMove() {
	this.lazyLoad();
	return this.killsOnMove;
    }

    private void lazyLoad() {
	if (!this.lazyLoaded) {
	    if (!this.imageOverridden) {
		this.image = ObjectImageLoader.load(this.id);
	    }
	    this.interactMessageIndex = GameObjectDataLoader.interactionMessageIndex(this.id);
	    this.interactMessage = Strings
		    .objectInteractMessage(ObjectInteractMessage.values()[this.interactMessageIndex]);
	    this.interactMorph = GameObjectDataLoader.interactionMorph(this.id);
	    this.interactSound = GameObjectDataLoader.interactionSound(this.id);
	    this.color = GameObjectDataLoader.color(this.id);
	    this.direction = GameObjectDataLoader.direction(this.id);
	    this.shop = GameObjectDataLoader.shopType(this.id);
	    this.solid = GameObjectDataLoader.solid(this.id);
	    this.friction = GameObjectDataLoader.friction(this.id);
	    this.sightBlock = GameObjectDataLoader.sightBlocking(this.id);
	    this.interactive = GameObjectDataLoader.isInteractive(this.id);
	    this.canPush = GameObjectDataLoader.pushable(this.id);
	    this.canPull = GameObjectDataLoader.pullable(this.id);
	    this.canMove = GameObjectDataLoader.isMoving(this.id);
	    this.isField = GameObjectDataLoader.isField(this.id);
	    this.isPlayer = GameObjectDataLoader.isPlayer(this.id);
	    this.layer = GameObjectDataLoader.layer(this.id);
	    this.blockHeight = GameObjectDataLoader.height(this.id);
	    this.damageDealt = GameObjectDataLoader.damage(this.id);
	    this.timerValue = GameObjectDataLoader.initialTimer(this.id);
	    this.maxFrameNumber = GameObjectDataLoader.maxFrame(this.id);
	    // this.bound = ...
	    // this.deferSetProperties = ...
	    // this.killsOnMove = ...
	    // this.solvesOnMove = ...
	    // this.isPassThrough = ...
	    // this.material = ...
	    this.lazyLoaded = true;
	}
    }

    public final void overrideImage(final BufferedImageIcon imageOverride) {
	this.imageOverridden = true;
	this.image = imageOverride;
    }

    public final void setBoundObjectX(final int newBX) {
	this.boundX = newBX;
    }

    public final void setBoundObjectY(final int newBY) {
	this.boundY = newBY;
    }

    public final void setPreviousStateObject(final GameObject savedObject) {
	this.previousState = savedObject.getId();
    }

    public final void setSavedObject(final GameObject savedObject) {
	this.saved = savedObject.getId();
    }

    public final void setTeamID(final int tid) {
	this.teamId = tid;
    }

    public final void setTriggered(final boolean isTriggered) {
	this.triggered = isTriggered;
    }

    public boolean solvesOnMove() {
	this.lazyLoad();
	return this.solvesOnMove;
    }

    public final void tickTimer() {
	if (this.timerActive) {
	    this.timerValue--;
	    if (this.timerValue == 0) {
		this.timerActive = false;
		// Time's up!
	    }
	}
    }

    private final void toggleColor() {
	var maxColor = Colors.values().length;
	if (this.hasColor()) {
	    var oldColorValue = this.color.ordinal();
	    final var newColorValue = oldColorValue;
	    oldColorValue++;
	    Colors newColor;
	    if (newColorValue >= maxColor) {
		newColor = Colors.GRAY;
	    } else {
		newColor = Colors.values()[newColorValue];
	    }
	    this.color = newColor;
	}
    }

    public final void toggleDirection() {
	this.direction = GameObjectDirectionsHelper.nextDirOrtho(this.direction);
    }

    public final void toggleFrameNumber() {
	if (this.isAnimated()) {
	    this.frameNumber++;
	    if (this.frameNumber > this.maxFrameNumber) {
		this.frameNumber = 0;
	    }
	}
    }

    public final void write(final DataIOWriter writer) throws IOException {
	writer.writeInt(this.id.ordinal());
    }
}
