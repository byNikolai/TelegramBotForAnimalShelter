package Telegram.Bot.AnimalShelter.service.impl;

import Telegram.Bot.AnimalShelter.listener.TelegramBotUpdatesListener;
import Telegram.Bot.AnimalShelter.repository.UserRepository;
import Telegram.Bot.AnimalShelter.service.ProcessMessageService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ProcessMessageService- обработка сообщений от пользователя
 */
@Service
@RequiredArgsConstructor
public class ProcessMessageServiceImpl implements ProcessMessageService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    private final UserRepository userRepository;


// вызываем сервис нового хозяина собаки


//  вызываем  сервис нового хозяина кошки


    /**
     * Обработка принятых сообщений от пользователя
     * обновления

     * Метод, присылающий приветствие для пользователя
     * обновления

     * Метод, выдающий информацию для пользователя
     * обновления

     * метод инфо по волонтером
     * обновления
     * return
     */
    public void info(Update update) {

        logger.info("Launched method: info, for user with id: " +
                update.message().chat().id());

        String infoMsg = "Инфо приют";

        SendMessage info = new SendMessage(update.message().chat().id(), infoMsg);
        telegramBot.execute(info);
    }

    /**
     * Метод - отправка информацию по связи с волонтером для пользователя
     * обновление
     * return - сообщение пользователю
     */
}
