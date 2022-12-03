/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.data_types;

import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;
import com.unn.user_core.net_protocol.NP_ChangeRequestStatus;
import com.unn.user_core.net_protocol.DB_RequestRecord;

/**
 *
 * @author acer
 */
@AllArgsConstructor
public class ChangeRequestStatusMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public String requestUUID;
    public DB_RequestRecord.REQ_STATUS newStatus;
    
    // RESPONSE
    public NP_ChangeRequestStatus.RESPONSE_TYPE respType;
}
