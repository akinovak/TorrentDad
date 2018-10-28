package old;

import refactored.LocalServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

public class Klijent extends Thread {

    protected Fajl F;
    private FileChannel toFile;
    private RandomAccessFile novi;
    protected boolean delovi[];
    private KlijentTred niz[];
    private ArrayList<LocalServer> availableSeeders;

    public Klijent(String name)  {

        try {
            Socket socket = new Socket("192.168.1.38", 8818); // adresa trekera, sa njim hocu komunikaciju
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("andra.jpg");

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            try {
                availableSeeders = (ArrayList<LocalServer>) inputStream.readObject();
                System.out.println(printAvailableSeeders());
            } catch (ClassNotFoundException e) {
                System.out.println("Ne mozes da dobijes niz seedera jer: ");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        niz = new KlijentTred[5];
        delovi = new boolean[5];
        for(int i=0;i<5;i++)
            delovi[i] = false;
        this.F = F;
        try
        {
            novi =                      // pitaj nemanju kako se resava default directorijum
                    new RandomAccessFile("/home/andrija/Desktop/" + F.getName(), "rw");
            novi.setLength(F.getSize());
            toFile = novi.getChannel();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {


        while(getBrojPreuzetih() < 5) {
            try {

                Socket socket = new Socket("192.168.0.124", 9090);

                KlijentTred klijentTred = new KlijentTred
                        (socket, getPrviSlobodan(), this, F);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

            for(int i=0;i<5;i++) {

                try {
                    niz[i].join();
                    byte[] pom = Arrays.copyOf(niz[i].getLocalBuffer(), niz[i].getLocalBuffer().length);
                    toFile.write(ByteBuffer.wrap(pom));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    public synchronized int getBrojPreuzetih() {
        int br = 0;
        for(int i=0;i<5;i++)
        {
            if(delovi[i])
                br++;
        }

        return br;
    }

    public synchronized int getPrviSlobodan() {
        for(int i=0;i<delovi.length;i++)
            if(!delovi[i])
                return i;

        return -1;
    }

    public synchronized void setDelovi(int index, boolean value) {
        delovi[index] = value;
    }
    public synchronized void setKlijentTred(int index, KlijentTred k) {
        niz[index] = k;
    }


    public synchronized String printAvailableSeeders() {
        return availableSeeders.get(0).getname();
    }
}
