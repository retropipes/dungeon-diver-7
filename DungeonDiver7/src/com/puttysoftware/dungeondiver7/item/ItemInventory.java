/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.item;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import com.puttysoftware.dungeondiver7.creature.AbstractCreature;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.locale.Slot;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.diane.fileio.DataIOReader;
import com.puttysoftware.diane.fileio.DataIOWriter;

public class ItemInventory {
    // Properties
    private final Equipment[] equipment;

    // Constructors
    public ItemInventory() {
        this.equipment = new Equipment[Strings.SLOTS_COUNT];
    }

    public void resetInventory() {
        Arrays.fill(this.equipment, null);
    }

    public void equip(final AbstractCreature pc, final Equipment ei, final boolean playSound) {
        // Fix character load, changing gear
        if (this.equipment[ei.getSlotUsed().ordinal()] != null) {
            pc.offsetLoad(-this.equipment[ei.getSlotUsed().ordinal()].getWeight());
        }
        pc.offsetLoad(ei.getWeight());
        // Equip it
        this.equipment[ei.getSlotUsed().ordinal()] = ei;
        if (playSound) {
            SoundLoader.playSound(Sounds.EQUIP);
        }
    }

    public Sounds getWeaponHitSound(final AbstractCreature pc) {
        final var weapon = this.equipment[Slot.WEAPON.ordinal()];
        if (weapon != null) {
            return weapon.getHitSound();
        }
        if (pc.getTeamID() == AbstractCreature.TEAM_PARTY) {
            return Sounds.ATTACK_HIT;
        }
        return Sounds.MONSTER_HIT;
    }

    public String[] generateEquipmentStringArray() {
        final var result = new String[this.equipment.length];
        StringBuilder sb;
        for (var x = 0; x < result.length - 1; x++) {
            sb = new StringBuilder();
            sb.append(Strings.slot(x));
            sb.append(": ");
            if (this.equipment[x] == null) {
                sb.append("Nothing (0)");
            } else {
                sb.append(this.equipment[x].getName());
                sb.append(" (");
                sb.append(this.equipment[x].getPotency());
                sb.append(")");
            }
            result[x] = sb.toString();
        }
        return result;
    }

    public int getTotalPower() {
        var total = 0;
        if (this.equipment[Slot.WEAPON.ordinal()] != null) {
            total += this.equipment[Slot.WEAPON.ordinal()].getPotency();
        }
        return total;
    }

    public int getTotalAbsorb() {
        var total = 0;
        if (this.equipment[Slot.HEAD.ordinal()] != null) {
            total += this.equipment[Slot.HEAD.ordinal()].getPotency();
        }
        if (this.equipment[Slot.NECK.ordinal()] != null) {
            total += this.equipment[Slot.NECK.ordinal()].getPotency();
        }
        if (this.equipment[Slot.ARMS.ordinal()] != null) {
            total += this.equipment[Slot.ARMS.ordinal()].getPotency();
        }
        if (this.equipment[Slot.HANDS.ordinal()] != null) {
            total += this.equipment[Slot.HANDS.ordinal()].getPotency();
        }
        if (this.equipment[Slot.BODY.ordinal()] != null) {
            total += this.equipment[Slot.BODY.ordinal()].getPotency();
        }
        if (this.equipment[Slot.FEET.ordinal()] != null) {
            total += this.equipment[Slot.FEET.ordinal()].getPotency();
        }
        return total;
    }

    public int getTotalEquipmentWeight() {
        var total = 0;
        for (var x = 0; x < Strings.SLOTS_COUNT; x++) {
            if (this.equipment[x] != null) {
                total += this.equipment[x].getWeight();
            }
        }
        return total;
    }

    public static ItemInventory readItemInventory(final DataIOReader dr) throws IOException {
        final var ii = new ItemInventory();
        for (var x = 0; x < ii.equipment.length; x++) {
            final var ei = Equipment.readEquipment(dr);
            if (ei != null) {
                ii.equipment[x] = ei;
            }
        }
        return ii;
    }

    public void writeItemInventory(final DataIOWriter dw) throws IOException {
        for (final Equipment ei : this.equipment) {
            if (ei != null) {
                ei.writeEquipment(dw);
            } else {
                dw.writeString("null");
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(this.equipment));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof final ItemInventory other)
                || !Arrays.equals(this.equipment, other.equipment)) {
            return false;
        }
        return true;
    }
}
