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
        portNum = -1;
        isListening = false;
    }

    @Override
    protected void terminationCallback() {
        serverSocketThread.stopListening();
    }

    @Override
    protected boolean handlePersonalMessage(SCM msg) {
        switch (msg.type) {
            case SMT_SET_PORT:
                if (msg.body != null && msg.body.getClass().getName().equals(Integer.class.getName())) {
                    this.portNum = (Integer) msg.body;
                    return true;
                } else {
                    Log.error("Body is not Integer");
                    return true;
                }

            case SMT_START_LISTENING:
                if (!this.isListening) {
                    try {
                        ServerSocket server = new ServerSocket(this.portNum);
                        if (server != null) {
                            serverSocketThread = new ServerSocketThread(server);
                            serverSocketThread.start();
                            this.isListening = true;

                            SCM ms = SCM.nm()
                                    .setFrom(SCM.TID.SOCKET_MANAGER_THREAD)
                                    .setTo(SCM.TID.UI_CHANGER_THREAD)
                                    .setType(SCM.TYPE.SMT_START_LISTENING)
                                    .setBody("задан");
                            router.sendMessage(ms);

                            return true;
                        } else {
                            Log.info("Can't create server on such port, try another");

                            SCM ms = SCM.nm()
                                    .setFrom(SCM.TID.SOCKET_MANAGER_THREAD)
                                    .setTo(SCM.TID.UI_CHANGER_THREAD)
                                    .setType(SCM.TYPE.SMT_START_LISTENING)
                                    .setBody("ошибка");
                            router.sendMessage(ms);

                            return true;
                        }
                    } catch (IOException e) {
                        Log.error("Can't create ServerSocket");

                        SCM ms = SCM.nm()
                                .setFrom(SCM.TID.SOCKET_MANAGER_THREAD)
                                .setTo(SCM.TID.UI_CHANGER_THREAD)
                                .setType(SCM.TYPE.SMT_START_LISTENING)
                                .setBody("ошибка");
                        router.sendMessage(ms);

                        return true;
                    }
                } else {
                    Log.info("Server is already listening");
                    return true;
                }
            case SMT_STOP_LISTENING:
                if (this.isListening) {
                    SCM ms = SCM.nm()
                            .setFrom(SCM.TID.SOCKET_MANAGER_THREAD)
                            .setTo(SCM.TID.UI_CHANGER_THREAD)
                            .setType(SCM.TYPE.SMT_STOP_LISTENING)
                            .setBody("остановлен");
                    router.sendMessage(ms);

                    return serverSocketThread.stopListening();
                } else {
                    Log.info("Server is already stopped");
                    return true;
                }
            default:
                return false;
        }
    }
}
