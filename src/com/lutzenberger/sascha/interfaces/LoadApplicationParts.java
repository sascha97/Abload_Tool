package com.lutzenberger.sascha.interfaces;

import javafx.beans.property.ReadOnlyObjectProperty;

import com.lutzenberger.sascha.image.WorkingDirectory;

public interface LoadApplicationParts {
    void loadCreateLinksScreen();

    void loadImagePreviewScreen();

    void loadSelectGalleryScreen();

    void loadSelectWorkingDirectoryScreen(WorkingDirectoryNext doNext);

    void loadWelcomeScreen();

    ReadOnlyObjectProperty<WorkingDirectory> workingDirectoryProperty();
}
