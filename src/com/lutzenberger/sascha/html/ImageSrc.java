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

/**
 * This class represents the path for one single image.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 27.12.2015
 */
public class ImageSrc {
    //This is the pre_path which has to be added to every single image, because this is the beginning of the image path
    private static final String PRE_PATH = "http://abload.de/img/";

    private String imageName;

    public ImageSrc(String imageName) {
        this.imageName = imageName;
    }

    static ImageSrc createImageSrc(String imageLink) {
        final String START = "img=";
        final String END = ".jpg";

        //search the start position
        int startPosition = imageLink.indexOf(START);

        //If there is a start position
        if(startPosition > 0){
            //Correct the start position
            startPosition = startPosition + START.length();

            //Delete everything before the start positon
            imageLink = imageLink.substring(startPosition);
        }

        //search the end position
        int endPositoion = imageLink.indexOf(END);

        //if there is a end position
        if(endPositoion > 0){
            //Correct the end position
            endPositoion = endPositoion + END.length();

            //delete everything after the end position
            imageLink = imageLink.substring(0,endPositoion);
        }

        if(imageLink.trim().isEmpty() || !imageLink.contains(".jpg")) {
            throw new IllegalArgumentException("This was not possible");
        }

        return new ImageSrc(imageLink);
    }

    /**
     * This method is used to get the image link.
     *
     * @return The image link.
     */
    String getImageLink() {
        return PRE_PATH + this.imageName;
    }
}
