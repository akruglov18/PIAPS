/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.net_protocol;

import com.google.gson.Gson;
import java.util.ArrayList;
import lombok.AllArgsConstructor;

/**
 *
 * @author STALKER
 */
@AllArgsConstructor
public class NP_GetSchedulePacket implements NetPackage.NetMessageInterface {
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
    
    public static boolean isCorrect(NP_GetSchedulePacket gsp) {
        if (gsp == null)
            return false;
        if (gsp.isRequest) {
            if (gsp.reqStat == null)
                return false;
        } else {
            if (gsp.respType == null)
                return false;
            if (gsp.respType == RESPONSE_TYPE.OK && gsp.records == null)
                return false;
        }
        return true;
    }

    @Override
    public NetPackage.NET_MSG_TYPE getMessageType() {
        if (isRequest)
            return NetPackage.NET_MSG_TYPE.REQUEST;
        else
            return NetPackage.NET_MSG_TYPE.RESPONSE;
    }

    @Override
    public NetPackage.COMMANDS_LIST getCommandType() {
        return NetPackage.COMMANDS_LIST.GET_SCHEDULE;
    }

    @Override
    public String convertToJson(Gson serializer) {
        return serializer.toJson(this);
    }
}
