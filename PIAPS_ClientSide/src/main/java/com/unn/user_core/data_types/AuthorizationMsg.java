package com.unn.user_core.data_types;

import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;

@AllArgsConstructor
public class AuthorizationMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public String login;
    public String password;
    
    // RESPONSE
    public AuthorizationMsg.RESPONSE_TYPE respType;

    
    public static enum RESPONSE_TYPE {
        AUTHORIZED,
        ERROR_WRONG_PASSWORD,
        ERROR_USER_NOT_FOUND,
        ERROR_INTERNAL_SERVER_ERROR
    }
}
