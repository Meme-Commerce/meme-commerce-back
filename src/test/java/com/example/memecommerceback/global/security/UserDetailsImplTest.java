package com.example.memecommerceback.global.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.example.memecommerceback.global.security.UserDetailsImpl;
import com.example.memecommerceback.user.User;
import com.example.memecommerceback.user.Role;
import com.example.memecommerceback.user.enums.RoleName;
import java.util.Collections;
import java.util.List;

public class UserDetailsImplTest {

    @Test
    public void whenBuildUserDetails_thenFieldsPopulatedCorrectly() {
        // Given
        Role role = new Role();
        role.setName(RoleName.ROLE_USER);
        User user = new User();
        user.setId(42L);
        user.setUsername("alice");
        user.setEmail("alice@example.com");
        user.setPassword("secret");
        user.setRoles(Collections.singleton(role));

        // When
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        // Then
        assertEquals(42L, userDetails.getId());
        assertEquals("alice", userDetails.getUsername());
        assertEquals("alice@example.com", userDetails.getEmail());
        assertEquals("secret", userDetails.getPassword());
        @SuppressWarnings("unchecked")
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) userDetails.getAuthorities();
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testAccountFlags_areTrueByDefault() {
        UserDetailsImpl userDetails =
            new UserDetailsImpl(1L, "bob", "bob@example.com", "pwd", Collections.emptyList());

        assertTrue(userDetails.isAccountNonExpired(), "account should be non-expired by default");
        assertTrue(userDetails.isAccountNonLocked(), "account should be non-locked by default");
        assertTrue(userDetails.isCredentialsNonExpired(), "credentials should be non-expired by default");
        assertTrue(userDetails.isEnabled(), "account should be enabled by default");
    }

    @Test
    public void givenNoRoles_thenAuthoritiesEmpty() {
        UserDetailsImpl userDetails =
            new UserDetailsImpl(2L, "charlie", "charlie@example.com", "pwd2", Collections.emptyList());

        assertTrue(userDetails.getAuthorities().isEmpty(), "authorities should be empty when user has no roles");
    }

    @Test
    public void testEqualsAndHashCode() {
        UserDetailsImpl userA =
            new UserDetailsImpl(100L, "userA", "a@example.com", "passA", Collections.emptyList());
        UserDetailsImpl userB =
            new UserDetailsImpl(100L, "userB", "b@example.com", "passB", Collections.emptyList());
        UserDetailsImpl userC =
            new UserDetailsImpl(200L, "userC", "c@example.com", "passC", Collections.emptyList());

        // Same id => equal, irrespective of other fields
        assertEquals(userA, userB);
        assertEquals(userA.hashCode(), userB.hashCode());

        // Different id => not equal
        assertNotEquals(userA, userC);
    }
}