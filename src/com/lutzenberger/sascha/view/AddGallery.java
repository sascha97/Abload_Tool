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
 * Created by saschalutzenberger on 31/12/15.
 */
public class AddGallery {
    private final static String SCREEN = "/layout/AddGalleryScreen.fxml";
    private FXMLLoader fxmlLoader = new FXMLLoader(AddGallery.class.getResource(SCREEN));

    AddGalleryController controller;
    private Stage window;

    public AddGallery(ReadOnlyStringProperty loginText, ReadOnlyObjectProperty<LoginResponse> loginResponse) {
        try {
            ResourceBundle res = ResourceBundle.getBundle("strings/Values");
            fxmlLoader.setResources(res);

            Parent root = fxmlLoader.load();
            controller = fxmlLoader.getController();

            controller.bindLoginText(loginText);
            controller.bindLoginResponse(loginResponse);

            controller.finishedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                window.close();
                            }
                        });
                    }
                }
            });

            window = new Stage();
            Scene scene = new Scene(root);

            window.initModality(Modality.APPLICATION_MODAL);
            window.setScene(scene);

            window.setTitle(res.getString("application.title"));
            window.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
