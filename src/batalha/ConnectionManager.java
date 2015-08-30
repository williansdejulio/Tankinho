/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batalha;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Leonardo
 */
public class ConnectionManager {
    
    private static final int PORT = 10007;
    
    public static void close() throws IOException {
        if (server)
            serverSocket.close();
        else
            echoSocket.close();
    }

    private static Socket echoSocket = null;
    private static ServerSocket serverSocket = null;
    private static DataOutputStream out;
    private static DataInputStream in;
    private static boolean server;

    static void flush() throws IOException {
        out.flush();
    }

    public static void createConnection(String hostname) {
        server = false;
        try {
            System.out.println("hostname : " + hostname);
            echoSocket = new Socket(hostname, PORT);
            
            out = new DataOutputStream(echoSocket.getOutputStream());
            out.flush();
            in = new DataInputStream(echoSocket.getInputStream());

        } catch (UnknownHostException e) {
            createServer();
        } catch (IOException e) {
            /*System.err.println("DEU RUIM AO TENTAR SE CONECTAR A PORTA: " +  PORT);
            System.exit(1);*/
            createServer();
        }
    }

    public static void write(byte[] pack) throws IOException {
        out.write(pack);
    }

    public static void read(byte[] pack) throws IOException {
        in.read(pack);
    }

    public static int unpack(byte[] bytes, int c) {
        return bytes[c] << 24 | (bytes[c + 1] & 0xFF) << 16 | (bytes[c + 2] & 0xFF) << 8 | (bytes[c + 3] & 0xFF);
    }
    public static void pack(int value, byte[] b, int c) {
        b[c] = (byte) (value >>> 24);
        b[c + 1] = (byte) (value >>> 16);
        b[c + 2] = (byte) (value >>> 8);
        b[c + 3] = (byte) (value);
    }

    private static void createServer() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " +  PORT);
            System.exit(1);
        }
        
        Socket clientSocket;
        System.out.println("Waiting for connection to tank selection.....");

        try {
            clientSocket = serverSocket.accept();
            out = new DataOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new DataInputStream(clientSocket.getInputStream());

            System.out.println("Connection established.");
            server = true;

        } catch (IOException e) {
            System.err.println("Accept failed.");
        }
    }

    public static boolean isServer() {
        return server;
    }
}
