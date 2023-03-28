package ru.sunrise.controllers.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    private Long id;

    private String firstName;

    private String surname;

    private String patronymic;

    private String address;

    private String phones;
}
