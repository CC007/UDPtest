package server.test.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rik Schaaf
 */
public class ServerController extends Thread {

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
    private Collection<InetSocketAddress> clients;
    private Collection<InetSocketAddress> players;
    private Collection<InetSocketAddress> spectators;

    public ServerController(InetSocketAddress address) throws SocketException {
        this.socket = new DatagramSocket(address);
        this.running = true;
        this.clients = Collections.synchronizedList(new LinkedList<InetSocketAddress>());
        this.players = Collections.synchronizedList(new LinkedList<InetSocketAddress>());
        this.spectators = Collections.synchronizedList(new LinkedList<InetSocketAddress>());
    }

    @Override
    public void run() {
        while (running) {
            try {
                receivePacket = new DatagramPacket(new byte[4096], 4096);
                socket.receive(receivePacket);
                bin = new ByteArrayInputStream(receivePacket.getData());
                in = new ObjectInputStream(bin);
                bout = new ByteArrayOutputStream(4096);
                out = new ObjectOutputStream(bout);
                switch (in.readInt()) {
                    default:
                        System.out.println("bla");
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
