package com.fds.opp.app.controller;

import com.fds.opp.app.daoImpl.MessageImpl;
import com.fds.opp.app.daoImpl.accountImpl;
import com.fds.opp.app.model.Account;
import com.fds.opp.app.model.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.Test;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

public class TelegramBotAPI extends TelegramLongPollingBot {
    public String getBotUsername() {
        return ReadConfig.readKey("namebot");
    }

    public String getBotToken() {
        return ReadConfig.readKey("bottokentelegram");
    }

    public void onUpdateReceived(Update update){
        String command=update.getMessage().getText();
        SendMessage message = new SendMessage();
        if(command.equals("/start")){
            accountImpl aImpl = new accountImpl();
            System.out.println(update.getMessage().getFrom().getFirstName());
            message.setText("Đang thực hiện cập nhật công việc của bạn!");
            String usernameTelegram = update.getMessage().getFrom().getUserName();
            Long botIdTelegram = update.getMessage().getChatId();
            System.out.println(usernameTelegram);
            System.out.println(botIdTelegram);
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            try {
                aImpl.insertNewAccountFromAPI();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                aImpl.syncCustomFieldTable();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Account account = accountImpl.getAccountByCustomField(usernameTelegram);
            account.setBotId(botIdTelegram.intValue());
            System.out.println(botIdTelegram.intValue());
            try {
                accountImpl.updateAccount(account);
            } catch (Exception e) {
                e.printStackTrace();
            }
            session.close();
        }
        message.setChatId(update.getMessage().getChatId());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(Message message, Integer BotId){
        SendMessage messageDetail = new SendMessage();
        messageDetail.setText(message.getMessage());
        messageDetail.setChatId(String.valueOf(BotId));
        try {
            execute(messageDetail);
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            MessageImpl.updateStatusMessage(message, "Done");
            session.close();
            System.out.println("Done Send Message!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public static void callExec(){
        List<Message> listMessagePending = MessageImpl.readPendingListMessage();
        TelegramBotAPI telegramBot = new TelegramBotAPI();
        for (Message message: listMessagePending) {
            Account account = accountImpl.getAccountByUserName(message);
            if(account.getBotId() != null){
                telegramBot.sendMessage(message, account.getBotId());
            }
        }

    }

//    public void scheduleFixedDelayTask() throws Exception {
//        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//        Session session = sessionFactory.openSession();
//        List<Message> listMessagePending = MessageImpl.readPendingListMessage(session);
//        if(listMessagePending.size() != 0){
//            TelegramBotAPI telegramBot = new TelegramBotAPI();
//            for (Message message: listMessagePending) {
//                String Query = "FROM Account AS A where A.username= :username_insert";
//                org.hibernate.query.Query query = session.createQuery(Query);
//                query.setParameter("username_insert", message.getNameUser());
//                Account account = (Account) query.getSingleResult();
//                if(account.getBotId() != null){
//                    telegramBot.sendMessage(message, account.getBotId());
//                }
//            }
//            session.close();
//        } else{
//            System.out.println("Kiểm tra Pending Message : 0");
//        }
//    }

}
