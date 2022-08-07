package com.puttysoftware.fileio;

import java.io.File;

public interface FileIOReader extends AutoCloseable {
    // Methods
    DataMode getDataIOMode();

    File getFile();

    @Override
    void close() throws FileIOException;

    boolean readBoolean() throws FileIOException;

    byte readByte() throws FileIOException;

    byte[] readBytes(int len) throws FileIOException;

    double readDouble() throws FileIOException;

    int readInt() throws FileIOException;

    long readLong() throws FileIOException;

    String readString() throws FileIOException;

    int readUnsignedByte() throws FileIOException;

    int readUnsignedShortByteArrayAsInt() throws FileIOException;

    String readWindowsString(byte[] buflen) throws FileIOException;

    boolean atEOF() throws FileIOException;
}