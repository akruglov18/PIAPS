/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside.threads;

import com.unn.piap_serverside.Log;
import com.unn.piap_serverside.data_types.NetPackageWrapper;
import com.unn.piap_serverside.data_types.SCM;
import com.unn.piap_serverside.interfaces.SCMI;
import com.unn.piap_serverside.net_protocol.NP_InfoPacket;
import com.unn.piap_serverside.net_protocol.NetPackage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author STALKER
 */
public class SocketManagerThread extends ThreadBase {
    private final SCMI router;

    // <editor-fold defaultstate="collapsed" desc="ServerSocketThread parts"> 
    private final SCMI self;
    private Integer portNum = -1;
    private ServerSocketThread serverSocketThread = null;
    private boolean isListening = false;
    private final ServerSocketThread.SSTCallback sstClbk = 
            new ServerSocketThread.SSTCallback() {
        @Override
        public void errorCreatingServSocket_clbk(String errStr) {
            isListening = false;
            Log.error("ServerSocketThread ServerSocket open error: " + errStr);
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.SOCKET_MANAGER_THREAD)
                    .setTo(SCM.TID.UI_CHANGER_THREAD)
                    .setType(SCM.TYPE.UIT_START_LISTENING)
                    .setBody("ошибка");
            router.sendMessage(msg);
        }

        @Override
        public void serverSocketOpened_clbk() {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.SOCKET_MANAGER_THREAD)
                    .setTo(SCM.TID.UI_CHANGER_THREAD)
                    .setType(SCM.TYPE.UIT_START_LISTENING)
                    .setBody("слушаю");
            router.sendMessage(msg);
        }

        @Override
        public void newClientConnected_clbk_difThrd(Socket newClient) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.SERVER_SOCKET_THREAD)
                    .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                    .setType(SCM.TYPE.SST_ADD_NEW_CLIENT)
                    .setBody(newClient);
            self.sendMessage(msg);
        }
    };
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Client connections management">
    private final HashMap<UUID, ClientConnection> connectionsPool = new HashMap<>();
    //</editor-fold>
    
    public SocketManagerThread(SCMI router) {
        super(SCM.TID.SOCKET_MANAGER_THREAD);
        this.router = router;
        this.self = this;
    }

    @Override
    protected void terminationCallback() {
        if (serverSocketThread != null)
            serverSocketThread.terminateThread();
        ArrayList<ClientConnection> connections = new ArrayList<>(connectionsPool.values());
        for (ClientConnection conn : connections) {
            SCM msg = SCM.nm()
                    .setFrom(SCM.TID.SOCKET_MANAGER_THREAD)
                    .setTo(SCM.TID.CLIENT_TX_THREAD)
                    .setType(SCM.TYPE.GLOBAL_TERMINATE)
                    .setBody(null);
            conn.sendMessage(msg);
        }
        connectionsPool.clear();
    }

    @Override
    protected boolean handlePersonalMessage(SCM msg) {
        switch (msg.type) {
            case RXT_AUTHORIZATION_REQUEST_ACQUIRED -> {
                msg.setTo(SCM.TID.DB_MANAGER_THREAD);
                router.sendMessage(msg);
                return true;
            }
            
            case RXT_REGISTRATION_REQUEST_ACQUIRED -> {
                msg.setTo(SCM.TID.DB_MANAGER_THREAD);
                router.sendMessage(msg);
                return true;
            }
            
            case RXT_RESOURSE_GET_ACQUIRED -> {
                msg.setTo(SCM.TID.DB_MANAGER_THREAD);
                router.sendMessage(msg);
                return true;
            }
            
            case DBT_SEND_NP_RESPONSE -> {
                NetPackageWrapper npw = (NetPackageWrapper) msg.body;
                NetPackage.NetMessageInterface np = (NetPackage.NetMessageInterface) npw.body;
                
                ClientConnection client = connectionsPool.get(npw.connUUID);
                if (client == null) {
                    Log.error(ID.name() + "DBT_SEND_NP_RESPONSE error: " +
                            np.getMessageType().name() + " " + np.getCommandType().name() +
                            ". Client disconnected");
                    return true;
                }
                msg.setTo(SCM.TID.CLIENT_TX_THREAD);
                msg.setBody(np);
                client.sendMessage(msg);
                return true;
            }
            
            case UIT_SEND_INFO_PACKET_TO_CLIENTS -> {
                ArrayList<ClientConnection> connections = new ArrayList<>(connectionsPool.values());
                for (ClientConnection conn : connections) {
                    NP_InfoPacket npip = new NP_InfoPacket((String) msg.body);
                    SCM nmsg = SCM.nm()
                            .setFrom(SCM.TID.SOCKET_MANAGER_THREAD)
                            .setTo(SCM.TID.CLIENT_TX_THREAD)
                            .setType(SCM.TYPE.DBT_SEND_NP_RESPONSE)
                            .setBody(npip);
                    conn.sendMessage(nmsg);
                }
                return true;
            }
            
            case SST_ADD_NEW_CLIENT -> {
                Socket newClient = (Socket) msg.body;
                UUID connUUID = UUID.randomUUID();
                ClientConnection cc = null;
                try {
                    cc = new ClientConnection(connUUID, newClient, this);
                    cc.start();
                } catch (IOException ex) {
                    cc = null;
                } finally {
                    if (cc != null)
                        connectionsPool.put(connUUID, cc);
                }
                return true;
            }
            
            case TXT_CLIENT_CONNECTION_UNEXPECTED_TERMINATION -> {
                UUID connUUID = (UUID) msg.body;
                try {
                    connectionsPool.remove(connUUID);
                } catch(Exception ex) {
                    Log.error(ID.name() + " is trying to remove already removed client");
                }
                return true;
            }
            
            case RXT_INFO_PACKET_ACQUIRED -> {
                msg.setTo(SCM.TID.UI_CHANGER_THREAD);
                router.sendMessage(msg);
                return true;
            }
            
            case UIT_SET_PORT -> {
                if (msg.body != null && msg.body.getClass().getName().equals(Integer.class.getName()))
                    this.portNum = (Integer) msg.body;
                else
                    Log.error("Body is not Integer");
                return true;
            }

            case UIT_START_LISTENING -> {
                if (isListening)
                    return true;
                String respStr;
                SCM nmsg = SCM.nm()
                    .setFrom(SCM.TID.SOCKET_MANAGER_THREAD)
                    .setTo(SCM.TID.UI_CHANGER_THREAD)
                    .setType(SCM.TYPE.UIT_START_LISTENING);
                if (portNum == -1) {
                    Log.error("SocketManagerThread start listening occured while port isn't set");
                    respStr = "ошибка";
                    nmsg.setBody(respStr);
                    router.sendMessage(nmsg);
                    return true;
                }
                respStr = "слушаю";
                nmsg.setBody(respStr);
                router.sendMessage(nmsg);
                isListening = true;
                serverSocketThread = new ServerSocketThread(portNum, sstClbk);
                serverSocketThread.start();
                return true;
            }
            
            case UIT_STOP_LISTENING -> {
                if (!isListening)
                    return true;
                String respStr = "остановлен";
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setTo(SCM.TID.UI_CHANGER_THREAD)
                        .setType(SCM.TYPE.UIT_STOP_LISTENING)
                        .setBody(respStr);
                router.sendMessage(nmsg);
                isListening = false;
                serverSocketThread.terminateThread();
                serverSocketThread = null;
                return true;
            }
            
            default -> {
                return false;
            }
        }
    }
}
