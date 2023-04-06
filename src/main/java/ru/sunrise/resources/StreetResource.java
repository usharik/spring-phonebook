package ru.sunrise.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.sunrise.persist.StreetRepository;
import ru.sunrise.persist.model.Street;

import java.util.List;

@RequestMapping("/api/v1/street")
@RestController
public class StreetResource {

    private final StreetRepository streetRepository;

    @Autowired
    public StreetResource(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    @GetMapping("/all")
    public List<Street> findAll() {
        return streetRepository.findAll();
    }

    @GetMapping("/{id}")
    public Street findById(@PathVariable long id) {
        return streetRepository.findById(id)
                .orElseThrow(() -> new RestNotFoundException("Street with id " + id + " not found"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Street create(@RequestBody Street street) {
        if (street.getId() != null) {
            throw new RestBadArgumentException("New Street should not have id");
        }
        return streetRepository.save(street);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Street update(@RequestBody Street street) {
        if (street.getId() == null) {
            throw new RestBadArgumentException("Updated Street should have id");
        }
        return streetRepository.save(street);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        streetRepository.deleteById(id);
    }
}
