package ru.phonebook.persist;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.phonebook.persist.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("select distinct p " +
            "from Person p " +
            "join fetch p.address " +
            "left join fetch p.phones")
    List<Person> findAllWithFetch(Sort sort);

    @Query("select distinct p " +
            "from Person p " +
            "join fetch p.address " +
            "left join fetch p.phones " +
            "where p.id = :id")
    Optional<Person> findByIdWithFetch(Long id);
}
