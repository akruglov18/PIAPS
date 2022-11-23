/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside.threads;

import com.unn.piap_serverside.data_types.SCM;
import com.unn.piap_serverside.interfaces.SCMI;

/**
 *
 * @author STALKER
 */
public class SocketManagerThread extends ThreadBase {
    private final SCMI router;
    
    public SocketManagerThread(SCMI router) {
        super(SCM.TID.SOCKET_MANAGER_THREAD);
        this.router = router;
    }

    @Override
    protected void terminationCallback() {
        
    }

    @Override
    protected boolean handlePersonalMessage(SCM msg) {
        return false;
    }
}