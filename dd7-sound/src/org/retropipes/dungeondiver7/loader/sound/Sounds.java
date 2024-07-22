/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.loader.sound;

import java.net.URL;

import org.retropipes.diane.asset.sound.DianeSoundIndex;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;

public enum Sounds implements DianeSoundIndex {
	ATTACK_HIT, AXE_HIT, BLINDNESS, BOLT_SPELL, BOSS_DIE, BUBBLE_SPELL, BUFF_1, BUFF_2, CLICK, COLD_SPELL,
	CONFUSION_SPELL, COUNTER, CRITICAL_HIT, DAGGER_HIT, DANGER, DEATH, DEBUFF_1, DEBUFF_2, DEFEATED, DISPEL_EFFECT,
	UNUSED_1, DRAIN_SPELL, DUMBFOUND_SPELL, ERROR, EXPLODE_SPELL, FAILED, FATAL_ERROR, FIGHT, FIREBALL_SPELL,
	FOCUS_SPELL, FREEZE_SPELL, FUMBLE, HAMMER_HIT, WALK_ICE, LEVEL_UP, MELT_SPELL, MISSED, MONSTER_HIT, NEXT_ROUND,
	PLAYER_UP, RUN_AWAY, SHOP, CAST_SPELL, STAFF_HIT, STEAM_SPELL, SWORD_HIT, TRANSACT, UNUSED_2, VICTORY, WALK,
	WAND_HIT, WEAKNESS_SPELL, WIN_GAME, ZAP_SPELL, HEAL_SPELL, DOOR_CLOSES, DOOR_OPENS, EQUIP, MONSTER_SPELL, SPECIAL,
	ANTI_DIE, ANTI_FIRE, BREAK_BRICKS, BUMP_HEAD, DEAD, END_LEVEL, FIRE_LASER, LASER_DIE, MOVE, PUSH_ANTI_TANK,
	PUSH_BOX, PUSH_MIRROR, REFLECT, ROTATE, SINK, TURN, MISSILE, BOOM, BOOST, MAGNET, STUN, STUNNED, STUNNER, STUN_OFF,
	DEFROST, FROZEN, DOWN, UP, BALL_ROLL, BARREL, GRAB, UNLOCK, DISRUPTOR, DISRUPTED, DISRUPT_END, WOOD_BURN, COOL_OFF,
	MELT, CRUSH, BUTTON, JUMPING, PREPARE, CRACK, DISCOVER, POWERFUL, POWER_LASER, FREEZE_MAGIC, KILL_SKULL, CONTROL,
	ERA_CHANGE, ERAS_ENABLED, IDENTIFY, _NONE;

	public String getDisplayName() {
		return this == _NONE ? null : Strings.sound(this.ordinal());
	}

	@Override
	public String getName() {
		return this == _NONE ? null : this.toString().toLowerCase().replace('_', '-');
	}

	@Override
	public URL getURL() {
		return Sounds.class.getResource("/asset/sound/" + this.getName() + Strings.fileExtension(FileExtension.SOUND));
	}
}