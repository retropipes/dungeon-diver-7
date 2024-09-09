/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.creature.gender;

public class Gender {
    private final int genderID;

    Gender(final int gid) {
	this.genderID = gid;
    }

    public int getGenderID() {
	return this.genderID;
    }
}
