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

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class MessageImpl {
    public static void addNewMessage(Session session, Message message) {
        try {
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
    public static List<Message> readPendingListMessage(Session session){
        try {
            List<Message> listResult = new ArrayList<>();
            List<Message> listMessage = session.createQuery("from Message", Message.class).getResultList();
            for (Message message: listMessage) {
                if(message.getStatus().equals("Pending...")){
                    listResult.add(message);
                }
            }
            return listResult;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
