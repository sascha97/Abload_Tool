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
 * This is the Java model of the UploadedImage API on http://www.abload.de
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 29.12.2015
 */
public class ImageUploaded implements Serializable {
    //The new file name of the image used on http://www.abload.de to link to the image
    private String newName;
    //The old file name of the image
    private String oldName;
    private String res;
    private String type;

    /**
     * Constructor for creating a new ImageUploaded model from http://www.abload.de
     *
     * @param newName The name for the Image used on http://www.abload.de
     * @param oldName The old file name of the image used on the local computer
     * @param res The resource used on http://www.abload.de
     * @param type The image type used on http://www.abload.de
     */
    public ImageUploaded(String newName, String oldName, String res, String type) {
        this.newName = newName;
        this.oldName = oldName;
        this.res = res;
        this.type = type;
    }

    /**
     * This method returns the new image name used on http://www.abload.de
     *
     * @return The file name of the image used on http://www.abload.de
     */
    public String getFileName() {
        return this.newName;
    }
}
