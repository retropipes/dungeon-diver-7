/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import com.puttysoftware.dungeondiver7.locale.Strings;

public class SoundConstants {
    // Public Sound Constants
    public static final int ATTACK_HIT = 0;
    public static final int AXE_HIT = 1;
    public static final int BLINDNESS = 2;
    public static final int BOLT_SPELL = 3;
    public static final int BOSS_DIE = 4;
    public static final int BUBBLE_SPELL = 5;
    public static final int BUFF_1 = 6;
    public static final int BUFF_2 = 7;
    public static final int CLICK = 8;
    public static final int COLD_SPELL = 9;
    public static final int CONFUSION_SPELL = 10;
    public static final int COUNTER = 11;
    public static final int CRITICAL_HIT = 12;
    public static final int DAGGER_HIT = 13;
    public static final int DANGER = 14;
    public static final int DEATH = 15;
    public static final int DEBUFF_1 = 16;
    public static final int DEBUFF_2 = 17;
    public static final int DEFEATED = 18;
    public static final int DISPEL_EFFECT = 19;
    // public static final int UNUSED_1 = 20;
    public static final int DRAIN_SPELL = 21;
    public static final int DUMBFOUND_SPELL = 22;
    public static final int ERROR = 23;
    public static final int EXPLODE_SPELL = 24;
    public static final int FAILED = 25;
    public static final int FATAL_ERROR = 26;
    public static final int FIGHT = 27;
    public static final int FIREBALL_SPELL = 28;
    public static final int FOCUS_SPELL = 29;
    public static final int FREEZE_SPELL = 30;
    public static final int FUMBLE = 31;
    public static final int HAMMER_HIT = 32;
    public static final int WALK_ICE = 33;
    public static final int LEVEL_UP = 34;
    public static final int MELT_SPELL = 35;
    public static final int MISSED = 36;
    public static final int MONSTER_HIT = 37;
    public static final int NEXT_ROUND = 38;
    public static final int PLAYER_UP = 39;
    public static final int RUN_AWAY = 40;
    public static final int SHOP = 41;
    public static final int CAST_SPELL = 42;
    public static final int STAFF_HIT = 43;
    public static final int STEAM_SPELL = 44;
    public static final int SWORD_HIT = 45;
    public static final int TRANSACT = 46;
    // public static final int UNUSED_2 = 47;
    public static final int VICTORY = 48;
    public static final int WALK = 49;
    public static final int WAND_HIT = 50;
    public static final int WEAKNESS_SPELL = 51;
    public static final int WIN_GAME = 52;
    public static final int ZAP_SPELL = 53;
    public static final int HEAL_SPELL = 54;
    public static final int DOOR_CLOSES = 55;
    public static final int DOOR_OPENS = 56;
    public static final int EQUIP = 57;
    public static final int MONSTER_SPELL = 58;
    public static final int SPECIAL = 59;
    public static final int ANTI_DIE = 60;
    public static final int ANTI_FIRE = 61;
    public static final int BREAK_BRICKS = 62;
    public static final int BUMP_HEAD = 63;
    public static final int DEAD = 64;
    public static final int END_LEVEL = 65;
    public static final int FIRE_LASER = 66;
    public static final int LASER_DIE = 67;
    public static final int MOVE = 68;
    public static final int PUSH_ANTI_TANK = 69;
    public static final int PUSH_BOX = 70;
    public static final int PUSH_MIRROR = 71;
    public static final int REFLECT = 72;
    public static final int ROTATE = 73;
    public static final int SINK = 74;
    public static final int TURN = 75;
    public static final int MISSILE = 76;
    public static final int BOOM = 77;
    public static final int BOOST = 78;
    public static final int MAGNET = 79;
    public static final int STUN = 80;
    public static final int STUNNED = 81;
    public static final int STUNNER = 82;
    public static final int STUN_OFF = 83;
    public static final int DEFROST = 84;
    public static final int FROZEN = 85;
    public static final int DOWN = 86;
    public static final int UP = 87;
    public static final int BALL_ROLL = 88;
    public static final int BARREL = 89;
    public static final int GRAB = 90;
    public static final int UNLOCK = 91;
    public static final int DISRUPTOR = 92;
    public static final int DISRUPTED = 93;
    public static final int DISRUPT_END = 94;
    public static final int WOOD_BURN = 95;
    public static final int COOL_OFF = 96;
    public static final int MELT = 97;
    public static final int CRUSH = 98;
    public static final int BUTTON = 99;
    public static final int JUMPING = 100;
    public static final int PREPARE = 101;
    public static final int CRACK = 102;
    public static final int DISCOVER = 103;
    public static final int POWERFUL = 104;
    public static final int POWER_LASER = 105;
    public static final int FREEZE_MAGIC = 106;
    public static final int KILL_SKULL = 107;
    public static final int CONTROL = 108;
    public static final int ERA_CHANGE = 109;
    public static final int ERAS_ENABLED = 110;
    public static final int IDENTIFY = 111;

    // Private constructor
    private SoundConstants() {
	// Do nothing
    }

    static String getSoundName(final int ID) {
	return Strings.sound(ID);
    }
}