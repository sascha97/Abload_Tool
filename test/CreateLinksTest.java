import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

import com.lutzenberger.sascha.controller.CreateLinksController;

/**
 * Created by saschalutzenberger on 28/12/15.
 */
public class CreateLinksTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/CreateLinksScreen.fxml"));

        ResourceBundle res = ResourceBundle.getBundle("strings/Values");
        fxmlLoader.setResources(res);

        Parent root = fxmlLoader.load();
        CreateLinksController controller = fxmlLoader.getController();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(CreateLinksTest.class, args);
    }
}
