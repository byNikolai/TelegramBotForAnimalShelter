package Telegram.Bot.AnimalShelter.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Класс User - сущность пользователя
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    /**
     * Поле: идентификационный номер пользователя
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Поле: номер чата пользователя
     */
    @Column(name = "chat_id")
    private Long chatId;

    /**
     * Поле: имя пользователя
     */
    @Column(name = "name")
    private String name;

    /**
     * Поле: выбор приюта
     */
    @Column(name = "is_dog", nullable = false)
    private boolean isDog = true;
    //Если пользователь выбирает собачий приют, возвращается true, иначе false

    public void setDog(boolean dog) {
        isDog = dog;
    }

    public boolean isDog() {
        return isDog;
    }

    public User(Long chatId, String name, boolean isDog) {
        this.chatId = chatId;
        this.name = name;
        this.isDog = isDog;
    }
}

