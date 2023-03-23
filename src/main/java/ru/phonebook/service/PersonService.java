package ru.phonebook.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.phonebook.controllers.NotFoundException;
import ru.phonebook.controllers.dto.PersonDto;
import ru.phonebook.controllers.dto.PersonWithAddressDto;
import ru.phonebook.controllers.dto.PhoneDto;
import ru.phonebook.persist.PersonRepository;
import ru.phonebook.persist.StreetRepository;
import ru.phonebook.persist.model.*;

import java.util.List;
import java.util.Map;
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
        return personRepository.findAllWithFetch(Sort.by("id")).stream()
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
        return personRepository.findByIdWithFetch(id)
                .map(person -> new PersonWithAddressDto(person.getId(), person.getFirstName(),
                        person.getSurname(), person.getPatronymic(),
                        Optional.ofNullable(person.getAddress()).map(Address::getCity).orElse(null),
                        Optional.ofNullable(person.getAddress()).map(Address::getStreet).map(Street::getId).orElse(null),
                        Optional.ofNullable(person.getAddress()).map(Address::getBuildingNumber).orElse(null),
                        person.getPhones().stream().map(PersonService::mapPhoneEntity2dto).collect(Collectors.toList())));
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
        if (person.getPhones().isEmpty()) {
            person.getPhones().addAll(dto.getPhones().stream()
                    .map(phDto -> mapPhoneDto2entity(phDto, person))
                    .toList());
        } else {
            Map<Long, PhoneDto> phoneDtoMap = dto.getPhones().stream()
                    .filter(phDto -> phDto.getId() != null)
                    .collect(Collectors.toMap(PhoneDto::getId, ph -> ph));

            // update existing phones
            person.getPhones().replaceAll(phone -> {
                PhoneDto phoneDto = phoneDtoMap.get(phone.getId());
                if (phoneDto != null) {
                    phone.setNumber(phoneDto.getNumber());
                    phone.setPhoneType(PhoneType.valueOf(phoneDto.getPhoneType()));
                }
                return phone;
            });
            // remove deleted phones
            person.getPhones().removeIf(phone -> phoneDtoMap.get(phone.getId()) == null);
            // add new phones
            dto.getPhones().stream()
                    .filter(phDto -> phDto.getId() == null)
                    .forEach(phDto -> person.addPhone(mapPhoneDto2entity(phDto, person)));
        }
        personRepository.save(person);
    }

    private static PhoneDto mapPhoneEntity2dto(Phone phone) {
        return new PhoneDto(phone.getId(), phone.getNumber(), phone.getPhoneType().toString());
    }

    private static Phone mapPhoneDto2entity(PhoneDto dto, Person person) {
        return new Phone(dto.getId(), dto.getNumber(), PhoneType.valueOf(dto.getPhoneType()), person);
    }
}
