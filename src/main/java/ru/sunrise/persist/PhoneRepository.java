package ru.sunrise.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sunrise.persist.model.Phone;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    Optional<Phone> findPhoneByNumber(String number);
}
