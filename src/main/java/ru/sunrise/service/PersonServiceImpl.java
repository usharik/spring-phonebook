package ru.sunrise.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sunrise.controllers.NotFoundException;
import ru.sunrise.controllers.dto.PersonDto;
import ru.sunrise.controllers.dto.PersonWithAddressDto;
import ru.sunrise.controllers.dto.PhoneDto;
import ru.sunrise.persist.PersonRepository;
import ru.sunrise.persist.StreetRepository;
import ru.sunrise.persist.model.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final StreetRepository streetRepository;
    private static ModelMapper modelMapper;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, StreetRepository streetRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.streetRepository = streetRepository;
        PersonServiceImpl.modelMapper = modelMapper;
    }

    public List<PersonDto> findAll(String filterSurname, String filterName, String filterPhone) {
        return personRepository.findByParams(filterSurname, filterName, filterPhone, Sort.by("id")).stream()
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
                        Optional.ofNullable(person.getAddress()).map(Address::getBuildingNumber).orElse(null),
                        person.getPhones().stream().map(PersonServiceImpl::toPhoneDto).collect(Collectors.toList())));
    }

    @Transactional
    public void savePersonWithAddress(PersonWithAddressDto dto) {
        Street street = streetRepository.findById(dto.getStreetId())
                .orElseThrow(() -> new NotFoundException("No street with id " + dto.getStreetId()));

        Person person;
        if (dto.getId() != null) {
            person = personRepository.findById(dto.getId())
                    .orElseThrow(() -> new NotFoundException("No Person with id " + dto.getId()));
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
                    .map((PhoneDto phoneDto) -> toPhone(phoneDto, person)).toList());
        } else {
            Map<Long, PhoneDto> phoneDtoMap = dto.getPhones().stream()
                    .filter(phDto -> phDto.getId() != null)
                    .collect(Collectors.toMap(PhoneDto::getId, ph -> ph));

            // update existing phones
            person.getPhones().forEach(phone -> {
                PhoneDto phoneDto = phoneDtoMap.get(phone.getId());
                if (phoneDto != null) {
                    phone.setNumber(phoneDto.getNumber());
                    phone.setPhoneType(phoneDto.getPhoneType());
                }
            });
            // remove deleted phones
            person.getPhones().removeIf(phone -> phoneDtoMap.get(phone.getId()) == null);
            // add new phones
            dto.getPhones().stream()
                    .filter(phDto -> phDto.getId() == null)
                    .forEach(phDto -> person.addPhone(toPhone(phDto, person)));
        }
        personRepository.save(person);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<PersonDto> findByFirstNameAndSurnameAndPatronymic(PersonDto personDto) {
//        Person person = toPerson(personDto);
//
//        List<PersonDto> people = personRepository.findByFirstNameAndSurnameAndPatronymic(person.getFirstName(),
//                        person.getSurname(), person.getPatronymic())
//                .orElseThrow(() -> new RuntimeException("Нет такого человека"));
        return Collections.emptyList();
    }

    private static Phone toPhone(PhoneDto phoneDto, Person person) {
        Phone phone = modelMapper.map(phoneDto, Phone.class);
        return new Phone(phone.getId(), phone.getNumber(), phone.getPhoneType(), person);
    }

    private static PhoneDto toPhoneDto(Phone phone) {
        return modelMapper.map(phone, PhoneDto.class);
    }

    private static Person toPerson(PersonDto personDto) {
        return modelMapper.map(personDto, Person.class);
    }

    private static PersonDto toPersonDTO(Person person) {
        return modelMapper.map(person, PersonDto.class);
    }
}
