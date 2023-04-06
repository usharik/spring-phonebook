package ru.sunrise.controllers.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {

    private Date date = new Date();

    private String message;

    public ErrorDto(String message) {
        this.message = message;
    }
}
