package server.test.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rik Schaaf
 */
public class Server extends Thread {

    public static final int OK = 0;
    public static final int ERROR = 1;
    public static final int CANCEL = Integer.MAX_VALUE;
    
    public static final int SEND_JOIN_REQUEST = 100;
    public static final int SEND_JOIN_ACCEPTED = 101;
    public static final int SEND_SPECTATE_REQUEST = 110;
    public static final int SEND_SPECTATE_ACCEPTED = 111;
    
    public static final int SEND_GAME_OBJECT_DATA = 200;
    
    
    public static final int SEND_GAME_OVER = 300;
    DatagramSocket serverSocket;
    boolean running;

    public Server(int port) throws SocketException {
        this.running = false;
        this.serverSocket = new DatagramSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(new byte[4096], 4096);
                try {
                    running = true;
                    serverSocket.receive(receivePacket);

                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                InputHandler ih = new InputHandler(receivePacket, serverSocket);
                ih.start();
            } catch (SocketException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean isRunning() {
        return running;
    }
}
