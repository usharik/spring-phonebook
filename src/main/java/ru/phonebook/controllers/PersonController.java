package ru.phonebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.phonebook.controllers.dto.PersonWithAddressDto;
import ru.phonebook.controllers.dto.PhoneDto;
import ru.phonebook.persist.StreetRepository;
import ru.phonebook.service.PersonService;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    private final StreetRepository streetRepository;

    @ModelAttribute
    public void commonAttributes(Model model) {
        model.addAttribute("streets", streetRepository.findAll());
    }

    @Autowired
    public PersonController(PersonService personService, StreetRepository streetRepository) {
        this.personService = personService;
        this.streetRepository = streetRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("persons", personService.findAll());
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
        return "person_form";
    }

    @PostMapping(params = {"addPhoneInput"})
    public String addPhoneInput(@ModelAttribute("person") PersonWithAddressDto person) {
        person.getPhones().add(new PhoneDto());
        return "person_form";
    }

    @PostMapping(params = {"removePhoneInputIndex"})
    public String removePhoneInput(@RequestParam int removePhoneInputIndex,
                                   @ModelAttribute("person") PersonWithAddressDto person) {
        person.getPhones().remove(removePhoneInputIndex);
        return "person_form";
    }

    @PostMapping
    public String savePerson(PersonWithAddressDto dto) {
        personService.savePersonWithAddress(dto);
        return "redirect:/person";
    }
}
