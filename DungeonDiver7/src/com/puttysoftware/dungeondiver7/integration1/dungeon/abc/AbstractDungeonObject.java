/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.abc;

import java.io.IOException;
import java.util.BitSet;

import com.puttysoftware.dungeondiver7.dungeon.utility.ImageColorConstants;
import com.puttysoftware.dungeondiver7.dungeon.utility.RandomGenerationRule;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.dungeon.CurrentDungeon;
import com.puttysoftware.dungeondiver7.loader.BattleImageManager;
import com.puttysoftware.dungeondiver7.loader.ObjectImageConstants;
import com.puttysoftware.dungeondiver7.loader.ObjectImageManager;
import com.puttysoftware.dungeondiver7.loader.SoundConstants;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.manager.dungeon.FormatConstants;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;
import com.puttysoftware.images.BufferedImageIcon;
import com.puttysoftware.randomrange.RandomRange;
import com.puttysoftware.storage.CloneableObject;

public abstract class AbstractDungeonObject extends CloneableObject implements RandomGenerationRule {
    // Properties
    private boolean solid;
    private boolean friction;
    private int timerValue;
    private int initialTimerValue;
    private boolean timerActive;
    protected BitSet type;
    private final boolean blocksLOS;
    private static int templateColor = ImageColorConstants.COLOR_NONE;
    private AbstractDungeonObject saved;
    public static final int DEFAULT_CUSTOM_VALUE = 0;
    protected static final int CUSTOM_FORMAT_MANUAL_OVERRIDE = -1;

    // Constructors
    public AbstractDungeonObject(final boolean isSolid, final boolean sightBlock) {
	this.solid = isSolid;
	this.friction = true;
	this.blocksLOS = sightBlock;
	this.type = new BitSet(TypeConstants.TYPES_COUNT);
	this.timerValue = 0;
	this.initialTimerValue = 0;
	this.timerActive = false;
    }

    public AbstractDungeonObject(final boolean isSolid, final boolean hasFriction, final boolean sightBlock) {
	this.solid = isSolid;
	this.friction = hasFriction;
	this.blocksLOS = sightBlock;
	this.type = new BitSet(TypeConstants.TYPES_COUNT);
	this.timerValue = 0;
	this.initialTimerValue = 0;
	this.timerActive = false;
    }

    public AbstractDungeonObject() {
	this.solid = false;
	this.friction = true;
	this.blocksLOS = false;
	this.type = new BitSet(TypeConstants.TYPES_COUNT);
	this.timerValue = 0;
	this.initialTimerValue = 0;
	this.timerActive = false;
    }

    // Methods
    @Override
    public AbstractDungeonObject clone() {
	final AbstractDungeonObject copy = (AbstractDungeonObject) super.clone();
	copy.solid = this.solid;
	copy.friction = this.friction;
	copy.type = (BitSet) this.type.clone();
	copy.timerValue = this.timerValue;
	copy.initialTimerValue = this.initialTimerValue;
	copy.timerActive = this.timerActive;
	copy.type = (BitSet) this.type.clone();
	return copy;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (this.friction ? 1231 : 1237);
	result = prime * result + (this.solid ? 1231 : 1237);
	result = prime * result + this.timerValue;
	result = prime * result + this.initialTimerValue;
	result = prime * result + (this.timerActive ? 1231 : 1237);
	return prime * result + (this.type == null ? 0 : this.type.hashCode());
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
	if (this.timerActive != other.timerActive) {
	    return false;
	}
	if (this.timerValue != other.timerValue) {
	    return false;
	}
	if (this.initialTimerValue != other.initialTimerValue) {
	    return false;
	}
	return true;
    }

    public AbstractDungeonObject getSavedObject() {
	if (this.saved == null) {
	    throw new NullPointerException("Saved object == NULL!");
	}
	return this.saved;
    }

    public void setSavedObject(final AbstractDungeonObject newSaved) {
	if (newSaved == null) {
	    throw new IllegalArgumentException("New saved object == NULL!");
	}
	this.saved = newSaved;
    }

    public boolean isSolid() {
	return this.solid;
    }

    public boolean isSolidInBattle() {
	if (this.enabledInBattle()) {
	    return this.isSolid();
	} else {
	    return false;
	}
    }

    public boolean isSightBlocking() {
	return this.blocksLOS;
    }

    public boolean isOfType(final int testType) {
	return this.type.get(testType);
    }

    public boolean hasFriction() {
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
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     */
    public void moveFailedAction(final int dirX, final int dirY, int dirZ) {
	SoundLoader.playSound(SoundConstants.FAILED);
	Integration1.getApplication().showMessage("Can't go that way");
    }

    /**
     *
     * @param ie
     * @param dirX
     * @param dirY
     * @param inv
     */
    public void interactAction() {
	SoundLoader.playSound(SoundConstants.FAILED);
	Integration1.getApplication().showMessage("Can't interact with that");
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
     */
    public void determineCurrentAppearance(final int x, final int y) {
	// Do nothing
    }

    public final void activateTimer(final int ticks) {
	this.timerActive = true;
	this.timerValue = ticks;
	this.initialTimerValue = ticks;
    }

    public final void tickTimer(final int dirX, final int dirY) {
	if (this.timerActive) {
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
     * @param dirX
     * @param dirY
     */
    public void timerExpiredAction(final int dirX, final int dirY) {
	// Do nothing
    }

    abstract public String getName();

    public abstract int getBaseID();

    public int getBattleBaseID() {
	if (this.enabledInBattle()) {
	    return this.getBaseID();
	} else {
	    return ObjectImageConstants.NONE;
	}
    }

    public boolean enabledInBattle() {
	return true;
    }

    public final String getIdentifier() {
	return this.getName();
    }

    abstract public String getPluralName();

    abstract public String getDescription();

    abstract public int getLayer();

    abstract public int getCustomProperty(int propID);

    abstract public void setCustomProperty(int propID, int value);

    public int getCustomFormat() {
	return 0;
    }

    @Override
    public boolean shouldGenerateObject(final CurrentDungeon dungeon, final int row, final int col, final int level,
	    final int layer) {
	if (layer == DungeonConstants.LAYER_LOWER_OBJECTS) {
	    // Handle object layer
	    if (!this.isOfType(TypeConstants.TYPE_PASS_THROUGH)) {
		// Limit generation of other objects to 20%, unless required
		if (this.isRequired(dungeon)) {
		    return true;
		} else {
		    final RandomRange r = new RandomRange(1, 100);
		    if (r.generate() <= 20) {
			return true;
		    } else {
			return false;
		    }
		}
	    } else {
		// Generate pass-through objects at 100%
		return true;
	    }
	} else {
	    // Handle ground layer
	    if (this.isOfType(TypeConstants.TYPE_FIELD)) {
		// Limit generation of fields to 20%
		final RandomRange r = new RandomRange(1, 100);
		if (r.generate() <= 20) {
		    return true;
		} else {
		    return false;
		}
	    } else {
		// Generate other ground at 100%
		return true;
	    }
	}
    }

    @Override
    public int getMinimumRequiredQuantity(final CurrentDungeon dungeon) {
	return RandomGenerationRule.NO_LIMIT;
    }

    @Override
    public int getMaximumRequiredQuantity(final CurrentDungeon dungeon) {
	return RandomGenerationRule.NO_LIMIT;
    }

    @Override
    public boolean isRequired(final CurrentDungeon dungeon) {
	return false;
    }

    public final void write(final FileIOWriter writer) throws IOException {
	writer.writeString(this.getIdentifier());
	if (this.saved == null) {
	    writer.writeString("NULL");
	} else {
	    this.saved.write(writer);
	}
	final int cc = this.getCustomFormat();
	if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
	    this.writeHook(writer);
	} else {
	    for (int x = 0; x < cc; x++) {
		final int cx = this.getCustomProperty(x + 1);
		writer.writeInt(cx);
	    }
	}
    }

    public final AbstractDungeonObject readV7(final FileIOReader reader, final String ident) throws IOException {
	if (ident.equals(this.getIdentifier())) {
	    final String savedIdent = reader.readString();
	    if (!savedIdent.equals("NULL")) {
		this.saved = Integration1.getApplication().getObjects().readSavedGameObject(reader, savedIdent,
			FormatConstants.MAZE_FORMAT_LATEST);
	    }
	    final int cc = this.getCustomFormat();
	    if (cc == AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE) {
		return this.readHookV7(reader, FormatConstants.MAZE_FORMAT_LATEST);
	    } else {
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
    protected void writeHook(final FileIOWriter writer) throws IOException {
	// Do nothing - but let subclasses override
    }

    /**
     *
     * @param reader
     * @param formatVersion
     * @return
     * @throws IOException
     */
    protected AbstractDungeonObject readHookV7(final FileIOReader reader, final int formatVersion)
	    throws IOException {
	// Dummy implementation, subclasses can override
	return this;
    }

    public boolean isMoving() {
	return false;
    }
}