package Telegram.Bot.AnimalShelter.listner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Класс для обработки сообщений
 */
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    /**
     * Объявление logger для логирования
     */
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    /**
     * Объявление бота
     */
    private final TelegramBot telegramBot;
    /**
     * Инжектим бота
     *
     * @param telegramBot - бот
     */
    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    /**
     * Настройка бота на получение входящих обновлений
     */
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }
    /**
     * Метод для обработки сообщений
     *
     * @param updates available updates - обновления бота
     * @return - возвращает идентификатор последнего обработанного обновления или подтверждает их все
     */
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message() != null && "/start".equals(update.message().text())) {
                telegramBot.execute(startMenu(update.message().chat().id()));
            }
            if (update.callbackQuery() != null) {
                if (update.callbackQuery().data().equals("1")) {
                    telegramBot.execute(newUserMenu(update.callbackQuery().message().chat().id()));
                } else {
                    telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(), update.callbackQuery().data()));
                }
            }

        });        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Метод создает Главного меню
     *
     * @param chatId - id чата
     * @return - возвращает сообщение с меню
     */
    private SendMessage startMenu(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button1 = new InlineKeyboardButton("Find out information about the shelter").callbackData("1");
        InlineKeyboardButton button2 = new InlineKeyboardButton("How to adopt an animal from the shelter").callbackData("2");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Send in a pet report").callbackData("3");
        InlineKeyboardButton button4 = new InlineKeyboardButton("Call for a volunteer").callbackData("4");
        inlineKeyboardMarkup.addRow(button1);
        inlineKeyboardMarkup.addRow(button2);
        inlineKeyboardMarkup.addRow(button3);
        inlineKeyboardMarkup.addRow(button4);
        return new SendMessage(chatId, "Welcome to AnimalShelterBot!").replyMarkup(inlineKeyboardMarkup);
    }
    /**
     * Метод создает меню - информация о приюте
     *
     * @param chatId - id чата
     * @return - возвращает сообщение с меню
     */
    private SendMessage newUserMenu(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button1 = new InlineKeyboardButton("About us").callbackData("line1");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Shelter's working hours and locations").callbackData("line2");
        InlineKeyboardButton button3 = new InlineKeyboardButton("general safety recommendations at the shelter area").callbackData("line3");
        InlineKeyboardButton button4 = new InlineKeyboardButton("Leave your contacts").callbackData("DataBase");
        InlineKeyboardButton button5 = new InlineKeyboardButton("Call for a volunteer").callbackData("5");
        inlineKeyboardMarkup.addRow(button1);
        inlineKeyboardMarkup.addRow(button2);
        inlineKeyboardMarkup.addRow(button3);
        inlineKeyboardMarkup.addRow(button4);
        inlineKeyboardMarkup.addRow(button5);
        return new SendMessage(chatId, "Menu for our new clients").replyMarkup(inlineKeyboardMarkup);
    }

    /**
     * Данный метод отпраляет сообщение пользователю
     *
     * @param chatId          - id чата
     * @param receivedMessage - текст сообщения пользователю
     */
    public void messaging(long chatId, String receivedMessage) {
        logger.info("Sending message");
        SendMessage message = new SendMessage(chatId, receivedMessage);
        SendResponse response = telegramBot.execute(message);
    }
}
