package com.lutzenberger.sascha.view;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class displays the HTMLPreview in the Default Browser on every system.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 29.12.2015
 */
public class HTMLPreview {
    //The file in which the html table should be written to
    private File tmp;

    //Make sure just one instance exists in the entire application
    private static HTMLPreview singleton;

    //Private constructor so no one outside the class could construct an object.
    private HTMLPreview() {
    }

    /**
     * Singleton design. Makes sure only one instance is available in the entire application.
     *
     * @return The only instance of the HTMLPreview in the entire application.
     */
    public static HTMLPreview getPreview() {
        //If no instance is created create one and then return the instance
        if(singleton == null) {
            singleton = new HTMLPreview();
        }

        return singleton;
    }

    /**
     * This method is used to create a preview file for the given String.
     *
     * @param htmlText A html table which should be displayed in the default web browser
     */
    public void createPreview(String htmlText) {
        //If an old temp file exists delete the old file first of all.
        if(tmp != null) {
            tmp.delete();
        }

        //Then try to create a new temporary file
        try {
            tmp = HTMLPreview.createTemporaryFile();

            //Create a FileWriter to write the htmlText to a file.
            FileWriter writer = new FileWriter(tmp);
            //Write the text to the file and flush the writer to finally write the string to the file.
            writer.write(htmlText);
            writer.flush();
            writer.close();

            //Opens the file in the default Browser of the client system.
            if(Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(tmp.toURI());
            }
        } catch (IOException e) {
            //TODO: implement some logging functionality
            e.printStackTrace();
        }
    }

    //This file creates temporary files for the application.
    private static File createTemporaryFile() throws IOException {
        File tmp = File.createTempFile("abloadLinks",".html");
        tmp.deleteOnExit();

        return tmp;
    }
}
