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

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * This class handles the scaling of an originalImage
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 27.12.2015
 */
class Scale {
    //The buffered originalImage which has to be scaled
    private BufferedImage originalImage;
    private AffineTransform transform;

    //The width to which the image has to be scaled
    private final int WIDTH;

    public Scale(BufferedImage originalImage) {
        this.originalImage = originalImage;
        transform = new AffineTransform();
        WIDTH = 800;
    }

    BufferedImage generateImage() {
        //Get the height and width of the original image.
        int originalImageWidth = originalImage.getWidth();
        int originalImageHeight = originalImage.getHeight();

        //Get the maximal width
        double max = (double) WIDTH;

        //Calculate the scale factor for the image
        double scale;
        if(originalImageWidth >= originalImageHeight) {
            scale = max / (double)originalImageWidth;
        } else {
            scale = max / (double)originalImageHeight;
        }

        //This doesn't allow any scaling to make the image bigger.
        if(scale > 1.0D) {
            scale = 1.0D;
        }

        //Calculate the height and width of the scaled image
        int width = (int)((double)originalImageWidth * scale);
        int height = (int)((double)originalImageHeight * scale);

        this.scale(scale, scale);

        //Draw the scaled image
        return drawImage(width, height);
    }

    private void scale(double sx, double sy) {
        this.transform.scale(sx, sy);
    }

    private BufferedImage drawImage(int width, int height) {
        //Get the color space form the original image
        int colorSpace = this.originalImage.getColorModel().getColorSpace().getType();

        //Create the scaled image
        BufferedImage scaledImage = new BufferedImage(width, height, colorSpace);
        Graphics2D g = scaledImage.createGraphics();
        //Set the rendering hints for the graphics object
        this.setRenderingHints(g);
        g.drawImage(this.originalImage, this.transform, null);
        return scaledImage;
    }

    private void setRenderingHints(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    }
}
