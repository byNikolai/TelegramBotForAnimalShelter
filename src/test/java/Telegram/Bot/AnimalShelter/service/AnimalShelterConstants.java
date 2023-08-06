package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.Cat;
import Telegram.Bot.AnimalShelter.entity.CatShelter;
import Telegram.Bot.AnimalShelter.entity.Dog;
import Telegram.Bot.AnimalShelter.entity.DogShelter;

public class AnimalShelterConstants {

    public static final String NAME_CAT_1 = "Буся";
    public static final String NAME_CAT_2 = "Mуся";
    public static final int AGE_CAT_1 = 1;
    public static final int AGE_CAT_2 = 2;
    public static final String ABOUT_ME_CAT = "В нашем приюте вы можете бесплатно взять кошку или котенка.";

    public static final String NAME_DOG_1 = "Амиго";
    public static final String NAME_DOG_2 = "Бинго";
    public static final int AGE_DOG_1 = 1;
    public static final int AGE_DOG_2 = 2;
    public static final String ABOUT_ME_DOG = "В нашем приюте вы можете собаку или щенка.";


    public static final boolean HEALTHY_TRUE = true;
    public static final boolean VACCINATED_TRUE = true;
    public static final Long OWNER_ID_1 = 111L;
    public static final Long SHELTER_ID_1 = 111L;
    public static final Long OWNER_ID_2 = 222L;
    public static final Long SHELTER_ID_2 = 222L;
    public static final String LOCATION = "Астана, Улица Аккорган, 5в";
    public static final String SAFETY = "Если вы решите взять из приюта котенка, он сначала будет намного больше зависеть от вас" +
            ", чем взрослая кошка, и его нельзя будет надолго оставлять одного.";
    public static final String SECURITY = "Запрещается: Кормить животных.";
    public static final String WORKING_HOURS = "8:00-20:00";
    public static CatShelter CAT_SHELTER_1 = new CatShelter(ABOUT_ME_CAT, LOCATION, NAME_CAT_1, SAFETY, SECURITY, WORKING_HOURS);
    public static CatShelter CAT_SHELTER_2 = new CatShelter(ABOUT_ME_CAT, LOCATION, NAME_CAT_2, SAFETY, SECURITY, WORKING_HOURS);
    public static Cat CAT_1 = new Cat(111L, ABOUT_ME_CAT, AGE_CAT_1, HEALTHY_TRUE, VACCINATED_TRUE, OWNER_ID_1, SHELTER_ID_1);
    public static Cat CAT_2 = new Cat(111L, ABOUT_ME_CAT, AGE_CAT_1, HEALTHY_TRUE, VACCINATED_TRUE, OWNER_ID_2, SHELTER_ID_2);


    public static DogShelter DOG_SHELTER_1 = new DogShelter(ABOUT_ME_DOG, LOCATION, NAME_DOG_1, SAFETY, SECURITY, WORKING_HOURS);
    public static DogShelter DOG_SHELTER_2 = new DogShelter(ABOUT_ME_DOG, LOCATION, NAME_DOG_2, SAFETY, SECURITY, WORKING_HOURS);
    public static Dog DOG_1 = new Dog(111L, ABOUT_ME_DOG, AGE_DOG_1, HEALTHY_TRUE, VACCINATED_TRUE, OWNER_ID_1, SHELTER_ID_1);
    public static Dog DOG_2 = new Dog(111L, ABOUT_ME_DOG, AGE_DOG_1, HEALTHY_TRUE, VACCINATED_TRUE, OWNER_ID_2, SHELTER_ID_2);

}
