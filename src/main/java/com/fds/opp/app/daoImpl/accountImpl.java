package com.fds.opp.app.daoImpl;

import java.util.ArrayList;
import java.util.List;

import com.fds.opp.app.model.CustomFields;
import org.hibernate.Session;
import java.sql.*;
import com.fds.opp.app.model.Account;
import com.fds.opp.app.syncDatabase.AccountSync;
import org.hibernate.Transaction;
import org.junit.Test;


public class accountImpl {

    public static void createAccount(Session session) throws Exception {
        List<Account> accList = AccountSync.getListAccountFromAPI();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (Account Account : accList) {
                session.save(Account);
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateAccount(Session session, Account account) throws Exception {
        Account accountAfterUpdate = session.get(Account.class, account.getIdUser());
        accountAfterUpdate.setUsername(account.getUsername());
        accountAfterUpdate.setIsAdmin(account.getIsAdmin());
        accountAfterUpdate.setFirstname(account.getFirstname());
        accountAfterUpdate.setLastname(account.getLastname());
        accountAfterUpdate.setEmail(account.getEmail());
        accountAfterUpdate.setStatus(account.getStatus());
        accountAfterUpdate.setFullname(account.getFullname());
        accountAfterUpdate.setCustomField(account.getCustomField());
        accountAfterUpdate.setBotId(account.getBotId());
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(accountAfterUpdate);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAccount(Session session, Account account) throws Exception {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(account);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
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

    public static Account read(Session session, Integer id) throws Exception {
        Account findAccount = session.get(Account.class, id);
        return findAccount;
    }

    public static List<Account> readListAccount(Session session) throws Exception {
        List<Account> accountList = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            accountList = session.createQuery("FROM account", Account.class).getResultList();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        }
        return accountList;

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
    @Test
    public void getListCustomField(){
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
            System.out.println("done.");
            for (CustomFields cf: listResult) {
                System.out.println(cf.getValue());
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

}
