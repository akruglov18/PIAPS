package com.unn.piap_serverside.threads;

import com.unn.piap_serverside.Log;
import com.unn.piap_serverside.data_types.SCM;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketThread extends Thread {

    protected ServerSocket server;

    public ServerSocketThread(ServerSocket _server) {
        server = _server;
    }

    public boolean stopListening() {
        try {
            server.close();
            return true;
        } catch (IOException e) {
            Log.error("Can't close serverSocket");
            return true;
        }
    }

    @Override
    public void run() {
        try {
            Socket client = server.accept();
            SCM ms = SCM.nm()
                    .setFrom(SCM.TID.SERVER_SOCKET_THREAD)
                    .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                    .setType(SCM.TYPE.SCT_ADD_NEW_CLIENT)
                    .setBody((Object) client);
        } catch (IOException e) {
            Log.info("Server has been closed");
        }
    }
}
