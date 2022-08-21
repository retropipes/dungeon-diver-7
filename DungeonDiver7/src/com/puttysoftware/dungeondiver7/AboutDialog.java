/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.desktop.AboutEvent;
import java.awt.desktop.AboutHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.strings.DianeStrings;
import com.puttysoftware.dungeondiver7.loader.LogoLoader;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Menu;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;
import com.puttysoftware.dungeondiver7.locale.old.LocaleConstants;
import com.puttysoftware.dungeondiver7.locale.old.LocaleLoader;

public class AboutDialog implements AboutHandler, MenuSection {
    // Fields
    private JFrame aboutFrame;
    private JMenuItem helpAbout, helpHelp;

    // Constructors
    AboutDialog(final String ver) {
	this.setUpGUI(ver);
    }

    // Methods
    public void showAboutDialog() {
	this.aboutFrame.setVisible(true);
    }

    void hideAboutDialog() {
	this.aboutFrame.setVisible(false);
    }

    private void setUpGUI(final String ver) {
	Container aboutPane, textPane, buttonPane, logoPane;
	JButton aboutOK;
	EventHandler handler;
	JLabel miniLabel;
	handler = new EventHandler();
	this.aboutFrame = new JFrame(Strings.dialog(DialogString.ABOUT) + LocaleConstants.COMMON_STRING_SPACE
		+ Strings.untranslated(Untranslated.PROGRAM_NAME));
	final Image iconlogo = LogoLoader.getIconLogo();
	this.aboutFrame.setIconImage(iconlogo);
	aboutPane = new Container();
	textPane = new Container();
	buttonPane = new Container();
	logoPane = new Container();
	aboutOK = new JButton(Strings.dialog(DialogString.OK_BUTTON));
	miniLabel = new JLabel(Strings.EMPTY, LogoLoader.getMiniatureLogo(), SwingConstants.LEFT);
	miniLabel.setLabelFor(null);
	aboutOK.setDefaultCapable(true);
	this.aboutFrame.getRootPane().setDefaultButton(aboutOK);
	this.aboutFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	aboutPane.setLayout(new BorderLayout());
	logoPane.setLayout(new FlowLayout());
	logoPane.add(miniLabel);
	textPane.setLayout(new GridLayout(4, 1));
	textPane.add(new JLabel(Strings.untranslated(Untranslated.PROGRAM_NAME) + LocaleConstants.COMMON_STRING_SPACE
		+ Strings.dialog(DialogString.VERSION) + LocaleConstants.COMMON_STRING_SPACE + ver));
	textPane.add(new JLabel(Strings.dialog(DialogString.AUTHOR)
		+ LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_AUTHOR_NAME)));
	textPane.add(new JLabel(Strings.dialog(DialogString.WEB_SITE) + LocaleLoader
		.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_GAME_WEB_URL)));
	textPane.add(new JLabel(Strings.dialog(DialogString.BUG_REPORTS)
		+ LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE, LocaleConstants.NOTL_STRING_GAME_EMAIL)));
	buttonPane.setLayout(new FlowLayout());
	buttonPane.add(aboutOK);
	aboutPane.add(logoPane, BorderLayout.WEST);
	aboutPane.add(textPane, BorderLayout.CENTER);
	aboutPane.add(buttonPane, BorderLayout.SOUTH);
	this.aboutFrame.setResizable(false);
	aboutOK.addActionListener(handler);
	this.aboutFrame.setContentPane(aboutPane);
	this.aboutFrame.pack();
    }

    private class EventHandler implements ActionListener {
	public EventHandler() {
	    // Do nothing
	}

	// Handle buttons
	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final AboutDialog ad = AboutDialog.this;
		final String cmd = e.getActionCommand();
		if (cmd.equals(Strings.dialog(DialogString.OK_BUTTON))) {
		    ad.hideAboutDialog();
		}
	    } catch (final Exception ex) {
		DungeonDiver7.logError(ex);
	    }
	}
    }

    private class MenuHandler implements ActionListener {
	public MenuHandler() {
	    // Do nothing
	}

	// Handle menus
	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		final StuffBag app = DungeonDiver7.getStuffBag();
		final String cmd = e.getActionCommand();
		if (cmd.equals(DianeStrings.subst(Strings.menu(Menu.ABOUT_PROGRAM),
			Strings.untranslated(Untranslated.PROGRAM_NAME)))) {
		    app.getAboutDialog().showAboutDialog();
		} else if (cmd.equals(DianeStrings.subst(Strings.menu(Menu.PROGRAM_HELP),
			Strings.untranslated(Untranslated.PROGRAM_NAME)))) {
		    app.getHelpManager().showHelp();
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
	this.helpHelp.setEnabled(true);
    }

    @Override
    public JMenu createCommandsMenu() {
	final MenuHandler mhandler = new MenuHandler();
	final JMenu helpMenu = new JMenu(Strings.menu(Menu.HELP));
	this.helpAbout = new JMenuItem(
		DianeStrings.subst(Strings.menu(Menu.ABOUT_PROGRAM), Strings.untranslated(Untranslated.PROGRAM_NAME)));
	this.helpHelp = new JMenuItem(
		DianeStrings.subst(Strings.menu(Menu.PROGRAM_HELP), Strings.untranslated(Untranslated.PROGRAM_NAME)));
	this.helpAbout.addActionListener(mhandler);
	this.helpHelp.addActionListener(mhandler);
	if (!System.getProperty(Strings.untranslated(Untranslated.OS_NAME))
		.equalsIgnoreCase(Strings.untranslated(Untranslated.MACOS))) {
	    helpMenu.add(this.helpAbout);
	}
	helpMenu.add(this.helpHelp);
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
    public void handleAbout(AboutEvent e) {
	this.showAboutDialog();
    }
}