package com.puttysoftware.fileio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GameIODataReader implements FileIOReader {
    // Fields
    private final RandomAccessFile fileIO;
    private final File file;

    // Constructors
    public GameIODataReader(final String filename) throws IOException {
	this.fileIO = new RandomAccessFile(filename, "r");
	this.file = new File(filename);
    }
    
    public GameIODataReader(final File theFile) throws IOException {
	this.fileIO = new RandomAccessFile(theFile, "r");
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
	    this.fileIO.close();
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
    public byte readByte() throws FileIOException {
	try {
	    return this.fileIO.readByte();
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
    public double readDouble() throws FileIOException {
	try {
	    return this.fileIO.readDouble();
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
    public long readLong() throws FileIOException {
	try {
	    return this.fileIO.readLong();
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
	    return this.fileIO.getFilePointer() == this.fileIO.length();
	} catch (IOException e) {
	    throw new FileIOException(e);
	}
    }
}
