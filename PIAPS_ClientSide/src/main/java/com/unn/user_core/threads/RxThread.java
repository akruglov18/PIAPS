/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.threads;
import com.unn.user_core.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acer
 */
public class RxThread extends Thread {
    BufferedReader sockReader;
    protected Socket socket;
    protected final Queue<String> messageQueue;
    protected boolean running;

    public RxThread(Queue<String> queue, Socket socket) {
        this.socket = socket;
        this.messageQueue = queue;
    }
    @Override
    public void run()
    {
        running = true;
        try {
            this.sockReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Log.error(ex.getMessage());
        }
        String str;
        try {
            while (running) {
                str = sockReader.readLine();
                synchronized (messageQueue) {
                    messageQueue.add(str);
                    messageQueue.notify();
                } 
            }
        } catch (IOException ex) {
            Log.error(ex.getMessage());
        }
        Log.info("RxThread stopped");
        
        try {
            sockReader.close();
        } catch (IOException ex) {
            Logger.getLogger(RxThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close()
    {
        running = false;
    }
}
