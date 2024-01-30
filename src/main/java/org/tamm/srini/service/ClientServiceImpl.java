package org.tamm.srini.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tamm.srini.model.AuthUser;
import org.tamm.srini.model.Client;
import org.tamm.srini.model.Country;
import org.tamm.srini.repository.ClientRepository;
import org.tamm.srini.repository.CountryRepository;
import org.tamm.srini.service.dto.AuthUserDetails;
import org.tamm.srini.service.dto.ClientDTO;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final CountryRepository countryRepository;

    @Override
    public List<ClientDTO> findAllByUser() {
        return clientRepository.findAllByAuthUser(getAuthUser())
                .stream()
                .map(ClientDTO::ofClientEntity)
                .toList();
    }

    @Override
    public Optional<ClientDTO> findClientById(long id) {
        return clientRepository.findOneByIdAndAuthUser(id, getAuthUser()).flatMap(ClientDTO::getOptionalClientDTO);
    }

    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        Optional<Country> optionalCountry = countryRepository.findById(clientDTO.getCountryId());
        if (optionalCountry.isEmpty()) {
            throw new IllegalArgumentException("Country not found for provided id=" + clientDTO.getCountryId());
        }
        Client client = Client.create(clientDTO, optionalCountry.get(), getAuthUser());
        Client updatedClient = clientRepository.save(client);

        log.info("Created new client: {}", client.getId());
        return ClientDTO.ofClientEntity(updatedClient);
    }

    @Override
    public ClientDTO updateClient(ClientDTO clientDTO) {
        Optional<Client> optionaClient = clientRepository.findOneByIdAndAuthUser(clientDTO.getId(), getAuthUser());
        if (optionaClient.isEmpty()) {
            throw new IllegalArgumentException("Client not found=" + clientDTO.getId());
        }
        Optional<Country> optionalCountry = countryRepository.findById(clientDTO.getCountryId());
        if (optionalCountry.isEmpty()) {
            throw new IllegalArgumentException("Country not found for provided id=" + clientDTO.getCountryId());
        }

        Client client = optionaClient.get();
        client.update(clientDTO, optionalCountry.get(), getAuthUser());
        Client persistedClient = clientRepository.save(client);

        log.info("Updated client: {}", client.getId());
        return ClientDTO.ofClientEntity(persistedClient);
    }

    @Override
    public Optional<ClientDTO> findClientByEmail(String email) {
        return clientRepository.findOneByEmailIgnoreCase(email).flatMap(ClientDTO::getOptionalClientDTO);
    }

    @Override
    public Optional<ClientDTO> findClientByUsername(String username) {
        return clientRepository.findOneByUsernameIgnoreCase(username).flatMap(ClientDTO::getOptionalClientDTO);
    }

    private static AuthUser getAuthUser() {
        AuthUserDetails authUserDetails = (AuthUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authUserDetails.getAuthUser();
    }

}
