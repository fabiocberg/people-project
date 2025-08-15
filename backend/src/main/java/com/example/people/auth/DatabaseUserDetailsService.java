package com.example.people.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final UserAccountRepository repo;
    public DatabaseUserDetailsService(UserAccountRepository repo) { this.repo = repo; }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount acc = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return User.withUsername(acc.getUsername()).password(acc.getPassword()).roles("ADMIN").disabled(!acc.isEnabled()).build();
    }
}
