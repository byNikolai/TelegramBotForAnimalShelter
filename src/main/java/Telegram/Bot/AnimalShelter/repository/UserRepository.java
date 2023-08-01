package Telegram.Bot.AnimalShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Telegram.Bot.AnimalShelter.entity.User;
import org.springframework.stereotype.Repository;

/**
 * UserRepository по работе с БД
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByChatId(Long chatId);
}
