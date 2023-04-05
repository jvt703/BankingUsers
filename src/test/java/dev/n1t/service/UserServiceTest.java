package dev.n1t.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import dev.n1t.dto.UserDTO;
import dev.n1t.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void contextLoads() {
        assertNotNull(userService);
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
    public void test_deleteUserById() {
        User createdUser = userService.createUser(new User());
        assertNotNull(userService.readUserById(createdUser.getId()));
        userService.deleteUserById(createdUser.getId());
        assertNull(userService.readUserById(createdUser.getId()));
    }

    @Test
    public void test_createUserDTO() {
        UserDTO newUser = new UserDTO(1, "John", "Smith",
                "John.Smith@email.com", "Password");
        UserDTO serviceUserDTO = userService.createUserDTO(newUser);
        assertEquals(newUser.toString(), serviceUserDTO.toString());
    }

    @Test
    public void test_readUserByIdDTO() {
        UserDTO userDTO = userService.createUserDTO(new UserDTO(0, "", "", "", ""));
        assertNotNull(userService.readUserByIdDTO(userDTO.id()));
    }

    @Test
    public void test_updateUserDTO() {
        UserDTO createdUserDTO = userService.createUserDTO(
                new UserDTO(0, "", "", "", ""));
        UserDTO serviceUserDTO = userService.readUserByIdDTO(createdUserDTO.id());
        UserDTO editedServiceUserDTO = new UserDTO(
                serviceUserDTO.id(),
                "Jane",
                serviceUserDTO.lastname(),
                serviceUserDTO.email(),
                serviceUserDTO.password()
        );
        UserDTO updatedUserDTO = userService.updateUserDTO(editedServiceUserDTO);
        UserDTO latestUserDTO = userService.readUserByIdDTO(updatedUserDTO.id());
        assertEquals(editedServiceUserDTO.toString(), latestUserDTO.toString());
    }
}
