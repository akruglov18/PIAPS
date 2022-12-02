/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.net_protocol;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;

/**
 *
 * @author STALKER
 */
@AllArgsConstructor
public class NP_CreateRequestPacket implements NetPackage.NetMessageInterface {
    public boolean isRequest;
    
    // REQUEST
    public DB_RequestRecord rr;
    
    // RESPONSE
    public NP_CreateRequestPacket.RESPONSE_TYPE respType;
    public DB_RequestRecord.REQ_STATUS requestStatus;
    
    
    public static boolean isCorrect(NP_CreateRequestPacket crp) {
        if (crp == null)
            return false;
        if (crp.isRequest) {
            // FOR REQUEST ONLY reqUUID ANS status CAN BE null
            if (crp.rr == null)
                return false;
            if (crp.rr.login == null)
                return false;
        } else {
            if (crp.respType == null)
                return false;
            if (crp.respType == RESPONSE_TYPE.OK && crp.requestStatus == null)
                return false;
        }
        return true;
    }

    public static enum RESPONSE_TYPE {
        OK,
        ERROR_INTERNAL_SERVER_ERROR,
        ERROR_NOT_AUTHORIZED,
        ERROR_USER_LOGIN_NOT_FOUND,
        ERROR_REQUEST_UUID_NOT_FOUND,
        ERROR_WRONG_LOGIN
    }
    
    @Override
    public NetPackage.NET_MSG_TYPE getMessageType() {
        if (isRequest)
            return NetPackage.NET_MSG_TYPE.REQUEST;
        else
            return NetPackage.NET_MSG_TYPE.RESPONSE;
    }

    @Override
    public NetPackage.COMMANDS_LIST getCommandType() {
        return NetPackage.COMMANDS_LIST.CREATE_REQUEST;
    }

    @Override
    public String convertToJson(Gson serializer) {
        return serializer.toJson(this);
    }
}
