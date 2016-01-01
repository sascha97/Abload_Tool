package com.lutzenberger.sascha.controller;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import com.lutzenberger.sascha.abload.GalleryManager;
import com.lutzenberger.sascha.abload.data.LoginResponse;
import com.lutzenberger.sascha.interfaces.LoginLabel;
import com.lutzenberger.sascha.interfaces.LoginResponseAble;

/**
 * Created by saschalutzenberger on 31/12/15.
 */
public class AddGalleryController implements Initializable, LoginLabel, LoginResponseAble {
    private ResourceBundle res;

    @FXML
    private Button buttonCreateGallery;

    @FXML
    private TextField textFieldGalleryName;

    @FXML
    private Text textLoginStatus;

    private SimpleObjectProperty<LoginResponse> loginResponse = new SimpleObjectProperty<>();
    private BooleanProperty finished = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFieldGalleryName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                boolean isEmpty = observable.getValue().trim().isEmpty();
                if(isEmpty) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            textFieldGalleryName.textProperty().setValue("");
                        }
                    });
                }
            }
        });

        buttonCreateGallery.disableProperty().bind(textFieldGalleryName.textProperty().isEmpty());
    }

    public ReadOnlyBooleanProperty finishedProperty() {
        return finished;
    }

    @Override
    public void bindLoginText(ReadOnlyStringProperty loginStatusProperty) {
        textLoginStatus.textProperty().bind(loginStatusProperty);
    }

    @Override
    public void bindLoginResponse(ReadOnlyObjectProperty<LoginResponse> loginResponseProperty) {
        loginResponse.bind(loginResponseProperty);
    }

    @FXML
    private void buttonCancelClicked(ActionEvent event) {
        unbind();
        finished.setValue(true);
    }

    @FXML
    private void buttonCreateGalleryClicked(ActionEvent event) {
        String galleryName = textFieldGalleryName.textProperty().getValue();

        GalleryManager galleryManager = new GalleryManager(loginResponse.getValue());
        boolean result = galleryManager.createNewGallery(galleryName);

        unbind();
        finished.setValue(true);
    }

    private void unbind() {
        textLoginStatus.textProperty().unbind();
        loginResponse.unbind();
    }
}
