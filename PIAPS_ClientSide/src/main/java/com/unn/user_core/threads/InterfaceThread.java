/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.threads;

import com.unn.user_core.data_types.UCM;
import com.unn.user_core.interfaces.UCMI;
import com.unn.user_core.interfaces.UIMI;

/**
 *
 * @author STALKER
 */
public class InterfaceThread extends ThreadBase{
    private final UCMI router;
    private final UIMI ui;
    
    public InterfaceThread(UCMI router, UIMI ui) {
        super(UCM.TID.INTERFACE_THREAD);
        this.router = router;
        this.ui = ui;
    }
    

    @Override
    protected void terminationCallback() {
        // send ui tremination message
        
    }

    @Override
    protected boolean handlePersonalMessage(UCM msg) {
        return false;
    }
}
