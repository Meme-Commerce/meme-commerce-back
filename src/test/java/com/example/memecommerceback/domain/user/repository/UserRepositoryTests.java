package com.example.memecommerceback.domain.user.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void saveUser_thenFindById_returnsSavedUser() {
        User user = new User();
        user.setUsername("alice");
        user.setEmail("alice@example.com");
        User saved = userRepository.save(user);

        User found = userRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("alice");
        assertThat(found.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void findByUsername_existingUsername_returnsUser() {
        User user = new User();
        user.setUsername("bob");
        user.setEmail("bob@example.com");
        entityManager.persistAndFlush(user);

        assertThat(userRepository.findByUsername("bob"))
            .isPresent()
            .get().satisfies(u -> assertThat(u.getUsername()).isEqualTo("bob"));
    }

    @Test
    void findByUsername_nonexistentUsername_returnsEmpty() {
        assertThat(userRepository.findByUsername("noone")).isEmpty();
    }

    @Test
    void existsByEmail_existingEmail_returnsTrue() {
        User user = new User();
        user.setUsername("carol");
        user.setEmail("carol@example.com");
        entityManager.persistAndFlush(user);

        assertThat(userRepository.existsByEmail("carol@example.com")).isTrue();
    }

    @Test
    void existsByEmail_nonexistentEmail_returnsFalse() {
        assertThat(userRepository.existsByEmail("ghost@example.com")).isFalse();
    }

    @Test
    void deleteById_existingUser_removesUser() {
        User user = new User();
        user.setUsername("dave");
        user.setEmail("dave@example.com");
        User persisted = entityManager.persistAndFlush(user);

        userRepository.deleteById(persisted.getId());
        assertThat(userRepository.findById(persisted.getId())).isEmpty();
    }

    @Test
    void saveUser_withoutUsername_throwsException() {
        User user = new User();
        user.setEmail("no_username@example.com");
        assertThatThrownBy(() -> userRepository.save(user))
            .isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
    }
}