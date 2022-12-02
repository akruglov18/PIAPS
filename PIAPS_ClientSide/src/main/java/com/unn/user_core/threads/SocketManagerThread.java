/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.threads;

import com.google.gson.Gson;
import com.unn.user_core.data_types.UCM;
import com.unn.user_core.interfaces.UCMI;
import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;
import com.unn.user_core.Log;
import java.util.ArrayDeque;
import com.unn.user_core.net_protocol.*;
import com.unn.user_core.data_types.UimParser;
/**
 *
 * @author STALKER
 */
public class SocketManagerThread extends ThreadBase {
    private final UCMI router;
    private final TxThread txThread;
    private final RxThread rxThread;
    private Socket socket;
    int port = 3124;
    InetAddress ip;
    protected final ArrayDeque<String> rxMessageQueue;
    UimParser uimParser;
    
    public SocketManagerThread(UCMI router) {
        super(UCM.TID.SOCKET_MANAGER_THREAD);
        this.router = router;
        this.rxMessageQueue = new ArrayDeque<>();
        this.txThread = new TxThread(socket);
        this.rxThread = new RxThread(rxMessageQueue, socket);
        try {
            this.ip = InetAddress.getLocalHost();
            this.socket = new Socket(ip, port);
        } catch (IOException ex) {
            Log.error(ex.getMessage());
        }
    }
    
    @Override
    public void run() {
        if (ID == UCM.TID.THREAD_BASE) {
            Log.error("Wrong thread ID init parameter used on thread: " + super.getName());
            return;
        }
        
        Log.info(ID.name() + " started");
        UCM internalMessage = null;
        String netMessage = null;
        while(running) {
            synchronized(messageQueue) {
                if(!messageQueue.isEmpty()) {
                    internalMessage = messageQueue.removeLast();
                }
            }
            if (internalMessage != null)
            {
                handleMessage(internalMessage);
                internalMessage = null;
            }
            synchronized(rxMessageQueue) {
                if(!rxMessageQueue.isEmpty()) {
                    netMessage = rxMessageQueue.removeLast();
                }
            }
            if (netMessage != null)
            {
                handleNetMessage(null);
                netMessage = null;
            }
        }
    }

    @Override
    protected void terminationCallback() {
//        try {
            txThread.close();
            rxThread.close();
//            txThread.join();
//            rxThread.join();
//        } catch (InterruptedException ex) {
//            Log.error("SocketManager ex: " + ex.getMessage());
//        }
    }

    @Override
    protected boolean handlePersonalMessage(UCM msg) {
        if (msg.type == UCM.TYPE.IF_THD_UI_MSG) {
            uimParser.parseMsg(msg.body);
        }
        return true;
    }
    
    protected void handleNetMessage(String msg) {
        
    }
}
