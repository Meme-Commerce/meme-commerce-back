package com.example.memecommerceback.domain.user.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class UsersTest {

    @Test
    @DisplayName("Constructor and getters initialize fields correctly")
    void testConstructorAndGetters() {
        Users user = new Users(1L, "alice", "alice@example.com", "p@ssw0rd");
        assertEquals(1L, user.getId());
        assertEquals("alice", user.getUsername());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("p@ssw0rd", user.getPassword());
    }

    @Test
    @DisplayName("equals returns true for same object")
    void equals_sameObject() {
        Users user = new Users(2L, "bob", "bob@example.com", "pw");
        assertTrue(user.equals(user));
    }

    @Test
    @DisplayName("equals returns true for two users with identical fields")
    void equals_identicalUsers() {
        Users u1 = new Users(3L, "carol", "carol@example.com", "pw");
        Users u2 = new Users(3L, "carol", "carol@example.com", "pw");
        assertTrue(u1.equals(u2));
    }

    @Test
    @DisplayName("equals returns false when comparing to null")
    void equals_null() {
        Users user = new Users(4L, "dave", "dave@example.com", "pw");
        assertFalse(user.equals(null));
    }

    @Test
    @DisplayName("equals returns false when comparing to different class")
    void equals_differentClass() {
        Users user = new Users(5L, "eve", "eve@example.com", "pw");
        Object other = new Object();
        assertFalse(user.equals(other));
    }

    @Test
    @DisplayName("equals returns false for users with different id")
    void equals_differentId() {
        Users u1 = new Users(6L, "frank", "frank@example.com", "pw");
        Users u2 = new Users(7L, "frank", "frank@example.com", "pw");
        assertFalse(u1.equals(u2));
    }

    @Test
    @DisplayName("hashCode is consistent across calls")
    void hashCode_consistency() {
        Users user = new Users(8L, "grace", "grace@example.com", "pw");
        int first = user.hashCode();
        int second = user.hashCode();
        assertEquals(first, second);
    }

    @Test
    @DisplayName("equal objects have equal hashCodes")
    void hashCode_equalsContract() {
        Users u1 = new Users(9L, "henry", "henry@example.com", "pw");
        Users u2 = new Users(9L, "henry", "henry@example.com", "pw");
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    @DisplayName("toString contains class name and field values")
    void toString_containsFields() {
        Users user = new Users(10L, "irene", "irene@example.com", "pw");
        String repr = user.toString();
        assertTrue(repr.contains("Users"));
        assertTrue(repr.contains("10"));
        assertTrue(repr.contains("irene"));
        assertTrue(repr.contains("irene@example.com"));
    }

    @Test
    @DisplayName("No-arg constructor sets default values")
    void defaultConstructor_defaults() {
        Users user = new Users();
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
    }
}