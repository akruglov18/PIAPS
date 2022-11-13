/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.command_line_interface;

import com.unn.user_core.Log;
import com.unn.user_core.data_types.UIM;
import com.unn.user_core.interfaces.UIMI;
import java.util.ArrayDeque;

/**
 *
 * @author STALKER
 */
public class CLOutputThread extends Thread implements UIMI {
    protected final ArrayDeque<UIM> messageQueue = new ArrayDeque<>();
    protected boolean running = true;
    
    
    public CLOutputThread() {
        
    }
    
    @Override
    public void run() {
        Log.info("CLOutputThread started");
        UIM inputMessage;
        while(running) {
            synchronized(messageQueue) {
                if(messageQueue.isEmpty()) {
                    try {
                        messageQueue.wait();
                    } catch (InterruptedException ex) {
                        Log.error("CLOutputThread got exception: " + ex.toString());
                    }
                }
                inputMessage = messageQueue.removeLast();
            }
            handleMessage(inputMessage);
        }
    }
    
    private void handleMessage(UIM msg) {
        
    }
    
    

    @Override
    public void sendMessage(UIM msg) {
        synchronized (messageQueue) {
            messageQueue.addFirst(msg);
            messageQueue.notify();
        }
    }

    @Override
    public void startModule() {
        super.start();
    }
    
}
