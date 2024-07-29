/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package org.retropipes.dungeondiver7.battle;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import org.retropipes.diane.asset.image.ImageCompositor;
import org.retropipes.diane.drawgrid.DrawGrid;
import org.retropipes.diane.gui.MainContent;
import org.retropipes.diane.gui.MainWindow;
import org.retropipes.diane.gui.dialog.CommonDialogs;
import org.retropipes.diane.integration.Integration;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.ai.AbstractMapAIRoutine;
import org.retropipes.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import org.retropipes.dungeondiver7.dungeon.objects.Darkness;
import org.retropipes.dungeondiver7.dungeon.objects.Wall;
import org.retropipes.dungeondiver7.loader.sound.SoundLoader;
import org.retropipes.dungeondiver7.loader.sound.Sounds;
import org.retropipes.dungeondiver7.locale.Strings;
import org.retropipes.dungeondiver7.prefs.Prefs;
import org.retropipes.dungeondiver7.utility.DungeonConstants;

class MapBattleGUI {
	private class EventHandler extends AbstractAction implements KeyListener {
		private static final long serialVersionUID = 20239525230523524L;

		public EventHandler() {
			// Do nothing
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			try {
				if (e.getSource() instanceof JButton) {
					SoundLoader.playSound(Sounds.CLICK);
				}
				final var cmd = e.getActionCommand();
				final var b = DungeonDiver7.getStuffBag().getBattle();
				// Do Player Actions
				if (cmd.equals("Cast Spell") || cmd.equals("c")) {
					// Cast Spell
					b.doPlayerActions(AbstractMapAIRoutine.ACTION_CAST_SPELL);
				} else if (cmd.equals("Steal") || cmd.equals("t")) {
					// Steal Money
					b.doPlayerActions(AbstractMapAIRoutine.ACTION_STEAL);
				} else if (cmd.equals("Drain") || cmd.equals("d")) {
					// Drain Enemy
					b.doPlayerActions(AbstractMapAIRoutine.ACTION_DRAIN);
				} else if (cmd.equals("End Turn") || cmd.equals("e")) {
					// End Turn
					b.endTurn();
				}
			} catch (final Throwable t) {
				DungeonDiver7.logError(t);
			}
		}

		private void handleMovement(final KeyEvent e) {
			try {
				if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
					if (e.isMetaDown()) {
						return;
					}
				} else if (e.isControlDown()) {
					return;
				}
				final var bl = DungeonDiver7.getStuffBag().getBattle();
				final var bg = MapBattleGUI.this;
				if (bg.eventHandlersOn) {
					final var keyCode = e.getKeyCode();
					switch (keyCode) {
					case KeyEvent.VK_NUMPAD4:
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_A:
						bl.updatePosition(-1, 0);
						break;
					case KeyEvent.VK_NUMPAD2:
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_X:
						bl.updatePosition(0, 1);
						break;
					case KeyEvent.VK_NUMPAD6:
					case KeyEvent.VK_RIGHT:
					case KeyEvent.VK_D:
						bl.updatePosition(1, 0);
						break;
					case KeyEvent.VK_NUMPAD8:
					case KeyEvent.VK_UP:
					case KeyEvent.VK_W:
						bl.updatePosition(0, -1);
						break;
					case KeyEvent.VK_NUMPAD7:
					case KeyEvent.VK_Q:
						bl.updatePosition(-1, -1);
						break;
					case KeyEvent.VK_NUMPAD9:
					case KeyEvent.VK_E:
						bl.updatePosition(1, -1);
						break;
					case KeyEvent.VK_NUMPAD3:
					case KeyEvent.VK_C:
						bl.updatePosition(1, 1);
						break;
					case KeyEvent.VK_NUMPAD1:
					case KeyEvent.VK_Z:
						bl.updatePosition(-1, 1);
						break;
					case KeyEvent.VK_NUMPAD5:
					case KeyEvent.VK_S:
						// Confirm before attacking self
						final var res = CommonDialogs.showConfirmDialog("Are you sure you want to attack yourself?",
								"Battle");
						if (res == CommonDialogs.YES_OPTION) {
							bl.updatePosition(0, 0);
						}
						break;
					default:
						break;
					}
				}
			} catch (final Exception ex) {
				DungeonDiver7.logError(ex);
			}
		}

		@Override
		public void keyPressed(final KeyEvent e) {
			if (!Prefs.oneMove()) {
				this.handleMovement(e);
			}
		}

		@Override
		public void keyReleased(final KeyEvent e) {
			if (Prefs.oneMove()) {
				this.handleMovement(e);
			}
		}

		@Override
		public void keyTyped(final KeyEvent e) {
			// Do nothing
		}
	}

	private static final int MAX_TEXT = 1000;
	// Fields
	private MainWindow mainWindow;
	private EventHandler handler;
	private MainContent borderPane;
	private MapBattleDraw battlePane;
	private JLabel messageLabel;
	private final MapBattleViewingWindowManager vwMgr;
	private final MapBattleStats bs;
	private final MapBattleEffects be;
	private DrawGrid drawGrid;
	boolean eventHandlersOn;
	private JButton spell, steal, drain, end;

	// Constructors
	MapBattleGUI() {
		this.vwMgr = new MapBattleViewingWindowManager();
		this.bs = new MapBattleStats();
		this.be = new MapBattleEffects();
		this.setUpGUI();
		this.eventHandlersOn = true;
	}

	boolean areEventHandlersOn() {
		return this.eventHandlersOn;
	}

	void clearStatusMessage() {
		this.messageLabel.setText(" ");
	}

	MapBattleViewingWindowManager getViewManager() {
		return this.vwMgr;
	}

	void hideBattle() {
		if (this.mainWindow != null) {
			this.mainWindow.removeKeyListener(this.handler);
			this.mainWindow.restoreSaved();
		}
	}

	void redrawBattle(final MapBattleDefinitions bd) {
		// Draw the battle, if it is visible
		if (this.mainWindow.checkContent(this.battlePane)) {
			int x, y;
			int xFix, yFix;
			final var xView = this.vwMgr.getViewingWindowLocationX();
			final var yView = this.vwMgr.getViewingWindowLocationY();
			final var xlView = this.vwMgr.getLowerRightViewingWindowLocationX();
			final var ylView = this.vwMgr.getLowerRightViewingWindowLocationY();
			for (x = xView; x <= xlView; x++) {
				for (y = yView; y <= ylView; y++) {
					xFix = x - xView;
					yFix = y - yView;
					try {
						final var lgobj = bd.getBattleDungeon().getCell(y, x, 0, DungeonConstants.LAYER_LOWER_GROUND);
						final var ugobj = bd.getBattleDungeon().getCell(y, x, 0, DungeonConstants.LAYER_UPPER_GROUND);
						final var lgimg = lgobj.battleRenderHook();
						final var ugimg = ugobj.battleRenderHook();
						final var cacheName = Strings.compositeCacheName(lgobj.getCacheName(), ugobj.getCacheName());
						final var img = ImageCompositor.composite(cacheName, lgimg, ugimg);
						this.drawGrid.setImageCell(img, xFix, yFix);
					} catch (final ArrayIndexOutOfBoundsException ae) {
						final var wall = new Wall();
						this.drawGrid.setImageCell(wall.battleRenderHook(), xFix, yFix);
					}
				}
			}
			this.battlePane.repaint();
		}
	}

	void redrawOneBattleSquare(final MapBattleDefinitions bd, final int x, final int y,
			final AbstractDungeonObject obj3) {
		// Draw the battle, if it is visible
		if (this.mainWindow.checkContent(this.battlePane)) {
			try {
				int xFix, yFix;
				final var xView = this.vwMgr.getViewingWindowLocationX();
				final var yView = this.vwMgr.getViewingWindowLocationY();
				xFix = y - xView;
				yFix = x - yView;
				final var lgobj = bd.getBattleDungeon().getCell(y, x, 0, DungeonConstants.LAYER_LOWER_GROUND);
				final var ugobj = bd.getBattleDungeon().getCell(y, x, 0, DungeonConstants.LAYER_UPPER_GROUND);
				final var lgimg = lgobj.battleRenderHook();
				final var ugimg = ugobj.battleRenderHook();
				final var o3img = obj3.battleRenderHook();
				final var cacheName = Strings.compositeCacheName(lgobj.getCacheName(), ugobj.getCacheName());
				final var img = ImageCompositor.composite(cacheName, lgimg, ugimg, o3img);
				this.drawGrid.setImageCell(img, xFix, yFix);
				this.battlePane.repaint();
			} catch (final ArrayIndexOutOfBoundsException ae) {
				// Do nothing
			}
		}
	}

	void setStatusMessage(final String msg) {
		if (this.messageLabel.getText().length() > MapBattleGUI.MAX_TEXT) {
			this.clearStatusMessage();
		}
		if (!msg.isEmpty() && !msg.matches("\\s+")) {
			this.messageLabel.setText(msg);
		}
	}

	private void setUpGUI() {
		this.handler = new EventHandler();
		this.mainWindow = MainWindow.mainWindow();
		this.borderPane = this.mainWindow.createContent();
		final var buttonPane = this.mainWindow.createContent();
		this.borderPane.setLayout(new BorderLayout());
		this.messageLabel = new JLabel(" ");
		this.messageLabel.setOpaque(true);
		this.spell = new JButton("Cast Spell");
		this.steal = new JButton("Steal");
		this.drain = new JButton("Drain");
		this.end = new JButton("End Turn");
		buttonPane.setLayout(new GridLayout(5, 1));
		buttonPane.add(this.spell);
		buttonPane.add(this.steal);
		buttonPane.add(this.drain);
		buttonPane.add(this.end);
		this.spell.setFocusable(false);
		this.steal.setFocusable(false);
		this.drain.setFocusable(false);
		this.end.setFocusable(false);
		this.spell.addActionListener(this.handler);
		this.steal.addActionListener(this.handler);
		this.drain.addActionListener(this.handler);
		this.end.addActionListener(this.handler);
		int modKey;
		if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) {
			modKey = InputEvent.META_DOWN_MASK;
		} else {
			modKey = InputEvent.CTRL_DOWN_MASK;
		}
		this.spell.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, modKey),
				"Cast Spell");
		this.spell.getActionMap().put("Cast Spell", this.handler);
		this.steal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_T, modKey),
				"Steal");
		this.steal.getActionMap().put("Steal", this.handler);
		this.drain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, modKey),
				"Drain");
		this.drain.getActionMap().put("Drain", this.handler);
		this.end.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, modKey),
				"End Turn");
		this.end.getActionMap().put("End Turn", this.handler);
		this.drawGrid = new DrawGrid(MapBattleViewingWindowManager.getViewingWindowSize());
		for (var x = 0; x < MapBattleViewingWindowManager.getViewingWindowSize(); x++) {
			for (var y = 0; y < MapBattleViewingWindowManager.getViewingWindowSize(); y++) {
				final AbstractDungeonObject dark = new Darkness();
				this.drawGrid.setImageCell(dark.battleRenderHook(), x, y);
			}
		}
		this.battlePane = new MapBattleDraw(this.drawGrid);
		this.borderPane.add(this.battlePane, BorderLayout.CENTER);
		this.borderPane.add(buttonPane, BorderLayout.WEST);
		this.borderPane.add(this.messageLabel, BorderLayout.NORTH);
		this.borderPane.add(this.bs.getStatsPane(), BorderLayout.EAST);
		this.borderPane.add(this.be.getEffectsPane(), BorderLayout.SOUTH);
	}

	void showBattle() {
		Integration.integrate().setDefaultMenuBar(DungeonDiver7.getStuffBag().getMenuManager().getMainMenuBar());
		this.mainWindow.setAndSave(this.borderPane, "Battle");
		this.mainWindow.addKeyListener(this.handler);
	}

	void turnEventHandlersOff() {
		this.eventHandlersOn = false;
		this.spell.setEnabled(false);
		this.steal.setEnabled(false);
		this.drain.setEnabled(false);
		this.end.setEnabled(false);
	}

	void turnEventHandlersOn() {
		this.eventHandlersOn = true;
		this.spell.setEnabled(true);
		this.steal.setEnabled(true);
		this.drain.setEnabled(true);
		this.end.setEnabled(true);
	}

	void updateStatsAndEffects(final MapBattleDefinitions bd) {
		this.bs.updateStats(bd.getActiveCharacter());
		this.be.updateEffects(bd.getActiveCharacter());
	}
}
