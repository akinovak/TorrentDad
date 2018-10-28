package refactored;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Client extends Thread {
    private FileChannel toFile;
    private RandomAccessFile newFile;
    private boolean collectedParts[] = new boolean[5];
    private ClientThread array[] = new ClientThread[5];
    private ArrayList<String> availableSeeders;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public Client(String fileName) {
        this.fileName = fileName;

        try {
            getSeeders();
            createFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        start();
    }

    private void createFile() throws IOException {
        newFile = new RandomAccessFile("/home/andrija/Desktop/"+fileName, "rw");
        newFile.setLength(92776);
        toFile = newFile.getChannel();
    }

    private void getSeeders() throws IOException, ClassNotFoundException {
        Socket socket = new Socket("192.168.1.4", 8818);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

        printWriter.println(fileName);

        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

        Object o = inputStream.readObject();
        availableSeeders = (ArrayList) o;

    }

    @Override
    public void run() {
        Random random =  new Random();
        while (getNumOfDownloaded() < 5) {

            String tmpIp = availableSeeders.get(random.nextInt(availableSeeders.size()));

            try {
                Socket socket = new Socket(tmpIp, 6881);

                ClientThread clientThread = new ClientThread(socket, getFirsAvailable(), this);

                fillFile();

                //TODO za skidanje/dodavanje na listi trackera
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    private void fillFile() throws InterruptedException, IOException {
        for (int i = 0; i < 5; i++) {
            array[i].join();

        }

        for (int i = 0; i < 5; i++) {
            byte[] bufferToWrite = Arrays.copyOf(array[i].getLocalBuffer(), array[i].getLocalBuffer().length);
            toFile.write(ByteBuffer.wrap(bufferToWrite));
        }
    }

    public synchronized void setCollectedParts(int index, boolean value) {
        collectedParts[index] = value;
    }

    public synchronized void setClientThread(int index, ClientThread k) {
        array[index] = k;
    }

    private synchronized int getNumOfDownloaded() {
        int num = 0;
        for (int i = 0; i < 5; i++) {
            if (collectedParts[i])
                num++;
        }

        return num;
    }

    private synchronized int getFirsAvailable() {
        for (int i = 0; i < collectedParts.length; i++) {
            if (!collectedParts[i])
                return i;
        }

        return -1;
    }
}
