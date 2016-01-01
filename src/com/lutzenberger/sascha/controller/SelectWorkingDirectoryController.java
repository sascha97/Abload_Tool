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
import com.lutzenberger.sascha.interfaces.LoadApplicationParts;
import com.lutzenberger.sascha.interfaces.LoginLabel;
import com.lutzenberger.sascha.interfaces.WorkingDirectoryNext;

/**
 * Created by saschalutzenberger on 28/12/15.
 */
public class SelectWorkingDirectoryController implements Initializable, LoginLabel {
    private ResourceBundle res;

    @FXML
    private Text textLoginStatus;

    @FXML
    private Text textSelectedDirectory;

    @FXML
    private Button buttonSelectDirectory;

    private LoadApplicationParts loadApplicationParts;

    private ObjectProperty<WorkingDirectory> workingDirectory = new SimpleObjectProperty<>();
    private File selectedWorkingDirectory;
    private WorkingDirectoryNext workingDirectoryNext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        res = resources;
    }

    public void setWorkingDirectoryNext(WorkingDirectoryNext workingDirectoryNext) {
        this.workingDirectoryNext = workingDirectoryNext;
    }

    public void setLoadApplicationParts(LoadApplicationParts loadApplicationParts) {
        this.loadApplicationParts = loadApplicationParts;
    }

    @Override
    public void bindLoginText(ReadOnlyStringProperty loginLabelProperty) {
        textLoginStatus.textProperty().bind(loginLabelProperty);
    }

    public ReadOnlyObjectProperty<WorkingDirectory> workingDirectoryProperty() {
        return workingDirectory;
    }

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

    @FXML
    private void buttonCancelClicked(ActionEvent event) {
        loadApplicationParts.loadWelcomeScreen();
    }

    @FXML
    private void buttonSelectDirectoryClicked(ActionEvent event) {
        workingDirectory.setValue(new WorkingDirectory(selectedWorkingDirectory));

        if(workingDirectoryNext != null) {
            workingDirectoryNext.doNext();
        }
    }
}
