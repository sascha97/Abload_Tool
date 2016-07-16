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
 * This class represents one row of the table.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 27.12.2015
 */
class TableRow {
    private final ImageFormat format;
    private final ResourceBundle res = ResourceBundle.getBundle("templates/html");

    private final ImageSrc[] images;

    /**
     * Creates a new row of the table
     *
     * @param format The format of the row
     */
    TableRow(ImageFormat format){
        this.format = format;

        images = new ImageSrc[format.getNumRow()];
    }

    /**
     * This method adds an ImageSrc to the table row, if possilbe and returns true, if not possible false will be
     * returned.
     *
     * @param image The imageSrc which should be added
     *
     * @return true if it could be added otherwise false
     */
    boolean fillRow(ImageSrc image){
        int index = freeSpace();
        if(index < 0)
            return false;
        images[index] = image;

        return true;
    }

    /**
     * This method returns one row of the table
     *
     * @return one row of the table
     *
     * @since 1.0
     */
    String getTableRow(){
        String row = "";

        for (ImageSrc image : images) {
            if(image !=  null){
                String attr = image.getImageLink();
                String link = String.format(res.getString("imgLink"), attr, attr, format.getWidth(), format.getHeight());
                row = row + String.format(res.getString("table_td"),link);
            }
        }

        return String.format(res.getString("table_tr"),row);
    }

    /**
     * This method checks if there is free space at the current row, if there is no free space -1 will be returned
     * otherwise the index of the free space will be returned.
     *
     * @return -1 if no free space otherwise the index of the free space.
     *
     * @since 1.0
     */
    private int freeSpace(){
        for (int i = 0; i < images.length; i++) {
            if(images[i] == null)
                return i;
        }

        return -1;
    }

}

