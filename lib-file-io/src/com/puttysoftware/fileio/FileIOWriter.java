package com.puttysoftware.fileio;

import java.io.File;

public interface FileIOWriter extends AutoCloseable {
    // Methods
    DataMode getDataIOMode();

    File getFile();

    @Override
    void close() throws FileIOException;

    void writeBoolean(boolean value) throws FileIOException;

    void writeByte(byte value) throws FileIOException;

    void writeDouble(double value) throws FileIOException;

    void writeInt(int value) throws FileIOException;

    void writeLong(long value) throws FileIOException;

    void writeString(String value) throws FileIOException;

    void writeUnsignedByte(int value) throws FileIOException;
}