/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.game;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.creature.party.PartyMember;

public final class InventoryViewer {
    private InventoryViewer() {
	// Do nothing
    }

    public static void showEquipmentDialog() {
	final String title = "Equipment";
	final PartyMember member = PartyManager.getParty().getLeader();
	if (member != null) {
	    final String[] equipString = member.getItems().generateEquipmentStringArray();
	    CommonDialogs.showInputDialog("Equipment", title, equipString, equipString[0]);
	}
    }
}
