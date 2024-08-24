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
    private static final int COUNTER_LAYER = 0;
    private static final int COUNTER_HEIGHT = 1;
    private static final int COUNTER_DAMAGE = 2;
    private static final int COUNTER_TIMER = 3;
    private static final int COUNTER_INITIAL_TIMER = 4;
    private static final int COUNTER_FRAME = 5;
    private static final int COUNTER_MAX_FRAME = 6;
    private static final int MAX_COUNTERS = 7;
    private static final int FLAG_SOLID = 0;
    private static final int FLAG_FRICTION = 1;
    private static final int FLAG_SIGHT_BLOCK = 2;
    private static final int FLAG_INTERACT = 3;
    private static final int FLAG_PUSH = 4;
    private static final int FLAG_PULL = 5;
    private static final int FLAG_MOVING = 6;
    private static final int FLAG_PLAYER = 7;
    private static final int FLAG_TIMER_ACTIVE = 8;
    private static final int MAX_FLAGS = 9;
    private final ObjectModel model;
    private final ObjectImageId id;
    private BufferedImageIcon image;
    private String interactMessage;
    private int interactMessageIndex;
    private Sounds interactSound;
    private ObjectImageId interactMorph;
    private ShopType shop;
    private boolean lazyLoaded;
    private transient int teamId;
    private boolean imageOverridden;
    private Direction direction;
    private Colors color;

    public GameObject(final ObjectImageId oid) {
	this.id = oid;
	this.imageOverridden = false;
	this.model = new ObjectModel();
	this.model.setId(oid);
	this.lazyLoaded = false;
	this.model.addCounters(MAX_COUNTERS);
	this.model.addFlags(MAX_FLAGS);
    }

    public final void activateTimer(final int ticks) {
	this.model.setFlag(FLAG_TIMER_ACTIVE, true);
	this.model.setCounter(COUNTER_TIMER, ticks);
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
	return this.model.getCounter(COUNTER_DAMAGE);
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
	return this.model.getCounter(COUNTER_HEIGHT);
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
	return this.model.getCounter(COUNTER_LAYER);
    }

    public final String getName() {
	return Strings.objectName(this.id.ordinal());
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
	return this.model.getFlag(FLAG_FRICTION);
    }

    public final boolean isAnimated() {
	this.lazyLoad();
	return this.model.getCounter(COUNTER_MAX_FRAME) > 0;
    }

    public final boolean isDamaging() {
	this.lazyLoad();
	return this.model.getCounter(COUNTER_DAMAGE) > 0;
    }

    public final boolean isInteractive() {
	this.lazyLoad();
	return this.model.getFlag(FLAG_INTERACT);
    }

    public final boolean isMoving() {
	this.lazyLoad();
	return this.model.getFlag(FLAG_MOVING);
    }

    public final boolean isPlayer() {
	this.lazyLoad();
	return this.model.getFlag(FLAG_PLAYER);
    }

    public final boolean isPullable() {
	this.lazyLoad();
	return this.model.getFlag(FLAG_PULL);
    }

    public final boolean isPushable() {
	this.lazyLoad();
	return this.model.getFlag(FLAG_PUSH);
    }

    public final boolean isShop() {
	return this.getShopType() != null;
    }

    public final boolean isSightBlocking() {
	this.lazyLoad();
	return this.model.getFlag(FLAG_SIGHT_BLOCK);
    }

    public final boolean isSolid() {
	this.lazyLoad();
	return this.model.getFlag(FLAG_SOLID);
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
	    this.model.setFlag(FLAG_SOLID, GameObjectDataLoader.solid(this.id));
	    this.model.setFlag(FLAG_FRICTION, GameObjectDataLoader.friction(this.id));
	    this.model.setFlag(FLAG_SIGHT_BLOCK, GameObjectDataLoader.sightBlocking(this.id));
	    this.model.setFlag(FLAG_INTERACT, GameObjectDataLoader.isInteractive(this.id));
	    this.model.setFlag(FLAG_PUSH, GameObjectDataLoader.pushable(this.id));
	    this.model.setFlag(FLAG_PULL, GameObjectDataLoader.pullable(this.id));
	    this.model.setFlag(FLAG_MOVING, GameObjectDataLoader.isMoving(this.id));
	    this.model.setFlag(FLAG_PLAYER, GameObjectDataLoader.isPlayer(this.id));
	    this.model.setCounter(COUNTER_LAYER, GameObjectDataLoader.layer(this.id));
	    this.model.setCounter(COUNTER_HEIGHT, GameObjectDataLoader.height(this.id));
	    this.model.setCounter(COUNTER_DAMAGE, GameObjectDataLoader.damage(this.id));
	    this.model.setCounter(COUNTER_INITIAL_TIMER, GameObjectDataLoader.initialTimer(this.id));
	    this.model.setCounter(COUNTER_MAX_FRAME, GameObjectDataLoader.maxFrame(this.id));
	    this.lazyLoaded = true;
	}
    }

    public final void overrideImage(final BufferedImageIcon imageOverride) {
	this.imageOverridden = true;
	this.image = imageOverride;
    }

    public final void setTeamID(final int tid) {
	this.teamId = tid;
    }

    public final void tickTimer() {
	if (this.model.getFlag(FLAG_TIMER_ACTIVE)) {
	    this.model.decrementCounter(COUNTER_TIMER);
	    if (this.model.getCounter(COUNTER_TIMER) == 0) {
		this.model.setFlag(FLAG_TIMER_ACTIVE, false);
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
	var maxFrame = this.model.getCounter(COUNTER_MAX_FRAME);
	var frame = this.model.getCounter(COUNTER_FRAME);
	if (this.isAnimated()) {
	    frame++;
	    if (frame > maxFrame) {
		frame = 0;
	    }
	    this.model.setCounter(COUNTER_FRAME, frame);
	}
    }
}
