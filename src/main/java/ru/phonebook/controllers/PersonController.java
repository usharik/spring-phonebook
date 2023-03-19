package ru.phonebook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.phonebook.controllers.dto.PersonWithAddressDto;
import ru.phonebook.persist.StreetRepository;
import ru.phonebook.service.PersonService;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    private final StreetRepository streetRepository;

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
        model.addAttribute("streets", streetRepository.findAll());
        return "person_form";
    }

    @GetMapping("/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("person", personService.findById(id)
                .orElseThrow(() -> new NotFoundException("No person with id " + id)));
        model.addAttribute("streets", streetRepository.findAll());
        return "person_form";
    }

    @PostMapping
    public String savePerson(PersonWithAddressDto dto) {
        personService.savePersonWithAddress(dto);
        return "redirect:/person";
    }
}
