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
public class NP_RegistrationRequestPacket implements NetPackage.NetMessageInterface {
    public String login;
    public String password;
    
    public NP_RegistrationRequestPacket(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    public NP_RegistrationRequestPacket(NP_RegistrationRequestPacket np_rrp) {
        this.login = np_rrp.login;
        this.password = np_rrp.password;
    }
    
    public static boolean isCorrect(NP_RegistrationRequestPacket regReqPc) {
        if (regReqPc == null)
            return false;
        if (regReqPc.login == null)
            return false;
        if (regReqPc.password == null)
            return false;
        return true;
    }

    @Override
    public NetPackage.NET_MSG_TYPE getMessageType() {
        return NetPackage.NET_MSG_TYPE.REQUEST;
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
