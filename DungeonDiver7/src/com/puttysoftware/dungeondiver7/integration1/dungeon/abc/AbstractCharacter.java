/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.dungeon.abc;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.TypeConstants;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;

public abstract class AbstractCharacter extends AbstractDungeonObject {
    // Fields
    private AbstractDungeonObject savedObject;

    // Constructors
    protected AbstractCharacter() {
	super(false, false);
	this.savedObject = new Empty();
	this.type.set(TypeConstants.TYPE_CHARACTER);
    }

    // Methods
    @Override
    public void postMoveAction(final int dirX, final int dirY, int dirZ) {
	// Do nothing
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_VIRTUAL_CHARACTER;
    }

    @Override
    public int getCustomFormat() {
	return AbstractDungeonObject.CUSTOM_FORMAT_MANUAL_OVERRIDE;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return AbstractDungeonObject.DEFAULT_CUSTOM_VALUE;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	// Do nothing
    }

    @Override
    protected void writeHook(final FileIOWriter writer) throws IOException {
	this.savedObject.write(writer);
    }

    @Override
    protected AbstractDungeonObject readHookV7(final FileIOReader reader, final int formatVersion)
	    throws IOException {
	this.savedObject = Integration1.getApplication().getObjects().read(reader, formatVersion);
	return this;
    }
}
