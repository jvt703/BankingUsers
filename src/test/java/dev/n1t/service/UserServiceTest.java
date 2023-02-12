package dev.n1t.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.n1t.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    private UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void contextLoads() {
        assertThat(userService).isNotNull();
    }

    @Test
    public void test_createUser() {
        User newUser = new User();
        newUser.setId(1);
        newUser.setFirstname("John");
        newUser.setLastname("Smith");
        newUser.setEmail("John.Smith@email.com");
        newUser.setPassword("password");
        User serviceUser = userService.createUser(newUser);
        assertEquals(newUser.toString(), serviceUser.toString());
    }

    @Test
    public void test_readUserById() {
        User serviceUser = userService.createUser(new User());
        assertNotNull(userService.readUserById(serviceUser.getId()));
    }

    @Test
    public void test_updateUser() {
        User createdUser = userService.createUser(new User());
        User serviceUser = userService.readUserById(createdUser.getId());
        serviceUser.setFirstname("Jane");
        User updatedUser = userService.updateUser(serviceUser);
        User latestUser = userService.readUserById(updatedUser.getId());
        assertEquals(serviceUser.toString(), latestUser.toString());
    }

    @Test
    public void test_deleteUser() {
        User createdUser = userService.createUser(new User());
        assertNotNull(userService.readUserById(createdUser.getId()));
        userService.deleteUser(createdUser.getId());
        assertThrows(RuntimeException.class, () -> {userService.readUserById(createdUser.getId());});
    }
}
