package ru.phonebook.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.phonebook.persist.StreetRepository;
import ru.phonebook.persist.model.Street;

@Slf4j
@RequestMapping("/street")
@Controller
public class StreetController {

    private final StreetRepository streetRepository;

    @Autowired
    public StreetController(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("streets", streetRepository.findAll(Sort.by("streetName")));
        return "streets";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("street", new Street());
        return "street_form";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable long id, Model model) {
        model.addAttribute("street", streetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No street with id " + id)));
        return "street_form";
    }

    @PostMapping
    public String save(Street street) {
        streetRepository.save(street);
        return "redirect:/street";
    }
}
