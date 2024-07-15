/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.battle;

import java.awt.GridLayout;

import javax.swing.JLabel;

import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;

import com.puttysoftware.dungeondiver7.dungeon.objects.BattleCharacter;

public class MapBattleEffects {
	// Fields
	private MainContent effectsPane;
	private JLabel[] effectLabels;

	// Constructors
	public MapBattleEffects() {
		// Do nothing
	}

	public MainContent getEffectsPane() {
		if (this.effectsPane == null) {
			MainWindow mainWindow = MainWindow.mainWindow();
			this.effectsPane = mainWindow.createContent();
		}
		return this.effectsPane;
	}

	private void setUpGUI(final int count) {
		this.effectsPane = this.getEffectsPane();
		this.effectsPane.removeAll();
		this.effectsPane.setLayout(new GridLayout(count, 1));
		this.effectLabels = new JLabel[count];
		for (var x = 0; x < count; x++) {
			this.effectLabels[x] = new JLabel(" ");
		}
	}

	public void updateEffects(final BattleCharacter bc) {
		final var count = bc.getTemplate().getActiveEffectCount();
		if (count > 0) {
			this.setUpGUI(count);
			final var es = bc.getTemplate().getCompleteEffectStringArray();
			for (var x = 0; x < count; x++) {
				this.effectLabels[x].setText(es[x]);
			}
		}
	}
}
