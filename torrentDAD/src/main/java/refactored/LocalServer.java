package refactored;

import old.ClientHandlerThread;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalServer extends Thread implements Serializable {
    private String Name;
    private String IP;
    private ServerSocket serverSocket;
    private ArrayList<TorrentFile> listOfDownloadableTorrents;
    private HashMap<String, TorrentFile> downloadableTorrents;

    // TODO uraditi metodu za menjanje stanja seedera (obavesti i trackera)

    public LocalServer(String Name, int port) {
        listOfDownloadableTorrents = new ArrayList<>();
        downloadableTorrents = new HashMap<>();
        this.Name = Name;

        try {
            InetAddress adresa = InetAddress.getByName(Name);
            IP = adresa.getHostAddress();
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Port " + port + " is already in use");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void addToHashMap(TorrentFile torrentFile) {
        downloadableTorrents.put(torrentFile.getFileName(), torrentFile);
    }
    //TODO izbacivanje iz hashMape ako brisem fajl sa kompa

    public synchronized TorrentFile getTorrentFileByName(String name) {
        return downloadableTorrents.get(name);
    }

    public synchronized ArrayList<TorrentFile> getListOfDownTorrents() {
        return listOfDownloadableTorrents;
    }

    public String getIP() {
        return IP;
    }

    @Override
    public void run() {

        boolean radi = true; // stavi na false ako neces vise da budes seeder
        while (radi) {

            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[In LocalServer] accepted connection...");
                ClientHandlerThread clientThread = new ClientHandlerThread(clientSocket, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getname() {
        return Name;
    }
}

