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
public class GetScheduleMsg implements IUimMessage {
    public boolean isRequest;
    
    // REQUEST
    public int year;
    public int month;
    public int day;
    public String loginSearchFor;  // if == null -> search for everyone (COORDINATOR ONLY)
    public ArrayList<DB_RequestRecord.REQ_STATUS> reqStat;  // requested statuses
    
    // RESPONSE
    public GetScheduleMsg.RESPONSE_TYPE respType;
    public ArrayList<DB_RequestRecord> records;
    
    
    public static enum RESPONSE_TYPE {
        OK,
        ERROR_INTERNAL_SERVER_ERROR,
        ERROR_USER_SEARCH_FOR_NOT_FOUND,
        ERROR_USER_IN_REQUEST_RECORD_NOT_FOUND,
        ERROR_NOT_AUTHORIZED,
        ERROR_ACCESS_DENIED,
        ERROR_WRONG_LOGIN,
        ERROR_REQUESTED_STATUSES_EMPTY
    }
}
