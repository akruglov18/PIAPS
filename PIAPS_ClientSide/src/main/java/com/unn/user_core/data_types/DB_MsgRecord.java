/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.data_types;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;

/**
 *
 * @author acer
 */
@AllArgsConstructor
public class DB_MsgRecord {
    public String loginFrom;
    public String loginTo;
    public Timestamp timestamp;  // null for request
    public String theme;
    public String body;
}
