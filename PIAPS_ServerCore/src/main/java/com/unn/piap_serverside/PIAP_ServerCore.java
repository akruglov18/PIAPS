/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.unn.piap_serverside;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unn.piap_serverside.database.MsgTableRecord;
import com.unn.piap_serverside.database.UsersTableRecord;
import com.unn.piap_serverside.net_protocol.DB_ResourseRecord;
import com.unn.piap_serverside.net_protocol.NP_AuthorizationPacket;
import com.unn.piap_serverside.net_protocol.NP_InfoPacket;
import com.unn.piap_serverside.net_protocol.NP_RegistrationPacket;
import com.unn.piap_serverside.net_protocol.NP_ResoursePacket;
import com.unn.piap_serverside.net_protocol.NetPackage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
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
            System.out.println(errClass.getName() + " error: " + errorStr);
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
    }
    
    public static class SerClbkImplementation implements NetPackage.SerializeCallbackInterface {
        @Override
        public void serializationError(String errStr) {
            System.out.println(errStr);
        }
    }
    
    */
    public static void main(String[] args) {
        /*
        Gson gson = new GsonBuilder().create();
        DeserClbkImplementation deserClbk = new DeserClbkImplementation();
        NetPackage np = new NetPackage(gson, deserClbk, new SerClbkImplementation());
        
       
        NP_ResoursePacket netp = new NP_ResoursePacket(true, null, null);
        //NP_AuthorizationPacket netp = new NP_AuthorizationPacket(true, "lgn", "pswd2", null);
        //NP_RegistrationPacket netp = new NP_RegistrationPacket(true, "lgn", "pswd", "Зубенко Михаил Петрович", NP_RegistrationPacket.USER_TYPE.CUSTOMER, null);
        //NP_InfoPacket netp = new NP_InfoPacket("testt000000estsetsekjajkjbh,fwehjb,fawejbh,fweahjbfawehjbkjhkbhjbferfaweknj");
        
        
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
