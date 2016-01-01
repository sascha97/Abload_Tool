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
