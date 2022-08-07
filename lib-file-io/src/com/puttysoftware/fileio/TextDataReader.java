package com.puttysoftware.fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextDataReader implements FileIOReader {
    // Fields
    private BufferedReader fileIO;
    private final File file;

    // Constructors
    public TextDataReader(final String filename) throws IOException {
	this.fileIO = new BufferedReader(new FileReader(filename));
	this.file = new File(filename);
    }

    public TextDataReader(final File filename) throws IOException {
	this.fileIO = new BufferedReader(new FileReader(filename));
	this.file = filename;
    }

    public TextDataReader(final InputStream stream) {
	this.fileIO = new BufferedReader(new InputStreamReader(stream));
	this.file = null;
    }

    // Methods
    @Override
    public DataMode getDataIOMode() {
	return DataMode.TEXT;
    }

    @Override
    public File getFile() {
	return this.file;
    }

    @Override
    public void close() throws FileIOException {
	try {
	    this.fileIO.close();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public int readInt() throws FileIOException {
	try {
	    return Integer.parseInt(this.fileIO.readLine());
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public double readDouble() throws FileIOException {
	try {
	    return Double.parseDouble(this.fileIO.readLine());
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public long readLong() throws FileIOException {
	try {
	    return Long.parseLong(this.fileIO.readLine());
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public byte readByte() throws FileIOException {
	try {
	    return Byte.parseByte(this.fileIO.readLine());
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public boolean readBoolean() throws FileIOException {
	try {
	    return Boolean.parseBoolean(this.fileIO.readLine());
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public String readString() throws FileIOException {
	try {
	    return this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
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
	String line = "";
	try {
	    line = this.fileIO.readLine();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
	return line == null;
    }
}
