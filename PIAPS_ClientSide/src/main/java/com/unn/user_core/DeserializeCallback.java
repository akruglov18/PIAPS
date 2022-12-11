/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core;
import com.unn.user_core.interfaces.UCMI;
import com.unn.user_core.net_protocol.*;
import com.unn.user_core.data_types.*;
import com.unn.user_core.interfaces.IUimMessage;

/**
 *
 * @author acer
 */
public class DeserializeCallback implements NetPackage.DeserializeCallbackInterface{
    private final UCMI router;
    
    public DeserializeCallback(UCMI router) {
        this.router = router;
    }
    
    private void sendMessage(IUimMessage msg) {
        UCM cmsg = UCM.nm()
                .setTo(UCM.TID.INTERFACE_THREAD)
                .setFrom(UCM.TID.SOCKET_MANAGER_THREAD)
                .setType(UCM.TYPE.IF_THD_UI_MSG)
                .setBody(msg);
        router.sendMessage(cmsg);
    }

    @Override
    public void deserializationError(Class<?> errClass, String errorStr) {
        Log.error(errorStr);
    }
    
    @Override
    public void np_infoPacketAcquired(NP_InfoPacket packet) {
        InfoMsg msg = new InfoMsg(packet.info);
        sendMessage(msg);
    }

    @Override
    public void np_registrationPacketAcquired(NP_RegistrationPacket packet) {
        RegistrationMsg msg = 
                new RegistrationMsg(packet.isRequest, packet.login, 
                                          packet.password, packet.fio, packet.userType, packet.respType);
        sendMessage(msg);
    }
    
    @Override
    public void np_authorizationPacketAcquired(NP_AuthorizationPacket packet) {
        AuthorizationMsg msg = 
                new AuthorizationMsg(packet.isRequest, packet.login, 
                                      packet.password, packet.respType);
        sendMessage(msg);
    }
    
    @Override
    public void np_resoursePacketAcquired(NP_ResoursePacket packet) {
        ResourceMsg msg = new ResourceMsg(packet.isRequest,
                                                         packet.respType, packet.records);
        sendMessage(msg);
    }
    
    @Override
    public void np_sendMsgPacketAcquired(NP_SendMsgPacket packet) {
        SendMessageMsg msg = new SendMessageMsg(packet.isRequest,
                                                         packet.msg, packet.respType);
        sendMessage(msg);
    }
    
    @Override
    public void np_getMsgPacketAcquired(NP_GetMsgPacket packet) {
        GetMessageMsg msg = new GetMessageMsg(packet.isRequest, packet.login, 
                                                      packet.respType, packet.records);
        sendMessage(msg);
    }

    @Override
    public void np_createRequestPacketAcquired(NP_CreateRequestPacket packet) {
        CreateRequestMsg msg = new CreateRequestMsg(packet.isRequest, packet.rr, 
                                                packet.respType, packet.requestStatus);
        sendMessage(msg);
    }
    
    @Override
    public void np_getSchedulePacketAcquired(NP_GetSchedulePacket packet) {
        GetScheduleMsg msg = new GetScheduleMsg(packet.isRequest, packet.year, packet.month, 
                                                    packet.day,packet.loginSearchFor, 
                                                    packet.reqStat,packet.respType, packet.records);
        sendMessage(msg);
    }
    
    @Override
    public void np_changeReqStatusPacketAcquired(NP_ChangeRequestStatus packet) {
        ChangeRequestStatusMsg msg = new ChangeRequestStatusMsg(packet.isRequest, packet.requestUUID, 
                                                                   packet.newStatus, packet.respType);
        sendMessage(msg);
    }
}
