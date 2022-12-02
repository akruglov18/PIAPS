/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.net_protocol;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;

/**
 *
 * @author STALKER
 */
@AllArgsConstructor
public class DB_MsgRecord {
    public String loginFrom;
    public String loginTo;
    public Timestamp timestamp;  // null for request
    public String theme;
    public String body;
}
