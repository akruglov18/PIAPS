/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.data_types;

import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;

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
    public ChangeRequestStatusMsg.RESPONSE_TYPE respType;
    
    public static enum RESPONSE_TYPE {
        OK,
        ERROR_INTERNAL_SERVER_ERROR,
        ERROR_NOT_AUTHORIZED,
        ERROR_REQUEST_NOT_FOUND,
        ERROR_ACCESS_DENIED
    }
}
