/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.data_types;

import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;
import com.unn.user_core.net_protocol.DB_RequestRecord;
import com.unn.user_core.net_protocol.NP_CreateRequestPacket;

/**
 *
 * @author acer
 */
@AllArgsConstructor
public class CreateRequestMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public DB_RequestRecord rr;
    
    // RESPONSE
    public NP_CreateRequestPacket.RESPONSE_TYPE respType;
    public DB_RequestRecord.REQ_STATUS requestStatus;
}
