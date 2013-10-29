import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UDPClient {
    public final int SERVER_PORT = 50001;

    public UDPClient() {
    }

    void sendMessage(final String message) {
        Runnable sendRunnable = new Runnable() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.


                DatagramSocket clientSocket; // UDP-Socketklasse
                InetAddress serverIpAddress; // IP-Adresse des Zielservers

                try {


                    // UDP-Socket erzeugen (kein Verbindungsaufbau!)
                    //Socket wird an irgendeinen freien (Quell-)Port gebunden, da kein Port angegeben */
                    clientSocket = new DatagramSocket();
                    serverIpAddress = InetAddress.getByName("localhost"); // Zieladresse


//                    System.out.println("ENTER UDP-DATA: ");

                /* Sende den String als UDP-Paket zum Server */
                    writeToServer(serverIpAddress, clientSocket, message);


                    clientSocket.close();



            /* Socket schließen (freigeben)*/


                } catch (SocketException | UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        };

      new Thread(sendRunnable).start();
    }

    private void writeToServer(InetAddress serverIpAddress, DatagramSocket clientSocket, String sendString) {
        /* Sende den String als UDP-Paket zum Server */
        try {
            /* String in Byte-Array umwandeln */
            byte[] sendData = sendString.getBytes();
            /* Paket erzeugen */
            DatagramPacket sendPacket =
                    new DatagramPacket(
                            sendData,
                            sendData.length,
                            serverIpAddress,
                            SERVER_PORT);
            /* Senden des Pakets */
            clientSocket.send(sendPacket);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
//        System.out.println("UDP Client has sent the message: " + sendString);
    }
}
