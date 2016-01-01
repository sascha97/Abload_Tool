package com.lutzenberger.sascha.abload.data;

import java.io.Serializable;

/**
 * This is the corresponding Java model of the Gallery API on http://www.abload.de
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 29.12.2015
 */
public class Gallery implements Serializable {
    //The name of the gallery
    private String name;
    //The id of the gallery. The gallery is used for identification
    private long id;

    /**
     * Constructor for creating a new Gallery model from http://www.abload.de
     * @param name The name of the gallery
     * @param id The id for the gallery
     */
    public Gallery(String name, long id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Gets the id for the Gallery. The id is used for identification of the gallery on the server of
     * http://www.abload.de
     *
     * @return The id of the gallery at http://www.abload.de
     */
    public long getId() {
        return this.id;
    }

    /**
     * Gets the String name of the gallery. The Name entered as Gallery Name on http://www.abload.de
     *
     * @return The gallery name entered on http://www.abload.de
     */
    private String getName() {
        return this.name;
    }

    /**
     * This method has to be overwritten because the Gallery should be displayed in a ChoiceBox on the user interface.
     * This makes sure the name of the Gallery will be displayed in the user interface.
     *
     * @return Returns the value of getName()
     */
    @Override
    public String toString() {
        return getName();
    }
}
