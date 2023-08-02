package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.constant.Commands;
import Telegram.Bot.AnimalShelter.listener.TelegramBotUpdatesListener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
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
public class MessageService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;


    /**
     * Обработка принятых сообщений от пользователя
     * @param update доступные обновления
     */


    public void processMessage(Update update) {
        if(update.message().contact() != null){
            createContactInDB(update);
            return;
        }
        else if (update.message().text() == null) {
            return;
        }
        StringBuilder text = new StringBuilder(update.message().text());
        text.delete(0, 1);
        Commands command;
        try {
            command = Commands.valueOf(text.toString().toUpperCase());
        } catch (IllegalArgumentException e){
            logger.info("Command not found in the list");
            telegramBot.execute(new SendMessage(update.message().chat().id(), "Некорректный ввод, повторите запрос"));
            return;
        }

        switch (command) {
            case START:
                greeting(update);
                break;
            case INFO:
                info(update);
                break;
            case VOLUNTEER:
                volunteerMenu(update);
                break;
        }
    }

    /**
     * Метод, присылающий приветствие для пользователя
     * @param update доступное обновление
     */

    public void greeting(Update update) {

        logger.info("method: greeting, for user with id: " +
                update.message().chat().id());

        SendMessage greeting = new SendMessage(update.message().chat().id(),
                "Привет, " + update.message().from().firstName() + "!приветственный текст");

        telegramBot.execute(greeting);

        String desc = update.message().from().firstName() + ", если повезет, то вы можете стать новым хозяином собаки или кошки! ";

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("Хочу \uD83D\uDC15 ").callbackData(Commands.TAKE_THE_DOG.getCallbackData()),
                new InlineKeyboardButton("Хочу \uD83D\uDC08").callbackData(Commands.TAKE_A_KITTEN.getCallbackData())
        );

        SendMessage description = new SendMessage(update.message().chat().id(), desc);
        description.replyMarkup(inlineKeyboardMarkup);

        telegramBot.execute(description);

    }

    /**
     * Метод, выдающий информацию для пользователя
     * @param update доступное обновление
     */
    public void info(Update update) {

        logger.info("Launched method: info, for user with id: " +
                update.message().chat().id());

        String infoMsg = "текст о приюте";

        SendMessage info = new SendMessage(update.message().chat().id(), infoMsg);
        telegramBot.execute(info);
    }

    /**
     * Метод, присылающий информацию по связи с волонтером для пользователя
     * @param update доступное обновление
     * @return сообщение пользователю
     */
    private void volunteerMenu(Update update) {

        logger.info("Launched method: volunteer, for user with id: " +
                update.message().chat().id());

        telegramBot.execute(new SendMessage(update.message().chat().id(),
                "Волонтер свяжется"));
    }

    private void createContactInDB(Update update) {
        logger.info("Created owner in database: " +
                update.message().chat().id());

        Long chatId = update.message().chat().id();

        if (update.message().contact() != null) {
        }
        telegramBot.execute(new SendMessage(update.message().chat().id(),
                "С Вами свяжется наш специалист, чтобы подтвердить визит"));
    }}
