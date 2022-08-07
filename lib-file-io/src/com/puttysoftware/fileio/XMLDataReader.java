/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.fileio;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XMLDataReader implements FileIOReader {
    // Fields
    private final InputStream inStream;
    private final XMLDecoder fileIO;
    private final File file;

    // Constructors
    public XMLDataReader(final String filename) throws IOException {
	this.inStream = new FileInputStream(filename);
	this.fileIO = new XMLDecoder(this.inStream);
	this.file = new File(filename);
    }

    public XMLDataReader(final File filename) throws IOException {
	this.inStream = new FileInputStream(filename);
	this.fileIO = new XMLDecoder(this.inStream);
	this.file = filename;
    }

    public XMLDataReader(final InputStream stream) {
	this.inStream = stream;
	this.fileIO = new XMLDecoder(stream);
	this.file = null;
    }

    // Methods
    @Override
    public DataMode getDataIOMode() {
	return DataMode.XML;
    }

    @Override
    public File getFile() {
	return this.file;
    }

    @Override
    public void close() throws FileIOException {
	this.fileIO.close();
    }

    @Override
    public double readDouble() throws FileIOException {
	return (double) this.fileIO.readObject();
    }

    @Override
    public int readInt() throws FileIOException {
	return (int) this.fileIO.readObject();
    }

    @Override
    public long readLong() throws FileIOException {
	return (long) this.fileIO.readObject();
    }

    @Override
    public byte readByte() throws FileIOException {
	return (byte) this.fileIO.readObject();
    }

    @Override
    public boolean readBoolean() throws FileIOException {
	return (boolean) this.fileIO.readObject();
    }

    @Override
    public String readString() throws FileIOException {
	return (String) this.fileIO.readObject();
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
	try {
	    this.fileIO.readObject();
	    return false;
	} catch (ArrayIndexOutOfBoundsException e) {
	    return true;
	}
    }
}
