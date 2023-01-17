package org.tamm.srini.service;

import java.util.List;
import java.util.Optional;

import org.tamm.srini.service.dto.ClientDTO;

public interface ClientService {

    List<ClientDTO> findAllByUser();

    Optional<ClientDTO> findClientById(long id);

    ClientDTO createClient(ClientDTO clientDTO);

    ClientDTO updateClient(ClientDTO clientDTO);

    Optional<ClientDTO> findClientByEmail(String email);

    Optional<ClientDTO> findClientByUsername(String username);
}
