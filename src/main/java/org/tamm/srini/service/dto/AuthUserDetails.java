package org.tamm.srini.service.dto;

import java.io.Serial;
import java.util.Collection;

import lombok.AllArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.tamm.srini.model.AuthUser;

@AllArgsConstructor
public class AuthUserDetails implements UserDetails {

    @Serial private static final long serialVersionUID = 3402042802396868026L;

    private AuthUser authUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
