package Telegram.Bot.AnimalShelter.model;

import lombok.*;

import javax.persistence.*;

/**
 * Класс пользователь для хранения контактных данных пользователя
 */
@Entity
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idChat;
    private String login;
    private String contactDetails;

    public User() {
    }

    public User(Long id, Long idChat, String login, String contactDetails) {
        this.id = id;
        this.idChat = idChat;
        this.login = login;
        this.contactDetails = contactDetails;
    }
}
