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
