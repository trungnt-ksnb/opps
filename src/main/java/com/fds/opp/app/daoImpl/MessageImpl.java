package com.fds.opp.app.daoImpl;

import com.fds.opp.app.model.MemberInProject;
import com.fds.opp.app.model.Message;
import com.fds.opp.app.syncDatabase.MemberInProjectSync;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class MessageImpl {
    public static void addNewMessage(Session session, Message message) {
        try {
            System.out.println("Adding Table Message...");
            session.beginTransaction();
            session.save(message);
            session.getTransaction().commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void updateStatusMessage (Session session, Message message, String status)
    {
        try{
            Message MessageToUpdate = session.get(Message.class, message.getIdMessage());
            MessageToUpdate.setStatus(status);
            session.beginTransaction();
            session.update(MessageToUpdate);
            session.getTransaction().commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
