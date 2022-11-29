package com.unn.piap_serverside.net_protocol;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NP_AuthorizationPacket implements NetPackage.NetMessageInterface {
    public boolean isRequest;
    
    // REQUEST
    public String login;
    public String password;
    
    // RESPONSE
    public NP_AuthorizationPacket.RESPONSE_TYPE respType;

    
    public static enum RESPONSE_TYPE {
        AUTHORIZED,
        ERROR_WRONG_PASSWORD,
        ERROR_USER_NOT_FOUND,
        ERROR_INTERNAL_SERVER_ERROR
    }
    
    public static boolean isCorrect(NP_AuthorizationPacket ap) {
        if (ap == null)
            return false;
        if (ap.isRequest) {
            if (ap.login == null || ap.password == null)
                return false;
        } else {
            if (ap.respType == null)
                return false;
        }
        return true;
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
        return NetPackage.COMMANDS_LIST.AUTHORIZE;
    }

    @Override
    public String convertToJson(Gson serializer) {
        return serializer.toJson(this);
    }
}
