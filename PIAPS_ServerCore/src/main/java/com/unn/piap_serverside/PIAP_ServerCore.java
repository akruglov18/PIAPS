/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.unn.piap_serverside;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unn.piap_serverside.database.UsersTableRecord;
import com.unn.piap_serverside.net_protocol.NP_InfoPacket;
import com.unn.piap_serverside.net_protocol.NP_RegistrationRequestPacket;
import com.unn.piap_serverside.net_protocol.NetPackage;
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
        public NP_RegistrationRequestPacket rrp;
        
        
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
            System.out.println("RX: " + infoPacket.info);
        }

        @Override
        public void np_registrationRequestPacketAcquired(NP_RegistrationRequestPacket rrp) {
            this.rrp = rrp;
            System.out.println("RX: " + rrp.login + " " + rrp.password);
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
        
        NP_InfoPacket np_ip1 = new NP_InfoPacket("info_info");
        NP_InfoPacket np_ip2 = new NP_InfoPacket();
        NP_InfoPacket np_ip3 = null;
        NP_RegistrationRequestPacket np_rrp1 = new NP_RegistrationRequestPacket("login", null);
        NP_RegistrationRequestPacket np_rrp2 = new NP_RegistrationRequestPacket("login", "password");
        
        
        // TX THREAD
        String s1 = np.serialize(np_ip1);
        String s2 = np.serialize(np_ip2);
        String s3 = np.serialize(np_ip3);
        String s4 = np.serialize(np_rrp1);
        String s5 = np.serialize(np_rrp2);
        System.out.println("TX: np_ip1 " + s1);
        System.out.println("TX: np_ip2 " + s2);
        System.out.println("TX: np_ip3 " + s3);
        System.out.println("TX: np_rrp1 " + s4);
        System.out.println("TX: np_rrp2 " + s5);
        
        System.out.println();
        System.out.println();
        
        // RX THREAD
        np.deserialize(s1);
        np.deserialize(s2);
        np.deserialize(s3);
        np.deserialize(s4);
        np.deserialize(s5);
        */
        
        
        
        
        /*
        SessionFactory factory = null;
        try {
            factory = new Configuration().configure().buildSessionFactory();
            Session s = factory.openSession();
            s.beginTransaction();
            s.save(new UsersTableRecord(UUID.randomUUID().toString(), "loginYES", "passwordYES", "DUDE", "Зубенко Михаил Петрович"));
            s.getTransaction().commit();
            s.clear();
            s.close();
            factory.close();
            System.out.println("OK");
        } catch (HibernateException ex) {
            System.out.println("Exception: " + ex.toString());
        }
        */
    }
}
