package ru.phonebook.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.phonebook.persist.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
