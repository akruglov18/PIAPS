/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unn.piap_serverside.net_protocol.NP_AuthorizationPacket;
import com.unn.piap_serverside.net_protocol.NP_ChangeRequestStatus;
import com.unn.piap_serverside.net_protocol.NP_CreateRequestPacket;
import com.unn.piap_serverside.net_protocol.NP_GetMsgPacket;
import com.unn.piap_serverside.net_protocol.NP_GetSchedulePacket;
import com.unn.piap_serverside.net_protocol.NP_InfoPacket;
import com.unn.piap_serverside.net_protocol.NP_RegistrationPacket;
import com.unn.piap_serverside.net_protocol.NP_ResoursePacket;
import com.unn.piap_serverside.net_protocol.NP_SendMsgPacket;
import com.unn.piap_serverside.net_protocol.NetPackage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;

/**
 *
 * @author STALKER
 */
public class Testing extends Thread implements NetPackage.DeserializeCallbackInterface, NetPackage.SerializeCallbackInterface {
    private final int portNum;
    private final ArrayList<Test> tests = new ArrayList<>();
    private boolean serErr;
    private boolean deserErr;
    private Object resp;
    
    
    
    
    public Testing(int portNum) {
        this.portNum = portNum;
    }
    
    public void addTest(NetPackage.NetMessageInterface np, Object awaitedResponse) {
        tests.add(new Test(np, awaitedResponse));
    }
    
    @Override
    public void run() {
        int testsPassed = 0;
        int currTest = 1;
        int testCnt = tests.size();
        Log.info("TESTING STARTED");
        Gson gson = new GsonBuilder().create();
        NetPackage np = new NetPackage(gson, this, this);
        
        Socket conn = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        String outStr, inStr;
        try {
            conn = new Socket("localhost", portNum);
            writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            for (Test test : tests) {
                Log.info("RUNNING TEST: " + currTest);
                currTest += 1;
                serErr = false;
                deserErr = false;
                outStr = np.serialize(test.np);
                
                writer.write(outStr + "\n");
                writer.flush();
                
                inStr = reader.readLine();
                np.deserialize(inStr);
                
                if (deserErr) {
                    Log.info("TEST: " + (currTest - 1) + " NOT PASSED: DESERIALIZATION ERROR OCCURED");
                    continue;
                }
                
                if (test.awaitedResponse == null) {
                    Log.info("TEST: " + (currTest - 1) + " NOT PASSED: USER ERROR IN TEST FORMING");
                    continue;
                }
                
                if (resp == null) {
                    Log.info("TEST: " + (currTest - 1) + " NOT PASSED: ERROR IN TESTING ENVIRONMENT");
                    continue;
                }
                
                if (!test.awaitedResponse.equals(resp)) {
                    Log.info("TEST: " + (currTest - 1) + " NOT PASSED: WRONG RESPONSE: " + resp.toString());
                    continue;
                }
                
                Log.info("TEST: " + (currTest - 1) + " PASSED");
                testsPassed += 1;
            }
        } catch (IOException ex) {
            Log.info("TESING EXCEPTION OCCURED: " + ex.toString());
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (writer != null)
                    writer.close();
                if (reader != null)
                    reader.close();
            } catch (IOException ex) {
                
            }
            Log.info("TESTING TERMINATION");
            Log.info("TESTS PASSED: " + testsPassed + " OUT OF " + testCnt);
        }
    }

    @Override
    public void deserializationError(Class<?> errClass, String errorStr) {
        deserErr = true;
    }

    @Override
    public void np_infoPacketAcquired(NP_InfoPacket infoPacket) {
        resp = infoPacket;
    }

    @Override
    public void np_registrationPacketAcquired(NP_RegistrationPacket rrp) {
        resp = rrp.respType;
    }

    @Override
    public void np_authorizationPacketAcquired(NP_AuthorizationPacket ap) {
        resp = ap.respType;
    }

    @Override
    public void np_resoursePacketAcquired(NP_ResoursePacket rp) {
        resp = rp.respType;
    }

    @Override
    public void np_sendMsgPacketAcquired(NP_SendMsgPacket smsgp) {
        resp = smsgp.respType;
    }

    @Override
    public void np_getMsgPacketAcquired(NP_GetMsgPacket gmsgp) {
        resp = gmsgp.respType;
    }

    @Override
    public void np_createRequestPacketAcquired(NP_CreateRequestPacket crp) {
        resp = crp.respType;
    }

    @Override
    public void np_getSchedulePacketAcquired(NP_GetSchedulePacket gsp) {
        resp = gsp.respType;
    }

    @Override
    public void np_changeReqStatusPacketAcquired(NP_ChangeRequestStatus crp) {
        resp = crp.respType;
    }

    @Override
    public void serializationError(String errStr) {
        serErr = true;
    }
    
    @AllArgsConstructor
    private class Test {
        public NetPackage.NetMessageInterface np;
        public Object awaitedResponse;
    }
}
