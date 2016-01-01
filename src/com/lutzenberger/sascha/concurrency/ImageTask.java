package com.lutzenberger.sascha.concurrency;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.concurrent.Task;

import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import com.lutzenberger.sascha.image.WorkingDirectory;

/**
 * This is an abstract class for all the image tasks used in this application.
 * This class offers some new Properties which are handy for an ImageTask.
 *
 * This class also provides methods to update those Properties. Those properties can be bound to the UI.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
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

    /**
     * The StringProperty which is representing the progressLabel formatted as "0.00 %" or "23.54 %"
     */
    private StringProperty progressLabelProperty = new SimpleStringProperty();

    /**
     * The StringProperty which is containing the name of the currently processed image.
     */
    private StringProperty currentImageProperty = new SimpleStringProperty();

    /**
     * A IntegerProperty which is holding the number of remaining images.
     */
    private IntegerProperty remainingImagesProperty = new SimpleIntegerProperty();

    /**
     * A StringProperty which is holding the remaining time String.
     */
    private StringProperty remainingTimeProperty = new SimpleStringProperty();

    /**
     * The directory the program should work in.
     */
    protected WorkingDirectory workingDirectory;

    //A constant containing the format String for the progress label
    private final static String PROGRESS_LABEL_FORMAT = "%3.2f %%";

    //The following variables are used to compute the time spent inside the thread
    private long lastTime; //Holds the last update time in milliseconds
    private double average = 1.0; //The average time spend until update is performed in milliseconds
    private int updateCounter = 0; //This counts the number of updates

    /**
     * Constructor for the ImageTask. The image task needs a directory to do its work.
     *
     * @param workingDirectory The working directory in which the task should operate.
     */
    protected ImageTask(WorkingDirectory workingDirectory) {
        //Set up the Properties to their default Strings
        progressLabelProperty.setValue(String.format(PROGRESS_LABEL_FORMAT, 0f));
        currentImageProperty.setValue(RES.getString("progress.pending"));
        remainingTimeProperty.setValue(RES.getString("progress.calculating"));

        this.workingDirectory = workingDirectory;
    }

    /**
     * Get the ProgressLabel Property as ReadOnlyProperty to be able to bind this to an User Interface.
     *
     * @return The ProgressLabel Property which can be easily bound to an User Interface.
     */
    public final ReadOnlyStringProperty progressLabelProperty() {
        return progressLabelProperty;
    }

    /**
     * Get the ProgressLabel Property as ReadOnlyProperty to be able to bind this to an User Interface.
     *
     * @return The ProgressLabel Property which can be easily bound to an User Interface.
     */
    public final ReadOnlyStringProperty currentImageProperty() {
        return currentImageProperty;
    }

    /**
     * Get the RemainingImages Property as ReadOnlyProperty to be able to bind this to an User Interface.
     *
     * @return The RemainingImages Property which can be easily bound to an User Interface.
     */
    public final ReadOnlyIntegerProperty remainingImagesProperty() {
        return remainingImagesProperty;
    }

    /**
     * Get the RemainingTime Property as ReadOnlyProperty to be able to bind this to an User Interface.
     *
     * @return The RemainingTime Property which can be easily bound to an User Interface.
     */
    public final ReadOnlyStringProperty remainingTimeProperty() {
        return remainingTimeProperty;
    }

    /**
     * This is the method for updating the CurrentImage Property. This method has to be called before any processing
     * has been done.
     *
     * @param currentImage The name of the current Image which should be processed by the task.
     */
    protected final void updateCurrentImage(String currentImage) {
        lastTime = System.currentTimeMillis(); //Update the access time

        //This can only be updated inside the FxApplicationThread
        if(isFxApplicationThread()) {
            this.currentImageProperty.setValue(currentImage);
        } else if(atomicReferenceCurrentImage.getAndSet(currentImage) == null) {
            //This can only be updated inside the FxApplicationThread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String currentImage = atomicReferenceCurrentImage.getAndSet(null);
                    ImageTask.this.currentImageProperty.setValue(currentImage);
                }
            });
        }
    }

    /**
     * This is the method for updating the RemainingImages Property. This method has to be called when the number of
     * remaining images has been changed. (i.e. after a image has been processed by the Task)
     *
     * @param remainingImages The number of remaining images which have to be processed by the task.
     */
    protected final void updateRemainingImages(int remainingImages) {
        //This can only be updated inside the FxApplicationThread
        if(isFxApplicationThread()) {
            remainingImagesProperty.setValue(remainingImages);
        } else if(atomicReferenceRemainingImages.getAndSet(remainingImages) == null) {
            //This can only be updated inside the FxApplicationThread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    int remainingImages = atomicReferenceRemainingImages.getAndSet(null);
                    ImageTask.this.remainingImagesProperty.setValue(remainingImages);
                }
            });
        }
    }

    /**
     * This is the method for updating the RemainingTimes Property. This method has to be called at the end of each
     * processed image after the updateRemainingImages(remainingImages) has been called. This will compute the estimated
     * runtime of the task.
     *
     * @see ImageTask#updateRemainingImages(int)
     */
    protected void updateRemainingTime() {
        int timeDiff = (int) (System.currentTimeMillis() - lastTime);
        average = (average * updateCounter + timeDiff) / (updateCounter + 1);

        double timeMillis = remainingImagesProperty.get() * average;
        int seconds = (int) Math.floor(timeMillis/1000);

        String message = String.format(RES.getString("progress.secondsRemaining"), seconds);

        //This can only be updated inside the FxApplicationThread
        if(isFxApplicationThread()) {
            remainingTimeProperty.setValue(message);
        } else if(atomicReferenceRemainingTime.getAndSet(message) == null) {
            //This can only be updated inside the FxApplicationThread
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

    /**
     * @see Task#updateProgress(double, double)
     *
     * This method has been overwritten so that the progress label will be updated with the same method
     */
    @Override
    protected void updateProgress(double workDone, double max) {
        super.updateProgress(workDone, max);

        updateProgressLabel(workDone/max);
    }

    /**
     * This is the method for updating the ProgressLabel Property. This method will be called each time you call the
     * updateProgress(double, double) method from the super class.
     *
     * @param progress The current progress of the task.
     */
    private void updateProgressLabel(double progress) {
        double percentage = progress * 100;

        String message = String.format(PROGRESS_LABEL_FORMAT, percentage);

        //This can only be updated inside the FxApplicationThread
        if(isFxApplicationThread()) {
            progressLabelProperty.setValue(message);
        } else if(atomicReferenceProgressLabel.getAndSet(message) == null) {
            //This can only be updated inside the FxApplicationThread
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String message = atomicReferenceProgressLabel.getAndSet(null);
                    ImageTask.this.progressLabelProperty.setValue(message);
                }
            });
        }
    }

    //Checks if current Thread is an JavaFx Application Thread
    private boolean isFxApplicationThread() {
        return Platform.isFxApplicationThread();
    }
}
