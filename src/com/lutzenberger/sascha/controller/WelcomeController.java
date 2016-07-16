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

package com.lutzenberger.sascha.controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import com.lutzenberger.sascha.concurrency.ImageResizeTask;
import com.lutzenberger.sascha.interfaces.ApplicationPartAble;
import com.lutzenberger.sascha.interfaces.LoadApplicationParts;
import com.lutzenberger.sascha.interfaces.LoginLabel;
import com.lutzenberger.sascha.interfaces.WorkingDirectoryNext;
import com.lutzenberger.sascha.view.ImageProgress;

/**
 * This is the Controller for the WelcomeScreen.fxml
 *
 * This controller executes the next parts of the program according to the button which has to be clicked by the user.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
 */
public class WelcomeController implements Initializable, LoginLabel, ApplicationPartAble {
    //The ResourceBundle that contains all the application strings
    private ResourceBundle res;

    /**
     * The Text for displaying the current login status
     */
    @FXML
    private Text textLoginStatus;

    /**
     * This is used to load different application parts from the current controller.
     * e.g. (switching screens or getting the working directory property)
     */
    private LoadApplicationParts loadApplicationParts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.res = resources;
    }

    /**
     * This method is used to initialize the controller with the ApplicationParts that are necessary for the entire
     * application.
     *
     * @param loadApplicationParts The application parts which are necessary for the program to run.
     */
    @Override
    public void setLoadApplicationParts(LoadApplicationParts loadApplicationParts) {
        this.loadApplicationParts = loadApplicationParts;
    }

    /**
     * This method binds the loginStatus to the Text Login Status
     *
     * @param loginStatusProperty The loginStatus which should be bound to the Login Status Text
     */
    @Override
    public void bindLoginText(ReadOnlyStringProperty loginStatusProperty) {
        textLoginStatus.textProperty().bind(loginStatusProperty);
    }

    /**
     * This method is used to handle the action if the user clicks on the first button on the WelcomeScreen.
     *
     * This will open a new window in which the WorkingDirectory will be initialized and given back to the Application.
     *
     * Afterwards an Implementation of the WorkingDirectoryNext will tell the application what should be executed
     * after the working directory has been selected.
     * In this case an ImageResize Task will be executed and all the images in the Working Directory will be prepared
     * for upload.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonResizeUploadCreateLinksClicked(ActionEvent event) {
        //Implementation what should be done after the WorkingDirectroy has been selected
        WorkingDirectoryNext workingDirectoryNext = new WorkingDirectoryNext() {
            @Override
            public void doNext() {
                //Create a new concurrent task so that the images can be resized without freezing the UI
                ImageResizeTask imageResizeTask =
                        new ImageResizeTask(loadApplicationParts.workingDirectoryProperty().get());
                //Getting an Progress Window that will display the current progress.
                ImageProgress imageProgress =
                        new ImageProgress(imageResizeTask, res.getString("application.title.resize"));

                //Set the action that should happen if cancel was clicked by the user...
                imageResizeTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        imageProgress.closeImageProgressWindow();
                        //TODO: Dialog to ask the user if resized folder should be cleaned
                    }
                });

                //If task is completed successfully go to the ImagePreview screen to set up the rotation of the images.
                imageResizeTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        imageProgress.closeImageProgressWindow();
                        WelcomeController.this.loadApplicationParts.loadImagePreviewScreen();
                    }
                });

                //Start the task in a new Thread
                Thread t = new Thread(imageResizeTask);
                t.setDaemon(true);
                t.start();
            }
        };

        loadApplicationParts.loadSelectWorkingDirectoryScreen(workingDirectoryNext);
    }

    /**
     * This method is used to handle the action if the user clicks on the first second on the WelcomeScreen.
     *
     * This will open a new window in which the WorkingDirectory will be initialized and given back to the Application.
     *
     * Afterwards an Implementation of the WorkingDirectoryNext will tell the application what should be executed
     * after the working directory has been selected.
     * In this case the gallery for the upload should be shown because images are already resized and rotated.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonUploadCreateLinksClicked(ActionEvent event) {
        WorkingDirectoryNext workingDirectoryNext = new WorkingDirectoryNext() {
            @Override
            public void doNext() {
                WelcomeController.this.loadApplicationParts.loadSelectGalleryScreen();
                //TODO: check if directory has resized images? and display dialog
            }
        };

        loadApplicationParts.loadSelectWorkingDirectoryScreen(workingDirectoryNext);
    }

    /**
     * This method is used to handle the action if the user clicks on the first second on the WelcomeScreen.
     *
     * This will open a new window for editing the Links you get from http://www.abload.de just in case you have already
     * uploaded images on another way and want to include them into a html table.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonCreateLinksClicked(ActionEvent event) {
        loadApplicationParts.loadCreateLinksScreen();
    }

    /**
     * This method is used to handle the action if the user clicks on the menu item "Close Application".
     *
     * This will close the JavaFx Application.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void menuCloseApplicationClicked(ActionEvent event) {
        //Will be executed in the JavaFx Application Thread
        Platform.exit();
    }
}
