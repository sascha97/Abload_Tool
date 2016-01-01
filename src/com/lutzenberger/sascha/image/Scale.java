package com.lutzenberger.sascha.image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * This class handles the scaling of an originalImage
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 08.09.2015
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
