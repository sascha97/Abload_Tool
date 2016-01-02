package com.lutzenberger.sascha.interfaces;

import javafx.beans.property.ReadOnlyStringProperty;

/**
 * This is an interface which has to be implemented by every controller that wants to be able to display a LoginStatus
 * in its User Interface
 *
 * @author Sascha Lutznberger
 * @version 1.0 - 29.12.2015
 */
public interface LoginLabel {
    /**
     * This method binds the loginStatus to an User Interface. This is done via the data binding.
     *
     * @param loginStatusProperty The loginStatus which should be bound to an User Interface
     */
    void bindLoginText(ReadOnlyStringProperty loginStatusProperty);
}
