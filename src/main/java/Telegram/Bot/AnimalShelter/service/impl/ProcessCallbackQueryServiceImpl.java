package Telegram.Bot.AnimalShelter.service.impl;

import Telegram.Bot.AnimalShelter.constant.Commands;
import Telegram.Bot.AnimalShelter.listener.TelegramBotUpdatesListener;
import Telegram.Bot.AnimalShelter.repository.UserRepository;
import Telegram.Bot.AnimalShelter.service.ProcessCallbackQueryService;
import Telegram.Bot.AnimalShelter.service.StartMenuService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * ProcessCallbackQueryService- обработка кнопок при нажатии
 */
@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ProcessCallbackQueryServiceImpl implements ProcessCallbackQueryService {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    private final StartMenuService startMenuService;
    private final UserRepository userRepository;

    public void processCallbackQuery (Update update){
        String data = update.callbackQuery().data();
        String command = null;
        for (Commands currentCommand : Commands.values()){
            if (currentCommand.getCallbackData().equals(data)){
                command = currentCommand.name();
            }
        }
    }


//данные о приюте


// сервисное меню - отправка отчетов

}
