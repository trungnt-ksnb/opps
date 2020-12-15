package com.fds.opp.app.daoImpl;

import java.util.List;

import org.hibernate.Session;

import com.fds.opp.app.model.Account;
import com.fds.opp.app.syncDatabase.AccountSync;
import org.hibernate.Transaction;


public class accountImpl {

    public static void createAccount(Session session) throws Exception {
        System.out.println("create into database");
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

}
