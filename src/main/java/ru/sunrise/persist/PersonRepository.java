package ru.sunrise.persist;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sunrise.persist.model.Person;

import java.util.List;


public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("select p " +
            "from Person p " +
            " left join p.phones ph " +
            "where (p.surname like ?1 or ?1 is null) " +
            "  and (p.firstName like ?2 or ?2 is null) " +
            "  and (ph.number like ?3 or ?3 is null)")
    List<Person> findByParams(String filterSurname, String filterName, String filterPhone, Sort sort);
}
