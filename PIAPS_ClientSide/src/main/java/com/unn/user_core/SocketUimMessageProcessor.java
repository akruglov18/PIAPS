/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core;

import com.google.gson.Gson;
import com.unn.user_core.data_types.*;
import com.unn.user_core.interfaces.IUimMessageProcessor;
import com.unn.user_core.threads.TxThread;
import com.unn.user_core.net_protocol.*;

/**
 *
 * @author acer
 */
public class SocketUimMessageProcessor implements IUimMessageProcessor {
    protected TxThread txThread;
    protected NetPackage netPackage;
    Gson gson;
    
    public SocketUimMessageProcessor(TxThread txThread) {
        this.txThread = txThread;
        gson = new Gson();
        netPackage = new NetPackage(gson, null, new SerializeCallback());
    }
    
    @Override
    public void handleMessage(AuthorizationMsg msg) {
        NP_AuthorizationPacket packet = 
                new NP_AuthorizationPacket(msg.isRequest, msg.login, 
                                            msg.password, msg.respType);
        txThread.addMessage(netPackage.serialize(packet));
    }
    
    @Override
    public void handleMessage(ChangeRequestStatusMsg msg) {
        NP_ChangeRequestStatus packet = new NP_ChangeRequestStatus(msg.isRequest, msg.requestUUID, 
                                                                   msg.newStatus, msg.respType);
        txThread.addMessage(netPackage.serialize(packet));
    }
    
    @Override
    public void handleMessage(CreateRequestMsg msg) {
        NP_CreateRequestPacket packet = new NP_CreateRequestPacket(msg.isRequest, msg.rr, 
                                                                    msg.respType, msg.requestStatus);
        txThread.addMessage(netPackage.serialize(packet));
    }
    
    @Override
    public void handleMessage(GetMessageMsg msg) {
        NP_GetMsgPacket packet = new NP_GetMsgPacket(msg.isRequest, msg.login, 
                                                      msg.respType, msg.records);
        txThread.addMessage(netPackage.serialize(packet));
    }
    
    @Override
    public void handleMessage(GetScheduleMsg msg) {
        NP_GetSchedulePacket packet = new NP_GetSchedulePacket(msg.isRequest, msg.year, msg.month, 
                                                                msg.day,msg.loginSearchFor, 
                                                                msg.reqStat,msg.respType, msg.records);
        txThread.addMessage(netPackage.serialize(packet));
    }
    
    @Override
    public void handleMessage(InfoMsg msg) {
        NP_InfoPacket packet = new NP_InfoPacket(msg.info);
        txThread.addMessage(netPackage.serialize(packet));
    }
    
    @Override
    public void handleMessage(RegistrationMsg msg) {
        NP_RegistrationPacket packet = 
                new NP_RegistrationPacket(msg.isRequest, msg.login, 
                                          msg.password, msg.fio, msg.userType, msg.respType);
        txThread.addMessage(netPackage.serialize(packet));
    }

    @Override
    public void handleMessage(ResourceMsg msg) {
        NP_ResoursePacket packet = new NP_ResoursePacket(msg.isRequest,
                                                         msg.respType, msg.records);
        txThread.addMessage(netPackage.serialize(packet));
    }
    
    @Override
    public void handleMessage(SendMessageMsg msg) {
        NP_SendMsgPacket packet = new NP_SendMsgPacket(msg.isRequest,
                                                         msg.msg, msg.respType);
        txThread.addMessage(netPackage.serialize(packet));
    }
}
