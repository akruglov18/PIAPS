/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.command_line_interface;

import com.unn.user_core.UserCore;
import com.unn.user_core.interfaces.UIMI;

/**
 *
 * @author STALKER
 */
public class CLInputThread {
    
    public static void main(String[] args) {
        //CLOutputThread comLineOutThrd = new CLOutputThread();
        //UserCore userCore = new UserCore(comLineOutThrd);
        
        UIMI comLineOutThrd = new CLOutputThread();
        UIMI userCore = new UserCore(comLineOutThrd);
        
        
        comLineOutThrd.startModule();
        userCore.startModule();
        
        // while true cin comm and
        //userCoreMI.sendMessage(msg);
        //
        
        
    }
    
}
