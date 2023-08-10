package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.Adaptation;
import Telegram.Bot.AnimalShelter.entity.DogOwner;

import java.util.List;

public interface DogOwnerService {

    DogOwner create(DogOwner dogOwner, Adaptation.AnimalType animalType, Long animalId);

    DogOwner create(Long id, Adaptation.AnimalType animalType, Long animalId);

    DogOwner update(DogOwner dogOwner);

    DogOwner getById(Long id);

    List<DogOwner> getAll();

    void delete(DogOwner dogOwner);

    void deleteById(Long id);
}
