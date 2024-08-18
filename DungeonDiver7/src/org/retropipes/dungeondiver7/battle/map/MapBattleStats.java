/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.


All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle.map;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.dungeondiver7.dungeon.abc.BattleCharacter;
import org.retropipes.dungeondiver7.loader.image.status.StatusImageId;
import org.retropipes.dungeondiver7.loader.image.status.StatusImageLoader;

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
	MainWindow mainWindow = MainWindow.mainWindow();
	this.statsPane = mainWindow.createContent();
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
	final var nameImage = StatusImageLoader.load(StatusImageId.CREATURE_ID);
	this.nameLabel.setIcon(nameImage);
	final var hpImage = StatusImageLoader.load(StatusImageId.HEALTH);
	this.hpLabel.setIcon(hpImage);
	final var mpImage = StatusImageLoader.load(StatusImageId.MAGIC);
	this.mpLabel.setIcon(mpImage);
	final var attImage = StatusImageLoader.load(StatusImageId.MELEE_ATTACK);
	this.attLabel.setIcon(attImage);
	final var defImage = StatusImageLoader.load(StatusImageId.DEFENSE);
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
