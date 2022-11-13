/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.unn.piap_serverside.interfaces;

import com.unn.piap_serverside.data_types.SCM;

/**
 * Server Core Message Interface
 * @author STALKER
 */
public interface SCMI {
    public void sendMessage(SCM msg);
    public SCM.TID getThreadID();
}
