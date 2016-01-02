package com.lutzenberger.sascha.interfaces;

/**
 * This is an interface which has to be implemented by all controllers which want to be able to deal with the
 * application parts of the application.
 *
 * With this implementation it is possible to use all Methods declared in
 * com.lutzenberger.sascha.interfaces.LoadApplicationParts
 *
 * This enables the controller to be able to switch the screen and get the working directory property of the
 * application.
 *
 * @see LoadApplicationParts
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 02.01.2016
 */

public interface ApplicationPartAble {
    /**
     * This method is used to initialize the controller with the ApplicationParts that are necessary for the entire
     * application. This enables the implementing class to be able to change the screens and get the working
     * directory property
     *
     * @param loadApplicationParts The application parts which are necessary for the program to run.
     */
    void setLoadApplicationParts(LoadApplicationParts loadApplicationParts);
}
