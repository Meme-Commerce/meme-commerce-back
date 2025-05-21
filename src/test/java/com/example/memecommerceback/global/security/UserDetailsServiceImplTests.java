/*
 * Unit tests for UserDetailsServiceImpl
 * Testing framework: JUnit Jupiter (JUnit 5)
 * Mocking framework: Mockito
 */
package com.example.memecommerceback.global.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.memecommerceback.global.security.UserDetailsServiceImpl;
import com.example.memecommerceback.global.security.UserRepository;
import com.example.memecommerceback.global.security.User;
import com.example.memecommerceback.global.security.Role;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        // MockitoExtension automatically initializes mocks
    }

    @Test
    @DisplayName("loadUserByUsername returns UserDetails when user exists with one role")
    void loadUserByUsername_UserExistsSingleRole_ReturnsUserDetails() {
        // Arrange
        Role role = new Role("ROLE_USER");
        User user = new User("john", "password123", List.of(role));
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("john");

        // Assert
        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        List<String> authorities = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains("ROLE_USER"));
    }

    @Test
    @DisplayName("loadUserByUsername throws UsernameNotFoundException when user does not exist")
    void loadUserByUsername_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
            UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername("unknown"),
            "Expected UsernameNotFoundException for non-existent user"
        );
    }

    @Test
    @DisplayName("loadUserByUsername returns empty authorities when user has no roles")
    void loadUserByUsername_UserWithNoRoles_ReturnsEmptyAuthorities() {
        // Arrange
        User user = new User("jane", "pass", Collections.emptyList());
        when(userRepository.findByUsername("jane")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("jane");

        // Assert
        assertNotNull(userDetails.getAuthorities());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    @DisplayName("loadUserByUsername maps multiple roles to authorities")
    void loadUserByUsername_UserWithMultipleRoles_ReturnsAllAuthorities() {
        // Arrange
        Role r1 = new Role("ROLE_USER");
        Role r2 = new Role("ROLE_ADMIN");
        User user = new User("alice", "secret", Arrays.asList(r1, r2));
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("alice");

        // Assert
        List<String> authorities = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        assertEquals(2, authorities.size());
        assertTrue(authorities.containsAll(List.of("ROLE_USER", "ROLE_ADMIN")));
    }

    @Test
    @DisplayName("loadUserByUsername throws when username is null")
    void loadUserByUsername_NullUsername_ThrowsException() {
        assertThrows(
            UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername(null),
            "Expected exception for null username"
        );
    }

    @Test
    @DisplayName("loadUserByUsername throws when username is blank")
    void loadUserByUsername_BlankUsername_ThrowsException() {
        when(userRepository.findByUsername("")).thenReturn(Optional.empty());
        assertThrows(
            UsernameNotFoundException.class,
            () -> userDetailsService.loadUserByUsername(""),
            "Expected exception for blank username"
        );
    }
}