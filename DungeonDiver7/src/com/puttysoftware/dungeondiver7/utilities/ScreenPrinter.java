/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.dungeondiver7.utilities;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JFrame;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.locales.LocaleConstants;
import com.puttysoftware.dungeondiver7.locales.LocaleLoader;

public class ScreenPrinter {
    private ScreenPrinter() {
	// Do nothing
    }

    public static void printBoard(final JFrame j) {
	try {
	    final Container c = j.getContentPane();
	    final Dimension d = c.getPreferredSize();
	    final BufferedImage bi = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
	    c.paintComponents(bi.createGraphics());
	    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(bi, LocaleLoader.loadString(LocaleConstants.NOTL_STRINGS_FILE,
		    LocaleConstants.NOTL_STRING_IMAGE_FORMAT_PNG), baos);
	    final byte[] data = baos.toByteArray();
	    final ByteArrayInputStream bais = new ByteArrayInputStream(data);
	    final PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
	    final DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
	    final PrinterJob pj = PrinterJob.getPrinterJob();
	    final boolean okay = pj.printDialog(pras);
	    if (okay) {
		final PrintService service = pj.getPrintService();
		final DocPrintJob job = service.createPrintJob();
		final DocAttributeSet das = new HashDocAttributeSet();
		final Doc doc = new SimpleDoc(bais, flavor, das);
		job.print(doc, pras);
	    }
	} catch (final IOException ioe) {
	    CommonDialogs.showErrorDialog(
		    LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE,
			    LocaleConstants.ERROR_STRING_PRINTING_FAILURE),
		    LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			    LocaleConstants.MENU_STRING_ITEM_PRINT_GAMEBOARD));
	} catch (final PrintException pe) {
	    CommonDialogs.showErrorDialog(
		    LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE,
			    LocaleConstants.ERROR_STRING_PRINTING_FAILURE),
		    LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			    LocaleConstants.MENU_STRING_ITEM_PRINT_GAMEBOARD));
	} catch (final NullPointerException npe) {
	    CommonDialogs.showErrorDialog(
		    LocaleLoader.loadString(LocaleConstants.ERROR_STRINGS_FILE,
			    LocaleConstants.ERROR_STRING_PRINTING_FAILURE),
		    LocaleLoader.loadString(LocaleConstants.MENU_STRINGS_FILE,
			    LocaleConstants.MENU_STRING_ITEM_PRINT_GAMEBOARD));
	}
    }
}
