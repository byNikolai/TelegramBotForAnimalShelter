package Telegram.Bot.AnimalShelter.service.impl;

import Telegram.Bot.AnimalShelter.constant.Commands;
import Telegram.Bot.AnimalShelter.listener.TelegramBotUpdatesListener;
import Telegram.Bot.AnimalShelter.repository.UserRepository;
import Telegram.Bot.AnimalShelter.service.StartMenuService;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * StartMenuService- сервис для стартового меню
 */
@Service
@Setter
@Getter
@RequiredArgsConstructor
public class StartMenuServiceImpl implements StartMenuService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final UserRepository userRepository;


    /**
     * Метод для запуска меню
     *
     * @param update доступное обновление
     * @return меню для пользователя с кнопками
     */
    public SendMessage startMenu(Update update) {
        String message;
        if (userRepository.findUserByChatId(update.callbackQuery().message().chat().id()).isDog()) {
            logger.info("start menu - dog shelter");

            message = "Помощь в подборе щенка";

        } else {
            logger.info("start menu - kitten shelter");

            message = "Помощь в подборе щенка kitten";
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); // InlineKeyboardMarkup – _клавиатура_
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(Commands.INFO.getDescription())
                        .callbackData(Commands.INFO.getCallbackData())
        );
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(Commands.ANIMAL_INFO.getDescription())
                        .callbackData(Commands.ANIMAL_INFO.getCallbackData())

        );
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(Commands.SUBMIT_REPORT.getDescription())
                        .callbackData(Commands.SUBMIT_REPORT.getCallbackData())
        );
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(Commands.VOLUNTEER.getDescription())
                        .callbackData(Commands.VOLUNTEER.getCallbackData())
        );
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(" текст/ссылка ")
                        .switchInlineQuery("текст/ссылка")
        );


        SendMessage mes = new SendMessage(update.callbackQuery().message().chat().id(), message);
        mes.replyMarkup(inlineKeyboardMarkup);

        return mes;
    }

    /**
     * Метод, присылающий информацию по связи с волонтером для пользователя
     *
     * @param update доступное обновление
     * @return сообщение пользователю
     */
    public SendMessage volunteerMenu(Update update) {
        logger.info("Launched method: volunteer, for user with id: " +
                update.callbackQuery().message().chat().id());

        SendMessage volunteer = new SendMessage(update.callbackQuery().message().chat().id(), "текст/ссылка");
        return volunteer;
    }
}
