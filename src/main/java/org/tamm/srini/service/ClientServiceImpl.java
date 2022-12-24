package org.tamm.srini.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAllByUser() {
        return clientRepository.findAllByAuthUser(getAuthUser())
                .stream()
                .map(ClientDTO::ofClient)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findClientById(long id) {
        Optional<Client> optionalClient = clientRepository.findOneByIdAndAuthUser(id, getAuthUser());
        Optional<ClientDTO> dto = Optional.empty();
        if (optionalClient.isPresent()) {
            dto = Optional.of(ClientDTO.ofClient(optionalClient.get()));
        }
        return dto;
    }

    @Override
    public Client createClient(ClientDTO clientDTO) {
        Optional<Country> optionalCountry = countryRepository.findById(clientDTO.getCountryId());
        if (optionalCountry.isEmpty()) {
            throw new IllegalArgumentException("Country not found for provided id=" + clientDTO.getCountryId());
        }
        Client client = new Client();
        client.setFirstname(clientDTO.getFirstname());
        client.setLastname(clientDTO.getLastname());
        client.setUsername(clientDTO.getUsername().toLowerCase(Locale.ROOT));
        client.setEmail(clientDTO.getEmail());
        client.setAddress(clientDTO.getAddress());
        client.setCountry(optionalCountry.get());
        AuthUser authUser = getAuthUser();
        client.setCreatedBy(String.valueOf(authUser.getId()));
        client.setAuthUser(authUser);
        client = clientRepository.save(client);

        log.info("Created new client: {}", client.getId());
        return client;
    }

    @Override
    public Client updateClient(ClientDTO clientDTO) {
        Optional<Client> optionaClient = clientRepository.findOneByIdAndAuthUser(clientDTO.getId(), getAuthUser());
        if (optionaClient.isEmpty()) {
            throw new IllegalArgumentException("Client not found=" + clientDTO.getId());
        }
        Optional<Country> optionalCountry = countryRepository.findById(clientDTO.getCountryId());
        if (optionalCountry.isEmpty()) {
            throw new IllegalArgumentException("Country not found for provided id=" + clientDTO.getCountryId());
        }
        Client client = optionaClient.get();
        client.setFirstname(clientDTO.getFirstname());
        client.setLastname(clientDTO.getLastname());
        client.setUsername(clientDTO.getUsername().toLowerCase(Locale.ROOT));
        client.setEmail(clientDTO.getEmail());
        client.setAddress(clientDTO.getAddress());
        client.setCountry(optionalCountry.get());
        client.setUpdatedBy(String.valueOf(getAuthUser().getId()));
        client = clientRepository.save(client);

        log.info("Updated client: {}", client.getId());
        return client;
    }

    @Override
    public Optional<Client> findClientByEmail(String email) {
        return clientRepository.findOneByEmailIgnoreCase(email);
    }

    @Override
    public Optional<Client> findClientByUsername(String username) {
        return clientRepository.findOneByUsernameIgnoreCase(username);
    }

    private static AuthUser getAuthUser() {
        AuthUserDetails authUserDetails = (AuthUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authUserDetails.getAuthUser();
    }

}
