package ru.phonebook.persist.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotEmpty
    private String firstName;

    @Column(name = "surname", nullable = false)
    @NotEmpty
    private String surname;

    @Column(name = "patronymic", nullable = false)
    @NotEmpty
    private String patronymic;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Phone> phones = new ArrayList<>();

    public void addPhone(Phone phone) {
        phone.setOwner(this);
        this.getPhones().add(phone);
    }

    public void addAddress(Address address) {
        address.setOwner(this);
        this.setAddress(address);
    }
}
