/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.data_types;

import lombok.AllArgsConstructor;

/**
 *
 * @author acer
 */
@AllArgsConstructor
public class DB_RequestRecord {
    public String reqUUID;
    public String login;
    public DB_RequestRecord.REQ_STATUS status;
    public int year;
    public int month;
    public int day;
    public int regTimeStart;
    public int regTimeStop;
    public int adtNum;
    public int chairCnt;
    public int projCnt;
    public int boardCnt;
    
    
    public static boolean isReviewNeeded(DB_RequestRecord rr) {
        if (rr.adtNum <= 0)
            return true;
        if (rr.chairCnt < 0 || rr.projCnt < 0 || rr.boardCnt < 0)
            return true;
        if (rr.regTimeStop <= rr.regTimeStart)
            return true;
        if (rr.regTimeStart < 0 || rr.regTimeStop < 0)
            return true;
        if (rr.regTimeStart > 23 || rr.regTimeStop > 23)
            return true;
        return false;
    }
    
    public static enum REQ_STATUS {
        APPROVED,
        AWAITS_REVIEW,
        REVIEWING,
        CANCELLED,
        PASSED
    }
}
