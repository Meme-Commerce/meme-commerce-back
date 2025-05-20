package com.example.memecommerceback.global.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.memecommerceback.global.security.UserDetailsImpl;
import com.example.memecommerceback.global.model.User;
import com.example.memecommerceback.global.model.Role;
import com.example.memecommerceback.global.model.ERole;

import java.util.List;
import java.util.Set;

class UserDetailsImplTests {

    @Test
    void shouldReturnCorrectUserDetailsFields() {
        List<SimpleGrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_USER"),
            new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testUser", "testPass", authorities);

        assertEquals(1L, userDetails.getId());
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPass", userDetails.getPassword());
        assertIterableEquals(authorities, userDetails.getAuthorities());
    }

    @Test
    void shouldAlwaysReturnTrueForAccountStatusChecks() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "u", "p", List.of());

        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void equalsShouldBeBasedOnId() {
        UserDetailsImpl u1 = new UserDetailsImpl(1L, "u1", "p1", List.of());
        UserDetailsImpl u2 = new UserDetailsImpl(1L, "u2", "p2", List.of());
        UserDetailsImpl u3 = new UserDetailsImpl(2L, "u3", "p3", List.of());

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertNotEquals(u1, u3);
        assertNotEquals(u1.hashCode(), u3.hashCode());
        assertNotEquals(u1, null);
        assertNotEquals(u1, "some string");
    }

    @Test
    void buildShouldMapUserPropertiesAndAuthorities() {
        User user = new User();
        user.setId(5L);
        user.setUsername("buildUser");
        user.setPassword("buildPass");

        Role role = new Role();
        role.setName(ERole.ROLE_MODERATOR);
        user.setRoles(Set.of(role));

        UserDetailsImpl built = UserDetailsImpl.build(user);

        assertEquals(user.getId(), built.getId());
        assertEquals(user.getUsername(), built.getUsername());
        assertEquals(user.getPassword(), built.getPassword());
        assertTrue(built.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MODERATOR")));
    }

    @Test
    void buildShouldHandleEmptyRoles() {
        User user = new User();
        user.setId(6L);
        user.setUsername("emptyRolesUser");
        user.setPassword("emptyPass");
        user.setRoles(Set.of());

        UserDetailsImpl built = UserDetailsImpl.build(user);
        assertTrue(built.getAuthorities().isEmpty());
    }

    @Test
    void buildShouldThrowWhenUserIsNull() {
        assertThrows(NullPointerException.class, () -> UserDetailsImpl.build(null));
    }

    @Test
    void buildShouldThrowWhenUserRolesIsNull() {
        User user = new User();
        user.setId(7L);
        user.setUsername("nullRolesUser");
        user.setPassword("nullRolesPass");
        user.setRoles(null);

        assertThrows(NullPointerException.class, () -> UserDetailsImpl.build(user));
    }
}