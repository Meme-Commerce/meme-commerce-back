package com.example.memecommerceback.domain.user.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUserById() {
        // given
        User user = new User();
        user.setUsername("alice");
        user.setEmail("alice@example.com");
        user.setPassword("secret");
        User saved = userRepository.save(user);

        // when
        Optional<User> found = userRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("alice");
        assertThat(found.get().getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void shouldFindByUsername() {
        // given
        User user = new User();
        user.setUsername("bob");
        user.setEmail("bob@example.com");
        user.setPassword("topsecret");
        userRepository.save(user);

        // when
        Optional<User> found = userRepository.findByUsername("bob");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("bob@example.com");
    }

    @Test
    void shouldReturnEmptyForNonexistentUsername() {
        // when
        Optional<User> found = userRepository.findByUsername("unknown");

        // then
        assertThat(found).isNotPresent();
    }

    @Test
    void shouldFindByEmail() {
        // given
        User user = new User();
        user.setUsername("carol");
        user.setEmail("carol@example.com");
        user.setPassword("pwd");
        userRepository.save(user);

        // when
        Optional<User> found = userRepository.findByEmail("carol@example.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("carol");
    }

    @Test
    void shouldReturnEmptyForNonexistentEmail() {
        // when
        Optional<User> found = userRepository.findByEmail("noone@example.com");

        // then
        assertThat(found).isNotPresent();
    }

    @Test
    void shouldReturnAllUsers() {
        // given
        userRepository.deleteAll();
        User u1 = new User();
        u1.setUsername("x");
        u1.setEmail("x@example.com");
        u1.setPassword("p1");
        User u2 = new User();
        u2.setUsername("y");
        u2.setEmail("y@example.com");
        u2.setPassword("p2");
        userRepository.saveAll(List.of(u1, u2));

        // when
        List<User> users = userRepository.findAll();

        // then
        assertThat(users).hasSize(2)
                         .extracting(User::getUsername)
                         .containsExactlyInAnyOrder("x", "y");
    }

    @Test
    void shouldThrowOnSaveWhenUsernameIsNull() {
        // given
        User user = new User();
        user.setEmail("null@example.com");
        user.setPassword("nopwd");

        // then
        assertThatThrownBy(() -> userRepository.saveAndFlush(user))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldThrowOnSaveWhenEmailIsNull() {
        // given
        User user = new User();
        user.setUsername("noemail");
        user.setPassword("nopwd");

        // then
        assertThatThrownBy(() -> userRepository.saveAndFlush(user))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}