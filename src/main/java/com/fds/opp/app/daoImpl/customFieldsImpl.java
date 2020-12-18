package com.fds.opp.app.daoImpl;

import com.fds.opp.app.model.CustomFields;
import com.fds.opp.app.model.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class customFieldsImpl {
    public static void saveListCustomField (CustomFields customFields)
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try{
            session.beginTransaction();
            session.saveOrUpdate(customFields);
            session.getTransaction().commit();
            session.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            session.close();
        }
    }
}
