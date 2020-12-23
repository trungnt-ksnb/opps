package com.fds.opp.app.daoImpl;

import com.fds.opp.app.model.MemberInProject;
import com.fds.opp.app.syncDatabase.MemberInProjectSync;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Service
@Transactional
public class memberInProjectImpl {
    public static void create() throws Exception
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        System.out.println("Creating Table Project...");
        List<MemberInProject> listMemberInProject = MemberInProjectSync.getListMemberInProject();
        for (MemberInProject memberInProject: listMemberInProject) {
            session.beginTransaction();
            session.save(memberInProject);
            session.getTransaction().commit();
        }
        session.close();
    }
    public static void syncMemberInProject()
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
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
        session.close();
    }

    public static void update(MemberInProject memberUpdate)
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try{
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
        session.close();
    }
    public static void delete(Integer idMemberInProject)
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try{
            MemberInProject objectToDelete = (MemberInProject) session.get(MemberInProject.class, idMemberInProject);
            session.beginTransaction();
            session.delete(objectToDelete);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        session.close();
    }
    public static List<MemberInProject> read(String nameProject)
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try
        {
            List<MemberInProject> result = new ArrayList<>();
            List<MemberInProject> findMemberInProject = session.createQuery("from MemberInProject", MemberInProject.class).getResultList();
            session.close();
            for (MemberInProject memberInProject:findMemberInProject) {
                if(memberInProject.getNameProject().equals(nameProject)){
                    result.add(memberInProject);
                    System.out.println(memberInProject);
                }
            }

            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            session.close();
            return null;
        }

    }
}
