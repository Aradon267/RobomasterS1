package com.example.robomaster;

import android.util.Log;
import java.io.*;
import java.net.*;

/**
 * The class will hold the information of the server and will manage sending commands to it.
 * ServerIP - the IP of the server we connect to.
 * ServerPort - the port of the server we connect to.
 * socket - the we create with the server.
 * writer - where we write the data to be sent.
 * inputStream - where we get data from the server.
 */

public class Server {

    /**
     * A regular constructor of the class
     * @param ip the ip of the server
     * @param port the port of the server
     */
    public Server(String ip, int port) {
        this.ServerIP = ip;
        this.ServerPort = port;
        this.socket = null;
        this.writer = null;
    }

    private String ServerIP;
    private int ServerPort;
    private Socket socket;
    private OutputStream writer;

    /**
     * Establishes connection with the server
     * @return true if connection was established, false otherwise
     */
    public boolean establishConnection(){
        try {
            this.socket = new Socket(ServerIP, ServerPort);
            this.writer = socket.getOutputStream();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            this.socket = null;
            this.writer = null;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Server", e.getMessage());
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Returns if the user is connected
     * @return true if connected, false otherwise
     */
    public boolean isConnected(){
        return this.socket != null;
    }

    /**
     * Sends the message to the python server
     * @param data message to be sent
     * @return true if managed to send, false otherwise
     */
    public boolean sendMessage(byte[] data) {
        if (!isConnected()) {
            return false;
        }
        try {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Combines all the given arrays to one array
     * @param arrays all the arrays to be combined
     * @return the combined array
     */
    public static byte[] concat(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        byte[] result = new byte[length];
        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    /**
     * Closes connection with the server
     * @return true if the connection closed, false if the connection failed to close or never existed
     */
    public boolean closeConnection(){
        if (!isConnected()) return false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}