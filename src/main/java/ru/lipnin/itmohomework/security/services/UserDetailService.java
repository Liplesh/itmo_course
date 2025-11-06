package ru.lipnin.itmohomework.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.lipnin.itmohomework.security.repository.ApplicationUserRepository;
import ru.lipnin.itmohomework.security.entity.ApplicationUser;
import ru.lipnin.itmohomework.security.model.CustomUserDetails;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("ApplicationUser not found"));

        GrantedAuthority authority = new SimpleGrantedAuthority(
                applicationUser.getUserRole().getRoleType().name());

        return new CustomUserDetails(username, applicationUser.getPassword(), Set.of(authority), applicationUser.getEmail());
    }
}
