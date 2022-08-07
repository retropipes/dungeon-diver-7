/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.integration1.game;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import com.puttysoftware.diane.gui.DrawGrid;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.integration1.Application;
import com.puttysoftware.dungeondiver7.integration1.Integration1;
import com.puttysoftware.dungeondiver7.integration1.dungeon.CurrentDungeon;
import com.puttysoftware.dungeondiver7.integration1.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.Darkness;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.Player;
import com.puttysoftware.dungeondiver7.integration1.dungeon.objects.Wall;
import com.puttysoftware.dungeondiver7.loader.ImageCompositor;
import com.puttysoftware.dungeondiver7.loader.MusicConstants;
import com.puttysoftware.dungeondiver7.loader.MusicLoader;
import com.puttysoftware.dungeondiver7.loader.ObjectImageManager;
import com.puttysoftware.dungeondiver7.manager.dungeon.DungeonManager;
import com.puttysoftware.dungeondiver7.prefs.PrefsManager;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.images.BufferedImageIcon;

class GameGUI {
    // Fields
    private JFrame outputFrame;
    private Container borderPane;
    private JLabel messageLabel;
    private GameViewingWindowManager vwMgr = null;
    private final StatGUI sg;
    private DrawGrid drawGrid;
    private GameDraw outputPane;
    private boolean knm;
    private boolean deferredRedraw;
    boolean eventFlag;
    private static Darkness DARK = new Darkness();

    // Constructors
    public GameGUI() {
	this.deferredRedraw = false;
	this.eventFlag = true;
	this.sg = new StatGUI();
    }

    // Methods
    public void updateStats() {
	this.sg.updateStats();
    }

    public void enableEvents() {
	this.outputFrame.setEnabled(true);
	this.eventFlag = true;
    }

    public void disableEvents() {
	this.outputFrame.setEnabled(false);
	this.eventFlag = false;
    }

    void initViewManager() {
	if (this.vwMgr == null) {
	    this.vwMgr = Integration1.getApplication().getGameLogic().getViewManager();
	    this.setUpGUI();
	}
    }

    void viewingWindowSizeChanged() {
	this.setUpGUI();
	this.updateGameGUI();
	this.deferredRedraw = true;
    }

    public JFrame getOutputFrame() {
	return this.outputFrame;
    }

    public void showOutput() {
	if (MusicLoader.isMusicPlaying()) {
	    MusicLoader.stopMusic();
	}
	int zoneID = PartyManager.getParty().getZone();
	if (zoneID == CurrentDungeon.getMaxLevels() - 1) {
	    MusicLoader.playMusic(MusicConstants.MUSIC_LAIR);
	} else {
	    MusicLoader.playMusic(MusicConstants.MUSIC_DUNGEON);
	}
	this.showOutputCommon();
    }

    public void showOutputAndKeepMusic() {
	this.showOutputCommon();
    }

    private void showOutputCommon() {
	final Application app = Integration1.getApplication();
	if (!this.outputFrame.isVisible()) {
	    app.getMenuManager().setGameMenus();
	    this.outputFrame.setVisible(true);
	    this.outputFrame.setJMenuBar(app.getMenuManager().getMainMenuBar());
	    if (this.deferredRedraw) {
		this.deferredRedraw = false;
		this.redrawDungeon();
	    }
	    this.updateStats();
	}
    }

    public void hideOutput() {
	if (this.outputFrame != null) {
	    this.outputFrame.setVisible(false);
	}
    }

    public void setStatusMessage(final String msg) {
	this.messageLabel.setText(msg);
    }

    private void resetBorderPane() {
	this.borderPane.removeAll();
	this.borderPane.add(this.outputPane, BorderLayout.CENTER);
	this.borderPane.add(this.messageLabel, BorderLayout.NORTH);
	this.borderPane.add(this.sg.getStatsPane(), BorderLayout.EAST);
    }

    public void redrawDungeon() {
	// Draw the maze, if it is visible
	if (this.outputFrame.isVisible()) {
	    final Application app = Integration1.getApplication();
	    final CurrentDungeon m = app.getDungeonManager().getDungeon();
	    int x, y, u, v;
	    int xFix, yFix;
	    boolean visible;
	    u = m.getPlayerLocationX();
	    v = m.getPlayerLocationY();
	    final AbstractDungeonObject wall = new Wall();
	    for (x = this.vwMgr.getViewingWindowLocationX(); x <= this.vwMgr
		    .getLowerRightViewingWindowLocationX(); x++) {
		for (y = this.vwMgr.getViewingWindowLocationY(); y <= this.vwMgr
			.getLowerRightViewingWindowLocationY(); y++) {
		    xFix = x - this.vwMgr.getViewingWindowLocationX();
		    yFix = y - this.vwMgr.getViewingWindowLocationY();
		    visible = app.getDungeonManager().getDungeon().isSquareVisible(u, v, y, x);
		    try {
			if (visible) {
			    final AbstractDungeonObject obj1 = m.getCell(y, x, DungeonConstants.LAYER_LOWER_GROUND);
			    final AbstractDungeonObject obj2 = m.getCell(y, x, DungeonConstants.LAYER_LOWER_OBJECTS);
			    final BufferedImageIcon img1 = obj1.gameRenderHook(y, x);
			    final BufferedImageIcon img2 = obj2.gameRenderHook(y, x);
			    if (u == y && v == x) {
				final AbstractDungeonObject obj3 = new Player();
				final BufferedImageIcon img3 = obj3.gameRenderHook(y, x);
				this.drawGrid.setImageCell(ImageCompositor.getVirtualCompositeImage(img1, img2, img3,
					ImageCompositor.getGraphicSize()), xFix, yFix);
			    } else {
				this.drawGrid.setImageCell(
					ImageCompositor.getCompositeImage(img1, img2, ImageCompositor.getGraphicSize()),
					xFix, yFix);
			    }
			} else {
			    this.drawGrid.setImageCell(
				    ObjectImageManager.getImage(GameGUI.DARK.getName(), GameGUI.DARK.getBaseID()), xFix,
				    yFix);
			}
		    } catch (final ArrayIndexOutOfBoundsException ae) {
			this.drawGrid.setImageCell(wall.gameRenderHook(y, x), xFix, yFix);
		    }
		}
	    }
	    if (this.knm) {
		this.knm = false;
	    } else {
		this.setStatusMessage(" ");
	    }
	    this.outputPane.repaint();
	    this.outputFrame.pack();
	    this.showOutputAndKeepMusic();
	}
    }

    public void keepNextMessage() {
	this.knm = true;
    }

    void updateGameGUI() {
	this.resetBorderPane();
	this.sg.updateImages();
	this.sg.updateStats();
    }

    private void setUpGUI() {
	final EventHandler handler = new EventHandler();
	this.borderPane = new Container();
	this.borderPane.setLayout(new BorderLayout());
	this.messageLabel = new JLabel(" ");
	this.messageLabel.setOpaque(true);
	this.outputFrame = new JFrame("Chrystalz");
	final Image iconlogo = Application.getIconLogo();
	this.outputFrame.setIconImage(iconlogo);
	this.drawGrid = new DrawGrid(PrefsManager.getViewingWindowSize());
	this.outputPane = new GameDraw(this.drawGrid);
	this.outputFrame.setContentPane(this.borderPane);
	this.outputFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	this.outputFrame.setResizable(false);
	this.outputFrame.addKeyListener(handler);
	this.outputFrame.addWindowListener(handler);
    }

    private class EventHandler implements KeyListener, WindowListener {
	EventHandler() {
	    // Do nothing
	}

	@Override
	public void keyPressed(final KeyEvent e) {
	    if (GameGUI.this.eventFlag) {
		if (!PrefsManager.oneMove()) {
		    this.handleMovement(e);
		}
	    }
	}

	@Override
	public void keyReleased(final KeyEvent e) {
	    if (GameGUI.this.eventFlag) {
		if (PrefsManager.oneMove()) {
		    this.handleMovement(e);
		}
	    }
	}

	@Override
	public void keyTyped(final KeyEvent e) {
	    // Do nothing
	}

	public void handleMovement(final KeyEvent e) {
	    try {
		final GameLogic glm = Integration1.getApplication().getGameLogic();
		final int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_LEFT:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(-1, -1);
		    } else {
			glm.updatePositionRelative(-1, 0);
		    }
		    break;
		case KeyEvent.VK_DOWN:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(-1, 1);
		    } else {
			glm.updatePositionRelative(0, 1);
		    }
		    break;
		case KeyEvent.VK_RIGHT:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(1, 1);
		    } else {
			glm.updatePositionRelative(1, 0);
		    }
		    break;
		case KeyEvent.VK_UP:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(1, -1);
		    } else {
			glm.updatePositionRelative(0, -1);
		    }
		    break;
		case KeyEvent.VK_ENTER:
		    if (e.isShiftDown()) {
			glm.updatePositionRelative(0, 0);
		    }
		    break;
		case KeyEvent.VK_SPACE:
		    final Application app = Integration1.getApplication();
		    final CurrentDungeon m = app.getDungeonManager().getDungeon();
		    int px = m.getPlayerLocationX();
		    int py = m.getPlayerLocationY();
		    AbstractDungeonObject there = new Empty();
		    try {
			there = m.getCell(px, py, DungeonConstants.LAYER_LOWER_OBJECTS);
		    } catch (final ArrayIndexOutOfBoundsException ae) {
			// Ignore
		    }
		    there.interactAction();
		    break;
		case KeyEvent.VK_NUMPAD7:
		case KeyEvent.VK_Q:
		    glm.updatePositionRelative(-1, -1);
		    break;
		case KeyEvent.VK_NUMPAD8:
		case KeyEvent.VK_W:
		    glm.updatePositionRelative(0, -1);
		    break;
		case KeyEvent.VK_NUMPAD9:
		case KeyEvent.VK_E:
		    glm.updatePositionRelative(1, -1);
		    break;
		case KeyEvent.VK_NUMPAD4:
		case KeyEvent.VK_A:
		    glm.updatePositionRelative(-1, 0);
		    break;
		case KeyEvent.VK_NUMPAD5:
		case KeyEvent.VK_S:
		    glm.updatePositionRelative(0, 0);
		    break;
		case KeyEvent.VK_NUMPAD6:
		case KeyEvent.VK_D:
		    glm.updatePositionRelative(1, 0);
		    break;
		case KeyEvent.VK_NUMPAD1:
		case KeyEvent.VK_Z:
		    glm.updatePositionRelative(-1, 1);
		    break;
		case KeyEvent.VK_NUMPAD2:
		case KeyEvent.VK_X:
		    glm.updatePositionRelative(0, 1);
		    break;
		case KeyEvent.VK_NUMPAD3:
		case KeyEvent.VK_C:
		    glm.updatePositionRelative(1, 1);
		    break;
		default:
		    break;
		}
	    } catch (final Exception ex) {
		DungeonDiver7.getErrorLogger().logError(ex);
	    }
	}

	// Handle windows
	@Override
	public void windowActivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowClosed(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowClosing(final WindowEvent we) {
	    try {
		final Application app = Integration1.getApplication();
		boolean success = false;
		int status = 0;
		if (app.getDungeonManager().getDirty()) {
		    app.getDungeonManager();
		    status = DungeonManager.showSaveDialog();
		    if (status == JOptionPane.YES_OPTION) {
			app.getDungeonManager();
			success = DungeonManager.saveGame();
			if (success) {
			    app.getGameLogic().exitGame();
			}
		    } else if (status == JOptionPane.NO_OPTION) {
			app.getGameLogic().exitGame();
		    }
		} else {
		    app.getGameLogic().exitGame();
		}
	    } catch (final Exception ex) {
		DungeonDiver7.getErrorLogger().logError(ex);
	    }
	}

	@Override
	public void windowDeactivated(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowDeiconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowIconified(final WindowEvent we) {
	    // Do nothing
	}

	@Override
	public void windowOpened(final WindowEvent we) {
	    // Do nothing
	}
    }
}
