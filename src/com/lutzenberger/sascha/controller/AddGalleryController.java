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
 * This is the Controller for the Screen AddGalleryScreen.fxml
 *
 * This controller handles the inputs from the user and executes the right parts of the code when a button is clicked.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 31.12.2015
 */
public class AddGalleryController implements Initializable, LoginLabel, LoginResponseAble {
    //The ResourceBundle that contains all the application strings
    private ResourceBundle res;

    /**
     * The button which is adding the Gallery to http://www.abload.de if a valid Gallery Name has been entered
     */
    @FXML
    private Button buttonCreateGallery;

    /**
     * The TextField for the name of the new gallery.
     */
    @FXML
    private TextField textFieldGalleryName;

    /**
     * The Text for displaying the current login Status
     */
    @FXML
    private Text textLoginStatus;

    /**
     * An ObjectProperty holding the LoginResponse object
     */
    private ObjectProperty<LoginResponse> loginResponse = new SimpleObjectProperty<>();

    /**
     * A Boolean property containing a flag when the dialog is finished
     */
    private BooleanProperty finished = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Make sure the text which is entered in the gallery field is not empty and does not contain any " "
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

        //The button create gallery should only be enabled if the text field is not empty
        buttonCreateGallery.disableProperty().bind(textFieldGalleryName.textProperty().isEmpty());
    }

    /**
     * Get the Finished Property as ReadOnlyProperty to be able to use data binding.
     *
     * @return The Finished Property which can be easily bound to another property.
     */
    public ReadOnlyBooleanProperty finishedProperty() {
        return finished;
    }

    /**
     * This method binds the loginStatus to the Text Login Status
     *
     * @param loginStatusProperty The loginStatus which should be bound to the Login Status Text
     */
    @Override
    public void bindLoginText(ReadOnlyStringProperty loginStatusProperty) {
        textLoginStatus.textProperty().bind(loginStatusProperty);
    }

    /**
     * This method binds the property of the LoginTask to this Controller so that it is possible to work with the
     * LoginResponse which will be needed for adding a new Gallery.
     *
     * @param loginResponseProperty The LoginResponse Property which is needed for creating a new Gallery
     */
    @Override
    public void bindLoginResponse(ReadOnlyObjectProperty<LoginResponse> loginResponseProperty) {
        loginResponse.bind(loginResponseProperty);
    }

    /**
     * This method is used to handle the action which should take place when the Cancel Button  is clicked.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonCancelClicked(ActionEvent event) {
        //unbind the components then the window can be closed.
        unbind();
        finished.setValue(true);
    }

    /**
     * This method is used to handle the action which should take place when the Button CreateGallery is clicked.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void buttonCreateGalleryClicked(ActionEvent event) {
        //Get the name of the gallery and trim the value.
        String galleryName = textFieldGalleryName.textProperty().getValue().trim();

        //Create a new Gallery manger which is responsible for creating the new gallery.
        GalleryManager galleryManager = new GalleryManager(loginResponse.getValue());
        //The result of the operation
        boolean result = galleryManager.createNewGallery(galleryName);
        //TODO: do something with the result

        //unbind the components and then the window can be closed.
        unbind();
        finished.setValue(true);
    }

    //unbinding the properties again so that no unnecessary reference is hold.
    private void unbind() {
        textLoginStatus.textProperty().unbind();
        loginResponse.unbind();
    }
}
