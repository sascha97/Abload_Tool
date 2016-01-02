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
