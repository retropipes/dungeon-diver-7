/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
module org.retropipes.dungeondiver7.loader.image {
    requires transitive org.retropipes.diane.internal;
    requires transitive org.retropipes.diane.asset.image;
    requires transitive org.retropipes.diane.fileio.utility;
    requires transitive org.retropipes.dungeondiver7.locale;
    requires transitive org.retropipes.diane.objectmodel;

    exports org.retropipes.dungeondiver7.loader.image.attribute;
    exports org.retropipes.dungeondiver7.loader.image.battle;
    exports org.retropipes.dungeondiver7.loader.image.effect;
    exports org.retropipes.dungeondiver7.loader.image.gameobject;
    exports org.retropipes.dungeondiver7.loader.image.halo;
    exports org.retropipes.dungeondiver7.loader.image.item;
    exports org.retropipes.dungeondiver7.loader.image.monster;
    exports org.retropipes.dungeondiver7.loader.image.status;
    exports org.retropipes.dungeondiver7.loader.image.ui;
}