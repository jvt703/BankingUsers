package dev.n1t.controller;

import dev.n1t.dto.UserDTO;
import dev.n1t.model.User;
import dev.n1t.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUserDTO(userDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> readUserById(@PathVariable("id") int id) {
        UserDTO userDTO = userService.readUserByIdDTO(id);
        if (userDTO == null) {return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);}
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> readAllUsers() {
        return ResponseEntity.ok(userService.readAllUsersDTO());
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO userDTO1 = userService.updateUserDTO(userDTO);
        if (userDTO1 == null) {return new ResponseEntity<UserDTO>(HttpStatus.BAD_REQUEST);}
        return ResponseEntity.ok(userDTO1);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("id") int id) {
        if (userService.deleteUser(id)) {
            return new ResponseEntity<UserDTO>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
    }
}
