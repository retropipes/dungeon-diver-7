/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.creature.characterfile;

import java.io.IOException;

public class CharacterVersionException extends IOException {
    private static final long serialVersionUID = 7521249394165201264L;

    public CharacterVersionException(final String message) {
	super(message);
    }
}
