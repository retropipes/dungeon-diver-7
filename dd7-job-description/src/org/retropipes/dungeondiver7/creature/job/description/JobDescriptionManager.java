/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.creature.job.description;

import java.io.IOException;

import org.retropipes.diane.Diane;
import org.retropipes.diane.fileio.utility.ResourceStreamReader;
import org.retropipes.dungeondiver7.locale.FileExtension;
import org.retropipes.dungeondiver7.locale.Strings;

public class JobDescriptionManager {
	public static String getJobDescription(final int j) {
		final var name = Integer.toString(j);
		try (final var rsr = new ResourceStreamReader(JobDescriptionManager.class.getResourceAsStream(
				"/asset/description/job/" + name + Strings.fileExtension(FileExtension.INTERNAL_DATA)))) {
			return rsr.readString();
		} catch (final IOException e) {
			Diane.handleError(e);
			return null;
		}
	}
}
