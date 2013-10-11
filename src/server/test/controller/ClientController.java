package server.test.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.test.ServerTest;

/**
 *
 * @author Rik Schaaf
 */
public class ClientController extends Thread implements Serializable {

    DatagramSocket socket;
    DatagramPacket receivePacket;
    DatagramPacket sendPacket;
    ByteArrayInputStream bin;
    ByteArrayOutputStream bout;
    DataInputStream in;
    DataOutputStream out;
    int port;

    public ClientController(int port) throws SocketException, UnknownHostException {

        this.socket = new DatagramSocket();
        this.port = port;

    }

    @Override
    public void run() {
        try {
            Scanner inFromUser = new Scanner(
                    new BufferedReader(
                    new InputStreamReader(System.in)));
            this.sendAndWait(ServerController.SERVER_READY, ServerController.OK);
            this.sendAndWait(ServerController.SEND_GAME_OBJECT_DATA, ServerController.READY_FOR_GAME_OBJECT_DATA);
            System.out.println("Geef de x, y, dx, en dy:");
            do {
                bout = new ByteArrayOutputStream(4096);
                out = new DataOutputStream(bout);
                out.writeInt(inFromUser.nextInt());
                out.writeInt(inFromUser.nextInt());
                out.writeInt(inFromUser.nextInt());
                out.writeInt(inFromUser.nextInt());
                sendPacket = new DatagramPacket(bout.toByteArray(), bout.toByteArray().length, InetAddress.getLocalHost(), port);
                socket.send(sendPacket);
                receivePacket = new DatagramPacket(new byte[4096], 4096);
                socket.receive(receivePacket);
                bin = new ByteArrayInputStream(receivePacket.getData());
                in = new DataInputStream(bin);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while (in.readInt() != ServerController.READY_FOR_GAME_OBJECT_DATA);
            System.out.println("CLIENT: server accepted the data.");
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendAndWait(int sendData, int expectedResponse) {
        int counter = 0;
        int actualResponse;
        try {
            do {
                if (counter == 0) {
                    bout = new ByteArrayOutputStream(4096);
                    out = new DataOutputStream(bout);
                    out.writeInt(sendData);
                }
                sendPacket = new DatagramPacket(bout.toByteArray(), bout.toByteArray().length, InetAddress.getLocalHost(), port);
                socket.send(sendPacket);
                receivePacket = new DatagramPacket(new byte[4096], 4096);
                socket.receive(receivePacket);
                bin = new ByteArrayInputStream(receivePacket.getData());
                in = new DataInputStream(bin);
                actualResponse = in.readInt();
                if (actualResponse != expectedResponse) {
                    System.out.println(actualResponse + " in stead of " + expectedResponse);
                    Thread.sleep(50);
                    counter = (counter == 10 ? 0 : counter + 1);
                }
            } while (actualResponse != expectedResponse);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
