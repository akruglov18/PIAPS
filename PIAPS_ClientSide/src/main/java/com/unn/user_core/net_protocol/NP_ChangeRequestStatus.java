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
public class NP_ChangeRequestStatus implements NetPackage.NetMessageInterface {
    public boolean isRequest;
    
    // REQUEST
    public String requestUUID;
    public DB_RequestRecord.REQ_STATUS newStatus;
    
    // RESPONSE
    public NP_ChangeRequestStatus.RESPONSE_TYPE respType;
    
    
    public static boolean isCorrect(NP_ChangeRequestStatus crs) {
        if (crs == null)
            return false;
        if (crs.isRequest) {
            if (crs.requestUUID == null)
                return false;
            if (crs.newStatus == null)
                return false;
        } else {
            if (crs.respType == null)
                return false;
        }
        return true;
    }
    
    public static enum RESPONSE_TYPE {
        OK,
        ERROR_INTERNAL_SERVER_ERROR,
        ERROR_NOT_AUTHORIZED,
        ERROR_REQUEST_NOT_FOUND,
        ERROR_ACCESS_DENIED
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
        return NetPackage.COMMANDS_LIST.CHANGE_REQ_STATUS;
    }

    @Override
    public String convertToJson(Gson serializer) {
        return serializer.toJson(this);
    }
}
