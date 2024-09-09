/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.creature.characterfile;

import java.io.IOException;

public class CharacterVersionException extends IOException {
    private static final long serialVersionUID = 7521249394165201264L;

    public CharacterVersionException(final String message) {
	super(message);
    }
}
