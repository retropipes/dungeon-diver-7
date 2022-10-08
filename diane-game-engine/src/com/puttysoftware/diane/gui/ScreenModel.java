/*
 * Diane Game Engine Copyleft (C) 2019-present Eric Ahnell
 * 
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
package com.puttysoftware.diane.gui;

import java.awt.Image;

public class ScreenModel {
    // Fields
    private final String title;
    private final Image systemIcon;

    // Constructor
    public ScreenModel(final String theTitle, final Image theSystemIcon) {
	super();
	this.title = theTitle;
	this.systemIcon = theSystemIcon;
    }

    // Methods
    public final String getTitle() {
	return this.title;
    }

    public final Image getSystemIcon() {
	return this.systemIcon;
    }
}
