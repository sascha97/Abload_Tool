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
