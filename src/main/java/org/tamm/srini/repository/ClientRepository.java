package org.tamm.srini.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tamm.srini.model.AuthUser;
import org.tamm.srini.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllByAuthUser(AuthUser user);

    Optional<Client> findOneByIdAndAuthUser(Long id, AuthUser authUser);

    Optional<Client> findOneByEmailIgnoreCase(String email);

    Optional<Client> findOneByUsernameIgnoreCase(String username);
}
