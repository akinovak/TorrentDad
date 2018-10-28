package sample;

public class TorrentData {

    private String name;
    private double downSpeed,upSpeed;
    private double progress;
    private double size;

    public TorrentData(String name, double downSpeed, double upSpeed, double progress, double size) {
        this.name = name;
        this.downSpeed = downSpeed;
        this.upSpeed = upSpeed;
        this.progress = progress;
        this.size = size;
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

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
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
}
