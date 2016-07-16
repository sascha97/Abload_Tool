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
