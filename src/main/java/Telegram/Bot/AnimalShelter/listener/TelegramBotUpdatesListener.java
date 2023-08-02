package Telegram.Bot.AnimalShelter.listener;

import Telegram.Bot.AnimalShelter.service.CallbackQueryService;
import Telegram.Bot.AnimalShelter.service.MessageService;
import Telegram.Bot.AnimalShelter.service.SendReportService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * TelegramBotUpdatesListener - обработка доступных обновлений в чате
 */
@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    private final CallbackQueryService callbackQueryService;
    private final MessageService messageService;
    private final SendReportService sendReportService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {

        // Process your updates here//
        try {
            updates.forEach(this::processUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    private void processUpdate(Update update) {
        if (update.message() != null) {
            if(update.message().photo() != null || update.message().caption()!=null){
                sendReportService.downloadReport(update);
            } else {
                messageService.processMessage(update);}
        } else if (update.callbackQuery() != null) {
            callbackQueryService.processCallbackQuery(update);
        }
    }
}