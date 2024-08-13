package tech.shopgi.authms.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tech.shopgi.authms.model.User;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_FindByUsername_ReturnSavedUser() {
        User user = new User("username", "pass", List.of("ROLE_USER"));

        var savedUser = userRepository.save(user);
        var fetchedUser = userRepository.findByUsername("username");

        Assertions.assertEquals(savedUser.getUsername(), fetchedUser.getUsername());
        Assertions.assertEquals(savedUser.getPassword(), fetchedUser.getPassword());
        Assertions.assertEquals(savedUser.getAuthorities(), fetchedUser.getAuthorities());
    }
}