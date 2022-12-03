package com.unn.user_core.data_types;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;
import com.unn.user_core.net_protocol.NP_ResoursePacket;

import com.unn.user_core.net_protocol.DB_ResourseRecord;

@AllArgsConstructor
public class ResourceMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST

    // RESPONSE
    public NP_ResoursePacket.RESPONSE_TYPE respType;
    public ArrayList<DB_ResourseRecord> records;
}
