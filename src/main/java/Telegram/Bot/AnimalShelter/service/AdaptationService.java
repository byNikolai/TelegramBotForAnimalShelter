package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.Adaptation;

import java.util.List;

public interface AdaptationService {

    Adaptation create(Adaptation adaptation);

    Adaptation create(Adaptation adaptation, Adaptation.AnimalType animalType);

    Adaptation update(Adaptation adaptation);

    Adaptation getById(Long id);

    List<Adaptation> getAllByOwnerId(Long ownerId);

    List<Adaptation> getAll();

    void delete(Adaptation adaptation);

    void deleteById(Long id);

}
