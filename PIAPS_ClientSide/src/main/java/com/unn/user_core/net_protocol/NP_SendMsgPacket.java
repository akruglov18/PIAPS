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
public class NP_SendMsgPacket implements NetPackage.NetMessageInterface {
    public boolean isRequest;
    
    // REQUEST
    public DB_MsgRecord msg;
    
    // RESPONSE
    public NP_SendMsgPacket.RESPONSE_TYPE respType;
    
    
    public static boolean isCorrect(NP_SendMsgPacket msgp) {
        if (msgp == null)
            return false;
        if (msgp.isRequest) {
            if (msgp.msg == null)
                return false;
            if (msgp.msg.loginFrom == null ||
                msgp.msg.loginTo == null ||
                msgp.msg.theme == null ||
                msgp.msg.body == null)
                return false;
        } else {
            if (msgp.respType == null)
                return false;
        }
        return true;
    }
    
    public static enum RESPONSE_TYPE {
        OK,
        ERROR_INTERNAL_SERVER_ERROR,
        ERROR_NOT_AUTHORIZED,
        ERROR_LOGIN_FROM_NOT_FOUND,
        ERROR_LOGIN_TO_NOT_FOUND,
        ERROR_WRONG_LOGIN_FROM,
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
        return NetPackage.COMMANDS_LIST.SEND_MESSAGE;
    }

    @Override
    public String convertToJson(Gson serializer) {
        return serializer.toJson(this);
    }
}
