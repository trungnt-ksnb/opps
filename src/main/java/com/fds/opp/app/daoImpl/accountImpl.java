package com.fds.opp.app.daoImpl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.query.Query;
import com.fds.opp.app.controller.ReadConfig;
import com.fds.opp.app.model.CustomFields;
import com.fds.opp.app.model.Message;
import org.hibernate.Session;
import java.sql.*;
import com.fds.opp.app.model.Account;
import com.fds.opp.app.syncDatabase.AccountSync;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class accountImpl {
    public static void updateAccount(Account account) throws Exception {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try{
            session.beginTransaction();
            session.update(account);
            session.getTransaction().commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        session.close();
    }
    public static Integer getIdCusTomField(){
        String host= ReadConfig.readKey("host");
        String port= ReadConfig.readKey("port");
        String dbname= ReadConfig.readKey("dbname");
        String user= ReadConfig.readKey("userOpenProject");
        String pass= ReadConfig.readKey("passOpenProject");
        String dburl = "jdbc:postgresql://"+host+":"+port+"/"+dbname+"?loggerLevel=OFF";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet ret = null;
        List<CustomFields> listResult = new ArrayList<>();
        try{
            conn = DriverManager.getConnection(dburl, user, pass);
            String QueryGetIDCustomField = "select id from custom_fields where name='"+ ReadConfig.readKey("nameCustomField") + "'";
            pstmt = conn.prepareStatement(QueryGetIDCustomField);
            ret = pstmt.executeQuery();
            while(ret.next()){
                return ret.getInt("id");
            }
            ret.close();
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
        return null;
    }
    public static List<CustomFields> getListCustomField(){
        String host= ReadConfig.readKey("host");
        String port= ReadConfig.readKey("port");
        String dbname= ReadConfig.readKey("dbname");
        String user= ReadConfig.readKey("userOpenProject");
        String pass= ReadConfig.readKey("passOpenProject");
        String dburl = "jdbc:postgresql://"+host+":"+port+"/"+dbname+"?loggerLevel=OFF";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet ret = null;
        List<CustomFields> listResult = new ArrayList<>();
        try{
            conn = DriverManager.getConnection(dburl, user, pass);
            String QueryGetIDCustomField = "select * from custom_values where custom_field_id='" + getIdCusTomField() + "'";
            pstmt = conn.prepareStatement(QueryGetIDCustomField);
            ret = pstmt.executeQuery();
            while(ret.next()){
                CustomFields customFields = new CustomFields();
                customFields.setId(ret.getInt("id"));
                customFields.setCustomized_id(ret.getInt("customized_id"));
                customFields.setValue(ret.getString("value"));
                listResult.add(customFields);
            }
            ret.close();
            return listResult;
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }
    public static void syncCustomFieldTable() throws Exception{
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        List<Account> accountList = AccountSync.getListAccountFromAPI();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (Account temp : accountList) {
                session.saveOrUpdate(temp);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
        String Query = "FROM Account";
        Query query = session.createQuery(Query);
        List<Account> accountList1 = query.getResultList();
        List<CustomFields> customFieldsList = getListCustomField();
        Boolean status;
        for (Account account: accountList1) {
            status = false;
            assert customFieldsList != null;
            for (CustomFields customFields: customFieldsList) {
                if(account.getIdUser() == customFields.getCustomized_id()){
                    account.setCustomField(customFields.getValue());
                    status = true;
                    break;
                }
            }
            if(status){
                updateAccount(account);
            }
        }
        session.close();

    }
    public static void insertNewAccountFromAPI() throws Exception {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        List<Account> accountListAPI = AccountSync.getListAccountFromAPI();
        String Query = "FROM Account";
        Query query = session.createQuery(Query);
        List<Account> accountListDB = query.getResultList();
        for (Account accountDB : accountListDB) {
            for (Account accountAPI: accountListAPI) {
                if(accountAPI.getIdUser() == accountDB.getIdUser()){
                    accountListAPI.remove(accountAPI);
                    break;
                }
            }
        }
        if (accountListAPI.size() != 0){
            for (Account accountSync: accountListAPI) {
                session.beginTransaction();
                session.save(accountSync);
                session.getTransaction().commit();
            }
        }
        session.close();
    }
    public static Account getAccountByCustomField(String usernameTelegram){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            String Query = "FROM Account AS A where A.customField= :customField_id";
            Query query = session.createQuery(Query);
            query.setParameter("customField_id", usernameTelegram);
            Account account = (Account) query.getSingleResult();
            session.close();
            return account;
        }
        catch (Exception ex){
            ex.printStackTrace();
            session.close();
            return null;
        }

    }
    public static Account getAccountByUserName(Message message){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            String Query = "FROM Account AS A where A.username= :username_insert";
            Query query = session.createQuery(Query);
            query.setParameter("username_insert", message.getNameUser());
            Account account = (Account) query.getSingleResult();
            session.close();
            return account;
        }
        catch (Exception ex){
            ex.printStackTrace();
            session.close();
            return null;
        }
    }
}
