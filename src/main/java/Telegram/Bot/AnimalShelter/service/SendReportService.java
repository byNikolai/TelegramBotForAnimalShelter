package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.listener.TelegramBotUpdatesListener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

/**
 * Сервис SendReportMenuService -отправка отчета
 * @see UpdatesListener
 */
@Service
@RequiredArgsConstructor
public class SendReportService {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    /**
     * Метод сохранения отчета из чата телеграм
     */
    public void downloadReport(Update update) {

        logger.info("Launched method: download_report, for user with id: " +
                update.message().chat().id());
    }
}
