package org.tamm.srini.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tamm.srini.repository.CountryRepository;
import org.tamm.srini.service.dto.CountryDTO;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Transactional(readOnly = true)
    public List<CountryDTO> findAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(CountryDTO::ofCountry)
                .collect(Collectors.toList());
    }

}
