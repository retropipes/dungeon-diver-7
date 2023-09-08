/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.loader;

import java.io.IOException;

import com.puttysoftware.diane.fileio.utility.ResourceStreamReader;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.creature.job.JobConstants;
import com.puttysoftware.dungeondiver7.locale.FileExtension;
import com.puttysoftware.dungeondiver7.locale.Strings;

public class JobDescriptionManager {
    public static String getJobDescription(final int j) {
	final var name = JobConstants.JOB_NAMES[j].toLowerCase();
	try (final var rsr = new ResourceStreamReader(JobDescriptionManager.class.getResourceAsStream(
		"/asset/description/job/" + name + Strings.fileExtension(FileExtension.INTERNAL_DATA)))) {
	    return rsr.readString();
	} catch (final IOException e) {
	    DungeonDiver7.logError(e);
	    return null;
	}
    }
}
