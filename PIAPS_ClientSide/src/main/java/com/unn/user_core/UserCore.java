/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core;

import com.unn.user_core.data_types.UCM;
import com.unn.user_core.data_types.UIM;
import com.unn.user_core.interfaces.UIMI;
import com.unn.user_core.threads.InterfaceThread;
import com.unn.user_core.threads.RouterThread;
import com.unn.user_core.threads.ShutdownHookThread;
import com.unn.user_core.threads.SocketManagerThread;

/**
 *
 * @author STALKER
 */
public class UserCore implements UIMI{
    private final RouterThread routerThread;
    private final InterfaceThread interfaceThread;
    private final SocketManagerThread socketManagerThread;
    
    public UserCore(UIMI ui) {
        routerThread = new RouterThread();
        
        interfaceThread = new InterfaceThread(routerThread, ui);
        routerThread.registerChild(interfaceThread);
        
        Runtime.getRuntime().addShutdownHook(new ShutdownHookThread(routerThread));
        
        socketManagerThread = new SocketManagerThread(routerThread);
        routerThread.registerChild(socketManagerThread);
    }
 
    @Override
    public void sendMessage(UIM msg) {
        UCM cmsg = UCM.nm()
                .setTo(UCM.TID.INTERFACE_THREAD)
                .setFrom(UCM.TID.USER_INTERFACE)
                .setType(UCM.TYPE.IF_THD_UI_MSG)
                .setBody(msg);
        routerThread.sendMessage(cmsg);
    }

    @Override
    public void startModule() {
        routerThread.start();
        interfaceThread.start();
        socketManagerThread.start();
    }
}
