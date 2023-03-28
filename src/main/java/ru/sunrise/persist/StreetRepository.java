package ru.sunrise.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sunrise.persist.model.Street;

public interface StreetRepository extends JpaRepository<Street, Long> {
}
