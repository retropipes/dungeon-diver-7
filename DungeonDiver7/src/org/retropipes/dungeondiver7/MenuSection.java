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
