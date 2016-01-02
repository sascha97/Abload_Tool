package com.lutzenberger.sascha.view;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import com.lutzenberger.sascha.abload.data.LoginResponse;
import com.lutzenberger.sascha.controller.AddGalleryController;

/**
 * This class Handles the Adding of a Gallery to http://www.abload.de
 *
 * This class creates a new window which is displayed as long as the user does click on create gallery.
 * The window will be closed afterwards.
 * The class also loads the screen from an FXML file and initializes it.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 31.12.2015
 */
public class AddGallery {
    //The path of the Screen in the application which has to be loaded by the FMXLLoader
    private final static String SCREEN = "/layout/AddGalleryScreen.fxml";

    //The window of the AddGalleryScreen which is APPLICATION_MODAL
    private Stage window;

    /**
     * Constructs a new AddGallery screen.
     *
     * @param loginText The property containing the login status
     * @param loginResponse The property containing the LoginResponse
     */
    public AddGallery(ReadOnlyStringProperty loginText, ReadOnlyObjectProperty<LoginResponse> loginResponse) {
        try {
            //Load the screen from an FXML file and give the ResourceBundle to the loader.
            FXMLLoader fxmlLoader = new FXMLLoader(AddGallery.class.getResource(SCREEN));
            ResourceBundle res = ResourceBundle.getBundle("strings/Values");
            fxmlLoader.setResources(res);

            //Load the Parent and the Controller.
            Parent root = fxmlLoader.load();
            //The Gallery controller which is managing everything.
            AddGalleryController controller = fxmlLoader.getController();

            //Initialize the data bindings of the controller
            controller.bindLoginText(loginText);
            controller.bindLoginResponse(loginResponse);

            //When finishedProperty = true then adding the gallery has been completed so the window can be closed.
            controller.finishedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    //If finished == true
                    if(newValue) {
                        //Has to be done inside the JavaFx Application Thread
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                window.close();
                            }
                        });
                    }
                }
            });

            //Create a new window and a new scene with the view in it
            window = new Stage();
            Scene scene = new Scene(root);

            //Set the window application modal so that it is possible to have two windows at the same time
            window.initModality(Modality.APPLICATION_MODAL);
            //Set the scene with the view in it to the window
            window.setScene(scene);

            //Set the title and show the window
            window.setTitle(res.getString("application.title"));
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
