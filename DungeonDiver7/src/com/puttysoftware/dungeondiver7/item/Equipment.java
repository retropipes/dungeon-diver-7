/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.item;

import java.io.IOException;

import com.puttysoftware.dungeondiver7.locale.Slot;
import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;

public class Equipment extends Item {
    // Properties
    private final int materialID;
    private final Slot slotUsed;
    private final int hitSound;

    // Constructors
    Equipment(final String itemName, final int buyFor, final int grams, final int power, final Slot slot,
	    final int newMaterialID, final int hitSoundID) {
	super(itemName, buyFor, grams, power);
	this.materialID = newMaterialID;
	this.slotUsed = slot;
	this.hitSound = hitSoundID;
    }

    Equipment(final Equipment e) {
	super(e);
	this.materialID = e.materialID;
	this.slotUsed = e.slotUsed;
	this.hitSound = e.hitSound;
    }

    // Methods
    @Override
    public int hashCode() {
	final var prime = 31;
	var result = super.hashCode();
	result = prime * result + this.slotUsed.ordinal();
	return prime * result + this.materialID;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!super.equals(obj) || !(obj instanceof final Equipment other) || this.slotUsed != other.slotUsed
		|| this.materialID != other.materialID) {
	    return false;
	}
	return true;
    }

    public final int getHitSound() {
	return this.hitSound;
    }

    public final Slot getSlotUsed() {
	return this.slotUsed;
    }

    public final int getMaterial() {
	return this.materialID;
    }

    static Equipment readEquipment(final FileIOReader dr) throws IOException {
	final var i = Item.readItem(dr);
	if (i == null) {
	    // Abort
	    return null;
	}
	final var matID = dr.readInt();
	final var slot = Slot.values()[dr.readInt()];
	final var hs = dr.readInt();
	return new Equipment(i.getName(), i.getBuyPrice(), i.getWeight(), i.getPotency(), slot, matID, hs);
    }

    final void writeEquipment(final FileIOWriter dw) throws IOException {
	super.writeItem(dw);
	dw.writeInt(this.materialID);
	dw.writeInt(this.slotUsed.ordinal());
	dw.writeInt(this.hitSound);
    }
}
