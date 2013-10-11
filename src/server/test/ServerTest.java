package server.test;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.test.controller.*;

/**
 *
 * @author Rik Schaaf
 */
public class ServerTest {

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        try {
            ServerController server = new ServerController(25565);
            server.start();
            ClientController client = new ClientController(25565);
            client.start();
        } catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
