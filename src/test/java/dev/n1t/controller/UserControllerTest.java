package dev.n1t.controller;

import dev.n1t.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;

    @Autowired
    public UserControllerTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    public void contextLoads() {
        assertNotNull(restTemplate);
    }

    @Test
    public void test_createUser() {
        String url = ("http://localhost:" + port + "/user");
        UserDTO userDTO = new UserDTO(0, "First", "Last", "Email", "Password");
        UserDTO postReturn = restTemplate.postForObject(url, userDTO, UserDTO.class);
        assertEquals(userDTO.firstname(), postReturn.firstname());
    }

    @Test
    public void test_readUserById() {
        String url = ("http://localhost:" + port + "/user");
        UserDTO userDTO = new UserDTO(1, "First", "Last", "Email", "Password");
        UserDTO postReturn = restTemplate.postForObject(url, userDTO, UserDTO.class);
        assertNotNull(restTemplate.getForObject(url + "/" + postReturn.id(), UserDTO.class));
    }

    @Test
    public void test_updateUser() {
        String url = ("http://localhost:" + port + "/user");
        UserDTO userDTO = new UserDTO(1, "First", "Last", "Email", "Password");
        UserDTO postReturn = restTemplate.postForObject(url, userDTO, UserDTO.class);
        UserDTO updatedUserDTO = new UserDTO(
                postReturn.id(),
                "Jane",
                postReturn.lastname(),
                postReturn.email(),
                postReturn.password());
        restTemplate.put(url, updatedUserDTO);
        UserDTO updatedGetReturn = restTemplate.getForObject(url + "/" + postReturn.id(), UserDTO.class);
        assertEquals(updatedUserDTO.toString(), updatedGetReturn.toString());
    }

    @Test
    public void test_deleteUserById() {
        String url = ("http://localhost:" + port + "/user");
        UserDTO userDTO = new UserDTO(1, "First", "Last", "Email", "Password");
        UserDTO postReturn = restTemplate.postForObject(url, userDTO, UserDTO.class);
        restTemplate.delete(url + "/" + postReturn.id());
        assertNull(restTemplate.getForObject(url + "/" + postReturn.id(), UserDTO.class));
    }
}
