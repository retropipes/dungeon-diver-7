package com.puttysoftware.fileio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GameIODataWriter implements FileIOWriter {
    // Fields
    private final RandomAccessFile raf;
    private final File file;

    // Constructors
    public GameIODataWriter(final String filename) throws IOException {
	this.raf = new RandomAccessFile(filename, "rwd");
	this.file = new File(filename);
    }

    public GameIODataWriter(final File theFile) throws IOException {
	this.raf = new RandomAccessFile(theFile, "rwd");
	this.file = theFile;
    }

    // Methods
    @Override
    public DataMode getDataIOMode() {
	return DataMode.GAME_IO;
    }

    @Override
    public File getFile() {
	return this.file;
    }

    @Override
    public void close() throws FileIOException {
	try {
	    this.raf.close();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeBoolean(final boolean value) throws FileIOException {
	try {
	    this.raf.writeBoolean(value);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeByte(final byte value) throws FileIOException {
	try {
	    this.raf.writeByte(value);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeDouble(final double value) throws FileIOException {
	try {
	    this.raf.writeDouble(value);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeInt(final int value) throws FileIOException {
	try {
	    this.raf.writeInt(value);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeLong(final long value) throws FileIOException {
	try {
	    this.raf.writeLong(value);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeString(final String value) throws FileIOException {
	try {
	    this.raf.writeUTF(value);
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }

    @Override
    public void writeUnsignedByte(final int value) throws FileIOException {
	this.writeInt(value);
    }
}
