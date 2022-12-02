/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core;

import com.unn.user_core.net_protocol.*;

/**
 *
 * @author acer
 */
public class NetMessageProcessor implements NetPackage.DeserializeCallbackInterface {
    
    @Override
    public void deserializationError(Class<?> errClass, String errorStr) {
        
    }
    
    @Override
    public void np_infoPacketAcquired(NP_InfoPacket infoPacket) {
        
    }

    @Override
    public void np_registrationPacketAcquired(NP_RegistrationPacket rrp) {
        
    }
    
    @Override
    public void np_authorizationPacketAcquired(NP_AuthorizationPacket ap) {
        
    }
    
    @Override
    public void np_resoursePacketAcquired(NP_ResoursePacket rp) {
        
    }
    
    @Override
    public void np_sendMsgPacketAcquired(NP_SendMsgPacket smsgp) {
        
    }
    
    @Override
    public void np_getMsgPacketAcquired(NP_GetMsgPacket gmsgp) {
        
    }
    
    @Override
    public void np_createRequestPacketAcquired(NP_CreateRequestPacket crp) {
        
    }
    
    @Override
    public void np_getSchedulePacketAcquired(NP_GetSchedulePacket gsp) {
        
    }
    
    @Override
    public void np_changeReqStatusPacketAcquired(NP_ChangeRequestStatus crp) {
        
    }
}
