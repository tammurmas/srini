package org.tamm.srini.service;

import java.util.List;

import org.tamm.srini.service.dto.CountryDTO;

public interface CountryService {

    List<CountryDTO> findAllCountries();
}
