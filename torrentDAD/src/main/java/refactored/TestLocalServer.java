package refactored;

public class TestLocalServer {
    public static void main(String[] args) {
        LocalServer localServer = new LocalServer("192.168.0.104", 6881);
        localServer.start();

        TorrentFile torrentFile = new TorrentFile("andra.jpg", 92776, 5);
//        torrentFile.addSeeder(localServer);
//        torrentFile.addSeeder(new LocalServer("10.0.96.81", 6881));
        localServer.addToHashMap(torrentFile);

//        Tracker tracker = new Tracker();
//        tracker.addTorrent(torrentFile);
    }
}
