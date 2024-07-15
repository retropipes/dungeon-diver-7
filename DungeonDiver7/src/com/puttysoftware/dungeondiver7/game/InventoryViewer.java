/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.game;

import org.retropipes.diane.gui.dialog.CommonDialogs;

import com.puttysoftware.dungeondiver7.creature.party.PartyManager;

public final class InventoryViewer {
	public static void showEquipmentDialog() {
		final var title = "Equipment";
		final var member = PartyManager.getParty().getLeader();
		if (member != null) {
			final var equipString = member.getItems().generateEquipmentStringArray();
			CommonDialogs.showInputDialog("Equipment", title, equipString, equipString[0]);
		}
	}

	private InventoryViewer() {
		// Do nothing
	}
}
