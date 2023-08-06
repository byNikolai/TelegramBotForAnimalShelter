package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.Dog;
import Telegram.Bot.AnimalShelter.entity.DogShelter;

import java.util.List;

public interface DogShelterService<D, D1> {

    DogShelter create(DogShelter dogShelter);

    DogShelter update(DogShelter dogShelter);

    DogShelter getShelterById(Long id);

    DogShelter getShelterByName(String name);

    List<DogShelter> getShelter();

    List<Dog> getAnimal(Long id);

    String delete(Long id);
}
