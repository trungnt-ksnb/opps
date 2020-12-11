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
    public static void create(Session session)
    {
        try {
            System.out.println("Creating Table Project...");
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
    }
    public static void addProject(Session session, Project project)
    {
        try {
            System.out.println("Adding Project...");
            session.beginTransaction();
            session.save(project);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void update(Session session, Project project)
    {
        try{
            System.out.println("Updating Table Project...");
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
    }
    public static void delete(Session session, Project project) throws Exception
    {
        try{
            System.out.println("Deleting Table Project...");
            Project projectAfterDelete = (Project) session.get(Project.class, project.getIdProject());
            session.beginTransaction();
            session.delete(projectAfterDelete);
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
            Project findProject = (Project) session.get(Project.class, id);
            System.out.println(findProject);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void syncProject(Session session)
    {
        try {
            List<Project> listProject = ProjectSync.getListProjectFromAPI();
            System.out.println("Syncing table Project ...");
            for (Project wp: listProject) {
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
