/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.objects;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.dungeondiver7.creature.party.PartyManager;
import org.retropipes.dungeondiver7.dungeon.AbstractDungeon;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractCharacter;
import org.retropipes.dungeondiver7.loader.ObjectImageConstants;

public class Player extends AbstractCharacter {
	// Constructors
	public Player() {
	}

	@Override
	public BufferedImageIcon gameRenderHook(final int x, final int y) {
		return PartyManager.getParty().getLeader().getImage();
	}

	@Override
	public int getBaseID() {
		return ObjectImageConstants.NONE;
	}

	@Override
	public String getDescription() {
		return "This is you - the Player.";
	}

	@Override
	public int getMaximumRequiredQuantity(final AbstractDungeon dungeon) {
		return 1;
	}

	@Override
	public int getMinimumRequiredQuantity(final AbstractDungeon dungeon) {
		return 1;
	}

	@Override
	public String getName() {
		return "Player";
	}

	@Override
	public String getPluralName() {
		return "Players";
	}

	// Random Generation Rules
	@Override
	public boolean isRequired(final AbstractDungeon dungeon) {
		return true;
	}
}