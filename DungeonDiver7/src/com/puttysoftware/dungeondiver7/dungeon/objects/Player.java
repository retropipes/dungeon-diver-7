/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.dungeon.objects;

import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.dungeon.AbstractDungeon;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractCharacter;
import com.puttysoftware.dungeondiver7.loader.ObjectImageConstants;
import com.puttysoftware.images.BufferedImageIcon;

public class Player extends AbstractCharacter {
    // Constructors
    public Player() {
	super();
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.NONE;
    }

    @Override
    public String getName() {
	return "Player";
    }

    @Override
    public String getPluralName() {
	return "Players";
    }

    @Override
    public String getDescription() {
	return "This is you - the Player.";
    }

    // Random Generation Rules
    @Override
    public boolean isRequired(final AbstractDungeon dungeon) {
	return true;
    }

    @Override
    public int getMinimumRequiredQuantity(final AbstractDungeon dungeon) {
	return 1;
    }

    @Override
    public int getMaximumRequiredQuantity(final AbstractDungeon dungeon) {
	return 1;
    }

    @Override
    public BufferedImageIcon gameRenderHook(final int x, final int y) {
	return PartyManager.getParty().getLeader().getImage();
    }
}