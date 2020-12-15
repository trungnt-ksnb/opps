package com.fds.opp.app.controller;


import com.fds.opp.app.daoImpl.accountImpl;
import com.fds.opp.app.model.Account;
import com.fds.opp.app.model.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import java.util.ArrayList;
import java.util.List;

public class AccountController {
    // litscustomfield (nameUser)
    // role
    // message

    private final List<Message> mslist = new ArrayList<>();

//    public boolean compareAccount() throws Exception {
//        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        Session session = sessionFactory.openSession();
//        List<Account> accountList = accountImpl.readListAccount(session);
//        for(Account temp : accountList){
//            if(temp.getCustomField().equals()){
//
//            }
//        }
//        return false;
//    }



}
