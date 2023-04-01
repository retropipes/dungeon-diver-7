/*  Chrystalz: A dungeon-crawling, roguelike game
Licensed under MIT. See the LICENSE file for details.

All support is handled via the GitHub repository: https://github.com/IgnitionIglooGames/chrystalz
 */
package com.puttysoftware.dungeondiver7.game;

import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.dungeondiver7.DungeonDiver7;
import com.puttysoftware.dungeondiver7.creature.party.PartyManager;
import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;
import com.puttysoftware.dungeondiver7.dungeon.objects.Empty;
import com.puttysoftware.dungeondiver7.dungeon.objects.Wall;
import com.puttysoftware.dungeondiver7.loader.Sounds;
import com.puttysoftware.dungeondiver7.loader.SoundLoader;
import com.puttysoftware.dungeondiver7.utility.DungeonConstants;
import com.puttysoftware.dungeondiver7.utility.DungeonObjectTypes;

final class MovementTask extends Thread {
    // Fields
    private final GameViewingWindowManager vwMgr;
    private final GameGUI gui;
    private AbstractDungeonObject saved;
    private boolean proceed;
    private boolean relative;
    private int moveX, moveY;

    // Constructors
    public MovementTask(final GameViewingWindowManager view, final GameGUI gameGUI) {
        this.setName("Movement Handler");
        this.vwMgr = view;
        this.gui = gameGUI;
        this.saved = new Empty();
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.waitForWork();
                if (this.relative) {
                    this.updatePositionRelative(this.moveX, this.moveY);
                }
                if (!this.relative) {
                    this.updatePositionAbsolute(this.moveX, this.moveY);
                }
            }
        } catch (final Throwable t) {
            DungeonDiver7.logError(t);
        }
    }

    private synchronized void waitForWork() {
        try {
            this.wait();
        } catch (final InterruptedException e) {
            // Ignore
        }
    }

    public synchronized void moveRelative(final int x, final int y) {
        this.moveX = x;
        this.moveY = y;
        this.relative = true;
        this.notify();
    }

    public synchronized void moveAbsolute(final int x, final int y) {
        this.moveX = x;
        this.moveY = y;
        this.relative = false;
        this.notify();
    }

    public void stopMovement() {
        this.proceed = false;
    }

    void fireStepActions() {
        final var m = DungeonDiver7.getStuffBag().getDungeonManager().getDungeon();
        final var px = m.getPlayerLocationX(0);
        final var py = m.getPlayerLocationY(0);
        m.updateVisibleSquares(px, py, 0);
        m.tickTimers();
        this.gui.updateStats();
        MovementTask.checkGameOver();
    }

    private void updatePositionRelative(final int dirX, final int dirY) {
        final var app = DungeonDiver7.getStuffBag();
        final var m = app.getDungeonManager().getDungeon();
        var px = m.getPlayerLocationX(0);
        var py = m.getPlayerLocationY(0);
        final var pz = 0;
        final var fX = dirX;
        final var fY = dirY;
        this.proceed = false;
        AbstractDungeonObject below = null;
        AbstractDungeonObject nextBelow = null;
        AbstractDungeonObject nextAbove = new Wall();
        do {
            try {
                below = m.getCell(px, py, 0, DungeonConstants.LAYER_LOWER_GROUND);
            } catch (final ArrayIndexOutOfBoundsException ae) {
                below = new Empty();
            }
            try {
                nextBelow = m.getCell(px + fX, py + fY, 0, DungeonConstants.LAYER_LOWER_GROUND);
            } catch (final ArrayIndexOutOfBoundsException ae) {
                nextBelow = new Empty();
            }
            try {
                nextAbove = m.getCell(px + fX, py + fY, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
            } catch (final ArrayIndexOutOfBoundsException ae) {
                nextAbove = new Wall();
            }
            try {
                this.proceed = nextAbove.preMoveAction(true, px + fX, py + fY);
            } catch (final ArrayIndexOutOfBoundsException ae) {
                this.proceed = true;
            }
            if (this.proceed) {
                m.savePlayerLocation();
                this.vwMgr.saveViewingWindow();
                try {
                    if (MovementTask.checkSolid(this.saved, below, nextBelow, nextAbove)) {
                        AbstractDungeonObject groundInto;
                        m.offsetPlayerLocationX(fX, 0);
                        m.offsetPlayerLocationY(fY, 0);
                        px += fX;
                        py += fY;
                        this.vwMgr.offsetViewingWindowLocationX(fY);
                        this.vwMgr.offsetViewingWindowLocationY(fX);
                        app.getDungeonManager().setDirty(true);
                        app.saveFormerMode();
                        this.fireStepActions();
                        this.redrawDungeon();
                        if (app.modeChanged()) {
                            this.proceed = false;
                        }
                        if (this.proceed) {
                            this.saved = m.getCell(px, py, 0, DungeonConstants.LAYER_LOWER_OBJECTS);
                            groundInto = m.getCell(px, py, 0, DungeonConstants.LAYER_LOWER_GROUND);
                            if (groundInto.overridesDefaultPostMove()) {
                                groundInto.postMoveAction(px, py, pz);
                                if (!this.saved.isOfType(DungeonObjectTypes.TYPE_PASS_THROUGH)) {
                                    this.saved.postMoveAction(px, py, pz);
                                }
                            } else {
                                this.saved.postMoveAction(px, py, pz);
                            }
                        }
                    } else {
                        // Move failed - object is solid in that direction
                        MovementTask.fireMoveFailedActions(px + fX, py + fY, this.saved, below, nextBelow, nextAbove);
                        this.fireStepActions();
                    }
                } catch (final ArrayIndexOutOfBoundsException ae) {
                    this.vwMgr.restoreViewingWindow();
                    m.restorePlayerLocation();
                    // Move failed - attempted to go outside the maze
                    nextAbove.moveFailedAction(px, py, pz);
                    app.showMessage("Can't go that way");
                    nextAbove = new Empty();
                    this.proceed = false;
                }
                this.fireStepActions();
            } else {
                // Move failed - pre-move check failed
                nextAbove.moveFailedAction(px + fX, py + fY, pz);
                this.fireStepActions();
                this.proceed = false;
            }
            px = m.getPlayerLocationX(0);
            py = m.getPlayerLocationY(0);
        } while (this.checkLoopCondition(below, nextBelow, nextAbove));
    }

    private boolean checkLoopCondition(final AbstractDungeonObject below, final AbstractDungeonObject nextBelow,
            final AbstractDungeonObject nextAbove) {
        return this.proceed && !nextBelow.hasFriction()
                && MovementTask.checkSolid(this.saved, below, nextBelow, nextAbove);
    }

    private static boolean checkSolid(final AbstractDungeonObject inside, final AbstractDungeonObject below,
            final AbstractDungeonObject nextBelow, final AbstractDungeonObject nextAbove) {
        final var insideSolid = inside.isSolid();
        final var belowSolid = below.isSolid();
        final var nextBelowSolid = nextBelow.isSolid();
        final var nextAboveSolid = nextAbove.isSolid();
        if (insideSolid || belowSolid || nextBelowSolid || nextAboveSolid) {
            return false;
        }
        return true;
    }

    private static void fireMoveFailedActions(final int x, final int y, final AbstractDungeonObject inside,
            final AbstractDungeonObject below, final AbstractDungeonObject nextBelow,
            final AbstractDungeonObject nextAbove) {
        final var insideSolid = inside.isSolid();
        final var belowSolid = below.isSolid();
        final var nextBelowSolid = nextBelow.isSolid();
        final var nextAboveSolid = nextAbove.isSolid();
        final var z = 0;
        if (insideSolid) {
            inside.moveFailedAction(x, y, z);
        }
        if (belowSolid) {
            below.moveFailedAction(x, y, z);
        }
        if (nextBelowSolid) {
            nextBelow.moveFailedAction(x, y, z);
        }
        if (nextAboveSolid) {
            nextAbove.moveFailedAction(x, y, z);
        }
    }

    private void updatePositionAbsolute(final int x, final int y) {
        final var app = DungeonDiver7.getStuffBag();
        final var m = app.getDungeonManager().getDungeon();
        try {
            m.getCell(x, y, 0, DungeonConstants.LAYER_LOWER_OBJECTS).preMoveAction(true, x, y);
        } catch (final ArrayIndexOutOfBoundsException ae) {
            // Ignore
        }
        m.savePlayerLocation();
        this.vwMgr.saveViewingWindow();
        try {
            if (!m.getCell(x, y, 0, DungeonConstants.LAYER_LOWER_OBJECTS).isSolid()) {
                m.setPlayerLocationX(x, 0);
                m.setPlayerLocationY(y, 0);
                this.vwMgr.setViewingWindowLocationX(
                        m.getPlayerLocationY(0) - GameViewingWindowManager.getOffsetFactorX());
                this.vwMgr.setViewingWindowLocationY(
                        m.getPlayerLocationX(0) - GameViewingWindowManager.getOffsetFactorY());
                this.saved = m.getCell(m.getPlayerLocationX(0), m.getPlayerLocationY(0), 0,
                        DungeonConstants.LAYER_LOWER_OBJECTS);
                app.getDungeonManager().setDirty(true);
                this.saved.postMoveAction(x, y, 0);
                final var px = m.getPlayerLocationX(0);
                final var py = m.getPlayerLocationY(0);
                m.updateVisibleSquares(px, py, 0);
                this.redrawDungeon();
            }
        } catch (final ArrayIndexOutOfBoundsException ae) {
            m.restorePlayerLocation();
            this.vwMgr.restoreViewingWindow();
            app.showMessage("Can't go outside the maze");
        }
    }

    private static void checkGameOver() {
        if (!PartyManager.getParty().isAlive()) {
            SoundLoader.playSound(Sounds.DEFEATED);
            CommonDialogs.showDialog(
                    "You have died! You lose 10% of your experience and all your Gold, but you are healed fully.");
            PartyManager.getParty().getLeader().onDeath(-10);
        }
    }

    private void redrawDungeon() {
        this.gui.redrawDungeon();
    }
}
