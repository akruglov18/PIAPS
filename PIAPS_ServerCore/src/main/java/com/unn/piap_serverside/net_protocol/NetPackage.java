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
    private final NetPackage.DeserializeCallbackInterface deserClbk;
    private final NetPackage.SerializeCallbackInterface serClbk;
    
    public NetPackage(Gson gson, 
            NetPackage.DeserializeCallbackInterface deserClbk,
            NetPackage.SerializeCallbackInterface serClbk) {
        this.gson = gson;
        
        if (deserClbk == null)
            this.deserClbk = new DefaultDeserializationCallback();
        else
            this.deserClbk = deserClbk;
        if (serClbk == null)
            this.serClbk = new DefaultSerializationCallback();
        else
            this.serClbk = serClbk;
    }
    
    public String serialize(NetPackage.NetMessageInterface netMsgIfc) {
        if (netMsgIfc == null) {
            serClbk.serializationError("Serialization of null");
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
            deserClbk.deserializationError(NetMessage.class, "Error deserializing NetMessage: msg = null");
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
            deserClbk.deserializationError(NetMessage.class, sb.toString());
            return;
        }
        
        switch(msg.type) {
            case INFO -> {
                NP_InfoPacket ip = gson.fromJson(msg.body, NP_InfoPacket.class);
                if (!NP_InfoPacket.isCorrect(ip)) {
                    deserClbk.deserializationError(NP_InfoPacket.class, NP_InfoPacket.class.getName() + " deserialization failure");
                    return;
                }
                deserClbk.np_infoPacketAcquired(ip);
            }
            case REQUEST -> requestsDeserialization(msg);
            case RESPONSE -> responsesDeserialization(msg);
            default -> deserClbk.deserializationError(NetPackage.class, "Deserialization unknown net message type: " + msg.type.name());
        }
    }
    
    private void requestsDeserialization(NetMessage msg) {
        /* <Add here more handlers> */
        switch(msg.cmd) {
            case REGISTER -> {
                NP_RegistrationRequestPacket rrp = gson.fromJson(msg.body, NP_RegistrationRequestPacket.class);
                if (!NP_RegistrationRequestPacket.isCorrect(rrp)) {
                    deserClbk.deserializationError(NP_RegistrationRequestPacket.class, NP_RegistrationRequestPacket.class.getName() + " deserialization failure");
                    return;
                }
                deserClbk.np_registrationRequestPacketAcquired(rrp);
            }
            
            default -> {
            }
        }
        /* </Add here more handlers> */
    }
    
    private void responsesDeserialization(NetMessage msg) {
        /* <Add here more handlers> */
        switch(msg.cmd) {
            
            default -> {
            }
        }
        /* </Add here more handlers> */  
    }
    
    public static enum NET_MSG_TYPE {
        REQUEST,
        RESPONSE,
        INFO
    }
    
    public static interface NetMessageInterface {
        public NetPackage.NET_MSG_TYPE getMessageType();
        public NetPackage.COMMANDS_LIST getCommandType();
        public String convertToJson(Gson serializer);
    }
    
    public static enum COMMANDS_LIST {
        NONE,
        
        /* <Add here more commands> */
        REGISTER,
        AUTHORIZE
        
        /* </Add here more commands> */
    }
    
    public static interface DeserializeCallbackInterface {
        void deserializationError(Class<?> errClass, String errorStr);
        
        /* <Add here more deserialization callbacks> */
        void np_infoPacketAcquired(NP_InfoPacket infoPacket);
        
        // Add more requests callbacks
        void np_registrationRequestPacketAcquired(NP_RegistrationRequestPacket rrp);
        
        
        // Add more responses callbacks
        
        
        /* </Add here more deserialization callbacks> */
    }
    
    public static interface SerializeCallbackInterface {
        void serializationError(String errStr);
    }
    
    private static class NetMessage {
        public NetPackage.NET_MSG_TYPE type;
        public NetPackage.COMMANDS_LIST cmd;
        public String body;
        
        public NetMessage(NetPackage.NET_MSG_TYPE type, NetPackage.COMMANDS_LIST cmd, String body) {
            this.type = type;
            this.cmd = cmd;
            this.body = body;
        }
    }
    
    private static class DefaultDeserializationCallback implements DeserializeCallbackInterface {

        @Override
        public void deserializationError(Class<?> errClass, String errorStr) {
            
        }

        @Override
        public void np_infoPacketAcquired(NP_InfoPacket infoPacket) {
            
        }

        @Override
        public void np_registrationRequestPacketAcquired(NP_RegistrationRequestPacket rrp) {
            
        }
    }
    
    private static class DefaultSerializationCallback implements SerializeCallbackInterface {

        @Override
        public void serializationError(String errStr) {
            
        }
        
    }
}
