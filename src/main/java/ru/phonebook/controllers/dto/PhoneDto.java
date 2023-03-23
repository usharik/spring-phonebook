package ru.phonebook.controllers.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDto {

    private Long id;

    private String number;

    private String phoneType;
}
