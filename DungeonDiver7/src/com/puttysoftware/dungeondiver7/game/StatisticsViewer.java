/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.game;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.diane.gui.dialog.CommonDialogs;

import com.puttysoftware.dungeondiver7.creature.StatConstants;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.locale.Strings;

public class StatisticsViewer {
	// Fields
	static MainWindow mainWindow;
	private static MainContent statisticsPane;
	private static MainContent contentPane;
	private static MainContent buttonPane;
	private static JButton btnOK;
	private static JLabel[] statisticsValues;
	private static boolean inited = false;

	private static void setUpGUI() {
		if (!StatisticsViewer.inited) {
			StatisticsViewer.mainWindow = MainWindow.mainWindow();
			StatisticsViewer.statisticsPane = mainWindow.createContent();
			StatisticsViewer.statisticsPane.setLayout(new BorderLayout());
			StatisticsViewer.contentPane = mainWindow.createContent();
			StatisticsViewer.contentPane.setLayout(new GridLayout(StatConstants.MAX_STATS, 1));
			StatisticsViewer.buttonPane = mainWindow.createContent();
			StatisticsViewer.buttonPane.setLayout(new FlowLayout());
			StatisticsViewer.btnOK = new JButton("OK");
			StatisticsViewer.btnOK.addActionListener(e -> StatisticsViewer.mainWindow.restoreSaved());
			StatisticsViewer.statisticsValues = new JLabel[StatConstants.MAX_DISPLAY_STATS];
			for (var x = 0; x < StatConstants.MAX_DISPLAY_STATS; x++) {
				StatisticsViewer.statisticsValues[x] = new JLabel();
				StatisticsViewer.contentPane.add(StatisticsViewer.statisticsValues[x]);
			}
			StatisticsViewer.buttonPane.add(StatisticsViewer.btnOK);
			StatisticsViewer.statisticsPane.add(StatisticsViewer.contentPane, BorderLayout.CENTER);
			StatisticsViewer.statisticsPane.add(StatisticsViewer.buttonPane, BorderLayout.SOUTH);
			StatisticsViewer.inited = true;
		}
	}

	public static void viewStatistics() {
		StatisticsViewer.setUpGUI();
		final var leader = PartyManager.getParty().getLeader();
		if (leader != null) {
			for (var x = 0; x < StatConstants.MAX_DISPLAY_STATS; x++) {
				final long value = leader.getStat(x);
				if (x == StatConstants.STAT_HIT || x == StatConstants.STAT_EVADE) {
					final var fmtVal = value / 100.0;
					StatisticsViewer.statisticsValues[x].setText(" " + Strings.stat(x) + ": " + fmtVal + "%  ");
				} else {
					StatisticsViewer.statisticsValues[x].setText(" " + Strings.stat(x) + ": " + value + "  ");
				}
			}
			StatisticsViewer.mainWindow.setAndSave(StatisticsViewer.statisticsPane, "Statistics");
		} else {
			CommonDialogs.showDialog("Nothing to display");
		}
	}

	// Private Constructor
	private StatisticsViewer() {
		// Do nothing
	}
}
