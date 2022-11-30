/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside.net_protocol;

import com.google.gson.Gson;
import java.util.ArrayList;
import lombok.AllArgsConstructor;

/**
 *
 * @author STALKER
 */
@AllArgsConstructor
public class NP_GetMsgPacket implements NetPackage.NetMessageInterface {
    public boolean isRequest;
    
    // REQUEST
    public String login;
    
    // RESPONSE
    public NP_GetMsgPacket.RESPONSE_TYPE respType;
    public ArrayList<DB_MsgRecord> records;
    
    
    public static boolean isCorrect(NP_GetMsgPacket gmsgp) {
        if (gmsgp == null)
            return false;
        if (gmsgp.isRequest) {
            if (gmsgp.login == null)
                return false;
        } else {
            if (gmsgp.respType == null)
                return false;
            if (gmsgp.respType == NP_GetMsgPacket.RESPONSE_TYPE.OK && gmsgp.records == null)
                return false;
        }
        return true;
    }

    public static enum RESPONSE_TYPE {
        OK,
        ERROR_INTERNAL_SERVER_ERROR,
        ERROR_NOT_AUTHORIZED,
        ERROR_USER_LOGIN_NOT_FOUND,
        ERROR_SENDER_LOGIN_NOT_FOUND,
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
        return NetPackage.COMMANDS_LIST.GET_MESSAGES;
    }

    @Override
    public String convertToJson(Gson serializer) {
        return serializer.toJson(this);
    }
}
