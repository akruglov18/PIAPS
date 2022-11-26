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
        UI_THREAD,
        UI_CHANGER_THREAD
    }
    
    public enum TYPE {
        NONE,
        GLOBAL_HANDSHAKE,
        GLOBAL_TERMINATE,
        
        SMT_SET_PORT,
        SMT_START_LISTENING,
        SMT_STOP_LISTENING,

        SCT_ADD_NEW_CLIENT
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
