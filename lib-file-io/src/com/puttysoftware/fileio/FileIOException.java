/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.fileio;

import java.io.IOException;

public class FileIOException extends IOException {
    private static final long serialVersionUID = 23250505322336L;

    public FileIOException() {
	super();
    }

    public FileIOException(String message, Throwable cause) {
	super(message, cause);
    }

    public FileIOException(String message) {
	super(message);
    }

    public FileIOException(Throwable cause) {
	super(cause);
    }
}
