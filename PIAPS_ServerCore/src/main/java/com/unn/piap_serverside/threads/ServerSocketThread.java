package com.unn.piap_serverside.threads;

import com.unn.piap_serverside.Log;
import com.unn.piap_serverside.data_types.SCM;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketThread extends ThreadBase {

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
            return false;
        }
    }

    public void run() {
        try {
            Socket client = server.accept();
            // Registration code
        } catch (IOException e) {
            Log.info("Server has been closed");
        }
    }

    @Override
    protected void terminationCallback() {

    }
    @Override
    protected boolean handlePersonalMessage(SCM msg) {
        return false;
    }
}
