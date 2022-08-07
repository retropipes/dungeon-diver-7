package com.puttysoftware.fileio;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BinaryDataReader implements FileIOReader {
    // Fields
    private DataInputStream fileIO;
    private final File file;

    // Constructors
    public BinaryDataReader(final String filename) throws IOException {
	this.fileIO = new DataInputStream(new FileInputStream(filename));
	this.file = new File(filename);
    }

    public BinaryDataReader(final File filename) throws IOException {
	this.fileIO = new DataInputStream(new FileInputStream(filename));
	this.file = filename;
    }

    public BinaryDataReader(final InputStream stream) {
	this.fileIO = new DataInputStream(stream);
	this.file = null;
    }

    // Methods
    @Override
    public DataMode getDataIOMode() {
	return DataMode.BINARY;
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
	    return this.fileIO.readInt();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public double readDouble() throws FileIOException {
	try {
	    return this.fileIO.readDouble();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public long readLong() throws FileIOException {
	try {
	    return this.fileIO.readLong();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public byte readByte() throws FileIOException {
	try {
	    return this.fileIO.readByte();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public boolean readBoolean() throws FileIOException {
	try {
	    return this.fileIO.readBoolean();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public String readString() throws FileIOException {
	try {
	    return this.fileIO.readUTF();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public byte[] readBytes(final int len) throws FileIOException {
	try {
	    final byte[] buf = new byte[len];
	    this.fileIO.read(buf);
	    return buf;
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public int readUnsignedByte() throws FileIOException {
	try {
	    return this.fileIO.readUnsignedByte();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public int readUnsignedShortByteArrayAsInt() throws FileIOException {
	try {
	    final byte[] buf = new byte[Short.BYTES];
	    this.fileIO.read(buf);
	    return FileIOUtilities.unsignedShortByteArrayToInt(buf);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public String readWindowsString(final byte[] buflen) throws FileIOException {
	try {
	    this.fileIO.read(buflen);
	    return FileIOUtilities.decodeWindowsStringData(buflen);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public boolean atEOF() throws FileIOException {
	try {
	    final byte[] buf = new byte[1];
	    return this.fileIO.read(buf) == -1;
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }
}
