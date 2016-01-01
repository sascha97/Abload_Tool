package com.lutzenberger.sascha.sax;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author Sascha Lutzenberger
 * @version 1.0 - 08.09.2015
 */
public class Xml {
    /**
     * Parses xml from the given input stream and fires events on the given SAX
     * handler.
     */
    public static void parse(InputStream in, Encoding encoding,
                             ContentHandler contentHandler) throws IOException, SAXException {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(contentHandler);
        InputSource source = new InputSource(in);
        source.setEncoding(encoding.expatName);
        reader.parse(source);
    }

    /**
     * Supported character encodings.
     */
    public enum Encoding {

        US_ASCII("US-ASCII"),
        UTF_8("UTF-8"),
        UTF_16("UTF-16"),
        ISO_8859_1("ISO-8859-1");

        final String expatName;

        Encoding(String expatName) {
            this.expatName = expatName;
        }
    }
}