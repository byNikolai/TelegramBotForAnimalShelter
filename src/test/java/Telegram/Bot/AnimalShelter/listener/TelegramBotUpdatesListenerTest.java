package Telegram.Bot.AnimalShelter.listener;

import Telegram.Bot.AnimalShelter.buttons.Constants;
import Telegram.Bot.AnimalShelter.entity.*;
import Telegram.Bot.AnimalShelter.repository.UserRepository;
import Telegram.Bot.AnimalShelter.service.AdaptationService;
import Telegram.Bot.AnimalShelter.service.ReportService;
import Telegram.Bot.AnimalShelter.service.UserService;
import Telegram.Bot.AnimalShelter.service.VolunteerService;
import Telegram.Bot.AnimalShelter.service.impl.CatShelterServiceImpl;
import Telegram.Bot.AnimalShelter.service.impl.DogShelterServiceImpl;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {
    @Mock
    TelegramBot telegramBot;
    @Mock
    UserService userService;
    @Mock
    CatShelterServiceImpl catShelterService;
    @Mock
    DogShelterServiceImpl dogShelterService;
    @Mock
    VolunteerService volunteerService;
    @Mock
    ReportService reportService;
    @Mock
    AdaptationService adaptationService;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    TelegramBotUpdatesListener telegramBotUpdatesListener;

    private final SendResponse sendResponse = BotUtils.fromJson("{ok: true}", SendResponse.class);
    private final GetFileResponse getFileResponse = BotUtils.fromJson("{ok : true, file : {file_id: qwerty}}", GetFileResponse.class);

    private final String messageTextJson = Files.readString(
                    Path.of(Objects.requireNonNull(TelegramBotUpdatesListenerTest.class.getResource("/message_update.json")).toURI()));

    private final byte[] photo = Files.readAllBytes(
                    Paths.get(Objects.requireNonNull(UpdatesListener.class.getResource("/img/cat.jpg")).toURI()));


    private final User catUser = new User(1L, "CatUnderGlue", "", "", "CAT", "SomeShelterName");
    private final User dogUser = new User(1L, "CatUnderGlue", "", "", "DOG", "SomeShelterName");
    private final CatShelter catShelter = new CatShelter(1L, "SomeShelterName", "location",
            "timetable", "about me", "security", "safety advice");
    private final DogShelter dogShelter = new DogShelter(1L, "SomeShelterName", "location",
            "timetable", "about me", "security", "safety advice");
    private final Cat cat = new Cat(1L, "CatName", 2, true, true, null, 1L);
    private final Dog dog = new Dog(1L, "DogName", 2, true, true, null, 1L);
    private final Report report = new Report(1L, 1L, LocalDate.now(), "qwerty", "ration", "health", "behavior");
    private final Adaptation adaptation;

    {
        adaptation = new Adaptation(List.of(report), 1L, 1L, 1L,
                Adaptation.AnimalType.CAT, LocalDate.now(), LocalDate.now().plusDays(30),
                LocalDate.now().minusDays(1), Adaptation.Result.IN_PROGRESS);
    }

    TelegramBotUpdatesListenerTest() throws IOException, URISyntaxException {
    }
    private SendMessage getSendMessage(Update update) {
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        Mockito.reset(telegramBot);
        return argumentCaptor.getValue();
    }
    private SendMessage testerForCatShelter(Update update) {
        when(userService.getById(any())).thenReturn(catUser);
        when(catShelterService.getShelterByName(any())).thenReturn(catShelter);
        return getSendMessage(update);
    }

    private SendMessage testerForDogShelter(Update update) {
        when(userService.getById(any())).thenReturn(dogUser);
        when(dogShelterService.getShelterByName(any())).thenReturn(dogShelter);
        return getSendMessage(update);
    }

    @Test
    void handleStartCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "/start"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("сюда нужно много текста", actual.getParameters().get("text"));
     }
    @Test
    void handleChooseCatsSheltersCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Cat shelter"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Выберите:", actual.getParameters().get("text"));
    }


    @Test
    void handleSendVolunteerReportPhotoCommand() throws IOException {
        when(telegramBot.execute(any(GetFile.class))).thenReturn(getFileResponse);
        when(reportService.getById(any())).thenReturn(report);
        when(adaptationService.getById(1L)).thenReturn(adaptation);
        when(telegramBot.getFileContent(any())).thenReturn(photo);
        telegramBotUpdatesListener.sendReportPhotoToVolunteer(1L, 1L);
        ArgumentCaptor<SendPhoto> argumentCaptor = ArgumentCaptor.forClass(SendPhoto.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendPhoto actual = argumentCaptor.getValue();
        Assertions.assertEquals(1L, actual.getParameters().get("chat_id"));
        Assertions.assertEquals(("Id Owner: " + adaptation.getOwnerId() + "\n" +
                "Id Adaptation period: " + adaptation.getId() + "\n" +
                "Id Report:" + report.getId()), actual.getParameters().get("caption"));
    }
    @Test
    void handleChooseDogsSheltersCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Dog shelter"), Update.class);
        when(userService.getById(any())).thenReturn(dogUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Выберите:", actual.getParameters().get("text"));
    }
    @Test
    void handleSendMessageToVolunteersCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Call a volunteer"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        when(volunteerService.getAll()).thenReturn(List.of(new Volunteer(123L, "firstName", "lastName")));
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("The first available volunteer will get back to you shortly", actual.getParameters().get("text"));
    }
    @Test
    void handleSendReportExampleCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Send report form"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendPhoto> argumentCaptor = ArgumentCaptor.forClass(SendPhoto.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        Mockito.reset(telegramBot);
        SendPhoto actual = argumentCaptor.getValue();
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Ration:_______; \n" +
        "Overall health:_______; \n" +
                "Behavior:_______;\n", actual.getParameters().get("caption"));
    }
    @Test
    void handleDogHandlersContactsCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Our proven dog handlers"), Update.class);
        when(userService.getById(any())).thenReturn(dogUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.DOG_HANDLERS_CONTACTS, actual.getParameters().get("text"));
    }
    @Test
    void handleListOfSheltersCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Get information about the shelter"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);

        when(userService.getShelterById(any())).thenReturn("CAT");
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Список кошачьих приютов", actual.getParameters().get("text"));

        when(userService.getShelterById(any())).thenReturn("DOG");
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Список собачьих приютов", actual.getParameters().get("text"));
    }
    @Test
    void handleDogHandlersAdviseCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Advices from dog handlers"), Update.class);
        when(userService.getById(any())).thenReturn(dogUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.DOG_HANDLERS_ADVICE, actual.getParameters().get("text"));
    }
    @Test
    void handleImprovementForDisabledAnimalCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Setup for dog with disabilities"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.RECOMMENDATIONS_HOME_ARRANGEMENT_ADULT_ANIMAL, actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Setup for cat with disabilities"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.RECOMMENDATIONS_HOME_ARRANGEMENT_ADULT_ANIMAL, actual.getParameters().get("text"));
    }
    @Test
    void handleImprovementForAdultAnimalCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Setup for adult dogs"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.RECOMMENDATIONS_HOME_ARRANGEMENT_ADULT_ANIMAL, actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Setup for adult cat"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.RECOMMENDATIONS_HOME_ARRANGEMENT_ADULT_ANIMAL, actual.getParameters().get("text"));
    }
    @Test
    void handleImprovementForLittleAnimalCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Setup for puppies"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.RECOMMENDATIONS_HOME_ARRANGEMENT_KITTEN_PUPPY, actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Setup for kitten"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.RECOMMENDATIONS_HOME_ARRANGEMENT_KITTEN_PUPPY, actual.getParameters().get("text"));
    }
    @Test
    void handleRecommendationsForAnimalCommands() {
        when(userService.getById(any())).thenReturn(catUser);
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "About dogs"), Update.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("About dogs", actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "About cats"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("About cats", actual.getParameters().get("text"));
    }
    @Test
    void handleReasonsForDenyCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Why we can deny you"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.LIST_OF_REASON_FOR_DENY, actual.getParameters().get("text"));
    }
    @Test
    void handleDocumentsListCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Required documents"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.LIST_OF_DOCUMENTS, actual.getParameters().get("text"));
    }
    @Test
    void handleAnimalTransportationCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Cat transportation"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.TRANSPORTATION_OF_THE_ANIMAL, actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Dog transportation"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.TRANSPORTATION_OF_THE_ANIMAL, actual.getParameters().get("text"));
    }
    @Test
    void handleAnimalDatingCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Rules for dating a cat"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.ANIMAL_DATING_RULES, actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Rules for dating a dog"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Constants.ANIMAL_DATING_RULES, actual.getParameters().get("text"));
    }
    @Test
    void handleBackToFAQCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Back to \\\"about cats\\\""), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Все о кошках", actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Back to \\\"about dogs\\\""), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Все о собаках", actual.getParameters().get("text"));
    }
    @Test
    void handleFAQCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "FAQ"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Все о кошках", actual.getParameters().get("text"));
        when(userService.getById(any())).thenReturn(dogUser);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Все о собаках", actual.getParameters().get("text"));
    }
    @Test
    void handleSecurityContactCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Security contact details"), Update.class);
        SendMessage actual = testerForCatShelter(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("security", actual.getParameters().get("text"));
        actual = testerForDogShelter(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("security", actual.getParameters().get("text"));
    }
    @Test
    void handleContactCommand() {
        when(userService.getById(any())).thenReturn(catUser);
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Send your contact details"), Update.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Enter the phone number in the format: 89997776655", actual.getParameters().get("text"));
    }
    @Test
    void handleSafetyAdviceCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Safety policy"), Update.class);
        SendMessage actual = testerForCatShelter(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("about me", actual.getParameters().get("text"));
        actual = testerForDogShelter(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("about me", actual.getParameters().get("text"));
    }
    @Test
    void handleAnimalListCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Our cats"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        catShelter.setList(List.of(cat));
        when(catShelterService.getShelterByName(any())).thenReturn(catShelter);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Name: CatName, Age: 2, Heath condition: healthy, Vaccinated: vaccinated\n============\n",
                actual.getParameters().get("text"));

        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Our dogs"), Update.class);
        when(userService.getById(any())).thenReturn(dogUser);
        dogShelter.setList(List.of(dog));
        when(dogShelterService.getShelterByName(any())).thenReturn(dogShelter);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Name: DogName, Age: 2, Heath condition: healthy, Vaccinated: vaccinated\n============\n",
                actual.getParameters().get("text"));
    }
    @Test
    void handleTimetableCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Timetable"), Update.class);
        SendMessage actual = testerForCatShelter(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("safety advice", actual.getParameters().get("text"));
        actual = testerForDogShelter(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("safety advice", actual.getParameters().get("text"));
    }
    @Test
    void handleMainMenuCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Main menu"), Update.class);
        User user = new User(1L, "CatUnderGlue", "", "", "CAT", "SomeShelterName");
        User userWithoutShelter = new User(1L, "CatUnderGlue", "", "", null, null);
        when(userService.getById(1L)).thenReturn(user);
        when(userService.update(userWithoutShelter)).thenReturn(userWithoutShelter);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(
                "сюда нужно много текста", actual.getParameters().get("text"));
    }
}


