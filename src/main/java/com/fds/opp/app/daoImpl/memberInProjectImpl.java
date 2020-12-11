package com.fds.opp.app.daoImpl;

import com.fds.opp.app.model.MemberInProject;
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
public class memberInProjectImpl {
    @Test
    public void crud() throws Exception {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
////        create(session);
////        Project pj = new Project();
////        pj.setIdProject(1);
////        pj.setNameProject("Updating Project");
////        pj.setDescriptionProject("Updated Complete!");
////        update(session,pj);
////        Project pj_Delete = new Project();
////        pj_Delete.setNameProject("Project Test 1");
////        pj_Delete.setIdProject(34);
////        pj_Delete.setDescriptionProject("");
////        delete(session, pj_Delete);
////        read(session, 1);
//
        syncMemberInProject(session);
        session.close();
    }
    public static void create(Session session) throws Exception
    {
        System.out.println("Creating Table Project...");
        List<MemberInProject> listMemberInProject = MemberInProjectSync.getListMemberInProject();
        for (MemberInProject memberInProject: listMemberInProject) {
            session.beginTransaction();
            session.save(memberInProject);
            session.getTransaction().commit();
        }
    }
    public static void addNewMemberInProject(Session session, MemberInProject memberInProject){
        System.out.println("Creating Table Project...");
        session.beginTransaction();
        session.save(memberInProject);
        session.getTransaction().commit();
    }
    public static void syncMemberInProject(Session session)
    {
        try {
            List<MemberInProject> listMemberInProject = MemberInProjectSync.getListMemberInProject();
            for (MemberInProject mip:listMemberInProject) {
                session.beginTransaction();
                session.saveOrUpdate(mip);
                session.getTransaction().commit();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void update(Session session, MemberInProject memberUpdate)
    {
        try{
            System.out.println("Updating Table Project...");
            MemberInProject memberInPorjectBefore = (MemberInProject) session.get(MemberInProject.class, memberUpdate.getIdMemberShip());
            memberInPorjectBefore.setIdMemberShip(memberUpdate.getIdMemberShip());
            memberInPorjectBefore.setNameUser(memberUpdate.getNameProject());
            memberInPorjectBefore.setRoles(memberUpdate.getRoles());
            memberInPorjectBefore.setNameUser(memberUpdate.getNameUser());
            session.beginTransaction();
            session.saveOrUpdate(memberInPorjectBefore);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void delete(Session session, Integer idMemberInProject)
    {
        try{
            System.out.println("Deleting Table Project...");
            MemberInProject objectToDelete = (MemberInProject) session.get(MemberInProject.class, idMemberInProject);
            session.beginTransaction();
            session.delete(objectToDelete);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void read(Session session, Integer id)
    {
        try
        {
            System.out.println("Reading Database");
            MemberInProject findMemberInProject = (MemberInProject) session.get(MemberInProject.class, id);
            System.out.println(findMemberInProject);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } session.close();
    }
}
