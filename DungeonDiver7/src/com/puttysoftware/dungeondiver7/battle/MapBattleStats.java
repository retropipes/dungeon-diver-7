/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.battle;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.puttysoftware.diane.gui.MainContent;
import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.dungeondiver7.dungeon.objects.BattleCharacter;
import com.puttysoftware.dungeondiver7.loader.StatImageManager;
import com.puttysoftware.dungeondiver7.locale.StatusImage;

public class MapBattleStats {
    // Fields
    private MainContent statsPane;
    private JLabel nameLabel;
    private JLabel hpLabel;
    private JLabel mpLabel;
    private JLabel attLabel;
    private JLabel defLabel;

    // Constructors
    public MapBattleStats() {
	this.setUpGUI();
	this.updateIcons();
    }

    public MainContent getStatsPane() {
	return this.statsPane;
    }

    private void setUpGUI() {
	this.statsPane = MainWindow.createContent();
	this.statsPane.setLayout(new GridLayout(9, 1));
	this.nameLabel = new JLabel("", null, SwingConstants.LEFT);
	this.hpLabel = new JLabel("", null, SwingConstants.LEFT);
	this.mpLabel = new JLabel("", null, SwingConstants.LEFT);
	this.attLabel = new JLabel("", null, SwingConstants.LEFT);
	this.defLabel = new JLabel("", null, SwingConstants.LEFT);
	this.statsPane.add(this.nameLabel);
	this.statsPane.add(this.hpLabel);
	this.statsPane.add(this.mpLabel);
	this.statsPane.add(this.attLabel);
	this.statsPane.add(this.defLabel);
    }

    private void updateIcons() {
	final var nameImage = StatImageManager.load(StatusImage.CREATURE_ID);
	this.nameLabel.setIcon(nameImage);
	final var hpImage = StatImageManager.load(StatusImage.HEALTH);
	this.hpLabel.setIcon(hpImage);
	final var mpImage = StatImageManager.load(StatusImage.MAGIC);
	this.mpLabel.setIcon(mpImage);
	final var attImage = StatImageManager.load(StatusImage.MELEE_ATTACK);
	this.attLabel.setIcon(attImage);
	final var defImage = StatImageManager.load(StatusImage.DEFENSE);
	this.defLabel.setIcon(defImage);
    }

    public void updateStats(final BattleCharacter bc) {
	this.nameLabel.setText(bc.getName());
	this.hpLabel.setText(bc.getTemplate().getHPString());
	this.mpLabel.setText(bc.getTemplate().getMPString());
	this.attLabel.setText(Integer.toString(bc.getTemplate().getAttack()));
	this.defLabel.setText(Integer.toString(bc.getTemplate().getDefense()));
    }
}
