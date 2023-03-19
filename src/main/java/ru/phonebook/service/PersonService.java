package ru.phonebook.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.phonebook.controllers.NotFoundException;
import ru.phonebook.controllers.dto.PersonDto;
import ru.phonebook.controllers.dto.PersonWithAddressDto;
import ru.phonebook.persist.PersonRepository;
import ru.phonebook.persist.StreetRepository;
import ru.phonebook.persist.model.Address;
import ru.phonebook.persist.model.Person;
import ru.phonebook.persist.model.Phone;
import ru.phonebook.persist.model.Street;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonService {

    private final PersonRepository personRepository;

    private final StreetRepository streetRepository;


    @Autowired
    public PersonService(PersonRepository personRepository, StreetRepository streetRepository) {
        this.personRepository = personRepository;
        this.streetRepository = streetRepository;
    }

    public List<PersonDto> findAll() {
        return personRepository.findAll(Sort.by("id")).stream()
                .map(person ->
                        new PersonDto(person.getId(),
                                person.getFirstName(),
                                person.getSurname(),
                                person.getPatronymic(),
                                Optional.ofNullable(person.getAddress())
                                        .map(address -> String.format("%s, %s, %s", address.getCity(),
                                                address.getStreet().getStreetName(), address.getBuildingNumber()))
                                        .orElse(""),
                                person.getPhones().stream()
                                        .map(Phone::getNumber)
                                        .collect(Collectors.joining(", ")))
                ).collect(Collectors.toList());
    }

    public Optional<PersonWithAddressDto> findById(long id) {
        return personRepository.findById(id)
                .map(person -> new PersonWithAddressDto(person.getId(), person.getFirstName(),
                        person.getSurname(), person.getPatronymic(),
                        Optional.ofNullable(person.getAddress()).map(Address::getCity).orElse(null),
                        Optional.ofNullable(person.getAddress()).map(Address::getStreet).map(Street::getId).orElse(null),
                        Optional.ofNullable(person.getAddress()).map(Address::getBuildingNumber).orElse(null)));
    }

    public void savePersonWithAddress(PersonWithAddressDto dto) {
        Street street = streetRepository.findById(dto.getStreetId())
                .orElseThrow(() -> new NotFoundException("No street with id " + dto.getStreetId()));

        Person person;
        if (dto.getId() != null) {
            person = personRepository.findById(dto.getId())
                    .orElseThrow(() -> new NotFoundException("NoPerson with id " + dto.getId()));
        } else {
            person = new Person();
        }

        person.setFirstName(dto.getFirstName());
        person.setSurname(dto.getSurname());
        person.setPatronymic(dto.getPatronymic());
        if (person.getAddress() == null) {
            person.addAddress(new Address(null, dto.getCity(), dto.getBuildingNumber(), street));
        } else {
            person.getAddress().setCity(dto.getCity());
            person.getAddress().setStreet(street);
            person.getAddress().setBuildingNumber(dto.getBuildingNumber());
        }
        personRepository.save(person);
    }
}
