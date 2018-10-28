import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.util.Observable;

public class TorrentMain extends Application{

    Stage window;
    Scene scene;
    TableView<TorrentData> table;
    TextField nameInput,downSpeedInput,upSpeedInput,sizeInput,progressInput;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Torrent Download VXX");

        //Name column
        TableColumn<TorrentData,String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        //Download Speed Column
        TableColumn<TorrentData,Double> downSpeed = new TableColumn<>("Down speed");
        downSpeed.setMinWidth(200);
        downSpeed.setCellValueFactory(new PropertyValueFactory<>("downSpeed"));

        //Upload Speed
        TableColumn<TorrentData,Double> uploadSpeed = new TableColumn<>("Upload Speed");
        uploadSpeed.setMinWidth(200);
        uploadSpeed.setCellValueFactory(new PropertyValueFactory<>("upSpeed"));

        //Size of a file
        TableColumn<TorrentData,Double> size = new TableColumn<>("Size");
        size.setMinWidth(200);
        size.setCellValueFactory(new PropertyValueFactory<>("size"));

        //Progress
        TableColumn<TorrentData,Double> progress = new TableColumn<>("Progress");
        progress.setMinWidth(200 );
        progress.setCellValueFactory(new PropertyValueFactory<>("progress"));

        //Name input
        nameInput = new TextField();
        nameInput.setPromptText("Name");
        nameInput.setMinWidth(100);

        //Down Speed Input
        downSpeedInput = new TextField();
        downSpeedInput.setPromptText("Download");

        //Upload Speed Input
        upSpeedInput = new TextField();
        upSpeedInput.setPromptText("Upload");

        //Size Input
        sizeInput = new TextField();
        sizeInput.setPromptText("Size");

        //Progress Input
        progressInput = new TextField();
        progressInput.setPromptText("Progress");

        //Button
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addButtonClicked());
        Button delButton = new Button("Delete");
        delButton.setOnAction(e -> delButtonClicked());

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(nameInput,downSpeedInput,upSpeedInput,sizeInput,progressInput,addButton,delButton);

        //Seeders Button
//        Button seedersButton = new Button("Seeders");
//        seedersButton.setOnAction();

        //Bottom menu




        //Top Menu (Start Pause button)
        HBox topMenu = new HBox();
        Button startButton = new Button();
        //TODO startButton.setOnAction();
        Button pauseButton = new Button();
        ImageView pauseImage = new ImageView("pause.png");
        pauseImage.setFitHeight(15);
        pauseImage.setFitWidth(30);
        pauseButton.setGraphic(pauseImage);

        //TODO pauseButton.setOnAction();
        topMenu.getChildren().addAll(startButton,pauseButton);
        ImageView startImage = new ImageView("play1.png");
        startImage.setFitHeight(15);
        startImage.setFitWidth(30);
        startButton.setGraphic(startImage);

        table = new TableView<>();
        table.setItems(showTable());
        table.getColumns().addAll(nameColumn,downSpeed,uploadSpeed,size,progress);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table,hBox);

        //Border Pane
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);
        borderPane.setCenter(vBox);

        scene = new Scene(borderPane,1100,500);
        window.setScene(scene);
        window.show();
    }

    //Show all the stuff
    public ObservableList<TorrentData> showTable(){
        ObservableList<TorrentData> data = FXCollections.observableArrayList();
        data.add(new TorrentData("Da Vinci's Code",565.5,15.3,7500000,34));
        data.add(new TorrentData("Memento",123.6,19.2,555000,56));
        data.add(new TorrentData("AC/DC - TNT",44.7,9.1,12000,99));

        return data;
    }

    public void addButtonClicked(){
        TorrentData data = new TorrentData();
        data.setName(nameInput.getText());
        data.setDownSpeed(Double.parseDouble(downSpeedInput.getText()));
        data.setUpSpeed(Double.parseDouble(upSpeedInput.getText()));
        data.setSize(Double.parseDouble(sizeInput.getText()));
        data.setProgress(Integer.parseInt(progressInput.getText()));
        table.getItems().add(data);
        nameInput.clear();
        downSpeedInput.clear();
        upSpeedInput.clear();
        sizeInput.clear();
        progressInput.clear();
    }

    public void delButtonClicked(){
        ObservableList<TorrentData> dataSelected,allData;
        allData = table.getItems();
        dataSelected = table.getSelectionModel().getSelectedItems();

        dataSelected.forEach(allData::remove);
    }

//    public void selectedItem(){
//        TorrentData dataSelected = new TorrentData();
//        dataSelected = table.getSelectionModel().getSelectedItem();
//
//        System.out.println(dataSelected.getName());
//    }
}
