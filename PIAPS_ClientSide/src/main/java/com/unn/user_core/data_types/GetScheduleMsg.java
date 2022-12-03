/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.data_types;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;
import com.unn.user_core.net_protocol.DB_RequestRecord;
import com.unn.user_core.net_protocol.NP_GetSchedulePacket;

/**
 *
 * @author acer
 */
@AllArgsConstructor
public class GetScheduleMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public int year;
    public int month;
    public int day;
    public String loginSearchFor;  // if == null -> search for everyone (COORDINATOR ONLY)
    public ArrayList<DB_RequestRecord.REQ_STATUS> reqStat;  // requested statuses
    
    // RESPONSE
    public NP_GetSchedulePacket.RESPONSE_TYPE respType;
    public ArrayList<DB_RequestRecord> records;
}
