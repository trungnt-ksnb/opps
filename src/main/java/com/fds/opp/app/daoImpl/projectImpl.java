package com.fds.opp.app.daoImpl;

import com.fds.opp.app.model.Project;
import com.fds.opp.app.syncDatabase.ProjectSync;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class projectImpl {
    public static void create()
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            List<Project> listProject = ProjectSync.getListProjectFromAPI();
            for (Project project: listProject) {
                session.beginTransaction();
                session.save(project);
                session.getTransaction().commit();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        session.close();
    }
    public static void addProject(Project project)
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(project);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        session.close();
    }
    public static void update(Project project)
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try{
            Project projectAfterUpdate = (Project) session.get(Project.class, project.getIdProject());
            projectAfterUpdate.setNameProject(project.getNameProject());
            projectAfterUpdate.setDescriptionProject(project.getDescriptionProject());
            projectAfterUpdate.setStatus(project.getStatus());
            session.beginTransaction();
            session.saveOrUpdate(projectAfterUpdate);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        session.close();
    }
    public static void delete(Project project) throws Exception
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try{
            Project projectAfterDelete = (Project) session.get(Project.class, project.getIdProject());
            session.beginTransaction();
            session.delete(projectAfterDelete);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        session.close();
    }
    public static void read(Integer id)
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try
        {
            Project findProject = (Project) session.get(Project.class, id);
            System.out.println(findProject);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        session.close();
    }
    public static void syncProject()
    {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            List<Project> listProject = ProjectSync.getListProjectFromAPI();
            for (Project wp: listProject) {
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

}
