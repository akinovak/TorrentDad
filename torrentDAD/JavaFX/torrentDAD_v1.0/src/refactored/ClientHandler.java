package refactored;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private TorrentFile torrentFile;
    private LocalServer myServer;

    public ClientHandler(Socket socket, LocalServer myServer) {
        this.clientSocket = socket;
        this.myServer = myServer;

        start();
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedOutputStream writer = new BufferedOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

            String torrentName = reader.readLine();
            System.out.println("[in CLIENT HANDLER] torrent name: "+torrentName);

            RandomAccessFile fileReader = new RandomAccessFile(torrentName, "r");

            torrentFile = myServer.getTorrentFileByName(torrentName);

            int part = Integer.parseInt(reader.readLine());

            System.out.println("[in CLIENT HANDLER] part: "+part);

            int offset = torrentFile.getPart(part).getKey();
            int buffLength = torrentFile.getPart(part).getValue();

            writeToFile(writer, fileReader, offset, buffLength);

            clientSocket.close();
        } catch (Exception e) { // PITAJ NEMANJU KAKO LEPSE OVO
            e.printStackTrace();
            try {
                clientSocket.close();
            }
            catch (Exception e1) {
                e.printStackTrace();
            }
        }
    }

    private void writeToFile(BufferedOutputStream writer, RandomAccessFile fileReader, int offset, int buffLength) throws IOException {
        fileReader.seek(offset);

        byte[] buffer = new byte[1024];

        int transfered = 0;

        while (transfered < buffLength) {
            int bytesRead = 0;
            bytesRead = fileReader.read(buffer, 0, buffer.length);

            if (transfered + bytesRead > buffLength)
                writer.write(buffer, 0, buffLength - transfered);
            else if (bytesRead != -1)
                writer.write(buffer, 0, bytesRead);
            else
                break;

            transfered += bytesRead;
            writer.flush();
        }
    }
}
