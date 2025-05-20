package com.example.memecommerceback.global.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.example.memecommerceback.user.User;
import com.example.memecommerceback.user.Role;
import org.springframework.security.core.GrantedAuthority;

public class UserDetailsImplTests {

    private User createUser(Long id, String username, String password, String... roles) {
        User u = new User();
        u.setId(id);
        u.setUsername(username);
        u.setPassword(password);
        for (String roleName : roles) {
            Role r = new Role();
            r.setName(roleName);
            u.getRoles().add(r);
        }
        return u;
    }

    @Test
    @DisplayName("build() should map User fields to UserDetailsImpl correctly")
    void buildShouldMapFieldsCorrectly() {
        User user = createUser(42L, "alice", "secret", "ROLE_USER", "ROLE_ADMIN");
        UserDetailsImpl details = UserDetailsImpl.build(user);

        assertThat(details.getId()).isEqualTo(42L);
        assertThat(details.getUsername()).isEqualTo("alice");
        assertThat(details.getPassword()).isEqualTo("secret");
        assertThat(details.getAuthorities())
            .extracting(GrantedAuthority::getAuthority)
            .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    @DisplayName("build() should produce empty authorities when user has no roles")
    void buildShouldHandleEmptyRoles() {
        User user = createUser(1L, "bob", "pwd");
        UserDetailsImpl details = UserDetailsImpl.build(user);

        assertThat(details.getAuthorities()).isEmpty();
    }

    @Test
    @DisplayName("build() should throw NullPointerException when user is null")
    void buildShouldThrowWhenUserIsNull() {
        assertThatThrownBy(() -> UserDetailsImpl.build(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("account flags should all be true by default")
    void accountFlagsShouldBeTrue() {
        UserDetailsImpl details = UserDetailsImpl.build(createUser(2L, "carol", "pwd"));
        assertThat(details.isAccountNonExpired()).isTrue();
        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isCredentialsNonExpired()).isTrue();
        assertThat(details.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("equals() and hashCode() should consider only the id field")
    void equalsAndHashCodeBasedOnId() {
        User u1 = createUser(100L, "x", "p");
        User u2 = createUser(100L, "y", "q");
        User u3 = createUser(101L, "x", "p");

        UserDetailsImpl d1 = UserDetailsImpl.build(u1);
        UserDetailsImpl d2 = UserDetailsImpl.build(u2);
        UserDetailsImpl d3 = UserDetailsImpl.build(u3);

        assertThat(d1).isEqualTo(d2);
        assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
        assertThat(d1).isNotEqualTo(d3);
    }

    @Test
    @DisplayName("equals() should return false when comparing to null or different types")
    void equalsWithNullOrDifferentType() {
        UserDetailsImpl details = UserDetailsImpl.build(createUser(200L, "dave", "pwd"));
        assertThat(details).isNotEqualTo(null);
        assertThat(details).isNotEqualTo("some string");
    }
}