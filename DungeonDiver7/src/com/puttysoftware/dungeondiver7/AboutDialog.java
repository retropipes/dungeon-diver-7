/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.desktop.AboutEvent;
import java.awt.desktop.AboutHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.puttysoftware.diane.gui.MainWindow;
import com.puttysoftware.diane.locale.DianeStrings;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Menu;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;

public class AboutDialog implements AboutHandler, MenuSection {
	// Fields
	private MainWindow mainWindow;
	private JPanel aboutPane;
	private JMenuItem helpAbout;

	// Constructors
	AboutDialog(final String ver) {
		this.setUpGUI(ver);
	}

	public void showAboutDialog() {
		this.mainWindow.setAndSave(this.aboutPane, DianeStrings.subst(Strings.dialog(DialogString.ABOUT),
				Strings.untranslated(Untranslated.PROGRAM_NAME)));
	}

	void hideAboutDialog() {
		this.mainWindow.restoreSaved();
	}

	private void setUpGUI(final String ver) {
		JPanel textPane, buttonPane, logoPane;
		JButton aboutOK;
		EventHandler handler;
		JLabel miniLabel;
		handler = new EventHandler();
		this.mainWindow = MainWindow.mainWindow();
		this.aboutPane = new JPanel();
		textPane = new JPanel();
		buttonPane = new JPanel();
		logoPane = new JPanel();
		aboutOK = new JButton(Strings.dialog(DialogString.OK_BUTTON));
		miniLabel = new JLabel(Strings.EMPTY, LogoLoader.getMiniatureLogo(), SwingConstants.LEFT);
		miniLabel.setLabelFor(null);
		aboutOK.setDefaultCapable(true);
		this.mainWindow.setDefaultButton(aboutOK);
		this.aboutPane.setLayout(new BorderLayout());
		logoPane.setLayout(new FlowLayout());
		logoPane.add(miniLabel);
		textPane.setLayout(new GridLayout(4, 1));
		textPane.add(new JLabel(DianeStrings.subst(Strings.dialog(DialogString.VERSION),
				Strings.untranslated(Untranslated.PROGRAM_NAME), ver)));
		textPane.add(new JLabel(DianeStrings.subst(Strings.dialog(DialogString.AUTHOR),
				Strings.untranslated(Untranslated.GAME_AUTHOR_NAME))));
		textPane.add(new JLabel(DianeStrings.subst(Strings.dialog(DialogString.WEB_SITE),
				Strings.untranslated(Untranslated.GAME_WEB_URL))));
		textPane.add(new JLabel(DianeStrings.subst(Strings.dialog(DialogString.BUG_REPORTS),
				Strings.untranslated(Untranslated.GAME_EMAIL))));
		buttonPane.setLayout(new FlowLayout());
		buttonPane.add(aboutOK);
		this.aboutPane.add(logoPane, BorderLayout.WEST);
		this.aboutPane.add(textPane, BorderLayout.CENTER);
		this.aboutPane.add(buttonPane, BorderLayout.SOUTH);
		aboutOK.addActionListener(handler);
	}

	private class EventHandler implements ActionListener {
		public EventHandler() {
			// Do nothing
		}

		// Handle buttons
		@Override
		public void actionPerformed(final ActionEvent e) {
			try {
				final var ad = AboutDialog.this;
				final var cmd = e.getActionCommand();
				if (cmd.equals(Strings.dialog(DialogString.OK_BUTTON))) {
					ad.hideAboutDialog();
				}
			} catch (final Exception ex) {
				DungeonDiver7.logError(ex);
			}
		}
	}

	private static class MenuHandler implements ActionListener {
		public MenuHandler() {
			// Do nothing
		}

		// Handle menus
		@Override
		public void actionPerformed(final ActionEvent e) {
			try {
				final var app = DungeonDiver7.getStuffBag();
				final var cmd = e.getActionCommand();
				if (cmd.equals(DianeStrings.subst(Strings.menu(Menu.ABOUT_PROGRAM),
						Strings.untranslated(Untranslated.PROGRAM_NAME)))) {
					app.getAboutDialog().showAboutDialog();
				}
				app.getMenuManager().checkFlags();
			} catch (final Exception ex) {
				DungeonDiver7.logError(ex);
			}
		}
	}

	@Override
	public void enableModeCommands() {
		// Do nothing
	}

	@Override
	public void disableModeCommands() {
		// Do nothing
	}

	@Override
	public void setInitialState() {
		this.helpAbout.setEnabled(true);
	}

	@Override
	public JMenu createCommandsMenu() {
		final var mhandler = new MenuHandler();
		final var helpMenu = new JMenu(Strings.menu(Menu.HELP));
		this.helpAbout = new JMenuItem(
				DianeStrings.subst(Strings.menu(Menu.ABOUT_PROGRAM), Strings.untranslated(Untranslated.PROGRAM_NAME)));
		this.helpAbout.addActionListener(mhandler);
		if (!System.getProperty(Strings.untranslated(Untranslated.OS_NAME))
				.equalsIgnoreCase(Strings.untranslated(Untranslated.MACOS))) {
			helpMenu.add(this.helpAbout);
		}
		return helpMenu;
	}

	@Override
	public void attachAccelerators(final Accelerators accel) {
		// Do nothing
	}

	@Override
	public void enableLoadedCommands() {
		// Do nothing
	}

	@Override
	public void disableLoadedCommands() {
		// Do nothing
	}

	@Override
	public void enableDirtyCommands() {
		// Do nothing
	}

	@Override
	public void disableDirtyCommands() {
		// Do nothing
	}

	@Override
	public void handleAbout(final AboutEvent e) {
		this.showAboutDialog();
	}
}