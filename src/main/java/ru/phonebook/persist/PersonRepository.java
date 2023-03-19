package ru.phonebook.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.phonebook.persist.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
