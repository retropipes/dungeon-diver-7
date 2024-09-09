/*  Dungeon Diver 7: A Dungeon-Diving RPG
Copyleft (C) 2024-present RetroPipes
Licensed under Apache 2.0. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/retropipes/dungeon-diver-7
 */
package org.retropipes.dungeondiver7;

import javax.swing.JMenu;

public interface MenuSection {
    void attachAccelerators(final Accelerators accel);

    JMenu createCommandsMenu();

    void disableDirtyCommands();

    void disableLoadedCommands();

    void disableModeCommands();

    void enableDirtyCommands();

    void enableLoadedCommands();

    void enableModeCommands();

    void setInitialState();
}
