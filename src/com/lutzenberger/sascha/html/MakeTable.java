package com.lutzenberger.sascha.html;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This class is responsible for creating the whole table
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 27.12.2015
 */
public class MakeTable {
    private final ImageFormat format;
    private final ResourceBundle res = ResourceBundle.getBundle("templates/html");

    private final List<ImageSrc> imageList;
    private final List<TableRow> tableRowList;

    private boolean tableMade;

    public MakeTable(ImageFormat format, List<ImageSrc> imageList){
        this.format = format;
        this.imageList = imageList;

        tableMade = false;

        this.tableRowList = new ArrayList<>(imageList.size()/format.getNumRow() + 1);
    }

    /**
     * This method returns the table.
     *
     * @return The table with all the HTML links
     */
    public String getTable(){
        setTableRowList();

        String table = "";
        for (TableRow r : tableRowList) {
            table = table + r.getTableRow();
        }

        return String.format(res.getString("tableVorlage"),table);
    }

    private void setTableRowList(){
        if(tableMade)
            return;

        TableRow row = new TableRow(format);
        tableRowList.add(row);
        for (ImageSrc imageSrc : imageList) {
            //Add the image src to the table row
            if(!row.fillRow(imageSrc)){
                //Create a new table row because an element still has to be added
                row = new TableRow(format);
                tableRowList.add(row);
                if(!row.fillRow(imageSrc)){
                    JOptionPane.showMessageDialog(null, res.getString("logischerFehler"), res.getString("fehler"),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        tableMade = true;
    }
}

