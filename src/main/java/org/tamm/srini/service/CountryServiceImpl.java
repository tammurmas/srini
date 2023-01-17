package org.tamm.srini.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tamm.srini.repository.CountryRepository;
import org.tamm.srini.service.dto.CountryDTO;

@Service
@Transactional
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public List<CountryDTO> findAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(CountryDTO::ofCountry)
                .toList();
    }

}
