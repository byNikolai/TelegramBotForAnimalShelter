package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.Dog;
import java.util.List;

public interface DogService {


    Dog create(Dog dog);

    Dog update(Dog dog);

    Dog getById(Long id);

    List<Dog> getAllByOwnerId(Long id);

    List<Dog> getAll();

    void delete(Long id);
}
