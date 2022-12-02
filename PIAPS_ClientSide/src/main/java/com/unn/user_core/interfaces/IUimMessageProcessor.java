/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.interfaces;

import com.unn.user_core.data_types.*;

/**
 *
 * @author acer
 */
public interface IUimMessageProcessor {
    public void handleMessage(AuthorizationMsg msg);
    public void handleMessage(ChangeRequestStatusMsg msg);
    public void handleMessage(CreateRequestMsg msg);
    public void handleMessage(GetMessageMsg msg);
    public void handleMessage(GetScheduleMsg msg);
    public void handleMessage(InfoMsg msg);
    public void handleMessage(RegistrationMsg msg);
    public void handleMessage(ResourceMsg msg);
    public void handleMessage(SendMessageMsg msg);
}
