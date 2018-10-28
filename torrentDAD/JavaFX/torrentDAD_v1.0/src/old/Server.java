package old;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server extends Thread{

    private String Name;
    private String IP;
    private ServerSocket serverSocket;
    private Fajl F;

    public Server(String Name, int port, Fajl f) {
        this.Name = Name;
        this.F = f;

        try {
            InetAddress adresa = InetAddress.getByName(Name);
            IP = adresa.getHostAddress();
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println("Port " + port + " is already in use");
        }
        catch (Exception e) {}
        start();
    }

    public String getIP() {
        return IP;
    }

    @Override
    public void run() {

       boolean radi = true;
        while(radi) {

            try {
                Socket clientSocket = serverSocket.accept();

//                ClientHandlerThread clientThread = new ClientHandlerThread(clientSocket, F);

            }
            catch (Exception e) {
                System.out.println(e.toString());
            }

        }
    }

    public String getname() {
        return Name;
    }

}
