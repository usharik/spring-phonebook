package ru.sunrise.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sunrise.persist.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
