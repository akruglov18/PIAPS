/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.data_types;

import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;

import com.unn.user_core.net_protocol.DB_MsgRecord;
import com.unn.user_core.net_protocol.NP_SendMsgPacket;

/**
 *
 * @author acer
 */
@AllArgsConstructor
public class SendMessageMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public DB_MsgRecord msg;
    
    // RESPONSE
    public NP_SendMsgPacket.RESPONSE_TYPE respType;
}
