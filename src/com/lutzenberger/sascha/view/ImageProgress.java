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
 * Created by saschalutzenberger on 28/12/15.
 */
public class ImageProgress {
    private final static String SCREEN = "/layout/ImageProgressScreen.fxml";
    private FXMLLoader fxmlLoader = new FXMLLoader(ImageProgress.class.getResource(SCREEN));

    private ImageProgressController controller;
    private Stage window;

    public ImageProgress(ImageTask imageTask, String title) {
        try {
            ResourceBundle res = ResourceBundle.getBundle("strings/Values");
            fxmlLoader.setResources(res);

            Parent root = fxmlLoader.load();
            controller = fxmlLoader.getController();

            window = new Stage();
            Scene scene = new Scene(root);

            window.initModality(Modality.APPLICATION_MODAL);
            window.setScene(scene);

            controller.bindProperties(imageTask);

            window.setTitle(title);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeImageProgressWindow() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.unbindProperties();
                window.close();
            }
        });
    }
}
