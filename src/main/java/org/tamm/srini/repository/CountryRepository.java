package org.tamm.srini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tamm.srini.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
