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
public class SendMessageMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public DB_MsgRecord msg;
    
    // RESPONSE
    public SendMessageMsg.RESPONSE_TYPE respType;
    
    public static enum RESPONSE_TYPE {
        OK,
        ERROR_INTERNAL_SERVER_ERROR,
        ERROR_NOT_AUTHORIZED,
        ERROR_LOGIN_FROM_NOT_FOUND,
        ERROR_LOGIN_TO_NOT_FOUND,
        ERROR_WRONG_LOGIN_FROM,
        ERROR_ACCESS_DENIED
    }
}
