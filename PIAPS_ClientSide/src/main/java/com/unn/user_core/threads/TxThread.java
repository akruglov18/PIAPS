/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.threads;
import com.unn.user_core.Log;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acer
 */
public class TxThread extends Thread {
    protected final ArrayDeque<String> messageQueue;
    private final Socket socket;
    private DataOutputStream dos;
    private boolean running;

    public TxThread(Socket socket) {
        this.socket = socket;
        messageQueue = new ArrayDeque<>();
    }
    public void addMessage(String msg) {
        synchronized (messageQueue) {
            messageQueue.addFirst(msg);
            messageQueue.notify();
        }
    }
    @Override
    public void run()
    {
        running = true;
        String msg;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(TxThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(running) {
            synchronized(messageQueue) {
                if(messageQueue.isEmpty()) {
                    try {
                        messageQueue.wait();
                    } catch (InterruptedException ex) {
                        Log.error("Tx thread exception: " + ex.toString());
                    }
                }
                msg = messageQueue.removeLast();
            }
            handleMessage(msg);
        }
        Log.info("TxThread stopped");
        try {
            dos.close();
        } catch (IOException ex) {
            Log.error("Tx thread exception: " + ex.toString());
        }
    }
    
    public void close()
    {
        running = false;
    }
    
    private void handleMessage(String msg) {
        try {
            if (dos != null)
                dos.writeUTF(msg);
        } catch (IOException ex) {
            Log.error("Tx thread exception: " + ex.toString());
        }
    }
}
