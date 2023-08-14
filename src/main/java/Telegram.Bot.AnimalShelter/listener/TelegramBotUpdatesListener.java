package Telegram.Bot.AnimalShelter.listener;

import Telegram.Bot.AnimalShelter.buttons.Constants;
import Telegram.Bot.AnimalShelter.entity.*;
import Telegram.Bot.AnimalShelter.menu_constructor.MenuConstructor;
import Telegram.Bot.AnimalShelter.repository.UserRepository;
import Telegram.Bot.AnimalShelter.service.AdaptationService;
import Telegram.Bot.AnimalShelter.service.ReportService;
import Telegram.Bot.AnimalShelter.service.UserService;
import Telegram.Bot.AnimalShelter.service.VolunteerService;
import Telegram.Bot.AnimalShelter.service.impl.CatShelterServiceImpl;
import Telegram.Bot.AnimalShelter.service.impl.DogShelterServiceImpl;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CatShelterServiceImpl catShelterService;
    private final DogShelterServiceImpl dogShelterService;
    private final VolunteerService volunteerService;
    private final AdaptationService adaptationService;
    private final ReportService reportService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private ResourceBundle messagesBundle = ResourceBundle.getBundle("messages");

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        MenuConstructor menuConstructor = new MenuConstructor(telegramBot, catShelterService, dogShelterService);
        try {
            updates.stream()
                    .filter(update -> update.message() != null)
                    .forEach(update -> {
                        logger.info("Обработка обновления: {}", update);
                        Message message = update.message();
                        Long chatId = message.chat().id();
                        Chat chat = message.chat();
                        String text = message.text();
                        if (!userRepository.existsById(chatId)) {
                            userService.create(new User(chatId, chat.firstName(), chat.lastName(), ""));
                        }
                        User user = userService.getById(chatId);

                        if (message.photo() != null) {
                            getReport(message);
                            return;
                        }

                        Pattern pattern = Pattern.compile("(\\d+)");
                        Matcher matcher = pattern.matcher(text);
                        if (matcher.find()) {
                            getContact(chatId, text);
                            return;
                        }

                        String shelterType = user.getShelterType();
                        if (shelterType != null) {
                            if (shelterType.equals("DOG")) {
                                dogShelterService.getShelter().forEach(dogShelter -> {
                                    if (dogShelter.getName().equals(text)) {
                                        user.setShelterName(dogShelter.getName());
                                        menuConstructor.sendMenuDog(chatId);
                                        userService.update(user);
                                    }
                                });
                            } else if (shelterType.equals("CAT")) {
                                catShelterService.getShelter().forEach(catShelter -> {
                                    if (catShelter.getName().equals(text)) {
                                        user.setShelterName(catShelter.getName());
                                        menuConstructor.sendMenuCat(chatId);
                                        userService.update(user);
                                    }
                                });
                            }
                        }

                        switch (text) {
                            case "/start": {
                                logger.info("Запустили бота/выбрали приют - ID:{}", chatId);
                                menuConstructor.sendStartMenu(chatId);
                                break;
                            }
                            case Constants.CAT_SHELTER: {
                                sendMenuStage("CAT", chatId);
                                break;
                            }
                            case Constants.DOG_SHELTER: {
                                sendMenuStage("DOG", chatId);
                                break;
                            }
                            case Constants.INFORMATION_ABOUT_SHELTER: {
                                logger.info("Узнать информацию о приюте - ID:{}", chatId);
                                shelterType = userService.getShelterById(chatId);
                                if ("CAT".equals(shelterType)) {
                                    menuConstructor.sendListMenuCat(chatId);
                                } else if ("DOG".equals(shelterType)) {
                                    menuConstructor.sendListMenuDog(chatId);
                                }
                                break;
                            }
                            case Constants.MAIN_MENU: {
                                logger.info("Главное меню - ID:{}", chatId);
                                user.setShelterType(null);
                                user.setShelterName(null);
                                userService.update(user);
                                menuConstructor.sendStartMenu(chatId);
                                break;
                            }
                            case Constants.WORKING_HOURS: {
                                logger.info("Расписание работы - ID:{}", chatId);
                                if (user.getShelterType().equals("CAT")) {
                                    sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getWorkingHours());
                                } else if (user.getShelterType().equals("DOG")) {
                                    sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getWorkingHours());
                                }
                                break;
                            }
                            case Constants.LIST_OF_CATS: {

                                logger.info("Список кошек - ID:{}", chatId);
                                List<Cat> catList = catShelterService.getShelterByName(user.getShelterName())
                                        .getList().stream()
                                        .filter(cat -> cat.getOwnerId() == null)
                                        .collect(Collectors.toList());
                                if (catList.isEmpty()) {
                                    sendMessage(chatId, "We don't have cats currently");
                                    return;
                                }
                                sendMessage(chatId, getStringFromList(catList));
                                break;
                            }
                            case Constants.LIST_OF_DOGS: {
                                logger.info("Список собак - ID:{}", chatId);
                                List<Dog> dogList = dogShelterService.getShelterByName(user.getShelterName())
                                        .getList().stream()
                                        .filter(dog -> dog.getOwnerId() == null)
                                        .collect(Collectors.toList());
                                if (dogList.isEmpty()) {
                                    sendMessage(chatId, "We don't have dogs currently");
                                    return;
                                }
                                sendMessage(chatId, getStringFromList(dogList));
                                break;
                            }
                            case Constants.ABOUT_SPECIFIC_SHELTER: {
                                logger.info("О приюте - ID:{}", chatId);
                                if (user.getShelterType().equals("CAT")) {
                                    sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getAboutMe());
                                } else if (user.getShelterType().equals("DOG")) {
                                    sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getAboutMe());
                                }
                                break;
                            }
                            case Constants.SAFETY_POLICY_RECOMMENDATIONS: {
                                logger.info("Рекомендации о ТБ - ID:{}", chatId);
                                if (user.getShelterType().equals("CAT")) {
                                    sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getSafety());
                                } else if (user.getShelterType().equals("DOG")) {
                                    sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getSafety());
                                }
                                break;
                            }
                            case Constants.CONTACT_DETAILS: {
                                logger.info("Как отправить свои данные для связи - ID:{}", chatId);
                                sendMessage(chatId, "Enter the phone number in the format: 89997776655");
                                break;
                            }

                            case Constants.CONTACT_SECURITY: {
                                logger.info("Контактные данные охраны - ID:{}", chatId);
                                if (user.getShelterType().equals("CAT")) {
                                    sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getSecurity());
                                } else if (user.getShelterType().equals("DOG")) {
                                    sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getSecurity());
                                }
                                break;
                            }
                            case Constants.FAQ: {
                                logger.info("Часто задаваемые вопросы - ID:{}", chatId);
                                if (user.getShelterType().equals("CAT")) {
                                    menuConstructor.menuCat(chatId);
                                } else if (user.getShelterType().equals("DOG")) {
                                    menuConstructor.menuDog(chatId);
                                }
                                break;
                            }
                            case Constants.BACK_TO_ALL_ABOUT_CATS: {
                                logger.info("Все о кошках - ID:{}", chatId);
                                menuConstructor.menuCat(chatId);
                                break;
                            }
                            case Constants.BACK_TO_ALL_ABOUT_DOGS: {
                                logger.info("Все о собаках - ID:{}", chatId);
                                menuConstructor.menuDog(chatId);
                                break;
                            }
                            case Constants.RULES_FOR_DATING_A_CAT: {
                                logger.info("Правила знакомства с котами- ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("ANIMAL_DATING_RULES"));
                                break;
                            }
                            case Constants.RULES_FOR_DATING_A_DOG: {
                                logger.info("Правила знакомства с собаками- ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("ANIMAL_DATING_RULES"));
                                break;
                            }
                            case Constants.CAT_TRANSPORTATION: {
                                logger.info("Перевозка котов - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("TRANSPORTATION_OF_THE_ANIMAL"));
                                break;
                            }
                            case Constants.DOG_TRANSPORTATION: {
                                logger.info("Перевозка собак - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("TRANSPORTATION_OF_THE_ANIMAL"));
                                break;
                            }
                            case Constants.REQUIRED_DOCUMENTS: {
                                logger.info("Необходимые документы - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("LIST_OF_DOCUMENTS"));
                                break;
                            }
                            case Constants.LIST_OF_REASONS: {
                                logger.info("Список причин для отказа выдачи питомца - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("LIST_OF_REASON_FOR_DENY"));
                                break;
                            }
                            case Constants.RECOMMENDATIONS_FOR_DOGS: {
                                logger.info("Рекомендации для собак - ID:{}", chatId);
                                menuConstructor.rulesForDogs(chatId);
                                break;
                            }
                            case Constants.RECOMMENDATIONS_FOR_CATS: {
                                logger.info("Рекомендации для кошек - ID:{}", chatId);
                                menuConstructor.rulesForCats(chatId);
                                break;
                            }
                            case Constants.PUPPY_SETUP: {
                                logger.info("Обустройство щенка - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("RECOMMENDATIONS_HOME_SETUP_KITTEN_PUPPY"));
                                break;
                            }
                            case Constants.KITTEN_SETUP: {
                                logger.info("Обустройство котенка - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("RECOMMENDATIONS_HOME_SETUP_KITTEN_PUPPY"));
                                break;
                            }
                            case Constants.ADULT_CAT_SETUP: {
                                logger.info("Обустройство взрослой кошки - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("RECOMMENDATIONS_HOME_SETUP_ADULT_ANIMAL"));
                                break;
                            }
                            case Constants.ADULT_DOG_SETUP: {
                                logger.info("Обустройство взрослой собаки - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("RECOMMENDATIONS_HOME_SETUP_ADULT_ANIMAL"));
                                break;
                            }
                            case Constants.SETUP_FOR_CAT_WITH_DISABILITIES: {
                                logger.info("Обустройство кошки с ограниченными возможностями - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("SETUP_FOR_DOG_WITH_DISABILITIES_INFO"));
                                break;
                            }
                            case Constants.SETUP_FOR_DOG_WITH_DISABILITIES: {
                                logger.info("Обустройство собаки с ограниченными возможностями - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("SETUP_FOR_DOG_WITH_DISABILITIES_INFO"));
                                break;
                            }
                            case Constants.DOG_HANDLERS_ADVICE: {
                                logger.info("Советы кинолога - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("DOG_HANDLERS_ADVICE_INFO"));
                                break;
                            }
                            case Constants.PROVEN_DOG_HANDLERS: {
                                logger.info("Проверенные кинологи для обращения - ID:{}", chatId);
                                sendMessage(chatId, messagesBundle.getString("DOG_HANDLERS_CONTACTS"));
                                break;
                            }
                            case Constants.SEND_REPORT: {
                                logger.info("Отправить форму отчёта - ID:{}", chatId);
                                sendReportExample(chatId);
                                break;
                            }
                            case Constants.CALL_VOLUNTEER: {
                                logger.info("Позвать волонтёра - ID:{}", chatId);
                                sendMessageToVolunteers(message);
                                sendMessage(chatId, "The first available volunteer will get back to you shortly");
                                break;
                            }
                        }
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CONFIRMED_UPDATES_ALL;
    }

    public void sendReportPhotoToVolunteer(Long reportId, Long volunteerId) {
        GetFile request = new GetFile(reportService.getById(reportId).getPhotoId());
        GetFileResponse getFileResponse = telegramBot.execute(request);
        Adaptation adaptation = adaptationService.getById(reportService.getById(reportId).getAdaptationPeriodId());
        if (getFileResponse.isOk()) {
            try {
                byte[] image = telegramBot.getFileContent(getFileResponse.file());
                SendPhoto sendPhoto = new SendPhoto(volunteerId, image);
                sendPhoto.caption("Id Owner: " + adaptation.getOwnerId() + "\n" +
                        "Id Adaptation period: " + adaptation.getId() + "\n" +
                        "Id Report:" + reportId);
                telegramBot.execute(sendPhoto);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendResponse sendResponse = telegramBot.execute(new SendMessage(chatId, message));
        if (!sendResponse.isOk()) {
            logger.error(sendResponse.description());
        }
    }

    private void getContact(Long chatId, String text) {
        Pattern pattern = Pattern.compile("^(\\d{11})$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            User byId = userService.getById(chatId);
            byId.setPhoneNumber(matcher.group(1));
            userService.update(byId);
            sendMessage(chatId, "Phone number accepted");
        } else {
            sendMessage(chatId, "Incorrect input");
        }
        logger.info("Пришел телефон от пользователя - ID:{} тел:{} ", chatId, text);
    }

    private void sendMessageToVolunteers(Message message) {
        Long chatId = message.chat().id();
        Integer integer = message.messageId();
        for (Volunteer volunteer : volunteerService.getAll()) {
            telegramBot.execute(new ForwardMessage(volunteer.getTelegramId(), chatId, integer));
        }
    }

    private void sendMessageToVolunteers(Long chatId) {
        for (Volunteer volunteer : volunteerService.getAll()) {
            telegramBot.execute(new SendMessage(volunteer.getTelegramId(), "The owner of the animal with id " + chatId +
                    "has not sent reports for more than two days!"));
        }
    }

    private void sendReportExample(Long chatId) {
        try {
            byte[] photo = Files.readAllBytes(
                    Paths.get(Objects.requireNonNull(UpdatesListener.class.getResource("/img/cat.jpg")).toURI()));
            SendPhoto sendPhoto = new SendPhoto(chatId, photo);
            sendPhoto.caption("Ration:_______; \n" +
                    "Overall health:_______; \n" +
                    "Behavior:_______;\n");
            telegramBot.execute(sendPhoto);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void getReport(Message message) {
        PhotoSize photoSize = message.photo()[message.photo().length - 1];
        String caption = message.caption();
        Long chatId = message.chat().id();
        try {
            reportService.createWithTelegramData(photoSize.fileId(), caption, chatId);
            sendMessage(chatId, "Your report has been accepted.");
        } catch (Exception e) {
            sendMessage(chatId, e.getMessage());
        }
    }

    private void sendMenuStage(String shelterType, Long chatId) {
        logger.info("Вызвано меню выбора приюта - ID:{}", chatId);
        MenuConstructor menuConstructor = new MenuConstructor(telegramBot, catShelterService, dogShelterService);
        User user = userService.getById(chatId);
        user.setShelterType(shelterType);
        userService.update(user);
        menuConstructor.sendMenuStage(chatId);
    }

    private String getStringFromList(List<?> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(o -> sb.append(o)
                .append("\n")
                .append("============").append("\n"));
        return sb.toString();
    }

    @Scheduled(cron = "@daily")
    private void sendWarning() {
        for (User user : userService.getAll()) {
            for (Adaptation adaptation : adaptationService.getAllByOwnerId(user.getTelegramId())) {
                if ((adaptation.getReports().size() < 45 && !adaptation.getDateOfLastReport().isEqual(adaptation.getEndDate())) &&
                        adaptation.getDateOfLastReport().isBefore(LocalDate.now().minusDays(2))) {
                    sendMessage(user.getTelegramId(), "You haven't sent a report in over two days. \n" +
                            "Please send a report or contact the volunteers. \n");
                    sendMessageToVolunteers(user.getTelegramId());
                }
            }

        }
    }

    @Scheduled(cron = "@daily")
    private void sendAdaptationStatus() {
        for (User user : userService.getAll()) {
            for (Adaptation adaptation : adaptationService.getAllByOwnerId(user.getTelegramId())) {
                if (adaptation.getResult().equals(Adaptation.Result.UNSUCCESSFUL)) {
                    sendMessage(user.getTelegramId(), Constants.ADAPTATION_NOT_SUCCESSFUL);
                } else if (adaptation.getResult().equals(Adaptation.Result.EXTENDED)) {
                    sendMessage(user.getTelegramId(), Constants.ADAPTATION_EXTENDED);
                } else if (adaptation.getResult().equals(Adaptation.Result.SUCCESSFUL)) {
                    sendMessage(user.getTelegramId(), Constants.SUCCESSFUL);
                }
            }
        }
    }
}
