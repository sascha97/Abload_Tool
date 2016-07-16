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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class sets up the Working Directory of the application.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
 */
public class WorkingDirectory {
    //The name of the cover image directory
    private static final String COVER_IMAGE_DIR = "cover";
    //The name of the resized image directory
    private static final String RESIZED_IMAGE_DIR = "resized";

    //The base directory in which all directories have to be created
    private final File BASE_DIR;
    //The cover directory in which the cover image will be stored
    private File coverDir;
    //The resized directory in which all the resized images will be stroed
    private File resizedDir;

    private List<File> resizedImagesList;

    /**
     * Creates a new Working Directory, all Folders will be setup by default. If the folders are already existing
     * they will not be cleared.
     *
     * @param baseDir The base directory of the working directory.
     */
    public WorkingDirectory(File baseDir) {
        this.BASE_DIR = baseDir;

        //create the working folders if they are non existent
        createWorkingFolders();

        resizedImagesList = new ArrayList<>();
    }

    public List<File> getImagesToResize() {
        File[] filesToResize = BASE_DIR.listFiles(ImageFilter.JPG_FILTER);

        return Arrays.asList(filesToResize);
    }

    public File getResizedDirectory() {
        return resizedDir;
    }

    public void addResizedImage(File resizedImage) {
        if(!resizedImagesList.contains(resizedImage)) {
            resizedImagesList.add(resizedImage);
        }
    }

    public List<File> getResizedImageList() {
        if(resizedImagesList.isEmpty()) {
            File[] resizedImages = resizedDir.listFiles(ImageFilter.JPG_FILTER);

            if(resizedImages != null) {
                for(File file : resizedImages) {
                    addResizedImage(file);
                }
            }
        }

        return resizedImagesList;
    }

    private void createWorkingFolders() {
        //Creates the working directories and stores them in the attributes
        coverDir = createFolder(COVER_IMAGE_DIR);
        resizedDir = createFolder(RESIZED_IMAGE_DIR);
    }

    //Create a folder if not already existent, if it fails to do so Log an error message
    private File createFolder(String folderName) {
        File folder = new File(BASE_DIR, folderName);

        if(!folder.exists()) {
            boolean created = folder.mkdir();
            if(!created) {
                //TODO: implement logging functionality
                System.out.println("Folder could not be created: " + folderName);
            }
        }

        return folder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkingDirectory that = (WorkingDirectory) o;

        return !(BASE_DIR != null ? !BASE_DIR.equals(that.BASE_DIR) : that.BASE_DIR != null);

    }

    @Override
    public int hashCode() {
        return BASE_DIR != null ? BASE_DIR.hashCode() : 0;
    }
}

