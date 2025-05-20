package com.example.memecommerceback.global.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.memecommerceback.global.security.UserDetailsServiceImpl;
import com.example.memecommerceback.global.repository.UserRepository;
import com.example.memecommerceback.global.model.User;
import com.example.memecommerceback.global.model.Role;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        reset(userRepository);
    }

    @Test
    void loadUserByUsername_whenUserExists_returnsUserDetails() {
        // Arrange
        User domainUser = new User("john", "encodedPassword", Set.of(new Role("ROLE_USER")));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(domainUser));

        // Act
        UserDetails details = userDetailsService.loadUserByUsername("john");

        // Assert
        assertEquals("john", details.getUsername());
        assertEquals("encodedPassword", details.getPassword());
        assertTrue(details.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        verify(userRepository).findByUsername("john");
    }

    @Test
    void loadUserByUsername_whenUserNotFound_throwsException() {
        // Arrange
        when(userRepository.findByUsername("jane")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername("jane")
        );
        verify(userRepository).findByUsername("jane");
    }

    @Test
    void loadUserByUsername_whenMultipleRoles_mapsAllAuthorities() {
        // Arrange
        User domainUser = new User("alice", "pw", Set.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN")));
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(domainUser));

        // Act
        UserDetails details = userDetailsService.loadUserByUsername("alice");

        // Assert
        Set<String> auths = details.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .collect(Collectors.toSet());
        assertEquals(Set.of("ROLE_USER", "ROLE_ADMIN"), auths);
        verify(userRepository).findByUsername("alice");
    }

    @Test
    void loadUserByUsername_whenUserDisabled_throwsException() {
        // Arrange
        User disabledUser = new User("bob", "pw", Set.of(new Role("ROLE_USER")));
        disabledUser.setEnabled(false);
        when(userRepository.findByUsername("bob")).thenReturn(Optional.of(disabledUser));

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername("bob")
        );
        verify(userRepository).findByUsername("bob");
    }
}