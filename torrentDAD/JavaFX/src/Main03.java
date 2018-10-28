import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Main03 extends Application {

    Stage window;
    Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("New Window");
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        Button button = new Button("Close Program");
        button.setOnAction(e -> closeProgram());

        StackPane layout = new StackPane();
        layout.getChildren().add(button);
        scene = new Scene(layout,300,250);
        window.setScene(scene);
        window.show();
    }

    private void closeProgram(){
       Boolean answer = ConfirmBox.display("Title","Sure you want to exit: ");
       if(answer)
           window.close();

    }
}
