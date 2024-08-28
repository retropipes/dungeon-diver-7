package org.retropipes.dungeondiver7.gameobject;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.diane.direction.Direction;
import org.retropipes.diane.objectmodel.ObjectModel;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageId;
import org.retropipes.dungeondiver7.loader.image.gameobject.ObjectImageLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.locale.Colors;
import org.retropipes.dungeondiver7.locale.ObjectInteractMessage;
import org.retropipes.dungeondiver7.locale.Strings;

public final class GameObject {
    private final ObjectModel model;
    private final ObjectImageId id;
    private transient boolean solid;
    private transient boolean friction;
    private transient boolean sightBlock;
    private transient boolean interactive;
    private transient boolean canMove;
    private transient boolean canPush;
    private transient boolean canPull;
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
    private transient Direction direction;
    private transient Colors color;
    private transient ObjectImageId saved;

    public GameObject(final ObjectImageId oid) {
	this.id = oid;
	this.imageOverridden = false;
	this.model = new ObjectModel();
	this.model.setId(oid);
	this.lazyLoaded = false;
    }

    private GameObject(final ObjectImageId oid, final ObjectImageId savedOid) {
	this.id = oid;
	this.saved = savedOid;
	this.imageOverridden = false;
	this.model = new ObjectModel();
	this.model.setId(oid);
	this.lazyLoaded = false;
    }

    public final void activateTimer(final int ticks) {
	this.timerActive = true;
	this.timerValue = ticks;
    }

    public final boolean canMove() {
	return this.isMoving() || this.isPlayer() || this.isPullable() || this.isPushable();
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
	return Integer.toString(this.id.ordinal());
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

    public final int getHeight() {
	this.lazyLoad();
	return this.blockHeight;
    }

    public final ObjectImageId getId() {
	return this.id;
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

    public final String getName() {
	return Strings.objectName(this.id.ordinal());
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

    public final boolean isAnimated() {
	this.lazyLoad();
	return this.maxFrameNumber > 0;
    }

    public final boolean isDamaging() {
	this.lazyLoad();
	return this.damageDealt > 0;
    }

    public final boolean isInteractive() {
	this.lazyLoad();
	return this.interactive;
    }

    public final boolean isMoving() {
	this.lazyLoad();
	return this.canMove;
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
	    this.isPlayer = GameObjectDataLoader.isPlayer(this.id);
	    this.layer = GameObjectDataLoader.layer(this.id);
	    this.blockHeight = GameObjectDataLoader.height(this.id);
	    this.damageDealt = GameObjectDataLoader.damage(this.id);
	    this.timerValue = GameObjectDataLoader.initialTimer(this.id);
	    this.maxFrameNumber = GameObjectDataLoader.maxFrame(this.id);
	    this.lazyLoaded = true;
	}
    }

    public final void overrideImage(final BufferedImageIcon imageOverride) {
	this.imageOverridden = true;
	this.image = imageOverride;
    }

    public final void setSavedObject(final GameObject savedObject) {
	this.saved = savedObject.getId();
    }

    public final void setTeamID(final int tid) {
	this.teamId = tid;
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
}
