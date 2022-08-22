/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utility;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.PrintException;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JFrame;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.locale.ErrorString;
import com.puttysoftware.dungeondiver7.locale.Menu;
import com.puttysoftware.dungeondiver7.locale.Strings;
import com.puttysoftware.dungeondiver7.locale.Untranslated;

public class ScreenPrinter {
    private ScreenPrinter() {
	// Do nothing
    }

    public static void printBoard(final JFrame j) {
	try {
	    final var c = j.getContentPane();
	    final var d = c.getPreferredSize();
	    final var bi = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
	    c.paintComponents(bi.createGraphics());
	    final var baos = new ByteArrayOutputStream();
	    ImageIO.write(bi, Strings.untranslated(Untranslated.IMAGE_FORMAT_PNG), baos);
	    final var data = baos.toByteArray();
	    final var bais = new ByteArrayInputStream(data);
	    final PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
	    final DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
	    final var pj = PrinterJob.getPrinterJob();
	    final var okay = pj.printDialog(pras);
	    if (okay) {
		final var service = pj.getPrintService();
		final var job = service.createPrintJob();
		final DocAttributeSet das = new HashDocAttributeSet();
		final Doc doc = new SimpleDoc(bais, flavor, das);
		job.print(doc, pras);
	    }
	} catch (final IOException | PrintException | NullPointerException npe) {
	    CommonDialogs.showErrorDialog(Strings.error(ErrorString.PRINTING_FAILURE),
		    Strings.menu(Menu.PRINT_GAME_WINDOW));
	}
    }
}
