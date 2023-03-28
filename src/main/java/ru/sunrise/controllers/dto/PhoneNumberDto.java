package ru.sunrise.controllers.dto;

import lombok.*;
import ru.sunrise.persist.model.Person;
import ru.sunrise.persist.model.PhoneType;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumberDto {

    private String number;

}
