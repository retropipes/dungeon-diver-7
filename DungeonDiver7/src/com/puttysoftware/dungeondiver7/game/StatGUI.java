/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.game;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.loader.StatImageManager;
import com.puttysoftware.dungeondiver7.locale.StatusImage;

class StatGUI {
	// Fields
	private JPanel statsPane;
	private JLabel hpLabel;
	private JLabel mpLabel;
	private JLabel goldLabel;
	private JLabel attackLabel;
	private JLabel defenseLabel;
	private JLabel xpLabel;
	private JLabel levelLabel;

	// Constructors
	StatGUI() {
		this.setUpGUI();
	}

	JPanel getStatsPane() {
		return this.statsPane;
	}

	void updateStats() {
		final var party = PartyManager.getParty();
		if (party != null) {
			final var pc = party.getLeader();
			if (pc != null) {
				this.hpLabel.setText(pc.getHPString());
				this.mpLabel.setText(pc.getMPString());
				this.goldLabel.setText(Integer.toString(pc.getGold()));
				this.attackLabel.setText(Integer.toString(pc.getAttack()));
				this.defenseLabel.setText(Integer.toString(pc.getDefense()));
				this.xpLabel.setText(pc.getXPString());
				this.levelLabel.setText(party.getZoneString());
			}
		}
	}

	private void setUpGUI() {
		this.statsPane = new JPanel();
		this.statsPane.setLayout(new GridLayout(7, 1));
		this.hpLabel = new JLabel("", null, SwingConstants.LEFT);
		this.mpLabel = new JLabel("", null, SwingConstants.LEFT);
		this.goldLabel = new JLabel("", null, SwingConstants.LEFT);
		this.attackLabel = new JLabel("", null, SwingConstants.LEFT);
		this.defenseLabel = new JLabel("", null, SwingConstants.LEFT);
		this.xpLabel = new JLabel("", null, SwingConstants.LEFT);
		this.levelLabel = new JLabel("", null, SwingConstants.LEFT);
		this.statsPane.add(this.hpLabel);
		this.statsPane.add(this.mpLabel);
		this.statsPane.add(this.goldLabel);
		this.statsPane.add(this.attackLabel);
		this.statsPane.add(this.defenseLabel);
		this.statsPane.add(this.xpLabel);
		this.statsPane.add(this.levelLabel);
	}

	void updateImages() {
		final var hpImage = StatImageManager.load(StatusImage.HEALTH);
		this.hpLabel.setIcon(hpImage);
		final var mpImage = StatImageManager.load(StatusImage.MAGIC);
		this.mpLabel.setIcon(mpImage);
		final var goldImage = StatImageManager.load(StatusImage.MONEY);
		this.goldLabel.setIcon(goldImage);
		final var attackImage = StatImageManager.load(StatusImage.MELEE_ATTACK);
		this.attackLabel.setIcon(attackImage);
		final var defenseImage = StatImageManager.load(StatusImage.DEFENSE);
		this.defenseLabel.setIcon(defenseImage);
		final var xpImage = StatImageManager.load(StatusImage.EXPERIENCE);
		this.xpLabel.setIcon(xpImage);
		final var levelImage = StatImageManager.load(StatusImage.CREATURE_LEVEL);
		this.levelLabel.setIcon(levelImage);
	}
}
