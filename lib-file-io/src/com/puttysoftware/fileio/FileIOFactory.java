package com.puttysoftware.fileio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileIOFactory {
    private static final String CUSTOM_XML = "customxml";

    public static FileIOReader createReader(final DataMode mode, final String filename) throws IOException {
	switch (mode) {
	case BINARY:
	    return new BinaryDataReader(filename);
	case CUSTOM_XML:
	    return new XDataReader(filename, CUSTOM_XML);
	case GAME_IO:
	    return new GameIODataReader(filename);
	case JSON:
	    return new JDataReader(filename);
	case TEXT:
	    return new TextDataReader(filename);
	case XML:
	    return new XMLDataReader(filename);
	default:
	    throw new IllegalArgumentException();
	}
    }

    public static FileIOReader createReader(final DataMode mode, final File file) throws IOException {
	switch (mode) {
	case BINARY:
	    return new BinaryDataReader(file);
	case CUSTOM_XML:
	    return new XDataReader(file, CUSTOM_XML);
	case GAME_IO:
	    return new GameIODataReader(file);
	case JSON:
	    return new JDataReader(file);
	case TEXT:
	    return new TextDataReader(file);
	case XML:
	    return new XMLDataReader(file);
	default:
	    throw new IllegalArgumentException();
	}
    }

    public static FileIOReader createReader(final DataMode mode, final InputStream stream) throws IOException {
	switch (mode) {
	case BINARY:
	    return new BinaryDataReader(stream);
	case CUSTOM_XML:
	    return new XDataReader(stream, CUSTOM_XML);
	case GAME_IO:
	    throw new IllegalArgumentException(); // Game IO doesn't support streams
	case JSON:
	    return new JDataReader(stream);
	case TEXT:
	    return new TextDataReader(stream);
	case XML:
	    return new XMLDataReader(stream);
	default:
	    throw new IllegalArgumentException();
	}
    }

    public static FileIOWriter createWriter(final DataMode mode, final String filename) throws IOException {
	switch (mode) {
	case BINARY:
	    return new BinaryDataWriter(filename);
	case CUSTOM_XML:
	    return new XDataWriter(filename, CUSTOM_XML);
	case GAME_IO:
	    return new GameIODataWriter(filename);
	case JSON:
	    return new JDataWriter(filename);
	case TEXT:
	    return new TextDataWriter(filename);
	case XML:
	    return new XMLDataWriter(filename);
	default:
	    throw new IllegalArgumentException();
	}
    }

    public static FileIOWriter createWriter(final DataMode mode, final File file) throws IOException {
	switch (mode) {
	case BINARY:
	    return new BinaryDataWriter(file);
	case CUSTOM_XML:
	    return new XDataWriter(file, CUSTOM_XML);
	case GAME_IO:
	    return new GameIODataWriter(file);
	case JSON:
	    return new JDataWriter(file);
	case TEXT:
	    return new TextDataWriter(file);
	case XML:
	    return new XMLDataWriter(file);
	default:
	    throw new IllegalArgumentException();
	}
    }

    public static FileIOWriter createWriter(final DataMode mode, final OutputStream stream) throws IOException {
	switch (mode) {
	case BINARY:
	    return new BinaryDataWriter(stream);
	case CUSTOM_XML:
	    return new XDataWriter(stream, CUSTOM_XML);
	case GAME_IO:
	    throw new IllegalArgumentException(); // Game IO doesn't support streams
	case JSON:
	    return new JDataWriter(stream);
	case TEXT:
	    return new TextDataWriter(stream);
	case XML:
	    return new XMLDataWriter(stream);
	default:
	    throw new IllegalArgumentException();
	}
    }
}
