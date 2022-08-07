/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.puttysoftware.diane.gui.DrawGrid;
import com.puttysoftware.dungeondiver7.loader.ImageCompositor;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;

class GameDraw extends JPanel {
    private static final long serialVersionUID = 35935343464625L;
    private transient DrawGrid drawGrid;

    public GameDraw(final DrawGrid grid) {
	super();
	this.drawGrid = grid;
	final int vSize = PrefsManager.getViewingWindowSize();
	final int gSize = ImageCompositor.getGraphicSize();
	this.setPreferredSize(new Dimension(vSize * gSize, vSize * gSize));
    }

    @Override
    public void paintComponent(final Graphics g) {
	super.paintComponent(g);
	if (this.drawGrid != null) {
	    final int gSize = ImageCompositor.getGraphicSize();
	    final int vSize = PrefsManager.getViewingWindowSize();
	    for (int x = 0; x < vSize; x++) {
		for (int y = 0; y < vSize; y++) {
		    g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize, y * gSize, gSize, gSize, null);
		}
	    }
	}
    }
}
