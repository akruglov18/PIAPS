/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.data_types;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;
import com.unn.user_core.net_protocol.DB_MsgRecord;
import com.unn.user_core.net_protocol.NP_GetMsgPacket;

/**
 *
 * @author acer
 */
@AllArgsConstructor
public class GetMessageMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public String login;
    
    // RESPONSE
    public NP_GetMsgPacket.RESPONSE_TYPE respType;
    public ArrayList<DB_MsgRecord> records;
}
