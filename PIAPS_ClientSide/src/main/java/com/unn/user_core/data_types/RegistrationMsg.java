package com.unn.user_core.data_types;

import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;

@AllArgsConstructor
public class RegistrationMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public String login;
    public String password;
    public String fio;
    public RegistrationMsg.USER_TYPE userType;
    
    // RESPONSE
    public RegistrationMsg.RESPONSE_TYPE respType;
    
    
    public enum USER_TYPE {
        COORDINATOR,
        CUSTOMER
    }
    
    public enum RESPONSE_TYPE {
        REGISTERED,
        ERROR_LOGIN_ALREADY_EXISTS,
        ERROR_INTERNAL_SERVER_ERROR
    }
}
