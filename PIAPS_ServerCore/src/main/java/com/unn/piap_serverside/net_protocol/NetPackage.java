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
public class NetPackage {
    private final Gson gson;
    private final NetPackage.DeserializeCallbackInterface clbk;
    
    public NetPackage(Gson gson, NetPackage.DeserializeCallbackInterface clbk) {
        this.gson = gson;
        this.clbk = clbk;
    }
    
    public String serialize(NetPackage.NetMessageInterface netMsgIfc) {
        if (netMsgIfc == null) {
            clbk.deserializationError(NetPackage.class, "Serialization of null");
            return null;
        }
        return gson.toJson(
                new NetPackage.NetMessage(
                        netMsgIfc.getMessageType(),
                        netMsgIfc.getCommandType(),
                        netMsgIfc.convertToJson(gson)));
    }
    
    public void deserialize(String json) {
        NetMessage msg = gson.fromJson(json, NetMessage.class);
        if (msg == null) {
            clbk.deserializationError(NetMessage.class, "Error deserializing NetMessage: msg = null");
            return;
        }
        
        if (msg.type == null || msg.cmd == null || msg.body == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error deserializing NetMessage:\n");
            sb.append("msg.NET_MSG_TYPE = ");
            if (msg.type == null)
                sb.append("NULL\n");
            else
                sb.append(msg.type.name()).append("\n");
            sb.append("msg.COMMANDS_LIST = ");
            if (msg.cmd == null)
                sb.append("NULL\n");
            else
                sb.append(msg.cmd.name()).append("\n");
            sb.append("msg.body = ");
            if (msg.body == null)
                sb.append("NULL\n");
            else
                sb.append(msg.body).append("\n");
            clbk.deserializationError(NetMessage.class, sb.toString());
            return;
        }
        
        if (msg.type == NET_MSG_TYPE.INFO) {
            NP_InfoPacket ip = gson.fromJson(msg.body, NP_InfoPacket.class);
            if (!NP_InfoPacket.isCorrect(ip)) {
                clbk.deserializationError(NP_InfoPacket.class, NP_InfoPacket.class.getName() + " deserialization failure");
                return;
            }
            clbk.np_infoPacketAcquired(ip);
            return;
        }
        
        switch(msg.type) {
            /* <Add here more handlers> */
            case REQUEST -> {
                switch(msg.cmd) {
                    case REGISTER -> {
                        NP_RegistrationRequestPacket rrp = gson.fromJson(msg.body, NP_RegistrationRequestPacket.class);
                        if (!NP_RegistrationRequestPacket.isCorrect(rrp)) {
                            clbk.deserializationError(NP_RegistrationRequestPacket.class, NP_RegistrationRequestPacket.class.getName() + " deserialization failure");
                            return;
                        }
                        clbk.np_registrationRequestPacketAcquired(rrp);
                        return;
                    }
                        
                    default -> {
                            
                    }
                }
            }  // case REQUEST
                
            case RESPONSE -> {
                switch(msg.cmd) {
                    
                    default -> {
                            
                    }
                }
            }  // case RESPONSE
            
            /* </Add here more handlers> */    
            default -> clbk.deserializationError(NetPackage.class, "Deserialization unknown net message type: " + msg.type.name());
        }
    }
    
    public /*static*/ enum NET_MSG_TYPE {
        REQUEST,
        RESPONSE,
        INFO
    }
    
    public /*static*/ interface NetMessageInterface {
        public NetPackage.NET_MSG_TYPE getMessageType();
        public NetPackage.COMMANDS_LIST getCommandType();
        public String convertToJson(Gson serializer);
    }
    
    public /*static*/ enum COMMANDS_LIST {
        NONE,
        
        /* <Add here more commands> */
        REGISTER,
        AUTHORIZE
        
        /* </Add here more commands> */
    }
    
    public /*static*/ interface DeserializeCallbackInterface {
        void deserializationError(Class<?> errClass, String errorStr);
        
        /* <Add here more deserialization callbacks> */
        void np_infoPacketAcquired(NP_InfoPacket infoPacket);
        void np_registrationRequestPacketAcquired(NP_RegistrationRequestPacket rrp);
        
        /* </Add here more deserialization callbacks> */
    }
    
    
    private /*static*/ class NetMessage {
        public NetPackage.NET_MSG_TYPE type;
        public NetPackage.COMMANDS_LIST cmd;
        public String body;
        
        public NetMessage(NetPackage.NET_MSG_TYPE type, NetPackage.COMMANDS_LIST cmd, String body) {
            this.type = type;
            this.cmd = cmd;
            this.body = body;
        }
    }
}
