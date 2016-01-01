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
 * Created by saschalutzenberger on 28/12/15.
 */
public class CreateLinksController implements Initializable {
    @FXML
    private MenuItem menuItemShowPreview;

    @FXML
    private ChoiceBox<ImageFormat> choiceBoxFormat;

    @FXML
    private TextArea textAreaLinks;

    private List<ImageSrc> imageSrcList;

    private AbloadLinks abloadLinks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBoxFormat.getItems().addAll(ImageFormat.values());
        choiceBoxFormat.getSelectionModel().selectFirst();
    }

    public void addImageSrcList(List<ImageSrc> imageSrcList) {
        this.imageSrcList = imageSrcList;
    }

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

    @FXML
    private void menuCloseApplicationClicked(ActionEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Platform.exit();
            }
        });
    }

    @FXML
    private void menuSelectAllClicked(ActionEvent event) {
        textAreaLinks.selectAll();
    }

    @FXML
    private void menuCutClicked(ActionEvent event) {
        textAreaLinks.cut();
    }

    @FXML
    private void menuCopyClicked(ActionEvent event) {
        textAreaLinks.copy();
    }

    @FXML
    private void menuPasteClicked(ActionEvent event) {
        textAreaLinks.paste();
    }

    @FXML
    private void menuShowPreviewInTableClicked(ActionEvent event) {
        HTMLPreview htmlPreview = HTMLPreview.getPreview();
        htmlPreview.createPreview(textAreaLinks.getText());
    }

    @FXML
    private void actionEditLinksClicked(ActionEvent event) {
        if(imageSrcList.isEmpty()) {
            if(abloadLinks == null) {
                String links = textAreaLinks.getText();
                abloadLinks = new AbloadLinks(links);
                imageSrcList.addAll(abloadLinks.getImageSrcList());
            }
        }

        ImageFormat imageFormat = choiceBoxFormat.getValue();
        MakeTable table = new MakeTable(imageFormat, imageSrcList);

        String result = table.getTable();
        textAreaLinks.setText(result);
        menuItemShowPreview.setDisable(false);
    }
}
