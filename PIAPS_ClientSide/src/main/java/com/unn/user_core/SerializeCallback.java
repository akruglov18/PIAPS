/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core;

import com.unn.user_core.net_protocol.NetPackage;

/**
 *
 * @author acer
 */
public class SerializeCallback implements NetPackage.SerializeCallbackInterface {
    @Override
    public void serializationError(String errStr) {
        Log.error("Serialization error: " + errStr);
    }
}
