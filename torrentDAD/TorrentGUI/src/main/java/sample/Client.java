package sample;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

import io.socket.client.IO;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Client extends Thread {
    private FileChannel toFile;
    private RandomAccessFile newFile;
    private ClientThread array[];
    private Integer fileSize;
    private String fileName;
    private Integer numOfParts;
    private ArrayList<String> availableSeeders;
    private JSONArray arrIp;
    private JSONArray heshJSON;

    public enum possibleStates {nije, uToku, gotov};
    public possibleStates stateOfParts[];

    public String getFileName() {
        return fileName;
    }

    //////////////////////////////////////

    private synchronized boolean sviGotovi() {
        for(int i=0;i<numOfParts;i++) {
            if(stateOfParts[i] != possibleStates.gotov)
                return false;
        }
        return true;
    }

    private synchronized boolean nemaNije() {

        for(int i=0;i<numOfParts;i++) {
            if(stateOfParts[i] == possibleStates.nije) {
                return false;
            }
        }
        return true;
    }


    /////////////////////////////////////

    public Client(String fileName) {
        arrIp = new JSONArray();
        heshJSON = new JSONArray();
        fileSize = new Integer(0);
        availableSeeders = new ArrayList<>();
        this.fileName = fileName;
        numOfParts = new Integer(0);
        try {
            getSeeders();
            createFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void createFile() throws IOException {
        newFile = new RandomAccessFile("/home/stefan/Desktop/"+fileName, "rw");
        newFile.setLength(fileSize);
        toFile = newFile.getChannel();
    }

    private void getSeeders() throws IOException, ClassNotFoundException {

        final io.socket.client.Socket socket1;
        try {
            socket1 = IO.socket("http://192.168.0.124:5000");

            socket1.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {

                public void call(Object... objects) {
                    socket1.emit("searchingFileTorrents", fileName);
                }
            }).on("torrentsReceive", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject Object = (JSONObject) args[0];

                    try {
                        arrIp = (JSONArray) Object.getJSONArray("listIp");
                        heshJSON = (JSONArray) Object.getJSONArray("hashList");
                        fileSize = (Integer) Object.getInt("size");
                        numOfParts = (Integer) Object.getInt("numOfParts");
                        setStates();
                        copyIps(arrIp);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(arrIp);
                    socket1.disconnect();
                }
            }).on(io.socket.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("Socket1 disconnected");
                }

            });

            socket1.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public synchronized void removeCorruptedOrDiskonnectedSeeder(String IP) {
        availableSeeders.remove(IP);
    }

    private void setStates() {
        stateOfParts = new possibleStates[numOfParts];
        array = new ClientThread[numOfParts];
        for(int i=0;i<numOfParts;i++)
            stateOfParts[i] = possibleStates.nije;
    }

    private void copyIps(JSONArray arrIp){
        for(int i=0;i<arrIp.length();i++) {
            try {
                availableSeeders.add(arrIp.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        start();
    }


    @Override
    public void run() {
        Random random =  new Random();
        int k = 0;
        String tmpIp = null;
        while (!sviGotovi()) {

            System.out.println("Iteracija broj : " +  k++);

            try {

                while (nemaNije() && imaUToku()) {
                    sleep(200); //Dajem vecu sansu ostaloj braci da zamene niz
                }
                if(sviGotovi()) {
                    break;
                }

                tmpIp = (String)availableSeeders.get(random.nextInt(availableSeeders.size()));


                Socket socket = new Socket(tmpIp, 9090);

                ClientThread clientThread = null;
                if(getFirst() != -1)
                    clientThread = new ClientThread(socket, getFirst(), this, tmpIp);


                //TODO za skidanje/dodavanje na listi trackera
            } catch (IOException e) {
                e.printStackTrace();
                removeCorruptedOrDiskonnectedSeeder(tmpIp);
                System.out.println(availableSeeders);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            fillFile();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillFile() throws InterruptedException, IOException {
        for (int i = 0; i < numOfParts; i++) {
            array[i].join();

        }

        for (int i = 0; i < numOfParts; i++) {
            byte[] bufferToWrite = Arrays.copyOf(array[i].getLocalBuffer(), array[i].getLocalBuffer().length);
            toFile.write(ByteBuffer.wrap(bufferToWrite));
        }


        notifyTracker();
    }

    private void notifyTracker() {
        final io.socket.client.Socket socket1;
        try {
            socket1 = IO.socket("http://192.168.0.124:5000");
            socket1.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {

                public void call(Object... objects) {
                    socket1.emit("addIp", fileName);
                    socket1.disconnect();
                }

            });

            socket1.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public synchronized void setClientThread(int index, ClientThread k) {
        array[index] = k;
    }

    private synchronized int getFirst() {
        for(int i=0;i<numOfParts;i++) {
            if(stateOfParts[i] == possibleStates.nije)
                return  i;
        }

        return -1;
    }

    private synchronized boolean imaUToku() {
        for(int i=0;i<numOfParts;i++) {
            if(stateOfParts[i] == possibleStates.uToku)
                return true;
        }
        return false;
    }

    public synchronized  String getHesh(int index) {
        try {
            return (String)heshJSON.get(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
