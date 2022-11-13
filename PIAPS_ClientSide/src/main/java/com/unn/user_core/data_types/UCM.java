/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.data_types;

/**
 * UserCoreMessage
 * @author STALKER
 */
public class UCM {
    public enum TID {
        THREAD_BASE,
        GLOBAL,
        ROUTER_THREAD,
        INTERFACE_THREAD,
        SHUTDOWN_HOOK_THREAD,
        SOCKET_MANAGER_THREAD,
        TX_THREAD,
        RX_THREAD,
        USER_INTERFACE,
        
        
        TEST_THREAD
    }
    
    public enum TYPE {
        NONE,
        GLOBAL_HANDSHAKE,
        GLOBAL_TERMINATE,
        
        IF_THD_UI_MSG  // Interface Thread UI message
    }
    
    
    public UCM.TID from;
    public UCM.TID to;
    public UCM.TYPE type;
    public Object body;
    
    public UCM() {
        from = to = TID.THREAD_BASE;
        type = TYPE.NONE;
        body = null;
    }
    
    public UCM(UCM c) {
        from = c.from;
        to = c.to;
        type = c.type;
        body = c.body;
    }
    
    public static UCM nm() {  // new message
        return new UCM();
    }
    
    public UCM setTo(TID to) {
        this.to = to;
        return this;
    }
    
    public UCM setFrom(TID from) {
        this.from = from;
        return this;
    }
    
    public UCM setType(TYPE type) {
        this.type = type;
        return this;
    }
    
    public UCM setBody(Object body) {
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
