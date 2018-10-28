package refactored;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class Tracker extends Thread {
    private ArrayList<TorrentFile> listOfTorrentFiles;
    private HashMap<String, TorrentFile> torrentFileHashMap;
    private ServerSocket socket;

    public Tracker() {
        listOfTorrentFiles = new ArrayList<>();
        torrentFileHashMap = new HashMap<>();
        try {
            socket = new ServerSocket(8818);
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        while (true) {

            try {
                Socket clientSocket = socket.accept(); // javio mi se klijent

                BufferedInputStream reader = new BufferedInputStream(clientSocket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(reader));

                System.out.println("[In Tracker] Client accepted...");


                String torrentName = bufferedReader.readLine();

                System.out.println("[In Tracker] Client wants: " + torrentName);
                // znam koji torrent hoce klijent da skida

                try {
//                sendArrayListSerialized(clientSocket, torrentName);
                    hardCode(clientSocket, torrentName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//            clientSocket.close();

                synchronized (this) {
                    Socket seederState = socket.accept();
                    InetAddress address = seederState.getInetAddress();
                    String IP = address.getHostAddress();

                    System.out.println("[Tracker] torr="+torrentName+" IP="+IP);

                    this.getSeedersForTorrent(torrentName).add(IP);
                    printAllSeeders(torrentName);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void hardCode(Socket socket, String torrent) throws IOException {
        ArrayList<String> names = new ArrayList<>(2);
        names.add("192.168.1.4");
        names.add("192.168.0.124");

        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(names);
    }

    private synchronized void printAllSeeders(String name) {
        StringBuilder out = new StringBuilder("Seeders for torrent "+name+"\n");

        for (String s: this.getSeedersForTorrent(name)) {
            out.append(s+"\n");
        }

        System.out.println(out);
    }

//    private synchronized ArrayList<String> getLocalServersIPs(String torrentName) {
//        ArrayList<String> names = new ArrayList<>();
//        for (String s: getSeedersForTorrent(torrentName)) {
//            names.add(s);
//        }
//
//        return names;
//    }

    private synchronized ArrayList<String> getSeedersForTorrent(String fileName) {
        return torrentFileHashMap.get(fileName).getSeeders();
    }

    public synchronized void addTorrent(TorrentFile torrentFile) {
        listOfTorrentFiles.add(torrentFile);
        torrentFileHashMap.put(torrentFile.getFileName(), torrentFile);
    }

    public synchronized void removeTorrent(TorrentFile torrentFile) {
        listOfTorrentFiles.remove(torrentFile);
    }
}
