package com.puttysoftware.fileio;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BinaryDataWriter implements FileIOWriter {
    // Fields
    private DataOutputStream fileIO;
    private final File file;

    // Constructors
    public BinaryDataWriter(final String filename) throws IOException {
	this.fileIO = new DataOutputStream(new FileOutputStream(filename));
	this.file = new File(filename);
    }

    public BinaryDataWriter(final File filename) throws IOException {
	this.fileIO = new DataOutputStream(new FileOutputStream(filename));
	this.file = filename;
    }

    public BinaryDataWriter(final OutputStream stream) {
	this.fileIO = new DataOutputStream(stream);
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
    public void writeInt(final int i) throws FileIOException {
	try {
	    this.fileIO.writeInt(i);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeDouble(final double d) throws FileIOException {
	try {
	    this.fileIO.writeDouble(d);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeLong(final long l) throws FileIOException {
	try {
	    this.fileIO.writeLong(l);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeByte(final byte b) throws FileIOException {
	try {
	    this.fileIO.writeByte(b);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeBoolean(final boolean b) throws FileIOException {
	try {
	    this.fileIO.writeBoolean(b);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeString(final String s) throws FileIOException {
	try {
	    this.fileIO.writeUTF(s);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeUnsignedByte(final int b) throws FileIOException {
	this.writeInt(b);
    }
}
