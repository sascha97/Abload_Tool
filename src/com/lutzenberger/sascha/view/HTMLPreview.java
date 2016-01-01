package com.lutzenberger.sascha.view;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by saschalutzenberger on 29/12/15.
 */
public class HTMLPreview {
    private File tmp;

    private static HTMLPreview singleton;

    private HTMLPreview() {
    }

    public static HTMLPreview getPreview() {
        if(singleton == null) {
            singleton = new HTMLPreview();
        }

        return singleton;
    }

    public void createPreview(String htmlText) {
        if(tmp != null) {
            tmp.delete();
        }

        try {
            tmp = HTMLPreview.createTemporaryFile();

            FileWriter writer = new FileWriter(tmp);
            writer.write(htmlText);
            writer.flush();
            writer.close();

            if(Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(tmp.toURI());
            }
        } catch (IOException e) {
            //TODO: implement some logging functionality
            e.printStackTrace();
        }
    }

    private static File createTemporaryFile() throws IOException {
        File tmp = File.createTempFile("abloadLinks",".html");
        tmp.deleteOnExit();

        return tmp;
    }
}
