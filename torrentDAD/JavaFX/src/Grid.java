import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Grid extends Application {

   Stage window;
   Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Grid Pane");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(8);
        grid.setHgap(10);

        //Name Label
        Label nameLabel = new Label("Username:");
        GridPane.setConstraints(nameLabel,0,0);

        //Name input
        TextField nameInput = new TextField();
        GridPane.setConstraints(nameInput,1,0);

        //Password Label
        Label passLabel = new Label("Password:");
        GridPane.setConstraints(passLabel,2,0);

        //Password input
        TextField passInput = new TextField();
        passInput.setPromptText("Password");
        GridPane.setConstraints(passInput,3,0);

        Button loginButton = new Button("Log in");
        GridPane.setConstraints(loginButton,4,0);

        grid.getChildren().addAll(nameLabel,nameInput,passLabel,passInput,loginButton);
        scene = new Scene(grid,600,500);
        window.setScene(scene);
        window.show();
    }
}
