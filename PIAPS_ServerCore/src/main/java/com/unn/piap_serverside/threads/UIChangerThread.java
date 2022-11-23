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
public class UIChangerThread extends ThreadBase {
    private final SCMI router;
    private final UICTIfc uiInterface;
    
    public UIChangerThread(SCMI router, UICTIfc uiInterface) {
        super(SCM.TID.UI_CHANGER_THREAD);
        this.router = router;
        this.uiInterface = uiInterface;
    }
    

    @Override
    protected void terminationCallback() {
        
    }

    @Override
    protected boolean handlePersonalMessage(SCM msg) {
        switch(msg.type) {
            case SMT_START_LISTENING -> {
                if (msg.body != null && msg.body.getClass().getName().equals(String.class.getName()))
                    uiInterface.setServerSocketStatus((String)msg.body);
                else
                    Log.error("UIChangerThread got SMT_START_LISTENING with wrong msg.body params " + msg.toString());
                return true;
            }
                
            case SMT_STOP_LISTENING -> { 
                if (msg.body != null && msg.body.getClass().getName().equals(String.class.getName()))
                    uiInterface.setServerSocketStatus((String)msg.body);
                else
                    Log.error("UIChangerThread got SMT_START_LISTENING with wrong msg.body params " + msg.toString());
                return true;
            }
        }
        
        
        
        return false;
    }
    
    public interface UICTIfc {
        void setServerSocketStatus(String status);
    }
}
