package com.lutzenberger.sascha.concurrency;

import java.io.File;
import java.util.List;

import com.lutzenberger.sascha.abload.UploadManager;
import com.lutzenberger.sascha.abload.data.Gallery;
import com.lutzenberger.sascha.abload.data.LoginResponse;
import com.lutzenberger.sascha.html.ImageSrc;
import com.lutzenberger.sascha.image.WorkingDirectory;

/**
 * Created by saschalutzenberger on 30/12/15.
 */
public class ImageUploadTask extends ImageTask<List<ImageSrc>> {
    private Gallery gallery;
    private LoginResponse loginResponse;

    public ImageUploadTask(WorkingDirectory workingDirectory, Gallery gallery, LoginResponse loginResponse) {
        super(workingDirectory);
        this.gallery = gallery;
        this.loginResponse = loginResponse;
    }

    @Override
    protected List<ImageSrc> call() throws Exception {
        //Get the list of images to upload
        List<File> imagesToUpload = super.workingDirectory.getResizedImageList();
        //Get the UploadManager
        UploadManager uploadManager = new UploadManager(gallery, loginResponse);

        int size = imagesToUpload.size();
        //now update the Remaining Images Label
        updateRemainingImages(size);
        for(int i=0;i<size;i++) {
            if(isCancelled()) {
                break;
            }
            //Get the Image which should be uploaded
            File currentFile = imagesToUpload.get(i);
            //Update the current image label
            updateCurrentImage(currentFile.getName());

            //Upload the image
            uploadManager.uploadFile(currentFile);

            //Update the remaining labels
            updateProgress(i+1, size);
            updateRemainingImages(size - (i+1));
            updateRemainingTime();
        }

        return uploadManager.getUploadedImageList();
    }
}
