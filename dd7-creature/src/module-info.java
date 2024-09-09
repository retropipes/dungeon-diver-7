/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
module org.retropipes.dungeondiver7.creature {
    requires transitive org.retropipes.diane.ack;
    requires transitive org.retropipes.diane.fileio;
    requires transitive org.retropipes.diane.fileio.utility;
    requires transitive org.retropipes.diane.gui;
    requires transitive org.retropipes.diane.internal;
    requires transitive org.retropipes.diane.polytable;
    requires transitive org.retropipes.diane.random;
    requires transitive org.retropipes.dungeondiver7.creature.job.description;
    requires transitive org.retropipes.dungeondiver7.loader.image.monster;
    requires transitive org.retropipes.dungeondiver7.loader.music;
    requires transitive org.retropipes.dungeondiver7.loader.sound;
    requires transitive org.retropipes.dungeondiver7.locale;
    
    exports org.retropipes.dungeondiver7.creature;
    exports org.retropipes.dungeondiver7.creature.characterfile;
    exports org.retropipes.dungeondiver7.creature.effect;
    exports org.retropipes.dungeondiver7.creature.gender;
    exports org.retropipes.dungeondiver7.creature.item;
    exports org.retropipes.dungeondiver7.creature.job;
    exports org.retropipes.dungeondiver7.creature.job.predefined;
    exports org.retropipes.dungeondiver7.creature.monster;
    exports org.retropipes.dungeondiver7.creature.party;
    exports org.retropipes.dungeondiver7.creature.spell;
}