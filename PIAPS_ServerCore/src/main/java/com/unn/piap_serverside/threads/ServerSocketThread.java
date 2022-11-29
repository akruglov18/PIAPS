package com.unn.piap_serverside.threads;

import com.unn.piap_serverside.Log;
import com.unn.piap_serverside.data_types.SCM;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class ServerSocketThread extends Thread {
    private int port;
    protected ServerSocket serverSocket = null;
    private boolean running = true;
    private final SSTCallback parentCallback;
    
    

    public ServerSocketThread(int port, SSTCallback sstClbk) {
        this.port = port;
        this.parentCallback = sstClbk;
        try {
            serverSocket = new ServerSocket();
            SocketAddress addr = new InetSocketAddress("localhost", port);
            serverSocket.bind(addr);
            this.parentCallback.serverSocketOpened_clbk();
        } catch (SecurityException | IOException | IllegalArgumentException ex) {
            serverSocket = null;
            this.parentCallback.errorCreatingServSocket_clbk(ex.toString());
        }
    }
    
    public void terminateThread() {
        running = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ex) {
                Log.error(SCM.TID.SERVER_SOCKET_THREAD.name() + " Error occured while closing ServerSocket: " + ex.toString());
            }
        }
    }

    @Override
    public void run() {
        if (serverSocket == null) {
            Log.error(SCM.TID.SERVER_SOCKET_THREAD.name() + " serverSocket is null. Terminating thread");
            return;
        }
        
        Log.info(SCM.TID.SERVER_SOCKET_THREAD.name() + " started and listening on port: " + port);
        while(running) {
            try {
                Socket client = serverSocket.accept();
                if (client != null)
                    parentCallback.newClientConnected_clbk_difThrd(client);
            } catch (IOException ex) {
                Log.info(SCM.TID.SERVER_SOCKET_THREAD.name() + " closed. ServerSocketThread termination");
                return;
            }
        }
    }
    
    public interface SSTCallback {
        public void errorCreatingServSocket_clbk(String errStr);
        public void serverSocketOpened_clbk();
        public void newClientConnected_clbk_difThrd(Socket newClient);
    }
}
