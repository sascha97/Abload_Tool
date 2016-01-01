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
 * Created by saschalutzenberger on 28/12/15.
 */
public class ImageProgressController implements Initializable {
    @FXML
    private ProgressBar progressBarImageProgress;

    @FXML
    private Text textImageProgress;

    @FXML
    private Text textCurrentImage;

    @FXML
    private Text textRemainingImages;

    @FXML
    private Text textRemainingTime;

    private ImageTask imageTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void buttonCancelClicked(ActionEvent event) {
        imageTask.cancel();
        System.out.println("Cancelled Task");
    }

    public void bindProperties(ImageTask imageTask) {
        this.imageTask = imageTask;

        progressBarImageProgress.progressProperty().bind(imageTask.progressProperty());
        textImageProgress.textProperty().bind(imageTask.progressLabelProperty());
        textCurrentImage.textProperty().bind(imageTask.currentImageProperty());
        textRemainingImages.textProperty().bind(imageTask.remainingImagesProperty().asString());
        textRemainingTime.textProperty().bind(imageTask.remainingTimeProperty());
    }

    public void unbindProperties() {
        progressBarImageProgress.progressProperty().unbind();
        textImageProgress.textProperty().unbind();
        textCurrentImage.textProperty().unbind();
        textRemainingImages.textProperty().unbind();
        textRemainingTime.textProperty().unbind();
    }
}
