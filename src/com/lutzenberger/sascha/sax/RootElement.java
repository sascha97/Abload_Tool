/*
 * Copyright (c) 2016. Sascha Lutzenberger. All rights reserved.
 *
 * This file is part of the project "Abload_Tool"
 *
 * Redistribution and use in source and binary forms, without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - The author of this source code has given you the permission to use this
 *   source code.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - The code is not used in commercial projects, except you got the permission
 *   for using the code in any commerical projects from the author.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.lutzenberger.sascha.sax;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The root XML element.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 08.09.2015
 */
public class RootElement extends Element {

    final Handler handler = new Handler();

    /**
     * Constructs a new root element with the given name.
     *
     * @param uri the namespace
     * @param localName the local name
     */
    public RootElement(String uri, String localName) {
        super(null, uri, localName, 0);
    }

    /**
     * Constructs a new root element with the given name. Uses an empty string
     * as the namespace.
     *
     * @param localName the local name
     */
    public RootElement(String localName) {
        this("", localName);
    }

    /**
     * Gets the SAX {@code ContentHandler}. Pass this to your SAX parser.
     */
    public ContentHandler getContentHandler() {
        return this.handler;
    }

    class Handler extends DefaultHandler {

        Locator locator;
        int depth = -1;
        Element current = null;
        StringBuilder bodyBuilder = null;

        @Override
        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            int depth = ++this.depth;

            if (depth == 0) {
                // This is the root element.
                startRoot(uri, localName, attributes);
                return;
            }

            // Prohibit mixed text and elements.
            if (bodyBuilder != null) {
                throw new BadXmlException("Encountered mixed content"
                        + " within text element named " + current + ".",
                        locator);
            }

            // If we're one level below the current element.
            if (depth == current.depth + 1) {
                // Look for a child to push onto the stack.
                Children children = current.children;
                if (children != null) {
                    Element child = children.get(uri, localName);
                    if (child != null) {
                        start(child, attributes);
                    }
                }
            }
        }

        void startRoot(String uri, String localName, Attributes attributes)
                throws SAXException {
            Element root = RootElement.this;
            if (root.uri.compareTo(uri) != 0
                    || root.localName.compareTo(localName) != 0) {
                throw new BadXmlException("Root element name does"
                        + " not match. Expected: " + root + ", Got: "
                        + Element.toString(uri, localName), locator);
            }

            start(root, attributes);
        }

        void start(Element e, Attributes attributes) {
            // Push element onto the stack.
            this.current = e;

            if (e.startElementListener != null) {
                e.startElementListener.start(attributes);
            }

            if (e.endTextElementListener != null) {
                this.bodyBuilder = new StringBuilder();
            }

            e.resetRequiredChildren();
            e.visited = true;
        }

        @Override
        public void characters(char[] buffer, int start, int length)
                throws SAXException {
            if (bodyBuilder != null) {
                bodyBuilder.append(buffer, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            Element current = this.current;

            // If we've ended the current element...
            if (depth == current.depth) {
                current.checkRequiredChildren(locator);

                // Invoke end element listener.
                if (current.endElementListener != null) {
                    current.endElementListener.end();
                }

                // Invoke end text element listener.
                if (bodyBuilder != null) {
                    String body = bodyBuilder.toString();
                    bodyBuilder = null;

                    // We can assume that this listener is present.
                    current.endTextElementListener.end(body);
                }

                // Pop element off the stack.
                this.current = current.parent;
            }

            depth--;
        }
    }
}