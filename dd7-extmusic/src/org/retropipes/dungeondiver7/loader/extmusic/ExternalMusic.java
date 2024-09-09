/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.loader.extmusic;

import org.retropipes.dungeondiver7.locale.Strings;

public class ExternalMusic {
    // Fields
    private String name;
    private String path;

    // Constructor
    public ExternalMusic() {
	this.name = Strings.EMPTY;
	this.path = Strings.EMPTY;
    }

    public String getName() {
	return this.name;
    }

    public String getPath() {
	return this.path;
    }

    public void setName(final String newName) {
	this.name = newName;
    }

    public void setPath(final String newPath) {
	this.path = newPath;
    }
}
