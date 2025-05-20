package com.example.memecommerceback.domain.user.repository;

import com.example.memecommerceback.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("When saving a valid User, then an ID is generated and fields match")
    void whenSaveValidUser_thenIdIsGeneratedAndFieldsMatch() {
        User u = new User();
        u.setUsername("testuser");
        u.setEmail("test@example.com");
        u.setPassword("password");
        User saved = userRepository.save(u);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("testuser");
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Given existing User, when findByEmail, then returns user")
    void givenExistingUser_whenFindByEmail_thenFound() {
        User u = new User(null, "jane", "jane@example.com", "pw");
        userRepository.save(u);

        Optional<User> opt = userRepository.findByEmail("jane@example.com");
        assertThat(opt).isPresent();
        assertThat(opt.get().getUsername()).isEqualTo("jane");
    }

    @Test
    @DisplayName("When findByEmail for unknown email, then empty")
    void whenFindByEmailUnknown_thenEmpty() {
        Optional<User> opt = userRepository.findByEmail("noone@nowhere.com");
        assertThat(opt).isNotPresent();
    }

    @Test
    @DisplayName("Given saved User, existsByUsername returns true")
    void givenSavedUser_whenExistsByUsername_thenTrue() {
        userRepository.save(new User(null, "sam", "sam@example.com", "pw"));
        assertThat(userRepository.existsByUsername("sam")).isTrue();
    }

    @Test
    @DisplayName("When existsByUsername for unknown, then false")
    void whenExistsByUsernameUnknown_thenFalse() {
        assertThat(userRepository.existsByUsername("ghost")).isFalse();
    }

    @Test
    @DisplayName("When repository is empty, findAll returns empty list")
    void whenEmptyRepository_thenFindAllEmpty() {
        List<User> list = userRepository.findAll();
        assertThat(list).isEmpty();
    }

    @Test
    @DisplayName("After saving users, findAll returns correct number of users")
    void afterSavingUsers_findAllReturnsAll() {
        userRepository.saveAll(List.of(
            new User(null, "a", "a@x.com", "pw"),
            new User(null, "b", "b@x.com", "pw")
        ));
        List<User> list = userRepository.findAll();
        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("Given saved user, findById returns user")
    void givenSavedUser_whenFindById_thenFound() {
        User saved = userRepository.save(new User(null, "findme", "f@x.com", "pw"));
        Optional<User> opt = userRepository.findById(saved.getId());
        assertThat(opt).isPresent();
        assertThat(opt.get().getUsername()).isEqualTo("findme");
    }

    @Test
    @DisplayName("When findById for unknown ID, then empty")
    void whenFindByIdUnknown_thenEmpty() {
        assertThat(userRepository.findById(999L)).isNotPresent();
    }

    @Test
    @DisplayName("Given saved User, when updating email, then change is persisted")
    void givenSavedUser_whenUpdateEmail_thenPersisted() {
        User saved = userRepository.save(new User(null, "up", "old@x.com", "pw"));
        saved.setEmail("new@x.com");
        userRepository.save(saved);

        Optional<User> opt = userRepository.findById(saved.getId());
        assertThat(opt).isPresent();
        assertThat(opt.get().getEmail()).isEqualTo("new@x.com");
    }

    @Test
    @DisplayName("Given saved User, when deleteById, then entity is removed")
    void givenSavedUser_whenDeleteById_thenGone() {
        User saved = userRepository.save(new User(null, "del", "del@x.com", "pw"));
        Long id = saved.getId();
        userRepository.deleteById(id);
        assertThat(userRepository.findById(id)).isNotPresent();
    }

    @Test
    @DisplayName("When saving two users with same email, then DataIntegrityViolationException")
    void whenSaveDuplicateEmail_thenException() {
        userRepository.saveAndFlush(new User(null, "u1", "dup@x.com", "pw"));
        assertThrows(DataIntegrityViolationException.class, () ->
            userRepository.saveAndFlush(new User(null, "u2", "dup@x.com", "pw"))
        );
    }

    @Test
    @DisplayName("When saving two users with same username, then DataIntegrityViolationException")
    void whenSaveDuplicateUsername_thenException() {
        userRepository.saveAndFlush(new User(null, "dupuser", "u@x.com", "pw"));
        assertThrows(DataIntegrityViolationException.class, () ->
            userRepository.saveAndFlush(new User(null, "dupuser", "v@x.com", "pw"))
        );
    }

    @Test
    @DisplayName("When saving null User, then IllegalArgumentException")
    void whenSaveNullUser_thenIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.save(null));
    }
}