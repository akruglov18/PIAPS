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
    Gson converter;
    
    public SocketManagerThread(UCMI router) {
        super(UCM.TID.SOCKET_MANAGER_THREAD);
        this.router = router;
        this.rxMessageQueue = new ArrayDeque<>();
        this.txThread = new TxThread(socket);
        this.rxThread = new RxThread(rxMessageQueue, socket);
        try {
            this.ip = InetAddress.getLocalHost();
            this.socket = new Socket(ip, port);
            converter = new Gson();
        } catch (IOException ex) {
            Log.error(ex.getMessage());
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
            txThread.addMessage(converter.toJson(msg.body));
        }
        return true;
    }
}
