package Telegram.Bot.AnimalShelter.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Класс Dog - сущность собаки
 */
@Entity
@Table(name = "dog")
@NoArgsConstructor
public class Dog {

    /**
     * Поле: идентификационный номер собаки
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Поле: имя собаки
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Поле: возраст собаки
     */
    @Column(name = "age", nullable = false)
    private Integer age;

    /**
     * Поле: порода собаки
     */
    @Column(name = "breed", nullable = false)
    private String breed;
}
