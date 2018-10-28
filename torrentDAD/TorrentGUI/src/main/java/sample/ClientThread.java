package sample;

import io.socket.client.IO;
import io.socket.emitter.Emitter;
import javafx.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ClientThread extends Thread {
    private Socket socket;
    private byte[] localBuffer;
    private int part;
    private Client myClient;
    private String seederIp;

    public ClientThread(Socket socket, int part, Client client, String tmpIp) {
        this.socket = socket;
        this.myClient = client;
        this.part = part;
        seederIp = tmpIp;

        synchronized (myClient) {
            myClient.stateOfParts[part] = Client.possibleStates.uToku;
        }

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
            //e.printStackTrace();
            System.out.println("Upao sam u ovaj exception jer Dule sisa");
            myClient.setClientThread(part, null);
            myClient.stateOfParts[part] = Client.possibleStates.nije;
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

        String md5Hash = getHash();

        if(md5Hash.compareTo(myClient.getHesh(part)) == 0) {
            myClient.setClientThread(part, this);
            myClient.stateOfParts[part] = Client.possibleStates.gotov;
        }
        else
        {
            System.out.println("Ovaj hesh je los");
            myClient.setClientThread(part, null);
            myClient.stateOfParts[part] = Client.possibleStates.nije;
            emitTracker(seederIp);
            myClient.removeCorruptedOrDiskonnectedSeeder(seederIp);

        }

        //TODO
        //POSALJI TRACKERU SOKET I VIDI JE L DOBAR HES


        socket.close();
    }

    private synchronized void emitTracker(String tmpIp) {

        final io.socket.client.Socket socket1;
        try {
            final JSONObject forDeletion = new JSONObject();

            try {
                forDeletion.put("seederIp", seederIp);
                forDeletion.put("fName", myClient.getFileName());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            socket1 = IO.socket("http://192.168.0.124:5000");
            socket1.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {

                public void call(Object... objects) {
                    socket1.emit("deleteIp", forDeletion);
                    socket1.disconnect();
                }

            });

            socket1.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void sendInfo(PrintWriter printWriter) throws IOException {
        printWriter.println(myClient.getFileName());
        printWriter.println(part);
    }

    public byte[] getLocalBuffer() {
        return localBuffer;
    }

    private String getHash() {
        char[] HEX = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5Digest = messageDigest.digest(localBuffer);
             HEX = getHexValue(md5Digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return String.valueOf(HEX);
    }

    private static char[] getHexValue(byte[] array){
        char[] symbols="0123456789ABCDEF".toCharArray();
        char[] hexValue = new char[array.length * 2];

        for(int i=0;i<array.length;i++)
        {
            int current = array[i] & 0xff;
            hexValue[i*2+1] = symbols[current & 0x0f];
            hexValue[i*2] = symbols[current >> 4];
        }
        return hexValue;
    }
}
