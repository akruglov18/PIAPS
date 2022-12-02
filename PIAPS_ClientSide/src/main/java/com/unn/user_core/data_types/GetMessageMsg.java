/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.data_types;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;

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
    public GetMessageMsg.RESPONSE_TYPE respType;
    public ArrayList<DB_MsgRecord> records;

    public static enum RESPONSE_TYPE {
        OK,
        ERROR_INTERNAL_SERVER_ERROR,
        ERROR_NOT_AUTHORIZED,
        ERROR_USER_LOGIN_NOT_FOUND,
        ERROR_SENDER_LOGIN_NOT_FOUND,
        ERROR_WRONG_LOGIN
    }
}
