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
public class CreateRequestMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public DB_RequestRecord rr;
    
    // RESPONSE
    public CreateRequestMsg.RESPONSE_TYPE respType;
    public DB_RequestRecord.REQ_STATUS requestStatus;

    public static enum RESPONSE_TYPE {
        OK,
        ERROR_INTERNAL_SERVER_ERROR,
        ERROR_NOT_AUTHORIZED,
        ERROR_USER_LOGIN_NOT_FOUND,
        ERROR_REQUEST_UUID_NOT_FOUND,
        ERROR_WRONG_LOGIN
    }
}
