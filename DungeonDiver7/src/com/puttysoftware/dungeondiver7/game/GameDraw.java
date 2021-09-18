package com.puttysoftware.dungeondiver7.game;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.puttysoftware.dungeondiver7.loaders.ImageLoader;
import com.puttysoftware.dungeondiver7.utilities.DrawGrid;

class GameDraw extends JPanel {
	private static final long serialVersionUID = 35935343464625L;
	private final DrawGrid drawGrid;

	public GameDraw() {
		super();
		final int vSize = GameViewingWindowManager.getViewingWindowSize();
		final int gSize = ImageLoader.getGraphicSize();
		this.setPreferredSize(new Dimension(vSize * gSize, vSize * gSize));
		this.drawGrid = new DrawGrid(vSize);
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		if (this.drawGrid != null) {
			final int gSize = ImageLoader.getGraphicSize();
			final int vSize = GameViewingWindowManager.getViewingWindowSize();
			for (int x = 0; x < vSize; x++) {
				for (int y = 0; y < vSize; y++) {
					g.drawImage(this.drawGrid.getImageCell(y, x), x * gSize, y * gSize, gSize, gSize, null);
				}
			}
		}
	}

	public DrawGrid getGrid() {
		return this.drawGrid;
	}
}
