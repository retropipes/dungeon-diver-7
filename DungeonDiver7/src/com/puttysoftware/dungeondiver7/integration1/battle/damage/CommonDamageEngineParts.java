/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.battle.damage;

import com.puttysoftware.randomrange.RandomRange;

class CommonDamageEngineParts {
    static final int MULTIPLIER_DIVIDE = 100000;
    private static final int MIN_CHANCE = 0;
    private static final int MAX_CHANCE = 10000;
    static final int ALWAYS = 10001;
    static final double FAITH_MULT_START = 1.0;

    private CommonDamageEngineParts() {
	// Do nothing
    }

    static boolean didSpecial(final int aSpecial) {
	final int rSpecial = new RandomRange(0, 10000).generate();
	return rSpecial < aSpecial;
    }

    static int fumbleDamage(final int power) {
	return new RandomRange(1, Math.max(1, power / 100)).generate();
    }

    static int chance() {
	return new RandomRange(CommonDamageEngineParts.MIN_CHANCE, CommonDamageEngineParts.MAX_CHANCE).generate();
    }
}