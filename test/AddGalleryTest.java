import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

import com.lutzenberger.sascha.controller.AddGalleryController;

/**
 * Created by saschalutzenberger on 31/12/15.
 */
public class AddGalleryTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/AddGalleryScreen.fxml"));

        ResourceBundle res = ResourceBundle.getBundle("strings/Values");
        fxmlLoader.setResources(res);

        Parent root = fxmlLoader.load();
        AddGalleryController controller = fxmlLoader.getController();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(AddGalleryTest.class, args);
    }
}
