package com.unn.piap_serverside.data_types;

import java.util.UUID;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NetPackageWrapper {
    public UUID connUUID;
    public Object body;
}
