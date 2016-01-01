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
