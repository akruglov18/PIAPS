/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.unn.piap_serverside;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unn.piap_serverside.database.MsgTableRecord;
import com.unn.piap_serverside.database.UsersTableRecord;
import com.unn.piap_serverside.net_protocol.DB_MsgRecord;
import com.unn.piap_serverside.net_protocol.DB_RequestRecord;
import com.unn.piap_serverside.net_protocol.DB_ResourseRecord;
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
import java.util.ArrayList;
import java.util.UUID;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 *
 * @author STALKER
 */
public class PIAP_ServerCore {
    public static class DeserClbkImplementation implements NetPackage.DeserializeCallbackInterface {
        public NP_InfoPacket infoPacket;
        public NP_RegistrationPacket rrp;
        
        
        public DeserClbkImplementation() {
            infoPacket = null;
            rrp = null;
        }

        @Override
        public void deserializationError(Class<?> errClass, String errorStr) {
            System.out.println("RX: " + errClass.getName() + " error: " + errorStr);
        }

        @Override
        public void np_infoPacketAcquired(NP_InfoPacket infoPacket) {
            this.infoPacket = infoPacket;
            System.out.println("RX info: " + infoPacket.info);
        }

        @Override
        public void np_registrationPacketAcquired(NP_RegistrationPacket rrp) {
            this.rrp = rrp;
            System.out.println("RX: " + rrp.respType.name());
        }

        @Override
        public void np_authorizationPacketAcquired(NP_AuthorizationPacket ap) {
            System.out.println("RX: " + ap.respType.name());
        }

        @Override
        public void np_resoursePacketAcquired(NP_ResoursePacket rp) {
            System.out.println("RX: " + rp.respType.name());
            if (rp.respType == NP_ResoursePacket.RESPONSE_TYPE.OK) {
                for (DB_ResourseRecord record : rp.records) {
                    System.out.println(record.type.name() + " " + record.name + " " + record.count);
                }
            }
        }

        @Override
        public void np_sendMsgPacketAcquired(NP_SendMsgPacket smsgp) {
            System.out.println("RX: " + smsgp.respType.name());
        }

        @Override
        public void np_getMsgPacketAcquired(NP_GetMsgPacket gmsgp) {
            System.out.println("RX: " + gmsgp.respType.name());
            if (gmsgp.respType == NP_GetMsgPacket.RESPONSE_TYPE.OK) {
                for (DB_MsgRecord record : gmsgp.records) {
                    System.out.println(record.loginFrom + " " +
                            record.loginTo + " " +
                            record.timestamp.toString() + "\n" +
                            record.theme + "\n" + 
                            record.body);
                }
            }
        }

        @Override
        public void np_createRequestPacketAcquired(NP_CreateRequestPacket crp) {
            System.out.println("RX: " + crp.respType.name());
            if (crp.respType == NP_CreateRequestPacket.RESPONSE_TYPE.OK) {
                System.out.println(crp.requestStatus.name());
            }
        }

        @Override
        public void np_getSchedulePacketAcquired(NP_GetSchedulePacket gsp) {
            System.out.println("RX: " + gsp.respType.name());
            if (gsp.respType == NP_GetSchedulePacket.RESPONSE_TYPE.OK) {
                for (DB_RequestRecord rr : gsp.records) {
                    System.out.println("RX: " + rr.reqUUID + " " + rr.login + " " + rr.status);
                }
            }
        }

        @Override
        public void np_changeReqStatusPacketAcquired(NP_ChangeRequestStatus crp) {
            System.out.println("RX: " + crp.respType.name());
        }
    }
    
    public static class SerClbkImplementation implements NetPackage.SerializeCallbackInterface {
        @Override
        public void serializationError(String errStr) {
            System.out.println(errStr);
        }
    }
    
    public static String TST_LGN = "testLogin";
    public static String TST_PSWD = "testPassword";
    public static String TST_FIO = "testFIO";
    public static void main(String[] args) throws InterruptedException { 
        
        Testing tests = new Testing(4968);
        
       
        /* <NP_RegistrationPacket> */
        tests.addTest(new NP_RegistrationPacket(false, null, null, null, null, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_RegistrationPacket(true, null, TST_PSWD, TST_FIO, NP_RegistrationPacket.USER_TYPE.COORDINATOR, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_RegistrationPacket(true, TST_LGN, null, TST_FIO, NP_RegistrationPacket.USER_TYPE.COORDINATOR, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_RegistrationPacket(true, TST_LGN, TST_PSWD, null, NP_RegistrationPacket.USER_TYPE.COORDINATOR, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_RegistrationPacket(true, TST_LGN, TST_PSWD, TST_FIO, null, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_RegistrationPacket(true, TST_LGN, TST_PSWD, TST_FIO, NP_RegistrationPacket.USER_TYPE.COORDINATOR, null),
                NP_RegistrationPacket.RESPONSE_TYPE.REGISTERED);
        tests.addTest(new NP_RegistrationPacket(true, TST_LGN, TST_PSWD, TST_FIO, NP_RegistrationPacket.USER_TYPE.COORDINATOR, null),
                NP_RegistrationPacket.RESPONSE_TYPE.ERROR_LOGIN_ALREADY_EXISTS);
        /* </NP_RegistrationPacket> */
        
        /* <Check for authorization errors> */
        tests.addTest(new NP_ResoursePacket(true, null, null),
                NP_ResoursePacket.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED);
        
        DB_MsgRecord tmsgr1 = new DB_MsgRecord(TST_LGN, TST_LGN, null, "theme", "body");
        tests.addTest(new NP_SendMsgPacket(true, tmsgr1, null),
                NP_SendMsgPacket.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED);
        
        tests.addTest(new NP_GetMsgPacket(true, TST_LGN, null, null),
                NP_GetMsgPacket.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED);
        
        DB_RequestRecord rr1 = new DB_RequestRecord(null, TST_LGN, null, 2022, 12, 15, 1, 2, 5, 2, 2, 2);
        tests.addTest(new NP_CreateRequestPacket(true, rr1, null, null),
                NP_CreateRequestPacket.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED);
        
        ArrayList<DB_RequestRecord.REQ_STATUS> reqStat1 = new ArrayList<>();
        reqStat1.add(DB_RequestRecord.REQ_STATUS.APPROVED);
        reqStat1.add(DB_RequestRecord.REQ_STATUS.AWAITS_REVIEW);
        reqStat1.add(DB_RequestRecord.REQ_STATUS.REVIEWING);
        reqStat1.add(DB_RequestRecord.REQ_STATUS.CANCELLED);
        reqStat1.add(DB_RequestRecord.REQ_STATUS.PASSED);
        tests.addTest(new NP_GetSchedulePacket(true, 0, 0, 0, null, reqStat1, null, null),
                NP_GetSchedulePacket.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED);
        
        tests.addTest(new NP_ChangeRequestStatus(true, TST_FIO, DB_RequestRecord.REQ_STATUS.REVIEWING, null),
                NP_ChangeRequestStatus.RESPONSE_TYPE.ERROR_NOT_AUTHORIZED);
        /* </Check for authorization errors> */
        
        
        /* <NP_AuthorizationPacket> */
        tests.addTest(new NP_AuthorizationPacket(false, null, null, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_AuthorizationPacket(true, null, TST_PSWD, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_AuthorizationPacket(true, TST_LGN, null, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_AuthorizationPacket(true, "qwerty", TST_PSWD, null),
                NP_AuthorizationPacket.RESPONSE_TYPE.ERROR_USER_NOT_FOUND);
        tests.addTest(new NP_AuthorizationPacket(true, TST_LGN, "qwerty", null),
                NP_AuthorizationPacket.RESPONSE_TYPE.ERROR_WRONG_PASSWORD);
        tests.addTest(new NP_AuthorizationPacket(true, TST_LGN, TST_PSWD, null),
                NP_AuthorizationPacket.RESPONSE_TYPE.AUTHORIZED);
        /* </NP_AuthorizationPacket> */
        
        /* <NP_ResoursePacket> */
        tests.addTest(new NP_ResoursePacket(true, null, null),
                NP_ResoursePacket.RESPONSE_TYPE.OK);
        /* </NP_ResoursePacket> */
        
        /* <NP_SendMsgPacket> */
        tests.addTest(new NP_SendMsgPacket(false, null, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_SendMsgPacket(true, null, null),
                new NP_InfoPacket(null));
        
        DB_MsgRecord tmsgr2 = new DB_MsgRecord("LGN_FROM", "LGN_TO", null, "theme", "body");
        tests.addTest(new NP_SendMsgPacket(true, tmsgr2, null),
                NP_SendMsgPacket.RESPONSE_TYPE.ERROR_WRONG_LOGIN_FROM);
        
        DB_MsgRecord tmsgr3 = new DB_MsgRecord(TST_LGN, "LGN_TO", null, "theme", "body");
        tests.addTest(new NP_SendMsgPacket(true, tmsgr3, null),
                NP_SendMsgPacket.RESPONSE_TYPE.ERROR_LOGIN_TO_NOT_FOUND);
        
        DB_MsgRecord tmsgr4 = new DB_MsgRecord(TST_LGN, TST_LGN, null, "theme", "body");
        tests.addTest(new NP_SendMsgPacket(true, tmsgr4, null),
                NP_SendMsgPacket.RESPONSE_TYPE.OK);
        /* </NP_SendMsgPacket> */
        
        /* <NP_GetMsgPacket> */
        tests.addTest(new NP_GetMsgPacket(false, null, null, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_GetMsgPacket(true, null, null, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_GetMsgPacket(true, "qwerty", null, null),
                NP_GetMsgPacket.RESPONSE_TYPE.ERROR_WRONG_LOGIN);
        tests.addTest(new NP_GetMsgPacket(true, TST_LGN, null, null),
                NP_GetMsgPacket.RESPONSE_TYPE.OK);
        /* </NP_GetMsgPacket> */
        
        /* <NP_CreateRequestPacket> */
        tests.addTest(new NP_CreateRequestPacket(false, null, null, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_CreateRequestPacket(true, null, null, null),
                new NP_InfoPacket(null));
        
        DB_RequestRecord rr2 = new DB_RequestRecord(null, "qwerty", null, 2022, 12, 15, 1, 2, 5, 2, 2, 2);
        tests.addTest(new NP_CreateRequestPacket(true, rr2, null, null),
                NP_CreateRequestPacket.RESPONSE_TYPE.ERROR_WRONG_LOGIN);
        
        DB_RequestRecord rr3 = new DB_RequestRecord("uuid", TST_LGN, null, 2022, 12, 15, 1, 2, 5, 2, 2, 2);
        tests.addTest(new NP_CreateRequestPacket(true, rr3, null, null),
                NP_CreateRequestPacket.RESPONSE_TYPE.ERROR_REQUEST_UUID_NOT_FOUND);
        
        DB_RequestRecord rr4 = new DB_RequestRecord(null, TST_LGN, null, 2022, 12, 15, 1, 2, 5, 2, 2, 2);
        tests.addTest(new NP_CreateRequestPacket(true, rr4, null, null),
                NP_CreateRequestPacket.RESPONSE_TYPE.OK);
        /* </NP_CreateRequestPacket> */
        
        /* <NP_GetSchedulePacket> */
        tests.addTest(new NP_GetSchedulePacket(false, 0, 0, 0, null, null, null, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_GetSchedulePacket(true, 0, 0, 0, null, null, null, null),
                new NP_InfoPacket(null));
        tests.addTest(new NP_GetSchedulePacket(true, 0, 0, 0, null, reqStat1, null, null),
                NP_GetSchedulePacket.RESPONSE_TYPE.OK);
        /* </NP_GetSchedulePacket> */
        
        /* <NP_ChangeRequestStatus> */
        tests.addTest(new NP_ChangeRequestStatus(true, "uuid", DB_RequestRecord.REQ_STATUS.APPROVED, null),
                NP_ChangeRequestStatus.RESPONSE_TYPE.ERROR_REQUEST_NOT_FOUND);
        /* </NP_ChangeRequestStatus> */
        
        tests.start();
        while (true) {
            tests.join(5000);
            return;
        }
        
        
        
        
        
        
        
        
        
        
        //Gson gson = new GsonBuilder().create();
        //DeserClbkImplementation deserClbk = new DeserClbkImplementation();
        //NetPackage np = new NetPackage(gson, deserClbk, new SerClbkImplementation());
        
        
        //NP_ChangeRequestStatus netp2 = new NP_ChangeRequestStatus(true,
        //        "7bb66586-d033-4fcd-8b7f-eff054ff28e7",  // requestUUID
        //        DB_RequestRecord.REQ_STATUS.PASSED,
        //        null);
        //DB_RequestRecord rr = new DB_RequestRecord(
        //        null, // reqUUID
        //        "lgn3", // login
        //        null, // status
        //        2022, // year
        //        12, // month
        //        16, // day
        //        2, // regTimeStart
        //        3, // regTimeStop
        //        6, // adtNum
        //        10, // chairCnt
        //        12, // projCnt
        //        39); // boardCnt 
        //NP_CreateRequestPacket netp2 = new NP_CreateRequestPacket(true, rr, null, null);
        
        //NP_GetMsgPacket netp = new NP_GetMsgPacket(true, "lgn2", null, null);
        //DB_MsgRecord dbmsgr = new DB_MsgRecord("lgn1", "lgn2", null, "theme2", "body2");
        //NP_SendMsgPacket netp = new NP_SendMsgPacket(true, dbmsgr, null);
        //NP_ResoursePacket netp = new NP_ResoursePacket(true, null, null);
        //NP_AuthorizationPacket netp = new NP_AuthorizationPacket(true, "lgn3", "pswd3", null);
        //NP_RegistrationPacket netp = new NP_RegistrationPacket(true, "lgn3", "pswd3", "Зубенко Михаил Петрович", NP_RegistrationPacket.USER_TYPE.CUSTOMER, null);
        //NP_InfoPacket netp = new NP_InfoPacket("Check Ignition");
        
        //DB_MsgRecord dbmsgr = new DB_MsgRecord("lgn3", "lgn2", null, "theme333", "body333");
        //NP_SendMsgPacket netp2 = new NP_SendMsgPacket(true, dbmsgr, null);
        
        /*ArrayList<DB_RequestRecord.REQ_STATUS> reqStat = new ArrayList<>();
        reqStat.add(DB_RequestRecord.REQ_STATUS.APPROVED);
        reqStat.add(DB_RequestRecord.REQ_STATUS.AWAITS_REVIEW);
        reqStat.add(DB_RequestRecord.REQ_STATUS.REVIEWING);
        reqStat.add(DB_RequestRecord.REQ_STATUS.CANCELLED);
        reqStat.add(DB_RequestRecord.REQ_STATUS.PASSED);
        NP_GetSchedulePacket netp2 = new NP_GetSchedulePacket(true, 0, 0, 0, "lgn3", reqStat, null, null);
        
       
        
        
        String s = np.serialize(netp);
        Socket conn = null;
        BufferedWriter writer = null;
        try { 
            conn = new Socket("localhost", 4968);
            writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            writer.write(s + "\n");
            writer.flush();
            String str = clientIn.readLine();
            np.deserialize(str);
            
            
            s = np.serialize(netp2);
            writer.write(s + "\n");
            writer.flush();
            str = clientIn.readLine();
            np.deserialize(str);
            
            
            
        } catch (IOException  ex) {
            System.out.println(ex.toString());
        } finally {
            try {
                if (conn != null)
                    conn.close();
                if (writer != null)
                    writer.close();
            } catch (IOException ex) {
                System.out.println("CLOSING ERROR: " + ex.toString());
            }
        }*/

    }
}
