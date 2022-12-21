package org.tamm.srini.service;

import java.util.List;
import java.util.Optional;

import org.tamm.srini.model.Client;
import org.tamm.srini.service.dto.ClientDTO;

public interface ClientService {

    List<ClientDTO> findAllUserClients();

    ClientDTO findUserById(long id);

    Client createClient(ClientDTO clientDTO);

    Client updateClient(ClientDTO clientDTO);

    Optional<Client> findClientByEmail(String email);

    Optional<Client> findClientByUserName(String userName);
}
