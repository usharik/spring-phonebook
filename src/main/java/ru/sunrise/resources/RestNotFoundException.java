package ru.sunrise.resources;

public class RestNotFoundException extends RuntimeException {

    public RestNotFoundException(String message) {
        super(message);
    }
}
