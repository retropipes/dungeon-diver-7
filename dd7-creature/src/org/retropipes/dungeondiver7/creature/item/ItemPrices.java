package org.retropipes.dungeondiver7.creature.item;

public class ItemPrices {
    public static int getEquipmentCost(final int x) {
	return 10 * x * x * x + 10 * x * x + 10 * x + 10;
    }
}
