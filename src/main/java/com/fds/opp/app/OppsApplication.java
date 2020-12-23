package com.fds.opp.app;

import com.fds.opp.app.controller.InitBotTelegram;
import com.fds.opp.app.controller.ReadConfig;
import com.fds.opp.app.controller.TelegramBotAPI;
import com.fds.opp.app.daoImpl.accountImpl;
import com.fds.opp.app.daoImpl.memberInProjectImpl;
import com.fds.opp.app.daoImpl.projectImpl;
import com.fds.opp.app.daoImpl.workPackageImpl;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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
	public static void UpdateProperties(){
		try {
			PropertiesConfiguration properties = new PropertiesConfiguration("application.properties");
			properties.setProperty("statusDB", "True");
			properties.save();
			System.out.println("config.properties updated Successfully!!");
		} catch (ConfigurationException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void main(String[] args) throws Exception {
		if(ReadConfig.readKey("statusDB").equals("False")){
			SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
			Session session = sessionFactory.openSession();
			accountImpl.syncCustomFieldTable(); // KHi chạy sẽ xóa hết botID
			memberInProjectImpl.syncMemberInProject();
			projectImpl.syncProject();
			workPackageImpl.syncWorkPackage();
			session.close();
			UpdateProperties();
		}
		InitBotTelegram.Init();
		SpringApplication.run(OppsApplication.class, args);
	}

}
