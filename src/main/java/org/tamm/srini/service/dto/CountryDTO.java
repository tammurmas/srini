package org.tamm.srini.service.dto;

import lombok.Getter;
import lombok.Setter;

import org.tamm.srini.model.Country;

@Getter
@Setter
public class CountryDTO {

    private Long id;
    private String name;

    public static CountryDTO ofCountry(Country country) {
        CountryDTO dto = new CountryDTO();
        dto.setId(country.getId());
        dto.setName(country.getName());

        return dto;
    }
}
