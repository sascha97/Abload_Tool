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

