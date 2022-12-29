package org.tamm.srini.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tamm.srini.model.AuthUser;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findOneByUsername(String username);

}
