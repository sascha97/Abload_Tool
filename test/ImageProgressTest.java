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
