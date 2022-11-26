/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside.net_protocol;

import com.google.gson.Gson;

/**
 *
 * @author STALKER
 */
public class NP_InfoPacket implements NetPackage.NetMessageInterface {
    public String info;
    
    public NP_InfoPacket() {
        this.info = null;
    }
    
    public NP_InfoPacket(String info) {
        this.info = info;
    }
    
    public NP_InfoPacket(NP_InfoPacket np_ip) {
        this.info = np_ip.info;
    }
    
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
