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

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.Jsoner;

public class JDataReader implements FileIOReader {
    // Fields
    private final BufferedReader fileIO;
    private final File file;

    // Constructors
    public JDataReader(final String filename) throws IOException {
	this.fileIO = new BufferedReader(new FileReader(filename));
	this.file = new File(filename);
	this.readJHeader();
    }

    public JDataReader(final File filename) throws IOException {
	this.fileIO = new BufferedReader(new FileReader(filename));
	this.file = filename;
	this.readJHeader();
    }

    public JDataReader(final InputStream stream) throws IOException {
	this.fileIO = new BufferedReader(new InputStreamReader(stream));
	this.file = null;
	this.readJHeader();
    }

    // Methods
    @Override
    public DataMode getDataIOMode() {
	return DataMode.JSON;
    }

    @Override
    public File getFile() {
	return this.file;
    }

    @Override
    public void close() throws FileIOException {
	try {
	    this.readJFooter();
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
	    if (line != null) {
		final String strobj = Jsoner.deserialize(line).toString();
		return Double.parseDouble(strobj);
	    }
	} catch (IOException | JsonException e) {
	    throw new FileIOException(e);
	}
	throw new FileIOException("End of file!"); //$NON-NLS-1$
    }

    @Override
    public int readInt() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	    if (line != null) {
		final String strobj = Jsoner.deserialize(line).toString();
		return Integer.parseInt(strobj);
	    }
	} catch (IOException | JsonException e) {
	    throw new FileIOException(e);
	}
	throw new FileIOException("End of file!"); //$NON-NLS-1$
    }

    @Override
    public long readLong() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	    if (line != null) {
		final String strobj = Jsoner.deserialize(line).toString();
		return Long.parseLong(strobj);
	    }
	} catch (IOException | JsonException e) {
	    throw new FileIOException(e);
	}
	throw new FileIOException("End of file!"); //$NON-NLS-1$
    }

    @Override
    public byte readByte() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	    if (line != null) {
		final String strobj = Jsoner.deserialize(line).toString();
		return Byte.parseByte(strobj);
	    }
	} catch (IOException | JsonException e) {
	    throw new FileIOException(e);
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
	    if (line != null) {
		final String strobj = Jsoner.deserialize(line).toString();
		return Boolean.parseBoolean(strobj);
	    }
	} catch (IOException | JsonException e) {
	    throw new FileIOException(e);
	}
	throw new FileIOException("End of file!"); //$NON-NLS-1$
    }

    @Override
    public String readString() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	    if (line != null) {
		final String strobj = Jsoner.deserialize(line).toString();
		return strobj;
	    }
	} catch (IOException | JsonException e) {
	    throw new FileIOException(e);
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

    private void readJHeader() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line == null) {
	    throw new FileIOException("Corrupt or invalid header!"); //$NON-NLS-1$
	}
	if (!line.equals(JDataConstants.J_HEADER)) {
	    throw new FileIOException("Corrupt or invalid header!"); //$NON-NLS-1$
	}
    }

    private void readJFooter() throws FileIOException {
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	if (line == null) {
	    throw new FileIOException("Corrupt or invalid footer!"); //$NON-NLS-1$
	}
	if (!line.equals(JDataConstants.J_FOOTER)) {
	    throw new FileIOException("Corrupt or invalid footer!"); //$NON-NLS-1$
	}
    }
}
