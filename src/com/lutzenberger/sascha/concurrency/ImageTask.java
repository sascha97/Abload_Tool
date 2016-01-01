package com.lutzenberger.sascha.concurrency;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.concurrent.Task;

import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import com.lutzenberger.sascha.image.WorkingDirectory;

/**
 * Created by saschalutzenberger on 28/12/15.
 */
public abstract class ImageTask<V> extends Task<V> {
    private final static ResourceBundle RES = ResourceBundle.getBundle("strings/Values");

    /**
     * Used to send updates in a thread safe manner from the subclass to the FXApplicationThread.
     * AtomicReference is used so as to coalesce updates such that the event queue doesn't get flooded.
     */
    private AtomicReference<String> atomicReferenceProgressLabel = new AtomicReference<>();

    /**
     * Used to send updates in a thread safe manner from the subclass to the FXApplicationThread.
     * AtomicReference is used so as to coalesce updates such that the event queue doesn't get flooded.
     */
    private AtomicReference<String> atomicReferenceCurrentImage = new AtomicReference<>();

    /**
     * Used to send updates in a thread safe manner from the subclass to the FXApplicationThread.
     * AtomicReference is used so as to coalesce updates such that the event queue doesn't get flooded.
     */
    private AtomicReference<Integer> atomicReferenceRemainingImages = new AtomicReference<>();

    /**
     * Used to send updates in a thread safe manner from the subclass to the FXApplicationThread.
     * AtomicReference is used so as to coalesce updates such that the event queue doesn't get flooded.
     */
    private AtomicReference<String> atomicReferenceRemainingTime = new AtomicReference<>();

    private StringProperty progressLabelProperty = new SimpleStringProperty();
    private StringProperty currentImageProperty = new SimpleStringProperty();
    private IntegerProperty remainingImagesProperty = new SimpleIntegerProperty();
    private StringProperty remainingTimeProperty = new SimpleStringProperty();

    protected WorkingDirectory workingDirectory;

    private String progressLabelFormat = "%3.2f %%";

    //The following variables are used to compute the time spent inside the thread
    private long lastTime; //Holds the last update time in milliseconds
    private double average = 1.0; //The average time spend until update is performed in milliseconds
    private int updateCounter = 0; //This counts the number of updates

    protected ImageTask(WorkingDirectory workingDirectory) {
        progressLabelProperty.setValue(String.format(progressLabelFormat, 0f));
        currentImageProperty.setValue(RES.getString("progress.pending"));
        remainingTimeProperty.setValue(RES.getString("progress.calculating"));

        this.workingDirectory = workingDirectory;
    }

    public final ReadOnlyStringProperty progressLabelProperty() {
        return progressLabelProperty;
    }

    public final ReadOnlyStringProperty currentImageProperty() {
        return currentImageProperty;
    }

    public final ReadOnlyIntegerProperty remainingImagesProperty() {
        return remainingImagesProperty;
    }

    public final ReadOnlyStringProperty remainingTimeProperty() {
        return remainingTimeProperty;
    }

    protected final void updateCurrentImage(String currentImage) {
        lastTime = System.currentTimeMillis(); //Update the access time

        if(isFxApplicationThread()) {
            this.currentImageProperty.setValue(currentImage);
        } else if(atomicReferenceCurrentImage.getAndSet(currentImage) == null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String currentImage = atomicReferenceCurrentImage.getAndSet(null);
                    ImageTask.this.currentImageProperty.setValue(currentImage);
                }
            });
        }
    }

    protected final void updateRemainingImages(int remainingImages) {
        if(isFxApplicationThread()) {
            remainingImagesProperty.setValue(remainingImages);
        } else if(atomicReferenceRemainingImages.getAndSet(remainingImages) == null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    int remainingImages = atomicReferenceRemainingImages.getAndSet(null);
                    ImageTask.this.remainingImagesProperty.setValue(remainingImages);
                }
            });
        }
    }

    protected void updateRemainingTime() {
        int timeDiff = (int) (System.currentTimeMillis() - lastTime);
        average = (average * updateCounter + timeDiff) / (updateCounter + 1);

        double timeMillis = remainingImagesProperty.get() * average;
        int seconds = (int) Math.floor(timeMillis/1000);

        String message = String.format(RES.getString("progress.secondsRemaining"), seconds);

        if(isFxApplicationThread()) {
            remainingTimeProperty.setValue(message);
        } else if(atomicReferenceRemainingTime.getAndSet(message) == null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String message = atomicReferenceRemainingTime.getAndSet(null);
                    ImageTask.this.remainingTimeProperty.setValue(message);
                }
            });
        }

        updateCounter++;
    }

    @Override
    protected void updateProgress(double workDone, double max) {
        super.updateProgress(workDone, max);

        updateProgressLabel(workDone/max);
    }

    private void updateProgressLabel(double progress) {
        double percentage = progress * 100;

        String message = String.format(progressLabelFormat, percentage);

        if(isFxApplicationThread()) {
            progressLabelProperty.setValue(message);
        } else if(atomicReferenceProgressLabel.getAndSet(message) == null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String message = atomicReferenceProgressLabel.getAndSet(null);
                    ImageTask.this.progressLabelProperty.setValue(message);
                }
            });
        }
    }

    private boolean isFxApplicationThread() {
        return Platform.isFxApplicationThread();
    }
}
