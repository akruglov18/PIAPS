/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside.threads;

import com.unn.piap_serverside.Log;
import com.unn.piap_serverside.data_types.SCM;
import com.unn.piap_serverside.interfaces.SCMI;

/**
 *
 * @author STALKER
 */
public class ShutdownHookThread extends Thread {
    private final SCMI router;
    
    public ShutdownHookThread(SCMI router) {
        this.router = router;
    }
    
    @Override
    public void run() {
        Log.info(SCM.TID.SHUTDOWN_HOOK_THREAD.name() + ": termination process begin");
        SCM msg = SCM.nm()
                .setTo(SCM.TID.GLOBAL)
                .setFrom(SCM.TID.SHUTDOWN_HOOK_THREAD)
                .setType(SCM.TYPE.GLOBAL_TERMINATE);
        router.sendMessage(msg);
        try {
            Thread.sleep(500);
        } catch(InterruptedException ex) {
            Log.error(SCM.TID.SHUTDOWN_HOOK_THREAD.name() + " got exception: " + ex.toString());
        }
    }
}
