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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class is responsible for Rotating all images.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
 */
public class ImageRotate {
    private static final double THETA_R90 = Math.toRadians(90);
    private static final double THETA_R180 = Math.toRadians(180);
    private static final double THETA_R270 = Math.toRadians(270);

    public static File rotateImage(File imageFile, int degree) throws IOException {
        //Rotation between 0 and 360
        degree = degree % 360;

        //Only rotate image if it is a multiple of 90 degrees.
        if(degree == 90 || degree == 180 || degree == 270) {
            BufferedImage imageToRotate = ImageIO.read(imageFile);
            BufferedImage rotatedImage = rotateImage(imageToRotate, degree);

            imageFile = ImageResizer.generateImage(imageFile, rotatedImage);
        }

        return imageFile;
    }

    public static BufferedImage rotateImage(BufferedImage imageToRotate, int degree) {
        AffineTransform transform = new AffineTransform();

        int imageWidth = imageToRotate.getWidth();
        int imageHeight = imageToRotate.getHeight();

        switch (degree) {
            case 90: {
                transform.translate(imageHeight, 0);
                transform.rotate(THETA_R90);
                imageWidth = imageToRotate.getHeight();
                imageHeight = imageToRotate.getWidth();
                break;
            }
            case 180: {
                transform.translate(imageWidth, imageHeight);
                transform.rotate(THETA_R180);
                break;
            }
            case 270: {
                transform.translate(0, imageWidth);
                transform.rotate(THETA_R270);
                imageWidth = imageToRotate.getHeight(null);
                imageHeight = imageToRotate.getWidth(null);
                break;
            }
        }

        int colorSpace = imageToRotate.getColorModel().getColorSpace().getType();

        BufferedImage rotatedImage = new BufferedImage(imageWidth, imageHeight, colorSpace);
        Graphics2D graphics2D = rotatedImage.createGraphics();
        graphics2D.drawImage(imageToRotate, transform, null);

        return rotatedImage;
    }
}
