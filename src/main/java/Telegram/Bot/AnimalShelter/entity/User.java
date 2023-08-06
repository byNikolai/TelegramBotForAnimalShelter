package Telegram.Bot.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column
    private Long telegramId;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column(name = "phone_number")
    private String phone;
    @Column(name = "shelter_type")
    private String shelterType;
    @Column(name = "shelter_name")
    private String shelterName;

    public User(Long telegramId, String firstName, String lastName, String phone) {
        this.telegramId = telegramId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}

