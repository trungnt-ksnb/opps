package com.fds.opp.app.controller;

import com.fds.opp.app.model.Project;
import com.fds.opp.app.syncDatabase.ProjectSync;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ProjectController {
    @Test
    public void crud() throws Exception {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
//        create(session);
//        Project pj = new Project();
//        pj.setIdProject(1);
//        pj.setNameProject("Updating Project");
//        pj.setDescriptionProject("Updated Complete!");
//        update(session,pj);
//        Project pj_Delete = new Project();
//        pj_Delete.setNameProject("Project Test 1");
//        pj_Delete.setIdProject(34);
//        pj_Delete.setDescriptionProject("");
//        delete(session, pj_Delete);
        read(session, 1);
        session.close();
    }
    public void create(Session session) throws Exception
    {
        System.out.println("Creating Table Project...");
        List<Project> listProject = ProjectSync.getListProjectFromAPI();
        for (Project project: listProject) {
            session.beginTransaction();
            session.save(project);
            session.getTransaction().commit();
        }
    }
    public void update(Session session, Project project) throws Exception
    {
        try{
            System.out.println("Updating Table Project...");
            Project projectAfterUpdate = (Project) session.get(Project.class, project.getIdProject());
            projectAfterUpdate.setNameProject(project.getNameProject());
            projectAfterUpdate.setDescriptionProject(project.getDescriptionProject());
            session.beginTransaction();
            session.saveOrUpdate(projectAfterUpdate);
            session.getTransaction().commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void delete(Session session, Project project) throws Exception
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
    public void read(Session session, Integer id)
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

}
