package com.fds.opp.app;

import com.fds.opp.app.controller.TelegramBotAPI;
import com.fds.opp.app.daoImpl.accountImpl;
import com.fds.opp.app.daoImpl.memberInProjectImpl;
import com.fds.opp.app.daoImpl.projectImpl;
import com.fds.opp.app.daoImpl.workPackageImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@SpringBootApplication
public class OppsApplication {

	public static void main(String[] args) throws Exception {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		accountImpl.syncCustomFieldTable(session);
		memberInProjectImpl.syncMemberInProject(session);
		projectImpl.syncProject(session);
		workPackageImpl.syncWorkPackage(session);
		session.close();
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot(new TelegramBotAPI());
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		SpringApplication.run(OppsApplication.class, args);
	}

}
