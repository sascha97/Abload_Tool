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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class parses the entered links.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 27.12.2015
 */
public class AbloadLinks {
    private final LinkType type;

    private final String links;
    private List<ImageSrc> imageSrcList;

    /**
     * Creates a new Abload Links object which will parse the given links from abload.
     *
     * @param links The links created by abload
     *
     * @since 1.0
     */
    public AbloadLinks(String links){
        this.links = links.trim();

        //Get the type of the links
        type = getType();
    }

    /**
     * This method returns the ImageSrcList.
     *
     * @return java.lang.Collections.unmodifiableList();
     */
    public List<ImageSrc> getImageSrcList(){
        makeList();

        return Collections.unmodifiableList(imageSrcList);
    }


    /**
     * This method tries to find out which Link type was given to the program
     *
     * @return The Link type
     */
    private LinkType getType(){
        if(links.startsWith("[url"))
            return LinkType.FORUM;
        if(links.startsWith("<a"))
            return LinkType.HTML;
        else
            return LinkType.LINKS;
    }

    /**
     * This method creates the list
     */
    private void makeList() {
        //Get the separator
        String separator = type.getSeparator();

        //Use the separator to find out which type it is
        String[] dividedLinks = links.split(separator);

        //Create a new imageSrcList
        imageSrcList = new ArrayList<>(dividedLinks.length);

        for (String s : dividedLinks) {
            try {
                ImageSrc imageSrc = ImageSrc.createImageSrc(s);
                imageSrcList.add(imageSrc);
            } catch(IllegalArgumentException e) {
                //No conversion was possible
            }
        }
    }


    private enum LinkType {
        FORUM("\\[\\/url\\]"),
        HTML("\\<\\/a\\>"),
        LINKS("\n");

        private final String separator;

        LinkType(String separator) {
            this.separator = separator;
        }

        /**
         * This method returns the separator "String" of the abload link.
         *
         * @return The separator "String"
         * @since 1.0
         */
        private String getSeparator() {
            return this.separator;
        }
    }
}
