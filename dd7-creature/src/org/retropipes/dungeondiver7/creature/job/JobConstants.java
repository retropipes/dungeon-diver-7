/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.creature.job;

public class JobConstants {
    public static final int JOB_ANNIHILATOR = 0;
    public static final int JOB_BUFFER = 1;
    public static final int JOB_CURER = 2;
    public static final int JOB_DEBUFFER = 3;
    public static final int JOBS_COUNT = 4;
    public static final String[] JOB_NAMES = { "Annihilator", "Buffer", "Curer", "Debuffer" };

    private JobConstants() {
	// Do nothing
    }
}