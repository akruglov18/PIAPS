/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core;

import com.unn.user_core.data_types.*;
import com.unn.user_core.data_types.AuthorizationMsg;
import com.unn.user_core.data_types.RegistrationMsg;
import com.unn.user_core.interfaces.IUimMessageProcessor;
import com.unn.user_core.threads.TxThread;

/**
 *
 * @author acer
 */
public class UimMessageProcessor implements IUimMessageProcessor {
    protected TxThread txThread;
    
    public UimMessageProcessor(TxThread txThread) {
        this.txThread = txThread;
    }
    
    @Override
    public void handleMessage(AuthorizationMsg msg) {
    }
    
    @Override
    public void handleMessage(ChangeRequestStatusMsg msg) {
        
    }
    
    @Override
    public void handleMessage(CreateRequestMsg msg) {
        
    }
    
    @Override
    public void handleMessage(GetMessageMsg msg) {
        
    }
    
    @Override
    public void handleMessage(GetScheduleMsg msg) {
        
    }
    
    @Override
    public void handleMessage(InfoMsg msg) {
        
    }
    
    @Override
    public void handleMessage(RegistrationMsg msg) {
        
    }

    @Override
    public void handleMessage(ResourceMsg msg) {
        
    }
    
    @Override
    public void handleMessage(SendMessageMsg msg) {
        
    }
}
