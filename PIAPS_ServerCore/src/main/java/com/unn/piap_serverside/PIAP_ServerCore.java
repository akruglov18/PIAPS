/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.unn.piap_serverside;

/*
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
*/

/**
 *
 * @author STALKER
 */
public class PIAP_ServerCore {
    /*
    
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
                    System.out.println("RX: " + rr.reqUUID + " " + rr.login);
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
    }*/
    
    
    public static void main(String[] args) { /*
        
        Gson gson = new GsonBuilder().create();
        DeserClbkImplementation deserClbk = new DeserClbkImplementation();
        NetPackage np = new NetPackage(gson, deserClbk, new SerClbkImplementation());
        
        
        //NP_ChangeRequestStatus netp2 = new NP_ChangeRequestStatus(true,
        //        "7bb66586-d033-4fcd-8b7f-eff054ff28e7",  // requestUUID
        //        DB_RequestRecord.REQ_STATUS.PASSED,
        //        null);
        //DB_RequestRecord rr = new DB_RequestRecord(
        //        "7037691b-2fed-4ac0-bbb1-dd01677d22bb", // reqUUID
        //        "lgn2", // login
        //        null, // status
        //        2022, // year
        //        11, // month
        //        30, // day
        //        1, // regTimeStart
        //        2, // regTimeStop
        //        5, // adtNum
        //        1, // chairCnt
        //        1, // projCnt
        //        39); // boardCnt 
        //NP_CreateRequestPacket netp2 = new NP_CreateRequestPacket(true, rr, null, null);
        //NP_GetMsgPacket netp = new NP_GetMsgPacket(true, "lgn2", null, null);
        //DB_MsgRecord dbmsgr = new DB_MsgRecord("lgn1", "lgn2", null, "theme2", "body2");
        //NP_SendMsgPacket netp = new NP_SendMsgPacket(true, dbmsgr, null);
        //NP_ResoursePacket netp = new NP_ResoursePacket(true, null, null);
        //NP_AuthorizationPacket netp = new NP_AuthorizationPacket(true, "lgn3", "pswd3", null);
        //NP_RegistrationPacket netp = new NP_RegistrationPacket(true, "lgn3", "pswd3", "Зубенко Михаил Петрович", NP_RegistrationPacket.USER_TYPE.COORDINATOR, null);
        //NP_InfoPacket netp = new NP_InfoPacket("testt000000estsetsekjajkjbh,fwehjb,fawejbh,fweahjbfawehjbkjhkbhjbferfaweknj");
        
        //DB_MsgRecord dbmsgr = new DB_MsgRecord("lgn3", "lgn2", null, "theme333", "body333");
        //NP_SendMsgPacket netp2 = new NP_SendMsgPacket(true, dbmsgr, null);
        
        //ArrayList<DB_RequestRecord.REQ_STATUS> reqStat = new ArrayList<>();
        //reqStat.add(DB_RequestRecord.REQ_STATUS.APPROVED);
        //reqStat.add(DB_RequestRecord.REQ_STATUS.AWAITS_REVIEW);
        //NP_GetSchedulePacket netp2 = new NP_GetSchedulePacket(true, 2022, 11, 30, "lgn2", reqStat, null, null);
        
       
        
        
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
        }
*/
    }
}
