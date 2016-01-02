package com.lutzenberger.sascha;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

import com.lutzenberger.sascha.concurrency.LoginTask;
import com.lutzenberger.sascha.controller.*;
import com.lutzenberger.sascha.html.ImageSrc;
import com.lutzenberger.sascha.image.WorkingDirectory;
import com.lutzenberger.sascha.interfaces.LoadApplicationParts;
import com.lutzenberger.sascha.interfaces.WorkingDirectoryNext;

/**
 * This is the Main Class for the JavaFx 8 Application.
 *
 * This class contains all the necessary models which have to be shared across the entire program.
 * e.g. The LoginTask which is holding the Login response, the ObjectProperty<WorkingDirectory> which is holding the
 * directory the program should work in. and the ObservableList<ImageSrc> list which is holding all ImageSrc objects
 * in order to be able to build a table in the CreateLinksScreen.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 30.12.2015
 */
public class Main extends Application implements LoadApplicationParts {
    //The resource bundle with all the application strings in it
    private final static ResourceBundle RES = ResourceBundle.getBundle("strings/Values");

    //The window of the application
    private Stage window;

    //The LoginTask runs in a own task so that login is possible concurrent.
    private LoginTask loginTask;
    //The workingDirectory Property will be bound to the SelectWorkingDirectory screen in order to get updated
    private ObjectProperty<WorkingDirectory> workingDirectory = new SimpleObjectProperty<>();
    //The List of ImageSrc of http://www.abload.de
    private ObservableList<ImageSrc> imageSrcObservableList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Save the window globally in the class
        this.window = primaryStage;

        //Start loginTask so that Login is already completed when user starts to use the program
        loginTask = new LoginTask();

        //Set the main application title
        window.setTitle(RES.getString("application.title"));

        loadWelcomeScreen();

        window.show();

        //Start the LoginTask in a new Thread so that task will be executed whilst the user interacts with the program
        Thread thread = new Thread(loginTask);
        thread.start();
    }

    /**
     * Get the WorkingDirectory Property as ReadOnlyProperty to be able to bind this to the Controllers.
     *
     * @return The WorkingDirectory Property which can be easily bound to any controllers. (If controller has a method
     *         accepting that object Property)
     */
    @Override
    public ReadOnlyObjectProperty<WorkingDirectory> workingDirectoryProperty() {
        return workingDirectory;
    }

    @Override
    public void loadCreateLinksScreen() {
        try {
            //Hide window so that it can be redrawn and centered again
            hideWindow();
            //Load the screen from the FXML file and give the ResourceBundle to the loader.
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/layout/CreateLinksScreen.fxml"));
            fxmlLoader.setResources(RES);

            //Load the Parent and the controller
            Parent parent = fxmlLoader.load();
            CreateLinksController controller = fxmlLoader.getController();

            //Initialize the controller
            controller.addImageSrcList(imageSrcObservableList);

            //Initialize the data bindings

            //Set a new scene with the view in it and add it to the main window
            Scene scene = new Scene(parent);
            window.setScene(scene);
            //Show window centered again
            showWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadImagePreviewScreen() {
        try {
            //Hide window so that it can be redrawn and centered again
            hideWindow();
            //Load the screen from the FXML file and give the ResourceBundle to the loader.
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/layout/ImagePreviewScreen.fxml"));
            fxmlLoader.setResources(RES);

            //Load the Parent and the Controller
            Parent parent = fxmlLoader.load();
            ImagePreviewController controller = fxmlLoader.getController();

            //Initialize the controller
            controller.setLoadApplicationParts(this);

            //Initialize the data bindings

            //Set a new scene with the view in it and add it to the main window
            Scene scene = new Scene(parent);
            window.setScene(scene);
            //Show the window centered again
            showWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadSelectGalleryScreen() {
        try {
            //Hide window so that it can be redrawn and centered again
            hideWindow();
            //Load the screen from the FXML file and give the ResourceBundle to the loader.
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/layout/SelectGalleryScreen.fxml"));
            fxmlLoader.setResources(RES);

            //Load the Parent and the Controller.
            Parent parent = fxmlLoader.load();
            SelectGalleryController controller = fxmlLoader.getController();
            //Initialize the controller
            controller.setLoadApplicationParts(this);
            controller.addImageSrcList(imageSrcObservableList);

            //Initialize the data bindings
            controller.bindLoginText(loginTask.loginStatusProperty());
            controller.bindLoginResponse(loginTask.valueProperty());

            //Set a new scene with the view in it and add it to the main window
            Scene scene = new Scene(parent);
            window.setScene(scene);
            //Show the window centred again
            showWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadSelectWorkingDirectoryScreen(WorkingDirectoryNext doNext) {
        try {
            //Hide window so that it can be redrawn and centered again
            hideWindow();
            //Load the screen from the FXML file and give the ResourceBundle to the loader.
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/layout/SelectWorkingDirectoryScreen.fxml"));
            fxmlLoader.setResources(RES);

            //Load the Parent and the Controller
            Parent parent = fxmlLoader.load();
            SelectWorkingDirectoryController controller = fxmlLoader.getController();

            //Initialize the controller
            controller.setLoadApplicationParts(this);
            controller.setWorkingDirectoryNext(doNext);

            //Initialize the data bindings
            controller.bindLoginText(loginTask.loginStatusProperty());
            workingDirectory.bind(controller.workingDirectoryProperty());

            //Set a new scene with the view in it and add it to the main window
            Scene scene = new Scene(parent);
            window.setScene(scene);
            //Show the window centered again
            showWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadWelcomeScreen() {
        try {
            //Hide window so that it can be redrawn and centered again
            hideWindow();
            //Load the screen from the FXML file and give the ResourceBundle to the loader.
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/layout/WelcomeScreen.fxml"));
            fxmlLoader.setResources(RES);

            //Load the Parent and the Controller
            Parent parent = fxmlLoader.load();
            WelcomeController controller = fxmlLoader.getController();

            //Initialize the controller
            controller.setLoadApplicationParts(this);

            //Initialize the data bindings
            controller.bindLoginText(loginTask.loginStatusProperty());

            //Set a new scene with the view in it and add it to the main window
            Scene scene = new Scene(parent);
            window.setScene(scene);
            //Show the window centered again
            showWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Hides the main window
    private void hideWindow() {
        window.hide();
    }

    //Shows the main window again
    private void showWindow() {
        window.show();
    }

    /**
     * This is the main method. This method is launching the JavaFx 8 Application
     *
     * @param args They will be ignored anyway.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}