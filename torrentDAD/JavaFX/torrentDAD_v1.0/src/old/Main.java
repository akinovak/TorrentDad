package old;

public class Main {

    public static void main(String[] args) {

         Fajl prvi = new Fajl("andra.jpg", 92776);
//         Klijent client = new Klijent(prvi);
        Server server = new Server("192.168.1.38", 9090, prvi);

    }

}
