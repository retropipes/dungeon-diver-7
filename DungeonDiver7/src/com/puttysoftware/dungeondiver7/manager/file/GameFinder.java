/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.manager.file;

import java.io.File;
import java.io.FilenameFilter;

import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;

public class GameFinder implements FilenameFilter {
    private static String getExtension(final String s) {
	String ext = null;
	final var i = s.lastIndexOf('.');
	if (i > 0 && i < s.length() - 1) {
	    ext = s.substring(i).toLowerCase();
	}
	return ext;
    }

    @Override
    public boolean accept(final File f, final String s) {
	final var extension = GameFinder.getExtension(s);
	if (extension != null && extension.equals(Strings.fileExtension(FileExtension.SUSPEND))) {
	    return true;
	}
	return false;
    }
}
