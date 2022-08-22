/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.editor;

import com.puttysoftware.dungeondiver7.dungeon.abc.AbstractDungeonObject;

class EditorUndoRedoEngine {
    // Fields
    private final LinkStack undoHistory;
    private final LinkStack redoHistory;
    private AbstractDungeonObject object;
    private int destX, destY, destZ, destW, destU;

    // Constructors
    public EditorUndoRedoEngine() {
	this.undoHistory = new LinkStack();
	this.redoHistory = new LinkStack();
	this.object = null;
	this.destX = -1;
	this.destY = -1;
	this.destZ = -1;
	this.destW = -1;
	this.destU = -1;
    }

    // Public methods
    void undo() {
	if (!this.undoHistory.isEmpty()) {
	    final var entry = this.undoHistory.pop();
	    this.object = entry.mo;
	    this.destX = entry.coordX;
	    this.destY = entry.coordY;
	    this.destZ = entry.coordZ;
	    this.destW = entry.coordW;
	    this.destU = entry.coordU;
	} else {
	    this.object = null;
	    this.destX = -1;
	    this.destY = -1;
	    this.destZ = -1;
	    this.destW = -1;
	    this.destU = -1;
	}
    }

    void redo() {
	if (!this.redoHistory.isEmpty()) {
	    final var entry = this.redoHistory.pop();
	    this.object = entry.mo;
	    this.destX = entry.coordX;
	    this.destY = entry.coordY;
	    this.destZ = entry.coordZ;
	    this.destW = entry.coordW;
	    this.destU = entry.coordU;
	} else {
	    this.object = null;
	    this.destX = -1;
	    this.destY = -1;
	    this.destZ = -1;
	    this.destW = -1;
	    this.destU = -1;
	}
    }

    boolean tryUndo() {
	return !this.undoHistory.isEmpty();
    }

    boolean tryRedo() {
	return !this.redoHistory.isEmpty();
    }

    boolean tryBoth() {
	return this.undoHistory.isEmpty() && this.redoHistory.isEmpty();
    }

    void updateUndoHistory(final AbstractDungeonObject obj, final int x, final int y, final int z, final int w,
	    final int u) {
	this.undoHistory.push(obj, x, y, z, w, u);
    }

    void updateRedoHistory(final AbstractDungeonObject obj, final int x, final int y, final int z, final int w,
	    final int u) {
	this.redoHistory.push(obj, x, y, z, w, u);
    }

    AbstractDungeonObject getObject() {
	return this.object;
    }

    int getX() {
	return this.destX;
    }

    int getY() {
	return this.destY;
    }

    int getZ() {
	return this.destZ;
    }

    int getW() {
	return this.destW;
    }

    int getU() {
	return this.destU;
    }

    // Inner classes
    private static class Link {
	// Fields
	public AbstractDungeonObject mo;
	public int coordX, coordY, coordZ, coordW, coordU;
	public Link next;

	Link(final AbstractDungeonObject obj, final int x, final int y, final int z, final int w, final int u) {
	    this.mo = obj;
	    this.coordX = x;
	    this.coordY = y;
	    this.coordZ = z;
	    this.coordW = w;
	    this.coordU = u;
	    this.next = null;
	}
    }

    private static class LinkList {
	// Fields
	private Link first;

	LinkList() {
	    this.first = null;
	}

	public boolean isEmpty() {
	    return this.first == null;
	}

	public void insertFirst(final AbstractDungeonObject obj, final int x, final int y, final int z, final int w,
		final int u) {
	    final var newLink = new Link(obj, x, y, z, w, u);
	    newLink.next = this.first;
	    this.first = newLink;
	}

	public Link deleteFirst() {
	    final var temp = this.first;
	    this.first = this.first.next;
	    return temp;
	}
    }

    private static class LinkStack {
	// Fields
	private final LinkList theList;

	LinkStack() {
	    this.theList = new LinkList();
	}

	public void push(final AbstractDungeonObject obj, final int x, final int y, final int z, final int w,
		final int u) {
	    this.theList.insertFirst(obj, x, y, z, w, u);
	}

	public Link pop() {
	    return this.theList.deleteFirst();
	}

	public boolean isEmpty() {
	    return this.theList.isEmpty();
	}
    }
}
