/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.threads;
import com.unn.user_core.data_types.UCM;
import com.unn.user_core.interfaces.UCMI;
import com.unn.user_core.Log;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acer
 */
public class RxThread extends Thread {
    DataInputStream dis;
    Socket socket;
    protected final Queue<String> messageQueue;
    boolean running;

    public RxThread(Queue<String> queue, Socket socket) {
        this.socket = socket;
        this.messageQueue = queue;
    }
    @Override
    public void run()
    {
        running = true;
        try {
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Log.error(ex.getMessage());
        }
        String str;
        try {
            while (running) {
                str = dis.readUTF();
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
            dis.close();
        } catch (IOException ex) {
            Logger.getLogger(RxThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close()
    {
        running = false;
    }
}
