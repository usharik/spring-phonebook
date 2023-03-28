package ru.sunrise.controllers.dto;

import lombok.*;
import ru.sunrise.persist.model.PhoneType;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDto {

    private Long id;

    private String number;

    private PhoneType phoneType;
}
