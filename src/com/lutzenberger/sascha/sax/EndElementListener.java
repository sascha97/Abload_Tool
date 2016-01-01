package com.lutzenberger.sascha.sax;

/**
 * Listens for the end of elements
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 08.09.2015
 */
public interface EndElementListener {
    /**
     * Invoked at the end of an element.
     */
    void end();
}