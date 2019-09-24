/*
 * File: ParserException.java
 * Creator: George Ferguson
 * Created: Mon Feb 21 20:25:16 2011
 * Time-stamp: <Fri Mar 30 11:29:54 EDT 2012 ferguson>
 */

package Parser;

import java.io.IOException;

/**
 * Exceptions thrown during parsing.
 */
public class ParserException extends IOException {

	public static final long serialVersionUID = 1L;

	public ParserException(String msg) {
		super(msg);
	}

}
