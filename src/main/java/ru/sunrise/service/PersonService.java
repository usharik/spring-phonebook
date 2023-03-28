package ru.sunrise.service;

import ru.sunrise.controllers.dto.PersonDto;
import ru.sunrise.controllers.dto.PersonWithAddressDto;

import java.util.List;
import java.util.Optional;


public interface PersonService {
    List<PersonDto> findAll(String filterSurname, String filterName, String filterPhone);
    Optional<PersonWithAddressDto> findById(long id);
    void savePersonWithAddress(PersonWithAddressDto dto);
    void deleteById(long id);

    List<PersonDto> findByFirstNameAndSurnameAndPatronymic(PersonDto personDto);
}
