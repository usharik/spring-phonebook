package ru.phonebook.persist.model;

import jakarta.persistence.*;
import lombok.*;


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
    private int id;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "phone_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PhoneType phoneType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id")
    private Person owner;
}
