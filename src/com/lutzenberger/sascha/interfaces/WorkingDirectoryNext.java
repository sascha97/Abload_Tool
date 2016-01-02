package com.lutzenberger.sascha.interfaces;

/**
 * This is the interface which has to be implemented whenever an action should be performed when the WorkingDirectory
 * has been selected.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 29.12.2015
 */
public interface WorkingDirectoryNext {
    /**
     * This is the method which is specifying what should be done after the WorkingDirectory has been selected.
     */
    void doNext();
}
