package com.fds.opp.app;

import com.fds.opp.app.daoImpl.accountImpl;
import com.fds.opp.app.daoImpl.memberInProjectImpl;
import com.fds.opp.app.daoImpl.projectImpl;
import com.fds.opp.app.daoImpl.workPackageImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OppsApplication {

	public static void main(String[] args) throws Exception {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		accountImpl.createObj(session);
		memberInProjectImpl.syncMemberInProject(session);
		projectImpl.syncProject(session);
		workPackageImpl.syncWorkPackage(session);
		session.close();
		SpringApplication.run(OppsApplication.class, args);
	}

}
