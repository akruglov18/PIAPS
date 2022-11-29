package com.unn.piap_serverside.net_protocol;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NP_InfoPacket implements NetPackage.NetMessageInterface {
    public String info;
    
    
    public static boolean isCorrect(NP_InfoPacket infoPacket) {
        if (infoPacket == null)
            return false;
        return infoPacket.info != null;
    }

    @Override
    public NetPackage.NET_MSG_TYPE getMessageType() {
        return NetPackage.NET_MSG_TYPE.INFO;
    }

    @Override
    public NetPackage.COMMANDS_LIST getCommandType() {
        return NetPackage.COMMANDS_LIST.NONE;
    }

    @Override
    public String convertToJson(Gson serializer) {
        return serializer.toJson(this);
    }
}
