package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML private TableView<TorrentData> tableView;
    @FXML private TableColumn<TorrentData,String> nameColumn;
    @FXML private TableColumn<TorrentData,Double> downSpeedColumn;
    @FXML private TableColumn<TorrentData,Double> upSpeedColumn;
    @FXML private TableColumn<TorrentData,Double> progressColumn;
    @FXML private TableColumn<TorrentData,Double> sizeColumn;
    @FXML private TextField text;

    public void playButtonClicked(){
        System.out.println("Play button clicked");
        //TODO add funtionality
    }

    public void pauseButtonClicked(){
        System.out.println("Pause button clicked");
        //TODO add funtionality
    }

    public void addButtonClicked(){
        System.out.println("Add button clicked");
        //TODO add funtionality
    }

    public void addTorrentButtonClicked(){
        String answer = text.getText();
//        System.out.println("In controler "+answer);

        tableView.getItems().add(new TorrentData(answer,0,0,0,0));
        Client client = new Client(answer);
    }

    //This method will remove selected row(s) from the table
    public void removeButtonClicked(){
        ObservableList<TorrentData> allTorrents,selectedTorrrents;
        allTorrents = tableView.getItems();

        selectedTorrrents = tableView.getSelectionModel().getSelectedItems();
        for(TorrentData data: selectedTorrrents){
            allTorrents.remove(data);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<TorrentData,String>("name"));
        downSpeedColumn.setCellValueFactory(new PropertyValueFactory<TorrentData,Double>("downSpeed"));
        upSpeedColumn.setCellValueFactory(new PropertyValueFactory<TorrentData,Double>("upSpeed"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<TorrentData,Double>("progress"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<TorrentData,Double>("size"));


//        tableView.setItems(fillTable(name));

        //This will allow us to select multiple rows at once
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    public ObservableList<TorrentData> fillTable(String nameOfTorrent){
        ObservableList<TorrentData> data = FXCollections.observableArrayList();
        data.add(new TorrentData(nameOfTorrent,1004,104,55,75000));
//        data.add(new TorrentData("Da Vinci's Code",524,75,42,91000));
//        data.add(new TorrentData("Insception",1235,122,21,114000));

        return data;
    }

}


