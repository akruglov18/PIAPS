/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.threads;

import com.unn.user_core.Log;
import com.unn.user_core.data_types.UCM;
import com.unn.user_core.interfaces.UCMI;
import java.util.ArrayDeque;

/**
 *
 * @author STALKER
 */
public abstract class ThreadBase extends Thread implements UCMI{
    protected final ArrayDeque<UCM> messageQueue = new ArrayDeque<>();
    protected boolean running = true;
    protected final UCM.TID ID;
    
    public ThreadBase() {
        this.ID = UCM.TID.THREAD_BASE;
    }
    
    public ThreadBase(UCM.TID ID) {
        this.ID = ID;
    }
    
    
    @Override
    public void sendMessage(UCM msg) {
        synchronized (messageQueue) {
            messageQueue.addFirst(msg);
            messageQueue.notify();
        }
    }
    
    @Override
    public UCM.TID getThreadID() {
        return ID;
    }
    
    /*@Override
    public void startThread() {
        super.start();
    }*/
    
    @Override
    public void run() {
        if (ID == UCM.TID.THREAD_BASE) {
            Log.error("Wrong thread ID init parameter used on thread: " + super.getName());
            return;
        }
        
        Log.info(ID.name() + " started");
        UCM inputMessage;
        while(running) {
            synchronized(messageQueue) {
                if(messageQueue.isEmpty()) {
                    try {
                        messageQueue.wait();
                    } catch (InterruptedException ex) {
                        Log.error(ID.name() + " got exception: " + ex.toString());
                    }
                }
                inputMessage = messageQueue.removeLast();
            }
            handleMessage(inputMessage);
        }
    }
    
    protected void handleMessage(UCM msg) {
        if (msg.to != ID && msg.to != UCM.TID.GLOBAL) {
            Log.error("Message rerouting error on thread: " + ID.name());
            return;
        }
        
        switch(msg.type) {
            case GLOBAL_HANDSHAKE -> Log.info("Handshake received in <" + ID.name()+ "> from <" + msg.from.name() + ">");
            case GLOBAL_TERMINATE -> {
                Log.info(ID.name() + " terminating");
                terminationCallback();
                running = false;
            }
            default -> {
                if (handlePersonalMessage(msg))
                    break;
                Log.error("Unhandled user core message in: " + ID.name() + 
                    " Msg: " + msg.toString());
            }
        }
    }
    
    protected abstract void terminationCallback();
    // return true if handled
    protected abstract boolean handlePersonalMessage(UCM msg);
}
