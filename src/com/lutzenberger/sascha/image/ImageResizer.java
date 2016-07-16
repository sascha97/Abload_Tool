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

package com.lutzenberger.sascha.image;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * This class is responsible for resizing the images.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
 */
public class ImageResizer {
    //The "settings" for the jpgImageWriter
    private static final ImageWriteParam jpgImageWriteParam;

    //A single instance of imageResizer for the whole application
    private static ImageResizer imageResizer;

    static {
        jpgImageWriteParam = new JPEGImageWriteParam(Locale.getDefault());
        jpgImageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgImageWriteParam.setCompressionQuality(0.9f);
    }

    private ImageResizer() {
    }

    public static ImageResizer getInstance() {
        if(imageResizer == null) {
            imageResizer = new ImageResizer();
        }

        return imageResizer;
    }

    /**
     * This metohd scales the image.
     *
     * @param savePath The path where the new image should be saved in.
     * @param image The file nam of the image which has to be scaled.
     *
     * @return The file in which the scaled image is stored
     *
     * @throws IOException When any IOException occurrs.
     */
    public File resizeImage(File image, File savePath) throws IOException {
        //Read in the image using the ImageIO function.
        BufferedImage originalImage = ImageIO.read(image);

        String fileName = image.getName();

        //Generate a scaled image
        return generateImage(savePath, fileName, originalImage);
    }

    //Generates a scaled image, uses the given path
    private File generateImage(File folderPath, String fileName, BufferedImage image) throws IOException {
        //File object referencing to the file in which the scaled image should be stored
        File scaledImageFile = new File(folderPath, fileName);

        //Generate the scaled image
        BufferedImage scaledImage = new Scale(image).generateImage();

        return generateImage(scaledImageFile, scaledImage);
    }

    static File generateImage(File imageFile, BufferedImage image) throws IOException {
        //To write the image to a file
        FileOutputStream fos = new FileOutputStream(imageFile);

        //The image writer to write the image to a file
        ImageWriter writer = getWriter();
        //Crate an image output stream to write the image to a file
        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
        writer.setOutput(ios);
        //Write the image to a file
        writer.write(null, new IIOImage(image, null, null), jpgImageWriteParam);

        //Close resources so that GC can remove them
        ios.close();
        fos.close();
        writer.dispose();

        //Return the file
        return imageFile;
    }

    //Get the JPGImageWriter
    private static ImageWriter getWriter() {
        return ImageIO.getImageWritersByFormatName("jpg").next();
    }
}

