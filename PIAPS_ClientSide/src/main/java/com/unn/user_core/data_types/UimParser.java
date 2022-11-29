/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.user_core.data_types;

import com.unn.user_core.Log;
import com.unn.user_core.interfaces.IUimMessageProcessor;

/**
 *
 * @author acer
 */
public class UimParser
{
    IUimMessageProcessor processor;
   
    public UimParser (IUimMessageProcessor processor)
    {
       this.processor = processor;
    }
    
    public void parseMsg(Object message)
    {
        if (processor == null)
            return;

        if (message.getClass().getName().equals(AuthorizationMsg.class.getName()))
        {
            AuthorizationMsg msg = (AuthorizationMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(AvailableResourcesMsg.class.getName()))
        {
            AvailableResourcesMsg msg = (AvailableResourcesMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(BrowseNotificationsMsg.class.getName()))
        {
            BrowseNotificationsMsg msg = (BrowseNotificationsMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(EditResourceMsg.class.getName()))
        {
            EditResourceMsg msg = (EditResourceMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(RegistrationMsg.class.getName()))
        {
            RegistrationMsg msg = (RegistrationMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(RequestsListMsg.class.getName()))
        {
            RequestsListMsg msg = (RequestsListMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(ScheduleMsg.class.getName()))
        {
            ScheduleMsg msg = (ScheduleMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        Log.error("UIMParser: unexpected message of type " + message.getClass().getName());
    }
}
