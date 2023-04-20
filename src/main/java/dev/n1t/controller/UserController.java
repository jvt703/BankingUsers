package dev.n1t.controller;

import dev.n1t.dto.UserDTO;
import dev.n1t.model.Address;
import dev.n1t.model.Role;
import dev.n1t.service.UserService;
import dev.n1t.util.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final EmailServiceImpl emailService;

    @Autowired
    public UserController(UserService userService, EmailServiceImpl emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        emailService.sendSimpleMessage(
                userDTO.email(),
                "Welcome to Nine Ten - Verify",
                "Welcome to Nine Ten " + userDTO.firstname() +
                        "! Please use this code to verify your email: 392887");
        return ResponseEntity.ok(userService.createUserDTO(userDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> readUserById(@PathVariable("id") int id) {
//        UserDTO userDTO = new UserDTO(1,
//                "Mario",
//                "Mario",
//                "mario.mario@email.com",
//                "password",
//                0L,
//                new Address(1L, "city", "state", "street", "zipCode"),
//                new Role(1L, "roleName"));
//        return ResponseEntity.ok(userDTO);
        UserDTO userDTO = userService.readUserByIdDTO(id);
        if (userDTO == null) {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> readAllUsers(@RequestParam Map<String, String> query) {
        return ResponseEntity.ok(userService.readAllUsersDTO(query));
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO userDTO1 = userService.updateUserDTO(userDTO);
        if (userDTO1 == null) {return new ResponseEntity<>(HttpStatus.BAD_REQUEST);}
        return ResponseEntity.ok(userDTO1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUserById(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
