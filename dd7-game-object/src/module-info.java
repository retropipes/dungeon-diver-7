module org.retropipes.dungeondiver7.gameobject {
    requires transitive org.retropipes.diane.direction;
    requires transitive org.retropipes.diane.fileio;
    requires transitive org.retropipes.diane.objectmodel;
    requires transitive org.retropipes.dungeondiver7.loader.image.gameobject;
    requires transitive org.retropipes.dungeondiver7.loader.sound;
    requires transitive org.retropipes.dungeondiver7.locale;

    exports org.retropipes.dungeondiver7.gameobject;
}