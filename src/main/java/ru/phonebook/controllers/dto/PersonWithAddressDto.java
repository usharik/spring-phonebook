package ru.phonebook.controllers.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PersonWithAddressDto {

    private Long id;

    private String firstName;

    private String surname;

    private String patronymic;

    private String city;

    private Long streetId;

    private String buildingNumber;

    private List<PhoneDto> phones = new ArrayList<>();
}
