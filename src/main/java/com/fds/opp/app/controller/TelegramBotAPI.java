package com.fds.opp.app.controller;

import com.fds.opp.app.daoImpl.MessageImpl;
import com.fds.opp.app.model.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

public class TelegramBotAPI extends TelegramLongPollingBot {
    public String getBotUsername() {
        return "siriBot";
    }

    public String getBotToken() {
        return "1457198760:AAE0y_ODocLo_j8Bj6hs7QqkqKvVhnXDA7o";
    }

    public void onUpdateReceived(Update update){
        String command=update.getMessage().getText();
        SendMessage message = new SendMessage();
        if(command.equals("/start")){
            System.out.println(update.getMessage().getFrom().getFirstName());
            message.setText(update.getMessage().getChatId().toString());
        }
        message.setChatId(update.getMessage().getChatId());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void sendMessage(Message message){
        SendMessage messageDetail = new SendMessage();
        messageDetail.setText(message.getMessage());
        messageDetail.setChatId("716275003");
        try {
            execute(messageDetail);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public static void callExec(){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        List<Message> listMessagePending = MessageImpl.readPendingListMessage(session);
        session.close();
        TelegramBotAPI telegramBot = new TelegramBotAPI();
        for (Message message: listMessagePending) {
            telegramBot.sendMessage(message);
        }
    }

}
