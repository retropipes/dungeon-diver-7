/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.battle;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.puttysoftware.diane.gui.DrawGrid;
import com.puttysoftware.dungeondiver7.loader.BattleImageManager;

public class MapBattleDraw extends JPanel {
    private static final long serialVersionUID = 35935343464625L;
    private final DrawGrid drawGrid;

    public MapBattleDraw(final DrawGrid grid) {
	this.drawGrid = grid;
	final var vSize = MapBattleViewingWindowManager.getViewingWindowSize();
	final var gSize = BattleImageManager.getGraphicSize();
	this.setPreferredSize(new Dimension(vSize * gSize, vSize * gSize));
    }

    @Override
    public void paintComponent(final Graphics g) {
	super.paintComponent(g);
	if (this.drawGrid != null) {
	    final var gSize = BattleImageManager.getGraphicSize();
	    final var vSize = MapBattleViewingWindowManager.getViewingWindowSize();
	    for (var x = 0; x < vSize; x++) {
		for (var y = 0; y < vSize; y++) {
		    g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize, y * gSize, gSize, gSize, null);
		}
	    }
	}
    }
}
