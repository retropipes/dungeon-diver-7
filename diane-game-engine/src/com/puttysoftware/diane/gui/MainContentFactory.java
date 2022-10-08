package com.puttysoftware.diane.gui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainContentFactory {
    private static int CONTENT_WIDTH = 800;
    private static int CONTENT_HEIGHT = 600;

    private MainContentFactory() {
	// do nothing
    }

    public static void setContentSize(final int w, final int h) {
	CONTENT_WIDTH = w;
	CONTENT_HEIGHT = h;
    }

    public static JPanel content() {
	var result = new JPanel();
	result.setPreferredSize(new Dimension(CONTENT_WIDTH, CONTENT_HEIGHT));
	return result;
    }

    public static JScrollPane scrollingContent(final JComponent view) {
	var result = new JScrollPane(view);
	result.setPreferredSize(new Dimension(CONTENT_WIDTH, CONTENT_HEIGHT));
	return result;
    }
}
