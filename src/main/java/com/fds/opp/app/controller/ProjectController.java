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

        create(session);

        session.close();
    }
    public void create(Session session) throws Exception
    {
        System.out.println("Creating Project...");
        List<Project> listProject = ProjectSync.getListProjectFromAPI();
        for (Project project: listProject) {
            session.beginTransaction();
            session.save(project);
            session.getTransaction().commit();
        }

    }

}
