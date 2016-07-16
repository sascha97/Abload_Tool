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

package com.lutzenberger.sascha.concurrency;

import java.io.File;
import java.util.List;

import com.lutzenberger.sascha.abload.UploadManager;
import com.lutzenberger.sascha.abload.data.Gallery;
import com.lutzenberger.sascha.abload.data.LoginResponse;
import com.lutzenberger.sascha.html.ImageSrc;
import com.lutzenberger.sascha.image.WorkingDirectory;

/**
 * This is a ImageUploadTask. This task is responsible uploading the images to http://www.abload.de
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 30.12.2015
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
