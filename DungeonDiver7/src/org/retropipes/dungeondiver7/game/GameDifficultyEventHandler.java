/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.retropipes.dungeondiver7.locale.DialogString;
import org.retropipes.dungeondiver7.locale.Strings;

class GameDifficultyEventHandler implements ActionListener, WindowListener {
    /**
     * 
     */
    private final GameGUI gameGUI;

    public GameDifficultyEventHandler(GameGUI gameGUI) {
        this.gameGUI = gameGUI;
        // Do nothing
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final var cmd = e.getActionCommand();
        final var gm = this.gameGUI;
        if (cmd.equals(Strings.dialog(DialogString.OK_BUTTON))) {
    	gm.difficultyDialogOKButtonClicked();
        } else {
    	gm.difficultyDialogCancelButtonClicked();
        }
    }

    @Override
    public void windowActivated(final WindowEvent e) {
        // Ignore
    }

    @Override
    public void windowClosed(final WindowEvent e) {
        // Ignore
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        this.gameGUI.difficultyDialogCancelButtonClicked();
    }

    @Override
    public void windowDeactivated(final WindowEvent e) {
        // Ignore
    }

    @Override
    public void windowDeiconified(final WindowEvent e) {
        // Ignore
    }

    @Override
    public void windowIconified(final WindowEvent e) {
        // Ignore
    }

    @Override
    public void windowOpened(final WindowEvent e) {
        // Ignore
    }
}