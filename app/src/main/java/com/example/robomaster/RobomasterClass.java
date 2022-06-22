package com.example.robomaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;


/**
 * The class will hold the information of the robot and will manage sending commands to the server.
 * server - the server we connect to and send all the data to.
 * queue - the queue we get the data from in the thread to send to the server.
 * eventQueue - the queue that holds all the commands until we execute all of the commands.
 */

public class RobomasterClass  {
    private Server server;
    private Queue<String> queue;
    private Queue<String> eventsQueue;


    /**
     * A regular constructor of the class
     * @param ip the ip of the server we connect to
     * @param port the port of the server we connect to
     */
    public RobomasterClass(String ip, int port) {
        queue = new LinkedList<String>();
        eventsQueue = new LinkedList<String>();

        this.server = new Server(ip, port);
    }

    /**
     * Returns if the user is connected
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return server.isConnected();
    }


    /**
     * Establishes connection with the server
     * @param curr the context of the current activity
     * @param next the class of the activity we want to move to
     */
    public void establishConnection(Context curr,Class next, String name) {
        Thread networkThread = new Thread(() -> {
            if (server.establishConnection()) {
                ((Activity)curr).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(curr, next);
                        i.putExtra("user",name);
                        ((Activity)curr).startActivity(i);
                    }
                });
                while (isConnected()) {
                        if (!queue.isEmpty()) {
                            String message = queue.remove();
                            requestMessage(message);
                        }
                }
            }
            else{
                ((Activity)curr).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(curr, "Couldn't connect to given server", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        networkThread.start();
    }

    /**
     * Establishes a quick connection with the server
     */
    public void establishQuickConnection() {
        Thread networkThread = new Thread(() -> {
            if (server.establishConnection()) {
                while (isConnected()) {
                    if (!queue.isEmpty()) {
                        String message = queue.remove();
                        requestMessage(message);
                    }
                }
            }
        });
        networkThread.start();
    }

    /**
     * Adds a command to the queue
     * @param message the command to add
     */
    public void addToMessagesQueue(final String message) {
        eventsQueue.add(message);
    }

    /**
     * Adds the combined commands to the queue we send to the server
     */
    public void sendMessagesQueue() {
        this.sendMessage(joinMessagesQueue());
    }

    /**
     * Combines the commands queue to one string
     * @return the combined commands as a string
     */
    private String joinMessagesQueue() {
        StringBuilder currentJoinedMessages = new StringBuilder();
        String currentMessage;
        while (!eventsQueue.isEmpty()) {
            currentMessage = eventsQueue.remove();

            currentJoinedMessages.append(currentMessage).append("\n");
        }
        return currentJoinedMessages.toString();
    }

    /**
     * Sends the combined commands to the queue to be sent
     * @param message the combines commands
     */
    public void sendMessage(String message) {
        this.queue.add(message);
    }

    /**
     * Sends the combined commands from the queue to the server
     * @param message the combined commands from the queue
     */
    private void requestMessage(String message) {
        if (!isConnected()) return;
        ByteBuffer streamSize = ByteBuffer.allocate(4);
        streamSize.putInt(message.length());
        //
        server.sendMessage(Server.concat(
                streamSize.array(),
                message.getBytes()
        ));
    }

    /**
     * Disconnects user from server
     */
    public void disconnect() {
        server.closeConnection();
    }

    /**
     * Return the queue with all of the commands to br executed
     * @return the queue with the commands
     */
    public Queue<String> getQ(){
        return this.eventsQueue;
    }

    /**
     * Clears the event queue
     */
    public void clearQueue(){
        this.eventsQueue.clear();
    }

}
