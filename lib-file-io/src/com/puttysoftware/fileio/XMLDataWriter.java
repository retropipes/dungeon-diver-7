/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.fileio;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class XMLDataWriter implements FileIOWriter {
    // Fields
    private final OutputStream outStream;
    private final XMLEncoder fileIO;
    private final File file;

    // Constructors
    public XMLDataWriter(final String filename) throws IOException {
	this.outStream = new FileOutputStream(filename);
	this.fileIO = new XMLEncoder(this.outStream);
	this.file = new File(filename);
    }

    public XMLDataWriter(final File filename) throws IOException {
	this.outStream = new FileOutputStream(filename);
	this.fileIO = new XMLEncoder(this.outStream);
	this.file = filename;
    }

    public XMLDataWriter(final OutputStream stream) {
	this.outStream = stream;
	this.fileIO = new XMLEncoder(stream);
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
    public void writeDouble(final double d) throws FileIOException {
	this.fileIO.writeObject(d);
    }

    @Override
    public void writeInt(final int i) throws FileIOException {
	this.fileIO.writeObject(i);
    }

    @Override
    public void writeLong(final long l) throws FileIOException {
	this.fileIO.writeObject(l);
    }

    @Override
    public void writeByte(final byte b) throws FileIOException {
	this.fileIO.writeObject(b);
    }

    @Override
    public void writeBoolean(final boolean b) throws FileIOException {
	this.fileIO.writeObject(b);
    }

    @Override
    public void writeString(final String s) throws FileIOException {
	this.fileIO.writeObject(s);
    }

    @Override
    public void writeUnsignedByte(final int b) throws FileIOException {
	this.writeInt(b);
    }
}
