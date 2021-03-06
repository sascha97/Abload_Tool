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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

import com.lutzenberger.sascha.concurrency.LoginTask;
import com.lutzenberger.sascha.controller.SelectGalleryController;


/**
 * Created by saschalutzenberger on 29/12/15.
 */
public class ApplicationTest extends Application {
    private LoginTask loginTask;

    @Override
    public void start(Stage primaryStage) throws Exception {
        loginTask = new LoginTask();
        loginTask.loginStatusProperty();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/SelectGalleryScreen.fxml"));
        loader.setResources(ResourceBundle.getBundle("strings/Values"));

        Parent root = loader.load();
        SelectGalleryController controller = loader.getController();

        controller.bindLoginText(loginTask.loginStatusProperty());

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();

        Thread thread = new Thread(loginTask);
        thread.start();
    }

    public static void main(String[] args) {
        Application.launch(ApplicationTest.class, args);
    }
}
