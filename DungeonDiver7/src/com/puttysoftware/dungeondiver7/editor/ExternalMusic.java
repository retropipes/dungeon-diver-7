/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.editor;

public class ExternalMusic {
    // Fields
    private String name;
    private String path;

    // Constructor
    public ExternalMusic() {
        this.name = "";
        this.path = "";
    }

    // Methods
    public String getName() {
        return this.name;
    }

    public void setName(final String newName) {
        this.name = newName;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(final String newPath) {
        this.path = newPath;
    }
}
