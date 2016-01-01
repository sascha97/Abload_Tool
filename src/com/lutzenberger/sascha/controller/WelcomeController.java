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
import com.lutzenberger.sascha.interfaces.LoadApplicationParts;
import com.lutzenberger.sascha.interfaces.LoginLabel;
import com.lutzenberger.sascha.interfaces.WorkingDirectoryNext;
import com.lutzenberger.sascha.view.ImageProgress;

/**
 * Created by saschalutzenberger on 28/12/15.
 */
public class WelcomeController implements Initializable, LoginLabel {
    private ResourceBundle res;

    @FXML
    private Text textLoginStatus;

    private LoadApplicationParts loadApplicationParts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.res = resources;
    }

    public void setLoadApplicationParts(LoadApplicationParts loadApplicationParts) {
        this.loadApplicationParts = loadApplicationParts;
    }

    @Override
    public void bindLoginText(ReadOnlyStringProperty loginStatusProperty) {
        textLoginStatus.textProperty().bind(loginStatusProperty);
    }

    @FXML
    private void buttonResizeUploadCreateLinksClicked(ActionEvent event) {
        WorkingDirectoryNext workingDirectoryNext = new WorkingDirectoryNext() {
            @Override
            public void doNext() {
                ImageResizeTask imageResizeTask =
                        new ImageResizeTask(loadApplicationParts.workingDirectoryProperty().get());
                ImageProgress imageProgress =
                        new ImageProgress(imageResizeTask, res.getString("application.title.resize"));

                imageResizeTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        imageProgress.closeImageProgressWindow();
                    }
                });

                imageResizeTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        imageProgress.closeImageProgressWindow();
                        WelcomeController.this.loadApplicationParts.loadImagePreviewScreen();
                    }
                });

                Thread t = new Thread(imageResizeTask);
                t.setDaemon(true);
                t.start();
            }
        };

        loadApplicationParts.loadSelectWorkingDirectoryScreen(workingDirectoryNext);
    }

    @FXML
    private void buttonUploadCreateLinksClicked(ActionEvent event) {
        WorkingDirectoryNext workingDirectoryNext = new WorkingDirectoryNext() {
            @Override
            public void doNext() {
                WelcomeController.this.loadApplicationParts.loadSelectGalleryScreen();
            }
        };

        loadApplicationParts.loadSelectWorkingDirectoryScreen(workingDirectoryNext);
    }

    @FXML
    private void buttonCreateLinksClicked(ActionEvent event) {
        loadApplicationParts.loadCreateLinksScreen();
    }

    @FXML
    private void menuCloseApplicationClicked(ActionEvent event) {
        Platform.exit();
    }
}
