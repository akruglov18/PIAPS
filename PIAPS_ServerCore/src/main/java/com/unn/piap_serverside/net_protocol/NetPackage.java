package com.unn.piap_serverside.net_protocol;

import com.google.gson.Gson;

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
            case REQUEST, RESPONSE -> cmdDeserialization(msg);
            default -> deserClbk.deserializationError(NetPackage.class, "Deserialization unknown net message type: " + msg.type.name());
        }
    }
    
    private void cmdDeserialization(NetMessage msg) {
        /* <Add here more handlers> */
        switch(msg.cmd) {
            case REGISTER -> {
                NP_RegistrationPacket rrp = gson.fromJson(msg.body, NP_RegistrationPacket.class);
                if (!NP_RegistrationPacket.isCorrect(rrp)) {
                    deserClbk.deserializationError(NP_RegistrationPacket.class, NP_RegistrationPacket.class.getName() + " deserialization failure");
                    return;
                }
                deserClbk.np_registrationPacketAcquired(rrp);
            }
            
            case AUTHORIZE -> {
                NP_AuthorizationPacket ap = gson.fromJson(msg.body, NP_AuthorizationPacket.class);
                if (!NP_AuthorizationPacket.isCorrect(ap)) {
                    deserClbk.deserializationError(NP_AuthorizationPacket.class, NP_AuthorizationPacket.class.getName() + " deserialization failure");
                    return;
                }
                deserClbk.np_authorizationPacketAcquired(ap);
            }
            
            case GET_RESOURSES -> {
                NP_ResoursePacket rp = gson.fromJson(msg.body, NP_ResoursePacket.class);
                if (!NP_ResoursePacket.isCorrect(rp)) {
                    deserClbk.deserializationError(NP_ResoursePacket.class, NP_ResoursePacket.class.getName() + " deserialization failure");
                    return;
                }
                deserClbk.np_resoursePacketAcquired(rp);
            }
            
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
        AUTHORIZE,
        GET_RESOURSES
        
        /* </Add here more commands> */
    }
    
    public static interface DeserializeCallbackInterface {
        void deserializationError(Class<?> errClass, String errorStr);
        void np_infoPacketAcquired(NP_InfoPacket infoPacket);
        
        /* <Add here more deserialization callbacks> */
        void np_registrationPacketAcquired(NP_RegistrationPacket rrp);
        void np_authorizationPacketAcquired(NP_AuthorizationPacket ap);
        void np_resoursePacketAcquired(NP_ResoursePacket rp);
        
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
        public void np_registrationPacketAcquired(NP_RegistrationPacket rrp) {
            
        }

        @Override
        public void np_authorizationPacketAcquired(NP_AuthorizationPacket ap) {
            
        }

        @Override
        public void np_resoursePacketAcquired(NP_ResoursePacket rp) {
            
        }
    }
    
    private static class DefaultSerializationCallback implements SerializeCallbackInterface {

        @Override
        public void serializationError(String errStr) {
            
        }
        
    }
}
