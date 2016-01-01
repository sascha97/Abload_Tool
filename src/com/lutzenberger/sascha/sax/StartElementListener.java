package com.lutzenberger.sascha.sax;

import org.xml.sax.Attributes;

/**
 * Listens for the beginning of elements.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 08.09.2015
 */
public interface StartElementListener {
    /**
     * Invoked at the beginning of an element.
     *
     * @param attributes from the element.
     */
    void start(Attributes attributes);
}