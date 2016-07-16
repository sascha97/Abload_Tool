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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.lutzenberger.sascha.image.WorkingDirectory;
import com.lutzenberger.sascha.interfaces.ApplicationPartAble;
import com.lutzenberger.sascha.interfaces.LoadApplicationParts;
import com.lutzenberger.sascha.interfaces.LoginLabel;
import com.lutzenberger.sascha.interfaces.WorkingDirectoryNext;

/**
 * This is the controller for the SelectWorkingDirectoriesScreen.fxml
 *
 * This controller sets the WorkingDirectory for the entire application.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
 */
public class SelectWorkingDirectoryController implements Initializable, LoginLabel, ApplicationPartAble {
    //The ResourceBundle that contains all the application strings
    private ResourceBundle res;

    /**
     * The Text for displaying the current login status.
     */
    @FXML
    private Text textLoginStatus;

    /**
     * The Text for displaying the currently selected directory.
     */
    @FXML
    private Text textSelectedDirectory;

    /**
     * The button that will be clicked if the user selects the working directory.
     */
    @FXML
    private Button buttonSelectDirectory;

    /**
     * This is used to load different application parts from the current controller.
     * e.g. (switching screens or getting the working directory property)
     */
    private LoadApplicationParts loadApplicationParts;

    private ObjectProperty<WorkingDirectory> workingDirectory = new SimpleObjectProperty<>();

    /**
     * The selected file by the DirectoryChooser
     */
    private File selectedWorkingDirectory;

    /**
     * What should be done after the Working Directory has been created.
     */
    private WorkingDirectoryNext workingDirectoryNext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        res = resources;
    }

    /**
     * This method is used to execute any other code after the WorkingDirectory has been selected.
     *
     * @param workingDirectoryNext What should be done after the Working Directory has been created.
     */
    public void setWorkingDirectoryNext(WorkingDirectoryNext workingDirectoryNext) {
        this.workingDirectoryNext = workingDirectoryNext;
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
     * This method returns the ReadOnlyObjectProperty for the workingDirectory which has to be bound to the
     * WorkingDirectory Property of the main application in order to be able to use this in the entire application.
     *
     * @return The ReadOnlyObjectProperty containing the WorkingDirectory.
     */
    public ReadOnlyObjectProperty<WorkingDirectory> workingDirectoryProperty() {
        return workingDirectory;
    }

    /**
     * This method is used to handle the action if the user clicks on the button "Browse...".
     *
     * This opens a new DirectoryChooser to choose the Working Directory of the current program.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonBrowseClicked(ActionEvent event) {
        //Get the button in order to disable the button until the DirectoryChooser is closed
        Button buttonBrowse = (Button) event.getSource();
        buttonBrowse.setDisable(true);

        //Show DirectoryChooser Dialog in order to select a WorkingDirectory
        DirectoryChooser chooser = new DirectoryChooser();
        selectedWorkingDirectory = chooser.showDialog(null);
        //Enable button again, because directory is selected
        buttonBrowse.setDisable(false);

        if(selectedWorkingDirectory == null) {
            textSelectedDirectory.setText(res.getString("workingDirectory.path"));
            buttonSelectDirectory.setDisable(true);
        } else {
            textSelectedDirectory.setText(selectedWorkingDirectory.getAbsolutePath());
            buttonSelectDirectory.setDisable(false);
        }
    }

    /**
     * This method is used to handle the action if the user clicks on the button "Cancel".
     *
     * Go back to the WelcomeScreen
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonCancelClicked(ActionEvent event) {
        loadApplicationParts.loadWelcomeScreen();
    }

    /**
     * This method is used to handle the action if the user clicks on the button "Browse...".
     *
     * Set the Working Directory and execute the method do next from the interface.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonSelectDirectoryClicked(ActionEvent event) {
        workingDirectory.setValue(new WorkingDirectory(selectedWorkingDirectory));

        if(workingDirectoryNext != null) {
            workingDirectoryNext.doNext();
        }
    }
}
