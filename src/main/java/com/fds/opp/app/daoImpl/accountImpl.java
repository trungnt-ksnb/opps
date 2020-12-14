package com.fds.opp.app.daoImpl;

import java.util.List;

import org.hibernate.Session;

import com.fds.opp.app.hibernateUtil.HibernateUtils;
import com.fds.opp.app.model.Account;
import com.fds.opp.app.syncDatabase.AccountSync;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class accountImpl {
	public static void main(String[] args) throws Exception {
		accountImpl.execute();
	}
	public static void execute() throws Exception{
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
		createObj(session);
		session.close();
	}
	
	public static void createObj(Session session) throws Exception {
		System.out.println("create into database");
		List<Account> accList = AccountSync.getListAccountFromAPI();
		for(Account Account : accList) {
			session.beginTransaction();
			session.save(Account);
			session.getTransaction().commit();
		}
	}
	public static Account findAccountById(Session session, int idAccount) throws Exception{
		Account result = session.get(Account.class, idAccount);
		return result;
	}
	
}
