package com.lutzenberger.sascha.image;

import java.io.File;
import java.io.FilenameFilter;

/**
 * This class holds all the Image Filters used by this application.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
 */
public class ImageFilter {
    //Make sure it is not possible to create any instances of this class
    private ImageFilter() {
    }

    //The only FilenameFilter used in the application to get valid .JPG files.
    public static final FilenameFilter JPG_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            //convert name to lower case
            name = name.toLowerCase();
            //File must have the extension .jpg in upper or lowercase
            boolean isJPG = name.endsWith(".jpg");

            //File must not start with a dot at the beginning
            boolean isValid = !name.startsWith(".");

            //File must be ending with .jpg and must not start with an '.'
            return isJPG && isValid;
        }
    };
}