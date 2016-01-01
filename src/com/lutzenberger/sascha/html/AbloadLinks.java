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
