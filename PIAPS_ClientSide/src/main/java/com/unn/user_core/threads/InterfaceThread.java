/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.threads;

import com.unn.user_core.data_types.UCM;
import com.unn.user_core.interfaces.UCMI;
import com.unn.user_core.interfaces.UIMI;
import com.unn.user_core.UimParser;
import com.unn.user_core.InterfaceUimMessageProcessor;

/**
 *
 * @author STALKER
 */
public class InterfaceThread extends ThreadBase{
    private final UCMI router;
    private UimParser uimParser;
    InterfaceUimMessageProcessor processor;
    private final UIMI ui;
    
    public InterfaceThread(UCMI router, UIMI ui) {
        super(UCM.TID.INTERFACE_THREAD);
        this.ui = ui;
        this.router = router;
        processor = new InterfaceUimMessageProcessor(ui);
        uimParser = new UimParser(processor);
    }
    

    @Override
    protected void terminationCallback() {
        // send ui tremination message
    }

    @Override
    protected boolean handlePersonalMessage(UCM msg) {
        if (msg.from.equals(UCM.TID.USER_INTERFACE)) {
            msg.from = UCM.TID.INTERFACE_THREAD;
            msg.to = UCM.TID.SOCKET_MANAGER_THREAD;
            router.sendMessage(msg);
            return true;
        } else if (msg.from.equals(UCM.TID.SOCKET_MANAGER_THREAD)) {
            uimParser.parseMsg(msg.body);
            return true;
        }
        return false;
    }
}
