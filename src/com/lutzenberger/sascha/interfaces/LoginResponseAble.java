package com.lutzenberger.sascha.interfaces;

import javafx.beans.property.ReadOnlyObjectProperty;

import com.lutzenberger.sascha.abload.data.LoginResponse;

/**
 * This is an interface which has to be implemented by every controller that wants to work with the LoginResponse of the
 * LoginTask.
 *
 * @author Sascha Lutznberger
 * @version 1.0 - 30.12.2015
 */
public interface LoginResponseAble {
    /**
     * This method binds the property of the LoginTask to any controller which needs to work with the LoginResponse
     *
     * @param loginResponseProperty The LoginResponse Property which can be bound to any corresponding ObjectProperty
     */
    void bindLoginResponse(ReadOnlyObjectProperty<LoginResponse> loginResponseProperty);
}
