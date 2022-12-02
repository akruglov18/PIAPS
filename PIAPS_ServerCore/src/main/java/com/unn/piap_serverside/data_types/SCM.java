/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside.data_types;

/**
 * Server Core Message
 * @author STALKER
 */
public class SCM {
    public enum TID {
        THREAD_BASE,
        GLOBAL,
        
        ROUTER_THREAD,
        
        RESOURCE_MANAGER_THREAD,
        
        DB_MANAGER_THREAD,
        
        SHUTDOWN_HOOK_THREAD,
        SOCKET_MANAGER_THREAD,
        SERVER_SOCKET_THREAD,
        CLIENT_TX_THREAD,
        CLIENT_RX_THREAD,
        
        UI_THREAD,
        UI_CHANGER_THREAD
    }
    
    public enum TYPE {
        NONE,
        GLOBAL_HANDSHAKE,
        GLOBAL_TERMINATE,
        
        UIT_SET_PORT,
        UIT_START_LISTENING,
        UIT_STOP_LISTENING,
        UIT_DB_UPDATE_RESOURSES,
        UIT_DB_CONNECT,
        UIT_DB_RESET,
        UIT_SEND_INFO_PACKET_TO_CLIENTS,
        
        TXT_CLIENT_CONNECTION_UNEXPECTED_TERMINATION,

        SST_ADD_NEW_CLIENT,
        
        RXT_UNEXPECTED_TERMINATION,
        RXT_DESERIALIZATION_ERROR,
        RXT_INFO_PACKET_ACQUIRED,
        RXT_RECEIVER_WRONG_CMD_PACKET_TYPE,
        RXT_REGISTRATION_REQUEST_ACQUIRED,
        RXT_AUTHORIZATION_REQUEST_ACQUIRED,
        RXT_RESOURSE_GET_ACQUIRED,
        RXT_SEND_MSG_ACQUIRED,
        RXT_GET_MSG_ACQUIRED,
        RXT_CREATE_REQUEST_ACQUIRED,
        RXT_GET_SCHEDULE_ACQUIRED,
        RXT_CHANGE_REQUEST_STATUS_ACQUIRED,
        
        DBT_UPDATE_DB_STATE,
        DBT_SEND_NP_RESPONSE
    }
    
    public SCM.TID from;
    public SCM.TID to;
    public SCM.TYPE type;
    public Object body;
    
    public SCM() {
        from = to = TID.THREAD_BASE;
        type = TYPE.NONE;
        body = null;
    }
    
    public SCM(SCM c) {
        from = c.from;
        to = c.to;
        type = c.type;
        body = c.body;
    }
    
    public static SCM nm() {
        return new SCM();
    }
    
    public SCM setTo(TID to) {
        this.to = to;
        return this;
    }
    
    public SCM setFrom(TID from) {
        this.from = from;
        return this;
    }
    
    public SCM setType(TYPE type) {
        this.type = type;
        return this;
    }
    
    public SCM setBody(Object body) {
        this.body = body;
        return this;
    }
    
    @Override
    public String toString() {
        return "{From: " + from.name() + 
                " | To: " + to.name() +
                " | Type: " + type.name() + "}";
    }
}
