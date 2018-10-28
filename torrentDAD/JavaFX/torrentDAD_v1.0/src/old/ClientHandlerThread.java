package old;

import refactored.LocalServer;
import refactored.TorrentFile;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.Socket;

public class ClientHandlerThread extends Thread {

    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedOutputStream writer;
    private TorrentFile torrentFile;
    private LocalServer myServer;

    public ClientHandlerThread(Socket s, LocalServer myServer) {
        this.clientSocket = s;
        this.myServer = myServer;

        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        try {
            String torrentName = reader.readLine();
            System.out.println("[In client handler] torrent name: "+torrentName);
            RandomAccessFile fileReader = new RandomAccessFile(torrentName, "r");

            torrentFile = myServer.getTorrentFileByName(torrentName);

            int part = Integer.parseInt(reader.readLine());

            System.out.println("[In client handler] part: "+part);

            int offset = torrentFile.getPart(part).getKey();
            int buffLength = torrentFile.getPart(part).getValue();

//            System.out.println("[in client handler] offset: "+offset+" bufflength: "+buffLength);

            fileReader.seek(offset);

            byte[] buffer = new byte[8];

            int transfered = 0;
            while(transfered < buffLength) {
               int bytesRead = 0;
               bytesRead = fileReader.read(buffer, 0, buffer.length);
               if(transfered + bytesRead > buffLength)
                   writer.write(buffer, 0, buffLength - transfered);
               else if(bytesRead != -1)
                    writer.write(buffer, 0, bytesRead);
               else
                   break;

//                System.out.println("[in client handler] bytesRead="+bytesRead);
               transfered += bytesRead;
               writer.flush();
            }

//            System.out.println("[in client handler] after loop: traans="+transfered+" bufflength="+buffLength);

            clientSocket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                clientSocket.close();
            }
            catch (Exception p) {
                e.printStackTrace();
            }
        }
    }


}
