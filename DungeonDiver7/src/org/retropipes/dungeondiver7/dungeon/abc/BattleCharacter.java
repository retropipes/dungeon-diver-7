/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.dungeon.abc;

import java.util.Objects;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.dungeondiver7.asset.ObjectImageConstants;
import org.retropipes.dungeondiver7.battle.ai.AIContext;
import org.retropipes.dungeondiver7.battle.ai.CreatureAI;
import org.retropipes.dungeondiver7.creature.Creature;
import org.retropipes.dungeondiver7.creature.StatConstants;
import org.retropipes.dungeondiver7.dungeon.objects.Empty;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

public class BattleCharacter extends DungeonObject {
    // Fields
    private final Creature creature;
    private final AIContext aic;
    private CreatureAI ai;
    private int actionCounter;
    private int attackCounter;
    private int spellCounter;
    private boolean isBattleCharacterActive;

    // Constructors
    public BattleCharacter(final Creature newTemplate, final int rows, final int columns) {
	super(true, false);
	this.creature = newTemplate;
	this.actionCounter = newTemplate.getMapBattleActionsPerRound();
	this.attackCounter = (int) newTemplate.getEffectedStat(StatConstants.STAT_ATTACKS_PER_ROUND);
	this.spellCounter = (int) newTemplate.getEffectedStat(StatConstants.STAT_SPELLS_PER_ROUND);
	this.isBattleCharacterActive = true;
	this.setSavedObject(new Empty());
	this.aic = new AIContext(this, rows, columns);
    }

    public final void activate() {
	this.isBattleCharacterActive = true;
    }

    @Override
    public BufferedImageIcon battleRenderHook() {
	return this.creature.getImage();
    }

    public final void deactivate() {
	this.isBattleCharacterActive = false;
    }

    @Override
    public boolean equals(final Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!super.equals(obj) || !(obj instanceof final BattleCharacter other)
		|| this.actionCounter != other.actionCounter || this.attackCounter != other.attackCounter) {
	    return false;
	}
	if (this.isBattleCharacterActive != other.isBattleCharacterActive || this.spellCounter != other.spellCounter
		|| !Objects.equals(this.creature, other.creature)) {
	    return false;
	}
	return true;
    }

    public final String getAPString() {
	return "Moves Left: " + (this.actionCounter >= 0 ? this.actionCounter : 0);
    }

    public final String getAttackString() {
	return "Attacks Left: " + (this.attackCounter >= 0 ? this.attackCounter : 0);
    }

    @Override
    public int getBaseID() {
	return ObjectImageConstants.NONE;
    }

    public final int getActionsLeft() {
	return this.actionCounter;
    }

    public final int getAttacksLeft() {
	return this.attackCounter;
    }

    public final CreatureAI getAI() {
	return this.ai;
    }

    public final AIContext getAIContext() {
	return this.aic;
    }

    public final int getSpellsLeft() {
	return this.spellCounter;
    }

    @Override
    public int getCustomFormat() {
	return 2;
    }

    @Override
    public int getCustomProperty(final int propID) {
	return switch (propID) {
	case 0 -> this.getX();
	case 1 -> this.getY();
	default -> DungeonObject.DEFAULT_CUSTOM_VALUE;
	};
    }

    @Override
    public int getLayer() {
	return DungeonConstants.LAYER_LOWER_OBJECTS;
    }

    public String getName() {
	return this.creature.getName();
    }

    public final String getSpellString() {
	return "Spells Left: " + (this.spellCounter >= 0 ? this.spellCounter : 0);
    }

    public final int getTeamID() {
	return this.creature.getTeamID();
    }

    public final String getTeamString() {
	if (this.getCreature().getTeamID() == 0) {
	    return "Team: Party";
	}
	return "Team: Enemies " + this.getCreature().getTeamID();
    }

    public final Creature getCreature() {
	return this.creature;
    }

    public final int getX() {
	return this.creature.getX();
    }

    public final int getY() {
	return this.creature.getY();
    }

    public final boolean hasAI() {
	return this.ai != null;
    }

    @Override
    public int hashCode() {
	final var prime = 31;
	var result = super.hashCode();
	result = prime * result + this.actionCounter;
	result = prime * result + this.attackCounter;
	result = prime * result + (this.isBattleCharacterActive ? 1231 : 1237);
	result = prime * result + this.spellCounter;
	return prime * result + (this.creature == null ? 0 : this.creature.hashCode());
    }

    public final boolean isActive() {
	return this.isBattleCharacterActive;
    }

    public final void modifyAP(final int mod) {
	this.actionCounter -= mod;
    }

    public final void modifyAttacks(final int mod) {
	this.attackCounter -= mod;
    }

    public final void modifySpells(final int mod) {
	this.spellCounter -= mod;
    }

    public final void offsetX(final int newX) {
	this.creature.offsetX(newX);
    }

    public final void offsetY(final int newY) {
	this.creature.offsetY(newY);
    }

    @Override
    public void postMoveAction(final int dirX, final int dirY, final int dirZ) {
	// Do nothing
    }

    public final void resetAll() {
	this.resetAP();
	this.resetAttacks();
	this.resetSpells();
    }

    public final void resetAP() {
	this.actionCounter = this.creature.getMapBattleActionsPerRound();
    }

    public final void resetAttacks() {
	this.attackCounter = (int) this.creature.getEffectedStat(StatConstants.STAT_ATTACKS_PER_ROUND);
    }

    public final void resetLocation() {
	this.creature.setX(-1);
	this.creature.setY(-1);
    }

    public final void resetSpells() {
	this.spellCounter = (int) this.creature.getEffectedStat(StatConstants.STAT_SPELLS_PER_ROUND);
    }

    public final void restoreLocation() {
	this.creature.restoreLocation();
    }

    public final void saveLocation() {
	this.creature.saveLocation();
    }

    public final void setAI(final CreatureAI newAI) {
	this.ai = newAI;
    }

    @Override
    public void setCustomProperty(final int propID, final int value) {
	switch (propID) {
	case 0:
	    this.setX(value);
	    break;
	case 1:
	    this.setY(value);
	    break;
	default:
	    break;
	}
    }

    public final void setX(final int newX) {
	this.creature.setX(newX);
    }

    public final void setY(final int newY) {
	this.creature.setY(newY);
    }
}
