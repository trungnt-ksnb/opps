package com.fds.opp.app.controller;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class InitBotTelegram {
    public static void Init(){
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TelegramBotAPI());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
