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
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rik Schaaf
 */
public class InputHandler extends Thread implements Serializable {

    DatagramSocket serversocket;
    DatagramPacket receivePacket;

    public InputHandler(DatagramPacket receivePacket, DatagramSocket serverSocket) throws SocketException {
        this.serversocket = serverSocket;
        this.receivePacket = receivePacket;
    }

    @Override
    public void run() {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(receivePacket.getData()));
        ByteArrayOutputStream bout = new ByteArrayOutputStream(4096);
        DataOutputStream out = new DataOutputStream(bout);
        try {
            int x = in.readInt();
            int y = in.readInt();
            int dx = in.readInt();
            int dy = in.readInt();
            System.out.println("SERVER: client send x=" + x + ", y=" + y + ", dx=" + dx + ", dy=" + dy + ".");
            out.writeInt(Server.OK);
            DatagramPacket sendPacket = new DatagramPacket(bout.toByteArray(), bout.toByteArray().length, receivePacket.getAddress(), receivePacket.getPort());
            serversocket.send(sendPacket);
        } catch (IOException ex) {
            Logger.getLogger(InputHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
