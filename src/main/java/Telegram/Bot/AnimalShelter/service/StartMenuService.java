package Telegram.Bot.AnimalShelter.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface StartMenuService {
    SendMessage startMenu(Update update);
}
