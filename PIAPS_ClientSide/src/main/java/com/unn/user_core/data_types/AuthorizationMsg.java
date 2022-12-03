package com.unn.user_core.data_types;

import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;
import com.unn.user_core.net_protocol.NP_AuthorizationPacket;

@AllArgsConstructor
public class AuthorizationMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public String login;
    public String password;
    
    // RESPONSE
    public NP_AuthorizationPacket.RESPONSE_TYPE respType;
}
