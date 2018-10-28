package old;

import java.io.BufferedInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class KlijentTred extends Thread {

    private Socket socket;
    private Fajl F;
    private byte[] localBuffer;
    private int deo;
    private Klijent K;

    public KlijentTred(Socket s, int deo, Klijent K, Fajl F) {
        socket = s;
        this.K = K;
        this.F = F;
        localBuffer = new byte[1024];
        this.deo = deo;
        K.setDelovi(deo, true);
        K.setKlijentTred(deo, this);
        start();
    }

    @Override
    public void run() {

        try {

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());

            writer.println("andra.jpg");
            writer.println(deo);

            localBuffer = new byte[20000];
            byte buffer[] = new byte[256];

            int bytesRead;
            int downloaded = 0;

            while ((bytesRead = reader.read(buffer, 0, buffer.length)) != -1) {

                System.arraycopy(buffer, 0, localBuffer, downloaded, bytesRead);
                downloaded += bytesRead;
            }

            localBuffer = Arrays.copyOf(localBuffer, downloaded);
            K.setDelovi(deo, true);
            K.setKlijentTred(deo, this);
        }
        catch (Exception e) {
            e.printStackTrace();
            K.setDelovi(deo, false);
            K.setKlijentTred(deo, null);
        }

    }

    public byte[] getLocalBuffer() {

        return localBuffer;
    }
}
//ASANA SERVIS
//SLACK CHAT
//TRELLO
//transmision qt