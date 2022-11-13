/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.threads;

import com.unn.user_core.data_types.UCM;
import com.unn.user_core.interfaces.UCMI;

/**
 *
 * @author STALKER
 */
public class SocketManagerThread extends ThreadBase {
    private final UCMI router;
    
    public SocketManagerThread(UCMI router) {
        super(UCM.TID.SOCKET_MANAGER_THREAD);
        this.router = router;
    }

    @Override
    protected void terminationCallback() {
        // close RX TX threads
    }

    @Override
    protected boolean handlePersonalMessage(UCM msg) {
        return false;
    }
}
