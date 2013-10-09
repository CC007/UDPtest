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
