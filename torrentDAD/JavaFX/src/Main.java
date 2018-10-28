import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import org.w3c.dom.events.Event;

import java.awt.*;

public class Main extends Application{

    Button button;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Title of the window");
        button = new Button();
        button.setText("Click me");
        button.setOnAction(e -> {
            System.out.println("hello");
            System.out.println("world!");
        });

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        Scene scene = new Scene(layout,300,250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
