package Telegram.Bot.AnimalShelter.repository;

import Telegram.Bot.AnimalShelter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
