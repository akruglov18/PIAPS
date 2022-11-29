/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside.threads;

import com.unn.piap_serverside.Log;
import com.unn.piap_serverside.data_types.NetPackageWrapper;
import com.unn.piap_serverside.data_types.SCM;
import com.unn.piap_serverside.data_types.UI_ResourseCount;
import com.unn.piap_serverside.database.MsgTableRecord;
import com.unn.piap_serverside.database.RequestsTableRecord;
import com.unn.piap_serverside.database.ResourceTableRecord;
import com.unn.piap_serverside.database.UsersTableRecord;
import com.unn.piap_serverside.interfaces.SCMI;
import com.unn.piap_serverside.net_protocol.DB_ResourseRecord;
import com.unn.piap_serverside.net_protocol.NP_AuthorizationPacket;
import com.unn.piap_serverside.net_protocol.NP_RegistrationPacket;
import com.unn.piap_serverside.net_protocol.NP_ResoursePacket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author STALKER
 */
public class DBManagerThread extends ThreadBase {
    private final SCMI router;
    
    
    // PSQL connection
    private Boolean isConnectedToDB = false;
    private SessionFactory factory = null;
    private Session session = null;
    // PSQL connection
    
    // DB cache
    List<ResourceTableRecord> resourseTable = null;
    // DB cache
    
    
    
    public DBManagerThread(SCMI router) {
        super(SCM.TID.DB_MANAGER_THREAD);
        this.router = router;
    }

    @Override
    protected void terminationCallback() {
        try {
            if (session != null)
                session.close();
            if (factory != null)
                factory.close();
        } catch (HibernateException ex) {
            Log.error("Hibernate exception: " + ex.toString());
        }
    }

    @Override
    protected boolean handlePersonalMessage(SCM msg) {
        switch(msg.type) {
            case RXT_REGISTRATION_REQUEST_ACQUIRED -> {
                if (!isConnectedToDB)
                    return true;
                
                NetPackageWrapper npw = (NetPackageWrapper) msg.body;
                NP_RegistrationPacket rp = (NP_RegistrationPacket) npw.body;
                
                NP_RegistrationPacket new_rp = new NP_RegistrationPacket(false, null,
                        null, null, null, null);
                NetPackageWrapper new_npw = new NetPackageWrapper(npw.connUUID, new_rp);
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.DB_MANAGER_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.DBT_SEND_NP_RESPONSE)
                        .setBody(new_npw);
                try {
                    String hql = String.format("FROM %s WHERE login = '%s'", UsersTableRecord.class.getName(), rp.login);
                    List<UsersTableRecord> list = session.createQuery(hql).getResultList();
                    if (list.isEmpty()) {
                        UsersTableRecord utr = new UsersTableRecord(
                                UUID.randomUUID().toString(),
                                rp.login,
                                rp.password,
                                rp.userType.name(),
                                rp.fio);
                    
                        session.beginTransaction();
                        session.save(utr);
                        session.getTransaction().commit();
                        session.clear();
                        new_rp.respType = NP_RegistrationPacket.RESPONSE_TYPE.REGISTERED;
                    } else {
                        if (list.size() > 1)
                            Log.error(ID.name() + " FOUND MORE THAN 1 LOGIN");
                        new_rp.respType = NP_RegistrationPacket.RESPONSE_TYPE.ERROR_LOGIN_ALREADY_EXISTS;
                    }
                } catch(HibernateException ex) {
                    Log.error("HIBERNATE EX: " + ex.toString());
                    new_rp.respType = NP_RegistrationPacket.RESPONSE_TYPE.ERROR_INTERNAL_SERVER_ERROR;
                } finally {
                    router.sendMessage(nmsg);
                }
                
                /*
                String hql = "FROM " + className + " WHERE userCreate like ':userName'";
                Query query = session.createQuery(hql);
                query.setParameter("userName", userName);
                List<Node> result = query.list();
                */
                return true;
            }
            
            case RXT_AUTHORIZATION_REQUEST_ACQUIRED -> {
                if (!isConnectedToDB)
                    return true;
                
                NetPackageWrapper npw = (NetPackageWrapper) msg.body;
                NP_AuthorizationPacket ap = (NP_AuthorizationPacket) npw.body;
                
                NP_AuthorizationPacket new_ap = new NP_AuthorizationPacket(false, null, null, null);
                NetPackageWrapper new_npw = new NetPackageWrapper(npw.connUUID, new_ap);
                
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.DB_MANAGER_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.DBT_SEND_NP_RESPONSE)
                        .setBody(new_npw);
                
                try {
                    String hql = String.format("FROM %s WHERE login = '%s'", UsersTableRecord.class.getName(), ap.login);
                    List<UsersTableRecord> list = session.createQuery(hql).getResultList();
                    switch (list.size()) {
                        case 0 -> new_ap.respType = NP_AuthorizationPacket.RESPONSE_TYPE.ERROR_USER_NOT_FOUND;
                        case 1 -> {
                            UsersTableRecord user = list.get(0);
                            if (user.getPassword().equals(ap.password))
                                new_ap.respType = NP_AuthorizationPacket.RESPONSE_TYPE.AUTHORIZED;
                            else
                                new_ap.respType = NP_AuthorizationPacket.RESPONSE_TYPE.ERROR_WRONG_PASSWORD;
                        }
                        default -> new_ap.respType = NP_AuthorizationPacket.RESPONSE_TYPE.ERROR_INTERNAL_SERVER_ERROR;
                    }
                } catch (HibernateException ex) {
                    Log.error("HIBERNATE EX: " + ex.toString());
                    new_ap.respType = NP_AuthorizationPacket.RESPONSE_TYPE.ERROR_INTERNAL_SERVER_ERROR;
                } finally {
                    router.sendMessage(nmsg);
                }   
                return true;
            }
            
            case RXT_RESOURSE_GET_ACQUIRED -> {
                if (!isConnectedToDB)
                    return true;
                
                NetPackageWrapper npw = (NetPackageWrapper) msg.body;
                //NP_ResoursePacket rp = (NP_ResoursePacket) npw.body;
                
                NP_ResoursePacket new_rp = new NP_ResoursePacket(false, null, null);
                NetPackageWrapper new_npw = new NetPackageWrapper(npw.connUUID, new_rp);
                
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.DB_MANAGER_THREAD)
                        .setTo(SCM.TID.SOCKET_MANAGER_THREAD)
                        .setType(SCM.TYPE.DBT_SEND_NP_RESPONSE)
                        .setBody(new_npw);
                
                try {
                    ResourceTableRecord r1 = resourseTable.get(0);
                    ResourceTableRecord r2 = resourseTable.get(1);
                    ResourceTableRecord r3 = resourseTable.get(2);
                    ResourceTableRecord r4 = resourseTable.get(3);
                    
                    ArrayList<DB_ResourseRecord> records = new ArrayList<>();
                    records.add(new DB_ResourseRecord(r1.getType(), r1.getName(), r1.getCount()));
                    records.add(new DB_ResourseRecord(r2.getType(), r2.getName(), r2.getCount()));
                    records.add(new DB_ResourseRecord(r3.getType(), r3.getName(), r3.getCount()));
                    records.add(new DB_ResourseRecord(r4.getType(), r4.getName(), r4.getCount()));
                    
                    new_rp.respType = NP_ResoursePacket.RESPONSE_TYPE.OK;
                    new_rp.records = records;
                } catch (Exception ex) {
                    Log.error("IMPOSSIBLE DBMT EX: " + ex.toString());
                    new_rp.respType = NP_ResoursePacket.RESPONSE_TYPE.ERROR_INTERNAL_SERVER_ERROR;
                } finally {
                    router.sendMessage(nmsg);
                }
                return true;
            }
            
            case UIT_DB_UPDATE_RESOURSES -> {
                if (!isConnectedToDB)
                    return true;
                
                if (resourseTable.size() != 4) {
                    Log.error(ID.name() + " CAN'T UPDATE RESOURSE TABLE");
                    return true;
                }
                
                UI_ResourseCount ui_rc = (UI_ResourseCount) msg.body;
                updateLocalResourseTable(ui_rc);
                session.beginTransaction();
                session.update(resourseTable.get(0));
                session.update(resourseTable.get(1));
                session.update(resourseTable.get(2));
                session.update(resourseTable.get(3));
                session.getTransaction().commit();
                session.clear();
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.DB_MANAGER_THREAD)
                        .setTo(SCM.TID.UI_CHANGER_THREAD)
                        .setType(SCM.TYPE.UIT_DB_UPDATE_RESOURSES)
                        .setBody(ui_rc);
                router.sendMessage(nmsg);
                return true;
            }
            
            case UIT_DB_CONNECT -> {
                if (isConnectedToDB)
                    return true;
                
                String conn = "";
                try {
                    factory = new Configuration().configure().buildSessionFactory();
                    session = factory.openSession();
                    isConnectedToDB = true;
                    conn = "подключен";
                } catch (HibernateException ex) {
                    isConnectedToDB = false;
                    conn = "ошибка подключения";
                    try {
                        if (session != null)
                            session.close();
                        if (factory != null)
                            factory.close();
                    } catch (HibernateException ex2) {
                        Log.error("Hibernate exception: " + ex2.toString());
                    }
                } finally {
                    SCM connMsg = SCM.nm()
                        .setFrom(SCM.TID.DB_MANAGER_THREAD)
                        .setTo(SCM.TID.UI_CHANGER_THREAD)
                        .setType(SCM.TYPE.DBT_UPDATE_DB_STATE)
                        .setBody(conn);
                    router.sendMessage(connMsg);
                }
                if (!isConnectedToDB)
                    return true;
                
                
                List<ResourceTableRecord> resTab = getRecords(ResourceTableRecord.class, session);
                if (resTab == null) {
                    Log.error("Resourse table is null");
                    return true;
                }
                
                UI_ResourseCount ui_rc = new UI_ResourseCount(0, 0, 0, 0);
                if (resTab.size() != 4) {
                    Log.info(SCM.TID.DB_MANAGER_THREAD.name() + " is resetting resource table");
                    resetResourseTable();
                } else {
                    ui_rc = getResCntByResTable(resTab);
                }
                resourseTable = getRecords(ResourceTableRecord.class, session);
                session.clear();
                
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.DB_MANAGER_THREAD)
                        .setTo(SCM.TID.UI_CHANGER_THREAD)
                        .setType(SCM.TYPE.UIT_DB_UPDATE_RESOURSES)
                        .setBody(ui_rc);
                router.sendMessage(nmsg);
                return true;
            }
            
            case UIT_DB_RESET -> {
                if (!isConnectedToDB)
                    return true;
                
                clearTable(RequestsTableRecord.class.getName());
                clearTable(MsgTableRecord.class.getName());
                clearTable(UsersTableRecord.class.getName());
                resetResourseTable();
                
                UI_ResourseCount ui_rc = new UI_ResourseCount(0, 0, 0, 0);
                SCM nmsg = SCM.nm()
                        .setFrom(SCM.TID.DB_MANAGER_THREAD)
                        .setTo(SCM.TID.UI_CHANGER_THREAD)
                        .setType(SCM.TYPE.UIT_DB_UPDATE_RESOURSES)
                        .setBody(ui_rc);
                router.sendMessage(nmsg);
                return true;
            }
            
            default -> {
                return false;
            }
        }
    }
    
    private static <T> List<T> getRecords(Class<T> type, Session session) {
        List<T> data = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteria = builder.createQuery(type);
            criteria.from(type);
            data = session.createQuery(criteria).getResultList();
        } catch (HibernateException ex) {
            Log.error("HIBERNATE EXCEPTION: " + ex.toString());
        }
        return data;
    }
    
    private void resetResourseTable() {
        try {
            clearTable(ResourceTableRecord.class.getName());
            session.clear();
            session.beginTransaction();
            session.save(new ResourceTableRecord(new DB_ResourseRecord(DB_ResourseRecord.TYPE.CHAIRS, "Стулья", 0)));
            session.save(new ResourceTableRecord(new DB_ResourseRecord(DB_ResourseRecord.TYPE.BOARDS, "Доски", 0)));
            session.save(new ResourceTableRecord(new DB_ResourseRecord(DB_ResourseRecord.TYPE.AUDIENCES, "Аудитории", 0)));
            session.save(new ResourceTableRecord(new DB_ResourseRecord(DB_ResourseRecord.TYPE.PROJECTORS, "Проекторы", 0)));
            session.getTransaction().commit();
            session.clear();
        } catch (HibernateException ex) {
            Log.error("HIBERNATE ERROR RESETTING RESOURSE TABLE: " + ex.toString());
        }
    }
    
    private void clearTable(String entityClassName) {
        session.beginTransaction();
        String hql = String.format("DELETE FROM %s", entityClassName);
        session.createQuery(hql).executeUpdate();
        session.getTransaction().commit();
        session.clear();
    }
    
    private UI_ResourseCount getResCntByResTable(List<ResourceTableRecord> resTable) {
        UI_ResourseCount uirc = new UI_ResourseCount(0, 0, 0, 0);
        for (ResourceTableRecord rtr : resTable) {
            DB_ResourseRecord dbrr = new DB_ResourseRecord(rtr.getType(), rtr.getName(), rtr.getCount());
            switch(dbrr.type) {
                case AUDIENCES -> uirc.numAudit = dbrr.count;
                case BOARDS -> uirc.numBoard = dbrr.count;
                case CHAIRS -> uirc.numChairs = dbrr.count;
                case PROJECTORS -> uirc.numProect = dbrr.count;
            }
        }
        return uirc;
    }
    
    private void updateLocalResourseTable(UI_ResourseCount ui_rc) {
        for (ResourceTableRecord rtr: resourseTable) {
            switch(DB_ResourseRecord.TYPE.valueOf(rtr.getType())) {
                case AUDIENCES -> rtr.setCount(ui_rc.numAudit);
                case BOARDS -> rtr.setCount(ui_rc.numBoard);
                case CHAIRS -> rtr.setCount(ui_rc.numChairs);
                case PROJECTORS -> rtr.setCount(ui_rc.numProect);
            }
        }
    }
}
