package Telegram.Bot.AnimalShelter.service;

import Telegram.Bot.AnimalShelter.entity.User;

import java.util.List;

public interface UserService {


    User create(User user);

    User update(User user);

    void delete(User user);

    void deleteById(Long id);

    User getById(Long id);

    List<User> getAll();

    String getShelterById(Long id);
}
