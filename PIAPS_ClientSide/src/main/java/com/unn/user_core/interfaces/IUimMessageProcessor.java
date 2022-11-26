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
    public void handleMessage(AvailableResourcesMsg msg);
    public void handleMessage(BrowseNotificationsMsg msg);
    public void handleMessage(EditResourceMsg msg);
    public void handleMessage(RegistrationMsg msg);
    public void handleMessage(RequestsListMsg msg);
    public void handleMessage(ScheduleMsg msg);
}
