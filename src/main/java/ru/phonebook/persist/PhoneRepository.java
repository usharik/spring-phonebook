package ru.phonebook.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.phonebook.persist.model.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
}
