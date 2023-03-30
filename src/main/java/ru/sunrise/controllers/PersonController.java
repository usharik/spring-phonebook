package ru.sunrise.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.sunrise.controllers.dto.PersonWithAddressDto;
import ru.sunrise.controllers.dto.PhoneDto;
import ru.sunrise.persist.PhoneTypeRepository;
import ru.sunrise.persist.StreetRepository;
import ru.sunrise.service.PersonService;

import java.util.Optional;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;
    private final StreetRepository streetRepository;
    private final PhoneTypeRepository phoneTypeRepository;

    @ModelAttribute
    public void commonAttributes(Model model) {
        model.addAttribute("streets", streetRepository.findAll());
        model.addAttribute("phoneTypes", phoneTypeRepository.findAll());
    }

    @Autowired
    public PersonController(PersonService personService, StreetRepository streetRepository,
                            PhoneTypeRepository phoneTypeRepository) {
        this.personService = personService;
        this.streetRepository = streetRepository;
        this.phoneTypeRepository = phoneTypeRepository;
    }

    @GetMapping
    public String index(@RequestParam(name = "filterSurname", required = false) Optional<String> filterSurname,
                        @RequestParam(name = "filterName", required = false) Optional<String> filterName,
                        @RequestParam(name = "filterPhone", required = false) Optional<String> filterPhone,
                        Model model) {
        model.addAttribute("persons",
                personService.findAll(
                        filterSurname
                                .filter(str -> !str.isBlank())
                                .map(str -> "%" + str + "%")
                                .orElse(null),
                        filterName
                                .filter(str -> !str.isBlank())
                                .map(str -> "%" + str + "%")
                                .orElse(null),
                        filterPhone
                                .filter(str -> !str.isBlank())
                                .map(str -> "%" + str + "%")
                                .orElse(null)));
        return "persons";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("person", new PersonWithAddressDto());
        return "person_form";
    }

    @GetMapping("/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("person", personService.findById(id)
                .orElseThrow(() -> new NotFoundException("No person with id " + id)));
        model.addAttribute("streets", streetRepository.findAll());
        return "person_form";
    }

    @PostMapping(params = {"addPhone"})
    public String addPhone(@ModelAttribute("person") PersonWithAddressDto personDto) {
        personDto.getPhones().add(new PhoneDto());
        return "person_form";
    }

    @PostMapping(params = {"removePhone"})
    public String removePhoneInput(@RequestParam int removePhone,
                                   @ModelAttribute("person") PersonWithAddressDto person) {
        person.getPhones().remove(removePhone);
        return "person_form";
    }

    @PostMapping
    public String savePerson(@Valid @ModelAttribute("person") PersonWithAddressDto dto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "person_form";
        }
        personService.savePersonWithAddress(dto);
        return "redirect:/person";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        personService.deleteById(id);
        return "redirect:/person";
    }
}
