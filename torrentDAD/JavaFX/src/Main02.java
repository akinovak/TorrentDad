import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class Main02 extends Application{

    Stage window;
    Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        Button button = new Button("Click me");
        button.setOnAction(e -> {
            boolean result = ConfirmBox.display("Title of Window","Are you sure you want this: ");
            System.out.println(result);
        });

        StackPane layout = new StackPane();
        layout.getChildren().add(button);
        scene = new Scene(layout,300,250);
        window.setScene(scene);
        window.setTitle("window");
        window.show();
    }
}
