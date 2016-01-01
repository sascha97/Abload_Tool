package com.lutzenberger.sascha.abload.data;

import java.io.Serializable;

/**
 * Created by saschalutzenberger on 29/12/15.
 */
public class ImageUploaded implements Serializable {
    private String newName;
    private String oldName;
    private String res;
    private String type;

    public ImageUploaded(String newName, String oldName, String res, String type) {
        this.newName = newName;
        this.oldName = oldName;
        this.res = res;
        this.type = type;
    }

    public String getFileName() {
        return this.newName;
    }
}
