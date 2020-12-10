package com.fds.opp.app.daoImpl;

import java.util.List;

import org.hibernate.Session;

import com.fds.opp.app.hibernateUtil.HibernateUtils;
import com.fds.opp.app.model.Account;
import com.fds.opp.app.syncDatabase.AccountSync;

public class accountImpl {
	public void execute() throws Exception{
		Session session = HibernateUtils.getSessionFactory().openSession();
		
		createObj(session);
		
		session.close();
	}
	
	public void createObj (Session session) throws Exception {
		System.out.println("create into database");
		List<Account> accList = AccountSync.getListAccountFromAPI();
		for(Account Account : accList) {
			session.beginTransaction();
			session.save(Account);
			session.getTransaction().commit();
		}
	}
	
}
