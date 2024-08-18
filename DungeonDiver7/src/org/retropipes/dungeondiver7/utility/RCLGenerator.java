/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package org.retropipes.dungeondiver7.utility;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.dungeondiver7.dungeon.Dungeon;

public class RCLGenerator {
    public static MainContent generateRowColumnLabels() {
	MainWindow mainWindow = MainWindow.mainWindow();
	final var outerOutputPane = mainWindow.createContent();
	outerOutputPane.setLayout(new BorderLayout());
	final var rowsPane = mainWindow.createContent();
	rowsPane.setLayout(new BoxLayout(rowsPane, BoxLayout.Y_AXIS));
	// Generate row labels
	rowsPane.add(Box.createVerticalGlue());
	for (var r = 1; r <= Dungeon.getMinRows(); r++) {
	    final var j = new JLabel(Integer.toString(r));
	    j.setLabelFor(null);
	    j.setHorizontalAlignment(SwingConstants.RIGHT);
	    j.setVerticalAlignment(SwingConstants.CENTER);
	    rowsPane.add(j);
	    if (r < Dungeon.getMinRows()) {
		rowsPane.add(Box.createVerticalGlue());
	    }
	}
	final var columnsPane = mainWindow.createContent();
	columnsPane.setLayout(new BoxLayout(columnsPane, BoxLayout.X_AXIS));
	// Generate column labels
	columnsPane.add(Box.createHorizontalGlue());
	for (var c = 1; c <= Dungeon.getMinColumns(); c++) {
	    final var j = new JLabel(Character.toString((char) (c + 64)));
	    j.setLabelFor(null);
	    j.setHorizontalAlignment(SwingConstants.CENTER);
	    j.setVerticalAlignment(SwingConstants.BOTTOM);
	    columnsPane.add(j);
	    if (c < Dungeon.getMinColumns()) {
		columnsPane.add(Box.createHorizontalGlue());
	    }
	}
	outerOutputPane.add(rowsPane, BorderLayout.WEST);
	outerOutputPane.add(columnsPane, BorderLayout.NORTH);
	return outerOutputPane;
    }

    // Constructor
    private RCLGenerator() {
	// Do nothing
    }
}
