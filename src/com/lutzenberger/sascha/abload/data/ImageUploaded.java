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
