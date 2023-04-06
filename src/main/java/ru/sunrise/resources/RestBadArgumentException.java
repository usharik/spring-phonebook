package ru.sunrise.resources;

public class RestBadArgumentException extends RuntimeException {

    public RestBadArgumentException(String message) {
        super(message);
    }
}
