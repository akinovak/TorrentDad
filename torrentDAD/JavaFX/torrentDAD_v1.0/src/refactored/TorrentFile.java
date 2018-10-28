package refactored;

import javafx.util.Pair;
import old.Server;

import java.io.Serializable;
import java.util.ArrayList;

public class TorrentFile implements Serializable{
    private String fileName;
    private ArrayList<String> seeders;
    private int size;
    private Pair<Integer, Integer> parts[]; // pair<offset, duzina>
    private int numOfParts;

    public String getFileName() {
        return fileName;
    }

    public ArrayList<String> getSeeders() {
        return seeders;
    }

    public Pair<Integer, Integer> getPart(int i) {
        return parts[i];
    }

    public TorrentFile(String fileName, int size, int numOfParts) {
        this.fileName = fileName;
        this.size = size;
        this.numOfParts = numOfParts;

        seeders = new ArrayList<>();
        parts = new Pair[numOfParts];

        int step = size / numOfParts;

        for (int i = 0; i < numOfParts; i++) {
            if (i == numOfParts-1)
                parts[i] = new Pair<>(i * step, size - (i*step));
            else
                parts[i] = new Pair<>(i*step, step);
                    // u serveru ne treba int buffLength = F.getDelovi()[deo].getValue() - offset;
                    // jer inicijalizacija parts[] resi sve
        }
    }

    public synchronized void addSeeder(String seeder) {
        seeders.add(seeder);
    }

    public synchronized void removeSeeder(String seeder) { //TODO signal trekeru da me ispise;
        seeders.remove(seeder);                                    // neka to uradi Golub na dugme necu da budem seeder
    }

    @Override
    public String toString() {
        return "[" + fileName + "]" + " size: " + size;
    }
}
