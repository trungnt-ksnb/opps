package com.fds.opp.app.daoImpl;

import java.util.ArrayList;
import java.util.List;

import com.fds.opp.app.model.CustomFields;
import com.fds.opp.app.model.Message;
import org.hibernate.Session;
import java.sql.*;
import com.fds.opp.app.model.Account;
import com.fds.opp.app.syncDatabase.AccountSync;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Test;


public class accountImpl {
    public static void updateAccount(Session session, Account account) throws Exception {
        try{
            session.beginTransaction();
            session.update(account);
            session.getTransaction().commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void delete(Session session, Account account) throws Exception {
        Account accountAfterDelete = session.get(Account.class, account.getIdUser());
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(accountAfterDelete);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void syncAccount(Session session) throws Exception {
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

    }
    public void test(){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        List<Account> listAccount = session.createQuery("From Account").getResultList();
        session.close();
        System.out.println(listAccount);
        for (Account account: listAccount) {
            System.out.println(account.getUsername());
        }
    }
    public static List<CustomFields> getListCustomField(){
        String host="localhost";
        String port="5436";
        String dbname="openproject";
        String user="postgres";
        String pass="p4ssw0rd";
        String dburl = "jdbc:postgresql://"+host+":"+port+"/"+dbname+"?loggerLevel=OFF";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet ret = null;
        List<CustomFields> listResult = new ArrayList<>();
        try{
            conn = DriverManager.getConnection(dburl, user, pass);
            pstmt = conn.prepareStatement("select * from custom_values where customized_type = 'Principal';");
            ret = pstmt.executeQuery();
            while(ret.next()){
                CustomFields customFields = new CustomFields();
                customFields.setId(ret.getInt("id"));
                customFields.setCustomized_id(ret.getInt("customized_id"));
                customFields.setValue(ret.getString("value"));
                listResult.add(customFields);
            }
            ret.close();
            for (CustomFields cf: listResult) {
                System.out.println(cf.getValue());
            }
            return listResult;
        }catch(SQLException ex){
            ex.printStackTrace();
            return null;
        }
    }
    public static void SyncCustomField() throws Exception {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        List<Account> accountList = session.createQuery("From Account").getResultList();
        boolean status = false;
//        session.close();
        List<CustomFields> customFieldsList = getListCustomField();
        for (Account account: accountList) {
            status = false;
            for (CustomFields customFields: customFieldsList) {
                if(account.getIdUser() == customFields.getCustomized_id()){
                    account.setCustomField(customFields.getValue());
                     status = true;
                    break;
                }
            }
            if(status==true){
                updateAccount(session, account);
            }
        }
        session.close();
    }
    public static void syncCustomFieldTable(Session session) throws Exception{
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
        org.hibernate.query.Query query = session.createQuery(Query);
        List<Account> accountList1 = query.getResultList();
        List<CustomFields> customFieldsList = getListCustomField();
        Boolean status;
        for (Account account: accountList1) {
            status = false;
            for (CustomFields customFields: customFieldsList) {
                if(account.getIdUser() == customFields.getCustomized_id()){
                    account.setCustomField(customFields.getValue());
                    status = true;
                    break;
                }
            }
            if(status==true){
                updateAccount(session, account);
            }
        }

    }
    public static void insertNewAccountFromAPI(Session session) throws Exception {
        List<Account> accountListAPI = AccountSync.getListAccountFromAPI();
        String Query = "FROM Account";
        org.hibernate.query.Query query = session.createQuery(Query);
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
    }
    public static Account getAccountByCustomField(Session session, String usernameTelegram){
        try {
            String Query = "FROM Account AS A where A.customField= :customField_id";
            org.hibernate.query.Query query = session.createQuery(Query);
            query.setParameter("customField_id", usernameTelegram);
            Account account = (Account) query.getSingleResult();
            return account;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    public static Account getAccountByUserName(Session session, Message message){
        try {
            String Query = "FROM Account AS A where A.username= :username_insert";
            org.hibernate.query.Query query = session.createQuery(Query);
            query.setParameter("username_insert", message.getNameUser());
            Account account = (Account) query.getSingleResult();
            return account;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
