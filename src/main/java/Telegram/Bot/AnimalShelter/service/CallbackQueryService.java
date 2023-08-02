package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.constant.Commands;
import Telegram.Bot.AnimalShelter.entity.User;
import Telegram.Bot.AnimalShelter.listener.TelegramBotUpdatesListener;
import Telegram.Bot.AnimalShelter.repository.UserRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
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
public class CallbackQueryService {
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
        switch (Commands.valueOf(command)) {
            // !!!Стартовое меню (startMenuService)!!!
            case TAKE_A_KITTEN:
                if (userRepository.findUserByChatId(update.callbackQuery().message().chat().id()) == null) {
                    userRepository.save(new User(update.callbackQuery().message().chat().id(),
                            update.callbackQuery().message().chat().firstName(),
                            false));
                } else {
                    User user = userRepository.findUserByChatId(update.callbackQuery().message().chat().id());
                    user.setDog(false); //setIsDog
                    userRepository.save(user);
                }
                telegramBot.execute(startMenuService.startMenu(update));
                break;
            case TAKE_THE_DOG:
                if (userRepository.findUserByChatId(update.callbackQuery().message().chat().id()) == null) {
                    userRepository.save(new User(update.callbackQuery().message().chat().id(),
                            update.callbackQuery().message().chat().firstName(),
                            true));
                } else {
                    User user = userRepository.findUserByChatId(update.callbackQuery().message().chat().id());
                    user.setDog(true);
                    userRepository.save(user);
                }
                telegramBot.execute(startMenuService.startMenu(update));
                break;
            case BACK:
                telegramBot.execute(startMenuService.startMenu(update));
                break;

            default:
                telegramBot.execute(new SendMessage(update.callbackQuery().message().chat().id(),
                        "Не получилось? Не переживай! Команда <<Классика>> скоро исправит!"));
        }
    }
}
