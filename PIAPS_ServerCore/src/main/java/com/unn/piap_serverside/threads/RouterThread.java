/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside.threads;

import com.unn.piap_serverside.Log;
import com.unn.piap_serverside.data_types.SCM;
import com.unn.piap_serverside.interfaces.SCMI;
import java.util.ArrayList;

/**
 *
 * @author STALKER
 */
public class RouterThread extends ThreadBase {
    private final ArrayList<SCMI> childrenModules = new ArrayList<>();
    
    public RouterThread() {
        super(SCM.TID.ROUTER_THREAD);
    }
    
    public void registerChild(SCMI child) {
        childrenModules.add(child);
    }
    
    @Override
    protected void handleMessage(SCM msg) {
        // handling personal messages
        if (msg.to == ID) {
            return;
        }
        
        // handling global messages
        if (msg.to == SCM.TID.GLOBAL) {
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
            for (SCMI child : childrenModules)
                child.sendMessage(msg);
            return;
        }
        
        // handling forwarded messages (FORWARD ONLY FOR 1 CHILD)
        for (SCMI child : childrenModules) {
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
    protected boolean handlePersonalMessage(SCM msg) {
        // unused because handleMessage() for router thread is overwritten
        return true;
    }
}
