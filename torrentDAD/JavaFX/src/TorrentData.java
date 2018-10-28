package Torrent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class TorrentData {

    private String name;
    private double downSpeed, upSpeed;
    private double size;
    private int progress;

    public TorrentData(String name, double downSpeed, double upSpeed, double size, int progress) {
        this.name = name;
        this.downSpeed = downSpeed;
        this.upSpeed = upSpeed;
        this.size = size;
        this.progress = progress;

    }

    public TorrentData() {
        this.name = "";
        this.downSpeed = 0;
        this.upSpeed = 0;
        this.size = 0;
        this.progress = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDownSpeed() {
        return downSpeed;
    }

    public void setDownSpeed(double downSpeed) {
        this.downSpeed = downSpeed;
    }

    public double getUpSpeed() {
        return upSpeed;
    }

    public void setUpSpeed(double upSpeed) {
        this.upSpeed = upSpeed;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
