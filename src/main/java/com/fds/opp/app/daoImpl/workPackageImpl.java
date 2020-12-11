package com.fds.opp.app.daoImpl;

import com.fds.opp.app.model.WorkPackage;
import com.fds.opp.app.syncDatabase.WorkPackageSync;
import org.hibernate.Session;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class workPackageImpl {
    public static void create(Session session) throws Exception {
        try {
            System.out.println("Create table Workpackage...");
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
    }
    public static void addWorkPackage(Session session, WorkPackage workPackage){
        try {
            session.beginTransaction();
            session.save(workPackage);
            session.getTransaction().commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void update(Session session, WorkPackage workPackage)
    {
        try {
            System.out.println("Updating 1 value");
            WorkPackage wpUpdate = (WorkPackage) session.get(WorkPackage.class, workPackage.getIdWorkPackage());
            //        wpUpdate.setIdWorkPackage(workPackage.getIdWorkPackage());
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
            session.beginTransaction();
            session.save(wpUpdate);
            session.getTransaction().commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static void delete(Session session, Integer idWorkPackage)
    {
        try {
            System.out.println("Deleting 1 value");
            WorkPackage wpDelete = (WorkPackage) session.get(WorkPackage.class, idWorkPackage);
            session.beginTransaction();
            session.delete(wpDelete);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void syncWorkPackage(Session session)
    {
        try {
            List<WorkPackage> listWorkspace = WorkPackageSync.getListWorkPackage();
            System.out.println("Syncing table WorkPackage ...");
            for (WorkPackage wp: listWorkspace) {
                session.beginTransaction();
                session.saveOrUpdate(wp);
                session.getTransaction().commit();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
