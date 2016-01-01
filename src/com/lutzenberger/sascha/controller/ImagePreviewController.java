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
import com.lutzenberger.sascha.interfaces.LoadApplicationParts;
import com.lutzenberger.sascha.view.ImageProgress;

/**
 * Created by saschalutzenberger on 28/12/15.
 */
public class ImagePreviewController implements Initializable {
    private ResourceBundle res;

    @FXML
    private ImageView imageViewPreview;

    private LoadApplicationParts loadApplicationParts;

    private ObjectProperty<Image> previewImage = new SimpleObjectProperty<>();
    private int totalRotation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.res = resources;
    }

    public void setLoadApplicationParts(LoadApplicationParts loadApplicationParts) {
        this.loadApplicationParts = loadApplicationParts;

        WorkingDirectory workingDirectory = loadApplicationParts.workingDirectoryProperty().get();

        if(workingDirectory.getResizedImageList().isEmpty()) {
            //TODO: implement what should happen if there are no images
        } else {
            File imageFile = workingDirectory.getResizedImageList().get(0);

            try {
                BufferedImage bufferedImage = ImageIO.read(imageFile);

                this.previewImage.setValue(SwingFXUtils.toFXImage(bufferedImage, null));
                imageViewPreview.imageProperty().bind(previewImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void buttonRotateClockwiseClicked(ActionEvent event) {
        rotate(90);
    }

    @FXML
    private void buttonRotateCounterClockwiseClicked(ActionEvent event) {
        rotate(270);
    }

    @FXML
    private void buttonRotateAllImagesClicked(ActionEvent event) {
        ImageRotateTask imageRotateTask =
                new ImageRotateTask(loadApplicationParts.workingDirectoryProperty().get(), totalRotation);
        ImageProgress imageProgress =
                new ImageProgress(imageRotateTask, res.getString("application.title.rotate"));

        imageRotateTask.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                imageProgress.closeImageProgressWindow();
                ImagePreviewController.this.loadApplicationParts.loadWelcomeScreen();
            }
        });

        imageRotateTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                imageProgress.closeImageProgressWindow();
                ImagePreviewController.this.loadApplicationParts.loadSelectGalleryScreen();
            }
        });

        Thread t = new Thread(imageRotateTask);
        t.setDaemon(true);
        t.start();
    }

    private void rotate(int degree) {
        totalRotation = totalRotation + degree;

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(previewImage.get(), null);
        BufferedImage rotatedImage = ImageRotate.rotateImage(bufferedImage, degree);

        previewImage.setValue(SwingFXUtils.toFXImage(rotatedImage, null));
    }
}
