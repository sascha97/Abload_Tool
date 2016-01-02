package com.lutzenberger.sascha.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.lutzenberger.sascha.concurrency.ImageRotateTask;
import com.lutzenberger.sascha.image.ImageRotate;
import com.lutzenberger.sascha.image.WorkingDirectory;
import com.lutzenberger.sascha.interfaces.ApplicationPartAble;
import com.lutzenberger.sascha.interfaces.LoadApplicationParts;
import com.lutzenberger.sascha.view.ImageProgress;

/**
 * This is the Controller for the ImagePreviewScreen.
 *
 * This controller handles the user interactions with the rotation of the images for the entire application.
 * This controller handles the preview of the Rotation on a single image. The image which is used for the preview
 * is the first image in the working directory.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
 */
public class ImagePreviewController implements Initializable, ApplicationPartAble {
    //The ResourceBundle that contains all the application strings
    private ResourceBundle res;

    /**
     * The ImageView that is responsible for displaying the preview Image to the user so that the user can pick the
     * right rotation for the entire application.
     */
    @FXML
    private ImageView imageViewPreview;

    /**
     * This is used to load different application parts from the current controller.
     * e.g. (switching screens or getting the working directory property)
     */
    private LoadApplicationParts loadApplicationParts;

    //The Property containing the preview image
    private ObjectProperty<Image> previewImage = new SimpleObjectProperty<>();
    //The rotation which has to be applied to all images in the entire application.
    private int totalRotation;

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

        //Get the working Directory of the application.
        WorkingDirectory workingDirectory = loadApplicationParts.workingDirectoryProperty().get();

        //If no resized images are available show a dialog to let the user choose what to do
        if(workingDirectory.getResizedImageList().isEmpty()) {
            //TODO: implement what should happen if there are no images
        } else {
            //Get the first image of the list as preview image
            File imageFile = workingDirectory.getResizedImageList().get(0);

            try {
                //Read in the image
                BufferedImage bufferedImage = ImageIO.read(imageFile);

                //Convert the BufferedImage to a FXImage.
                this.previewImage.setValue(SwingFXUtils.toFXImage(bufferedImage, null));
                //Bind the image of the ObjectProperty to the imagePreview in order to have less code
                imageViewPreview.imageProperty().bind(previewImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to handle the action if the user clicks on the "Rotate Clockwise" button on the screen.
     *
     * @param event The event from the JavaFx Application Thread.
     */
    @FXML
    private void buttonRotateClockwiseClicked(ActionEvent event) {
        rotate(90);
    }

    /**
     * This method is used to handle the action if the user clicks on the "Rotate Counterclockwise" button on the
     * screen.
     *
     * @param event The event from the JavaFx Application Thread.
     */
    @FXML
    private void buttonRotateCounterClockwiseClicked(ActionEvent event) {
        rotate(270);
    }

    /**
     * This method is used to handle the action if the user clicks on the "Rotate Images" button on the screen.
     *
     * This will apply the current rotation to all images in the resized folder of the working directory.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonRotateAllImagesClicked(ActionEvent event) {
        //Create new concurrent task so that this will be done without freezing the UI
        ImageRotateTask imageRotateTask =
                new ImageRotateTask(loadApplicationParts.workingDirectoryProperty().get(), totalRotation);
        //Getting an Progress Window that will be displaying the current progress.
        ImageProgress imageProgress =
                new ImageProgress(imageRotateTask, res.getString("application.title.rotate"));

        //If the task will be cancelled the Welcome Screen should be loaded
        imageRotateTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                imageProgress.closeImageProgressWindow();
                ImagePreviewController.this.loadApplicationParts.loadWelcomeScreen();
            }
        });

        //If everything was completed successfully go to the next screen.
        imageRotateTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                imageProgress.closeImageProgressWindow();
                ImagePreviewController.this.loadApplicationParts.loadSelectGalleryScreen();
            }
        });

        //Start the task in a new thread.
        Thread t = new Thread(imageRotateTask);
        t.setDaemon(true);
        t.start();
    }

    /**
     * This method is responsible for rotating the preview Image in the given rotation.
     *
     * @param degree The rotation in degree in which the image should be rotated
     */
    private void rotate(int degree) {
        //Add the rotation to the rotation of the images
        totalRotation = totalRotation + degree;

        //Get the image from the Preview
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(previewImage.get(), null);
        //Rotate the image
        BufferedImage rotatedImage = ImageRotate.rotateImage(bufferedImage, degree);

        //Set the rotated image to the previewImage property. Automatic update on the ui through data binding.
        previewImage.setValue(SwingFXUtils.toFXImage(rotatedImage, null));
    }
}
