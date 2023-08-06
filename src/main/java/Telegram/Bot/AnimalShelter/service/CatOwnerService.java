package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.Adaptation;
import Telegram.Bot.AnimalShelter.entity.CatOwner;

import java.util.List;

public interface CatOwnerService {

    CatOwner create(CatOwner catOwner, Adaptation.AnimalType animalType, Long animalId);

    CatOwner create(Long id, Adaptation.AnimalType animalType, Long animalId);

    CatOwner update(CatOwner cat1Owner);

    CatOwner getById(Long id);

    List<CatOwner> getAll();

    void delete(CatOwner catOwner);

    void deleteById(Long id);
}
