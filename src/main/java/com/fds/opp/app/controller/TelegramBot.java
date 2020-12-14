package com.fds.opp.app.controller;

import com.fds.opp.app.model.Message;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {
    public String getBotUsername() {
        return "FDS Notification";
    }

    public String getBotToken() {
        return "1457198760:AAE0y_ODocLo_j8Bj6hs7QqkqKvVhnXDA7o";
    }

    public void onUpdateReceived(Update update){
        String command=update.getMessage().getText();
        SendMessage message = new SendMessage();
        if(command.equals("/start")){
            System.out.println(update.getMessage().getFrom().getFirstName());
            message.setText(update.getMessage().getFrom().getFirstName());
        }
        message.setChatId(update.getMessage().getChatId());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
