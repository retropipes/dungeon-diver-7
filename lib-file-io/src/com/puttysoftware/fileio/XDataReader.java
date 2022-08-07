/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XDataReader implements FileIOReader {
    // Fields
    private final BufferedReader fileIO;
    private final File file;
    private final String docTag;

    // Constructors
    public XDataReader(final String filename, final String newDocTag) throws IOException {
	this.fileIO = new BufferedReader(new FileReader(filename));
	this.file = new File(filename);
	this.docTag = newDocTag;
	this.readXHeader();
	this.readOpeningDocTag();
    }

    public XDataReader(final File filename, final String newDocTag) throws IOException {
	this.fileIO = new BufferedReader(new FileReader(filename));
	this.file = filename;
	this.docTag = newDocTag;
	this.readXHeader();
	this.readOpeningDocTag();
    }

    public XDataReader(final InputStream stream, final String newDocTag) throws IOException {
	this.fileIO = new BufferedReader(new InputStreamReader(stream));
	this.file = null;
	this.docTag = newDocTag;
	this.readXHeader();
	this.readOpeningDocTag();
    }

    // Methods
    @Override
    public DataMode getDataIOMode() {
	return DataMode.CUSTOM_XML;
    }

    @Override
    public File getFile() {
	return this.file;
    }

    @Override
    public void close() throws FileIOException {
	try {
	    this.readClosingDocTag();
	    this.fileIO.close();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public double readDouble() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line != null) {
	    final String[] split = XDataReader.splitLine(line);
	    XDataReader.validateOpeningTag(split[0], XDataConstants.DOUBLE_TAG);
	    XDataReader.validateClosingTag(split[2], XDataConstants.DOUBLE_TAG);
	    return Double.parseDouble(split[1]);
	}
	throw new FileIOException("End of file!"); //$NON-NLS-1$
    }

    @Override
    public int readInt() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line != null) {
	    final String[] split = XDataReader.splitLine(line);
	    XDataReader.validateOpeningTag(split[0], XDataConstants.INT_TAG);
	    XDataReader.validateClosingTag(split[2], XDataConstants.INT_TAG);
	    return Integer.parseInt(split[1]);
	}
	throw new FileIOException("End of file!"); //$NON-NLS-1$
    }

    @Override
    public long readLong() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line != null) {
	    final String[] split = XDataReader.splitLine(line);
	    XDataReader.validateOpeningTag(split[0], XDataConstants.LONG_TAG);
	    XDataReader.validateClosingTag(split[2], XDataConstants.LONG_TAG);
	    return Long.parseLong(split[1]);
	}
	throw new FileIOException("End of file!"); //$NON-NLS-1$
    }

    @Override
    public byte readByte() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line != null) {
	    final String[] split = XDataReader.splitLine(line);
	    XDataReader.validateOpeningTag(split[0], XDataConstants.BYTE_TAG);
	    XDataReader.validateClosingTag(split[2], XDataConstants.BYTE_TAG);
	    return Byte.parseByte(split[1]);
	}
	throw new FileIOException("End of file!"); //$NON-NLS-1$
    }

    @Override
    public byte[] readBytes(final int len) throws FileIOException {
	try {
	    final byte[] buf = new byte[len];
	    for (int b = 0; b < len; b++) {
		buf[b] = readByte();
	    }
	    return buf;
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public boolean readBoolean() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line != null) {
	    final String[] split = XDataReader.splitLine(line);
	    XDataReader.validateOpeningTag(split[0], XDataConstants.BOOLEAN_TAG);
	    XDataReader.validateClosingTag(split[2], XDataConstants.BOOLEAN_TAG);
	    return Boolean.parseBoolean(split[1]);
	}
	throw new FileIOException("End of file!"); //$NON-NLS-1$
    }

    @Override
    public String readString() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line != null) {
	    final String[] split = XDataReader.splitLine(line);
	    XDataReader.validateOpeningTag(split[0], XDataConstants.STRING_TAG);
	    XDataReader.validateClosingTag(split[2], XDataConstants.STRING_TAG);
	    return XDataReader.replaceSpecialCharacters(split[1]);
	}
	throw new FileIOException("End of file!"); //$NON-NLS-1$
    }

    @Override
    public int readUnsignedByte() throws FileIOException {
	return readInt();
    }

    @Override
    public int readUnsignedShortByteArrayAsInt() throws FileIOException {
	try {
	    final byte[] buf = new byte[Short.BYTES];
	    for (int b = 0; b < Short.BYTES; b++) {
		buf[b] = readByte();
	    }
	    return FileIOUtilities.unsignedShortByteArrayToInt(buf);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public String readWindowsString(final byte[] buflen) throws FileIOException {
	try {
	    final byte[] buf = new byte[buflen.length];
	    for (int b = 0; b < buflen.length; b++) {
		buf[b] = readByte();
	    }
	    return FileIOUtilities.decodeWindowsStringData(buf);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public boolean atEOF() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	return line == null;
    }

    protected String readRawString() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	return line;
    }

    public void readOpeningGroup(final String groupName) throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line != null) {
	    XDataReader.validateOpeningTag(XDataReader.replaceSpecialCharacters(line), groupName);
	} else {
	    throw new FileIOException("End of file!");
	}
    }

    public void readClosingGroup(final String groupName) throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line != null) {
	    XDataReader.validateClosingTag(XDataReader.replaceSpecialCharacters(line), groupName);
	} else {
	    throw new FileIOException("End of file!");
	}
    }

    protected static void validateOpeningTag(final String tag, final String tagType) throws FileIOException {
	if (!tag.equals("<" + tagType + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
	    throw new FileIOException("Expected opening tag of <" //$NON-NLS-1$
		    + tagType + ">, found " + tag + "!"); //$NON-NLS-1$ //$NON-NLS-2$
	}
    }

    protected static void validateClosingTag(final String tag, final String tagType) throws FileIOException {
	if (!tag.equals("</" + tagType + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
	    throw new FileIOException("Expected closing tag of </" //$NON-NLS-1$
		    + tagType + ">, found " + tag + "!"); //$NON-NLS-1$ //$NON-NLS-2$
	}
    }

    protected static String[] splitLine(final String line) throws FileIOException {
	final String[] split = new String[3];
	final int loc0 = line.indexOf('>') + 1;
	final int loc2 = line.indexOf('<', loc0);
	if (loc0 == -1 || loc2 == -1) {
	    throw new FileIOException("Unexpected string found: " //$NON-NLS-1$
		    + line + "!"); //$NON-NLS-1$
	}
	split[0] = line.substring(0, loc0);
	split[1] = line.substring(loc0, loc2);
	split[2] = line.substring(loc2);
	return split;
    }

    private void readXHeader() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line == null) {
	    throw new FileIOException("Corrupt or invalid header!"); //$NON-NLS-1$
	}
	if (!line.equals(XDataConstants.X_HEADER)) {
	    throw new FileIOException("Corrupt or invalid header!"); //$NON-NLS-1$
	}
    }

    private void readOpeningDocTag() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line != null && !line.equals("<" + this.docTag + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
	    throw new FileIOException("Opening doc tag does not match: expected <" + this.docTag //$NON-NLS-1$
		    + ">, found " + line + "!"); //$NON-NLS-1$ //$NON-NLS-2$
	}
    }

    private void readClosingDocTag() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line != null && !line.equals("</" + this.docTag + ">")) { //$NON-NLS-1$ //$NON-NLS-2$
	    throw new FileIOException("Closing doc tag does not match: expected </" + this.docTag //$NON-NLS-1$
		    + ">, found " + line + "!"); //$NON-NLS-1$ //$NON-NLS-2$
	}
    }

    private static String replaceSpecialCharacters(final String s) {
	String r = s;
	r = r.replace("&amp;", "&"); //$NON-NLS-1$ //$NON-NLS-2$
	r = r.replace("&lt;", "<"); //$NON-NLS-1$ //$NON-NLS-2$
	r = r.replace("&gt;", ">"); //$NON-NLS-1$ //$NON-NLS-2$
	r = r.replace("&quot;", "\""); //$NON-NLS-1$ //$NON-NLS-2$
	r = r.replace("&apos;", "\'"); //$NON-NLS-1$ //$NON-NLS-2$
	return r.replace("&#xA;", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
