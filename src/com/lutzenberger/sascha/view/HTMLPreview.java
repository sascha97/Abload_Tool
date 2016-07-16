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
