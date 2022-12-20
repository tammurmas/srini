package org.tamm.srini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tamm.srini.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
