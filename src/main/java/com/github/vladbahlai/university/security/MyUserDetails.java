package com.github.vladbahlai.university.security;

import com.github.vladbahlai.university.model.Privilege;
import com.github.vladbahlai.university.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MyUserDetails implements UserDetails {

    private final User user;
    private final Set<Privilege> privileges;

    public MyUserDetails(User user, Set<Privilege> privileges) {
        this.user = user;
        this.privileges = privileges;
    }

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthority(privileges);
    }

    @Override
    public String getPassword() {
        return this.user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return this.user.getName();
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

    public User getUser() {
        return this.user;
    }

    private Set<GrantedAuthority> getGrantedAuthority(Set<Privilege> privileges) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Privilege privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege.getName()));
        }
        return authorities;
    }
}
