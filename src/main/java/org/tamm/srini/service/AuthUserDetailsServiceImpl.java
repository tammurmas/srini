package org.tamm.srini.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tamm.srini.model.AuthUser;
import org.tamm.srini.repository.AuthUserRepository;
import org.tamm.srini.service.dto.AuthUserDetails;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<AuthUser> optionalUser = authUserRepository.findOneByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new AuthUserDetails(optionalUser.get());
    }
}
