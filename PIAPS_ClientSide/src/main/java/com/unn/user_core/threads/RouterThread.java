/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.threads;

import com.unn.user_core.Log;
import com.unn.user_core.data_types.UCM;
import com.unn.user_core.interfaces.UCMI;
import java.util.ArrayList;

/**
 *
 * @author STALKER
 */
public class RouterThread extends ThreadBase{
    private final ArrayList<UCMI> childrenModules = new ArrayList<>();
    
    public RouterThread() {
        super(UCM.TID.ROUTER_THREAD);
    }
    
    public void registerChild(UCMI child) {
        childrenModules.add(child);
    }
    
    
    @Override
    protected void handleMessage(UCM msg) {
        // handling personal messages
        if (msg.to == ID) {
            return;
        }
        
        // handling global messages
        if (msg.to == UCM.TID.GLOBAL) {
            switch (msg.type) {
                case GLOBAL_HANDSHAKE -> {
                    Log.info("Handshake received in <" + ID.name()+ "> from <" + msg.from.name() + ">");
                }
                case GLOBAL_TERMINATE -> {
                    Log.info(ID.name() + " terminating");
                    running = false;
                }
                default -> {
                    Log.error(ID.name() + " got unknown global message: " + msg.toString());
                }
            }
            for (UCMI child : childrenModules)
                child.sendMessage(msg);
            return;
        }
        
        // handling forwarded messages (FORWARD ONLY FOR 1 CHILD)
        for (UCMI child : childrenModules) {
            if (child.getThreadID() == msg.to) {
                child.sendMessage(msg);
                return;
            }
        }
        
        Log.error(ID.name() + " unhandled message: " + msg.toString());
    }

    @Override
    protected void terminationCallback() {
        // unused because handleMessage() for router thread is overwritten
    }

    @Override
    protected boolean handlePersonalMessage(UCM msg) {
        // unused because handleMessage() for router thread is overwritten
        return true;
    }
    
}
