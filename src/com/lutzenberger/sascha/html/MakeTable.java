/*
 * Copyright (c) 2016. Sascha Lutzenberger. All rights reserved.
 *
 * This file is part of the project "Abload_Tool"
 *
 * Redistribution and use in source and binary forms, without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - The author of this source code has given you the permission to use this
 *   source code.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - The code is not used in commercial projects, except you got the permission
 *   for using the code in any commerical projects from the author.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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

