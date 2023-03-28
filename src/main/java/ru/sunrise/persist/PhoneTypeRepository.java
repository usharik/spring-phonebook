package ru.sunrise.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sunrise.persist.model.PhoneType;

public interface PhoneTypeRepository extends JpaRepository<PhoneType, Long> {
}
