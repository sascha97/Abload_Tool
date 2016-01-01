package com.lutzenberger.sascha.sax;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

/**
 * An XML parse exception which includes the line number in the message.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 08.09.2015
 */
public class BadXmlException extends SAXParseException {

    public BadXmlException(String message, Locator locator) {
        super(message, locator);
    }

    @Override
    public String getMessage() {
        return "Line " + getLineNumber() + ": " + super.getMessage();
    }
}
