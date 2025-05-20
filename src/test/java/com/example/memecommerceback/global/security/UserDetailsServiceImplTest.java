package com.example.memecommerceback.global.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.memecommerceback.global.security.UserDetailsServiceImpl;
import com.example.memecommerceback.global.security.UserPrincipal;
import com.example.memecommerceback.user.User;
import com.example.memecommerceback.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        clearInvocations(userRepository);
    }

    @Nested
    @DisplayName("loadUserByUsername - Successful Cases")
    class LoadUserByUsernameSuccess {

        @Test
        @DisplayName("returns UserPrincipal when user exists")
        void returnsUserPrincipalWhenUserExists() {
            // Arrange
            String username = "alice";
            User mockUser = new User();
            mockUser.setUsername(username);
            mockUser.setPassword("securePass");
            mockUser.setActive(true);
            when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

            // Act
            UserPrincipal principal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

            // Assert
            assertNotNull(principal);
            assertEquals(username, principal.getUsername());
            assertEquals("securePass", principal.getPassword());
            assertTrue(principal.isEnabled());
            verify(userRepository, times(1)).findByUsername(username);
        }
    }

    @Nested
    @DisplayName("loadUserByUsername - Failure and Edge Cases")
    class LoadUserByUsernameFailure {

        @Test
        @DisplayName("throws UsernameNotFoundException when user not found")
        void throwsWhenUserNotFound() {
            // Arrange
            String username = "unknown";
            when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username));
            verify(userRepository, times(1)).findByUsername(username);
        }

        @Test
        @DisplayName("throws IllegalArgumentException when username is null")
        void throwsWhenUsernameNull() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                () -> userDetailsService.loadUserByUsername(null));
            verify(userRepository, never()).findByUsername(any());
        }

        @Test
        @DisplayName("throws IllegalArgumentException when username is empty")
        void throwsWhenUsernameEmpty() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                () -> userDetailsService.loadUserByUsername(""));
            verify(userRepository, never()).findByUsername(any());
        }
    }
}