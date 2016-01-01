package com.lutzenberger.sascha.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by saschalutzenberger on 28/12/15.
 */
public class ImageRotate {
    private static final double THETA_R90 = Math.toRadians(90);
    private static final double THETA_R180 = Math.toRadians(180);
    private static final double THETA_R270 = Math.toRadians(270);

    public static File rotateImage(File imageFile, int degree) throws IOException {
        degree = degree % 360;

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
