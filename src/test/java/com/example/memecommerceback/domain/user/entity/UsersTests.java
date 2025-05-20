package com.example.memecommerceback.domain.user.entity;

/*
 * Testing framework: JUnit 5 (JUnit Jupiter)
 * Assertion library: AssertJ
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UsersTests {

    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setId(1L);
        user.setUsername("alice");
        user.setEmail("alice@example.com");
    }

    @Test
    void getId_ReturnsIdSetInSetup() {
        assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    void getUsername_ReturnsUsernameSetInSetup() {
        assertThat(user.getUsername()).isEqualTo("alice");
    }

    @Test
    void getEmail_ReturnsEmailSetInSetup() {
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void setId_UpdatesId() {
        user.setId(2L);
        assertThat(user.getId()).isEqualTo(2L);
    }

    @Test
    void setUsername_UpdatesUsername() {
        user.setUsername("bob");
        assertThat(user.getUsername()).isEqualTo("bob");
    }

    @Test
    void setEmail_UpdatesEmail() {
        user.setEmail("bob@example.com");
        assertThat(user.getEmail()).isEqualTo("bob@example.com");
    }

    @Test
    void equalsAndHashCode_WithSameFieldValues_IsConsistent() {
        Users other = new Users();
        other.setId(1L);
        other.setUsername("alice");
        other.setEmail("alice@example.com");
        assertThat(other).isEqualTo(user);
        assertThat(other.hashCode()).isEqualTo(user.hashCode());
    }

    @Test
    void toString_IncludesKeyFields() {
        String s = user.toString();
        assertThat(s).contains("id=1", "username='alice'", "email='alice@example.com'");
    }

    @Test
    void setUsername_Null_ThrowsNullPointerException() {
        assertThatThrownBy(() -> user.setUsername(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void setEmail_Null_ThrowsNullPointerException() {
        assertThatThrownBy(() -> user.setEmail(null))
            .isInstanceOf(NullPointerException.class);
    }
}