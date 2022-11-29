/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.graphical_user_interface;

import com.unn.user_core.Log;
import com.unn.user_core.interfaces.UIMI;
import java.util.ArrayDeque;
import com.unn.user_core.interfaces.IUimMessage;

/**
 *
 * @author STALKER
 */
public class UIChangerThread extends Thread implements UIMI {
    protected final ArrayDeque<IUimMessage> messageQueue = new ArrayDeque<>();
    protected boolean running = true;
    
    public UIChangerThread() {
        // add uithread
    }
    
    @Override
    public void run() {
        Log.info("UIChangerThread started");
        IUimMessage inputMessage;
        while(running) {
            synchronized(messageQueue) {
                if(messageQueue.isEmpty()) {
                    try {
                        messageQueue.wait();
                    } catch (InterruptedException ex) {
                        Log.error("UIChangerThread got exception: " + ex.toString());
                    }
                }
                inputMessage = messageQueue.removeLast();
            }
            handleMessage(inputMessage);
        }
    }
    
    private void handleMessage(IUimMessage msg) {
        
    }

    @Override
    public void sendMessage(IUimMessage msg) {
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
