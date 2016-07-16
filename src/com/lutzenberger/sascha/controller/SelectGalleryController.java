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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.lutzenberger.sascha.abload.GalleryManager;
import com.lutzenberger.sascha.abload.data.Gallery;
import com.lutzenberger.sascha.abload.data.LoginResponse;
import com.lutzenberger.sascha.concurrency.ImageUploadTask;
import com.lutzenberger.sascha.html.ImageSrc;
import com.lutzenberger.sascha.interfaces.ApplicationPartAble;
import com.lutzenberger.sascha.interfaces.LoadApplicationParts;
import com.lutzenberger.sascha.interfaces.LoginLabel;
import com.lutzenberger.sascha.interfaces.LoginResponseAble;
import com.lutzenberger.sascha.view.AddGallery;
import com.lutzenberger.sascha.view.ImageProgress;

/**
 * This is the Controller for the SelectGalleryScreen.fxml
 *
 * This controller gets the selected gallery from the user and starts an task uploading the images to
 * http://www.abload.de
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
 */
public class SelectGalleryController implements Initializable, LoginLabel, LoginResponseAble, ApplicationPartAble {
    //The ResourceBundle that contains all the application strings
    private ResourceBundle res;

    /**
     * The ChoiceBox which is containing all supported image formats.
     */
    @FXML
    private ChoiceBox<Gallery> choiceBoxGallery;

    /**
     * The Button  will be clicked if a gallery has been selected to proceed with the program.
     */
    @FXML
    private Button buttonSelectGallery;

    /**
     * The Text for displaying the current login status
     */
    @FXML
    private Text textLoginStatus;

    /**
     * The Button will be clicked if the gallery list should be refreshed.
     */
    @FXML
    private Button buttonRefreshGalleries;

    /**
     * The Button that will be clicked if the user wants to add a new gallery to http://www.abload.de
     */
    @FXML
    private Button buttonAddGallery;

    /**
     * This is used to load different application parts from the current controller.
     * e.g. (switching screens or getting the working directory property)
     */
    private LoadApplicationParts loadApplicationParts;

    /**
     * An ObjectProperty holding the LoginResponse object
     */
    private ObjectProperty<LoginResponse> loginResponse = new SimpleObjectProperty<>();

    /**
     * An List<ImageSrc> containing all ImageSrc for the entire application
     */
    private List<ImageSrc> imageSrcList;

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
     * This method binds the property of the LoginTask to this Controller so that it is possible to work with the
     * LoginResponse which will be needed for adding a new Gallery.
     *
     * @param loginResponseProperty The LoginResponse Property which is needed for creating a new Gallery
     */
    @Override
    public void bindLoginResponse(ReadOnlyObjectProperty<LoginResponse> loginResponseProperty) {
        loginResponse.bind(loginResponseProperty);

        if(loginResponseProperty.getValue() != null) {
            updateChoiceBoxGallery();
        }

        //If login response changes update the ChoiceBox which is displaying the Galleries associated with the user
        loginResponse.addListener(new ChangeListener<LoginResponse>() {
            @Override
            public void changed(ObservableValue<? extends LoginResponse> observable, LoginResponse oldValue,
                                LoginResponse newValue) {
                updateChoiceBoxGallery();
            }
        });
    }

    /**
     * This method sets the image src list of the controller to a list used by the application.
     *
     * @param imageSrcList The List<ImageSrc> of the entire application in which everything should be stored into.
     */
    public void addImageSrcList(List<ImageSrc> imageSrcList) {
        this.imageSrcList = imageSrcList;
    }

    /**
     * This method is used to handle the action if the user clicks on the Button "Select Gallery".
     *
     * This should create a new ImageUploadTask in which every image file in the resized folder of the working directory
     * should be uploaded to http://www.abload.de
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonSelectGalleryClicked(ActionEvent event) {
        //Get the gallery the user has selected
        Gallery selectedGallery = choiceBoxGallery.getValue();

        //Create new concurrent task so that the upload will be done without freezing the UI
        ImageUploadTask imageUploadTask = new ImageUploadTask(loadApplicationParts.workingDirectoryProperty().get(),
                selectedGallery, loginResponse.get());
        //Getting an ProgressWindow that will be displaying the current progress.
        ImageProgress imageProgress = new ImageProgress(imageUploadTask, res.getString("application.title.upload"));

        //If the task will be cancelled by the user the program should be terminated
        imageUploadTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                imageProgress.closeImageProgressWindow();
                Platform.exit();
            }
        });

        //If everything was completed successfully show the CreateLinks Screen to create the html table
        imageUploadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                imageProgress.closeImageProgressWindow();
                imageSrcList.addAll(imageUploadTask.getValue());
                unbind();

                loadApplicationParts.loadCreateLinksScreen();
            }
        });

        //Start the Task in a new thread.
        Thread t = new Thread(imageUploadTask);
        t.setDaemon(true);
        t.start();
    }

    /**
     * This method is used to handle the action if the user clicks on the Button "Refresh Galleries".
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonRefreshGalleriesClicked(ActionEvent event) {
        //Simply update the choiceBox
        updateChoiceBoxGallery();
    }

    /**
     * This method is used to handle the action if the user clicks on the Button "Add Gallery".
     *
     * Show a new screen to display the gallery.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonAddGalleryClicked(ActionEvent event) {
        AddGallery addGallery = new AddGallery(textLoginStatus.textProperty(), loginResponse);

        updateChoiceBoxGallery();
    }

    /**
     * This method is used to update the ChoiceBox that contains all the galleries of the user
     */
    private void updateChoiceBoxGallery() {
        //Enable all the buttons...
        buttonSelectGallery.setDisable(false);
        buttonRefreshGalleries.setDisable(false);
        buttonAddGallery.setDisable(false);

        //Fetch the galleries from the API.
        GalleryManager galleryManager = new GalleryManager(loginResponse.get());

        //Set the Galleries fetched to the choice box.
        choiceBoxGallery.getItems().setAll(galleryManager.getGalleries());
        choiceBoxGallery.getSelectionModel().selectFirst();
    }

    /**
     * This method is used for unbinding the properties.
     */
    private void unbind() {
        textLoginStatus.textProperty().unbind();
        loginResponse.unbind();
    }
}
