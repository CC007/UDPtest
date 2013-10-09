package server.test.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rik Schaaf
 */
public class Client extends Thread implements Serializable {

    DatagramSocket socket;
    int port;

    public Client(int port) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket();
        this.port = port;

    }

    @Override
    public void run() {
        try {
            //Create data output
            ByteArrayOutputStream bout = new ByteArrayOutputStream(4096);
            DataOutputStream out = new DataOutputStream(bout);
            out.writeInt(20);
            out.writeInt(50);
            out.writeInt(3);
            out.writeInt(-2);
            DatagramPacket sendPacket = new DatagramPacket(bout.toByteArray(), bout.toByteArray().length, InetAddress.getLocalHost(), port);
            socket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(new byte[4096], 4096);
            socket.receive(receivePacket);
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(receivePacket.getData()));
            if (in.readInt() == Server.OK) {
                System.out.println("CLIENT: server send OK back.");
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
