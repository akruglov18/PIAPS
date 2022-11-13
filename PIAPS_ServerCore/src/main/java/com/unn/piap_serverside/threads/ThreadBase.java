/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside.threads;

import com.unn.piap_serverside.Log;
import com.unn.piap_serverside.data_types.SCM;
import com.unn.piap_serverside.interfaces.SCMI;
import java.util.ArrayDeque;

/**
 *
 * @author STALKER
 */
public abstract class ThreadBase extends Thread implements SCMI {
    protected final ArrayDeque<SCM> messageQueue = new ArrayDeque<>();
    protected boolean running = true;
    protected final SCM.TID ID;
    
    public ThreadBase() {
        ID = SCM.TID.THREAD_BASE;
    }
    
    public ThreadBase(SCM.TID ID) {
        this.ID = ID;
    }
    
    @Override
    public void sendMessage(SCM msg) {
        synchronized (messageQueue) {
            messageQueue.addFirst(msg);
            messageQueue.notify();
        }
    }

    @Override
    public SCM.TID getThreadID() {
        return ID;
    }
    
    @Override
    public void run() {
        if (ID == SCM.TID.THREAD_BASE) {
            Log.error("Wrong thread ID init parameter used on thread: " + super.getName());
            return;
        }
        
        Log.info(ID.name() + " started");
        SCM inputMessage;
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
    
    protected void handleMessage(SCM msg) {
        if (msg.to != ID && msg.to != SCM.TID.GLOBAL) {
            Log.error("Message rerouting error on thread: " + ID.name());
            return;
        }
        
        switch(msg.type) {
            case GLOBAL_HANDSHAKE -> {
                Log.info("Handshake received in <" + ID.name()+ "> from <" + msg.from.name() + ">");
            }
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
    protected abstract boolean handlePersonalMessage(SCM msg);
}
