/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside.threads;

import com.unn.piap_serverside.Log;
import com.unn.piap_serverside.data_types.SCM;
import com.unn.piap_serverside.interfaces.SCMI;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author STALKER
 */
public class SocketManagerThread extends ThreadBase {
    private final SCMI router;

    private Integer portNum;
    private ServerSocketThread serverSocketThread;
    private boolean isListening;
    
    public SocketManagerThread(SCMI router) {
        super(SCM.TID.SOCKET_MANAGER_THREAD);
        this.router = router;
        isListening = false;
    }

    @Override
    protected void terminationCallback() {
        
    }

    @Override
    protected boolean handlePersonalMessage(SCM msg) {
        switch (msg.type) {
            case SMT_SET_PORT:
                if (msg.body != null && msg.body.getClass().getName().equals(Integer.class.getName())) {
                    this.portNum = (Integer) msg.body;
                    return true;
                }
                else {
                    Log.error("Body is not Integer");
                    return false;
                }

            case SMT_START_LISTENING:
                if (!this.isListening) {
                    try {
                        ServerSocket server = new ServerSocket(this.portNum);
                        if (server != null) {
                            serverSocketThread = new ServerSocketThread(server);
                            serverSocketThread.start();
                            this.isListening = true;
                            return true;
                        }
                        else {
                            Log.info("Can't create server on such port, try another");
                            return false;
                        }
                    } catch (IOException e) {
                        Log.error("Can't create ServerSocket");
                        return false;
                    }
                }
                else {
                    Log.info("Server is already listening");
                    return false;
                }
            case SMT_STOP_LISTENING:
                if (this.isListening) {
                    return serverSocketThread.stopListening();
                }
                else {
                    Log.info("Server is already stopped");
                    return false;
                }
            default:
                return false;
        }
    }
}
