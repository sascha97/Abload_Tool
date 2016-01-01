package com.lutzenberger.sascha.interfaces;

import javafx.beans.property.ReadOnlyObjectProperty;

import com.lutzenberger.sascha.abload.data.LoginResponse;

/**
 * Created by saschalutzenberger on 30/12/15.
 */
public interface LoginResponseAble {
    void bindLoginResponse(ReadOnlyObjectProperty<LoginResponse> loginResponseProperty);
}
