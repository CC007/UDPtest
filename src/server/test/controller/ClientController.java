package server.test.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rik Schaaf
 */
public class ClientController extends Thread implements Serializable {

    protected DatagramSocket socket;
    protected DatagramPacket receivePacket;
    protected DatagramPacket sendPacket;
    protected ByteArrayInputStream bin;
    protected ByteArrayOutputStream bout;
    protected DataInputStream din;
    protected DataOutputStream dout;
    protected ObjectInputStream in;
    protected ObjectOutputStream out;
    protected volatile boolean running;
    InetSocketAddress serverAddress;
    boolean serverReady;

    public ClientController(InetSocketAddress address, InetSocketAddress serverAddress) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket(address);
        this.running = true;
        this.serverReady = false;
        this.serverAddress = serverAddress;
        boolean error = true;
        while (error) {
            int counter = 0;
            int actualResponse;
            try {
                if (counter == 0) {
                    bout = new ByteArrayOutputStream(4096);
                    dout = new DataOutputStream(bout);
                    out = new ObjectOutputStream(dout);
                    out.writeInt(2);
                }
                sendPacket = new DatagramPacket(bout.toByteArray(), bout.toByteArray().length, serverAddress);
                socket.send(sendPacket);
            } catch (IOException ex) {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void run() {
    }
}
