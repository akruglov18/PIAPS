/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core;

import com.google.gson.Gson;
import com.unn.user_core.data_types.*;
import com.unn.user_core.interfaces.IUimMessageProcessor;
import com.unn.user_core.interfaces.UIMI;
import com.unn.user_core.threads.TxThread;
import com.unn.user_core.net_protocol.*;

/**
 *
 * @author acer
 */
public class InterfaceUimMessageProcessor implements IUimMessageProcessor {
    private final UIMI ui;
    
    public InterfaceUimMessageProcessor(UIMI ui) {
        this.ui = ui;
    }
    
    @Override
    public void handleMessage(AuthorizationMsg msg) {
        ui.sendMessage(msg);
    }
    
    @Override
    public void handleMessage(ChangeRequestStatusMsg msg) {
        ui.sendMessage(msg);
    }
    
    @Override
    public void handleMessage(CreateRequestMsg msg) {
        ui.sendMessage(msg);
    }
    
    @Override
    public void handleMessage(GetMessageMsg msg) {
        ui.sendMessage(msg);
    }
    
    @Override
    public void handleMessage(GetScheduleMsg msg) {
        ui.sendMessage(msg);
    }
    
    @Override
    public void handleMessage(InfoMsg msg) {
        ui.sendMessage(msg);
    }
    
    @Override
    public void handleMessage(RegistrationMsg msg) {
        ui.sendMessage(msg);
    }

    @Override
    public void handleMessage(ResourceMsg msg) {
        ui.sendMessage(msg);
    }
    
    @Override
    public void handleMessage(SendMessageMsg msg) {
        ui.sendMessage(msg);
    }
}
