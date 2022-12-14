package com.unn.piap_serverside.threads;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unn.piap_serverside.Log;
import com.unn.piap_serverside.data_types.SCM;
import com.unn.piap_serverside.interfaces.SCMI;
import com.unn.piap_serverside.net_protocol.NP_InfoPacket;
import com.unn.piap_serverside.net_protocol.NP_RegistrationPacket;
import com.unn.piap_serverside.net_protocol.NetPackage;
import com.unn.piap_serverside.data_types.NetPackageWrapper;
import com.unn.piap_serverside.net_protocol.DB_RequestRecord;
import com.unn.piap_serverside.net_protocol.NP_AuthorizationPacket;
import com.unn.piap_serverside.net_protocol.NP_ChangeRequestStatus;
import com.unn.piap_serverside.net_protocol.NP_CreateRequestPacket;
import com.unn.piap_serverside.net_protocol.NP_GetMsgPacket;
import com.unn.piap_serverside.net_protocol.NP_GetSchedulePacket;
import com.unn.piap_serverside.net_protocol.NP_ResoursePacket;
import com.unn.piap_serverside.net_protocol.NP_SendMsgPacket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.UUID;


public class ClientConnection extends ThreadBase implements NetPackage.SerializeCallbackInterface {
    private final Socket client;
    private final SCMI scktMgrThrd;
    private final UUID connUUID;
    private final NetPackage np;
    private final Gson serializer;
    private final RxThread rxThread;
    private BufferedWriter clientOut;
    private boolean serSuccess;
    private boolean isAuth = false;
    private boolean isCoordinator = false;
    private final int portNum;
    private String login = null;
            
    
    // DO NOT CALL .start() if exception occured. Just clear created object
    // Exception means, that it's impossible to get input/output streams from client Socket
    public ClientConnection(UUID connUUID, Socket client, SCMI scktMgrThrd) throws IOException {
        super(SCM.TID.CLIENT_TX_THREAD);
        this.client = client;
        this.scktMgrThrd = scktMgrThrd;
        this.connUUID = connUUID;
        serializer = new GsonBuilder().create();  // change later
        np = new NetPackage(serializer, null, this);
        
        clientOut = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        BufferedReader clientIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
        
        Gson deserializer = new GsonBuilder().create();  // change later
        rxThread = new RxThread(clientIn, deserializer, this, client.getPort());
        portNum = client.getPort();
    }
    
    @Override
    public void start() {
        rxThread.start();
        super.start();
    }
    
    @Override
    protected void terminationCallback() {
        try {
            clientOut.close();
        } catch (IOException ex) {
            Log.error("ClientConnection out.close() ex: " + ex.toString());
        }
        
        rxThread.terminate();
        
        try {
            client.close();
        } catch (IOException ex) {
            Log.error("ClientConnection client.close() ex: " + ex.toString());
        }
    }

    @Override
    protected boolean handlePersonalMessage(SCM msg) {
        switch(msg.type) {
            case DBT_SEND_NP_RESPONSE -> {
                NetPackage.NetMessageInterface np = (NetPackage.NetMessageInterface) msg.body;
                if (np.getCommandType() == NetPackage.COMMANDS_LIST.AUTHORIZE) {
                    NP_AuthorizationPacket ap = (NP_AuthorizationPacket) np;
                    if (ap.isRequest) {
                        Log.error("CLIENT IMPOSSIBLE STATE ACHIEVED");
                    } else {
                        if (ap.respType == NP_AuthorizationPacket.RESPONSE_TYPE.AUTHORIZED) {
                            isAuth = true;
                            if (ap.password.equals(NP_RegistrationPacket.USER_TYPE.COORDINATOR.name()))
                                isCoordinator = true;
                            else
                                isCoordinator = false;
                            login = ap.login;
                        } else {
                            isAuth = false;
                        }
                    }
                }
                txNpToClient(np);
                return true;
            }
            
            case RXT_UNEXPECTED_TERMINATION -> {
                SCM msg1 = SCM.nm()
                        .setFrom(SCM.TID.CLIENT_TX_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.TXT_CLIENT_CONNECTION_UNEXPECTED_TERMINATION)
                        .setBody(connUUID);
                scktMgrThrd.sendMessage(msg1);
                SCM msg2 = SCM.nm()
                        .setFrom(SCM.TID.CLIENT_TX_THREAD)
                        .setTo(SCM.TID.CLIENT_TX_THREAD)
                        .setType(SCM.TYPE.GLOBAL_TERMINATE)
                        .setBody(null);
                this.sendMessage(msg2);
                return true;
            }
            
            case RXT_DESERIALIZATION_ERROR -> {
                String errStr = (String) msg.body;
                NP_InfoPacket ip = new NP_InfoPacket(errStr);
                txNpToClient(ip);
                return true;
            }
            
            case RXT_INFO_PACKET_ACQUIRED -> {
                msg.setTo(SCM.TID.SOCKET_MANAGER_THREAD);
                scktMgrThrd.sendMessage(msg);
                return true;
            }
            
            case RXT_RECEIVER_WRONG_CMD_PACKET_TYPE -> {
                String errStr = (String) msg.body;
                NP_InfoPacket ip = new NP_InfoPacket(errStr);
                txNpToClient(ip);
                return true;
            }
            
            case RXT_REGISTRATION_REQUEST_ACQUIRED -> {
                NetPackageWrapper npw = new NetPackageWrapper(connUUID, msg.body);
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.CLIENT_TX_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.RXT_REGISTRATION_REQUEST_ACQUIRED)
                        .setBody(npw);
                scktMgrThrd.sendMessage(nmsg);
                return true;
            }
            
            case RXT_AUTHORIZATION_REQUEST_ACQUIRED -> {
                NetPackageWrapper npw = new NetPackageWrapper(connUUID, msg.body);
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.CLIENT_TX_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.RXT_AUTHORIZATION_REQUEST_ACQUIRED)
                        .setBody(npw);
                scktMgrThrd.sendMessage(nmsg);
                return true;
            }
            
            case RXT_RESOURSE_GET_ACQUIRED -> {
                if (!isAuth) {
                    NP_ResoursePacket rp = new NP_ResoursePacket(false, NP_ResoursePacket.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED, null);
                    txNpToClient(rp);
                    return true;
                }
                
                NetPackageWrapper npw = new NetPackageWrapper(connUUID, msg.body);
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.CLIENT_TX_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.RXT_RESOURSE_GET_ACQUIRED)
                        .setBody(npw);
                scktMgrThrd.sendMessage(nmsg);
                return true;
            }
            
            case RXT_SEND_MSG_ACQUIRED -> {
                if (!isAuth) {
                    NP_SendMsgPacket smsgp = new NP_SendMsgPacket(false, null, NP_SendMsgPacket.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED);
                    txNpToClient(smsgp);
                    return true;
                }
                
                NP_SendMsgPacket smsp = (NP_SendMsgPacket) msg.body;
                if (!smsp.msg.loginFrom.equals(login)) {
                    NP_SendMsgPacket smsgp = new NP_SendMsgPacket(false, null, NP_SendMsgPacket.RESPONSE_TYPE.ERROR_WRONG_LOGIN_FROM);
                    txNpToClient(smsgp);
                    return true;
                }
                
                if (!isCoordinator) {
                    NP_SendMsgPacket smsgp = new NP_SendMsgPacket(false, null, NP_SendMsgPacket.RESPONSE_TYPE.ERROR_ACCESS_DENIED);
                    txNpToClient(smsgp);
                    return true;
                }
                
                NetPackageWrapper npw = new NetPackageWrapper(connUUID, msg.body);
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.CLIENT_TX_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.RXT_SEND_MSG_ACQUIRED)
                        .setBody(npw);
                scktMgrThrd.sendMessage(nmsg);
                return true;
            }
            
            case RXT_GET_MSG_ACQUIRED -> {
                if (!isAuth) {
                    NP_GetMsgPacket gmsgp = new NP_GetMsgPacket(false, null, NP_GetMsgPacket.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED, null);
                    txNpToClient(gmsgp);
                    return true;
                }
                
                NP_GetMsgPacket gmp = (NP_GetMsgPacket) msg.body;
                if (!gmp.login.equals(login)) {
                    NP_GetMsgPacket gmsgp = new NP_GetMsgPacket(false, null, NP_GetMsgPacket.RESPONSE_TYPE.ERROR_WRONG_LOGIN, null);
                    txNpToClient(gmsgp);
                    return true;
                }
                
                NetPackageWrapper npw = new NetPackageWrapper(connUUID, msg.body);
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.CLIENT_TX_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.RXT_GET_MSG_ACQUIRED)
                        .setBody(npw);
                scktMgrThrd.sendMessage(nmsg);
                return true;
            }
            
            case RXT_CREATE_REQUEST_ACQUIRED -> {
                if (!isAuth) {
                    NP_CreateRequestPacket ncpr = new NP_CreateRequestPacket(false, null,
                            NP_CreateRequestPacket.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED, null);
                    txNpToClient(ncpr);
                    return true;
                }
                
                NP_CreateRequestPacket crp = (NP_CreateRequestPacket) msg.body;
                if (!crp.rr.login.equals(login)) {
                    NP_CreateRequestPacket ncpr = new NP_CreateRequestPacket(false, null,
                            NP_CreateRequestPacket.RESPONSE_TYPE.ERROR_WRONG_LOGIN, null);
                    txNpToClient(ncpr);
                    return true;
                }
                
                NetPackageWrapper npw = new NetPackageWrapper(connUUID, msg.body);
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.CLIENT_TX_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.RXT_CREATE_REQUEST_ACQUIRED)
                        .setBody(npw);
                scktMgrThrd.sendMessage(nmsg);
                return true;
            }
            
            case RXT_GET_SCHEDULE_ACQUIRED -> {
                if (!isAuth) {
                    NP_GetSchedulePacket ngsp = new NP_GetSchedulePacket(false,
                            0, 0, 0, null, null,
                            NP_GetSchedulePacket.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED, null);
                    txNpToClient(ngsp);
                    return true;
                }
                
                NP_GetSchedulePacket gsp = (NP_GetSchedulePacket) msg.body;
                if (gsp.reqStat.isEmpty()) {
                    NP_GetSchedulePacket ngsp = new NP_GetSchedulePacket(false,
                        0, 0, 0, null, null,
                        NP_GetSchedulePacket.RESPONSE_TYPE.ERROR_REQUESTED_STATUSES_EMPTY, null);
                    txNpToClient(ngsp);
                    return true;
                }
                
                if (!isCoordinator) {
                    if (gsp.loginSearchFor == null) {
                        if (gsp.year == 0 && gsp.month == 0 && gsp.day == 0) {
                            NP_GetSchedulePacket ngsp = new NP_GetSchedulePacket(false,
                                0, 0, 0, null, null,
                                NP_GetSchedulePacket.RESPONSE_TYPE.ERROR_ACCESS_DENIED, null);
                            txNpToClient(ngsp);
                            return true;
                        }
                        
                        if (gsp.reqStat.size() != 1 || gsp.reqStat.get(0) != DB_RequestRecord.REQ_STATUS.APPROVED) {
                            NP_GetSchedulePacket ngsp = new NP_GetSchedulePacket(false,
                                0, 0, 0, null, null,
                                NP_GetSchedulePacket.RESPONSE_TYPE.ERROR_ACCESS_DENIED, null);
                            txNpToClient(ngsp);
                            return true;
                        }
                    } else {
                        if (!gsp.loginSearchFor.equals(login)) {
                            NP_GetSchedulePacket ngsp = new NP_GetSchedulePacket(false,
                                0, 0, 0, null, null,
                                NP_GetSchedulePacket.RESPONSE_TYPE.ERROR_WRONG_LOGIN, null);
                            txNpToClient(ngsp);
                            return true;
                        }
                    }
                }
                
                NetPackageWrapper npw = new NetPackageWrapper(connUUID, msg.body);
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.CLIENT_TX_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.RXT_GET_SCHEDULE_ACQUIRED)
                        .setBody(npw);
                scktMgrThrd.sendMessage(nmsg);
                return true;
            }
            
            case RXT_CHANGE_REQUEST_STATUS_ACQUIRED -> {
                if (!isAuth) {
                    NP_ChangeRequestStatus ncrp = new NP_ChangeRequestStatus(false,
                            null, null, NP_ChangeRequestStatus.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED);
                    txNpToClient(ncrp);
                    return true;
                }
                
                if (!isCoordinator) {
                    NP_ChangeRequestStatus ncrp = new NP_ChangeRequestStatus(false,
                            null, null, NP_ChangeRequestStatus.RESPONSE_TYPE.ERROR_ACCESS_DENIED);
                    txNpToClient(ncrp);
                    return true;
                }
                
                NetPackageWrapper npw = new NetPackageWrapper(connUUID, msg.body);
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.CLIENT_TX_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.RXT_CHANGE_REQUEST_STATUS_ACQUIRED)
                        .setBody(npw);
                scktMgrThrd.sendMessage(nmsg);
                return true;
            }
            
            default -> {
                return false;
            }
        }
    }
    
    @Override
    public void run() {
        if (ID == SCM.TID.THREAD_BASE) {
            Log.error("Wrong thread ID init parameter used on thread: " + super.getName() + " on port: " + portNum);
            return;
        }
        
        Log.info(ID.name() + " started on port: " + portNum);
        SCM inputMessage;
        while(running) {
            synchronized(messageQueue) {
                if(messageQueue.isEmpty()) {
                    try {
                        messageQueue.wait();
                    } catch (InterruptedException ex) {
                        Log.error(ID.name() + " got exception: " + ex.toString() + " on port: " + portNum);
                    }
                }
                inputMessage = messageQueue.removeLast();
            }
            handleMessage(inputMessage);
        }
    }
    
    @Override
    protected void handleMessage(SCM msg) {
        if (msg.to != ID && msg.to != SCM.TID.GLOBAL) {
            Log.error("Message rerouting error on thread: " + ID.name() + " on port: " + portNum);
            return;
        }
        
        switch(msg.type) {
            case GLOBAL_HANDSHAKE -> {
                Log.info("Handshake received in <" + ID.name()+ "> from <" + msg.from.name() + ">" + " on port: " + portNum);
            }
            case GLOBAL_TERMINATE -> {
               Log.info(ID.name() + " terminating on port: " + portNum);
                terminationCallback();
                running = false;
            }
            default -> {
               if (handlePersonalMessage(msg))
                    break;
                Log.error("Unhandled user core message in: " + ID.name() + 
                    " Msg: " + msg.toString() + " on port: " + portNum);
            }
        }
    }
    
    private void txNpToClient(NetPackage.NetMessageInterface msg) {
        serSuccess = true;
        String sendBack = np.serialize(msg);
        if (sendBack == null || !serSuccess)
            return;
        try {
            clientOut.write(sendBack + "\n");
            clientOut.flush();
        } catch (IOException ex) {
            //Log.error(SCM.TID.CLIENT_TX_THREAD.name() + " tx error: " + ex.toString());
            SCM msg1 = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_TX_THREAD)
                    .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                    .setType(SCM.TYPE.TXT_CLIENT_CONNECTION_UNEXPECTED_TERMINATION)
                    .setBody(connUUID);
            scktMgrThrd.sendMessage(msg1);
                SCM msg2 = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_TX_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD)
                    .setType(SCM.TYPE.GLOBAL_TERMINATE)
                    .setBody(null);
            this.sendMessage(msg2);
        }
    }

    @Override
    public void serializationError(String errStr) {
        serSuccess = false;
        Log.error(connUUID.toString() + " " + errStr);
    }
    
    private static class RxThread extends Thread implements NetPackage.DeserializeCallbackInterface {
        private boolean running = true;
        private final SCMI txThread;
        private final NetPackage np;
        private final BufferedReader clientIn;
        private final int portNum;
        private boolean isTerminated = false;
        
        public RxThread(BufferedReader clientIn, Gson deserializer, SCMI txThread, int portNum) {
            this.txThread = txThread;
            this.clientIn = clientIn;
            np = new NetPackage(deserializer, this, null);
            this.portNum = portNum;
        }
        
        public void terminate(){
            if (isTerminated)
                return;
            running = false;
            try {
                clientIn.close();
            } catch (IOException ex) {
                Log.error("RXT_IMPOSSIBLE EXCEPTION_1: " + ex.toString());
            }
        }
        
        @Override
        public void run() {
            String inStr;
            Log.info(SCM.TID.CLIENT_RX_THREAD.name() + " started on port: " + portNum);
            try {
                while (running) {
                    inStr = clientIn.readLine();
                    if (inStr == null || !running) {
                        break;
                    }
                    np.deserialize(inStr);
                }
            } catch (IOException ex) {
                // is happening when clientIn.close() from this.termiante() is called
            } finally {
                Log.info(SCM.TID.CLIENT_RX_THREAD.name() + " termination on port: " + portNum);
                isTerminated = true;
                try {
                    if (running) {
                        SCM msg = SCM.nm()
                                .setFrom(SCM.TID.CLIENT_RX_THREAD)
                                .setTo(SCM.TID.CLIENT_TX_THREAD)
                                .setType(SCM.TYPE.RXT_UNEXPECTED_TERMINATION)
                                .setBody(null);
                        txThread.sendMessage(msg);
                    }
                    clientIn.close();
                } catch (IOException ex) {
                    Log.error("RXT_IMPOSSIBLE EXCEPTION2: " + ex.toString());
                } 
            }
        }

        @Override
        public void deserializationError(Class<?> errClass, String errorStr) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_RX_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD)
                    .setType(SCM.TYPE.RXT_DESERIALIZATION_ERROR)
                    .setBody(errorStr);
            txThread.sendMessage(msg);
        }

        @Override
        public void np_infoPacketAcquired(NP_InfoPacket infoPacket) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_RX_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD)
                    .setType(SCM.TYPE.RXT_INFO_PACKET_ACQUIRED)
                    .setBody(infoPacket);
            txThread.sendMessage(msg);
        }

        @Override
        public void np_registrationPacketAcquired(NP_RegistrationPacket rrp) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_RX_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD);
            if (rrp.isRequest) {
                msg.setType(SCM.TYPE.RXT_REGISTRATION_REQUEST_ACQUIRED)
                    .setBody(rrp);
            } else {
                String errStr = "Server received registration response packet which is forbidden";
                msg.setType(SCM.TYPE.RXT_RECEIVER_WRONG_CMD_PACKET_TYPE)
                        .setBody(errStr);
            }
            txThread.sendMessage(msg);
        }

        @Override
        public void np_authorizationPacketAcquired(NP_AuthorizationPacket ap) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_RX_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD);
            if (ap.isRequest) {
                msg.setType(SCM.TYPE.RXT_AUTHORIZATION_REQUEST_ACQUIRED)
                    .setBody(ap);
            } else {
                String errStr = "Server received authorization response packet which is forbidden";
                msg.setType(SCM.TYPE.RXT_RECEIVER_WRONG_CMD_PACKET_TYPE)
                        .setBody(errStr);
            }
            txThread.sendMessage(msg);
        }

        @Override
        public void np_resoursePacketAcquired(NP_ResoursePacket rp) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_RX_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD);
            if (rp.isRequest) {
                msg.setType(SCM.TYPE.RXT_RESOURSE_GET_ACQUIRED)
                    .setBody(rp);
            } else {
                String errStr = "Server received get_resourse response packet which is forbidden";
                msg.setType(SCM.TYPE.RXT_RECEIVER_WRONG_CMD_PACKET_TYPE)
                        .setBody(errStr);
            }
            txThread.sendMessage(msg);
        }

        @Override
        public void np_sendMsgPacketAcquired(NP_SendMsgPacket smsgp) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_RX_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD);
            if (smsgp.isRequest) {
                msg.setType(SCM.TYPE.RXT_SEND_MSG_ACQUIRED)
                        .setBody(smsgp);
            } else {
                String errStr = "Server received send_message response packet which is forbidden";
                msg.setType(SCM.TYPE.RXT_RECEIVER_WRONG_CMD_PACKET_TYPE)
                        .setBody(errStr);
            }
            txThread.sendMessage(msg);
        }

        @Override
        public void np_getMsgPacketAcquired(NP_GetMsgPacket gmsgp) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_RX_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD);
            if (gmsgp.isRequest) {
                msg.setType(SCM.TYPE.RXT_GET_MSG_ACQUIRED)
                        .setBody(gmsgp);
            } else {
                String errStr = "Server received get_message response packet which is forbidden";
                msg.setType(SCM.TYPE.RXT_RECEIVER_WRONG_CMD_PACKET_TYPE)
                        .setBody(errStr);
            }
            txThread.sendMessage(msg);
        }

        @Override
        public void np_createRequestPacketAcquired(NP_CreateRequestPacket crp) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_RX_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD);
            if (crp.isRequest) {
                msg.setType(SCM.TYPE.RXT_CREATE_REQUEST_ACQUIRED)
                        .setBody(crp);
            } else {
                String errStr = "Server received create_request response packet which is forbidden";
                msg.setType(SCM.TYPE.RXT_RECEIVER_WRONG_CMD_PACKET_TYPE)
                        .setBody(errStr);
            }
            txThread.sendMessage(msg);
        }

        @Override
        public void np_getSchedulePacketAcquired(NP_GetSchedulePacket gsp) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_RX_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD);
            if (gsp.isRequest) {
                msg.setType(SCM.TYPE.RXT_GET_SCHEDULE_ACQUIRED)
                        .setBody(gsp);
            } else {
                String errStr = "Server received get_schedule response packet which is forbidden";
                msg.setType(SCM.TYPE.RXT_RECEIVER_WRONG_CMD_PACKET_TYPE)
                        .setBody(errStr);
            }
            txThread.sendMessage(msg);
        }

        @Override
        public void np_changeReqStatusPacketAcquired(NP_ChangeRequestStatus crp) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.CLIENT_RX_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD);
            if (crp.isRequest) {
                msg.setType(SCM.TYPE.RXT_CHANGE_REQUEST_STATUS_ACQUIRED)
                        .setBody(crp);
            } else {
                String errStr = "Server received change_request_status response packet which is forbidden";
                msg.setType(SCM.TYPE.RXT_RECEIVER_WRONG_CMD_PACKET_TYPE)
                        .setBody(errStr);
            }
            txThread.sendMessage(msg);
        }
    }
}
