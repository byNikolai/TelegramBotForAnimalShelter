package Telegram.Bot.AnimalShelter.constant;

/**
 * реализация кнопок меню
 */

public enum Commands {
    TAKE_A_KITTEN("/take_a_kitten", "Start menu", generateCallbackData()),
    TAKE_THE_DOG("/take_the_dog", "Start menu", generateCallbackData()),
    START("/start", "Старт", generateCallbackData()),
    GET_ANIMAL_INFO("/get_animal_info", "как взять питомца", generateCallbackData()),
    SUBMIT_REPORT("/submit_report", "прислать отчет", generateCallbackData()),
    VOLUNTEER("/volunteer", "позвать волонтера", generateCallbackData()),
    INFO("/info", "информация о приюте", generateCallbackData()),
    BACK("/back", "Назад", generateCallbackData());

    /**
     * "Заголовок"
     */
    private final String title;

    /**
     * "Описание"
     */
    private final String description;

    /**
     * "Данные обратного вызова нажатия кнопки"
     */
    private final String callbackData;

    /**
     * "Счетчик вызовов метода generateCallbackData"
     */
    private static int count;

    /**
     * Конструктор создания Command
     * title        заголовок Command
     * description  описание Command
     * callbackData данные обратного вызова нажатия кнопки
     */
    Commands(String title, String description, String callbackData) {
        this.title = title;
        this.description = description;
        this.callbackData = callbackData;
    }

    /**
     * Получение данных обратного вызова нажатия кнопки
     * @return данные обратного вызова нажатия кнопки
     */
    public String getCallbackData() {
        return callbackData;
    }

    /**
     * Получение заголовка команды
     * @return заголовок команды
     */
    public String getTitle() {
        return title;
    }

    /**
     * Получение описание команды
     * @return описание команды
     */
    public String getDescription() {
        return description;
    }

    /**
     * Генерация строки callbackData
     * @return строка CallbackData
     */

    public static String generateCallbackData() {
        StringBuilder sb = new StringBuilder(" ");
        sb.append(" ".repeat(Math.max(0, count + 1)));
        count += 1;
        return sb.toString();
    }
}
