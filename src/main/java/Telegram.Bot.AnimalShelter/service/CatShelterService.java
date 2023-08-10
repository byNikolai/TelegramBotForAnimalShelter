package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.Cat;
import Telegram.Bot.AnimalShelter.entity.CatShelter;

import java.util.List;

public interface CatShelterService {

    CatShelter create(CatShelter catShelter);

    CatShelter update(CatShelter catShelter);

    CatShelter getShelterById(Long id);

    CatShelter getShelterByName(String name);

    List<CatShelter> getShelter();

    List<Cat> getAnimal(Long id);

    String delete(Long id);


}
