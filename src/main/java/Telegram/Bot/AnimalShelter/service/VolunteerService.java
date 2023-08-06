package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.Volunteer;

import java.util.List;

public interface VolunteerService {

    Volunteer create(Volunteer volunteer);

    Volunteer update(Volunteer volunteer);

    Volunteer getById(Long id);

    List<Volunteer> getAll();

    void delete(Volunteer volunteer);

    void deleteById(Long id);
}
