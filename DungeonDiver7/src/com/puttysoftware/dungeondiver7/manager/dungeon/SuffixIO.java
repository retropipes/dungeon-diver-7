/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.manager.dungeon;

import java.io.IOException;

import com.puttysoftware.fileio.FileIOReader;
import com.puttysoftware.fileio.FileIOWriter;

public interface SuffixIO {
    void writeSuffix(FileIOWriter writer) throws IOException;

    void readSuffix(FileIOReader reader, int formatVersion) throws IOException;
}
