package Telegram.Bot.AnimalShelter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "dog")
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer age;

    @Column
    private boolean healthy;

    @Column
    private boolean vaccinated;

    @Column
    private Long ownerId;

    @Column
    private Long shelterId;

    public Dog(String name, Integer age, boolean healthy, boolean vaccinated, Long shelterId) {
        this.name = name;
        this.age = age;
        this.healthy = healthy;
        this.vaccinated = vaccinated;
        this.shelterId = shelterId;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                ", Age: " + age +
                ", Heath condition: " + (healthy ? "healthy" : "not healthy") +
                ", Vaccinated: " + (vaccinated ? "vaccinated" : "not vaccinated");
    }
}
