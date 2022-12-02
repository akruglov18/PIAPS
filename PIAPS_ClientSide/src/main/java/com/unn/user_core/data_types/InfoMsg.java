package com.unn.user_core.data_types;

import lombok.AllArgsConstructor;
import com.unn.user_core.interfaces.IUimMessage;

@AllArgsConstructor
public class InfoMsg implements IUimMessage {
    public String info;
}
