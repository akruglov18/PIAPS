package com.unn.user_core.net_protocol;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NP_RegistrationPacket implements NetPackage.NetMessageInterface {
    public boolean isRequest;
    
    // REQUEST
    public String login;
    public String password;
    public String fio;
    public NP_RegistrationPacket.USER_TYPE userType;
    
    // RESPONSE
    public NP_RegistrationPacket.RESPONSE_TYPE respType;
    
    
    public enum USER_TYPE {
        COORDINATOR,
        CUSTOMER
    }
    
    public enum RESPONSE_TYPE {
        REGISTERED,
        ERROR_LOGIN_ALREADY_EXISTS,
        ERROR_INTERNAL_SERVER_ERROR
    }
    
    public static boolean isCorrect(NP_RegistrationPacket rp) {
        if (rp == null)
            return false;
        if (rp.isRequest) {
            if (rp.login == null || rp.password == null ||
                    rp.fio == null || rp.userType == null)
                return false;
        } else {
            if (rp.respType == null)
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
        return NetPackage.COMMANDS_LIST.REGISTER;
    }

    @Override
    public String convertToJson(Gson serializer) {
        return serializer.toJson(this);
    }
}
