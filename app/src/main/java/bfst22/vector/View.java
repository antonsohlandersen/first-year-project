package bfst22.vector;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class View {
    public View(Model model, Stage primaryStage) throws IOException {
        primaryStage.show();
        var loader = new FXMLLoader(View.class.getResource("View.fxml"));
        primaryStage.setScene(loader.load());
        primaryStage.getScene().getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        Controller controller = loader.getController();
        primaryStage.setTitle("Map");
        primaryStage.setMaximized(true);
        controller.init(model, primaryStage.getWidth() + 400, primaryStage.getHeight() + 35, primaryStage.getScene());
    }
}