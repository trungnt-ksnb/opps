package com.fds.opp.app.bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class TelegramBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        String command = update.getMessage().getText();
        SendMessage message = new SendMessage();



        System.out.println(update.getMessage().getText());

//        message.setChatId(update.getMessage().getChatId());
        message.getChatId();
        try {
            execute(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "siriBot";
    }

    @Override
    public String getBotToken() {
        return "1442982441:AAHnizqKepZxQ9pQtAVifJ9Uj_RV6OWbWfY";
    }
}
