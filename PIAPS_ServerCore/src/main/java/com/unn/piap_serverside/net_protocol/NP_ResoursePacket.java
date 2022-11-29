package com.unn.piap_serverside.net_protocol;

import com.google.gson.Gson;
import java.util.ArrayList;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NP_ResoursePacket implements NetPackage.NetMessageInterface {
    public boolean isRequest;
    
    // REQUEST

    // RESPONSE
    public NP_ResoursePacket.RESPONSE_TYPE respType;
    public ArrayList<DB_ResourseRecord> records;
    
    
    public static boolean isCorrect(NP_ResoursePacket rp) {
        if (rp == null)
            return false;
        if (rp.isRequest)
            return true;
        if (rp.respType == null)
            return false;
        if (rp.respType == RESPONSE_TYPE.ERROR_NOT_AUTHORIZED ||
                rp.respType == RESPONSE_TYPE.ERROR_INTERNAL_SERVER_ERROR) {
            return true;
        }
        return rp.records != null;
    }
    
    public static enum RESPONSE_TYPE {
        OK,
        ERROR_INTERNAL_SERVER_ERROR,
        ERROR_NOT_AUTHORIZED
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
        return NetPackage.COMMANDS_LIST.GET_RESOURSES;
    }

    @Override
    public String convertToJson(Gson serializer) {
        return serializer.toJson(this);
    }
}
