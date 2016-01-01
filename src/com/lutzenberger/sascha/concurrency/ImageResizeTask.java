package com.lutzenberger.sascha.concurrency;

import java.io.File;
import java.util.List;

import com.lutzenberger.sascha.image.ImageResizer;
import com.lutzenberger.sascha.image.WorkingDirectory;

/**
 * This is a ImageResizeTask. This task is responsible for resizing the images to prepare them for the upload.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 30.12.2015
 */
public class ImageResizeTask extends ImageTask<Void> {

    public ImageResizeTask(WorkingDirectory workingDirectory) {
        super(workingDirectory);
    }

    @Override
    protected Void call() throws Exception {
        //Get the list of images to resize
        List<File> imagesToResize = super.workingDirectory.getImagesToResize();
        //Get the ImageResizer
        ImageResizer imageResizer = ImageResizer.getInstance();

        int size = imagesToResize.size();
        //now update the Remaining Images Label
        updateRemainingImages(size);
        for(int i=0;i<size;i++) {
            //Check if Task was cancelled by the user if the task was cancelled then stop the task
            if(isCancelled()) {
                break;
            }
            //Get the Image file to work with
            File currentImage = imagesToResize.get(i);
            //Update the current image label
            updateCurrentImage(currentImage.getName());

            //Resize the image
            File resizedImage = imageResizer.resizeImage(currentImage, workingDirectory.getResizedDirectory());
            workingDirectory.addResizedImage(resizedImage);

            //Update the remaining labels
            updateProgress(i+1, size);
            updateRemainingImages(size - (i+1));
            updateRemainingTime();
        }

        return null;
    }
}
