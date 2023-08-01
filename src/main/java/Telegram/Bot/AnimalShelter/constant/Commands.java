package Telegram.Bot.AnimalShelter.constant;

/**
 * реализация кнопок меню
 */

public enum Commands {
    TAKE_A_KITTEN("/take_a_kitten", "Start menu", generateCallbackData()),
    TAKE_THE_DOG("/take_the_dog", "Start menu", generateCallbackData()),
    START("/start", "Старт", generateCallbackData()),
    ANIMAL_INFO("/animal_info", "как взять питомца", generateCallbackData()),
    SUBMIT_REPORT("/submit_report", "прислать отчет", generateCallbackData()),
    VOLUNTEER("/volunteer", "позвать волонтера", generateCallbackData()),
    INFO("/info", "информация о приюте", generateCallbackData());

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
     * Конструктор создания команды
     * title        заголовок команды
     * description  описание команды
     * callbackData данные обратного вызова нажатия кнопки
     */
    Commands(String title, String description, String callbackData) {
        this.title = title;
        this.description = description;
        this.callbackData = callbackData;
    }


// Генерация строки callbackData

// return строка CallbackData



    /**
     * Получение данных (Call bac) нажатия кнопки
     *
     * @return данные (Call bac) нажатия кнопки
     */
    public String getCallbackData() {
        return callbackData;
    }



    /**
     * Получение описание команды
     *
     * @return описание команды
     */
    public String getDescription() {
        return description;
    }

    public static String generateCallbackData() {
        StringBuilder sb = new StringBuilder(" ");
        sb.append(" ".repeat(Math.max(0, count + 1)));
        count += 1;
        return sb.toString();
    }
}
