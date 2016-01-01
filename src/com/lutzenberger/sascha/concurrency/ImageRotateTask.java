package com.lutzenberger.sascha.concurrency;

import java.io.File;
import java.util.List;

import com.lutzenberger.sascha.image.ImageRotate;
import com.lutzenberger.sascha.image.WorkingDirectory;

/**
 * This is a ImageRotateTask. This task is responsible for rotating the images to prepare them for the upload.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 30.12.2015
 */
public class ImageRotateTask extends ImageTask<Void> {
    private int degree;

    public ImageRotateTask(WorkingDirectory workingDirectory, int degree) {
        super(workingDirectory);
        this.degree = degree;
    }

    @Override
    protected Void call() throws Exception {
        //Get the list of images to rotate
        List<File> imagesToRotate = super.workingDirectory.getResizedImageList();

        int size = imagesToRotate.size();
        //now update the Remaining Images Label
        updateRemainingImages(size);
        for(int i=0;i<size;i++) {
            //Check if Task was cancelled by the user if the task was cancelled then stop the task
            if(isCancelled()) {
                break;
            }
            //Get the Image file to work with
            File currentImage = imagesToRotate.get(i);
            //Update the current image label
            updateCurrentImage(currentImage.getName());

            //Rotate the image
            ImageRotate.rotateImage(currentImage, degree);

            //Update the remaining labels
            updateProgress(i+1, size);
            updateRemainingImages(size - (i+1));
            updateRemainingTime();
        }

        return null;
    }
}