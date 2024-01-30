package org.tamm.srini.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

import org.tamm.srini.model.Client;

import java.util.Optional;

@Getter
@Setter
public class ClientDTO {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String firstname;

    @NotBlank
    @Size(max = 100)
    private String lastname;

    @NotBlank
    @Size(max = 100)
    private String username;

    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 300)
    private String address;

    @NotNull
    private Long countryId;

    public static ClientDTO ofClientEntity(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setFirstname(client.getFirstname());
        dto.setLastname(client.getLastname());
        dto.setUsername(client.getUsername());
        dto.setEmail(client.getEmail());
        dto.setAddress(client.getAddress());
        dto.setCountryId(client.getCountry().getId());

        return dto;
    }

    public static Optional<ClientDTO> getOptionalClientDTO(Client client) {
        ClientDTO clientDTO = ofClientEntity(client);
        return Optional.of(clientDTO);
    }
}
