package Telegram.Bot.AnimalShelter.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс OwnerDog - сущность владельца собаки
 */
@Entity
@Table(name = "owner_dog")
@NoArgsConstructor
public class OwnerDog {

    /**
     * Поле: идентификационный номер владельца
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Поле: номер чата владельца
     */
    @Column(name = "chat_id")
    private Long chatId;

    /**
     * Поле: имя владельца
     */
    @Column(name = "name")
    private String name;

    /**
     * Поле: номер телефона владельца
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Поле: возраст владельца
     */
    @Column(name = "age")
    private int age;

    /**
     * Поле: имеющаяся собака
     *
     *  Dog
     */
    @OneToOne
    @JoinColumn(name = "dog_id")
    private Dog dog;

    /**
     * Поле: количество дней испытательного срока
     */
    @Column(name = "number_of_report_days" )
    private Long numberOfReportDays;

}
