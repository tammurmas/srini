package org.tamm.srini.service;

import java.util.List;
import java.util.Locale;
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
                .map(ClientDTO::ofClient)
                .toList();
    }

    @Override
    public Optional<ClientDTO> findClientById(long id) {
        Optional<Client> optionalClient = clientRepository.findOneByIdAndAuthUser(id, getAuthUser());
        return getClientDTO(optionalClient);
    }

    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
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
        Client updatedClient = clientRepository.save(client);

        log.info("Created new client: {}", client.getId());
        return ClientDTO.ofClient(updatedClient);
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
        client.setFirstname(clientDTO.getFirstname());
        client.setLastname(clientDTO.getLastname());
        client.setUsername(clientDTO.getUsername().toLowerCase(Locale.ROOT));
        client.setEmail(clientDTO.getEmail());
        client.setAddress(clientDTO.getAddress());
        client.setCountry(optionalCountry.get());
        client.setUpdatedBy(String.valueOf(getAuthUser().getId()));
        Client persistedClient = clientRepository.save(client);

        log.info("Updated client: {}", client.getId());
        return ClientDTO.ofClient(persistedClient);
    }

    @Override
    public Optional<ClientDTO> findClientByEmail(String email) {
        Optional<Client> optionalClient = clientRepository.findOneByEmailIgnoreCase(email);
        return getClientDTO(optionalClient);
    }

    @Override
    public Optional<ClientDTO> findClientByUsername(String username) {
        Optional<Client> optionalClient = clientRepository.findOneByUsernameIgnoreCase(username);
        return getClientDTO(optionalClient);
    }

    private static AuthUser getAuthUser() {
        AuthUserDetails authUserDetails = (AuthUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authUserDetails.getAuthUser();
    }

    private static Optional<ClientDTO> getClientDTO(Optional<Client> optionalClient) {
        Optional<ClientDTO> optionalDto = Optional.empty();
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            ClientDTO dto = ClientDTO.ofClient(client);
            optionalDto = Optional.of(dto);
        }
        return optionalDto;
    }

}
