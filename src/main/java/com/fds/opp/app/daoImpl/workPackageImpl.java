package com.fds.opp.app.daoImpl;

import com.fds.opp.app.model.WorkPackage;
import com.fds.opp.app.syncDatabase.WorkPackageSync;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class workPackageImpl {

    public static void create() throws Exception {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            List<WorkPackage> listWorkpackage = WorkPackageSync.getListWorkPackage();
            for (WorkPackage wp : listWorkpackage) {
                session.beginTransaction();
                session.save(wp);
                session.getTransaction().commit();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        session.close();
    }
    public static void addWorkPackage(WorkPackage workPackage){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(workPackage);
            session.getTransaction().commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        session.close();
    }

    public static void update(WorkPackage workPackage)
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            WorkPackage wpUpdate = (WorkPackage) session.get(WorkPackage.class, workPackage.getIdWorkPackage());
            wpUpdate.setNameWorkPackage(workPackage.getNameWorkPackage());
            wpUpdate.setDescriptionWorkPackage(workPackage.getDescriptionWorkPackage());
            wpUpdate.setStartDate(workPackage.getStartDate());
            wpUpdate.setDueDate(workPackage.getDueDate());
            wpUpdate.setDeadlineDate(workPackage.getDeadlineDate());
            wpUpdate.setPriorityWorkPackage(workPackage.getPriorityWorkPackage());
            wpUpdate.setStatusWorkPackage(workPackage.getStatusWorkPackage());
            wpUpdate.setTypeWorkPackage(workPackage.getTypeWorkPackage());
            wpUpdate.setNameProject(workPackage.getNameProject());
            wpUpdate.setNameUser(workPackage.getNameUser());
            wpUpdate.setAuthor(workPackage.getAuthor());
            wpUpdate.setAccountable(workPackage.getAccountable());
            session.beginTransaction();
            session.update(wpUpdate);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.close();
    }
    public static void delete(Integer idWorkPackage)
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            WorkPackage wpDelete = (WorkPackage) session.get(WorkPackage.class, idWorkPackage);
            session.beginTransaction();
            session.delete(wpDelete);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        session.close();
    }
    public static void syncWorkPackage()
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            List<WorkPackage> listWorkspace = WorkPackageSync.getListWorkPackage();
            for (WorkPackage wp: listWorkspace) {
                session.beginTransaction();
                session.save(wp);
                session.getTransaction().commit();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        session.close();
    }
    public static String DateString(String dateString) throws Exception{
        String dateStr = dateString;
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = (Date)formatter.parse(dateStr);
        System.out.println(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" +         cal.get(Calendar.YEAR);
        return formatedDate;
    }
}
