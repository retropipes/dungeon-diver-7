/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.dungeondiver7.asset.ObjectImageConstants;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.dungeon.Dungeon;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractCharacter;

public class Player extends AbstractCharacter {
    // Constructors
    public Player() {
    }

    @Override
    public BufferedImageIcon gameRenderHook(final int x, final int y) {
	return PartyManager.getParty().getLeader().getImage();
    }

    @Override
    public int getId() {
	return ObjectImageConstants.NONE;
    }

    @Override
    public int getMaximumRequiredQuantity(final Dungeon dungeon) {
	return 1;
    }

    @Override
    public int getMinimumRequiredQuantity(final Dungeon dungeon) {
	return 1;
    }

    // Random Generation Rules
    @Override
    public boolean isRequired(final Dungeon dungeon) {
	return true;
    }
}