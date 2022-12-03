package com.unn.user_core.data_types;

import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;
import com.unn.user_core.net_protocol.NP_RegistrationPacket;

@AllArgsConstructor
public class RegistrationMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public String login;
    public String password;
    public String fio;
    public NP_RegistrationPacket.USER_TYPE userType;
    
    // RESPONSE
    public NP_RegistrationPacket.RESPONSE_TYPE respType;
}
