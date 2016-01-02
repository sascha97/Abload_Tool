package com.lutzenberger.sascha.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import com.lutzenberger.sascha.concurrency.ImageTask;
import com.lutzenberger.sascha.controller.ImageProgressController;

/**
 * This class Handles the Image Progress.
 *
 * This class creates a new window which is displayed as long as the image progress is shown.
 * It will be closed afterwards.
 * The class loads the screen from an FXML file and initializes it.
 *
 * @author Sascha Lutzenbergeer
 * @version 1.0 - 28.12.2015
 */
public class ImageProgress {
    //The path of the Screen in the application which has to be loaded by the FMXLLoader
    private final static String SCREEN = "/layout/ImageProgressScreen.fxml";

    //The ImageProgressController which is displaying all the data of an ImageTask
    private ImageProgressController controller;
    //The window of the ImageProgress which is APPLICATION_MODAL
    private Stage window;

    /**
     * Constructs a new ImageProgress
     *
     * @param imageTask The ImageTask which should be bound to the User Interface
     * @param title The Title of the Window
     */
    public ImageProgress(ImageTask imageTask, String title) {
        try {
            //Load the screen from the FXML file and give the ResourceBundle to the loader.
            FXMLLoader fxmlLoader = new FXMLLoader(ImageProgress.class.getResource(SCREEN));
            ResourceBundle res = ResourceBundle.getBundle("strings/Values");
            fxmlLoader.setResources(res);

            //Load the Parent and the Controller
            Parent root = fxmlLoader.load();
            controller = fxmlLoader.getController();

            //Initialize the bindings of the controller
            controller.bindProperties(imageTask);

            //Create a new window and a new scene with the view in it
            window = new Stage();
            Scene scene = new Scene(root);

            //Set the window application modal so that it is possible to have two windows at the same time
            window.initModality(Modality.APPLICATION_MODAL);
            //Set the scene with the view in it to the window
            window.setScene(scene);

            //Set the title and show the window
            window.setTitle(title);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method for closing the window
    public void closeImageProgressWindow() {
        //Has to be performed in the JavaFxApplication Thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //unbind all the properties again
                controller.unbindProperties();
                //Close the window
                window.close();
            }
        });
    }
}
