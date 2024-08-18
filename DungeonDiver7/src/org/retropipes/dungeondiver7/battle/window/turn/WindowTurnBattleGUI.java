/* RetroRPGCS: An RPG */
package org.retropipes.dungeondiver7.battle.window.turn;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import org.retropipes.diane.asset.image.BufferedImageIcon;
import org.retropipes.dungeondiver7.DungeonDiver7;
import org.retropipes.dungeondiver7.ai.BattleAction;
import org.retropipes.dungeondiver7.battle.BattleResult;
import org.retropipes.dungeondiver7.loader.image.ui.UiImageId;
import org.retropipes.dungeondiver7.loader.image.ui.UiImageLoader;

public class WindowTurnBattleGUI {
    private class BattleEventHandler extends AbstractAction {
	private static final long serialVersionUID = 20239525230523523L;

	public BattleEventHandler() {
	    // Do nothing
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    try {
		var success = true;
		final var cmd = e.getActionCommand();
		final var wbg = WindowTurnBattleGUI.this;
		final var b = DungeonDiver7.getStuffBag().getBattle();
		// Clear Message Area
		wbg.clearMessageArea();
		// Display Beginning Stats
		wbg.setStatusMessage("*** Beginning of Round ***");
		b.displayBattleStats();
		wbg.setStatusMessage("*** Beginning of Round ***\n");
		// Do Player Actions
		if (cmd.equals("Attack") || cmd.equals("a")) {
		    // Attack
		    success = b.doPlayerActions(BattleAction.ATTACK);
		} else if (cmd.equals("Flee") || cmd.equals("f")) {
		    // Try to Flee
		    success = b.doPlayerActions(BattleAction.FLEE);
		    if (success) {
			// Strip Extra Newline Character
			wbg.stripExtraNewLine();
			// Pack Battle Frame
			wbg.battleFrame.pack();
			// Get out of here
			b.doResult();
			return;
		    } else {
			success = b.doPlayerActions(BattleAction.ATTACK);
		    }
		} else if (cmd.equals("Continue")) {
		    // Battle Done
		    b.battleDone();
		    return;
		} else if (cmd.equals("Cast Spell") || cmd.equals("c")) {
		    // Cast Spell
		    success = b.doPlayerActions(BattleAction.CAST_SPELL);
		    if (!success) {
			// Strip Two Extra Newline Characters
			wbg.stripExtraNewLine();
			wbg.stripExtraNewLine();
			// Pack Battle Frame
			wbg.battleFrame.pack();
			// Get out of here
			return;
		    }
		} else if (cmd.equals("Steal") || cmd.equals("s")) {
		    // Steal Money
		    success = b.doPlayerActions(BattleAction.STEAL);
		} else if (cmd.equals("Drain") || cmd.equals("d")) {
		    // Drain Enemy
		    success = b.doPlayerActions(BattleAction.DRAIN);
		}
		// Maintain Player Effects
		b.maintainEffects(true);
		// Check result
		var bResult = b.getResult();
		if (bResult != BattleResult.IN_PROGRESS) {
		    b.setResult(bResult);
		    b.doResult();
		    return;
		}
		// Do Enemy Actions
		b.executeNextAIAction();
		// Maintain Enemy Effects
		b.maintainEffects(false);
		// Display Active Effects
		b.displayActiveEffects();
		// Display End Stats
		wbg.setStatusMessage("\n*** End of Round ***");
		b.displayBattleStats();
		wbg.setStatusMessage("*** End of Round ***");
		// Check Result
		bResult = b.getResult();
		if (bResult != BattleResult.IN_PROGRESS) {
		    b.setResult(bResult);
		    b.doResult();
		} else {
		    // Strip Extra Newline Character
		    wbg.stripExtraNewLine();
		    // Pack Battle Frame
		    wbg.battleFrame.pack();
		}
	    } catch (final Throwable t) {
		DungeonDiver7.logError(t);
	    }
	}
    }

    // Fields
    JFrame battleFrame;
    private final JLabel iconLabel;
    private final JTextArea messageArea;
    private final JButton attack, flee, spell, steal, drain, item, done;

    // Constructor
    public WindowTurnBattleGUI() {
	// Initialize GUI
	this.battleFrame = new JFrame("Battle");
	final Image iconlogo = UiImageLoader.load(UiImageId.ICONLOGO);
	this.battleFrame.setIconImage(iconlogo);
	Container holderPane, iconPane, messagePane, buttonPane;
	holderPane = new Container();
	iconPane = new Container();
	messagePane = new Container();
	buttonPane = new Container();
	this.iconLabel = new JLabel("");
	this.messageArea = new JTextArea();
	this.messageArea.setOpaque(true);
	this.messageArea.setEditable(false);
	this.attack = new JButton("Attack");
	this.flee = new JButton("Flee");
	this.spell = new JButton("Cast Spell");
	this.steal = new JButton("Steal");
	this.drain = new JButton("Drain");
	this.item = new JButton("Use Item");
	this.done = new JButton("Continue");
	this.battleFrame.getRootPane().setDefaultButton(this.done);
	iconPane.setLayout(new FlowLayout());
	messagePane.setLayout(new FlowLayout());
	buttonPane.setLayout(new GridLayout(2, 4));
	holderPane.setLayout(new BorderLayout());
	iconPane.add(this.iconLabel);
	messagePane.add(this.messageArea);
	buttonPane.add(this.attack);
	buttonPane.add(this.flee);
	buttonPane.add(this.spell);
	buttonPane.add(this.steal);
	buttonPane.add(this.drain);
	buttonPane.add(this.item);
	buttonPane.add(this.done);
	holderPane.add(iconPane, BorderLayout.WEST);
	holderPane.add(messagePane, BorderLayout.CENTER);
	holderPane.add(buttonPane, BorderLayout.SOUTH);
	this.battleFrame.setContentPane(holderPane);
	this.battleFrame.setResizable(false);
	this.battleFrame.pack();
	// Initialize Event Handlers
	final var handler = new BattleEventHandler();
	this.attack.addActionListener(handler);
	this.flee.addActionListener(handler);
	this.spell.addActionListener(handler);
	this.steal.addActionListener(handler);
	this.drain.addActionListener(handler);
	this.item.addActionListener(handler);
	this.done.addActionListener(handler);
	this.attack.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0),
		"Attack");
	this.attack.getActionMap().put("Attack", handler);
	this.flee.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "Flee");
	this.flee.getActionMap().put("Flee", handler);
	this.spell.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0),
		"Cast Spell");
	this.spell.getActionMap().put("Cast Spell", handler);
	this.steal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0),
		"Steal");
	this.steal.getActionMap().put("Steal", handler);
	this.drain.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0),
		"Drain");
	this.drain.getActionMap().put("Drain", handler);
	this.item.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0),
		"Use Item");
	this.item.getActionMap().put("Use Item", handler);
    }

    final void clearMessageArea() {
	this.messageArea.setText("");
    }

    void doResultCleanup() {
	// Cleanup
	this.attack.setVisible(false);
	this.flee.setVisible(false);
	this.spell.setVisible(false);
	this.steal.setVisible(false);
	this.drain.setVisible(false);
	this.item.setVisible(false);
	this.done.setVisible(true);
	this.attack.setEnabled(false);
	this.flee.setEnabled(false);
	this.spell.setEnabled(false);
	this.steal.setEnabled(false);
	this.drain.setEnabled(false);
	this.item.setEnabled(false);
	this.done.setEnabled(true);
    }

    void doResultFinalCleanup() {
	// Final Cleanup
	this.stripExtraNewLine();
	this.battleFrame.pack();
    }

    public JFrame getOutputFrame() {
	return this.battleFrame;
    }

    void initBattle(final BufferedImageIcon enemyIcon) {
	this.iconLabel.setIcon(enemyIcon);
	this.attack.setVisible(true);
	this.flee.setVisible(true);
	this.spell.setVisible(true);
	this.steal.setVisible(true);
	this.drain.setVisible(true);
	this.item.setVisible(true);
	this.done.setVisible(false);
	this.attack.setEnabled(true);
	this.flee.setEnabled(true);
	this.spell.setEnabled(true);
	this.steal.setEnabled(true);
	this.drain.setEnabled(true);
	this.item.setEnabled(true);
	this.done.setEnabled(false);
	this.battleFrame.setVisible(true);
    }

    public final void setStatusMessage(final String s) {
	this.messageArea.setText(this.messageArea.getText() + s + "\n");
    }

    final void stripExtraNewLine() {
	final var currText = this.messageArea.getText();
	this.messageArea.setText(currText.substring(0, currText.length() - 1));
    }
}