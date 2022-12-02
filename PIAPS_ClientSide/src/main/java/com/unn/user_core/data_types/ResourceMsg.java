package com.unn.user_core.data_types;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;

@AllArgsConstructor
public class ResourceMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST

    // RESPONSE
    public ResourceMsg.RESPONSE_TYPE respType;
    public ArrayList<DB_ResourseRecord> records;
    
    public static enum RESPONSE_TYPE {
        OK,
        ERROR_INTERNAL_SERVER_ERROR,
        ERROR_NOT_AUTHORIZED
    }
}
