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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import com.lutzenberger.sascha.concurrency.ImageTask;

/**
 * This is the controller for the Screen ImageProgressScreen.fxml
 *
 * This controller handles the refreshing of the User Interface according to the current progress of the Image Task
 * which has to be given to the controller.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
 */
public class ImageProgressController implements Initializable {
    /**
     * The ProgressBar to visualize the current progress.
     */
    @FXML
    private ProgressBar progressBarImageProgress;

    /**
     * The Progress in a percentage (e.g. '0.00 %', '25.51 %')
     */
    @FXML
    private Text textImageProgress;

    /**
     * The Text for displaying the current image which is currently be processed.
     */
    @FXML
    private Text textCurrentImage;

    /**
     * The Text for displaying the number of remaining items which have to be processed..
     */
    @FXML
    private Text textRemainingImages;

    /**
     * The Text for displaying an estimated time for the task to complete.
     */
    @FXML
    private Text textRemainingTime;

    //The ImageTask to which the controller is bound to
    private ImageTask imageTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * This method is used to handle the action which should take place when the Button "Cancel" is clicked by the user.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonCancelClicked(ActionEvent event) {
        //Cancel the Task
        imageTask.cancel();
        System.out.println("Cancelled Task");
    }

    /**
     * This method is used to bind the ImageTask to the controller. This method has to be called because otherwise the
     * program would have nothing to display.
     *
     * @param imageTask The ImageTask which should be executed.
     */
    public void bindProperties(ImageTask imageTask) {
        this.imageTask = imageTask;

        //Bind all the Properties of the User Interface to the Image Progress
        progressBarImageProgress.progressProperty().bind(imageTask.progressProperty());
        textImageProgress.textProperty().bind(imageTask.progressLabelProperty());
        textCurrentImage.textProperty().bind(imageTask.currentImageProperty());
        textRemainingImages.textProperty().bind(imageTask.remainingImagesProperty().asString());
        textRemainingTime.textProperty().bind(imageTask.remainingTimeProperty());
    }

    /**
     * This method should be called when the Task ended. It does not matter if the Task was completed successfully or
     * not. This is important to unbind the User Interfaces components from the Task.
     */
    public void unbindProperties() {
        progressBarImageProgress.progressProperty().unbind();
        textImageProgress.textProperty().unbind();
        textCurrentImage.textProperty().unbind();
        textRemainingImages.textProperty().unbind();
        textRemainingTime.textProperty().unbind();
    }
}
