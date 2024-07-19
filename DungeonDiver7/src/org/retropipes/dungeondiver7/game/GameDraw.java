package org.retropipes.dungeondiver7.game;

import java.awt.Dimension;
import java.awt.Graphics;

import org.retropipes.diane.drawgrid.DrawGrid;
import org.retropipes.diane.gui.MainContent;
import org.retropipes.dungeondiver7.asset.ImageLoader;

public class GameDraw extends MainContent {
	private static final long serialVersionUID = 35935343464625L;
	private final DrawGrid drawGrid;

	public GameDraw() {
		final var vSize = GameViewingWindowManager.getFixedViewingWindowSize();
		final var gSize = ImageLoader.imageSize();
		this.setPreferredSize(new Dimension(vSize * gSize, vSize * gSize));
		this.drawGrid = new DrawGrid(vSize);
	}

	public GameDraw(final DrawGrid grid) {
		final var vSize = GameViewingWindowManager.getFixedViewingWindowSize();
		final var gSize = ImageLoader.imageSize();
		this.setPreferredSize(new Dimension(vSize * gSize, vSize * gSize));
		this.drawGrid = grid;
	}

	public DrawGrid getGrid() {
		return this.drawGrid;
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		if (this.drawGrid != null) {
			final var gSize = ImageLoader.imageSize();
			final var vSize = GameViewingWindowManager.getFixedViewingWindowSize();
			for (var x = 0; x < vSize; x++) {
				for (var y = 0; y < vSize; y++) {
					g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize, y * gSize, gSize, gSize, null);
				}
			}
		}
	}
}
