package ru.sunrise.persist.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "building_number")
    private String buildingNumber;

    @OneToOne
    @JoinColumn(name = "person_id")
    @ToString.Exclude
    private Person owner;

    @ManyToOne
    @JoinColumn(name = "street_id")
    @ToString.Exclude
    private Street street;

    public Address(Long id, String city, String buildingNumber, Street street) {
        this.id = id;
        this.city = city;
        this.buildingNumber = buildingNumber;
        this.street = street;
    }
}
