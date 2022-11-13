/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.threads;

import com.unn.user_core.Log;
import com.unn.user_core.data_types.UCM;
import com.unn.user_core.interfaces.UCMI;

/**
 *
 * @author STALKER
 */
public class ShutdownHookThread extends Thread {
    private final UCMI router;
    
    public ShutdownHookThread(UCMI router) {
        this.router = router;
    }
    
    @Override
    public void run() {
        Log.info(UCM.TID.SHUTDOWN_HOOK_THREAD.name() + ": termination process begin");
        UCM msg = UCM.nm()
                .setTo(UCM.TID.GLOBAL)
                .setFrom(UCM.TID.SHUTDOWN_HOOK_THREAD)
                .setType(UCM.TYPE.GLOBAL_TERMINATE);
        router.sendMessage(msg);
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Log.error(UCM.TID.SHUTDOWN_HOOK_THREAD.name() + " got exception: " + ex.toString());
        }
    }
}
