/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1;

public class Integration1 {
    // Constants
    private static Application application;

    // Methods
    public static Application getApplication() {
	if (Integration1.application == null) {
	    Integration1.application = new Application();
	    Integration1.application.postConstruct();
	}
	return Integration1.application;
    }
}
