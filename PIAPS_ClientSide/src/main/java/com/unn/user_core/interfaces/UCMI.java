/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.unn.user_core.interfaces;

import com.unn.user_core.data_types.UCM;

/**
 * User Core Message Interface
 * @author STALKER
 */
public interface UCMI {
    public void sendMessage(UCM msg);
    public UCM.TID getThreadID();
    //public void startThread();
}
