package com.lutzenberger.sascha.abload.data;

import java.io.Serializable;

/**
 * Created by saschalutzenberger on 29/12/15.
 */
public class Gallery implements Serializable {
    private String name;
    private long id;

    public Gallery(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    private String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
