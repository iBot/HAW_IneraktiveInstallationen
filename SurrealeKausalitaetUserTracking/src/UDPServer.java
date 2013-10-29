import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.BlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: tobi
 * Date: 29.10.13
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */
public class UDPServer {

    private static final int BUFFER_SIZE = 1024;
    private final static int SERVER_PORT = 50001;
    private DatagramSocket serverSocket; // UDP-Socketklasse
    private boolean serviceRequested = true;

    public synchronized String getMessage() {
        return message;
    }

    public synchronized void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public UDPServer() {
    }



    public void startJob() {
        try {
            /* UDP-Socket erzeugen (kein Verbindungsaufbau!)
* Socket wird an irgendeinen freien (Quell-)Port gebunden, da kein Port angegeben */
            this.serverSocket = new DatagramSocket(SERVER_PORT);
            System.out.println("UDP Server: Waiting for connection - listening UDP port " +
                    SERVER_PORT);

            String message;

            while (serviceRequested) {
                message = readFromClient();
                this.setMessage(message);
            }

            /* Socket schließen (freigeben)*/
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
            System.exit(1);
        }
        System.out.println("UDP Client stopped!");
    }

    public void stopJob() {
        this.serviceRequested = false;
    }

    private String readFromClient() {
        /* Liefere den nächsten String vom Server */
        String receiveString = null;

        try {
            /* Paket für den Empfang erzeugen */
            byte[] receiveData = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, BUFFER_SIZE);

            /* Warte auf Empfang des Antwort-Pakets auf dem eigenen Port */
            serverSocket.receive(receivePacket);

            /* Paket wurde empfangen --> auspacken und Inhalt anzeigen */
            receiveString = new String(receivePacket.getData(), 0, receivePacket.getLength());
        } catch (IOException e) {
            System.err.println("Connection aborted by client!");
            serviceRequested = false;
        }
        System.out.println("UDP Client got from Client: " + receiveString);
        return receiveString;
    }

}
