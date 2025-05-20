package com.example.memecommerceback.domain.user.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class UsersTests {

    private Users defaultUser;

    @BeforeEach
    void setUp() {
        defaultUser = new Users(1L, "john_doe", "john@example.com", "password123");
    }

    @Test
    void constructor_and_getters_should_initialize_all_fields() {
        assertEquals(1L, defaultUser.getId());
        assertEquals("john_doe", defaultUser.getUsername());
        assertEquals("john@example.com", defaultUser.getEmail());
        assertEquals("password123", defaultUser.getPassword());
    }

    @Test
    void setters_should_update_fields() {
        defaultUser.setUsername("jane_doe");
        assertEquals("jane_doe", defaultUser.getUsername());

        defaultUser.setEmail("jane@example.com");
        assertEquals("jane@example.com", defaultUser.getEmail());

        defaultUser.setPassword("newpass");
        assertEquals("newpass", defaultUser.getPassword());
    }

    @Test
    void equals_and_hashCode_should_be_consistent_for_equal_objects() {
        Users u1 = new Users(2L, "alice", "alice@example.com", "pwd");
        Users u2 = new Users(2L, "alice", "alice@example.com", "pwd");
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void equals_should_return_false_for_different_ids() {
        Users u1 = new Users(3L, "bob", "bob@example.com", "pwd");
        Users u2 = new Users(4L, "bob", "bob@example.com", "pwd");
        assertNotEquals(u1, u2);
    }

    @Test
    void toString_should_contain_username_and_email() {
        String repr = defaultUser.toString();
        assertTrue(repr.contains("john_doe"));
        assertTrue(repr.contains("john@example.com"));
    }

    @Test
    void constructor_should_throw_when_email_is_null() {
        // Ensure null email is not allowed
        assertThrows(NullPointerException.class,
            () -> new Users(5L, "charlie", null, "pwd"));
    }
}