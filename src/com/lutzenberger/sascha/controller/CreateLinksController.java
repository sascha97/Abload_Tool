package com.lutzenberger.sascha.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.lutzenberger.sascha.html.AbloadLinks;
import com.lutzenberger.sascha.html.ImageFormat;
import com.lutzenberger.sascha.html.ImageSrc;
import com.lutzenberger.sascha.html.MakeTable;
import com.lutzenberger.sascha.view.HTMLPreview;

/**
 * This is the Controller for the Screen CreateLinksScreen.fxml
 *
 * This controller handles the user inputs and the interactions with the menu and executes actions when a button is
 * clicked.
 *
 * You can add a Link List from http://www.abload.de to create a HTML Table or you just use the complete program
 * to create such a table.
 *
 * This program is only capable of taking care of images from http://www.abload.de.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 28.12.2015
 */
public class CreateLinksController implements Initializable {
    /**
     * The MenuItem showPreview which has to be disabled as long as there is nothing to display in the preview screeen.
     */
    @FXML
    private MenuItem menuItemShowPreview;

    /**
     * This is a ChoiceBox containing all ImageFormats supported by this application.
     */
    @FXML
    private ChoiceBox<ImageFormat> choiceBoxFormat;

    /**
     * The TextArea in which the links can be entered directly. Afterwards this will only display the html text of the
     * table
     */
    @FXML
    private TextArea textAreaLinks;

    /**
     * This is a ImageSrc list. This list is used to build the HTML image table.
     */
    private List<ImageSrc> imageSrcList;

    //This is responsible for parsing the list from http://www.abload.de
    private AbloadLinks abloadLinks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Add all supported image formats to the choice box
        choiceBoxFormat.getItems().addAll(ImageFormat.values());
        choiceBoxFormat.getSelectionModel().selectFirst();
    }

    /**
     * This method is used to set the imageSrcList for the controller. This method has to be called by every single
     * class which wants to add an ImageSrc list to this controller. (i.e. the main class is going to call this method)
     *
     * @param imageSrcList The imageSrcList which should be used to create the HTML table
     */
    public void addImageSrcList(List<ImageSrc> imageSrcList) {
        this.imageSrcList = imageSrcList;
    }

    /**
     * This method is used to handle the action if the user clicks on the menu item "New table...".
     *
     * This resets the Controller to its default state, in which it is only possible to create a new HTMLTable by
     * inserting a list of Images you can get from http://www.abload.de
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void menuCreateNewTableClicked(ActionEvent event) {
        //Clear the ImageSrcList
        imageSrcList.clear();
        //set the text to nothing
        textAreaLinks.clear();
        //set abloadLinks to nothing
        abloadLinks = null;
        //disable the preview menu item
        menuItemShowPreview.setDisable(true);
    }

    /**
     * This method is used to handle the action if the user clicks on the menu item "Close Application".
     *
     * This closes the JavaFx Application
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void menuCloseApplicationClicked(ActionEvent event) {
        //Has to be done in the JavaFx Application Thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Platform.exit();
            }
        });
    }

    /**
     * This method is used to handle the action if the user clicks on the menu item "Select All".
     *
     * This should select the complete text which is displayed in the TextArea.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void menuSelectAllClicked(ActionEvent event) {
        textAreaLinks.selectAll();
    }

    /**
     * This method is used to handle the action if the user clicks on the menu item "Cut".
     *
     * This should Cut the selected text from the TextArea.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void menuCutClicked(ActionEvent event) {
        textAreaLinks.cut();
    }

    /**
     * This method is used to handle the action if the user clicks on the menu item "Copy".
     *
     * This should Copy the selected text from the TextArea.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void menuCopyClicked(ActionEvent event) {
        textAreaLinks.copy();
    }

    /**
     * This method is used to handle the action if the user clicks on the menu item "Paste".
     *
     * This should Paste the current value of the clipboard into the TextArea.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void menuPasteClicked(ActionEvent event) {
        textAreaLinks.paste();
    }

    /**
     * This method is used to handle the action if the user clicks on the menu item "Preview (created table)".
     *
     * This should open the Preview in the Default WebBrowser of the client system.
     * This creates a temporarily file which is stored in the "tmp" directory of the client.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void menuShowPreviewInTableClicked(ActionEvent event) {
        //Get an instance of HTMLPreview
        HTMLPreview htmlPreview = HTMLPreview.getPreview();
        //Try to create a HTMLPreview of the actual content of the TextArea
        htmlPreview.createPreview(textAreaLinks.getText());
    }

    /**
     * This method is used to handle the action if the user clicks on the menu item "Edit List".
     *
     * This should try to generate a new HTML code for the image table.
     *
     * @param event The event from the JavaFx Application Thread
     */
    @FXML
    private void actionEditLinksClicked(ActionEvent event) {
        //If the ImageSrc List is empty use the Text from the table to generate the image table.
        if(imageSrcList.isEmpty()) {
            if(abloadLinks == null) {
                //Get the Lins from the Text Area
                String links = textAreaLinks.getText();
                //Try to parse them to an ImageSrc List
                abloadLinks = new AbloadLinks(links);
                imageSrcList.addAll(abloadLinks.getImageSrcList());
            }
        }

        //Get the ImageFormat in which the table should be formatted.
        ImageFormat imageFormat = choiceBoxFormat.getValue();
        //Generate the new HTML table using the imageSrcList and the ImageFormat
        MakeTable table = new MakeTable(imageFormat, imageSrcList);

        //Get the HTML code of the table and set the result to the TextArea
        String result = table.getTable();
        textAreaLinks.setText(result);
        //Enable the menu item "Preview (created table)" because preview can now be created.
        menuItemShowPreview.setDisable(false);
    }
}
