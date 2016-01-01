package com.lutzenberger.sascha.controller;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.lutzenberger.sascha.abload.GalleryManager;
import com.lutzenberger.sascha.abload.data.Gallery;
import com.lutzenberger.sascha.abload.data.LoginResponse;
import com.lutzenberger.sascha.concurrency.ImageUploadTask;
import com.lutzenberger.sascha.html.ImageSrc;
import com.lutzenberger.sascha.interfaces.LoadApplicationParts;
import com.lutzenberger.sascha.interfaces.LoginLabel;
import com.lutzenberger.sascha.interfaces.LoginResponseAble;
import com.lutzenberger.sascha.view.AddGallery;
import com.lutzenberger.sascha.view.ImageProgress;

/**
 * Created by saschalutzenberger on 28/12/15.
 */
public class SelectGalleryController implements Initializable, LoginLabel, LoginResponseAble {
    private ResourceBundle res;

    @FXML
    private ChoiceBox<Gallery> choiceBoxGallery;

    @FXML
    private Button buttonSelectGallery;

    @FXML
    private Text textLoginStatus;

    @FXML
    private Button buttonRefreshGalleries;

    @FXML
    private Button buttonAddGallery;

    private LoadApplicationParts loadApplicationParts;
    private ObjectProperty<LoginResponse> loginResponse = new SimpleObjectProperty<>();
    private List<ImageSrc> imageSrcList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.res = resources;
    }

    public void setLoadApplicationParts(LoadApplicationParts loadApplicationParts) {
        this.loadApplicationParts = loadApplicationParts;
    }

    @Override
    public void bindLoginText(ReadOnlyStringProperty loginStatusProperty) {
        textLoginStatus.textProperty().bind(loginStatusProperty);
    }

    @Override
    public void bindLoginResponse(ReadOnlyObjectProperty<LoginResponse> loginResponseProperty) {
        loginResponse.bind(loginResponseProperty);

        if(loginResponseProperty.getValue() != null) {
            updateChoiceBoxGallery();
        }

        loginResponse.addListener(new ChangeListener<LoginResponse>() {
            @Override
            public void changed(ObservableValue<? extends LoginResponse> observable, LoginResponse oldValue, LoginResponse newValue) {
                updateChoiceBoxGallery();
            }
        });
    }

    public void addImageSrcList(List<ImageSrc> imageSrcList) {
        this.imageSrcList = imageSrcList;
    }

    @FXML
    private void buttonSelectGalleryClicked(ActionEvent event) {
        Gallery selectedGallery = choiceBoxGallery.getValue();

        ImageUploadTask imageUploadTask = new ImageUploadTask(loadApplicationParts.workingDirectoryProperty().get(),
                selectedGallery, loginResponse.get());
        ImageProgress imageProgress = new ImageProgress(imageUploadTask, res.getString("application.title.upload"));

        imageUploadTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                imageProgress.closeImageProgressWindow();
                Platform.exit();
            }
        });

        imageUploadTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                imageProgress.closeImageProgressWindow();
                imageSrcList.addAll(imageUploadTask.getValue());
                unbind();

                loadApplicationParts.loadCreateLinksScreen();
            }
        });

        Thread t = new Thread(imageUploadTask);
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void buttonRefreshGalleriesClicked(ActionEvent event) {
        updateChoiceBoxGallery();
    }

    @FXML
    private void buttonAddGalleryClicked(ActionEvent event) {
        AddGallery addGallery = new AddGallery(textLoginStatus.textProperty(), loginResponse);

        updateChoiceBoxGallery();
    }

    private void updateChoiceBoxGallery() {
        buttonSelectGallery.setDisable(false);
        buttonRefreshGalleries.setDisable(false);
        buttonAddGallery.setDisable(false);

        GalleryManager galleryManager = new GalleryManager(loginResponse.get());

        choiceBoxGallery.getItems().setAll(galleryManager.getGalleries());
        choiceBoxGallery.getSelectionModel().selectFirst();
    }

    private void unbind() {
        textLoginStatus.textProperty().unbind();
        loginResponse.unbind();
    }
}
