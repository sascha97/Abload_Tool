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
