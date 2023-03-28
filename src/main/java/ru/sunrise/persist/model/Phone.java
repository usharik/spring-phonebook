package ru.sunrise.persist.model;

import jakarta.persistence.*;
import lombok.*;
import ru.sunrise.controllers.dto.PhoneDto;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "phones")
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number", nullable = false)
    private String number;

    @ManyToOne
    @JoinColumn(name = "type_id")
    @ToString.Exclude
    private PhoneType phoneType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    private Person owner;

    public Phone(Phone phone, Person person) {
    }
}
