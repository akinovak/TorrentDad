package refactored;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class ClientThread extends Thread {
    private Socket socket;
    private byte[] localBuffer;
    private int part;
    private Client myClient;

    public ClientThread(Socket socket, int part, Client client) {
        this.socket = socket;
        this.myClient = client;
        this.part = part;

        myClient.setCollectedParts(part, true);
        myClient.setClientThread(part, this);

        start();
    }

    @Override
    public void run() {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            sendInfo(printWriter);

            BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());

            read(reader);

        } catch (IOException e) {
            e.printStackTrace();
            myClient.setClientThread(part, null);
            myClient.setCollectedParts(part, false);
        }
    }

    private void read(BufferedInputStream reader) throws IOException {
        localBuffer = new byte[20000];
        byte buffer[] = new byte[1024];

        int bytesRead, downloaded = 0;

        while ( (bytesRead = reader.read(buffer, 0, buffer.length)) != -1) {
            System.arraycopy(buffer, 0, localBuffer, downloaded, bytesRead);
            downloaded += bytesRead;
        }

        localBuffer = Arrays.copyOf(localBuffer, downloaded);
        myClient.setCollectedParts(part, true);
        myClient.setClientThread(part, this);
        socket.close();
    }

    private void sendInfo(PrintWriter printWriter) throws IOException {
        printWriter.println(myClient.getFileName());
        printWriter.println(part);
    }

    public byte[] getLocalBuffer() {
        return localBuffer;
    }
}
