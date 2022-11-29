/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside.database;

import com.unn.piap_serverside.net_protocol.DB_ResourseRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author STALKER
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ResourceTableRecord {
    private String type;
    private String name;
    private int count;
    
    public ResourceTableRecord(DB_ResourseRecord rr) {
        type = rr.type.name();
        name = rr.name;
        count = rr.count;
    }
}
