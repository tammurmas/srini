package org.tamm.srini.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tamm.srini.model.AuthUser;
import org.tamm.srini.model.Client;
import org.tamm.srini.model.Country;
import org.tamm.srini.repository.ClientRepository;
import org.tamm.srini.repository.CountryRepository;
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
    public List<ClientDTO> findAllUserClients() {
        return clientRepository.findAll()
                .stream()
                .map(ClientDTO::ofClient)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientDTO> findClientById(long id) {
        Optional<Client> optionalClient = clientRepository.findOneById(id);
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
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setUserName(clientDTO.getUserName().toLowerCase(Locale.ROOT));
        client.setEmail(clientDTO.getEmail());
        client.setAddress(clientDTO.getAddress());
        client.setCountry(optionalCountry.get());
        // TODO: add logged in user ad creator and owner
        client.setCreatedBy("1");
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
        client.setAuthUser(authUser);

        client = clientRepository.save(client);

        log.info("Created new client: {}", client.getId());
        return client;
    }

    @Override
    public Client updateClient(ClientDTO clientDTO) {
        Optional<Client> optionaClient = clientRepository.findOneById(clientDTO.getId());
        if (optionaClient.isEmpty()) {
            throw new IllegalArgumentException("Client not found=" + clientDTO.getId());
        }
        Optional<Country> optionalCountry = countryRepository.findById(clientDTO.getCountryId());
        if (optionalCountry.isEmpty()) {
            throw new IllegalArgumentException("Country not found for provided id=" + clientDTO.getCountryId());
        }
        Client client = optionaClient.get();
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setUserName(clientDTO.getUserName().toLowerCase(Locale.ROOT));
        client.setEmail(clientDTO.getEmail());
        client.setAddress(clientDTO.getAddress());
        client.setCountry(optionalCountry.get());
        // TODO: add logged in user as updater
        client.setUpdatedBy("1");

        client = clientRepository.save(client);

        log.info("Updated client: {}", client.getId());
        return client;
    }

    @Override
    public Optional<Client> findClientByEmail(String email) {
        return clientRepository.findOneByEmailIgnoreCase(email);
    }

    @Override
    public Optional<Client> findClientByUserName(String userName) {
        return clientRepository.findOneByUserNameIgnoreCase(userName);
    }

}
