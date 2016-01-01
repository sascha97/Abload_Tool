package com.lutzenberger.sascha.sax;

/**
 * Listens for the end of text elements.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 08.09.2015
 */
public interface EndTextElementListener {
    /**
     * Invoked at the end fo a text element with the body of the element.
     *
     * @param body of the element
     */
    void end(String body);
}