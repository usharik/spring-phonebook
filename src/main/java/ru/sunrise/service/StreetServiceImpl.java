package ru.sunrise.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sunrise.controllers.NotFoundException;
import ru.sunrise.persist.StreetRepository;
import ru.sunrise.persist.model.Address;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class StreetServiceImpl implements StreetService {

    private final StreetRepository streetRepository;

    @Autowired
    public StreetServiceImpl(StreetRepository streetRepository) {
        this.streetRepository = streetRepository;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        List<Address> addresses = streetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No street with id " + id))
                .getAddresses();
        for (Address a:addresses) {
            a.setStreet(null);
        }
        streetRepository.deleteById(id);
    }
}
