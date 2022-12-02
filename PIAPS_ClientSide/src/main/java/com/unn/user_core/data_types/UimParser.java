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
        
        if (message.getClass().getName().equals(ChangeRequestStatusMsg.class.getName()))
        {
            ChangeRequestStatusMsg msg = (ChangeRequestStatusMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(CreateRequestMsg.class.getName()))
        {
            CreateRequestMsg msg = (CreateRequestMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(GetMessageMsg.class.getName()))
        {
            GetMessageMsg msg = (GetMessageMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(GetScheduleMsg.class.getName()))
        {
            GetScheduleMsg msg = (GetScheduleMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(InfoMsg.class.getName()))
        {
            InfoMsg msg = (InfoMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(RegistrationMsg.class.getName()))
        {
            RegistrationMsg msg = (RegistrationMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        if (message.getClass().getName().equals(ResourceMsg.class.getName()))
        {
            ResourceMsg msg = (ResourceMsg)message;
            processor.handleMessage(msg);
            return;
        }

        if (message.getClass().getName().equals(SendMessageMsg.class.getName()))
        {
            SendMessageMsg msg = (SendMessageMsg)message;
            processor.handleMessage(msg);
            return;
        }
        
        Log.error("UIMParser: unexpected message of type " + message.getClass().getName());
    }
}
