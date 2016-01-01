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

public class Main extends Application implements LoadApplicationParts {
    private final static ResourceBundle RES = ResourceBundle.getBundle("strings/Values");

    private Stage window;

    private LoginTask loginTask;
    private ObjectProperty<WorkingDirectory> workingDirectory = new SimpleObjectProperty<>();
    private ObservableList<ImageSrc> imageSrcObservableList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.window = primaryStage;

        //Start loginTask so that Login is already completed when user starts to use the program
        loginTask = new LoginTask();

        window.setTitle(RES.getString("application.title"));

        loadWelcomeScreen();

        window.show();

        Thread thread = new Thread(loginTask);
        thread.start();
    }

    @Override
    public ReadOnlyObjectProperty<WorkingDirectory> workingDirectoryProperty() {
        return workingDirectory;
    }

    @Override
    public void loadCreateLinksScreen() {
        try {
            hideWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/layout/CreateLinksScreen.fxml"));
            fxmlLoader.setResources(RES);

            Parent parent = fxmlLoader.load();
            CreateLinksController controller = fxmlLoader.getController();
            //Initialize the controller
            controller.addImageSrcList(imageSrcObservableList);

            //Initialize the data bindings

            Scene scene = new Scene(parent);
            window.setScene(scene);
            showWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadImagePreviewScreen() {
        try {
            hideWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/layout/ImagePreviewScreen.fxml"));
            fxmlLoader.setResources(RES);

            Parent parent = fxmlLoader.load();
            ImagePreviewController controller = fxmlLoader.getController();
            //Initialize the controller
            controller.setLoadApplicationParts(this);

            //Initialize the data bindings

            Scene scene = new Scene(parent);
            window.setScene(scene);
            showWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadSelectGalleryScreen() {
        try {
            hideWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/layout/SelectGalleryScreen.fxml"));
            fxmlLoader.setResources(RES);

            Parent parent = fxmlLoader.load();
            SelectGalleryController controller = fxmlLoader.getController();
            //Initialize the controller
            controller.setLoadApplicationParts(this);
            controller.addImageSrcList(imageSrcObservableList);

            //Initialize the data bindings
            controller.bindLoginText(loginTask.loginStatusProperty());
            controller.bindLoginResponse(loginTask.valueProperty());

            Scene scene = new Scene(parent);
            window.setScene(scene);
            showWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadSelectWorkingDirectoryScreen(WorkingDirectoryNext doNext) {
        try {
            hideWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/layout/SelectWorkingDirectoryScreen.fxml"));
            fxmlLoader.setResources(RES);

            Parent parent = fxmlLoader.load();
            SelectWorkingDirectoryController controller = fxmlLoader.getController();
            //Initialize the controller
            controller.setLoadApplicationParts(this);
            controller.setWorkingDirectoryNext(doNext);

            //Initialize the data bindings
            controller.bindLoginText(loginTask.loginStatusProperty());
            workingDirectory.bind(controller.workingDirectoryProperty());

            Scene scene = new Scene(parent);
            window.setScene(scene);
            showWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadWelcomeScreen() {
        try {
            hideWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/layout/WelcomeScreen.fxml"));
            fxmlLoader.setResources(RES);

            Parent parent = fxmlLoader.load();
            WelcomeController controller = fxmlLoader.getController();

            //Initialize the controller
            controller.setLoadApplicationParts(this);

            //Initialize the data bindings
            controller.bindLoginText(loginTask.loginStatusProperty());

            Scene scene = new Scene(parent);
            window.setScene(scene);
            showWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hideWindow() {
        window.hide();
    }

    private void showWindow() {
        window.show();
    }

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}