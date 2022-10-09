package com.puttysoftware.diane.gui.listwithimage;

import com.puttysoftware.diane.gui.ScreenController;
import com.puttysoftware.diane.gui.ScreenModel;
import com.puttysoftware.images.BufferedImageIcon;

public final class ListWithImageDialog extends ScreenController {
    public ListWithImageDialog(final String labelText, final String title, final String initialValue,
	    final String[] possibleValues, final BufferedImageIcon imgValue,
	    final BufferedImageIcon... possibleImages) {
	super(new ScreenModel(title),
		new ListWithImageView(labelText, initialValue, possibleValues, imgValue, possibleImages));
    }
}
