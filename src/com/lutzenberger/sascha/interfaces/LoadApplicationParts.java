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

package com.lutzenberger.sascha.interfaces;

import javafx.beans.property.ReadOnlyObjectProperty;

import com.lutzenberger.sascha.image.WorkingDirectory;

/**
 * This is an interface which has to be implemented by every class which wants to give access to the following screens.
 *
 * CreateLinksScreen - This screen is responsible for editing the HTML table.
 * ImagePreviewScreen - This screen is responsible for displaying an image preview.
 * SelectGalleryScreen - This screen is responsible for selecting a gallery in which the images should be uploaded into.
 * SelectWorkingDirectoryScreen - This screen is responsible for selecting the WorkingDirectory of the application.
 * WelcomeScreen - This screen is responsible for letting the user choose which part of the program should be used.
 *
 * @author Sascha Lutzenberger
 * @version 1.1 - 30.12.2015
 */
public interface LoadApplicationParts {
    /**
     * This method loads the CreateLinksScreen and takes care of displaying it correctly within the application.
     */
    void loadCreateLinksScreen();

    /**
     * This method loads the ImagePreviewScreen and takes care of displaying it correctly within the application.
     */
    void loadImagePreviewScreen();

    /**
     * This method loads the SelectGalleryScreen and takes care of displaying it correctly within the application.
     */
    void loadSelectGalleryScreen();

    /**
     * This method loads the SelectWorkingDirectoryScreen and takes care of displaying it correctly within the
     * application.
     *
     * This method also accepts an implementation of an Interface which is determining the action which should
     * be performed when the directory was selected.
     *
     * @param doNext The interface to tell the program what should be done after the working directory was selected.
     */
    void loadSelectWorkingDirectoryScreen(WorkingDirectoryNext doNext);

    /**
     * This method loads the WelcomeScreen and takes care of displaying it correctly within the application.
     */
    void loadWelcomeScreen();

    ReadOnlyObjectProperty<WorkingDirectory> workingDirectoryProperty();
}
