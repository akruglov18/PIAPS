package com.unn.user_core.data_types;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DB_ResourseRecord {
    public DB_ResourseRecord.TYPE type;
    public String name;
    public int count;
    
    public DB_ResourseRecord(String type, String name, int count) {
        try {
            this.type = DB_ResourseRecord.TYPE.valueOf(type);
        } catch (IllegalArgumentException ex) {
            this.type = null;
        }
        this.name = name;
        this.count = count;
    }
    
    public static enum TYPE {
        AUDIENCES,
        CHAIRS,
        PROJECTORS,
        BOARDS
    }
}
