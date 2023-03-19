package ru.phonebook.persist.model;

public enum PhoneType {

    HOME_PHONE("Домашний телефон"),
    WORK_PHONE("Рабочий телефон"),
    MOBILE_PHONE("Мобильный телефон");

    final String readableName;

    PhoneType(String readableName) {
        this.readableName = readableName;
    }

    public String getReadableName() {
        return readableName;
    }
}
