package com.puttysoftware.dungeondiver7.manager.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.dungeondiver7.locale.DialogString;
import com.puttysoftware.dungeondiver7.locale.Strings;

public class DungeonProtectionWrapper {
    // Constants
    private static final int BLOCK_MULTIPLIER = 16;

    private DungeonProtectionWrapper() {
	// Do nothing
    }

    public static void protect(final File src, final File dst) throws IOException {
	try (FileInputStream in = new FileInputStream(src); FileOutputStream out = new FileOutputStream(dst)) {
	    final char[] transform = DungeonProtectionWrapper.getTransform();
	    if (transform == null) {
		throw new ProtectionCancelException();
	    }
	    final byte[] buf = new byte[transform.length * DungeonProtectionWrapper.BLOCK_MULTIPLIER];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		for (int x = 0; x < buf.length; x++) {
		    buf[x] += transform[x % transform.length];
		}
		out.write(buf, 0, len);
	    }
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    public static void unprotect(final File src, final File dst) throws IOException {
	try (FileInputStream in = new FileInputStream(src); FileOutputStream out = new FileOutputStream(dst)) {
	    final char[] transform = DungeonProtectionWrapper.getTransform();
	    if (transform == null) {
		throw new ProtectionCancelException();
	    }
	    final byte[] buf = new byte[transform.length * DungeonProtectionWrapper.BLOCK_MULTIPLIER];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		for (int x = 0; x < buf.length; x++) {
		    buf[x] -= transform[x % transform.length];
		}
		out.write(buf, 0, len);
	    }
	} catch (final IOException ioe) {
	    throw ioe;
	}
    }

    private static char[] getTransform() {
	return CommonDialogs.showPasswordInputDialog(Strings.dialog(DialogString.PROTECTION_PROMPT),
		Strings.dialog(DialogString.PROTECTION_TITLE), 15);
    }
}
