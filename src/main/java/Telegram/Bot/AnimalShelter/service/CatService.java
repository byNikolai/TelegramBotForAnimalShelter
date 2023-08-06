package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.Cat;

import java.util.List;

public interface CatService {

    Cat create(Cat cat);

    Cat update(Cat cat);

    Cat getById(Long id);

    List<Cat> getAllByOwnerId(Long id);

    List<Cat> getAll();

    void delete(Long id);
}
