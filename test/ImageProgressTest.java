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
import javafx.stage.Stage;

import com.lutzenberger.sascha.concurrency.ImageTask;
import com.lutzenberger.sascha.view.ImageProgress;

/**
 * Created by saschalutzenberger on 28/12/15.
 */
public class ImageProgressTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        ImageTask<Void> imageTask = new ImageTask<Void>(null) {
            @Override
            protected Void call() throws Exception {
                int iterations = 333;

                updateRemainingImages(iterations);
                for(int i=0;i<iterations;i++) {
                    if(isCancelled()) {
                        break;
                    }

                    updateCurrentImage("IMG_" + i);

                    Thread.sleep(69);

                    updateRemainingImages(iterations - (i+1));
                    updateRemainingTime();
                    updateProgress(i+1, iterations);
                }

                return null;
            }
        };

        ImageProgress imageProgress = new ImageProgress(imageTask, "Test");
        Thread thread = new Thread(imageTask);
        thread.start();
    }

    public static void main(String[] args) {
        Application.launch(ImageProgressTest.class, args);
    }
}
