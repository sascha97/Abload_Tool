package com.lutzenberger.sascha.html;

import java.util.ResourceBundle;

/**
 * This class represents all the formats which can be used in the program for the images.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 27.12.2015
 */
public enum ImageFormat {
    PORTRAIT_2(300,400,2),
    PORTRAIT_3(300,400,3),
    LANDSCAPE_2(500,375,2);

    private final static ResourceBundle RES = ResourceBundle.getBundle("strings/Values");

    //The height of the image
    private final int height;
    //The width of the image
    private final int width;
    //The number of pictures in a row of the table
    private final int numRow;

    /**
     * All values are in the unit pixel
     *
     * @param width The maximal width of the picture in the table
     * @param height The maximal height of the picture in the table
     * @param numRow The maximal number of pictures in a row of the table
     *
     * @since 1.0
     */
    ImageFormat(int width, int height, int numRow) {
        this.width = width;
        this.height = height;
        this.numRow = numRow;
    }

    /**
     * Gets the name to use in the gui.
     *
     * @return The name of the constant
     *
     * @since 1.0
     */
    public final String toString(){
        //Get the String representation of the constant
        String string = "format.".concat(super.toString());
        if(!RES.containsKey(string)) {
            return string;
        }

        return RES.getString(string);
    }

    /**
     * Returns the maximal width of the image in the table.
     * VALUE IS IN PIXEL
     *
     * @return The maximal height of the image in the table.
     *
     * @since 1.0
     */
    final int getWidth(){
        return this.width;
    }

    /**
     * Returns the maximal height of the image in the table.
     * VALUE IS IN PIXEL
     *
     * @return The maximal height of the image in the table.
     *
     * @since 1.0
     */
    final int getHeight(){
        return this.height;
    }

    /**
     * Returns the maximal number of images in one row of the table
     * VALUE IS IN PIXEL
     *
     * @return The maximal number of images in one row of the table.
     *
     * @since 1.0
     */
    final int getNumRow(){
        return this.numRow;
    }
}
