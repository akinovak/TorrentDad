package old;

import javafx.util.Pair;

public class Fajl {

    private String Name;
    private Server serverArr[];
    private int serverNum;
    private int velicinaNiza;
    private int size;
    private Pair<Integer, Integer> parts[];


    private static int korakZaAlokaciju = 10;

    public Fajl(String Name, int size) {
        this.Name = Name;
        serverNum = 0;
        serverArr = new Server[korakZaAlokaciju];
        velicinaNiza = korakZaAlokaciju;
        parts = new Pair[5];
        this.size = size;
        int kor = size/5;
        for(int i=0; i < 5; i++) {
            if(i == 4)
                parts[i] = new Pair<>(i*kor, size);
            else
                parts[i] = new Pair<>(i*kor, (i+1)*kor);
        }

    }

    public synchronized void dodajServer(Server S) {

        if(serverNum == velicinaNiza) {
            serverArr = new Server[velicinaNiza + korakZaAlokaciju];
            velicinaNiza += korakZaAlokaciju;
            serverArr[serverNum++] = S;
        }
        else
        {
            serverArr[serverNum++] = S;
        }

    }

    public synchronized Server[] getNizServera() {
        return serverArr;
    }

    public synchronized int getBrojServera() {
        return serverArr.length;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return Name;
    }

    public Pair<Integer, Integer>[] getDelovi() {
        return parts;
    }


}
