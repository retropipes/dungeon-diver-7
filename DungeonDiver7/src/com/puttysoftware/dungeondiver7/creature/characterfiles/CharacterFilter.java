/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.creature.characterfiles;

import java.io.File;
import java.io.FilenameFilter;

import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;

class CharacterFilter implements FilenameFilter {
    @Override
    public boolean accept(final File dir, final String name) {
        final var ext = CharacterFilter.getExtension(name);
        if (ext == null) {
            return false;
        }
        if (ext.equals(Strings.fileExtension(FileExtension.CHARACTER))) {
            return true;
        }
        return false;
    }

    private static String getExtension(final String s) {
        String ext = null;
        final var i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i).toLowerCase();
        }
        return ext;
    }
}
