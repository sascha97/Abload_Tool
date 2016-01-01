package com.lutzenberger.sascha.interfaces;

import javafx.beans.property.ReadOnlyStringProperty;

/**
 * Created by saschalutzenberger on 29/12/15.
 */
public interface LoginLabel {
    void bindLoginText(ReadOnlyStringProperty loginStatusProperty);
}
