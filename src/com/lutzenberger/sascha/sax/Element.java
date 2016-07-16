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

import java.util.ArrayList;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

/**
 * An XML element. Provides access to child elements and hooks to listen for events related to this element.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 08.09.2015
 */
public class Element {

    final String uri;
    final String localName;
    final int depth;
    final Element parent;

    Children children;
    ArrayList<Element> requiredChildren;

    boolean visited;

    StartElementListener startElementListener;
    EndElementListener endElementListener;
    EndTextElementListener endTextElementListener;

    Element(Element parent, String uri, String localName, int depth) {
        this.parent = parent;
        this.uri = uri;
        this.localName = localName;
        this.depth = depth;
    }

    /**
     * Gets the child element with the given name. Uses an empty string as the
     * namespace.
     */
    public Element getChild(String localName) {
        return getChild("", localName);
    }

    /**
     * Gets the child element with the given name.
     */
    public Element getChild(String uri, String localName) {
        if (endTextElementListener != null) {
            throw new IllegalStateException("This element already has an end"
                    + " text element listener. It cannot have children.");
        }

        if (children == null) {
            children = new Children();
        }

        return children.getOrCreate(this, uri, localName);
    }

    /**
     * Sets a listener for the start of this element.
     */
    public void setStartElementListener(
            StartElementListener startElementListener) {
        if (this.startElementListener != null) {
            throw new IllegalStateException(
                    "Start element listener has already been set.");
        }
        this.startElementListener = startElementListener;
    }

    @Override
    public String toString() {
        return toString(uri, localName);
    }

    static String toString(String uri, String localName) {
        return "'" + (uri.equals("") ? localName : uri + ":" + localName) + "'";
    }

    /**
     * Clears flags on required children.
     */
    void resetRequiredChildren() {
        ArrayList<Element> requiredChildren = this.requiredChildren;
        if (requiredChildren != null) {
            for (int i = requiredChildren.size() - 1; i >= 0; i--) {
                requiredChildren.get(i).visited = false;
            }
        }
    }

    /**
     * Throws an exception if a required child was not present.
     */
    void checkRequiredChildren(Locator locator) throws SAXParseException {
        ArrayList<Element> requiredChildren = this.requiredChildren;
        if (requiredChildren != null) {
            for (int i = requiredChildren.size() - 1; i >= 0; i--) {
                Element child = requiredChildren.get(i);
                if (!child.visited) {
                    throw new BadXmlException(
                            "Element named " + this + " is missing required"
                                    + " child element named "
                                    + child + ".", locator);
                }
            }
        }
    }
}
