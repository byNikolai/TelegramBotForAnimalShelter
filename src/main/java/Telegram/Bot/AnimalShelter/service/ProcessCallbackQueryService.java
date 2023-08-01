package Telegram.Bot.AnimalShelter.service;

import com.pengrad.telegrambot.model.Update;

public interface ProcessCallbackQueryService {
    void processCallbackQuery (Update update);
}
