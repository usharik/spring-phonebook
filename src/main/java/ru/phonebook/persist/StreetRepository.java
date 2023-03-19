package ru.phonebook.persist;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.phonebook.persist.model.Street;

import java.util.List;

public interface StreetRepository extends JpaRepository<Street, Long> {
}
